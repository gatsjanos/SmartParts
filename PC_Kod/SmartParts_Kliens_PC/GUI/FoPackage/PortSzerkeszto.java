package FoPackage;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.awt.Window.Type;
import javax.swing.JTextArea;
import java.awt.Color;
import java.awt.SystemColor;
import javax.swing.border.BevelBorder;
import java.awt.Font;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import javax.swing.border.SoftBevelBorder;
import FoPackage.Port;
import FoPackage.TCPCom;

public class PortSzerkeszto
{

	JFrame frame;
	private JTextField textPortNev;

	/**
	 * Create the application.
	 */
	/**
	 * @wbp.parser.constructor
	 */

	public PortSzerkeszto()
	{
		initialize();
	}

	public PortSzerkeszto(String szerkPortNev)
	{
		Portszerk = true;
		SzerkPortNev = szerkPortNev;

		initialize();

		btnAutoFelismers.setEnabled(false);
		frame.setTitle("Okoskocka Szerkesztése: " + SzerkPortNev);
		btnPortMentese.setText("Módosítások mentése");
		textPortNev.setText(SzerkPortNev);
		for(Port item : FoClass.PortokSzerver)
		{
			if(SzerkPortNev.equals(item.Nev))
			{
				SetTipus(item.getPortTipusa());
				spinner.setValue(item.RFAzonosito);
				break;
			}
		}
	}

