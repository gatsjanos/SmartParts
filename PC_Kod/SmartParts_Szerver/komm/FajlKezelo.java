import java.util.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FajlKezelo
{
	public static Map<String, KodFeldolgozo> FolyamatBeolvas()// kodok.kdk///Elsõ sor: fájlverzió
	{
		Map<String, KodFeldolgozo> FolyamatokMap = new HashMap<String, KodFeldolgozo>();
		BufferedReader beReader = null;
		try
		{
			beReader = new BufferedReader(new InputStreamReader(
					new FileInputStream("lstk" + System.getProperty("file.separator") + "kodok.kdk"), "Cp1250"));

			String buffSor;

			buffSor = beReader.readLine();
			if(buffSor == null)
			{
				beReader.close();
				return FolyamatokMap;
			}

			int fajlverzio = Integer.parseInt(buffSor);
			String kod = "", esemeny = "", nev = "";
			boolean nemTamFajlverz = false;
			while(true)
			{
				buffSor = beReader.readLine();
				if(buffSor == null)
					break;
				try
				{

					switch(fajlverzio)
					{
						case 1:
						{
							if(buffSor.length() > 7)
							{
								if(buffSor.charAt(0) == '<' && buffSor.charAt(1) == 'f' && buffSor.charAt(2) == 'e'
										&& buffSor.charAt(3) == 'j' && buffSor.charAt(4) == 'l'
										&& buffSor.charAt(5) == 'é' && buffSor.charAt(6) == 'c'
										&& buffSor.charAt(7) == '>')
								{
									String[] SplitBuff = buffSor.split(":");
									nev = SplitBuff[1];
									esemeny = SplitBuff[2].substring(0, SplitBuff[2].length() - 1);
									break;
								}
							}

							if(buffSor.equals("<>#&@[]~¡^¢°²`ÿ´//lezáró karakterlánc"))//Minden kód utolsó sora után be kell illeszteni a fájlba
							{
								FolyamatokMap.put(nev, new KodFeldolgozo(esemeny, kod, nev));

								nev = "";
								esemeny = "";
								kod = "";
							}
							else
								kod += buffSor + "\n";

							break;
						}

						default:
						{
							nemTamFajlverz = true;
							throw new Exception("Nem támogatott fájlverzió.");
						}
					}
				}
				catch (Exception e)
				{
					if(nemTamFajlverz)
						throw new Exception("Nem támogatott fájlverzió.");
					else
						System.out.println("HIBA: " + e);
				}
			}
			beReader.close();

			return FolyamatokMap;

		}
		catch (Exception e)
		{
			try
			{
				beReader.close();
			}
			catch (Exception ex)
			{}
			System.out.println("HIBA: " + e.getMessage());
		}

		return FolyamatokMap;
	}

	public static Map<String, Port> PortBeolvas()// Portok.prtk///<Név>,<Típus>,<Azonosító>////Elsõ sor: fájlverzió
	{
		Map<String, Port> PortokMap = new HashMap<String, Port>();

		BufferedReader beReader = null;
		try
		{
			beReader = new BufferedReader(new InputStreamReader(
					new FileInputStream("lstk" + System.getProperty("file.separator") + "portok.prtk"), "Cp1250"));

			String buffSor;

			buffSor = beReader.readLine();
			if(buffSor == null)
			{
				beReader.close();
				return PortokMap;
			}

			int fajlverzio = Integer.parseInt(buffSor);

			while(true)
			{
				buffSor = beReader.readLine();
				if(buffSor == null)
					break;

				switch(fajlverzio)
				{
					case 1:
					{
						String[] SplitBuff = buffSor.split(",");

						PortokMap.put(SplitBuff[0], new Port(PortTipus.valueOf(SplitBuff[1]),
								Integer.parseInt(SplitBuff[2]), SplitBuff[0]));
						break;
					}

					default:
					{
						throw new Exception("Nem támogatott fájlverzió.");
					}
				}
			}
			beReader.close();

			return PortokMap;

		}
		catch (Exception e)
		{
			try
			{
				beReader.close();
			}
			catch (Exception ex)
			{}
			System.out.println("HIBA: " + e.getMessage());
		}

		return PortokMap;
	}

	public static boolean FolyamatKiir(Map<String, KodFeldolgozo> Folybe)// kodok.kdk///Elsõ sor: fájlverzió
	{
		PrintWriter pwriter = null;
		for(int i = 0; i < 3; ++i)
		{
			try
			{
				if(!Files.exists(Paths.get("lstk")))
				{
					Files.createDirectory(Paths.get("lstk"));
				}
				
				pwriter = new PrintWriter("lstk" + System.getProperty("file.separator") + "kodok.kdk", "Cp1250");

				pwriter.println(FoClass.FolyamatokFajlverz);

				for(String item : Folybe.keySet())
				{
					pwriter.println("<fejléc>:" + Folybe.get(item).Nev + ":" + Folybe.get(item).Esemeny + ";");
					String[] sorok = Folybe.get(item).KodBe.split("\n");
					for(String sor : sorok)
					{
						pwriter.println(sor);
					}
					pwriter.println("<>#&@[]~¡^¢°²`ÿ´//lezáró karakterlánc");
				}
				pwriter.close();
				return true;
			}
			catch (Exception e)
			{
				try
				{
					pwriter.close();
				}
				catch (Exception ex)
				{}
				System.out.println("HIBA: " + e.getMessage());
			}
		}

		return false;
	}

	public static boolean PortKiir(Map<String, Port> Portbe)// portok.prtk///<Név>,<Típus>,<Azonosító>////Elsõ sor: fájlverzió
	{
		PrintWriter pwriter = null;
		for(int i = 0; i < 3; ++i)
		{
			try
			{
				if(!Files.exists(Paths.get("lstk")))
				{
					Files.createDirectory(Paths.get("lstk"));
				}
				
				pwriter = new PrintWriter("lstk" + System.getProperty("file.separator") + "portok.prtk", "Cp1250");

				pwriter.println(FoClass.PortokFajlverz);

				for(String item : Portbe.keySet())
				{
					pwriter.println(Portbe.get(item).Nev + "," + Portbe.get(item).getPortTipusa().toString() + ","
							+ Portbe.get(item).RFAzonosito);
				}
				pwriter.close();
				return true;
			}
			catch (Exception e)
			{
				try
				{
					pwriter.close();
				}
				catch (Exception ex)
				{}
				System.out.println("HIBA: " + e.getMessage());
			}
		}

		return false;
	}
}
