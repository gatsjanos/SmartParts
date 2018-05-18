import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.swing.JOptionPane;
import jssc.SerialPortException;

public class AVRCom
{
	static int SWatchUARTKuldottTimeoutms = 45;
	static int SmartPartsRFAzonosito = 0xABCD;

	public static boolean Kapcsol(Port Portbe)
	{
		System.out.print("--AVRCOM: KAPCSOL: '" + Portbe.Nev + "': '" + Portbe.Ertek + "'");
		try
		{
			List<Short> kuldendo = new ArrayList<Short>();
			switch(Portbe.getPortTipusa())
			{
				case KIRele:
				{
					kuldendo.add((short) 13);//UART uzenet hossz (+2 CRC)
					kuldendo.add((short) 13);//hossz megegyszer

					kuldendo.add((short) 10);//RF k�ld�s k�dja

					kuldendo.add((short) (SmartPartsRFAzonosito & 0x00FF));//uzenet...
					kuldendo.add((short) ((SmartPartsRFAzonosito & 0xFF00) >> 8));

					kuldendo.add((short) 8);
					kuldendo.add((short) 8);

					short felado = 0x001;//12bit
					kuldendo.add((short) ((Portbe.RFAzonosito & 0x0FF0) >> 4));
					kuldendo.add((short) (((Portbe.RFAzonosito & 0x000F) << 4) | ((felado & 0x0F00) >> 8)));
					kuldendo.add((short) (felado & 0x00FF));

					if((int) Portbe.Ertek == 0 || (int) Portbe.Ertek == 1)
					{//Bekapcs, vagy kikapcs
						kuldendo.add((short) (int) Portbe.Ertek);
					}
					else
					{//Periodikus
						kuldendo.add((short) (((int) Portbe.Ertek / 100) + 2));//+2: eltolja a 0 �s 1 �rt�kekr�l
					}

					int crc16 = CRC.Crc16Szamol(kuldendo);

					kuldendo.add((short) (crc16 & 0x00FF));
					kuldendo.add((short) ((crc16 & 0xFF00) >> 8));
				}
			}

			RFKuldesMajdNyugtavaras(kuldendo);//Exception-t dob

			System.out.println(" --Sikeres");
		}
		catch (Exception e)
		{
			System.out.println(" --SikerTELEN: " + e.getMessage());
			//			e.printStackTrace();
			return false;
		}

		return true;
	}

	public static boolean PortErtekFrissit(Port Portbe)
	{
		//Portt�pust�l f�gg�en vagy azonnali v�laszt k�r (k�d: 12), vagy nyugtav�r�sos m�r�st ind�t meg (k�d: 10), amire esem�nyben j�n v�lasz
		return true;
	}

	public static void KockaHozzaadI2C(Port Portbe)
	{
		System.out.print("--AVRCOM: i2C Hozz�ad: '" + Portbe.Nev);
		try
		{
			if(FoClass.PortokMap.size() > 4095)
			{
				System.out.println("--APPLICATION: i2C: Nem adhat� hozz� t�bb kocka!");
				return;
			}

			Random r = new Random();
			int RFID = 4095;

			boolean foglalt = true;
			while(foglalt)
			{
				foglalt = false;
				RFID = r.nextInt(4096);

				if(RFID == 0 || RFID == 1)//0: General call   1: K�zpont
				{
					foglalt = true;
					continue;
				}

				for(String item : FoClass.PortokMap.keySet())
				{
					if(RFID == FoClass.PortokMap.get(item).RFAzonosito)
					{
						foglalt = true;
						break;
					}
				}
			}

			List<Short> kuldendo = new ArrayList<Short>();
			kuldendo.add((short) 7);//hossz
			kuldendo.add((short) 7);//hossz2

			kuldendo.add((short) 30);//I2C hozz�ad�s k�dja

			kuldendo.add((short) (RFID & 0x00FF));
			kuldendo.add((short) ((RFID & 0xFF00) >> 8));

			int crc16 = CRC.Crc16Szamol(kuldendo);

			kuldendo.add((short) (crc16 & 0x00FF));
			kuldendo.add((short) ((crc16 & 0xFF00) >> 8));

			I2CKockaHozzaadasKuldes(kuldendo, Portbe.Nev, RFID);
			System.out.println(" --Sikeres");
		}
		catch (Exception e)
		{
			System.out.println(" --SikerTELEN: " + e.getMessage());
			//e.printStackTrace();

			if(Portbe.SegedErtek < 2)//hozz�ad�s �jrapr�b�l�sa
			{
				FoClass.SegedSzalTH.Kapcsolandok.add(new UARTPortMuvelet(new Port(Portbe.Nev, Portbe.SegedErtek + 1),
						UARTPortMuveletTipus.HozzaadI2C));
			}
		}
	}

