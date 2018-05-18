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
import javax.swing.JCheckBox;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class Snip_Email
{

	private JFrame frame;

	/**
	 * Create the application.
	 */
	public Snip_Email()
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
	private JTextField textFieldCimzett;

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize()
	{
		frame = new JFrame();
		frame.setUndecorated(true);
		frame.setBounds(100, 100, 455, 392);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		frame.setBackground(new Color((float) (frame.getBackground().getRed() / (float) 256),
				(float) (frame.getBackground().getGreen() / (float) 256),
				(float) (frame.getBackground().getBlue() / (float) 256), 1.0f));

		JLayeredPane layeredPane = new JLayeredPane();
		frame.getContentPane().add(layeredPane, BorderLayout.CENTER);

		textFieldCimzett = new JTextField();
		textFieldCimzett.setToolTipText(
				"Adja meg a ciklus felt\u00E9tel\u00E9t, (vagy hagyja \u00FCresen,) majd \u00FCss\u00F6n Entert a gener\u00E1l\u00E1shoz!");
		textFieldCimzett.setBounds(68, 6, 375, 28);
		layeredPane.add(textFieldCimzett);
		textFieldCimzett.setColumns(10);

		JLabel lblFelttel = new JLabel("C\u00EDmzett:");
		lblFelttel.setBounds(6, 12, 50, 16);
		layeredPane.add(lblFelttel);

		textFieldTargy = new JTextField();
		textFieldTargy.setToolTipText(
				"Adja meg a ciklus felt\u00E9tel\u00E9t, (vagy hagyja \u00FCresen,) majd \u00FCss\u00F6n Entert a gener\u00E1l\u00E1shoz!");
		textFieldTargy.setColumns(10);
		textFieldTargy.setBounds(68, 47, 375, 28);
		layeredPane.add(textFieldTargy);

		JLabel lblTrgy = new JLabel("T\u00E1rgy:");
		lblTrgy.setBounds(6, 53, 50, 16);
		layeredPane.add(lblTrgy);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(6, 107, 437, 242);
		layeredPane.add(scrollPane);

		textAreaUzenet = new JTextArea();
		textAreaUzenet.setToolTipText("Az ide \u00EDrt id\u00E9z\u0151jel, sort\u00F6r\u00E9s \u00E9s tabul\u00E1tor karakterek k\u00F3dolva lesznek. (\":  \\\" , sor: \\n , tab: \\t)");
		scrollPane.setViewportView(textAreaUzenet);

		JLabel lblzenetSzvege = new JLabel("\u00DCzenet sz\u00F6vege:");
		lblzenetSzvege.setBounds(6, 87, 96, 16);
		layeredPane.add(lblzenetSzvege);

		JButton btnKlds = new JButton("Gener\u00E1l\u00E1s");
		btnKlds.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseReleased(MouseEvent e)
			{
				Generalas();
			}
		});
		btnKlds.setBounds(6, 358, 90, 28);
		layeredPane.add(btnKlds);

		Component[] c = layeredPane.getComponents();
		for(Component item : c)
		{
			try//MERT a JSpinner-nek máshogy kell KeyListener-t adni. Ha az objektum nem kasztolható JSpinner-be, akkor simán ad neki
			{
				((JSpinner.DefaultEditor) (((JSpinner) item).getEditor())).getTextField()
						.addKeyListener(MindenKomponensKeyadapter);
			}
			catch (Exception e)
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
	private JTextField textFieldTargy;
	private JTextArea textAreaUzenet;

	void Generalas()
	{
		FoClass.FolySzerkPeldany.KodBeillesztesVanKurzornalWhitespaceVizsgalo(" ");

		FoClass.FolySzerkPeldany.textAreaKod.insert("EmailKüld(\"" + textFieldCimzett.getText() +"\", \"" + textFieldTargy.getText() + "\", \"" + textAreaUzenet.getText().replace("\n", "\\n").replace("\t", "\\t").replace("\"", "\\\"") + "\");",
				FoClass.FolySzerkPeldany.textAreaKod.getCaretPosition());

		Szulo.frame.setEnabled(true);
		Szulo.Szulo.frame.setEnabled(true);
		Szulo.frame.dispose();
		frame.dispose();
	}
}
