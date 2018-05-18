package FoPackage;
import java.util.*;
import FoPackage.Port;
import java.io.*;
import java.net.*;

public class TCPCom
{
	public static List<KliFolyamat> FolyamatLekero()
	{
		for(int i = 0; i < 3; ++i)// Próbálkozások
		{
			Socket skt = null;
			BufferedReader in = null;
			PrintWriter out = null;
			try
			{
				boolean UjraKezd = false;
				skt = new Socket(FoClass.TCPCim, FoClass.TCPPortSzam);
				in = new BufferedReader(new InputStreamReader(skt.getInputStream(), "Cp1250"));
				out = new PrintWriter(new OutputStreamWriter(skt.getOutputStream(), "Cp1250"), true);
				// System.out.print("Received string: ");

				out.println("Folyamatlekeres");

				List<String> UzenetBe = new ArrayList<String>();

				int hossz = 9999999;
				int x = 0;
				while(x < hossz)
				{
					String buff = (String) UzenetKovsorVar(in, 500);
					if(buff == null && (UzenetBe.size() > 0 || x > 2))// Az elsõ 3 próbálkozásnál nem baj,ha null jön vissza, de utána hibának veszi
					{
						UjraKezd = true;
						break;
					}
					else if(buff != null)
					{
						UzenetBe.add(buff);
						System.out.println(buff);
					}

					if(UzenetBe.size() == 2)
					{
						if(UzenetBe.get(0).equals(UzenetBe.get(1)))
						{
							hossz = Integer.parseInt(UzenetBe.get(0));
							x = 1;
						}
						else
						{
							UjraKezd = true;
							break;
						}
					}

					++x;
				}

				try
				{
					in.close();
				}
				catch (Exception e)
				{}
				try
				{
					out.close();
				}
				catch (Exception e)
				{}
				try
				{
					skt.close();
				}
				catch (Exception e)
				{}

				if(UjraKezd)
					continue;

				UzenetBe.remove(0);// Hossz Eltávolítása
				UzenetBe.remove(0);

				for(int a = 0; a + 1 < UzenetBe.size(); ++a)// Sorok
															// ellenõrzése
															// páronként
															// (CRC helyett)
				{
					if(!UzenetBe.get(a).equals(UzenetBe.get(a + 1)))
					{
						UjraKezd = true;
						break;
					}
					UzenetBe.remove(a);
				}
				if(UjraKezd)
					continue;

				List<KliFolyamat> FolyList = new LinkedList<KliFolyamat>();

				for(int a = 0; a + 3 < UzenetBe.size(); a += 4)// Objektummá
																// alakítás
				{
					FolyList.add(new KliFolyamat(UzenetBe.get(a), UzenetBe.get(a + 1),
							UzenetBe.get(a + 2).replace("²´¢/TCPUjs", "\n"),
							KliFolyamat.GetEnumFromString(UzenetBe.get(a + 3))));
				}

				TCPFrissitoSzal.TCPHibaszamlalo = 0;
				return FolyList;

			}
			catch (Exception e)
			{
				System.out.println("HIBA: " + (i + 1) + ": " + e.getMessage());
				try
				{
					in.close();
				}
				catch (Exception ex)
				{}
				try
				{
					out.close();
				}
				catch (Exception ex)
				{}
				try
				{
					skt.close();
				}
				catch (Exception ex)
				{}
			}
		}
		++TCPFrissitoSzal.TCPHibaszamlalo;
		return null;
	}

