package FoPackage;
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import javax.swing.JButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JCheckBox;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import org.eclipse.swt.widgets.Spinner;
import FoPackage.TCPCom;

public class RelePeriodizalo
{

	private JFrame frame;

	/**
	 * Create the application.
	 */
	public RelePeriodizalo()
	{
		initialize();
	}

	public boolean ShowDialog(Object szulo)
	{
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);//DISPOSE_ON_CLOSE: Nem zárja be a szülõablakot
		((FoAblak) szulo).frame.setEnabled(false);
		Szulo = (FoAblak) szulo;
		frame.setLocationRelativeTo(Szulo.frame);
		frame.setVisible(true);
		return true;
	}

	FoAblak Szulo;
	private JSpinner spinner;

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize()
	{
		frame = new JFrame();
		frame.setUndecorated(true);
		frame.setBounds(100, 100, 194, 38);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		frame.setBackground(new Color((float) (frame.getBackground().getRed() / (float) 256),
				(float) (frame.getBackground().getGreen() / (float) 256),
				(float) (frame.getBackground().getBlue() / (float) 256), 1.0f));

		JLayeredPane layeredPane = new JLayeredPane();
		frame.getContentPane().add(layeredPane, BorderLayout.CENTER);

		JLabel lblFelttel = new JLabel("F\u00E9lperi\u00F3dus (ms):");
		lblFelttel.setBounds(6, 12, 98, 16);
		layeredPane.add(lblFelttel);

		spinner = new JSpinner();
		spinner.setModel(new SpinnerNumberModel(500, 200, 24000, 100));
		spinner.setToolTipText("F\u00E9lperi\u00F3dus hossza ezredm\u00E1sodpercben. (Max: 24 000ms)");
		spinner.setBounds(116, 6, 73, 28);
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
							&& ertek > (double) (int) ((SpinnerNumberModel) spinner.getModel()).getMaximum())
					{
						spinner.setValue(((SpinnerNumberModel) spinner.getModel()).getMaximum());
					}
					else if(((SpinnerNumberModel) spinner.getModel()).getMinimum() != null
							&& ertek < (double) (int) ((SpinnerNumberModel) spinner.getModel()).getMinimum())
					{
						spinner.setValue(((SpinnerNumberModel) spinner.getModel()).getMinimum());
					}
					else
					{
						Ertekadas();
					}
				}
				catch (Exception e)
				{
					System.out.println(e.getMessage());
				}
			}
		}
	};

	void Ertekadas()
	{
		ObjTipus buffCelTip = ObjTipus.Int;//Érték típusa az értékadáshoz 
		int bufffertek = (int) spinner.getValue();
		try
		{
			HashMap<String, Object> modositandok = new LinkedHashMap<String, Object>();

			for(int item : Szulo.tablePort.getSelectedRows())
			{
				for(int i = 0; i < FoClass.PortokSzerver.size(); ++i)
				{
					if(FoClass.PortokSzerver.get(i).Nev.equals((String) Szulo.tablePort.getValueAt(item, 0)))
					{
						if(FoClass.PortokSzerver.get(i).GetSajatErtekTipus() == buffCelTip)
						{
							modositandok.put(FoClass.PortokSzerver.get(i).Nev, bufffertek);
						}
						break;
					}
				}
			}

			if(!TCPCom.PortErtekad(modositandok))
			{
				String[] ODoptionsX = new String[1];
				ODoptionsX[0] = new String("Tovább");
				JOptionPane.showOptionDialog(frame, "A port(ok) vezérlése sikertelen.\nPróbálja újra!",
						"Hiba a kapcsolatban", JOptionPane.OK_OPTION, JOptionPane.ERROR_MESSAGE, null, ODoptionsX,
						ODoptionsX[0]);
			}
			else
			{
				Szulo.frame.setEnabled(true);
				frame.dispose();
			}
		}
		catch (Exception e)
		{}
	}
}
