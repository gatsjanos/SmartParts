package FoPackage;

import java.awt.Color;
import java.util.*;
import javax.swing.*;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.plaf.nimbus.*;
import javax.swing.plaf.ColorUIResource;
import javax.swing.border.Border;

public class FoClass
{
	public static final int FolyamatokFajlverz = 1;
	public static final int PortokFajlverz = 1;
	public static final long TCPFrissitoSzalVarakozas = 2000;

	public static int TCPPortSzam = 43000;
	public static int UDPBroadcastPortSzam = 43002;
	public static String TCPCim = "";
	public static boolean UDPBroadcast = true;

	public static List<KliFolyamat> FolyamatokSzerver = new ArrayList<KliFolyamat>();
	public static List<Port> PortokSzerver = new ArrayList<Port>();

	static List<KliFolyamat> FolyamatokKliens = new ArrayList<KliFolyamat>();

	public static TCPFrissitoSzal TCPFrissitoSzalTH = new TCPFrissitoSzal();
	
	public static FoAblak FoAblakPeldany;
	public static FolyamatListazo FolyListazoPeldany;
	public static FolySzerkeszto FolySzerkPeldany;
	public static Beallitasok BeallitasokPeldany;

	public static void main(String[] args)
	{
		UIManager.put("ToolTip.background", Color.BLUE);

		System.out.println("APPLICATION: Kliens FoClass fut...");

		boolean vanLF = false;
		try
		{
			UIManager.setLookAndFeel(new SajatLookAndFeel());
			vanLF = true;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			try
			{
				for(LookAndFeelInfo info : UIManager.getInstalledLookAndFeels())
				{
					if("Nimbus".equals(info.getName()))
					{
						UIManager.setLookAndFeel(info.getClassName());
						vanLF = true;
						break;
					}
				}
			}
			catch (Exception ex) //Nimbus is not available
			{}
		}
		if(!vanLF)
		{
			try
			{
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
			}
		}

		UIManager.put("ToolTip.background", Color.BLUE);

		FoClass.FolyamatokKliens = FajlKezelo.FolyamatBeolvas();

		try
		{
			FoAblakPeldany = new FoAblak();
			FoAblakPeldany.frame.setVisible(true);
		}
		catch (Exception e)
		{
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		
		TCPFrissitoSzal.TCPHibaszamlalo = TCPFrissitoSzal.TCPHibaszamlaloMaxertek;//hogy azonal broadcastoljon a TCPFrissitoSzalTH
		TCPFrissitoSzalTH.start();
		
		//		TCPCom.FolyamatLekero();
		//		TCPCom.PortLekero();
	}

}

class SajatLookAndFeel extends NimbusLookAndFeel
{
	protected void initSystemColorDefaults(UIDefaults table)
	{
		super.initSystemColorDefaults(table);
		table.put("info", new ColorUIResource(255, 247, 200));
	}

	protected void initComponentDefaults(UIDefaults table)
	{
		super.initComponentDefaults(table);

		Border border = BorderFactory.createLineBorder(new Color(76, 79, 83));
		table.put("ToolTip.border", border);
		table.put("ToolTip.background", Color.BLUE);
	}
}