	public static boolean FolyamatTorlo(List<String> folyNevek)
	{
		for(int i = 0; i < 3; ++i)// Próbálkozások
		{
			Socket skt = null;
			BufferedReader in = null;
			PrintWriter out = null;
			try
			{
				boolean UjraKezd = false;
				skt = new Socket(FoClass.TCPCim, FoClass.TCPPortSzam);
				in = new BufferedReader(new InputStreamReader(skt.getInputStream(), "Cp1250"));
				out = new PrintWriter(new OutputStreamWriter(skt.getOutputStream(), "Cp1250"), true);
				// System.out.print("Received string: ");

				out.println("FolyamatTorles");

				List<String> UzenetKi = new ArrayList<String>();

				out.println(folyNevek.size() * 2 + 2);
				out.println(folyNevek.size() * 2 + 2);
				for(String item : folyNevek)
				{
					out.println(item);
					out.println(item);

					UzenetKi.add(item);
				}

				List<String> UzenetBe = new ArrayList<String>();

				int hossz = 9999999;
				int x = 0;
				while(x < hossz)
				{
					String buff = (String) UzenetKovsorVar(in, 500);
					if(buff == null && (UzenetBe.size() > 0 || x > 2))// Az elsõ 3 próbálkozásnál nem baj, ha null jön vissza, de utána hibának veszi
					{
						UjraKezd = true;
						break;
					}
					else if(buff != null)
					{
						UzenetBe.add(buff);
						System.out.println(buff);
					}

					if(UzenetBe.size() == 2)
					{
						if(UzenetBe.get(0).equals(UzenetBe.get(1)))
						{
							hossz = Integer.parseInt(UzenetBe.get(0));
							x = 1;
						}
						else
						{
							UjraKezd = true;
							break;
						}
					}

					++x;
				}

				try
				{
					in.close();
				}
				catch (Exception e)
				{}
				try
				{
					out.close();
				}
				catch (Exception e)
				{}
				try
				{
					skt.close();
				}
				catch (Exception e)
				{}

				if(UjraKezd)
					continue;

				UzenetBe.remove(0);// Hossz Eltávolítása
				UzenetBe.remove(0);

				for(int a = 0; a + 1 < UzenetBe.size(); ++a)// Sorok ellenõrzése páronként (CRC helyett)
				{
					if(!UzenetBe.get(a).equals(UzenetBe.get(a + 1)))
					{
						UjraKezd = true;
						break;
					}
					UzenetBe.remove(a);
				}
				if(UjraKezd)
					continue;

				for(int j = 0; j < UzenetKi.size(); j++)
				{
					if(!UzenetKi.get(j).equals(UzenetBe.get(j)))
					{
						UjraKezd = true;
						break;
					}
				}

				if(UjraKezd)
					continue;

				TCPFrissitoSzal.TCPHibaszamlalo = 0;
				return true;
			}
			catch (Exception e)
			{
				System.out.println("HIBA: " + (i + 1) + ": " + e.getMessage());
				try
				{
					in.close();
				}
				catch (Exception ex)
				{}
				try
				{
					out.close();
				}
				catch (Exception ex)
				{}
				try
				{
					skt.close();
				}
				catch (Exception ex)
				{}
			}
		}
		++TCPFrissitoSzal.TCPHibaszamlalo;
		return false;
	}

	public static boolean FolyamatHozzaad(List<KliFolyamat> folyamatok)
	{
		for(int i = 0; i < 3; ++i)// Próbálkozások
		{
			Socket skt = null;
			BufferedReader in = null;
			PrintWriter out = null;
			try
			{
				boolean UjraKezd = false;
				skt = new Socket(FoClass.TCPCim, FoClass.TCPPortSzam);
				in = new BufferedReader(new InputStreamReader(skt.getInputStream(), "Cp1250"));
				out = new PrintWriter(new OutputStreamWriter(skt.getOutputStream(), "Cp1250"), true);

				out.println("FolyamatHozzaadas");

				List<String> UzenetKi = new ArrayList<String>();

				out.println(folyamatok.size() * 3 * 2 + 2);
				out.println(folyamatok.size() * 3 * 2 + 2);

				for(KliFolyamat item : folyamatok)
				{
					out.println(item.Nev);
					out.println(item.Nev);
					out.println(item.Esemeny);
					out.println(item.Esemeny);
					out.println(item.Kod.replaceAll("\r", "").replace("\n", "²´¢/TCPUjs"));
					out.println(item.Kod.replaceAll("\r", "").replace("\n", "²´¢/TCPUjs"));

					UzenetKi.add(item.Nev);
					UzenetKi.add(item.Esemeny);
					UzenetKi.add(item.Kod.replaceAll("\r", "").replace("\n", "²´¢/TCPUjs"));
				}

				List<String> UzenetBe = new ArrayList<String>();

				int hossz = 9999999;
				int x = 0;
				while(x < hossz)
				{
					String buff = (String) UzenetKovsorVar(in, 500);
					if(buff == null && (UzenetBe.size() > 0 || x > 2))// Az elsõ 3 próbálkozásnál nem baj, ha null jön vissza, de utána hibának veszi
					{
						UjraKezd = true;
						break;
					}
					else if(buff != null)
					{
						UzenetBe.add(buff);
						System.out.println(buff);
					}

					if(UzenetBe.size() == 2)
					{
						if(UzenetBe.get(0).equals(UzenetBe.get(1)))
						{
							hossz = Integer.parseInt(UzenetBe.get(0));
							x = 1;
						}
						else
						{
							UjraKezd = true;
							break;
						}
					}

					++x;
				}

				try
				{
					in.close();
				}
				catch (Exception e)
				{}
				try
				{
					out.close();
				}
				catch (Exception e)
				{}
				try
				{
					skt.close();
				}
				catch (Exception e)
				{}

				if(UjraKezd)
					continue;

				UzenetBe.remove(0);// Hossz Eltávolítása
				UzenetBe.remove(0);

				for(int a = 0; a + 1 < UzenetBe.size(); ++a)// Sorok ellenõrzése páronként (CRC helyett)
				{
					if(!UzenetBe.get(a).equals(UzenetBe.get(a + 1)))
					{
						UjraKezd = true;
						break;
					}
					UzenetBe.remove(a);
				}
				if(UjraKezd)
					continue;

				for(int j = 0; j < UzenetKi.size(); j++)
				{
					if(!UzenetKi.get(j).equals(UzenetBe.get(j)))
					{
						UjraKezd = true;
						break;
					}
				}

				if(UjraKezd)
					continue;

				TCPFrissitoSzal.TCPHibaszamlalo = 0;
				return true;

			}
			catch (Exception e)
			{
				System.out.println("HIBA: " + (i + 1) + ": " + e.getMessage());
				try
				{
					in.close();
				}
				catch (Exception ex)
				{}
				try
				{
					out.close();
				}
				catch (Exception ex)
				{}
				try
				{
					skt.close();
				}
				catch (Exception ex)
				{}
			}
		}
		++TCPFrissitoSzal.TCPHibaszamlalo;
		return false;
	}

