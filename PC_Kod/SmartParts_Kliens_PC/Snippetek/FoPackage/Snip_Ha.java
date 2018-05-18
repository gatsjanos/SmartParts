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

public class Snip_Ha
{

	private JFrame frame;

	/**
	 * Create the application.
	 */
	public Snip_Ha()
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
	private JCheckBox chckbxEgybkntg;

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize()
	{
		frame = new JFrame();
		frame.setUndecorated(true);
		frame.setBounds(100, 100, 195, 106);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		frame.setBackground(new Color((float) (frame.getBackground().getRed() / (float) 256),
				(float) (frame.getBackground().getGreen() / (float) 256),
				(float) (frame.getBackground().getBlue() / (float) 256), 1.0f));

		JLayeredPane layeredPane = new JLayeredPane();
		frame.getContentPane().add(layeredPane, BorderLayout.CENTER);

		textField = new JTextField();
		textField.setToolTipText(
				"Adja meg a ciklus felt\u00E9tel\u00E9t, (vagy hagyja \u00FCresen,) majd \u00FCss\u00F6n Entert a gener\u00E1l\u00E1shoz!");
		textField.setBounds(55, 6, 122, 28);
		layeredPane.add(textField);
		textField.setColumns(10);

		JLabel lblFelttel = new JLabel("Felt\u00E9tel:");
		lblFelttel.setBounds(6, 12, 55, 16);
		layeredPane.add(lblFelttel);
		
		chckbxEgybkntg = new JCheckBox("Egy\u00E9bk\u00E9nt \u00C1g");
		chckbxEgybkntg.setBounds(55, 46, 104, 18);
		layeredPane.add(chckbxEgybkntg);

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
			else if(arg0.getKeyCode() == KeyEvent.VK_ENTER)
			{
				Generalas();
			}
		}
	};
	void Generalas()
	{
		FoClass.FolySzerkPeldany.KodBeillesztesVanKurzornalWhitespaceVizsgalo(" ");
		
		int kurzorsor = FoClass.FolySzerkPeldany.textAreaKod.getCaretLineNumber() + 2;//+2: eltolás a Ha kapcsoszárójelek közepére

		FoClass.FolySzerkPeldany.textAreaKod.insert("Ha(" + textField.getText() + ")\n{\n\n}",
				FoClass.FolySzerkPeldany.textAreaKod.getCaretPosition());
		if(chckbxEgybkntg.isSelected())
		{

			FoClass.FolySzerkPeldany.textAreaKod.insert("\nEgyébként\n{\n\n}",
					FoClass.FolySzerkPeldany.textAreaKod.getCaretPosition());
		}
		FoClass.FolySzerkPeldany.KodAutoFormat(kurzorsor);
		
		Szulo.frame.setEnabled(true);
		Szulo.Szulo.frame.setEnabled(true);
		Szulo.frame.dispose();
		frame.dispose();
	}
}
