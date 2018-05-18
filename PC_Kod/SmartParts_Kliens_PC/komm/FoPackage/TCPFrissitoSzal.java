package FoPackage;
public class TCPFrissitoSzal extends Thread
{
	public static int TCPHibaszamlalo = 0;
	public static int TCPHibaszamlaloMaxertek = 2;

	public void run()
	{
		while(true)
		{
			try
			{

				if(TCPHibaszamlalo >= TCPHibaszamlaloMaxertek && FoClass.UDPBroadcast)
				{
					try
					{
						UDPBroadcasting.Broadcast();
					}
					catch (Exception e)
					{
						System.out.println(e.getMessage());
						e.printStackTrace();
					}
				}
				try
				{
					synchronized(this)
					{
						wait(FoClass.TCPFrissitoSzalVarakozas - 300);//Notify-olni kell a sz�lat, hogy tov�bb fusson tesztelje a benne l�v� ciklusokat
					}
				}
				catch (InterruptedException e)
				{}

				if(FoClass.FoAblakPeldany != null && FoClass.FoAblakPeldany.frame.isVisible()
						&& FoClass.FoAblakPeldany.frame.isEnabled())
				{
					FoClass.FoAblakPeldany.PortokListaFrissit(false);
					try
					{
						synchronized(this)
						{
							Thread.sleep(300);
						}
					}
					catch (InterruptedException e)
					{}
				}
				if(FoClass.FolyListazoPeldany != null && FoClass.FolyListazoPeldany.frame.isVisible()
						&& FoClass.FolyListazoPeldany.frame.isEnabled())
				{
					FoClass.FolyListazoPeldany.SzerverListaFrissit(false);
					try
					{
						synchronized(this)
						{
							Thread.sleep(300);
						}
					}
					catch (InterruptedException e)
					{}
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
