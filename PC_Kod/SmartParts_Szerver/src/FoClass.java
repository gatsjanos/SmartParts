import java.util.*;

public class FoClass
{
	public static final int FolyamatokFajlverz = 1;
	public static final int PortokFajlverz = 1;
	
	public static int TCPPortSzam = 43000;
	public static int UDPBroadcastPortSzam = 43002;

	public static final String Emailusername = "smartpartssmtp@gmail.com";
	public static final String Emailpassword = "qwertzuiop0123456789";

	static Scanner Scan = new Scanner(System.in);

	static Map<String, Port> PortokMap = new HashMap<String, Port>();
	static Map<String, KodFeldolgozo> FolyamatokMap = new LinkedHashMap<String, KodFeldolgozo>();

	static TCPServer TCPSzervTH;
	static SegedSzal SegedSzalTH;

	public static SorosPort SorosPortAVR = new SorosPort();

	public static void main(String[] args)
	{
		System.out.println("APPLICATION: Szerver FoClass fut...");

		PortokMap.putAll(FajlKezelo.PortBeolvas());
		FolyamatokMap.putAll(FajlKezelo.FolyamatBeolvas());


		new Folyszervezo().start();

		TCPSzervTH = new TCPServer();
		TCPSzervTH.start();

		SegedSzalTH = new SegedSzal();
		SegedSzalTH.start();

		new UDPListenerThread().start();

		SorosPortAVR.Kezfogas();
		
		while(true)
		{
			try
			{
				String buff = Scan.nextLine();

				if(buff.equals("exit"))
				{
					System.out.println("APPLICATION: Kilépés...");
					System.exit(0);
				}
				else if(buff.startsWith("nyit"))
				{
					if((boolean) PortokMap.get(buff.substring(5)).Ertek != true)
					{
						PortokMap.get(buff.substring(5)).setErtek(true);
						Folyszervezo.EsemenyTeszt("Nyitva(" + buff.substring(5) + ")");
						System.out.println("--DEBUG: " + buff.substring(5) + " NYITVA parancssorból");
					}
				}
				else if(buff.startsWith("zar"))
				{
					if((boolean) PortokMap.get(buff.substring(4)).Ertek != false)
					{
						PortokMap.get(buff.substring(4)).setErtek(false);
						Folyszervezo.EsemenyTeszt("Zárva(" + buff.substring(4) + ")");
						System.out.println("--DEBUG: " + buff.substring(4) + " ZÁRVA parancssorból");
					}
				}
			}
			catch (Exception e)
			{
				System.out.println(e.getMessage());
				e.printStackTrace();
			}
		}

	}

}
