package FoPackage;
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import javax.swing.JButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.NumberFormat;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JCheckBox;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.text.html.HTMLDocument.HTMLReader.SpecialAction;
import FoPackage.FolySzerkSnippetkiv;

public class Snip_Gyok
{

	private JFrame frame;

	/**
	 * Create the application.
	 */
	public Snip_Gyok()
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
	private JSpinner spinnerKitevo;

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize()
	{
		frame = new JFrame();
		frame.setUndecorated(true);
		frame.setBounds(100, 100, 196, 79);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		frame.setBackground(new Color((float) (frame.getBackground().getRed() / (float) 256),
				(float) (frame.getBackground().getGreen() / (float) 256),
				(float) (frame.getBackground().getBlue() / (float) 256), 1.0f));

		JLayeredPane layeredPane = new JLayeredPane();
		layeredPane.setToolTipText("A gener\u00E1l\u00E1shoz \u00FCss\u00F6n Entert!");
		frame.getContentPane().add(layeredPane, BorderLayout.CENTER);

		JLabel lblFelttel = new JLabel("Gy\u00F6kkitev\u0151:");
		lblFelttel.setBounds(6, 12, 88, 16);
		layeredPane.add(lblFelttel);

		spinnerKitevo = new JSpinner();
		spinnerKitevo.setModel(new SpinnerNumberModel(new Double(2), null, null, new Double(0.5)));
		spinnerKitevo.setBounds(78, 6, 110, 28);
		layeredPane.add(spinnerKitevo);

		JLabel lblItercisVltoz = new JLabel("Alap:");
		lblItercisVltoz.setToolTipText(
				"A v\u00E1ltoz\u00F3 neve, ami tartalmazza, hogy az aktu\u00E1lis lefut\u00E1s a ciklus h\u00E1nyadik lefut\u00E1sa. (Nem k\u00F6telez\u0151)");
		lblItercisVltoz.setBounds(6, 51, 101, 16);
		layeredPane.add(lblItercisVltoz);

		spinnerAlap = new JSpinner();
		spinnerAlap.setModel(new SpinnerNumberModel(new Double(0), null, null, new Double(0.5)));
		spinnerAlap.setBounds(78, 45, 110, 28);
		layeredPane.add(spinnerAlap);

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
			else if(arg0.getKeyCode() == KeyEvent.VK_ENTER)
			{

				try
				{
					String s = ((JSpinner.DefaultEditor) (((JSpinner) spinnerKitevo).getEditor())).getTextField()
							.getText();
					double ertekKit = (double) NumberFormat.getInstance().parse(s.replace(".", ",")).doubleValue();

					s = ((JSpinner.DefaultEditor) (((JSpinner) spinnerAlap).getEditor())).getTextField().getText();
					double ertekAl = (double) NumberFormat.getInstance().parse(s.replace(".", ",")).doubleValue();

					if(((SpinnerNumberModel) spinnerKitevo.getModel()).getMaximum() != null
							&& ertekKit > (double) ((SpinnerNumberModel) spinnerKitevo.getModel()).getMaximum())
					{
						spinnerKitevo.setValue(((SpinnerNumberModel) spinnerKitevo.getModel()).getMaximum());
					}
					else if(((SpinnerNumberModel) spinnerKitevo.getModel()).getMaximum() != null
							&& ertekKit < (double) ((SpinnerNumberModel) spinnerKitevo.getModel()).getMinimum())
					{
						spinnerKitevo.setValue(((SpinnerNumberModel) spinnerKitevo.getModel()).getMinimum());
					}
					else if(((SpinnerNumberModel) spinnerAlap.getModel()).getMaximum() != null
							&& ertekAl > (double) ((SpinnerNumberModel) spinnerAlap.getModel()).getMaximum())
					{
						spinnerAlap.setValue(((SpinnerNumberModel) spinnerAlap.getModel()).getMaximum());
					}
					else if(((SpinnerNumberModel) spinnerAlap.getModel()).getMaximum() != null
							&& ertekAl < (double) ((SpinnerNumberModel) spinnerAlap.getModel()).getMinimum())
					{
						spinnerAlap.setValue(((SpinnerNumberModel) spinnerAlap.getModel()).getMinimum());
					}
					else
					{
						Generalas();
					}
				}
				catch (Exception e)
				{
					System.out.println(e.getMessage());
				}
			}
		}
	};
	private JSpinner spinnerAlap;

	void Generalas()
	{
		FoClass.FolySzerkPeldany.KodBeillesztesVanKurzornalWhitespaceVizsgalo(" ");

		int kurzorsor = FoClass.FolySzerkPeldany.textAreaKod.getCaretLineNumber();

		String beillesztendo = "Gyök(" + String.format("%.0f", spinnerAlap.getValue()) + ", " + String.format("%.0f", spinnerKitevo.getValue()) + ")";

		FoClass.FolySzerkPeldany.textAreaKod.insert(beillesztendo,
				FoClass.FolySzerkPeldany.textAreaKod.getCaretPosition());

		FoClass.FolySzerkPeldany.KodAutoFormat(kurzorsor);

		Szulo.frame.setEnabled(true);
		Szulo.Szulo.frame.setEnabled(true);
		Szulo.frame.dispose();
		frame.dispose();
	}
}
