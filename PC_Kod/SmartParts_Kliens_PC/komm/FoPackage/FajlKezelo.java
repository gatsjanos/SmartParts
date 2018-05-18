package FoPackage;

import java.util.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FajlKezelo
{
	public static List<KliFolyamat> FolyamatBeolvas()// kodok.kdk///Els� sor: f�jlverzi�
	{
		List<KliFolyamat> FolyamatokList = new ArrayList<KliFolyamat>();
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
				return FolyamatokList;
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
										&& buffSor.charAt(5) == '�' && buffSor.charAt(6) == 'c'
										&& buffSor.charAt(7) == '>')
								{
									String[] SplitBuff = buffSor.split(":");
									nev = SplitBuff[1];
									esemeny = SplitBuff[2].substring(0, SplitBuff[2].length() - 1);
									break;
									//FolyamatokMap.put(SplitBuff[1], new Port(Port.GetEnumFromString(SplitBuff[1]), Integer.parseInt(SplitBuff[2])));
									//break;
								}
							}

							if(buffSor.equals("<>#&@[]~�^���`��//lez�r� karakterl�nc"))//Minden k�d utols� sora ut�n be kell illeszteni a f�jlba
							{
								FolyamatokList.add(new KliFolyamat(nev, esemeny, kod));

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
							throw new Exception("Nem t�mogatott f�jlverzi�.");
						}
					}
				}
				catch (Exception e)
				{
					if(nemTamFajlverz)
						throw new Exception("Nem t�mogatott f�jlverzi�.");
					else
						System.out.println("HIBA: " + e.getMessage());
				}
			}
			beReader.close();

			return FolyamatokList;

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

		return FolyamatokList;
	}

	public static boolean FolyamatKiir(List<KliFolyamat> Folybe)// kodok.kdk///Els� sor: f�jlverzi�
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

				for(KliFolyamat item : Folybe)
				{
					pwriter.println("<fejl�c>:" + item.Nev + ":" + item.Esemeny + ";");
					String[] sorok = item.Kod.split("\n");
					for(String sor : sorok)
					{
						pwriter.println(sor);
					}
					pwriter.println("<>#&@[]~�^���`��//lez�r� karakterl�nc");
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
