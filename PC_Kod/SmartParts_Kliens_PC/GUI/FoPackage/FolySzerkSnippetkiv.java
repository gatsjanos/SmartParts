package FoPackage;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import org.eclipse.swt.widgets.Display;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import javax.swing.JButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class FolySzerkSnippetkiv
{

	JFrame frame;

	/**
	 * Create the application.
	 */
	public FolySzerkSnippetkiv()
	{
		initialize();
	}

	public boolean ShowDialog(Object szulo)
	{
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);//DISPOSE_ON_CLOSE: Nem zárja be a szülõablakot
		((FolySzerkeszto) szulo).frame.setEnabled(false);
		Szulo = (FolySzerkeszto) szulo;
		frame.setLocationRelativeTo(Szulo.frame);
		frame.setVisible(true);

		return true;
	}

	FolySzerkeszto Szulo;

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize()
	{
		frame = new JFrame();
		frame.setUndecorated(true);
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		frame.setBackground(new Color((float) (frame.getBackground().getRed() / (float) 256),
				(float) (frame.getBackground().getGreen() / (float) 256),
				(float) (frame.getBackground().getBlue() / (float) 256), 0.7f));

		JLayeredPane layeredPane = new JLayeredPane();
		frame.getContentPane().add(layeredPane, BorderLayout.CENTER);

		JButton btnHa = new JButton("Ha(?)");
		FolySzerkSnippetkiv THIS = this;
		btnHa.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseReleased(MouseEvent e)
			{
				new Snip_Ha().ShowDialog(THIS);
			}
		});
		btnHa.setBounds(6, 6, 99, 28);
		layeredPane.add(btnHa);

		JButton btnIdo = new JButton("Napt\u00E1r");
		btnIdo.setBounds(228, 6, 99, 28);
		btnIdo.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseReleased(MouseEvent e)
			{
				new Snip_Naptar().ShowDialog(THIS);
			}
		});
		layeredPane.add(btnIdo);

		JButton btnAmg = new JButton("Am\u00EDg(?)");
		btnAmg.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseReleased(MouseEvent arg0)
			{
				new Snip_Amig().ShowDialog(THIS);
			}
		});
		btnAmg.setBounds(6, 46, 99, 28);
		layeredPane.add(btnAmg);

		JButton btnIsmteld = new JButton("Ism\u00E9teld(x)");
		btnIsmteld.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseReleased(MouseEvent e)
			{
				new Snip_Ismeteld().ShowDialog(THIS);
			}
		});
		btnIsmteld.setBounds(6, 85, 99, 28);
		layeredPane.add(btnIsmteld);

		JButton btnKulcsszaak = new JButton("Kulcsszavak");
		btnKulcsszaak.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseReleased(MouseEvent e)
			{
				new Snip_Kulcsszavak().ShowDialog(THIS);
			}
		});
		btnKulcsszaak.setBounds(117, 6, 99, 28);
		layeredPane.add(btnKulcsszaak);

		JButton btnOkoskockaMveletek = new JButton("Okoskocka Rel\u00E9 M\u0171veletek");
		btnOkoskockaMveletek.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseReleased(MouseEvent e)
			{
				new Snip_ReleKockamuveletek().ShowDialog(THIS);
			}
		});
		btnOkoskockaMveletek.setBounds(6, 207, 175, 28);
		layeredPane.add(btnOkoskockaMveletek);

		JButton btnEmailKlds = new JButton("E-mail");
		btnEmailKlds.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseReleased(MouseEvent arg0)
			{
				new Snip_Email().ShowDialog(THIS);
			}
		});
		btnEmailKlds.setBounds(228, 85, 99, 28);
		layeredPane.add(btnEmailKlds);

		JButton btnNewButton = new JButton("Gy\u00F6k(x, y)");
		btnNewButton.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseReleased(MouseEvent e)
			{
				new Snip_Gyok().ShowDialog(THIS);
			}
		});
		btnNewButton.setBounds(339, 46, 99, 28);
		layeredPane.add(btnNewButton);

		JButton btnVrakozsx = new JButton("V\u00E1rakoz\u00E1s(x)");
		btnVrakozsx.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseReleased(MouseEvent e)
			{
				new Snip_Varj().ShowDialog(THIS);
			}
		});
		btnVrakozsx.setBounds(228, 46, 99, 28);
		layeredPane.add(btnVrakozsx);

		JButton btnHatvnyxy = new JButton("Hatv\u00E1ny(x, y)");
		btnHatvnyxy.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseReleased(MouseEvent arg0)
			{
				new Snip_Hatvany().ShowDialog(THIS);
			}
		});
		btnHatvnyxy.setBounds(339, 6, 99, 28);
		layeredPane.add(btnHatvnyxy);

		JButton btnKdAutomatikusFormzsa = new JButton("K\u00F3d Automatikus Form\u00E1z\u00E1sa");
		btnKdAutomatikusFormzsa.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseReleased(MouseEvent arg0)
			{
				Szulo.KodAutoFormat(Szulo.textAreaKod.getCaretLineNumber());
				Szulo.frame.setEnabled(true);
				frame.dispose();
			}
		});
		btnKdAutomatikusFormzsa.setBounds(127, 266, 186, 28);
		layeredPane.add(btnKdAutomatikusFormzsa);

		JButton btnFolyamatokKezelse = new JButton("Folyamatok Kezel\u00E9se");
		btnFolyamatokKezelse.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseReleased(MouseEvent arg0)
			{
				new Snip_Folykezeles().ShowDialog(THIS);
			}
		});
		btnFolyamatokKezelse.setBounds(6, 166, 175, 28);
		layeredPane.add(btnFolyamatokKezelse);
		
		JButton btnAlgebraiFormula = new JButton("Algebrai Formula");
		btnAlgebraiFormula.addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent arg0) 
			{
				Displayx.MainApplet dmablak = new Displayx.MainApplet();
//				dmablak.setLocation(Szulo.frame.getLocation());
				dmablak.init();

				JFrame frame = new JFrame("FrameDemo");
				frame.add(dmablak);
				frame.setVisible(true);
				
				//Snip_DragMathIndito dmind = new Snip_DragMathIndito();
				//dmind.Szulo = THIS;
				//dmind.start();
				
			}
		});
		btnAlgebraiFormula.setBounds(263, 207, 175, 28);
		layeredPane.add(btnAlgebraiFormula);

		Component[] c = layeredPane.getComponents();
		for(Component item : c)
		{
			item.addKeyListener(new KeyAdapter()
			{
				@Override
				public void keyReleased(KeyEvent arg0)
				{
					if(arg0.getKeyCode() == KeyEvent.VK_ESCAPE)
					{
						Szulo.frame.setEnabled(true);
						frame.dispose();
					}
				}
			});
		}
		layeredPane.addKeyListener(new KeyAdapter()
		{
			@Override
			public void keyReleased(KeyEvent arg0)
			{
				if(arg0.getKeyCode() == KeyEvent.VK_ESCAPE)
				{
					Szulo.frame.setEnabled(true);
					frame.dispose();
				}
			}
		});
	}
}
