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
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Label;
import FoPackage.FolySzerkSnippetkiv;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Snip_Folykezeles
{

	private JFrame frame;

	/**
	 * Create the application.
	 */
	public Snip_Folykezeles()
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
		frame.setBounds(100, 100, 222, 185);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		frame.setBackground(new Color((float) (frame.getBackground().getRed() / (float) 256),
				(float) (frame.getBackground().getGreen() / (float) 256),
				(float) (frame.getBackground().getBlue() / (float) 256), 1.0f));

		JLayeredPane layeredPane = new JLayeredPane();
		layeredPane.setToolTipText("A gener\u00E1l\u00E1shoz \u00FCss\u00F6n Entert!");
		frame.getContentPane().add(layeredPane, BorderLayout.CENTER);

		comboBox = new JComboBox();
		comboBox.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				switch(comboBox.getSelectedIndex())
				{
					case 0:
					{
						labelCboxSegitseg.setText("-Válasszon mûveletet!");
						break;
					}
					case 1:
					{
						labelCboxSegitseg.setText(
								"-Folyamat elindítása új szálon. Ha a folyamat már fut, nem történik változás.");
						break;
					}
					case 2:
					{
						labelCboxSegitseg
								.setText("-Folyamat leállítása. Ha a folyamat nem fut, nem történik változás.");
						break;
					}
					case 3:
					{
						labelCboxSegitseg.setText("-Folyamat újraindítása. Ha a folyamat nem fut, elindul.");
						break;
					}
					case 4:
					{
						labelCboxSegitseg.setText(
								"-A folyamat végrehajtása ezen a szálon. Nincs hatással a folyamat esetlegesen futó példányára.");
						break;
					}
					case 5:
					{
						labelCboxSegitseg.setText("-A folyamat Szüneteltetése.");
						break;
					}
					case 6:
					{
						labelCboxSegitseg.setText("-Szünetelõ folyamat folytatása.");
						break;
					}
					case 7:
					{
						labelCboxSegitseg
								.setText("-Logikai érték: Fut-e a folyamat? (Akkor is igaz, ha szünetel, de fut.)");
						break;
					}
					case 8:
					{
						labelCboxSegitseg.setText(
								"-Logikai érték: Szünetel-e a folyamat? (Akkor is hamis, ha nem fut a folyamat.)");
						break;
					}
					default:
					{
						labelCboxSegitseg.setText("");
						break;
					}
				}
				labelCboxSegitseg.setToolTipText(labelCboxSegitseg.getText());
			}
		});
		comboBox.setModel(new DefaultComboBoxModel(new String[] {
				"-V\u00E1lasszon m\u0171veletet!",
				"Folyamat Ind\u00EDt\u00E1sa",
				"Folyamat Le\u00E1ll\u00EDt\u00E1sa",
				"Folyamat \u00DAjraind\u00EDt\u00E1sa",
				"Folyamat Szinkron V\u00E9grehajt\u00E1sa",
				"Folyamat Felfüggesztése",
				"Folyamat Folytatása",
				"Folyamat Fut-e?",
				"Folyamat Szünetel-e?" }));
		comboBox.setBounds(6, 6, 209, 26);
		layeredPane.add(comboBox);

		labelCboxSegitseg = new JLabel("-V\u00E1lasszon m\u0171veletet!");
		labelCboxSegitseg.setBounds(16, 44, 261, 16);
		layeredPane.add(labelCboxSegitseg);

		JComboBox comboBox_1 = new JComboBox();
		comboBox_1.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				textField.setText((String) comboBox_1.getItemAt(comboBox_1.getSelectedIndex()));
			}
		});
		String[] kliensfolyamatok = new String[FoClass.FolyamatokKliens.size() + 1];

		kliensfolyamatok[0] = "-Válassza ki a folyamatot!";

		for(int i = 1; i < kliensfolyamatok.length; ++i)
		{
			kliensfolyamatok[i] = FoClass.FolyamatokKliens.get(i - 1).Nev;
		}

		comboBox_1.setModel(new DefaultComboBoxModel(kliensfolyamatok));
		comboBox_1.setBounds(6, 72, 209, 26);
		layeredPane.add(comboBox_1);

		textField = new JTextField();
		textField.setToolTipText("Folyamat neve. Ha a név nincs a legördülõ listán, ebben a mezõben módosíthatja azt.");
		textField.setBounds(6, 110, 209, 28);
		layeredPane.add(textField);
		textField.setColumns(10);

		JButton btnNewButton = new JButton("K\u00F3dr\u00E9szlet Gener\u00E1l\u00E1sa");
		btnNewButton.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseReleased(MouseEvent e)
			{
				Generalas();
			}
		});
		btnNewButton.setBounds(6, 150, 209, 28);
		layeredPane.add(btnNewButton);

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
				Generalas();
			}
		}
	};
	private JLabel labelCboxSegitseg;
	private JTextField textField;
	private JComboBox comboBox;

	void Generalas()
	{
		FoClass.FolySzerkPeldany.KodBeillesztesVanKurzornalWhitespaceVizsgalo(" ");

		int kurzorsor = FoClass.FolySzerkPeldany.textAreaKod.getCaretLineNumber();

		String beillesztendo = "";

		switch(comboBox.getSelectedIndex())
		{
			case 0:
			{
				return;
			}
			case 1:
			{
				beillesztendo = "FolyamatIndít(\"";
				break;
			}
			case 2:
			{
				beillesztendo = "FolyamatLeállít(\"";
				break;
			}
			case 3:
			{
				beillesztendo = "FolyamatÚjraindít(\"";
				break;
			}
			case 4:
			{
				beillesztendo = "FolyamatSzinkronVégrehajt(\"";
				break;
			}
			case 5:
			{
				beillesztendo = "FolyamatFelfüggeszt(\"";
				break;
			}
			case 6:
			{
				beillesztendo = "FolyamatFolytat(\"";
				break;
			}
			case 7:
			{
				beillesztendo = "FolyamatFut(\"";
				break;
			}
			case 8:
			{
				beillesztendo = "FolyamatSzünetel(\"";
				break;
			}
			default:
			{
				return;
			}
		}

		beillesztendo += textField.getText() + "\")";

		if(comboBox.getSelectedIndex() != 7 && comboBox.getSelectedIndex() != 8)
		{
			beillesztendo += ";";
		}

		FoClass.FolySzerkPeldany.textAreaKod.insert(beillesztendo,
				FoClass.FolySzerkPeldany.textAreaKod.getCaretPosition());

		FoClass.FolySzerkPeldany.KodAutoFormat(kurzorsor);

		Szulo.frame.setEnabled(true);
		Szulo.Szulo.frame.setEnabled(true);
		Szulo.frame.dispose();
		frame.dispose();
	}
}
