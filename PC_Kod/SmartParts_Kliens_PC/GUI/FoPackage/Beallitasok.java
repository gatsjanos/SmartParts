package FoPackage;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.JRadioButton;
import java.awt.Color;
import javax.swing.ButtonGroup;
import java.awt.Toolkit;
import javax.swing.JButton;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;

public class Beallitasok
{

	JFrame frame;
	private JTextField TextFieldSzevercim;
	private final ButtonGroup buttonGroup = new ButtonGroup();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args)
	{
		EventQueue.invokeLater(new Runnable()
		{
			public void run()
			{
				try
				{
					Beallitasok window = new Beallitasok();
					window.frame.setVisible(true);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Beallitasok()
	{
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize()
	{
		frame = new JFrame();
		frame.setResizable(false);
		frame.setTitle("Be\u00E1ll\u00EDt\u00E1sok");
		frame.setBounds(100, 100, 411, 207);
		frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		frame.setLocationRelativeTo(FoClass.FoAblakPeldany.frame);

		JLayeredPane layeredPane = new JLayeredPane();
		frame.getContentPane().add(layeredPane, BorderLayout.CENTER);

		TextFieldSzevercim = new JTextField();
		TextFieldSzevercim.setText("localhost");
		TextFieldSzevercim.setBounds(238, 81, 138, 28);
		layeredPane.add(TextFieldSzevercim);
		TextFieldSzevercim.setColumns(10);

		JSpinner spinnerSzerverPort = new JSpinner();
		spinnerSzerverPort.setModel(new SpinnerNumberModel(33000, 0, 999999, 1));
		spinnerSzerverPort.setBounds(296, 18, 80, 28);
		layeredPane.add(spinnerSzerverPort);
		spinnerSzerverPort.setValue(FoClass.TCPPortSzam);

		JLabel lblPort = new JLabel("Port:");
		lblPort.setBounds(261, 24, 25, 16);
		layeredPane.add(lblPort);

		JLabel lblKapcsoldsASmartparts = new JLabel("Kapcsol\u00F3d\u00E1s a SmartParts Szerverre:");
		lblKapcsoldsASmartparts.setBounds(31, 24, 205, 16);
		layeredPane.add(lblKapcsoldsASmartparts);

		JRadioButton rdbtnUdpBroadcasting = new JRadioButton("UDP Broadcasting");
		rdbtnUdpBroadcasting.addItemListener(new ItemListener()
		{
			public void itemStateChanged(ItemEvent arg0)
			{
				int state = arg0.getStateChange();
				if(state == ItemEvent.SELECTED)
				{
					TextFieldSzevercim.setEnabled(false);
				}
				else if(state == ItemEvent.DESELECTED)
				{
					TextFieldSzevercim.setEnabled(true);
				}
			}
		});
		rdbtnUdpBroadcasting.setSelected(true);
		buttonGroup.add(rdbtnUdpBroadcasting);
		rdbtnUdpBroadcasting.setBounds(60, 57, 138, 18);
		layeredPane.add(rdbtnUdpBroadcasting);

		JRadioButton rdbtnCmManulisMegadsa = new JRadioButton("C\u00EDm manu\u00E1lis megad\u00E1sa:");
		buttonGroup.add(rdbtnCmManulisMegadsa);
		rdbtnCmManulisMegadsa.setBounds(60, 86, 166, 18);
		layeredPane.add(rdbtnCmManulisMegadsa);

		JButton btnMents = new JButton("Ment\u00E9s");
		btnMents.setBounds(299, 134, 90, 28);
		layeredPane.add(btnMents);
	}
}
