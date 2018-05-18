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
import java.util.Locale;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JCheckBox;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.text.html.HTMLDocument.HTMLReader.SpecialAction;
import FoPackage.FolySzerkSnippetkiv;

public class Snip_Varj
{

	private JFrame frame;

	/**
	 * Create the application.
	 */
	public Snip_Varj()
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
	private JSpinner spinner;

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize()
	{
		frame = new JFrame();
		frame.setUndecorated(true);
		frame.setBounds(100, 100, 294, 41);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		frame.setBackground(new Color((float) (frame.getBackground().getRed() / (float) 256),
				(float) (frame.getBackground().getGreen() / (float) 256),
				(float) (frame.getBackground().getBlue() / (float) 256), 1.0f));

		JLayeredPane layeredPane = new JLayeredPane();
		layeredPane.setToolTipText("A gener\u00E1l\u00E1shoz \u00FCss\u00F6n Entert!");
		frame.getContentPane().add(layeredPane, BorderLayout.CENTER);

		JLabel lblFelttel = new JLabel("V\u00E1rakoz\u00E1s id\u0151tartama (ms):");
		lblFelttel.setBounds(6, 12, 159, 16);
		layeredPane.add(lblFelttel);

		spinner = new JSpinner();
		spinner.setModel(new SpinnerNumberModel(new Integer(1000), new Integer(0), null, new Integer(100)));
		spinner.setBounds(177, 6, 110, 28);
		layeredPane.add(spinner);

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
					double ertek = (double) NumberFormat.getInstance().parse(s).doubleValue();
					if(((SpinnerNumberModel) spinner.getModel()).getMaximum() != null
							&& ertek > (int) ((SpinnerNumberModel) spinner.getModel()).getMaximum())
					{
						spinner.setValue(((SpinnerNumberModel) spinner.getModel()).getMaximum());
					}
					else if(((SpinnerNumberModel) spinner.getModel()).getMaximum() != null
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

		int kurzorsor = FoClass.FolySzerkPeldany.textAreaKod.getCaretLineNumber();

		String beillesztendo = "Várj(" + Integer.toString((int)spinner.getValue()) + ");";

		FoClass.FolySzerkPeldany.textAreaKod.insert(beillesztendo,
				FoClass.FolySzerkPeldany.textAreaKod.getCaretPosition());

		FoClass.FolySzerkPeldany.KodAutoFormat(kurzorsor);

		Szulo.frame.setEnabled(true);
		Szulo.Szulo.frame.setEnabled(true);
		Szulo.frame.dispose();
		frame.dispose();
	}
}