	public static boolean FolyamatInditLeall(boolean inditas, List<String> folyNevek)
	{
		for(int i = 0; i < 3; ++i)// Próbálkozások
		{
			Socket skt = null;
			BufferedReader in = null;
			PrintWriter out = null;
			try
			{
				boolean UjraKezd = false;
				skt = new Socket(FoClass.TCPCim, FoClass.TCPPortSzam);
				in = new BufferedReader(new InputStreamReader(skt.getInputStream(), "Cp1250"));
				out = new PrintWriter(new OutputStreamWriter(skt.getOutputStream(), "Cp1250"), true);
				// System.out.print("Received string: ");

				if(inditas)
					out.println("FolyamatInditas");
				else
					out.println("FolyamatLeallitas");

				List<String> UzenetKi = new ArrayList<String>();

				out.println(folyNevek.size() * 2 + 2);
				out.println(folyNevek.size() * 2 + 2);

				for(String item : folyNevek)
				{
					out.println(item);
					out.println(item);
					UzenetKi.add(item);
				}

				List<String> UzenetBe = new ArrayList<String>();

				int hossz = 9999999;
				int x = 0;
				while(x < hossz)
				{
					String buff = (String) UzenetKovsorVar(in, 500);
					if(buff == null && (UzenetBe.size() > 0 || x > 2))// Az elsõ 3 próbálkozásnál nem baj, ha null jön vissza, de utána hibának veszi
					{
						UjraKezd = true;
						break;
					}
					else if(buff != null)
					{
						UzenetBe.add(buff);
						System.out.println(buff);
					}

					if(UzenetBe.size() == 2)
					{
						if(UzenetBe.get(0).equals(UzenetBe.get(1)))
						{
							hossz = Integer.parseInt(UzenetBe.get(0));
							x = 1;
						}
						else
						{
							UjraKezd = true;
							break;
						}
					}

					++x;
				}

				try
				{
					in.close();
				}
				catch (Exception e)
				{}
				try
				{
					out.close();
				}
				catch (Exception e)
				{}
				try
				{
					skt.close();
				}
				catch (Exception e)
				{}

				if(UjraKezd)
					continue;

				UzenetBe.remove(0);// Hossz Eltávolítása
				UzenetBe.remove(0);

				for(int a = 0; a + 1 < UzenetBe.size(); ++a)// Sorok ellenõrzése páronként (CRC helyett)
				{
					if(!UzenetBe.get(a).equals(UzenetBe.get(a + 1)))
					{
						UjraKezd = true;
						break;
					}
					UzenetBe.remove(a);
				}
				if(UjraKezd)
					continue;

				for(int j = 0; j < UzenetKi.size(); j++)
				{
					if(!UzenetKi.get(j).equals(UzenetBe.get(j)))
					{
						UjraKezd = true;
						break;
					}
				}

				if(UjraKezd)
					continue;

				TCPFrissitoSzal.TCPHibaszamlalo = 0;
				return true;

			}
			catch (Exception e)
			{
				System.out.println("HIBA: " + (i + 1) + ": " + e.getMessage());
				try
				{
					in.close();
				}
				catch (Exception ex)
				{}
				try
				{
					out.close();
				}
				catch (Exception ex)
				{}
				try
				{
					skt.close();
				}
				catch (Exception ex)
				{}
			}
		}
		++TCPFrissitoSzal.TCPHibaszamlalo;
		return false;
	}
	
