import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class Eszkozok
{

	public static void UjrainditApp()
	{
		try
		{
			if(isWindows(System.getProperty("os.name").toLowerCase()))
			{
				System.out.println("Windows");
				Runtime.getRuntime().exec("SzerUjraindWin.bat");
			}
			else if(isMac(System.getProperty("os.name").toLowerCase()))
			{
				System.out.println("Mac");
			}
			else if(isUnix(System.getProperty("os.name").toLowerCase()))
			{
				System.out.println("Unix / Linux");
				//TODO Szerver Újraindítás Linuxon
			}
			else if(isSolaris(System.getProperty("os.name").toLowerCase()))
			{
				System.out.println("Solaris");
			}

			System.exit(0);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public static boolean isWindows(String OS)
	{

		return (OS.indexOf("win") >= 0);

	}

	public static boolean isMac(String OS)
	{

		return (OS.indexOf("mac") >= 0);

	}

	public static boolean isUnix(String OS)
	{

		return (OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0 || OS.indexOf("aix") > 0);

	}

	public static boolean isSolaris(String OS)
	{

		return (OS.indexOf("sunos") >= 0);

	}

	public static Image getScaledImage(Image srcImg, int w, int h)
	{
		BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = resizedImg.createGraphics();

		g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g2.drawImage(srcImg, 0, 0, w, h, null);
		g2.dispose();

		return resizedImg;
	}

	public static boolean IgyKezdodik(String karlanc, List<Character> KodList, int elsoKarakterIndexe)
	{
		if(KodList.size() < elsoKarakterIndexe + karlanc.length())
			return false;
		
		for(int i = 0; i < karlanc.length(); ++i)
		{
			if(KodList.get(elsoKarakterIndexe + i) != karlanc.charAt(i))
				return false;
		}
		
		return true;
	}
	
	public static String StringEscape(String be)
	{
		String ki = "";

		for(int i = 0; i < be.length(); ++i)
		{
			if(be.charAt(i) == '\\')
			{
				if(i + 1 < be.length())
				{
					switch(be.charAt(i + 1))
					{
						case '\\':
						{
							ki += '\\';
							break;
						}
						case '\"':
						{
							ki += '\"';
							break;
						}
						case '\'':
						{
							ki += '\'';
							break;
						}
						case 'n':
						{
							ki += '\n';
							break;
						}
						case 't':
						{
							ki += '\t';
							break;
						}
						case 'r':
						{
							ki += '\r';
							break;
						}
						case 'b':
						{
							ki += '\b';
							break;
						}
						case 'f':
						{
							ki += '\f';
							break;
						}
					}
					++i;
				}
			}
			else
			{
				ki += be.charAt(i);
			}
		}

		return ki;
	}

	public static boolean ContainsStringNEMSZOVEGBEN(List<Character> kifejezes, String tartalmazando)//igaz, ha a bemenõ kifejezés tartalmazza a bemenõ karakterláncot és ez a tartalmazás nem egy függvényhívás String paraméterében van
	{
		String stringkif = "";
		for(char item : kifejezes)
		{
			stringkif += item;
		}
		return ContainsStringNEMSZOVEGBEN(stringkif, tartalmazando);
	}

	public static boolean ContainsStringNEMSZOVEGBEN(String kifejezés, String tartalmazando)//igaz, ha a bemenõ kifejezés tartalmazza a bemenõ karakterláncot és ez a tartalmazás nem egy függvényhívás String paraméterében van
	{
		boolean Stringbenvan = false;
		String RedukaltKifejezes = "";
		for(int i = 0; i < kifejezés.length(); ++i)
		{
			if(kifejezés.charAt(i) == '"')
			{

				if(!Stringbenvan)
				{
					Stringbenvan = true;
				}
				else if(Stringbenvan && kifejezés.charAt(i - 1) != '\\')
				{
					Stringbenvan = false;
					continue;
				}
			}

			if(!Stringbenvan)
			{
				RedukaltKifejezes += kifejezés.charAt(i);
			}

		}
		return RedukaltKifejezes.contains(tartalmazando);
	}

	public static List<String> OsszetettStringParameterSzetszedo(String parameter)
	{
		List<String> Osszetevok = new ArrayList<String>();

		String osszebuff = "";
		boolean Stringbenvan = false;
		int zarojelszam = 0;
		for(int i = 0; i < parameter.length(); ++i)
		{
			if(parameter.charAt(i) == '"')
			{

				if(!Stringbenvan)
				{
					Stringbenvan = true;
				}
				else if(Stringbenvan && parameter.charAt(i - 1) != '\\')
				{
					Stringbenvan = false;
				}
			}

			if(!Stringbenvan)
			{

				if(zarojelszam == 0 && parameter.charAt(i) == '+')
				{
					Osszetevok.add(osszebuff);
					osszebuff = "";
					continue;
				}

				if(parameter.charAt(i) == '(')
				{
					++zarojelszam;
				}
				else if(parameter.charAt(i) == ')')
				{
					--zarojelszam;
				}
			}

			osszebuff += parameter.charAt(i);
		}
		Osszetevok.add(osszebuff);

		//		System.out.println("--KODFELDOLGOZO:OSSZETETTSTRINGPARAMETERSZETSZEDO//////////////////////////////");
		//		System.out.println("--/////////////////////////////////////////////////////////////////////////////");
		//
		//		for(String item : Osszetevok)
		//		{
		//			System.out.println("------->" + item);
		//		}
		//		System.out.println("--/////////////////////////////////////////////////////////////////////////////");

		return Osszetevok;
	}

	public static String StringParameterFeldolgozo(String be, KodFeldolgozo KF) throws Exception
	{
		List<String> Osszetevok = OsszetettStringParameterSzetszedo(be);

		String kimenet = "";

		for(String item : Osszetevok)
		{
			if(item.startsWith("\""))
			{
				kimenet += StringEscape(ParameterSzelerolIdezojelLeszedo(item));
			}
			else
			{
				Kiftip buff = KodFeldolgozo.ErtekLekero(item, KF);

				if(buff.Stringe)
				{
					kimenet += buff.Ertek;
				}
				else
				{
					kimenet += DecFormat((double) buff.Ertek);
				}
			}
		}

		return kimenet;
	}

	public static String ParameterSzelerolIdezojelLeszedo(String be)
	{
		if(be.charAt(0) == '"')
			be = be.substring(1);

		if(be.length() > 1)
		{
			if(be.charAt(be.length() - 1) == '"' && be.charAt(be.length() - 2) != '\\')
				be = be.substring(0, be.length() - 1);
		}
		return be;
	}

	public static List<String> ParameterSzetvalaszto(String parameter)
	{
		List<String> Parameterek = new ArrayList<String>();

		String osszebuff = "";
		boolean Stringbenvan = false;
		int zarojelszam = 0;
		for(int i = 0; i < parameter.length(); ++i)
		{
			if(parameter.charAt(i) == '"')
			{

				if(!Stringbenvan)
				{
					Stringbenvan = true;
				}
				else if(Stringbenvan && parameter.charAt(i - 1) != '\\')
				{
					Stringbenvan = false;
				}
			}

			if(!Stringbenvan)
			{

				if(zarojelszam == 0 && parameter.charAt(i) == ',')
				{
					Parameterek.add(osszebuff);
					osszebuff = "";
					continue;
				}

				if(parameter.charAt(i) == '(')
				{
					++zarojelszam;
				}
				else if(parameter.charAt(i) == ')')
				{
					--zarojelszam;
				}
			}

			osszebuff += parameter.charAt(i);
		}
		Parameterek.add(osszebuff);

		return Parameterek;
	}

	public static String FgvHivasbolParamKiszedo(String esemeny) throws Exception
	{
		return FgvHivasbolParamKiszedo(esemeny, 0).EString;
	}

	public static Epar FgvHivasbolParamKiszedo(List<Character> kifejezes, int Ibe) throws Exception
	{
		return FgvHivasbolParamKiszedo(CharlistbolString(kifejezes), Ibe);
	}

	public static Epar FgvHivasbolParamKiszedo(String esemeny, int Ibe) throws Exception
	{
		return ZarojelBlokkKiszedo(esemeny, Ibe, '(', ')');
	}

	public static Epar KapcsosZarojelBlokkKiszedo(List<Character> kifejezes, int Ibe) throws Exception
	{
		return KapcsosZarojelBlokkKiszedo(CharlistbolString(kifejezes), Ibe);
	}

	public static Epar KapcsosZarojelBlokkKiszedo(String esemeny, int Ibe) throws Exception
	{
		return ZarojelBlokkKiszedo(esemeny, Ibe, '{', '}');
	}

	public static Epar ZarojelBlokkKiszedo(String esemeny, int Ibe, char pluszchar, char minuszchar) throws Exception
	{
		for(; Ibe < esemeny.length(); ++Ibe)
		{
			if(esemeny.charAt(Ibe) == pluszchar)
			{
				++Ibe;
				break;
			}
		}

		String Parameter = "";

		boolean Stringbenvan = false;
		int zarojelszam = 1;
		for(;; ++Ibe)
		{
			if(esemeny.charAt(Ibe) == '"')
			{

				if(!Stringbenvan)
				{
					Stringbenvan = true;
				}
				else if(Stringbenvan && esemeny.charAt(Ibe - 1) != '\\')
				{
					Stringbenvan = false;
				}
			}
			if(!Stringbenvan)
			{
				if(esemeny.charAt(Ibe) == pluszchar)
					++zarojelszam;
				else if(esemeny.charAt(Ibe) == minuszchar)
					--zarojelszam;

				if(zarojelszam == 0)
					break;
			}
			Parameter += esemeny.charAt(Ibe);
		}

		return new Epar(Parameter, Ibe);
	}

	public static List<Character> StringbolCharlist(String s)
	{
		List<Character> lista = new ArrayList<Character>();

		for(int i = 0; i < s.length(); ++i)
		{
			lista.add(s.charAt(i));
		}

		return lista;
	}

	public static String CharlistbolString(List<Character> lista)
	{
		String stringkif = "";
		for(char item : lista)
		{
			stringkif += item;
		}

		return stringkif;
	}

	public static String EsemenybolPortKiszedo(String esemeny) throws Exception
	{
		String Parameter = FgvHivasbolParamKiszedo(esemeny);
		Parameter = Eszkozok.ParameterSzelerolIdezojelLeszedo(Parameter);

		return Parameter;
	}
	
	public static HashMap<String, Double> DeepCopyValtozokMap(HashMap<String, Double> be)
	{
		HashMap<String, Double> ki = new HashMap<String, Double>();
		for(String item: be.keySet())
		{
			ki.put(item, be.get(item));	
		}
		return ki;
	}
	
	public static List<Character> KodElokeszitoVegrehajtasra(String kodbe, KodFeldolgozo meghivopeldany)
	{
		List<Character> KodList = new ArrayList<Character>();

		KodList.add('{');//Hogy indexelésnél az egyel kisebb indexbõl ne legyen Exception

		for(int i = 0; i < kodbe.length(); ++i)
		{
			KodList.add(kodbe.charAt(i));
		}

		KodList.add('}');//index Exception miatti zárójel párja
		
		boolean Stringbenvan = false;

		for(int i = 0; i < KodList.size(); ++i)
		{
			if(meghivopeldany.AlljmegFlag)
			{
				break;
			}
			if(KodList.size() > i + 7)
			{
				if(KodList.get(i) == '<' && KodList.get(i + 1) == 'f' && KodList.get(i + 2) == 'e'
						&& KodList.get(i + 3) == 'j' && KodList.get(i + 4) == 'l' && KodList.get(i + 5) == 'é'
						&& KodList.get(i + 6) == 'c' && KodList.get(i + 7) == '>')
				{
					while(KodList.get(i) != ';')
					{
						KodList.remove(i);
					}
					KodList.remove(i);
				}
			}

			if(KodList.get(i) == '"')
			{

				if(Stringbenvan)
				{
					if(KodList.get(i - 1) != '\\')
					{
						Stringbenvan = false;
					}
					else
					{}
				}
				else
				{
					Stringbenvan = true;
				}
			}

			if(!Stringbenvan)
			{
				if(KodList.size() > i + 1)
				{
					if(KodList.get(i) == '/' && KodList.get(i + 1) == '/')
					{
						while(KodList.size() > i && KodList.get(i) != '\n')
						{
							KodList.remove(i);
						}
					}
				}
				if(KodList.size() > i && (KodList.get(i) == ' ' || KodList.get(i) == '\t' || KodList.get(i) == '\n'
						|| KodList.get(i) == '\r'))
				{
					KodList.remove(i);
					--i;
				}
			}
		}
		
		return KodList;
	}

	public static String DecFormat(double d)//minimális, de legnagyobb pontosságú tizedesjegyet ír (elhagyja a fölös 0-kat)
	{
		if(d == (long) d)
			return String.format("%d", (long) d);
		else
			return String.format("%s", d);
	}
}