	private static void I2CKockaHozzaadasKuldes(List<Short> kuldendo, String Portnev, int RFID)
			throws Exception
	{
		////////////////////////////////////UART ODA/////////////////////////////////////
		int varidoms = 150;
		int i = 0;
		boolean NyugtaHelyes = false;
		try
		{
			for(; i < 5 && !NyugtaHelyes; ++i)
			{
				FoClass.SorosPortAVR.sp.readBytes();
				FoClass.SorosPortAVR.FogadottByteok.clear();
				for(short item : kuldendo)
				{
					FoClass.SorosPortAVR.WriteByte(item);
				}
				int n = 0;
				for(; n < SWatchUARTKuldottTimeoutms / 4 && FoClass.SorosPortAVR.FogadottByteok.size() < 2; ++n)
				{
					Thread.sleep(4);
				}

				if(n < SWatchUARTKuldottTimeoutms / 4)
				{
					if(FoClass.SorosPortAVR.FogadottByteok.size() >= 2)
					{
						if(FoClass.SorosPortAVR.FogadottByteok.get(0) == 9)
						{
							if(FoClass.SorosPortAVR.FogadottByteok.get(1) == 1)
							{
								NyugtaHelyes = true;
							}
							else
							{
								varidoms *= new Random().nextDouble() + 1.1;//random n�vekv� v�rakoz�si id� m�lva k�ldi �jra
								Thread.sleep(varidoms);
							}
						}
					}
				}
			}
		}
		catch (Exception e)
		{
			++FoClass.SorosPortAVR.HibaSzamlalo;
			throw e;
		}
		if(i >= 5)
		{
			++FoClass.SorosPortAVR.HibaSzamlalo;
			throw new Exception("UART Kimen� id�t�ll�p�s.");
		}
		else
		{
			FoClass.SorosPortAVR.HibaSzamlalo = 0;
		}

		//////////////////////////////////////UART VISSZA/////////////////////////////////////
		int varakozasido = 400;

		boolean hibasuzenet = true;
		for(i = 0; i < 5 && hibasuzenet; ++i)
		{
			FoClass.SorosPortAVR.sp.readBytes();
			FoClass.SorosPortAVR.FogadottByteok.clear();
			int n = 0;
			for(; n < varakozasido / 4 && FoClass.SorosPortAVR.FogadottByteok.size() < 4; ++n)
			{
				Thread.sleep(4);
			}
			if(n < varakozasido / 4)
			{
				if(FoClass.SorosPortAVR.FogadottByteok.get(0) == 35)//sikeres i2C
				{
					List<Short> uzenetbe = new ArrayList<Short>();

					for(int k = 0; k < 2; ++k)
					{
						uzenetbe.add(FoClass.SorosPortAVR.FogadottByteok.get(k));
					}

					int crcBe = FoClass.SorosPortAVR.FogadottByteok.get(2)
							| (FoClass.SorosPortAVR.FogadottByteok.get(3) << 8);

					int crc16v = CRC.Crc16Szamol(uzenetbe);

					if(crcBe == crc16v)
					{
						FoClass.SorosPortAVR.WriteByte((short) 4);
						FoClass.SorosPortAVR.WriteByte((short) 4);
						FoClass.SorosPortAVR.WriteByte((short) 19);
						FoClass.SorosPortAVR.WriteByte((short) 1);
						PortTipus tipus = PortTipus.KIRele;

						switch(uzenetbe.get(1))
						{
							case 1:
							{
								tipus = PortTipus.KIRele;
								break;
							}

						}

						FoClass.PortokMap.put(Portnev, new Port(tipus, RFID, Portnev));
						synchronized(FoClass.SegedSzalTH)
						{
							FoClass.SegedSzalTH.PortMentes = true;
							FoClass.SegedSzalTH.notify();
						}
						hibasuzenet = false;
					}
					else
					{
						FoClass.SorosPortAVR.WriteByte((short) 4);
						FoClass.SorosPortAVR.WriteByte((short) 4);
						FoClass.SorosPortAVR.WriteByte((short) 19);
						FoClass.SorosPortAVR.WriteByte((short) 0);//hib�s bej�v�
						
						hibasuzenet = true;
						varakozasido = SWatchUARTKuldottTimeoutms;
					}
				}
				else if(FoClass.SorosPortAVR.FogadottByteok.get(0) == 37)//sikertelen id�t�ll�p�s i2C
				{
					FoClass.SorosPortAVR.WriteByte((short) 4);
					FoClass.SorosPortAVR.WriteByte((short) 4);
					FoClass.SorosPortAVR.WriteByte((short) 19);
					FoClass.SorosPortAVR.WriteByte((short) 1);

					throw new Exception("i2C kommunik�ci�s hiba. Id�t�ll�p�s -> A kocka nincs csatlakoztatva.");
				}
				else if(FoClass.SorosPortAVR.FogadottByteok.get(0) == 38)//sikertelen CRC Vissza hiba i2C
				{
					FoClass.SorosPortAVR.WriteByte((short) 4);
					FoClass.SorosPortAVR.WriteByte((short) 4);
					FoClass.SorosPortAVR.WriteByte((short) 19);
					FoClass.SorosPortAVR.WriteByte((short) 1);

					throw new Exception("i2C kommunik�ci�s hiba. Helytelen CRC Vissza.");
				}
				else if(FoClass.SorosPortAVR.FogadottByteok.get(0) == 39)//sikertelen CRC El hiba i2C
				{
					FoClass.SorosPortAVR.WriteByte((short) 4);
					FoClass.SorosPortAVR.WriteByte((short) 4);
					FoClass.SorosPortAVR.WriteByte((short) 19);
					FoClass.SorosPortAVR.WriteByte((short) 1);

					throw new Exception("i2C kommunik�ci�s hiba. Helytelen CRC El.");
				}
				else
				{
					FoClass.SorosPortAVR.WriteByte((short) 4);
					FoClass.SorosPortAVR.WriteByte((short) 4);
					FoClass.SorosPortAVR.WriteByte((short) 19);
					FoClass.SorosPortAVR.WriteByte((short) 0);//hib�s bej�v�

					hibasuzenet = true;
					varakozasido = SWatchUARTKuldottTimeoutms;
				}
			}
			else
			{
				throw new Exception("UART Bej�v� id�t�ll�p�s.");
			}
		}
		if(i >= 5)
		{
			throw new Exception("UART Bej�v� CRC hiba.");
		}
	}