	public static boolean FolyamatSzunetFolytat(boolean folytatas, List<String> folyNevek)
	{
		for(int i = 0; i < 3; ++i)// Próbálkozások
		{
			Socket skt = null;
			BufferedReader in = null;
			PrintWriter out = null;
			try
			{
				boolean UjraKezd = false;
				skt = new Socket(FoClass.TCPCim, FoClass.TCPPortSzam);
				in = new BufferedReader(new InputStreamReader(skt.getInputStream(), "Cp1250"));
				out = new PrintWriter(new OutputStreamWriter(skt.getOutputStream(), "Cp1250"), true);
				// System.out.print("Received string: ");

				if(folytatas)
					out.println("FolyamatFolytatas");
				else
					out.println("FolyamatFelfuggesztes");

				List<String> UzenetKi = new ArrayList<String>();

				out.println(folyNevek.size() * 2 + 2);
				out.println(folyNevek.size() * 2 + 2);

				for(String item : folyNevek)
				{
					out.println(item);
					out.println(item);
					UzenetKi.add(item);
				}

				List<String> UzenetBe = new ArrayList<String>();

				int hossz = 9999999;
				int x = 0;
				while(x < hossz)
				{
					String buff = (String) UzenetKovsorVar(in, 500);
					if(buff == null && (UzenetBe.size() > 0 || x > 2))// Az elsõ 3 próbálkozásnál nem baj, ha null jön vissza, de utána hibának veszi
					{
						UjraKezd = true;
						break;
					}
					else if(buff != null)
					{
						UzenetBe.add(buff);
						System.out.println(buff);
					}

					if(UzenetBe.size() == 2)
					{
						if(UzenetBe.get(0).equals(UzenetBe.get(1)))
						{
							hossz = Integer.parseInt(UzenetBe.get(0));
							x = 1;
						}
						else
						{
							UjraKezd = true;
							break;
						}
					}

					++x;
				}

				try
				{
					in.close();
				}
				catch (Exception e)
				{}
				try
				{
					out.close();
				}
				catch (Exception e)
				{}
				try
				{
					skt.close();
				}
				catch (Exception e)
				{}

				if(UjraKezd)
					continue;

				UzenetBe.remove(0);// Hossz Eltávolítása
				UzenetBe.remove(0);

				for(int a = 0; a + 1 < UzenetBe.size(); ++a)// Sorok ellenõrzése páronként (CRC helyett)
				{
					if(!UzenetBe.get(a).equals(UzenetBe.get(a + 1)))
					{
						UjraKezd = true;
						break;
					}
					UzenetBe.remove(a);
				}
				if(UjraKezd)
					continue;

				for(int j = 0; j < UzenetKi.size(); j++)
				{
					if(!UzenetKi.get(j).equals(UzenetBe.get(j)))
					{
						UjraKezd = true;
						break;
					}
				}

				if(UjraKezd)
					continue;

				TCPFrissitoSzal.TCPHibaszamlalo = 0;
				return true;

			}
			catch (Exception e)
			{
				System.out.println("HIBA: " + (i + 1) + ": " + e.getMessage());
				try
				{
					in.close();
				}
				catch (Exception ex)
				{}
				try
				{
					out.close();
				}
				catch (Exception ex)
				{}
				try
				{
					skt.close();
				}
				catch (Exception ex)
				{}
			}
		}
		++TCPFrissitoSzal.TCPHibaszamlalo;
		return false;
	}

