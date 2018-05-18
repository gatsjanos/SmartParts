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

public class Snip_Kulcsszavak
{

	private JFrame frame;

	/**
	 * Create the application.
	 */
	public Snip_Kulcsszavak()
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
		frame.setBounds(100, 100, 241, 169);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		frame.setBackground(new Color((float) (frame.getBackground().getRed() / (float) 256),
				(float) (frame.getBackground().getGreen() / (float) 256),
				(float) (frame.getBackground().getBlue() / (float) 256), 1.0f));

		JLayeredPane layeredPane = new JLayeredPane();
		frame.getContentPane().add(layeredPane, BorderLayout.CENTER);

		JButton btnIgaz = new JButton("Igaz");
		btnIgaz.setToolTipText("Logikai igaz \u00E9rt\u00E9k");
		btnIgaz.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseReleased(MouseEvent arg0)
			{
				FoClass.FolySzerkPeldany.KodBeillesztesVanKurzornalWhitespaceVizsgalo(" ");
				FoClass.FolySzerkPeldany.textAreaKod.insert("Igaz",
						FoClass.FolySzerkPeldany.textAreaKod.getCaretPosition());

				Szulo.frame.setEnabled(true);
				Szulo.Szulo.frame.setEnabled(true);
				Szulo.frame.dispose();
				frame.dispose();
			}
		});
		btnIgaz.setBounds(10, 11, 105, 28);
		layeredPane.add(btnIgaz);

		JButton btnTord = new JButton("Ciklust\u00F6r\u00E9s");
		btnTord.setToolTipText("Kiugr\u00E1s az aktu\u00E1lis \"Ism\u00E9teld()\" / \"Am\u00EDg()\" ciklusb\u00F3l.");
		btnTord.setBounds(127, 72, 105, 28);
		btnTord.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseReleased(MouseEvent arg0)
			{
				FoClass.FolySzerkPeldany.KodBeillesztesVanKurzornalWhitespaceVizsgalo(" ");
				FoClass.FolySzerkPeldany.textAreaKod.insert("Törd;",
						FoClass.FolySzerkPeldany.textAreaKod.getCaretPosition());

				Szulo.frame.setEnabled(true);
				Szulo.Szulo.frame.setEnabled(true);
				Szulo.frame.dispose();
				frame.dispose();
			}
		});
		layeredPane.add(btnTord);

		JButton btnHamis = new JButton("Hamis");
		btnHamis.setToolTipText("Logikai hamis \u00E9rt\u00E9k");
		btnHamis.setBounds(127, 11, 105, 28);
		btnHamis.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseReleased(MouseEvent arg0)
			{
				FoClass.FolySzerkPeldany.KodBeillesztesVanKurzornalWhitespaceVizsgalo(" ");
				FoClass.FolySzerkPeldany.textAreaKod.insert("Hamis",
						FoClass.FolySzerkPeldany.textAreaKod.getCaretPosition());

				Szulo.frame.setEnabled(true);
				Szulo.Szulo.frame.setEnabled(true);
				Szulo.frame.dispose();
				frame.dispose();
			}
		});
		layeredPane.add(btnHamis);

		JButton btnTovabb = new JButton("Ciklus\u00E1tugr\u00E1s");
		btnTovabb.setToolTipText("Aktu\u00E1lis \"Ism\u00E9teld()\" / \"Am\u00EDg()\" ciklus l\u00E9ptet\u00E9se. (Ciklusmag v\u00E9grehajt\u00E1sa az elej\u00E9t\u0151l, a k\u00F6vetkez\u0151 ism\u00E9tl\u00E9sben.)");
		btnTovabb.setBounds(10, 72, 105, 28);
		btnTovabb.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseReleased(MouseEvent arg0)
			{
				FoClass.FolySzerkPeldany.KodBeillesztesVanKurzornalWhitespaceVizsgalo(" ");
				FoClass.FolySzerkPeldany.textAreaKod.insert("Tovább;",
						FoClass.FolySzerkPeldany.textAreaKod.getCaretPosition());

				Szulo.frame.setEnabled(true);
				Szulo.Szulo.frame.setEnabled(true);
				Szulo.frame.dispose();
				frame.dispose();
			}
		});
		layeredPane.add(btnTovabb);

		JButton btnHtNapja = new JButton("Kil\u00E9p\u00E9s");
		btnHtNapja.setToolTipText("A folyamat le\u00E1ll\u00EDt\u00E1sa");
		btnHtNapja.setBounds(10, 134, 222, 28);
		btnHtNapja.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseReleased(MouseEvent arg0)
			{
				FoClass.FolySzerkPeldany.KodBeillesztesVanKurzornalWhitespaceVizsgalo(" ");
				FoClass.FolySzerkPeldany.textAreaKod.insert("Kilép;",
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
