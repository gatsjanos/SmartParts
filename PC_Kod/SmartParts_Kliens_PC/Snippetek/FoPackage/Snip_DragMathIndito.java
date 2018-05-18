package FoPackage;

import javax.swing.JFrame;

public class Snip_DragMathIndito extends Thread
{
	public FolySzerkSnippetkiv Szulo = null;
	public void run()
	{
		Displayx.MainApplet dmablak = new Displayx.MainApplet();
		
		Szulo.frame.setEnabled(false);
		
		dmablak.setLocation(Szulo.frame.getLocation());
		dmablak.init();
		Szulo.frame.setEnabled(true);
	}
}