	public boolean ShowDialog(Object szulo)
	{
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);//DISPOSE_ON_CLOSE: Nem zárja be a szülõablakot
		Szulo = (FoAblak) szulo;
		Szulo.frame.setEnabled(false);
		frame.setLocationRelativeTo(Szulo.frame);
		frame.setVisible(true);
		return true;
	}

	FoAblak Szulo;
	boolean Portszerk = false;
	String SzerkPortNev;
	String PortNevAlapszoveg = "Írja ide a kocka nevét!";
	private JTextField textFieldFOCUSELVEVO;
	private JButton btnPortMentese;
	private JComboBox comboBox;
	private JSpinner spinner;
	private JButton btnAutoFelismers;

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize()
	{
		frame = new JFrame();
		frame.setType(Type.UTILITY);
		frame.setResizable(false);
		frame.setTitle("\u00DAj Okoskocka Hozz\u00E1ad\u00E1sa");
		frame.setBounds(100, 100, 244, 247);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JLayeredPane layeredPane = new JLayeredPane();
		frame.getContentPane().add(layeredPane, BorderLayout.CENTER);

		frame.addWindowListener(new java.awt.event.WindowAdapter()
		{
			@Override
			public void windowClosing(java.awt.event.WindowEvent windowEvent)
			{
				//				String[] ODoptions = new String[2];
				//				ODoptions[0] = new String("Kilépek mentés nélkül");
				//				ODoptions[1] = new String("Folytatom a szerkesztést");
				//				if(JOptionPane.showOptionDialog(frame, "Bizosan kilépsz mentés nélkül?", "Biztosan kilépsz?", 0,
				//						JOptionPane.YES_NO_OPTION, null, ODoptions, ODoptions[1]) == JOptionPane.YES_OPTION)
				{
					Szulo.frame.setEnabled(true);
					frame.dispose();
				}
			}
		});
		textFieldFOCUSELVEVO = new JTextField();
		textFieldFOCUSELVEVO.setEditable(false);
		textFieldFOCUSELVEVO.setColumns(10);
		textFieldFOCUSELVEVO.setBounds(128, -22, -19, 28);
		layeredPane.add(textFieldFOCUSELVEVO);

		btnAutoFelismers = new JButton("");
		btnAutoFelismers.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseReleased(MouseEvent arg0)
			{
				if(textPortNev.getText().equals("") || textPortNev.getText().equals(PortNevAlapszoveg))
				{
					JOptionPane.showMessageDialog(frame, "A kocka nem adható hozzá név nélkül.",
							"Adjon nevet az Okoskockának!", JOptionPane.WARNING_MESSAGE);
				}
				else
				{
					////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
					if(!textPortNev.getText().matches("[a-zA-Z0-9_ áéíóöõúüûÁÉÍÓÖÕÚÜÛ]+"))
					{
						JOptionPane.showMessageDialog(frame,
								"A kocka neve csak kis és nagybetüket, számokat, szóközt, illetve '_' karaktert tartalmazhat.",
								"Névhiba", JOptionPane.WARNING_MESSAGE);
					}
					else
					{////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
						boolean vanmarilyenNev = false;

						for(Port item : FoClass.PortokSzerver)
						{
							if(!(Portszerk && item.Nev.equals(SzerkPortNev)))//Nincs egyezésvizsgálat, ha portot szerkesztünk és az új név megegyezik az eredetivel
							{
								if(textPortNev.getText().equals(item.Nev))
								{
									vanmarilyenNev = true;
								}
							}
						}

						if(vanmarilyenNev)
						{
							if(vanmarilyenNev)
							{
								JOptionPane.showMessageDialog(frame, "Ilyen nevû kocka már létezik.",
										"Duplikált Név", JOptionPane.WARNING_MESSAGE);
							}
						}
						else
						{
							if(TCPCom.KockaHozzaadoI2C(textPortNev.getText()))
							{
								Szulo.frame.setEnabled(true);
								frame.dispose();
							}
							else
							{
								String[] ODoptionsX = new String[1];
								ODoptionsX[0] = new String("Tovább");
								JOptionPane.showOptionDialog(frame, "A szerver nem érhetõ el.", "Hiba a kapcsolatban",
										JOptionPane.OK_OPTION, JOptionPane.ERROR_MESSAGE, null, ODoptionsX, ODoptionsX[0]);
							}
						}
					}
				}
			}
		});
		btnAutoFelismers.setBounds(6, 46, 226, 28);
		btnAutoFelismers.setLayout(new BorderLayout());
		btnAutoFelismers.setToolTipText(
				"Csatlakoztassa az Okoskock\u00E1t a k\u00F6zponti egys\u00E9ghez, majd kattintson ide az automatikus felismer\u00E9shez.");
		btnAutoFelismers.setText("    Kocka Automatikus Ment\u00E9se    ");
		layeredPane.add(btnAutoFelismers);

		textPortNev = new JTextField();
		textPortNev.setHorizontalAlignment(SwingConstants.CENTER);
		textPortNev.setBounds(6, 6, 226, 28);
		layeredPane.add(textPortNev);
		textPortNev.setColumns(10);
		textPortNev.setText(PortNevAlapszoveg);
		textPortNev.addFocusListener(new FocusAdapter()
		{
			@Override
			public void focusGained(FocusEvent arg0)
			{
				if(textPortNev.getText().equals(PortNevAlapszoveg))
				{
					textPortNev.setText("");
				}
			}

			@Override
			public void focusLost(FocusEvent e)
			{
				if(textPortNev.getText().equals(""))
				{
					textPortNev.setText(PortNevAlapszoveg);
				}
			}
		});
		comboBox = new JComboBox();
		comboBox.setModel(new DefaultComboBoxModel(new String[] {
				"-V\u00E1lassza ki a kocka t\u00EDpus\u00E1t!",
				"Kapcsol\u00F3 (2 \u00E1ll\u00E1s\u00FA)",
				"Nyom\u00F3gomb",
				"Ajt\u00F3nyit\u00E1s \u00C9rz\u00E9kel\u0151",
				"Rel\u00E9",
				"LED Vez\u00E9rl\u0151 (1 csatorn\u00E1s)" }));
		comboBox.setBounds(6, 115, 226, 26);
		layeredPane.add(comboBox);

		JLabel lblAzonost = new JLabel("Okoskocka azonos\u00EDt\u00F3ja:");
		lblAzonost.setBounds(6, 158, 135, 16);
		layeredPane.add(lblAzonost);

		spinner = new JSpinner();
		spinner.setBackground(SystemColor.activeCaptionBorder);
		spinner.setModel(new SpinnerNumberModel(100, 2, 4095, 1));
		spinner.setBounds(145, 152, 87, 28);
		layeredPane.add(spinner);

		btnPortMentese = new JButton("\u2191\u2191  Kocka Manu\u00E1lis Ment\u00E9se  \u2191\u2191");
		btnPortMentese.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseClicked(MouseEvent arg0)
			{
				try
				{////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
					if(comboBox.getSelectedIndex() == 0)
					{
						JOptionPane.showMessageDialog(frame, "Válassza ki az Okoskocka típusát!",
								"Hiányos kockatípus", JOptionPane.WARNING_MESSAGE);
					}
					else
					{
						////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
						if(textPortNev.getText().equals("") || textPortNev.getText().equals(PortNevAlapszoveg))
						{
							JOptionPane.showMessageDialog(frame, "A kocka nem adható hozzá név nélkül.",
									"Adjon nevet az Okoskockának!", JOptionPane.WARNING_MESSAGE);
						}
						else
						{
							////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
							if(!textPortNev.getText().matches("[a-zA-Z0-9_ áéíóöõúüûÁÉÍÓÖÕÚÜÛ]+"))
							{
								JOptionPane.showMessageDialog(frame,
										"A kocka neve csak kis és nagybetüket, számokat, szóközt, illetve '_' karaktert tartalmazhat.",
										"Névhiba", JOptionPane.WARNING_MESSAGE);
							}
							else
							{
								////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
								boolean vanmarilyenNev = false;
								boolean vanmarilyenID = false;

								for(Port item : FoClass.PortokSzerver)
								{
									if(!(Portszerk && item.Nev.equals(SzerkPortNev)))//Nincs egyezésvizsgálat, ha portot szerkesztünk és az új név megegyezik az eredetivel
									{
										if(textPortNev.getText().equals(item.Nev))
										{
											vanmarilyenNev = true;
										}

										if((int) spinner.getValue() == item.RFAzonosito)
										{
											vanmarilyenID = true;
										}
									}
								}

								if(vanmarilyenNev || vanmarilyenID)
								{
									if(vanmarilyenNev)
									{
										JOptionPane.showMessageDialog(frame,
												"Ilyen nevû kocka már létezik.", "Duplikált Név",
												JOptionPane.WARNING_MESSAGE);
									}
									else if(vanmarilyenID)
									{
										JOptionPane.showMessageDialog(frame,
												"Ilyen azonosítójú kocka már létezik.", "Duplikált Azonosító",
												JOptionPane.WARNING_MESSAGE);
									}
								}
								else
								{
									////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
									if(Portszerk)
									{
										for(int i = 0; i < FoClass.PortokSzerver.size(); ++i)
										{
											if(FoClass.PortokSzerver.get(i).Nev.equals(SzerkPortNev))
											{
												FoClass.PortokSzerver.remove(i);

												List<String> torlendok = new ArrayList<String>();
												torlendok.add(SzerkPortNev);
												TCPCom.PortTorlo(torlendok);
											}
										}
									}

									List<Port> buff = new ArrayList<Port>();
									buff.add(new Port(GetTipus(), (int) spinner.getValue(), textPortNev.getText()));

									FoClass.PortokSzerver.add(buff.get(0));
									try
									{

										TCPCom.PortHozzaad(buff);

										Szulo.PortokListaFrissit(true);
									}
									catch (Exception ex)
									{

									}
									Szulo.frame.setEnabled(true);
									frame.dispose();
								}
							}

						}
					}
				}
				catch (Exception e)
				{

				}
			}
		});
		btnPortMentese.setBounds(6, 186, 226, 28);
		layeredPane.add(btnPortMentese);

		JLabel lblHalad = new JLabel("HALAD\u00D3");
		lblHalad.setBorder(null);
		lblHalad.setFont(new Font("SansSerif", Font.BOLD, 16));
		lblHalad.setForeground(SystemColor.info);
		lblHalad.setHorizontalAlignment(SwingConstants.CENTER);
		lblHalad.setHorizontalTextPosition(SwingConstants.CENTER);
		lblHalad.setBounds(6, 92, 226, 20);
		layeredPane.add(lblHalad);

		JTextArea textAreaDESIGN = new JTextArea();
		textAreaDESIGN.setBorder(new EtchedBorder(EtchedBorder.RAISED, SystemColor.textHighlight, null));
		textAreaDESIGN.setBackground(SystemColor.activeCaption);
		textAreaDESIGN.setEditable(false);
		textAreaDESIGN.setBounds(0, 82, 238, 136);
		layeredPane.add(textAreaDESIGN);
	}

	void SetTipus(PortTipus be)
	{
		switch(be)
		{
			case BEKapcsolo:
			{
				comboBox.setSelectedIndex(1);
				break;
			}
			case BENyomogomb:
			{
				comboBox.setSelectedIndex(2);
				break;
			}
			case BEAjtoNyitas:
			{
				comboBox.setSelectedIndex(3);
				break;
			}
			case KIRele:
			{
				comboBox.setSelectedIndex(4);
				break;
			}
			case KILed1csat:
			{
				comboBox.setSelectedIndex(5);
				break;
			}
			default:
			{
				comboBox.setSelectedIndex(0);
				break;
			}
		}
	}

	PortTipus GetTipus()
	{
		switch(comboBox.getSelectedIndex())
		{
			case 1:
			{
				return PortTipus.BEKapcsolo;
			}
			case 2:
			{
				return PortTipus.BENyomogomb;
			}
			case 3:
			{
				return PortTipus.BEAjtoNyitas;
			}
			case 4:
			{
				return PortTipus.KIRele;
			}
			case 5:
			{
				return PortTipus.KILed1csat;
			}
			default:
			{
				return PortTipus.KIRele;
			}
		}
	}
}
