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
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import FoPackage.FolySzerkSnippetkiv;

public class Snip_ReleKockamuveletek
{

	private JFrame frame;

	/**
	 * Create the application.
	 */
	public Snip_ReleKockamuveletek()
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
		frame.setBounds(100, 100, 232, 166);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		frame.setBackground(new Color((float) (frame.getBackground().getRed() / (float) 256),
				(float) (frame.getBackground().getGreen() / (float) 256),
				(float) (frame.getBackground().getBlue() / (float) 256), 1.0f));

		JLayeredPane layeredPane = new JLayeredPane();
		frame.getContentPane().add(layeredPane, BorderLayout.CENTER);

		JButton btnErtleker = new JButton("\u00C9rt\u00E9klek\u00E9r\u00E9s");
		btnErtleker.setToolTipText(
				"A rel\u00E9 aktu\u00E1lis \u00E1llapot\u00E1nak lek\u00E9rdez\u00E9se. 0: KI, 1: BE, Egy\u00E9b sz\u00E1m: F\u00E9lperi\u00F3dus \u00E9rt\u00E9ke");
		btnErtleker.setBounds(10, 54, 100, 28);
		btnErtleker.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseReleased(MouseEvent arg0)
			{
				FoClass.FolySzerkPeldany.KodBeillesztesVanKurzornalWhitespaceVizsgalo(" ");
				FoClass.FolySzerkPeldany.textAreaKod.insert("RErtekleker(\"" + textField.getText() + "\")",
						FoClass.FolySzerkPeldany.textAreaKod.getCaretPosition());

				Szulo.frame.setEnabled(true);
				Szulo.Szulo.frame.setEnabled(true);
				Szulo.frame.dispose();
				frame.dispose();
			}
		});
		layeredPane.add(btnErtleker);

		JButton btnBekapcs = new JButton("Bekapcs");
		btnBekapcs.setToolTipText("Rel\u00E9 Kocka kimenet bekapcsol\u00E1sa");
		btnBekapcs.setBounds(10, 13, 100, 28);
		btnBekapcs.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseReleased(MouseEvent arg0)
			{
				FoClass.FolySzerkPeldany.KodBeillesztesVanKurzornalWhitespaceVizsgalo(" ");
				FoClass.FolySzerkPeldany.textAreaKod.insert("Bekapcs(\"" + textField.getText() + "\");",
						FoClass.FolySzerkPeldany.textAreaKod.getCaretPosition());

				Szulo.frame.setEnabled(true);
				Szulo.Szulo.frame.setEnabled(true);
				Szulo.frame.dispose();
				frame.dispose();
			}
		});
		layeredPane.add(btnBekapcs);

		JButton btnKikapcs = new JButton("Kikapcs");
		btnKikapcs.setToolTipText("Rel\u00E9 Kocka kimenet kikapcsol\u00E1sa");
		btnKikapcs.setBounds(122, 13, 100, 28);
		btnKikapcs.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseReleased(MouseEvent arg0)
			{
				FoClass.FolySzerkPeldany.KodBeillesztesVanKurzornalWhitespaceVizsgalo(" ");
				FoClass.FolySzerkPeldany.textAreaKod.insert("Kikapcs(\"" + textField.getText() + "\");",
						FoClass.FolySzerkPeldany.textAreaKod.getCaretPosition());

				Szulo.frame.setEnabled(true);
				Szulo.Szulo.frame.setEnabled(true);
				Szulo.frame.dispose();
				frame.dispose();
			}
		});
		layeredPane.add(btnKikapcs);

		JButton btnPeriodizal = new JButton("Periodiz\u00E1l");
		btnPeriodizal.setToolTipText(
				"A rel\u00E9 ki-be kapcsol\u00E1s\u00E1nak periodiz\u00E1l\u00E1sa. A rel\u00E9t \u00EDgy nem a k\u00F6zpont fogja kapcsolgatni, hanem \u00F6nmaga kapcsolgat periodikusan. (Am\u00EDg ezt egy, a k\u00F6zpontb\u00F3l kiadott \u00FAjabb parancs ezt meg nem v\u00E1ltoztatja.)");
		btnPeriodizal.setBounds(122, 54, 100, 28);
		btnPeriodizal.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseReleased(MouseEvent arg0)
			{
				try
				{
					int ertek = Integer.parseInt(
							((JSpinner.DefaultEditor) (((JSpinner) spinner).getEditor())).getTextField().getText());
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
						FoClass.FolySzerkPeldany.KodBeillesztesVanKurzornalWhitespaceVizsgalo(" ");
						FoClass.FolySzerkPeldany.textAreaKod.insert(
								"RPeriodizal(\"" + textField.getText() + "\", " + String.format("%.0f",spinner.getValue()) + ");",
								FoClass.FolySzerkPeldany.textAreaKod.getCaretPosition());

						Szulo.frame.setEnabled(true);
						Szulo.Szulo.frame.setEnabled(true);
						Szulo.frame.dispose();
						frame.dispose();
					}
				}
				catch (Exception e)
				{}
			}
		});
		layeredPane.add(btnPeriodizal);

		JLabel lblKockaNeve = new JLabel("Kocka neve:");
		lblKockaNeve.setBounds(10, 101, 66, 16);
		layeredPane.add(lblKockaNeve);

		textField = new JTextField();
		textField.setBounds(88, 95, 134, 28);
		layeredPane.add(textField);
		textField.setColumns(10);

		spinner = new JSpinner();
		spinner.setToolTipText(
				"Csak Periodiz\u00E1l\u00E1sn\u00E1l sz\u00FCks\u00E9ges \u00E9rt\u00E9k. (Max: 24000ms)");
		spinner.setModel(new SpinnerNumberModel(500, 200, 24000, 100));
		spinner.setBounds(122, 130, 100, 28);
		layeredPane.add(spinner);

		JLabel lblNewLabel = new JLabel("F\u00E9lperi\u00F3dus (ms):");
		lblNewLabel.setToolTipText("Csak Periodiz\u00E1l\u00E1sn\u00E1l sz\u00FCks\u00E9ges \u00E9rt\u00E9k.");
		lblNewLabel.setBounds(10, 136, 100, 16);
		layeredPane.add(lblNewLabel);

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
}