	public static boolean PortHozzaad(List<Port> portok)
	{
		for(int i = 0; i < 3; ++i)// Próbálkozások
		{
			Socket skt = null;
			BufferedReader in = null;
			PrintWriter out = null;
			try
			{
				boolean UjraKezd = false;
				skt = new Socket(FoClass.TCPCim, FoClass.TCPPortSzam);
				in = new BufferedReader(new InputStreamReader(skt.getInputStream(), "Cp1250"));
				out = new PrintWriter(new OutputStreamWriter(skt.getOutputStream(), "Cp1250"), true);

				out.println("PortHozzaadas");

				List<String> UzenetKi = new ArrayList<String>();

				out.println(portok.size() * 3 * 2 + 2);
				out.println(portok.size() * 3 * 2 + 2);

				for(Port item : portok)
				{
					out.println(item.Nev);
					out.println(item.Nev);
					out.println(item.getPortTipusa().name());
					out.println(item.getPortTipusa().name());
					out.println(item.RFAzonosito);
					out.println(item.RFAzonosito);

					UzenetKi.add(item.Nev);
					UzenetKi.add(item.getPortTipusa().name());
					UzenetKi.add(Integer.toString(item.RFAzonosito));
				}

				List<String> UzenetBe = new ArrayList<String>();

				int hossz = 9999999;
				int x = 0;
				while(x < hossz)
				{
					String buff = (String) UzenetKovsorVar(in, 500);
					if(buff == null && (UzenetBe.size() > 0 || x > 2))// Az elsõ 3 próbálkozásnál nem baj, ha null jön vissza, de utána hibának veszi
					{
						UjraKezd = true;
						break;
					}
					else if(buff != null)
					{
						UzenetBe.add(buff);
						System.out.println(buff);
					}

					if(UzenetBe.size() == 2)
					{
						if(UzenetBe.get(0).equals(UzenetBe.get(1)))
						{
							hossz = Integer.parseInt(UzenetBe.get(0));
							x = 1;
						}
						else
						{
							UjraKezd = true;
							break;
						}
					}

					++x;
				}

				try
				{
					in.close();
				}
				catch (Exception e)
				{}
				try
				{
					out.close();
				}
				catch (Exception e)
				{}
				try
				{
					skt.close();
				}
				catch (Exception e)
				{}

				if(UjraKezd)
					continue;

				UzenetBe.remove(0);// Hossz Eltávolítása
				UzenetBe.remove(0);

				for(int a = 0; a + 1 < UzenetBe.size(); ++a)// Sorok ellenõrzése páronként (CRC helyett)
				{
					if(!UzenetBe.get(a).equals(UzenetBe.get(a + 1)))
					{
						UjraKezd = true;
						break;
					}
					UzenetBe.remove(a);
				}
				if(UjraKezd)
					continue;

				for(int j = 0; j < UzenetKi.size(); j++)
				{
					if(!UzenetKi.get(j).equals(UzenetBe.get(j)))
					{
						UjraKezd = true;
						break;
					}
				}

				if(UjraKezd)
					continue;

				TCPFrissitoSzal.TCPHibaszamlalo = 0;
				return true;

			}
			catch (Exception e)
			{
				System.out.println("HIBA: " + (i + 1) + ": " + e.getMessage());
				try
				{
					in.close();
				}
				catch (Exception ex)
				{}
				try
				{
					out.close();
				}
				catch (Exception ex)
				{}
				try
				{
					skt.close();
				}
				catch (Exception ex)
				{}
			}
		}
		++TCPFrissitoSzal.TCPHibaszamlalo;
		return false;
	}

	public static List<Port> PortLekero()
	{
		for(int i = 0; i < 3; ++i)// Próbálkozások
		{
			Socket skt = null;
			BufferedReader in = null;
			PrintWriter out = null;
			try
			{
				boolean UjraKezd = false;
				skt = new Socket();
				skt.setSoTimeout(1200);
				skt.connect(new InetSocketAddress(FoClass.TCPCim, FoClass.TCPPortSzam));
				in = new BufferedReader(new InputStreamReader(skt.getInputStream(), "Cp1250"));
				out = new PrintWriter(new OutputStreamWriter(skt.getOutputStream(), "Cp1250"), true);
				// System.out.print("Received string: ");

				out.println("Portlekeres");

				List<String> UzenetBe = new ArrayList<String>();

				int hossz = 9999999;
				int x = 0;
				while(x < hossz)
				{
					String buff = (String) UzenetKovsorVar(in, 500);
					if(buff == null && (UzenetBe.size() > 0 || x > 2))// Az elsõ 3 próbálkozásnál nem baj, ha null jön vissza, de utána hibának veszi
					{
						UjraKezd = true;
						break;
					}
					else if(buff != null)
					{
						UzenetBe.add(buff);
						System.out.println(buff);
					}

					if(UzenetBe.size() == 2)
					{
						if(UzenetBe.get(0).equals(UzenetBe.get(1)))
						{
							hossz = Integer.parseInt(UzenetBe.get(0));
							x = 1;
						}
						else
						{
							UjraKezd = true;
							break;
						}
					}

					++x;
				}

				try
				{
					in.close();
				}
				catch (Exception e)
				{}
				try
				{
					out.close();
				}
				catch (Exception e)
				{}
				try
				{
					skt.close();
				}
				catch (Exception e)
				{}

				if(UjraKezd)
					continue;

				UzenetBe.remove(0);// Hossz Eltávolítása
				UzenetBe.remove(0);

				for(int a = 0; a + 1 < UzenetBe.size(); ++a)// Sorok ellenõrzése páronként (CRC helyett)
				{
					if(!UzenetBe.get(a).equals(UzenetBe.get(a + 1)))
					{
						UjraKezd = true;
						break;
					}
					UzenetBe.remove(a);
				}
				if(UjraKezd)
					continue;

				List<Port> PortList = new LinkedList<Port>();

				for(int a = 0; a + 3 < UzenetBe.size(); a += 4)// Objektummá alakítás
				{
					Object portertek = null;
					switch(Port.GetErtekTipus(PortTipus.valueOf(UzenetBe.get(a))))
					{
						case Bool:
						{
							portertek = Boolean.parseBoolean(UzenetBe.get(a + 3));
							break;
						}
						case Int:
						{
							portertek = Integer.parseInt(UzenetBe.get(a + 3));
							break;
						}
					}
					PortList.add(new Port(PortTipus.valueOf(UzenetBe.get(a)), Integer.parseInt(UzenetBe.get(a + 1)),
							UzenetBe.get(a + 2), portertek));

				}

				TCPFrissitoSzal.TCPHibaszamlalo = 0;
				return PortList;

			}
			catch (Exception e)
			{
				System.out.println("HIBA: " + (i + 1) + ": " + e.getMessage());
				try
				{
					in.close();
				}
				catch (Exception ex)
				{}
				try
				{
					out.close();
				}
				catch (Exception ex)
				{}
				try
				{
					skt.close();
				}
				catch (Exception ex)
				{}
			}
		}
		++TCPFrissitoSzal.TCPHibaszamlalo;
		return null;
	}

