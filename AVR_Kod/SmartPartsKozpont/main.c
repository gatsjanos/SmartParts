/*
* SmartPartsKozpont.c
*
* Created: 2016. 10. 16. 12:00:53
* Author : gatsj
*/

#define F_CPU 7372800

#define USART_BAUDRATE 57600  // soros kommunikacio sebessege: 57600 bps
#define UBRR_ERTEK ((F_CPU / (USART_BAUDRATE * 16UL)) - 1)  // UBRR

#define UART_TimeoutKovetkezoByteig 15 //ms
#define UART_KovByteVarakSzamlaloMaxertek (((double)UART_TimeoutKovetkezoByteig*(double)F_CPU)/((double)23040000)) //23040000 = 256*90*1000 (90-es osztó if-en belül + sec-bõl ms)
volatile short UART_KovByteVarakSzamlalo = 0;

///////TWI/////
volatile char TWICim = (50<<1);
#define SCL_CLOCK 10000
#define TWIprescaler_value 1
#define TWIBaudRate ((F_CPU/SCL_CLOCK)-16)/(2*TWIprescaler_value)
volatile char TWISlaveKockaCim = (60<<1);
volatile char TWISlaveCim;
#define TWI_IR 0
#define TWI_OLVAS 1

volatile short TWIKockaValaszSzamlalo = 31111;
#define TWIKockaValaszSzamlaloMaxido 270 //ms
#define TWIKockaValaszSzamlaloMaxertek (((double)TWIKockaValaszSzamlaloMaxido*(double)F_CPU)/((double)23040000)) //23040000 = 256*90*1000 (90-es osztó if-en belül + sec-bõl ms)
////////////////

///////TWI bufferek/////
volatile char TWIkuldendo[20];
volatile char TWIkuldendoHanyadik = 0, TWIkuldendoHossz = 0;
volatile char TWIvisszaolvasson = 0;

volatile char TWIbejovo[20];
volatile char TWIbejovoHossz = 0, TWIbejovoHanyadik = 0, TWIujbejovo = 0;
volatile char TWIkuldjvissza = 0;

volatile char twiRepstartolt = 0;
////////////////

#define RFKiSetALACSONY PORTD &= ~(1<<PD7);
#define RFKiSetMAGAS PORTD |= (1<<PD7);

#define RFBeMagase (PIND & (1<<PD6))

#define SmartPartsRFAzonosito 0xABCD
short RFSajatAzonosito = 0x001;

#include <avr/io.h>
#include <avr/interrupt.h>
#include <util/delay.h>
#include <avr/pgmspace.h>

#define Int0PergesIdokoz 200//ms
#define Int0PergesSzamlaloMaxertek (((double)Int0PergesIdokoz*(double)F_CPU)/((double)23040000))//23040000 = 256*90*1000 (90-es osztó if-en belül + sec-bõl ms)
volatile char Int0PergesSzamlalo = 0;

volatile char Gomb1 = 0;

volatile unsigned short RFbejovo[30];//Manchester kódolt byte-ok
volatile char RFbejovohossz = 255;//255: Nincs megadva érték
volatile short RFbejovoLepteto = 0;

volatile short RFBejovoMagasokSzama = 0;
volatile char RFBejovoStatusz = 0;//0: Semmi   1: Folyamatos magasban van   2: Fogadás folyamatban 3: Új üzenet fogadva
volatile char RFFogadasKozbeniTeszt = 0b00000000;//(1<<0): Preamble   (1<<1): SmartParts azonosító    (1<<2): Hossz
#define RFPreambTesztFlagBE RFFogadasKozbeniTeszt |= (1<<0);
#define RFPreambTesztFlagKI RFFogadasKozbeniTeszt &= ~(1<<0);
#define RFPreambTesztFlagGet (RFFogadasKozbeniTeszt & (1<<0))
#define RFSPAzonTesztFlagBE RFFogadasKozbeniTeszt |= (1<<1);
#define RFSPAzonTesztFlagKI RFFogadasKozbeniTeszt &= ~(1<<1);
#define RFSPAzonTesztFlagGet (RFFogadasKozbeniTeszt & (1<<1))
#define RFHosszTesztFlagBE RFFogadasKozbeniTeszt |= (1<<2);
#define RFHosszTesztFlagKI RFFogadasKozbeniTeszt &= ~(1<<2);
#define RFHosszTesztFlagGet (RFFogadasKozbeniTeszt & (1<<2))

volatile unsigned short RFkuldendo[30];//Manchester kódolt byte-ok
volatile char RFkuldendohossz = 0;
volatile char RFKuldesFolyamatban = 0;
volatile short RFkuldendoLepteto = 0;

volatile char RFBitrataKategoria = 6;
volatile char TM0OvfBitSzamlalo = 0;

volatile char RFAdasFolyamatban = 0;

#define RFNyugtazasMaxprobalkozasok 10//ennél 1-gyel többször fog próbálkozni
volatile char RFNyugtazasLepteto = 255;//255: nem kell nyugtázás
#define  RFNyugtaMaxertekKiszamolo ((((30 + (double)6*16*((double)1000/((double)F_CPU/(double)(256*RFBitrataKategoria)))))*F_CPU)/(23040000)) //nyugtahossza*(sec-bõl ms / egy bit ideje)
volatile short RFNyugtazasSzamlaloMaxertek = 0;
volatile short RFNyugtazasSzamlalo = 28000;
volatile unsigned short RFVisszavartCimzett = 0;

