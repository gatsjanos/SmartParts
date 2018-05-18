/*
* SmartPartsKockaRele.c
*
* Created: 2016. 10. 16. 12:21:41
* Author : gatsj
*/
#define F_CPU 7372800

#include <avr/io.h>
#include <avr/interrupt.h>
#include <util/delay.h>

#define USART_BAUDRATE 9600  // soros kommunikacio sebessege: 9600 bps
#define UBRR_ERTEK ((F_CPU / (USART_BAUDRATE * 16UL)) - 1)  // UBRR

///////TWI/////
volatile char TWICim = (60<<1);
#define SCL_CLOCK 10000
#define TWIprescaler_value 1
#define TWIBaudRate ((F_CPU/SCL_CLOCK)-16)/(2*TWIprescaler_value)
volatile char TWISlaveKozpontCim = (50<<1);
volatile char TWISlaveCim;
#define TWI_IR 0
#define TWI_OLVAS 1
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

volatile char SajatAzonositoMentendoFlag = 0;

#define RFBeMagase (PIND & (1<<PD6))

#define RFKiSetALACSONY PORTD &= ~(1<<PD7);
#define RFKiSetMAGAS PORTD |= (1<<PD7);

#define SmartPartsRFAzonosito 0xABCD
short SajatRFAzonosito = 0x002;
#define SajatKockaTipus 1

volatile unsigned short RFbejovo[40];//Manchester kódolt byte-ok
volatile char RFbejovohossz = 255;//255: Nincs megadva érték
volatile short RFbejovoLepteto = 0;

volatile short RFBejovoMagasokSzama = 0;
volatile char RFBejovoStatusz = 0;//0: Semmi   1: Folyamatos magasban van   2: Fogadás folyamatban 3: Új üzenet fogadva
volatile char TM0OvfBitSzamlalo = 0;
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

volatile char RFBitrataKategoria = 6;//TODO: TESZTELD LE, HOGY HOGYAN MÛKÖDNEK A 12-NÉL GYORSABB BITRÁTÁK!!!!!

volatile unsigned short RFkuldendo[40];//Manchester kódolt byte-ok
volatile char RFkuldendohossz = 0;
volatile char RFKuldesFolyamatban = 0;
volatile short RFkuldendoLepteto = 0;

volatile char RFAdasFolyamatban = 0;

volatile long RelePeriodikusSzamlalo = 0;
volatile long RelePeriodikusSzamlaloMaxertek = 0;