	public static boolean PortTorlo(List<String> portNevek)
	{
		for(int i = 0; i < 3; ++i)// Próbálkozások
		{
			Socket skt = null;
			BufferedReader in = null;
			PrintWriter out = null;
			try
			{
				boolean UjraKezd = false;
				skt = new Socket(FoClass.TCPCim, FoClass.TCPPortSzam);
				in = new BufferedReader(new InputStreamReader(skt.getInputStream(), "Cp1250"));
				out = new PrintWriter(new OutputStreamWriter(skt.getOutputStream(), "Cp1250"), true);
				// System.out.print("Received string: ");

				out.println("PortTorles");

				List<String> UzenetKi = new ArrayList<String>();

				out.println(portNevek.size() * 2 + 2);
				out.println(portNevek.size() * 2 + 2);
				for(String item : portNevek)
				{
					out.println(item);
					out.println(item);

					UzenetKi.add(item);
				}

				List<String> UzenetBe = new ArrayList<String>();

				int hossz = 9999999;
				int x = 0;
				while(x < hossz)
				{
					String buff = (String) UzenetKovsorVar(in, 500);
					if(buff == null && (UzenetBe.size() > 0 || x > 2))// Az elsõ 3 próbálkozásnál nem baj, ha null jön vissza, de utána hibának veszi
					{
						UjraKezd = true;
						break;
					}
					else if(buff != null)
					{
						UzenetBe.add(buff);
						System.out.println(buff);
					}

					if(UzenetBe.size() == 2)
					{
						if(UzenetBe.get(0).equals(UzenetBe.get(1)))
						{
							hossz = Integer.parseInt(UzenetBe.get(0));
							x = 1;
						}
						else
						{
							UjraKezd = true;
							break;
						}
					}

					++x;
				}

				try
				{
					in.close();
				}
				catch (Exception e)
				{}
				try
				{
					out.close();
				}
				catch (Exception e)
				{}
				try
				{
					skt.close();
				}
				catch (Exception e)
				{}

				if(UjraKezd)
					continue;

				UzenetBe.remove(0);// Hossz Eltávolítása
				UzenetBe.remove(0);

				for(int a = 0; a + 1 < UzenetBe.size(); ++a)// Sorok ellenõrzése páronként (CRC helyett)
				{
					if(!UzenetBe.get(a).equals(UzenetBe.get(a + 1)))
					{
						UjraKezd = true;
						break;
					}
					UzenetBe.remove(a);
				}

				if(UjraKezd)
					continue;

				for(int j = 0; j < UzenetKi.size(); j++)
				{
					if(!UzenetKi.get(j).equals(UzenetBe.get(j)))
					{
						UjraKezd = true;
						break;
					}
				}

				if(UjraKezd)
					continue;

				TCPFrissitoSzal.TCPHibaszamlalo = 0;
				return true;

			}
			catch (Exception e)
			{
				System.out.println("HIBA: " + (i + 1) + ": " + e.getMessage());
				try
				{
					in.close();
				}
				catch (Exception ex)
				{}
				try
				{
					out.close();
				}
				catch (Exception ex)
				{}
				try
				{
					skt.close();
				}
				catch (Exception ex)
				{}
			}
		}
		++TCPFrissitoSzal.TCPHibaszamlalo;
		return false;
	}