volatile char UARTBejovoLepteto = 0;
volatile char UARTBejovoHossz = 255;
volatile char UARTBejovoHosszMaradando = 0;//csak sikeres vétel esetén változik
volatile char UARTbejovo[30];
volatile char UARTujbejovo = 0;

char UARTkuldendo[30];
char UARTkuldendoHossz = 0;
char UARTkuldendoProbSzamlalo = 255;
#define UARTNyugtazasMaxprobalkozasok 6
volatile short UARTNyugtazasSzamlalo = 28000;
#define UARTNyugtazaSzamlaloMaxido 40*144/5// (5/144)ms		NEM mehet a 90-es osztóba, mert abban baszik mûködni (nem tudni, miért)

volatile char timeroszto90szamlalo = 0;
ISR(TIMER0_OVF_vect)
{
	if(UARTNyugtazasSzamlalo <= UARTNyugtazaSzamlaloMaxido)
	{
		++UARTNyugtazasSzamlalo;
	}

	if(RFAdasFolyamatban)
	{
		++TM0OvfBitSzamlalo;
		if(TM0OvfBitSzamlalo >= RFBitrataKategoria)
		{
			if(RFKuldesFolyamatban && RFkuldendoLepteto < RFkuldendohossz*16)
			{
				if(RFkuldendo[RFkuldendoLepteto/16] & (1<<RFkuldendoLepteto%16))
				{
					RFKiSetMAGAS
				}
				else
				{
					RFKiSetALACSONY
				}
				++RFkuldendoLepteto;
			}
			else if(RFkuldendoLepteto >= RFkuldendohossz*16)//ELSE ágra kell tenni, hogy az utolsó bitet is elküldje
			{
				RFKiSetALACSONY
				RFKuldesFolyamatban = 0;
				RFAdasFolyamatban = 0;
			}

			TM0OvfBitSzamlalo = 0;
		}
		
		RFNyugtazasSzamlalo = 0;
	}
	else
	{
		++TM0OvfBitSzamlalo;

		if(RFBeMagase)
		{
			if(RFBejovoMagasokSzama < 28000)//Túlcsordulás ellen
			++RFBejovoMagasokSzama;
		}
		else
		{
			RFBejovoMagasokSzama = 0;
		}

		if(RFBejovoMagasokSzama > RFBitrataKategoria*10 && RFBejovoStatusz == 0)//bitenként 12 méréssel 10 bit hosszan && státusz 0
		{
			RFBejovoStatusz = 1;
		}

		if(RFBejovoStatusz == 1)
		{
			if(!RFBeMagase)//Magas utáni alacsony szinkronizáló bit megjött
			{
				RFbejovo[0] = 0b0111111111111111;
				RFbejovoLepteto = 15;
				if(RFNyugtazasLepteto == 255)
				{
					RFbejovohossz = 255;
				}
				else
				{
					RFbejovohossz = 5;
				}

				if(RFBitrataKategoria > 8)//mintavételezés idejének beállítása a bit közepére
				{
					TM0OvfBitSzamlalo = RFBitrataKategoria/2 + 1;
				}
				else
				{
					TM0OvfBitSzamlalo = RFBitrataKategoria/2;
				}

				RFBejovoStatusz = 2;
			}
		}

		if(RFBejovoStatusz == 2 && TM0OvfBitSzamlalo >= RFBitrataKategoria)//24: 1200bps, stb...
		{

			if(RFBeMagase)
			{
				RFbejovo[RFbejovoLepteto/16] |= (1<<RFbejovoLepteto%16);
			}
			else
			{
				RFbejovo[RFbejovoLepteto/16] &= ~(1<<RFbejovoLepteto%16);
			}
			++RFbejovoLepteto;

			if(RFbejovoLepteto/16 >= RFbejovohossz)
			{
				RFBejovoStatusz = 3;
			}

			if(RFbejovoLepteto/16 == 2)
			{
				RFPreambTesztFlagBE
			}
			if(RFbejovoLepteto/16 == 4 && RFNyugtazasLepteto == 255 )
			{
				RFSPAzonTesztFlagBE
			}
			if(RFbejovoLepteto/16 == 6 && RFNyugtazasLepteto == 255 )
			{
				RFHosszTesztFlagBE
			}
			TM0OvfBitSzamlalo = 0;
		}
	}
	
	++timeroszto90szamlalo;
	if(timeroszto90szamlalo > 89)
	{
		timeroszto90szamlalo = 0;

		++UART_KovByteVarakSzamlalo;
		if(UART_KovByteVarakSzamlalo > UART_KovByteVarakSzamlaloMaxertek)
		{
			UARTBejovoHossz = 255;//UART vétel alaphelyzetbe
			UARTBejovoLepteto = 0;
		}
		
		if(Int0PergesSzamlalo <= Int0PergesSzamlaloMaxertek)
		++Int0PergesSzamlalo;

		if(TWIKockaValaszSzamlalo <= TWIKockaValaszSzamlaloMaxertek)
		++TWIKockaValaszSzamlalo;
		
		if(RFNyugtazasSzamlalo <= RFNyugtazasSzamlaloMaxertek)
		++RFNyugtazasSzamlalo;
	}
}
unsigned int Beolvas10bitADC(unsigned char csatorna)
{
	ADMUX = (ADMUX & 0b11110000) | csatorna;    //ADC csatorna kivalasztasa
	ADCSRA |= (1<<ADSC);    // ADC konverzio elinditasa
	while (ADCSRA & (1<<ADSC));    // varas az atalakitasra
	ADCSRA |= (1<<ADSC);         // konverzió elindítás
	while (ADCSRA & (1<<ADSC));    // varas az atalakitasra
	return (ADCL | (ADCH<<8));    // ADC ertek kiolvasasa
}
uint16_t man_encode(uint8_t unenc)
{

	uint16_t ki = 0;
	int i;
	for (i = 7; i>=0;--i)
	{
		if(unenc & (1<<i))
		{
			ki |= (1<<((2*i)+1));
		}
		else
		{
			ki |= (1<<(2*i));
		}
	}
	return ki;
}