	private static boolean RFKuldesMajdNyugtavaras(List<Short> kuldendo) throws Exception
	{
		////////////////////////////////////UART ODA/////////////////////////////////////
		int varidoms = 150;
		int i = 0;
		boolean NyugtaHelyes = false;
		try
		{
			for(; i < 5 && !NyugtaHelyes; ++i)
			{
				FoClass.SorosPortAVR.sp.readBytes();
				FoClass.SorosPortAVR.FogadottByteok.clear();
				for(short item : kuldendo)
				{
					FoClass.SorosPortAVR.WriteByte(item);
				}
				int n = 0;
				for(; n < SWatchUARTKuldottTimeoutms / 4 && FoClass.SorosPortAVR.FogadottByteok.size() < 2; ++n)
				{
					Thread.sleep(4);
				}

				if(n < SWatchUARTKuldottTimeoutms / 4)
				{
					if(FoClass.SorosPortAVR.FogadottByteok.size() >= 2)
					{
						if(FoClass.SorosPortAVR.FogadottByteok.get(0) == 9)
						{
							if(FoClass.SorosPortAVR.FogadottByteok.get(1) == 1)
							{
								NyugtaHelyes = true;
							}
							else
							{
								varidoms *= new Random().nextDouble() + 1.1;//random n�vekv� v�rakoz�si id� m�lva k�ldi �jra
								Thread.sleep(varidoms);
							}
						}
					}
				}
			}
		}
		catch (Exception e)
		{
			++FoClass.SorosPortAVR.HibaSzamlalo;
			throw e;
		}
		if(i >= 5)
		{
			++FoClass.SorosPortAVR.HibaSzamlalo;
			throw new Exception("UART Kimen� id�t�ll�p�s.");
		}
		else
		{
			FoClass.SorosPortAVR.HibaSzamlalo = 0;
		}

		//////////////////////////////////////UART VISSZA/////////////////////////////////////
		int varakozasido = (int) (((((double) 1000 / ((double) 7372800 / ((double) 256
				* 24/* 24: A v�laszthat� leglassabb RF bitr�ta kateg�ria */))) * 16 * (kuldendo.size() + 6)) + 30)
				* (double) 14);

		boolean hibasuzenet = true;
		for(i = 0; i < 5 && hibasuzenet; ++i)
		{
			FoClass.SorosPortAVR.sp.readBytes();
			FoClass.SorosPortAVR.FogadottByteok.clear();
			int n = 0;
			for(; n < varakozasido / 4 && FoClass.SorosPortAVR.FogadottByteok.size() < 1; ++n)
			{
				Thread.sleep(4);
			}

			if(n < varakozasido / 4)
			{
				if(FoClass.SorosPortAVR.FogadottByteok.get(0) == 50)//sikeres RF
				{
					hibasuzenet = false;

					FoClass.SorosPortAVR.WriteByte((short) 4);
					FoClass.SorosPortAVR.WriteByte((short) 4);
					FoClass.SorosPortAVR.WriteByte((short) 19);
					FoClass.SorosPortAVR.WriteByte((short) 1);
				}
				else if(FoClass.SorosPortAVR.FogadottByteok.get(0) == 53)//sikertelen RF
				{
					hibasuzenet = false;

					FoClass.SorosPortAVR.WriteByte((short) 4);
					FoClass.SorosPortAVR.WriteByte((short) 4);
					FoClass.SorosPortAVR.WriteByte((short) 19);
					FoClass.SorosPortAVR.WriteByte((short) 1);
					throw new Exception("RF kommunik�ci�s hiba.");
				}
				else
				{
					FoClass.SorosPortAVR.WriteByte((short) 4);
					FoClass.SorosPortAVR.WriteByte((short) 4);
					FoClass.SorosPortAVR.WriteByte((short) 19);
					FoClass.SorosPortAVR.WriteByte((short) 0);//hib�s bej�v�

					hibasuzenet = true;
					varakozasido = SWatchUARTKuldottTimeoutms;
				}
			}
			else
			{
				throw new Exception("UART Bej�v� id�t�ll�p�s.");
			}
		}
		if(i >= 5)
		{
			throw new Exception("UART Bej�v� CRC hiba.");
		}
		return true;
	}

	static short ByteElojelEltavolit(byte be)
	{
		if(be < 0)
		{
			return (short) ((int) 256 + (int) be);
		}
		else
		{
			return be;
		}
	}
}