	public static boolean PortErtekad(HashMap<String, Object> portok)
	{
		for(int i = 0; i < 3; ++i)// Próbálkozások
		{
			Socket skt = null;
			BufferedReader in = null;
			PrintWriter out = null;
			try
			{
				boolean UjraKezd = false;
				skt = new Socket(FoClass.TCPCim, FoClass.TCPPortSzam);
				in = new BufferedReader(new InputStreamReader(skt.getInputStream(), "Cp1250"));
				out = new PrintWriter(new OutputStreamWriter(skt.getOutputStream(), "Cp1250"), true);

				out.println("PortErtekadas");

				List<String> UzenetKi = new ArrayList<String>();

				out.println(portok.size() * 2 * 2 + 2);
				out.println(portok.size() * 2 * 2 + 2);

				for(String item : portok.keySet())
				{
					out.println(item);
					out.println(item);
					out.println(portok.get(item));
					out.println(portok.get(item));

					UzenetKi.add(item);
					UzenetKi.add(portok.get(item).toString());
				}

				List<String> UzenetBe = new ArrayList<String>();

				int hossz = 9999999;
				int x = 0;
				while(x < hossz)
				{
					String buff = (String) UzenetKovsorVar(in, 500);
					if(buff == null && (UzenetBe.size() > 0 || x > 2))// Az elsõ 3 próbálkozásnál nem baj, ha null jön vissza, de utána hibának veszi
					{
						UjraKezd = true;
						break;
					}
					else if(buff != null)
					{
						UzenetBe.add(buff);
						System.out.println(buff);
					}

					if(UzenetBe.size() == 2)
					{
						if(UzenetBe.get(0).equals(UzenetBe.get(1)))
						{
							hossz = Integer.parseInt(UzenetBe.get(0));
							x = 1;
						}
						else
						{
							UjraKezd = true;
							break;
						}
					}

					++x;
				}

				try
				{
					in.close();
				}
				catch (Exception e)
				{}
				try
				{
					out.close();
				}
				catch (Exception e)
				{}
				try
				{
					skt.close();
				}
				catch (Exception e)
				{}

				if(UjraKezd)
					continue;

				UzenetBe.remove(0);// Hossz Eltávolítása
				UzenetBe.remove(0);

				for(int a = 0; a + 1 < UzenetBe.size(); ++a)// Sorok ellenõrzése páronként (CRC helyett)
				{
					if(!UzenetBe.get(a).equals(UzenetBe.get(a + 1)))
					{
						UjraKezd = true;
						break;
					}
					UzenetBe.remove(a);
				}
				if(UjraKezd)
					continue;

				for(int j = 0; j < UzenetKi.size(); j++)
				{
					if(!UzenetKi.get(j).equals(UzenetBe.get(j)))
					{
						UjraKezd = true;
						break;
					}
				}

				if(UjraKezd)
					continue;

				TCPFrissitoSzal.TCPHibaszamlalo = 0;
				return true;

			}
			catch (Exception e)
			{
				System.out.println("HIBA: " + (i + 1) + ": " + e.getMessage());
				try
				{
					in.close();
				}
				catch (Exception ex)
				{}
				try
				{
					out.close();
				}
				catch (Exception ex)
				{}
				try
				{
					skt.close();
				}
				catch (Exception ex)
				{}
			}
		}
		++TCPFrissitoSzal.TCPHibaszamlalo;
		return false;
	}

	public static boolean SzerverUjraindit()
	{
		for(int i = 0; i < 3; ++i)// Próbálkozások
		{
			Socket skt = null;
			BufferedReader in = null;
			PrintWriter out = null;
			try
			{
				boolean UjraKezd = false;
				skt = new Socket(FoClass.TCPCim, FoClass.TCPPortSzam);
				in = new BufferedReader(new InputStreamReader(skt.getInputStream(), "Cp1250"));
				out = new PrintWriter(new OutputStreamWriter(skt.getOutputStream(), "Cp1250"), true);
				// System.out.print("Received string: ");

				out.println("SzerverUjraindit");

				List<String> UzenetKi = new ArrayList<String>();

				UzenetKi.add("SzerverUjraindit");

				List<String> UzenetBe = new ArrayList<String>();

				int x = 0;
				while(x < 1)
				{
					String buff = (String) UzenetKovsorVar(in, 500);
					if(buff == null && (UzenetBe.size() > 0 || x > 2))// Az elsõ 3 próbálkozásnál nem baj, ha null jön vissza, de utána hibának veszi
					{
						UjraKezd = true;
						break;
					}
					else if(buff != null)
					{
						UzenetBe.add(buff);
						System.out.println(buff);
					}

					++x;
				}

				try
				{
					in.close();
				}
				catch (Exception e)
				{}
				try
				{
					out.close();
				}
				catch (Exception e)
				{}
				try
				{
					skt.close();
				}
				catch (Exception e)
				{}

				if(UjraKezd)
					continue;

				for(int j = 0; j < UzenetKi.size(); j++)
				{
					if(!UzenetKi.get(j).equals(UzenetBe.get(j)))
					{
						UjraKezd = true;
						break;
					}
				}

				if(UjraKezd)
					continue;

				TCPFrissitoSzal.TCPHibaszamlalo = 0;
				return true;

			}
			catch (Exception e)
			{
				System.out.println("HIBA: " + (i + 1) + ": " + e.getMessage());
				try
				{
					in.close();
				}
				catch (Exception ex)
				{}
				try
				{
					out.close();
				}
				catch (Exception ex)
				{}
				try
				{
					skt.close();
				}
				catch (Exception ex)
				{}
			}
		}
		++TCPFrissitoSzal.TCPHibaszamlalo;
		return false;
	}