volatile uint8_t receive_error = 0;
uint8_t man_decode(uint16_t enc)
{
	receive_error = 0;
	uint8_t ki = 0;
	int i;
	for(i = 0; i < 8;++i)
	{
		if(((enc & (1<<(2*i))) == 0) && (enc & (1<<((2*i)+1))))
		{
			ki |= (1<<i);
		}
		else if(!((enc & (1<<(2*i))) && (enc & (1<<(2*i+1)) == 0)))//TAGADVA => ha nem 0
		{
			receive_error = 1;
		}
	}

	return(ki);
}

ISR(INT0_vect)
{
	if(Int0PergesSzamlalo > Int0PergesSzamlaloMaxertek)
	{
		Int0PergesSzamlalo = 0;
		if(PIND & (1<<PD2))
		{
			Gomb1 = 1;
		}
	}
}
void _varj_us(unsigned int varido)
{
	varido -= 250;
	unsigned int i;
	unsigned int korokszama = varido/760;
	

	if(varido >= 760)
	{
		for (i = 0; i < korokszama; ++i)
		{
			_delay_us(757);
		}
		
		varido = varido%760;
	}

	if(varido >= 50)
	{
		korokszama = varido/50;
		for (i = 0; i < korokszama; ++i)
		{
			_delay_us(47);
		}

		varido = varido%50;
	}
	if(varido >= 5)
	{
		korokszama = varido/5;
		for (i = 0; i < korokszama; ++i)
		{
			_delay_us(2);
		}
	}
}
unsigned short RFBejovobolAdattomb(char *ki, char *manchasterHiba)
{
	manchasterHiba = 0;
	char i;
	for(i = 0;i<RFbejovohossz - 4;++i)
	{
		ki[i] = man_decode(RFbejovo[i+2]);

		if(receive_error)
		{
			manchasterHiba=1;
		}
	}

	return man_decode(RFbejovo[RFbejovohossz-2]) | (man_decode(RFbejovo[RFbejovohossz-1]) << 8);
}
unsigned short const crc16table[256] PROGMEM = {
	0x0000, 0xC0C1, 0xC181, 0x0140, 0xC301, 0x03C0, 0x0280, 0xC241,
	0xC601, 0x06C0, 0x0780, 0xC741, 0x0500, 0xC5C1, 0xC481, 0x0440,
	0xCC01, 0x0CC0, 0x0D80, 0xCD41, 0x0F00, 0xCFC1, 0xCE81, 0x0E40,
	0x0A00, 0xCAC1, 0xCB81, 0x0B40, 0xC901, 0x09C0, 0x0880, 0xC841,
	0xD801, 0x18C0, 0x1980, 0xD941, 0x1B00, 0xDBC1, 0xDA81, 0x1A40,
	0x1E00, 0xDEC1, 0xDF81, 0x1F40, 0xDD01, 0x1DC0, 0x1C80, 0xDC41,
	0x1400, 0xD4C1, 0xD581, 0x1540, 0xD701, 0x17C0, 0x1680, 0xD641,
	0xD201, 0x12C0, 0x1380, 0xD341, 0x1100, 0xD1C1, 0xD081, 0x1040,
	0xF001, 0x30C0, 0x3180, 0xF141, 0x3300, 0xF3C1, 0xF281, 0x3240,
	0x3600, 0xF6C1, 0xF781, 0x3740, 0xF501, 0x35C0, 0x3480, 0xF441,
	0x3C00, 0xFCC1, 0xFD81, 0x3D40, 0xFF01, 0x3FC0, 0x3E80, 0xFE41,
	0xFA01, 0x3AC0, 0x3B80, 0xFB41, 0x3900, 0xF9C1, 0xF881, 0x3840,
	0x2800, 0xE8C1, 0xE981, 0x2940, 0xEB01, 0x2BC0, 0x2A80, 0xEA41,
	0xEE01, 0x2EC0, 0x2F80, 0xEF41, 0x2D00, 0xEDC1, 0xEC81, 0x2C40,
	0xE401, 0x24C0, 0x2580, 0xE541, 0x2700, 0xE7C1, 0xE681, 0x2640,
	0x2200, 0xE2C1, 0xE381, 0x2340, 0xE101, 0x21C0, 0x2080, 0xE041,
	0xA001, 0x60C0, 0x6180, 0xA141, 0x6300, 0xA3C1, 0xA281, 0x6240,
	0x6600, 0xA6C1, 0xA781, 0x6740, 0xA501, 0x65C0, 0x6480, 0xA441,
	0x6C00, 0xACC1, 0xAD81, 0x6D40, 0xAF01, 0x6FC0, 0x6E80, 0xAE41,
	0xAA01, 0x6AC0, 0x6B80, 0xAB41, 0x6900, 0xA9C1, 0xA881, 0x6840,
	0x7800, 0xB8C1, 0xB981, 0x7940, 0xBB01, 0x7BC0, 0x7A80, 0xBA41,
	0xBE01, 0x7EC0, 0x7F80, 0xBF41, 0x7D00, 0xBDC1, 0xBC81, 0x7C40,
	0xB401, 0x74C0, 0x7580, 0xB541, 0x7700, 0xB7C1, 0xB681, 0x7640,
	0x7200, 0xB2C1, 0xB381, 0x7340, 0xB101, 0x71C0, 0x7080, 0xB041,
	0x5000, 0x90C1, 0x9181, 0x5140, 0x9301, 0x53C0, 0x5280, 0x9241,
	0x9601, 0x56C0, 0x5780, 0x9741, 0x5500, 0x95C1, 0x9481, 0x5440,
	0x9C01, 0x5CC0, 0x5D80, 0x9D41, 0x5F00, 0x9FC1, 0x9E81, 0x5E40,
	0x5A00, 0x9AC1, 0x9B81, 0x5B40, 0x9901, 0x59C0, 0x5880, 0x9841,
	0x8801, 0x48C0, 0x4980, 0x8941, 0x4B00, 0x8BC1, 0x8A81, 0x4A40,
	0x4E00, 0x8EC1, 0x8F81, 0x4F40, 0x8D01, 0x4DC0, 0x4C80, 0x8C41,
	0x4400, 0x84C1, 0x8581, 0x4540, 0x8701, 0x47C0, 0x4680, 0x8641,
	0x8201, 0x42C0, 0x4380, 0x8341, 0x4100, 0x81C1, 0x8081, 0x4040
};
unsigned short Crc16Szamolo(char *adat, char hossz)
{
	unsigned short crc16 = 0;

	int i;
	for (i=0; i<hossz; ++i)
	{
		//crc16 = _crc_ccitt_update(crc16, adat[i]);//Belsõ táblás
		crc16 = (crc16 >> 8) ^ pgm_read_dword(&crc16table[(crc16 ^ adat[i]) & 0xff]);//saját táblás

		//data = pgm_read_byte(&crc16table[(crc16 ^ adat[i]) & 0xff]);
		//crc16 = (crc16 >> 8) ^ crc16table[(crc16 ^ adat[i]) & 0xff];//saját táblás
	}

	return crc16;
}
void RFKulddAmiBenneVan()
{
	RFAdasFolyamatban = 1;

	RFkuldendoLepteto = 0;
	RFKuldesFolyamatban = 1;
}
void RFKulddUzenet(char kuldendo[], char hossz, char paramCRC, unsigned short CRC16)
{
	RFkuldendo[0] = 0b0111111111111111;//PREAMBLE 1
	RFkuldendo[1] = 0b0001000100010001;//PREAMBLE 2
	char i;
	for(i = 0; i < hossz; ++i)
	{
		RFkuldendo[i+2] = man_encode(kuldendo[i]);
	}
	if(!paramCRC)
	{
		CRC16 = Crc16Szamolo(kuldendo,hossz);
	}
	
	RFkuldendo[hossz + 2] =  man_encode(CRC16 & 0x00FF);
	RFkuldendo[hossz + 3] =  man_encode((CRC16 & 0xFF00) >> 8);

	RFkuldendohossz = hossz + 4;//4 = 2 preamble + 2 crc

	RFKulddAmiBenneVan();
}
void UARTAdatKuld(char adat)  // Ez a fuggveny a kuldendo adatot beirja az UDR regiszter kimeno pufferjebe
{
	while(!(UCSRA & (1<<UDRE))) // Varakozas amig az Ado kesz nem lesz az adatkuldesre
	{
		//   Varakozas
	}
	// Az Ado mar kesz az adatkuldesre, a kuldendo adatot a kimeno pufferjebe irjuk
	UDR=adat;
}
void UARTTombKuld(char* tomb, char hossz)  // Ez a fuggveny a kuldendo adatot beirja az UDR regiszter kimeno pufferjebe
{
	int i = 0;
	for(;i < hossz; ++i)
	{
		UARTAdatKuld(tomb[i]);
	}
}
void Konfig10bitADC()    // ADC konfiguralas (beallitas)
{
	ADMUX |= (1<<REFS0);    // Vcc mint referencia
	ADCSRA = (1<<ADEN) | (1<<ADPS1) | (1<<ADPS0);  // ADC engedelyezese, ADC eloosztas = 8 (125 KHz)
}
void KonfigUART()  // UART beallitasa
{
	// 9600 bps soros kommunikacio sebesseg beallitasa
	UBRRL = UBRR_ERTEK;        // UBRR_ERTEK also 8 bitjenek betoltese az UBRRL regiszterbe
	UBRRH = (UBRR_ERTEK>>8);   // UBRR_ERTEK felso 8 bitjenek betoltese az UBRRH regiszterbe
	// Aszinkron mod, 8 Adat Bit, Nincs Paritas Bit, 1 Stop Bit
	UCSRC |= (1 << URSEL) | (1 << UCSZ0) | (1 << UCSZ1);
	//Ado es Vevo aramkorok bekapcsolasa + az RX interrupt engedelyezese
	UCSRB |= (1 << RXEN) | (1 << RXCIE) | (1 << TXEN);   //
}
void init()
{
	TWAR = TWICim;
	TWBR = TWIBaudRate;
	TWCR = (1<<TWEN) | (1<<TWEA)| (1<<TWIE);

	TCCR0 |= (1<<CS00);//F_CPU/1
	TIMSK |= (1<<TOIE0);

	GICR |= (1<<INT0); //INT0 interrupt enable
	MCUCR |= (1<<ISC00);//Any logical change on INT0

	DDRD |= (1<<PD7);

	DDRC |= (1<<PC3);
	
	KonfigUART();
	Konfig10bitADC();

	sei();
}
int main(void)
{
	init();
	/* Replace with your application code */
	while (1)
	{
		if(RFPreambTesztFlagGet)
		{
			if(RFbejovo[1] != 0b0001000100010001)
			{
				RFbejovohossz = 0;//2. preamble nem jó, vétel befejezése
				RFBejovoStatusz = 0;
				RFBejovoMagasokSzama = 0;
			}
			RFPreambTesztFlagKI
		}
		if(RFSPAzonTesztFlagGet)
		{
			if((man_decode(RFbejovo[2]) | (man_decode(RFbejovo[3]) << 8)) != SmartPartsRFAzonosito)
			{
				RFbejovohossz = 0;//nem SmartParts jel, vétel befejezése
				RFBejovoStatusz = 0;
				RFBejovoMagasokSzama = 0;
			}
			RFSPAzonTesztFlagKI
		}
		if(RFHosszTesztFlagGet)
		{
			char hossz1buff = man_decode(RFbejovo[4]);
			if(hossz1buff == man_decode(RFbejovo[5]))
			{
				RFbejovohossz = hossz1buff + 4;//4 = 2 preamble + 2 crc
			}
			else
			{
				RFbejovohossz = 0;//hibás adatátvitel, vétel befejezése
				RFBejovoStatusz = 0;
				RFBejovoMagasokSzama = 0;
			}
			RFHosszTesztFlagKI
		}
		if(RFNyugtazasSzamlalo >= RFNyugtazasSzamlaloMaxertek)
		{
			if(RFNyugtazasLepteto < RFNyugtazasMaxprobalkozasok)
			{
				++RFNyugtazasLepteto;
				RFKulddAmiBenneVan();
				RFNyugtazasSzamlalo = 0;
			}
			else if(RFNyugtazasLepteto != 255)
			{
				
				RFNyugtazasLepteto = 255;
				UARTkuldendo[0] = 53;//sikertelen nyugta
				UARTkuldendoHossz = 1;
				UARTkuldendoProbSzamlalo = 0;
			}
		}
		if(Gomb1 && RFKuldesFolyamatban == 0)
		{
			char kd[13];
			kd[0] = 13;
			kd[1] = 13;
			kd[2] = 10;
			kd[3] = SmartPartsRFAzonosito & 0x00FF;
			kd[4] = (SmartPartsRFAzonosito & 0xFF00) >> 8;
			kd[5] = 8;//hossz
			kd[6] = 8;//hossz újra

			short cimzett = 0x002;//12bit => max 4095
			kd[7] =  (cimzett & 0x0FF0) >> 4;
			kd[8] = (cimzett & 0x000F) << 4;
			kd[8] |= (RFSajatAzonosito & 0x0F00) >> 8;
			kd[9] = RFSajatAzonosito & 0x00FF;
			if(PORTC & (1<<PC5))
			{
				kd[10] = 0;//GERINC
			}
			else
			{
				kd[10] = 1;//GERINC
			}

			unsigned short crc16 = Crc16Szamolo(kd, 11);
			kd[11] = crc16 & 0x00FF;
			kd[12] = (crc16 & 0xFF00) >> 8;

			int i;
			for(i = 0; i < 13; ++i)
			{
				UARTbejovo[i] = kd[i];
			}
			UARTBejovoHosszMaradando = 13;
			UARTujbejovo = 1;

			Gomb1 = 0;
		}
		if(RFBejovoStatusz == 3)
		{
			if(RFNyugtazasLepteto < RFNyugtazasMaxprobalkozasok && RFNyugtazasSzamlalo < RFNyugtazasSzamlaloMaxertek)
			{
				char behossz = RFbejovohossz;
				char beuzenet[behossz-2];
				int i;
				for(i = 2; i < behossz; ++i)
				{
					beuzenet[i] = man_decode(RFbejovo[i]);
				}
				if(((beuzenet[2] & 0x0F)<<8) | beuzenet[3] == RFVisszavartCimzett && (beuzenet[2] & 0xF0) == 0b10000000)//címzett és nyugta 4bit ellenõrzése
				{
					if(beuzenet[4] == 1)
					{
						RFNyugtazasLepteto = 255;
						
						UARTkuldendo[0] = 50;//sikeres nyugta
						UARTkuldendoHossz = 1;
						UARTkuldendoProbSzamlalo = 0;
					}
					else
					{
						RFNyugtazasSzamlalo = RFNyugtazasSzamlaloMaxertek;//hibás CRC, ismétlés
					}
				}
			}
			else
			{

			}
			RFBejovoStatusz = 0;
		}
		if(TWIKockaValaszSzamlalo != 31111 && TWIKockaValaszSzamlalo >= TWIKockaValaszSzamlaloMaxertek)
		{
			TWIKockaValaszSzamlalo = 31111;

			UARTkuldendo[0] = 37;//sikertelen idõtúllépés i2C
			UARTkuldendo[1] = 0;
			UARTkuldendo[2] = 0;
			UARTkuldendo[3] = 0;

			UARTkuldendoHossz = 4;
			UARTkuldendoProbSzamlalo = 0;
		}
		if(UARTkuldendoProbSzamlalo != 255 && UARTNyugtazasSzamlalo >= UARTNyugtazaSzamlaloMaxido)
		{
			if(UARTkuldendoProbSzamlalo < UARTNyugtazasMaxprobalkozasok)
			{
				UARTTombKuld(UARTkuldendo, UARTkuldendoHossz);
				++UARTkuldendoProbSzamlalo;
				UARTNyugtazasSzamlalo = 0;
			}
			else
			{
				UARTkuldendoProbSzamlalo = 255;
			}
		}
		if(UARTujbejovo)
		{
			char behossz = UARTBejovoHosszMaradando;
			char beuzenet[behossz];
			int i;
			for(i = 0; i < behossz; ++i)
			{
				beuzenet[i] = UARTbejovo[i];
			}

			if(beuzenet[2] == 19 && behossz == 4)//Nyugtázás
			{
				if(beuzenet[3] == 1)
				{
					UARTkuldendoProbSzamlalo = 255;
				}
			}
			else if(beuzenet[2] == 255 && behossz == 3)//Kézfogás
			{
				UARTTombKuld("SmartPartsKP", 12);
			}
			else
			{
				if(PORTC & (1<<PC3))
				PORTC &= ~(1<<PC3);
				else
				PORTC |= (1<<PC3);

				unsigned short crc16 = Crc16Szamolo(beuzenet, behossz - 2);
				unsigned short crc_Be = beuzenet[behossz-2] | (beuzenet[behossz-1] << 8);

				if(crc16 == crc_Be)//HA AZ ÜZENET HELYES
				{
					UARTAdatKuld(9);//nyugta
					UARTAdatKuld(1);
					if(beuzenet[2] == 10)//RF nyugtavárás üzenet kódja (pl. kapcsolás, vagy mérésindítás, amire esemény választ vár)
					{
						char kd[behossz - 5];
						
						for(i = 0; i < behossz - 5; ++i)
						{
							kd[i] = beuzenet[i + 3];
						}

						RFVisszavartCimzett = (kd[4]<<4) | (kd[5] >> 4);

						RFKulddUzenet(kd, behossz - 5, 1, Crc16Szamolo(kd, behossz - 5));
						RFNyugtazasSzamlaloMaxertek = RFNyugtaMaxertekKiszamolo;
						RFNyugtazasSzamlalo = 0;
						RFNyugtazasLepteto = 0;
					}
					else if(beuzenet[2] == 12)//RF Azonnali válasz várás üzenet kódja
					{
					}
					else if(beuzenet[2] == 30)//I2C Kocka Hozzáadás
					{
						char kd[6];
						kd[0] = TWIkuldendoHossz = 6;
						kd[1] = 30;//i2C hozzáadás kódja
						kd[2] = beuzenet[3];//RFID
						kd[3] = beuzenet[4];//RFID

						unsigned short crc16 = Crc16Szamolo(kd, 4);
						kd[4] = crc16 & 0x00FF;
						kd[5] = (crc16 & 0xFF00)>>8;

						int i = 0;
						for(;i < TWIkuldendoHossz;++i)
						{
							TWIkuldendo[i] = kd[i];
						}
						twi_Kuld_kuldendo(TWISlaveKockaCim,1);
						TWIKockaValaszSzamlalo = 0;
						
					}
				}
				else
				{
					UARTAdatKuld(9);//nyugta
					UARTAdatKuld(0);
				}
			}
			UARTujbejovo = 0;
		}
		if(TWIujbejovo)
		{
			if(TWIbejovo[0] > 1)
			{
				char behossz = TWIbejovoHossz;
				char beuzenet[behossz];
				int i = 0;
				for(; i < behossz; ++i)
				{
					beuzenet[i] = TWIbejovo[i];
				}
				//UARTAdatKuld(beuzenet[0]);
				//UARTAdatKuld(beuzenet[1]);
				//UARTAdatKuld(beuzenet[2]);
				//UARTAdatKuld(beuzenet[3]);
				//UARTAdatKuld(beuzenet[4]);
				switch(beuzenet[1])
				{
					case 31://Sikeres kocka hozzáadás
					{
						unsigned short crc_Be = beuzenet[behossz-2] | (beuzenet[behossz-1] << 8);
						unsigned short crc16 = Crc16Szamolo(beuzenet, behossz - 2);

						if(crc16 == crc_Be)
						{
							TWIKockaValaszSzamlalo = 31111;

							char kd[4];
							kd[0] = 35;//sikeres i2C
							kd[1] = beuzenet[2];//kocka típus
							unsigned short crc16u = Crc16Szamolo(kd, 2);
							kd[2] = crc16u & 0x00FF;
							kd[3] = (crc16u & 0xFF00)>>8;

							int x = 0;
							for(;x < TWIkuldendoHossz;++x)
							{
								UARTkuldendo[x] = kd[x];
							}

							UARTkuldendoHossz = 4;
							UARTkuldendoProbSzamlalo = 0;
						}
						else
						{
							UARTkuldendo[0] = 38;//sikertelen CRC Vissza hiba i2C
							UARTkuldendo[1] = 0;
							UARTkuldendo[2] = 0;
							UARTkuldendo[3] = 0;

							UARTkuldendoHossz = 4;
							UARTkuldendoProbSzamlalo = 0;
						}
						break;
					}
					case 33:
					{
						UARTkuldendo[0] = 39;//sikertelen CRC El hiba i2C
						UARTkuldendo[1] = 0;
						UARTkuldendo[2] = 0;
						UARTkuldendo[3] = 0;

						UARTkuldendoHossz = 4;
						UARTkuldendoProbSzamlalo = 0;
						break;
					}
				}
			}
			TWIujbejovo = 0;
		}
	}
}
ISR(USART_RXC_vect)
{
	if(UARTBejovoLepteto < UARTBejovoHossz)
	{
		UARTbejovo[UARTBejovoLepteto] = UDR;
		++UARTBejovoLepteto;
		UART_KovByteVarakSzamlalo = 0;//Köv Byteig lévõ idõ újrakezdése

		if(UARTBejovoLepteto == UARTBejovoHossz)
		{
			UARTBejovoHosszMaradando = UARTBejovoHossz;
		}
	}

	if(UARTBejovoLepteto == 2)
	{
		if(UARTbejovo[0] == UARTbejovo[1])
		{
			UARTBejovoHossz = UARTbejovo[0];
		}
		else
		{
			UARTBejovoHossz = 0;//hibás hossz, vétel befejezése
		}
	}
	if(UARTBejovoLepteto >= UARTBejovoHossz)
	{
		UARTujbejovo = 1;
	}
}

