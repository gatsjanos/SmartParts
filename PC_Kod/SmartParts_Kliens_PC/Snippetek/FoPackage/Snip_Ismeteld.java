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
import FoPackage.FolySzerkSnippetkiv;

public class Snip_Ismeteld
{

	private JFrame frame;

	/**
	 * Create the application.
	 */
	public Snip_Ismeteld()
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
	private JTextField textField;
	private JSpinner spinner;

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize()
	{
		frame = new JFrame();
		frame.setUndecorated(true);
		frame.setBounds(100, 100, 234, 79);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		frame.setBackground(new Color((float) (frame.getBackground().getRed() / (float) 256),
				(float) (frame.getBackground().getGreen() / (float) 256),
				(float) (frame.getBackground().getBlue() / (float) 256), 1.0f));

		JLayeredPane layeredPane = new JLayeredPane();
		layeredPane.setToolTipText("A gener\u00E1l\u00E1shoz \u00FCss\u00F6n Entert!");
		frame.getContentPane().add(layeredPane, BorderLayout.CENTER);

		JLabel lblFelttel = new JLabel("Ism\u00E9tl\u00E9ssz\u00E1m:");
		lblFelttel.setBounds(6, 12, 88, 16);
		layeredPane.add(lblFelttel);

		spinner = new JSpinner();
		spinner.setModel(new SpinnerNumberModel(new Integer(5), new Integer(1), null, new Integer(1)));
		spinner.setBounds(104, 6, 88, 28);
		layeredPane.add(spinner);

		textField = new JTextField();
		textField.setToolTipText("Nem k\u00F6telez\u0151. \u00DCresen hagyhatja.");
		textField.setBounds(104, 45, 122, 28);
		layeredPane.add(textField);
		textField.setColumns(10);

		JLabel lblItercisVltoz = new JLabel("Iter\u00E1ci\u00F3s v\u00E1ltoz\u00F3:");
		lblItercisVltoz.setToolTipText(
				"A v\u00E1ltoz\u00F3 neve, ami tartalmazza, hogy az aktu\u00E1lis lefut\u00E1s a ciklus h\u00E1nyadik lefut\u00E1sa. (Nem k\u00F6telez\u0151)");
		lblItercisVltoz.setBounds(6, 51, 101, 16);
		layeredPane.add(lblItercisVltoz);

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
					String s = ((JSpinner.DefaultEditor) (((JSpinner) spinner).getEditor())).getTextField().getText();
					int ertek = (int) NumberFormat.getInstance().parse(s.replace(".", ",")).intValue();

					if(((SpinnerNumberModel) spinner.getModel()).getMaximum() != null
							&& ertek > (int) ((SpinnerNumberModel) spinner.getModel()).getMaximum())
					{
						spinner.setValue(((SpinnerNumberModel) spinner.getModel()).getMaximum());
					}
					else if(((SpinnerNumberModel) spinner.getModel()).getMinimum() != null
							&& ertek < (int) ((SpinnerNumberModel) spinner.getModel()).getMinimum())
					{
						spinner.setValue(((SpinnerNumberModel) spinner.getModel()).getMinimum());
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

	void Generalas()
	{
		FoClass.FolySzerkPeldany.KodBeillesztesVanKurzornalWhitespaceVizsgalo(" ");

		int kurzorsor = FoClass.FolySzerkPeldany.textAreaKod.getCaretLineNumber() + 2;//+2: eltolás az Ismételd kapcsoszárójelek közepére

		String beillesztendo = "Ismételd(" + String.format("%.0f", (double) (int) spinner.getValue());
		if(textField.getText().length() != 0)
		{
			beillesztendo += ", " + textField.getText();
		}
		beillesztendo += ")\n{\n\n}";

		FoClass.FolySzerkPeldany.textAreaKod.insert(beillesztendo,
				FoClass.FolySzerkPeldany.textAreaKod.getCaretPosition());

		FoClass.FolySzerkPeldany.KodAutoFormat(kurzorsor);

		Szulo.frame.setEnabled(true);
		Szulo.Szulo.frame.setEnabled(true);
		Szulo.frame.dispose();
		frame.dispose();
	}
}