	public static boolean KockaHozzaadoI2C(String Portnev)
	{
		for(int i = 0; i < 3; ++i)// Próbálkozások
		{
			Socket skt = null;
			BufferedReader in = null;
			PrintWriter out = null;
			try
			{
				boolean UjraKezd = false;
				skt = new Socket(FoClass.TCPCim, FoClass.TCPPortSzam);
				in = new BufferedReader(new InputStreamReader(skt.getInputStream(), "Cp1250"));
				out = new PrintWriter(new OutputStreamWriter(skt.getOutputStream(), "Cp1250"), true);

				List<String> UzenetKi = new ArrayList<String>();
				out.println("KockaHozzaadasI2C");

				out.println(4);
				out.println(4);
				out.println(Portnev);
				out.println(Portnev);

				UzenetKi.add(Portnev);

				List<String> UzenetBe = new ArrayList<String>();

				int hossz = 9999999;
				int x = 0;
				while(x < hossz)
				{
					String buff = (String) UzenetKovsorVar(in, 500);
					if(buff == null && (UzenetBe.size() > 0 || x > 2))// Az elsõ 3 próbálkozásnál nem baj, ha null jön vissza, de utána hibának veszi
					{
						UjraKezd = true;
						break;
					}
					else if(buff != null)
					{
						UzenetBe.add(buff);
						System.out.println(buff);
					}

					if(UzenetBe.size() == 2)
					{
						if(UzenetBe.get(0).equals(UzenetBe.get(1)))
						{
							hossz = Integer.parseInt(UzenetBe.get(0));
							x = 1;
						}
						else
						{
							UjraKezd = true;
							break;
						}
					}

					++x;
				}

				try
				{
					in.close();
				}
				catch (Exception e)
				{}
				try
				{
					out.close();
				}
				catch (Exception e)
				{}
				try
				{
					skt.close();
				}
				catch (Exception e)
				{}

				if(UjraKezd)
					continue;

				UzenetBe.remove(0);// Hossz Eltávolítása
				UzenetBe.remove(0);

				for(int a = 0; a + 1 < UzenetBe.size(); ++a)// Sorok ellenõrzése páronként (CRC helyett)
				{
					if(!UzenetBe.get(a).equals(UzenetBe.get(a + 1)))
					{
						UjraKezd = true;
						break;
					}
					UzenetBe.remove(a);
				}
				if(UjraKezd)
					continue;

				for(int j = 0; j < UzenetKi.size(); j++)
				{
					if(!UzenetKi.get(j).equals(UzenetBe.get(j)))
					{
						UjraKezd = true;
						break;
					}
				}

				if(UjraKezd)
					continue;

				TCPFrissitoSzal.TCPHibaszamlalo = 0;
				return true;
			}
			catch (Exception e)
			{
				System.out.println("HIBA: " + (i + 1) + ": " + e.getMessage());
				try
				{
					in.close();
				}
				catch (Exception ex)
				{}
				try
				{
					out.close();
				}
				catch (Exception ex)
				{}
				try
				{
					skt.close();
				}
				catch (Exception ex)
				{}
			}
		}
		++TCPFrissitoSzal.TCPHibaszamlalo;
		return false;
	}

	public static Object UzenetKovsorVar(BufferedReader in, long timeout)
	{

		try
		{
			long idokezdet = System.currentTimeMillis();
			boolean idotullepes = false;
			while(!in.ready())
			{
				if(System.currentTimeMillis() - idokezdet > timeout)
				{
					idotullepes = true;
					break;
				}

				try
				{
					Thread.sleep(1);
				}
				catch (InterruptedException e)
				{}
			}
			if(idotullepes)
			{
				return null;
			}

			return in.readLine();
		}
		catch (IOException e)
		{}
		return null;
	}
}