ISR(TIMER0_OVF_vect)
{
	if(RelePeriodikusSzamlalo <= RelePeriodikusSzamlaloMaxertek)
	++RelePeriodikusSzamlalo;

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
				RFbejovohossz = 255;
				TM0OvfBitSzamlalo = 7;//mintavételezés idejének beállítása a bit közepére

				//if(RFBitrataKategoria > 8)//mintavételezés idejének beállítása a bit közepére
				{
					TM0OvfBitSzamlalo = RFBitrataKategoria/2 + 1;
				}
				//else
				{
					//TM0OvfBitSzamlalo = RFBitrataKategoria/2;
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
			if(RFbejovoLepteto/16 == 4)
			{
				RFSPAzonTesztFlagBE
			}
			if(RFbejovoLepteto/16 == 6)
			{
				RFHosszTesztFlagBE
			}
			TM0OvfBitSzamlalo = 0;
		}
	}
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
unsigned short crc16table[] = {
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
	0x8201, 0x42C0, 0x4380, 0x8341, 0x4100, 0x81C1, 0x8081, 0x4040,
};
unsigned short Crc16Szamolo(char *adat, char hossz)
{
	unsigned short crc16 = 0;

	int i;
	for (i=0; i<hossz; ++i)
	{
		//crc16 = _crc_ccitt_update(crc16, adat[i]);//Belsõ táblás
		crc16 = (crc16 >> 8) ^ crc16table[(crc16 ^ adat[i]) & 0xff];//saját táblás
	}

	return crc16;
}
void RFKulddAmiBenneVan()
{
	RFAdasFolyamatban = 1;

	RFkuldendoLepteto = 0;
	RFKuldesFolyamatban = 1;
}
void RFKulddNyugta(char helyes)
{
	RFkuldendo[0] = 0b0111111111111111;//PREAMBLE 1
	RFkuldendo[1] = 0b0001000100010001;//PREAMBLE 2
	RFkuldendo[2] |= man_encode(0b10000000 | ( (SajatRFAzonosito & 0x0F00) >> 8 ));//0b10000000: nyugta azonosító
	RFkuldendo[3] = man_encode(SajatRFAzonosito & 0x00FF);
	RFkuldendo[4] = man_encode(helyes);


	RFkuldendohossz = 5;

	RFKulddAmiBenneVan();
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
void EEPROM_write(unsigned int uiAddress, unsigned char ucData)
{
	//Wait for completion of previous write
	while(EECR & (1<<EEWE));

	/* Set up address and data registers */
	EEAR = uiAddress;
	EEDR = ucData;
	/*
	Write logical one to EEMWE */
	cli();
	EECR |= (1<<EEMWE);
	/* Start eeprom write by setting EEWE */
	EECR |= (1<<EEWE);
	sei();
}
unsigned char EEPROM_read( unsigned int uiAddress)
{
	/* Wait for completion of previous write */
	while(EECR & (1<<EEWE))
	;
	/* Set up address register */
	EEAR = uiAddress;
	/*
	Start eeprom read by writing EERE */
	EECR |= (1<<EERE);
	/* Return data from data register */
	return EEDR;
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

	DDRC |= (1<<PC3);
	DDRD |= (1<<PD7);

	//KonfigUART();

	SajatRFAzonosito = EEPROM_read(10) | (EEPROM_read(11) << 8);

	sei();
}
int main(void)
{
	init();
	
	while (1)
	{
		if(RelePeriodikusSzamlaloMaxertek != 0 && RelePeriodikusSzamlalo > RelePeriodikusSzamlaloMaxertek)
		{
			if(PORTC & (1<<PC3))
			PORTC &= ~(1<<PC3);
			else
			PORTC |= (1<<PC3);

			RelePeriodikusSzamlalo = 0;
		}
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
		if(RFBejovoStatusz == 3)
		{
			char ManchesterHiba;
			char behossz = RFbejovohossz;
			char beuzenet[behossz-4];
			unsigned short crc_Be = RFBejovobolAdattomb(beuzenet, ManchesterHiba);
			
			RFBejovoStatusz = 0;
			////////////////DEBUG////////////////////////////
			//int i;
			//for(i = 0; i < behossz-4;++i)
			//{
				//UARTAdatKuld(beuzenet[i]);
				//_delay_ms(1);
			//}
			//
			//UARTAdatKuld(crc_Be & 0x00FF);
			//_delay_ms(1);
			//UARTAdatKuld((crc_Be & 0xFF00) >> 8);
			////////////////////////////////////////////////

			if(!ManchesterHiba)
			{
				unsigned short crc16 = Crc16Szamolo(beuzenet, behossz-4);
				if(((beuzenet[4] << 4) | (beuzenet[5] >> 4)) == SajatRFAzonosito)
				{
					if(crc_Be == crc16)
					{
						RFKulddNyugta(1);
						if(((beuzenet[4] << 4) | (beuzenet[5] >> 4)) == SajatRFAzonosito || ((beuzenet[4] << 4) | (beuzenet[5] >> 4)) == 0)//0: General Call
						{
							if(beuzenet[7] == 0)
							{
								RelePeriodikusSzamlaloMaxertek = 0;
								PORTC &= ~(1<<PC3);
							}
							else if(beuzenet[7] == 1)
							{
								RelePeriodikusSzamlaloMaxertek = 0;
								PORTC |= (1<<PC3);
							}
							else if(beuzenet[7] == 255)
							{
								//TODO ÉRTÉKLEKÉRÉS
							}
							else
							{
								RelePeriodikusSzamlaloMaxertek = (long)(beuzenet[7]-2)*(long)100*(long)144/(long)5;//144/5: ms-é teszi
								RelePeriodikusSzamlalo = 0;
							}
						}
					}
					else
					{
						RFKulddNyugta(0);
					}

				}
			}
			RFBejovoStatusz = 0;
		}
		if(SajatAzonositoMentendoFlag)
		{
			EEPROM_write(10, SajatRFAzonosito & 0x00FF);
			EEPROM_write(11, (SajatRFAzonosito & 0xFF00)>>8);
			SajatAzonositoMentendoFlag = 0;
		}
	}
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
ISR(USART_RXC_vect)
{
	if(UDR == 'c')
	{
		UARTAdatKuld(SajatRFAzonosito & 0x00FF);
		UARTAdatKuld((SajatRFAzonosito & 0xFF00)>>8);
		UARTAdatKuld(EEPROM_read(10));
		UARTAdatKuld(EEPROM_read(11));
	}
}


#pragma region TWI kezeles
void twi_master_start()
{
	twiRepstartolt = 0;
	// Enable interrupt and start bits
	TWCR = (1 << TWINT)| (1 << TWEN) | (1 << TWSTA) | (1<<TWIE);
}
void twi_master_Repstart()
{
	twiRepstartolt = 1;
	// Enable interrupt and start bits
	TWCR = (1 << TWINT)| (1 << TWEN) | (1 << TWSTA) | (1<<TWIE);
}

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
	twi_master_start();
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
					twi_master_start();
				}
				else
				twi_master_stop();
			}
			else
			{
				TWDR = TWIkuldendo[TWIkuldendoHanyadik];
				++TWIkuldendoHanyadik;
				twi_setAck();
			}
			break;
		}
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
					twi_master_Repstart();
				}
				else
				{
					twi_master_stop();
				}
			}
			break;
		}
		case 0x30://Data byte transmitted; NOT ACK received
		{
			twi_master_stop();
			break;
		}
		case 0x38://Arbitration lost in SLA+W or data bytes
		{

			break;
		}
		//////////////////////MASTER RECEIVER///////////////////////
		case 0x40://SLA+R transmitted; ACK received
		{
			TWIbejovoHanyadik = 0;
			TWIujbejovo = 0;
			twi_setAck();
			break;
		}
		case 0x48://SLA+R transmitted; NOT ACK received
		{
			break;
		}
		case 0x50://Data byte received; ACK returned
		{
			if(TWIbejovoHanyadik == 0)
			{
				TWIbejovoHossz = TWDR;
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
		case 0x58://Data byte received; NOT ACK returned
		{
			
			break;
		}
	}
	//////////////////////SLAVE RECEIVER///////////////////////
	switch (TWSR & 0b11111000)
	{
		case 0x60://Own SLA+W  received; ACK  returned
		{
			TWIbejovoHanyadik = 0;
			TWIujbejovo = 0;
			twi_setAck();
			break;
		}
		case 0x68://Arbitration lost in SLA+R/W as Master; own SLA+W  received; ACK returned
		{

			break;
		}
		case 0x70://General call address received; ACK returned
		{

			break;
		}
		case 0x78://Arbitration lost in SLA+R/W as Master; General call address received; ACK returned
		{

			break;
		}
		case 0x80://Previously addressed with own SLA+W; data received; ACK  returned
		{
			if(TWIbejovoHanyadik == 0)
			{
				TWIbejovoHossz = TWDR;
			}

			if(TWIbejovoHanyadik < TWIbejovoHossz)
			{
				TWIbejovo[TWIbejovoHanyadik] = TWDR;
				//twi_setAck();
			}
			
			++TWIbejovoHanyadik;

			if(TWIbejovoHanyadik >= TWIbejovoHossz)
			{
				TWIujbejovo = 1;
			}
			break;
		}
		case 0x88://Previously addressed with own SLA+W; data received; NOT ACK  returned
		{

			break;
		}
		case 0x90://Previously addressed with general call; data received; ACK  returned
		{

			break;
		}
		case 0x98://Previously addressed with general call; data received; NOT ACK returned
		{

			break;
		}
		case 0xA0://A STOP condition or repeated START condition received while still addressed as Slave
		{

			break;
		}
		//////////////////////SLAVE TRANSMITTER///////////////////////
		case 0xA8://Own SLA+R received; ACK returned
		{

			if(TWIbejovo[0] > 1)
			{
				char behossz = TWIbejovoHossz;
				char beuzenet[behossz];
				int i;
				for(i = 0; i < behossz; ++i)
				{
					beuzenet[i] = TWIbejovo[i];
				}
				switch(TWIbejovo[1])
				{
					case 30://i2C Kocka hozzáadás
					{
						TWIkuldjvissza = 1;
						unsigned short crc_Be = beuzenet[behossz-2] | (beuzenet[behossz-1] << 8);
						unsigned short crc16 = Crc16Szamolo(beuzenet, behossz - 2);

						if(crc16 == crc_Be)
						{
							SajatRFAzonosito = beuzenet[2] | (beuzenet[3] << 8);
							SajatAzonositoMentendoFlag = 1;

							char kd[5];
							kd[0] = TWIkuldendoHossz = 5;
							kd[1] = 31;//sikeres i2C hozzaadas
							kd[2] = SajatKockaTipus;//kocka típus
							unsigned short crc16c = Crc16Szamolo(kd, 3);
							kd[3] = crc16c & 0x00FF;
							kd[4] = (crc16c & 0xFF00)>>8;

							int i = 0;
							for(;i<TWIkuldendoHossz;++i)
							{
								TWIkuldendo[i] = kd[i];
							}

							TWIkuldendoHanyadik = 1;
							TWDR = TWIkuldendo[0];
						}
						else
						{
							TWIkuldjvissza = 1;
							TWIkuldendo[0] = TWIkuldendoHossz = 2;
							TWIkuldendo[1] = 33;//sikertelen i2C hozzaadas
							
							TWIkuldendoHanyadik = 1;
							TWDR = TWIkuldendo[0];
						}

						break;
					}
				}
			}
			break;
		}
		case 0xB0://Arbitration lost in SLA+R/W as Master; own SLA+R received; ACK returned
		{

			break;
		}
		case 0xB8://Data byte in TWDR transmitted; ACK received
		{
			if(TWIkuldjvissza)
			{
				if(TWIkuldendoHanyadik < TWIkuldendoHossz)
				{
					TWDR = TWIkuldendo[TWIkuldendoHanyadik];

					if(TWIkuldendoHanyadik == TWIkuldendoHossz -1)
					{
						twi_setNack();
					}
					++TWIkuldendoHanyadik;
				}
				else
				{
					TWDR=255;
				}
			}
			else
			{
				TWDR=255;
			}
			break;
		}
		case 0xC0://Data byte in TWDR transmitted; NOT ACK received
		{

			break;
		}
		case 0xC8://Last data byte in TWDR transmitted (TWEA = “0”); ACK received
		{
			twi_setAck();
			break;
		}
	}

	TWCR |= 0x80;// Clear interrupt flag bit
}

#pragma endregion TWI kezeles