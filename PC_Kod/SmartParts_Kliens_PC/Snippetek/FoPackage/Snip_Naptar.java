package FoPackage;
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JSpinner;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import javax.swing.JButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JTextField;
import FoPackage.FolySzerkSnippetkiv;
import javax.swing.JLabel;

public class Snip_Naptar
{

	private JFrame frame;

	/**
	 * Create the application.
	 */
	public Snip_Naptar()
	{
		initialize();
	}

	public boolean ShowDialog(Object szulo)
	{
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);//DISPOSE_ON_CLOSE: Nem zárja be a szülõablakot
		((FolySzerkSnippetkiv) szulo).frame.setEnabled(false);
		Szulo = (FolySzerkSnippetkiv) szulo;
		frame.setLocationRelativeTo(Szulo.frame);
		frame.setVisible(true);
		return true;
	}

	FolySzerkSnippetkiv Szulo;

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize()
	{
		frame = new JFrame();
		frame.setUndecorated(true);
		frame.setBounds(100, 100, 215, 171);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		frame.setBackground(new Color((float) (frame.getBackground().getRed() / (float) 256),
				(float) (frame.getBackground().getGreen() / (float) 256),
				(float) (frame.getBackground().getBlue() / (float) 256), 1.0f));

		JLayeredPane layeredPane = new JLayeredPane();
		frame.getContentPane().add(layeredPane, BorderLayout.CENTER);

		JButton btnMsodperc = new JButton("M\u00E1sodperc");
		btnMsodperc.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseReleased(MouseEvent arg0)
			{
				FoClass.FolySzerkPeldany.KodBeillesztesVanKurzornalWhitespaceVizsgalo(" ");
				FoClass.FolySzerkPeldany.textAreaKod.insert("Másodperc",
						FoClass.FolySzerkPeldany.textAreaKod.getCaretPosition());

				Szulo.frame.setEnabled(true);
				Szulo.Szulo.frame.setEnabled(true);
				Szulo.frame.dispose();
				frame.dispose();
			}
		});
		btnMsodperc.setBounds(10, 11, 90, 28);
		layeredPane.add(btnMsodperc);

		JButton btnPerc = new JButton("Perc");
		btnPerc.setBounds(10, 51, 90, 28);
		btnPerc.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseReleased(MouseEvent arg0)
			{
				FoClass.FolySzerkPeldany.KodBeillesztesVanKurzornalWhitespaceVizsgalo(" ");
				FoClass.FolySzerkPeldany.textAreaKod.insert("Perc",
						FoClass.FolySzerkPeldany.textAreaKod.getCaretPosition());

				Szulo.frame.setEnabled(true);
				Szulo.Szulo.frame.setEnabled(true);
				Szulo.frame.dispose();
				frame.dispose();
			}
		});
		layeredPane.add(btnPerc);

		JButton btnra = new JButton("\u00D3ra");
		btnra.setBounds(10, 91, 90, 28);
		btnra.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseReleased(MouseEvent arg0)
			{
				FoClass.FolySzerkPeldany.KodBeillesztesVanKurzornalWhitespaceVizsgalo(" ");
				FoClass.FolySzerkPeldany.textAreaKod.insert("Óra",
						FoClass.FolySzerkPeldany.textAreaKod.getCaretPosition());

				Szulo.frame.setEnabled(true);
				Szulo.Szulo.frame.setEnabled(true);
				Szulo.frame.dispose();
				frame.dispose();
			}
		});
		layeredPane.add(btnra);

		JButton btnNap = new JButton("Nap");
		btnNap.setToolTipText("H\u00F3nap Napja");
		btnNap.setBounds(112, 11, 90, 28);
		btnNap.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseReleased(MouseEvent arg0)
			{
				FoClass.FolySzerkPeldany.KodBeillesztesVanKurzornalWhitespaceVizsgalo(" ");
				FoClass.FolySzerkPeldany.textAreaKod.insert("HónapNapja",
						FoClass.FolySzerkPeldany.textAreaKod.getCaretPosition());

				Szulo.frame.setEnabled(true);
				Szulo.Szulo.frame.setEnabled(true);
				Szulo.frame.dispose();
				frame.dispose();
			}
		});
		layeredPane.add(btnNap);

		JButton btnHnap = new JButton("H\u00F3nap");
		btnHnap.setBounds(112, 51, 90, 28);
		btnHnap.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseReleased(MouseEvent arg0)
			{
				FoClass.FolySzerkPeldany.KodBeillesztesVanKurzornalWhitespaceVizsgalo(" ");
				FoClass.FolySzerkPeldany.textAreaKod.insert("Hónap",
						FoClass.FolySzerkPeldany.textAreaKod.getCaretPosition());

				Szulo.frame.setEnabled(true);
				Szulo.Szulo.frame.setEnabled(true);
				Szulo.frame.dispose();
				frame.dispose();
			}
		});
		layeredPane.add(btnHnap);

		JButton btnv = new JButton("\u00C9v");
		btnv.setBounds(112, 91, 90, 28);
		btnv.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseReleased(MouseEvent arg0)
			{
				FoClass.FolySzerkPeldany.KodBeillesztesVanKurzornalWhitespaceVizsgalo(" ");
				FoClass.FolySzerkPeldany.textAreaKod.insert("Év",
						FoClass.FolySzerkPeldany.textAreaKod.getCaretPosition());

				Szulo.frame.setEnabled(true);
				Szulo.Szulo.frame.setEnabled(true);
				Szulo.frame.dispose();
				frame.dispose();
			}
		});
		layeredPane.add(btnv);

		JButton btnHtNapja = new JButton("H\u00E9t Napja");
		btnHtNapja.setToolTipText("1: H\u00E9tf\u0151 - 7: Vas\u00E1rnap");
		btnHtNapja.setBounds(10, 131, 192, 28);
		btnHtNapja.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseReleased(MouseEvent arg0)
			{
				FoClass.FolySzerkPeldany.KodBeillesztesVanKurzornalWhitespaceVizsgalo(" ");
				FoClass.FolySzerkPeldany.textAreaKod.insert("HétNapja",
						FoClass.FolySzerkPeldany.textAreaKod.getCaretPosition());

				Szulo.frame.setEnabled(true);
				Szulo.Szulo.frame.setEnabled(true);
				Szulo.frame.dispose();
				frame.dispose();
			}
		});
		layeredPane.add(btnHtNapja);

		Component[] c = layeredPane.getComponents();
		for(Component item : c)
		{
			try//MERT a JSpinner-nek máshogy kell KeyListener-t adni. Ha az objektum nem kasztolható JSpinner-be, akkor simán ad neki
			{
				((JSpinner.DefaultEditor)(((JSpinner)item).getEditor())).getTextField().addKeyListener(MindenKomponensKeyadapter);
			}
			catch(Exception e)
			{
				item.addKeyListener(MindenKomponensKeyadapter);
			}
		}
		layeredPane.addKeyListener(MindenKomponensKeyadapter);
	}

	KeyAdapter MindenKomponensKeyadapter = new KeyAdapter()
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
	};
}