#pragma region TWI kezeles
void twi_master_start(char repstart)
{
	twiRepstartolt = repstart;
	// Enable interrupt and start bits
	TWCR = (1 << TWINT)| (1 << TWEN) | (1 << TWSTA) | (1<<TWIE);
}
//void twi_master_Repstart()
//{
//twiRepstartolt = 1;
//// Enable interrupt and start bits
//TWCR = (1 << TWINT)| (1 << TWEN) | (1 << TWSTA) | (1<<TWIE);
//}
void twi_master_stop()
{
	// Set stop bit
	TWCR = (1 << TWINT) | (1 << TWEN) | (1 << TWSTO)| (1<<TWIE);
}

void twi_setAck()
{
	TWCR = (1 << TWINT) | (1 << TWEN) | (1 << TWEA)| (1<<TWIE);
}

void twi_setNack()
{
	TWCR = (1 << TWINT) | (1 << TWEN)| (1<<TWIE);
}

void twi_Kuld_kuldendo(char SlaveCim, char VisszOlv)
{
	TWISlaveCim = SlaveCim;
	TWIvisszaolvasson = VisszOlv;
	TWIkuldendoHossz = TWIkuldendo[0];
	TWIkuldendoHanyadik = 0;
	twi_master_stop();
	while(TWCR & (1<<TWINT));
	twi_master_start(0);
}
ISR(TWI_vect)
{
	//UARTAdatKuld(TWSR & 0b11111000); UARTAdatKuld(TWSR & 0b11111000); UARTAdatKuld(TWSR & 0b11111000);
	//UARTAdatKuld(TWDR);
	switch (TWSR & 0b11111000)
	{
		//////////////////////MASTER TRANSMITTER///////////////////////
		case 0x08://A START condition transmitted
		case 0x10://A repeated START condition transmitted
		{
			if(twiRepstartolt == 0)
			{
				TWDR = TWISlaveCim + TWI_IR;
				twi_setAck();
			}
			else
			{
				if(TWIvisszaolvasson)
				{
					TWDR = TWISlaveCim + TWI_OLVAS;
					twi_setAck();
				}
			}
			break;
		}
		case 0x18://SLA+W transmitted; ACK received
		{
			if(TWIkuldendoHanyadik >= TWIkuldendoHossz)
			{
				if(TWIvisszaolvasson)
				{
					twi_master_start(0);
				}
				else
				{
					twi_master_stop();
				}
			}
			else
			{
				TWDR = TWIkuldendo[TWIkuldendoHanyadik];
				++TWIkuldendoHanyadik;
				twi_setAck();
			}
			break;
		}
		case 0x30://Data byte transmitted; NOT ACK received
		//{
		//twi_master_stop();
		//break;
		//}
		case 0x20://SLA+W transmitted; NOT ACK received
		{
			twi_master_stop();
			break;
		}
		case 0x28://Data byte transmitted; ACK received
		{
			if(TWIkuldendoHanyadik < TWIkuldendoHossz)
			{
				TWDR = TWIkuldendo[TWIkuldendoHanyadik];
			}
			++TWIkuldendoHanyadik;

			if(TWIkuldendoHanyadik > TWIkuldendoHossz)
			{
				if(TWIvisszaolvasson)
				{
					twi_master_start(1);
				}
				else
				{
					twi_master_stop();
				}
			}
			break;
		}
		//case 0x38://Arbitration lost in SLA+W or data bytes
		//{//Kikommentezve, mert nem fér a FLASH-be
		//
		//break;
		//}
		//////////////////////MASTER RECEIVER///////////////////////
		case 0x40://SLA+R transmitted; ACK received
		{
			TWIbejovoHanyadik = 0;
			TWIujbejovo = 0;
			twi_setAck();
			break;
		}
		//case 0x48://SLA+R transmitted; NOT ACK received
		//{//Kikommentezve, mert nem fér a FLASH-be
		//break;
		//}
		case 0x50://Data byte received; ACK returned
		{
			if(TWIbejovoHanyadik == 0)
			{
				if(TWDR != 255)
				TWIbejovoHossz = TWDR;
				else
				TWIbejovoHossz = 1;
			}

			if(TWIbejovoHanyadik < TWIbejovoHossz)
			{
				TWIbejovo[TWIbejovoHanyadik] = TWDR;
				twi_setAck();
			}
			
			++TWIbejovoHanyadik;
			
			if(TWIbejovoHanyadik >= TWIbejovoHossz)
			{
				TWIujbejovo = 1;
				twi_master_stop();
				//UARTStringKuld("STOPPED\r\n",9);
			}
			break;
		}
		//case 0x58://Data byte received; NOT ACK returned
		//{//Kikommentezve, mert nem fér a FLASH-be
		//
		//break;
		//}
	}
	//Kikommentezve, mert nem fér a FLASH-be
	//////////////////////SLAVE RECEIVER///////////////////////
	//switch (TWSR & 0b11111000)
	//{
	//case 0x60://Own SLA+W  received; ACK  returned
	//{
	//bejovoHanyadik = 0;
	//ujbejovo = 0;
	//twi_setAck();
	//break;
	//}
	//case 0x68://Arbitration lost in SLA+R/W as Master; own SLA+W  received; ACK returned
	//{
	//
	//break;
	//}
	//case 0x70://General call address received; ACK returned
	//{
	//
	//break;
	//}
	//case 0x78://Arbitration lost in SLA+R/W as Master; General call address received; ACK returned
	//{
	//
	//break;
	//}
	//case 0x80://Previously addressed with own SLA+W; data received; ACK  returned
	//{
	//if(bejovoHanyadik == 0)
	//{
	//bejovoHossz = TWDR;
	//}
	//
	//if(bejovoHanyadik < bejovoHossz)
	//{
	//bejovo[bejovoHanyadik] = TWDR;
	////twi_setAck();
	//}
	//
	//++bejovoHanyadik;
	//
	//if(bejovoHanyadik >= bejovoHossz)
	//{
	//ujbejovo = 1;
	//}
	//break;
	//}
	//case 0x88://Previously addressed with own SLA+W; data received; NOT ACK  returned
	//{
	//
	//break;
	//}
	//case 0x90://Previously addressed with general call; data received; ACK  returned
	//{
	//
	//break;
	//}
	//case 0x98://Previously addressed with general call; data received; NOT ACK returned
	//{
	//
	//break;
	//}
	//case 0xA0://A STOP condition or repeated START condition received while still addressed as Slave
	//{
	//
	//break;
	//}
	////////////////////////SLAVE TRANSMITTER///////////////////////
	//case 0xA8://Own SLA+R received; ACK returned
	//{
	//if(bejovoHossz > 1)
	//{
	//switch (bejovo[1])
	//{
	//case 65:
	//{
	//kuldjvissza = 1;
	//kuldendoHossz = 3;
	//kuldendo[0] = 3;
	//kuldendo[1] = 70;
	//kuldendo[2] = ADCH;
	//
	//kuldendoHanyadik = 1;
	//TWDR = kuldendo[0];
	//}
	//break;
	//}
	//}
	//break;
	//}
	//case 0xB0://Arbitration lost in SLA+R/W as Master; own SLA+R received; ACK returned
	//{
	//
	//break;
	//}
	//case 0xB8://Data byte in TWDR transmitted; ACK received
	//{
	//if(kuldjvissza)
	//{
	//if(kuldendoHanyadik < kuldendoHossz)
	//{
	//TWDR = kuldendo[kuldendoHanyadik];
	//
	//if(kuldendoHanyadik == kuldendoHossz -1)
	//{
	//twi_setNack();
	//}
	//++kuldendoHanyadik;
	//}
	//else
	//{
	//TWDR=233;
	//}
	//}
	//else
	//{
	//TWDR=0;
	//}
	//break;
	//}
	//case 0xC0://Data byte in TWDR transmitted; NOT ACK received
	//{
	//
	//break;
	//}
	//case 0xC8://Last data byte in TWDR transmitted (TWEA = “0”); ACK received
	//{
	//twi_setAck();
	//break;
	//}
	//}

	TWCR |= 0x80;// Clear interrupt flag bit
}

#pragma endregion TWI kezeles