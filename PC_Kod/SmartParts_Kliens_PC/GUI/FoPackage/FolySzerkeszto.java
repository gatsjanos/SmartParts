package FoPackage;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JFrame;
import java.awt.BorderLayout;
import javax.swing.JRadioButton;
import javax.swing.JLayeredPane;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import org.fife.ui.rsyntaxtextarea.AbstractTokenMakerFactory;
import org.fife.ui.rsyntaxtextarea.SyntaxScheme;
import org.fife.ui.rsyntaxtextarea.TokenMakerFactory;
import org.fife.ui.rtextarea.ConfigurableCaret;
import org.fife.ui.rtextarea.RTextScrollPane;
import FoPackage.Eszkozok;
import FoPackage.Port;
import Szinkodolo.SajatRSyntaxTextArea;
import javax.swing.SwingConstants;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import javax.swing.ScrollPaneConstants;
import java.awt.Font;

public class FolySzerkeszto
{
	public enum EgyMuvTip
	{
		Bekapcs, Kikapcs, Email;//Olyan sorrendben legyenek az elemek, mint a comboboxban!

		public static EgyMuvTip getEnum(int be)
		{
			switch(be)
			{
				case 0:
					return Bekapcs;
				case 1:
					return Kikapcs;
				case 2:
					return Email;
			}
			return Bekapcs;
		}

		public static int getInt(EgyMuvTip be)
		{
			switch(be)
			{
				case Bekapcs:
					return 0;
				case Kikapcs:
					return 1;
				case Email:
					return 2;
			}
			return 0;
		}
	}

	public FolySzerkeszto()
	{
		initialize();
		frame.setBounds(100, 100, 710, 233);
	}

	public FolySzerkeszto(String szerkFolyNev)
	{
		Folyszerk = true;
		SzerkFolyNev = szerkFolyNev;

		initialize();
		frame.setBounds(100, 100, 710, 233);

		frame.setTitle("Folyamat Szerkesztése: " + SzerkFolyNev);
		btnMentes.setText("Módosítások mentése");
		textFolyNev.setText(SzerkFolyNev);
		for(KliFolyamat item : FoClass.FolyamatokKliens)
		{
			if(szerkFolyNev.equals(item.Nev))
			{
				SetEsemeny(item.Esemeny);
				SetKod(item.Kod);
				break;
			}
		}

	}

	public boolean ShowDialog(Object szulo)
	{
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);//DISPOSE_ON_CLOSE: Nem zárja be a szülõablakot
		((FolyamatListazo) szulo).frame.setEnabled(false);
		Szulo = (FolyamatListazo) szulo;
		frame.setLocationRelativeTo(Szulo.frame);
		frame.setVisible(true);
		return true;
	}

	FolyamatListazo Szulo;
	boolean Folyszerk = false;
	String SzerkFolyNev;
	String FolyNevAlapszoveg = "Írja ide a folyamat nevét!";

	protected JFrame frame;
	private JSpinner spinnerHonap;
	private final ButtonGroup buttonGroup = new ButtonGroup();
	private RTextScrollPane scrollPaneKod;
	public SajatRSyntaxTextArea textAreaKod;
	private JTextField textFolyNev;
	private JTextField textFieldFOCUSELVEVO;
	private JRadioButton radioProgramozas = new JRadioButton("Folyamat programoz\u00E1sa");
	private JComboBox comboBoxEsemTip = new JComboBox();
	private JComboBox comboBoxEsemPortok = new JComboBox();
	private JRadioButton radioEgyszeruMuv = new JRadioButton("Egyszer\u0171 m\u0171velet:");
	private JCheckBox checkBoxSec;
	private JCheckBox checkBoxPerc = new JCheckBox("");
	private JCheckBox checkBoxOra = new JCheckBox("");
	private JCheckBox checkBoxHonapNapja = new JCheckBox("");
	private JCheckBox checkBoxHetNapja = new JCheckBox("");
	private JCheckBox checkBoxHonap = new JCheckBox("");
	private JSpinner spinnerSec = new JSpinner();
	private JCheckBox checkBoxEv = new JCheckBox("");
	private JSpinner spinnerPerc;
	private JSpinner spinnerOra;
	private JSpinner spinnerHetNapja = new JSpinner();
	private JSpinner spinnerHonapNapja = new JSpinner();
	private JRadioButton radioEsemeny = new JRadioButton("Ind\u00EDt\u00E1s Esem\u00E9nyre:");
	private JRadioButton radioUtemez = new JRadioButton("\u00DCtemezett Ind\u00EDt\u00E1s:");
	private JComboBox comboBoxEgyMuvTip = new JComboBox();
	private JComboBox comboBoxEgyMuvPortok = new JComboBox();
	private JSpinner spinnerEv = new JSpinner();
	private JButton btnMentes = new JButton("Folyamat ment\u00E9se");
	private JTextField textFieldEmailUzenet;
	private JTextField textFieldEmailCim;
	private JPopupMenu popupMenu;
	private JMenuItem mntmSnipBeszur;
	private JMenuItem mntmAutoFormat;

	/**
	 * @wbp.parser.constructor
	 */
	public FolySzerkeszto(boolean b1, boolean b2, boolean b3, boolean b4, boolean b5)//NEM HASZNÁLT PARAMÉTEREK A PARSEING MIATT
	{
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize()
	{
		frame = new JFrame();
		frame.setTitle("\u00DAj Folyamat L\u00E9trehoz\u00E1sa");
		frame.addComponentListener(new ComponentAdapter()
		{
			@Override
			public void componentResized(ComponentEvent arg0)
			{
				try
				{
					scrollPaneKod.setBounds((int) scrollPaneKod.getBounds().getX(),
							(int) scrollPaneKod.getBounds().getY(), frame.getBounds().width - 30,
							frame.getBounds().height - 243);
					scrollPaneKod.setViewportView(textAreaKod);
				}
				catch (NullPointerException e)
				{
					e.printStackTrace();
				}
			}
		});
		frame.setBounds(100, 100, 710, 540);

		frame.addWindowListener(new java.awt.event.WindowAdapter()
		{
			@Override
			public void windowClosing(java.awt.event.WindowEvent windowEvent)
			{
				String[] ODoptions = new String[2];
				ODoptions[0] = new String("Kilépek mentés nélkül");
				ODoptions[1] = new String("Folytatom a szerkesztést");
				if(JOptionPane.showOptionDialog(frame, "Bizosan kilépsz mentés nélkül?", "Biztosan kilépsz?", 0,
						JOptionPane.YES_NO_OPTION, null, ODoptions, ODoptions[1]) == JOptionPane.YES_OPTION)
				{
					Szulo.frame.setEnabled(true);
					frame.dispose();
				}
			}
		});

		JLayeredPane layeredPane = new JLayeredPane();
		frame.getContentPane().add(layeredPane, BorderLayout.CENTER);

		textFieldEmailCim = new JTextField();
		textFieldEmailCim.setVisible(false);
		textFieldEmailCim.setToolTipText("Email c\u00EDm");
		textFieldEmailCim.setHorizontalAlignment(SwingConstants.LEFT);
		textFieldEmailCim.setColumns(10);
		textFieldEmailCim.setBounds(270, 125, 133, 28);
		layeredPane.add(textFieldEmailCim);

		textFieldEmailUzenet = new JTextField();
		textFieldEmailUzenet.setVisible(false);
		textFieldEmailUzenet.setToolTipText("\u00DCzenet sz\u00F6vege");
		textFieldEmailUzenet.setHorizontalAlignment(SwingConstants.LEFT);
		textFieldEmailUzenet.setColumns(10);
		textFieldEmailUzenet.setBounds(406, 125, 276, 28);
		layeredPane.add(textFieldEmailUzenet);

		radioUtemez.setToolTipText(
				"A folyamat ind\u00EDt\u00E1s\u00E1nak id\u0151pontja. - Az \u00FCres jel\u00F6l\u0151n\u00E9gyzetek hely\u00E9re az \u00F6sszes lehets\u00E9ges \u00E9rt\u00E9k behelyettes\u00EDt\u00E9sre ker\u00FCl.");
		radioUtemez.setBounds(18, 91, 123, 18);
		layeredPane.add(radioUtemez);

		radioEsemeny.setSelected(true);
		radioEsemeny.setBounds(18, 56, 133, 18);
		layeredPane.add(radioEsemeny);

		spinnerEv.setToolTipText("\u00C9v");
		spinnerEv.setModel(new SpinnerNumberModel(2016, 1, 3000, 1));
		spinnerEv.setBounds(153, 86, 63, 28);
		spinnerEv.setEditor(new JSpinner.NumberEditor(spinnerEv, "#"));
		layeredPane.add(spinnerEv);

		spinnerHonap = new JSpinner();
		spinnerHonap.setToolTipText("H\u00F3nap");
		spinnerHonap.setModel(new SpinnerNumberModel(12, 1, 12, 1));
		spinnerHonap.setBounds(244, 86, 48, 28);
		layeredPane.add(spinnerHonap);

		spinnerHonapNapja.setToolTipText("H\u00F3nap napja");
		spinnerHonapNapja.setModel(new SpinnerNumberModel(15, 1, 31, 1));
		spinnerHonapNapja.setBounds(320, 86, 48, 28);
		layeredPane.add(spinnerHonapNapja);

		spinnerHetNapja.setToolTipText("H\u00E9t napja:   1: H\u00E9tf\u0151 - 7: Vas\u00E1rnap");
		spinnerHetNapja.setModel(new SpinnerNumberModel(1, 1, 7, 1));
		spinnerHetNapja.setBounds(396, 86, 48, 28);
		layeredPane.add(spinnerHetNapja);

		spinnerOra = new JSpinner();
		spinnerOra.setToolTipText("\u00D3ra");
		spinnerOra.setModel(new SpinnerNumberModel(16, 0, 23, 1));
		spinnerOra.setBounds(472, 86, 48, 28);
		layeredPane.add(spinnerOra);

		spinnerPerc = new JSpinner();
		spinnerPerc.setToolTipText("Perc");
		spinnerPerc.setModel(new SpinnerNumberModel(30, 0, 59, 1));
		spinnerPerc.setBounds(548, 86, 48, 28);
		layeredPane.add(spinnerPerc);

		spinnerSec.setToolTipText("M\u00E1sodperc");
		spinnerSec.setModel(new SpinnerNumberModel(30, 0, 59, 1));
		spinnerSec.setBounds(624, 86, 48, 28);
		layeredPane.add(spinnerSec);

		checkBoxEv.setBounds(214, 91, 18, 18);
		layeredPane.add(checkBoxEv);

		checkBoxHonap.setBounds(290, 91, 18, 18);
		layeredPane.add(checkBoxHonap);

		checkBoxHonapNapja.setBounds(366, 91, 18, 18);
		layeredPane.add(checkBoxHonapNapja);

		checkBoxHetNapja.setBounds(442, 91, 18, 18);
		layeredPane.add(checkBoxHetNapja);

		checkBoxOra.setBounds(518, 91, 18, 18);
		layeredPane.add(checkBoxOra);

		checkBoxPerc.setBounds(594, 91, 18, 18);
		layeredPane.add(checkBoxPerc);

		checkBoxSec = new JCheckBox("");
		checkBoxSec.setSelected(true);
		checkBoxSec.setToolTipText("Ha nincs kiv\u00E1lasztva, a folyamat 10 m\u00E1sodpercenk\u00E9nt fut le");
		checkBoxSec.setBounds(670, 91, 18, 18);
		layeredPane.add(checkBoxSec);

		//Group the radio buttons.
		ButtonGroup group = new ButtonGroup();
		group.add(radioEsemeny);
		group.add(radioUtemez);

		comboBoxEsemTip.setToolTipText("Esem\u00E9ny t\u00EDpusa");
		comboBoxEsemTip.setModel(new DefaultComboBoxModel(new String[] { "Nyit", "Z\u00E1r", "V\u00E1lt" }));
		comboBoxEsemTip.setBounds(153, 52, 63, 26);
		layeredPane.add(comboBoxEsemTip);

		comboBoxEsemPortok.setToolTipText("Esem\u00E9nyt kiv\u00E1lt\u00F3 bemenet");
		comboBoxEsemPortok.setBounds(228, 52, 454, 26);
		layeredPane.add(comboBoxEsemPortok);

		radioEgyszeruMuv.setSelected(true);
		buttonGroup.add(radioEgyszeruMuv);
		radioEgyszeruMuv.setToolTipText(
				"K\u00F6nnyen l\u00E9trehozhat\u00F3 m\u0171velet, kev\u00E9s be\u00E1ll\u00EDt\u00E1si lehet\u0151s\u00E9ggel");
		radioEgyszeruMuv.setBounds(18, 130, 123, 18);
		layeredPane.add(radioEgyszeruMuv);

		radioEgyszeruMuv.addItemListener(new ItemListener()
		{

			@Override
			public void itemStateChanged(ItemEvent event)
			{
				int state = event.getStateChange();
				if(state == ItemEvent.SELECTED)
				{
					frame.setBounds((int) frame.getBounds().getX(), (int) frame.getBounds().getY(), 710, 233);
				}
				else if(state == ItemEvent.DESELECTED)
				{
					// do something else when the button is deselected

				}
			}
		});

		buttonGroup.add(radioProgramozas);
		radioProgramozas.setToolTipText(
				"Folyamat l\u00E9trehoz\u00E1sa a legr\u00E9szletesebb be\u00E1ll\u00EDt\u00E1si lehet\u0151ss\u00E9gekkel");
		radioProgramozas.setBounds(18, 168, 164, 18);
		layeredPane.add(radioProgramozas);

		radioProgramozas.addItemListener(new ItemListener()
		{

			@Override
			public void itemStateChanged(ItemEvent event)
			{
				int state = event.getStateChange();
				if(state == ItemEvent.SELECTED)
				{
					frame.setBounds((int) frame.getBounds().getX(), (int) frame.getBounds().getY(), 710, 698);
				}
				else if(state == ItemEvent.DESELECTED)
				{

					// do something else when the button is deselected

				}
			}
		});

		comboBoxEgyMuvTip.setModel(new DefaultComboBoxModel(
				new String[] { "Bekapcsol\u00E1s", "Kikapcsol\u00E1s", "E-mail k\u00FCld\u00E9s" }));
		comboBoxEgyMuvTip.setToolTipText("V\u00E9grehajtand\u00F3 m\u0171velet");
		comboBoxEgyMuvTip.setBounds(153, 126, 105, 26);
		layeredPane.add(comboBoxEgyMuvTip);

		comboBoxEgyMuvTip.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent event)
			{
				switch(EgyMuvTip.getEnum(((JComboBox) event.getSource()).getSelectedIndex()))
				{
					case Bekapcs://Bekapcs
					case Kikapcs://Kikapcs
					{
						textFieldEmailCim.setVisible(false);
						textFieldEmailUzenet.setVisible(false);
						comboBoxEgyMuvPortok.setVisible(true);
						break;
					}
					case Email://Email
					{
						textFieldEmailCim.setVisible(true);
						textFieldEmailUzenet.setVisible(true);
						comboBoxEgyMuvPortok.setVisible(false);
						break;
					}
				}

			}
		});

		comboBoxEgyMuvPortok.setToolTipText("Kimenet, amin a m\u0171velet v\u00E9grehajt\u00F3dik");
		comboBoxEgyMuvPortok.setBounds(270, 126, 402, 26);
		layeredPane.add(comboBoxEgyMuvPortok);

		textAreaKod = new SajatRSyntaxTextArea();

		FolySzerkeszto THIS = this;
		textAreaKod.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseReleased(MouseEvent arg0)
			{
				if(arg0.getButton() == MouseEvent.BUTTON3)
				{
					popupMenu.setVisible(true);
					popupMenu.show(arg0.getComponent(), arg0.getX() - 90, arg0.getY() - 20);
				}
				if(arg0.getButton() == MouseEvent.BUTTON2)
				{
					try
					{
						FolySzerkSnippetkiv w = new FolySzerkSnippetkiv();
						w.ShowDialog(THIS);
					}
					catch (Exception e)
					{
						e.printStackTrace();
					}
				}
			}
		});
		textAreaKod.setFont(new Font("Consolas", Font.PLAIN, 14));
		textAreaKod.setBackground(new java.awt.Color(142, 151, 168));

		ConfigurableCaret ccar = new ConfigurableCaret();
		ccar.setPasteOnMiddleMouseClick(false);
		textAreaKod.setCaret(ccar);
		textAreaKod.setCaretColor(java.awt.Color.WHITE);

		SyntaxScheme sxm = new SyntaxScheme(true);
		sxm.setStyles(Szinkodolo.SajatTokenTypes.GetStyleTomb());
		textAreaKod.setSyntaxScheme(sxm);

		AbstractTokenMakerFactory atmf = (AbstractTokenMakerFactory) TokenMakerFactory.getDefaultInstance();
		atmf.putMapping("SajatProgNyelv", "Szinkodolo.SajatNyelvTokenMaker");
		textAreaKod.setSyntaxEditingStyle("SajatProgNyelv");

		textAreaKod.setCodeFoldingEnabled(true);
		textAreaKod.setCurrentLineHighlightColor(new java.awt.Color(126, 166, 209, 180));
		textAreaKod.setFadeCurrentLineHighlight(true);

		textAreaKod.setBracketMatchingEnabled(true);
		textAreaKod.setMatchedBracketBGColor(java.awt.Color.YELLOW);
		textAreaKod.setMatchedBracketBorderColor(java.awt.Color.RED);
		textAreaKod.setAnimateBracketMatching(true);
		textAreaKod.setPaintMatchedBracketPair(true);
		textAreaKod.setShowMatchedBracketPopup(true);
		textAreaKod.setPopupMenu(null);

		scrollPaneKod = new RTextScrollPane(textAreaKod, true, new java.awt.Color(200, 200, 0));
		scrollPaneKod.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPaneKod.setBounds(6, 198, 682, 297);
		layeredPane.add(scrollPaneKod);
		btnMentes.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseClicked(MouseEvent arg0)
			{
				try
				{
					////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
					if(textFolyNev.getText().equals("") || textFolyNev.getText().equals(FolyNevAlapszoveg))
					{
						JOptionPane.showMessageDialog(frame, "A folyamat nem menthetõ név nélkül.",
								"Adjon nevet a folyamatnak!", JOptionPane.WARNING_MESSAGE);
					}
					else
					{
						////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
						if(textFolyNev.getText().contains(":") || textFolyNev.getText().contains("<")
								|| textFolyNev.getText().contains(">") || textFolyNev.getText().contains(";"))
						{
							JOptionPane.showMessageDialog(frame,
									"A folyamat neve nem tartalmazhat ':', '<', '>' és ';' karaktereket.", "Névhiba",
									JOptionPane.WARNING_MESSAGE);
						}
						else
						{
							////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
							if(textAreaKod.getText().contains("<>#&@[]~¡^¢°²`ÿ´//lezáró karakterlánc"))
							{
								JOptionPane.showMessageDialog(frame,
										"A kód nem tartalmazhatja a '<>#&@[]~¡^¢°²`ÿ´//lezáró karakterlánc' karakterláncot.",
										"", JOptionPane.WARNING_MESSAGE);
							}
							else
							{
								////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
								boolean vanmarilyen = false;

								if(!(Folyszerk && textFolyNev.getText().equals(SzerkFolyNev)))//Nincs egyezésvizsgálat, ha folyamatot szerkesztünk és az új név megegyezik az eredetivel
								{
									for(KliFolyamat item : FoClass.FolyamatokKliens)
									{
										if(textFolyNev.getText().equals(item.Nev))
										{
											vanmarilyen = true;
											break;
										}
									}
								}

								if(vanmarilyen)
								{
									JOptionPane.showMessageDialog(frame, "Ilyen nevû folyamat már létezik.",
											"Duplikált Név", JOptionPane.WARNING_MESSAGE);
								}
								else
								{
									////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
									if(Folyszerk)
									{
										for(int i = 0; i < FoClass.FolyamatokKliens.size(); ++i)
										{
											if(FoClass.FolyamatokKliens.get(i).Nev.equals(SzerkFolyNev))
											{
												FoClass.FolyamatokKliens.remove(i);
											}
										}
									}
									FoClass.FolyamatokKliens
											.add(new KliFolyamat(textFolyNev.getText(), GetEsemeny(), GetKod()));
									Szulo.KliensListaFrissit();
									FajlKezelo.FolyamatKiir(FoClass.FolyamatokKliens);

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
		btnMentes.setBounds(194, 163, 488, 28);
		layeredPane.add(btnMentes);

		textFolyNev = new JTextField();
		textFolyNev.addFocusListener(new FocusAdapter()
		{
			@Override
			public void focusGained(FocusEvent arg0)
			{
				if(textFolyNev.getText().equals(FolyNevAlapszoveg))
				{
					textFolyNev.setText("");
				}
			}

			@Override
			public void focusLost(FocusEvent e)
			{
				if(textFolyNev.getText().equals(""))
				{
					textFolyNev.setText(FolyNevAlapszoveg);
				}
			}
		});
		textFolyNev.setText("\u00CDrja ide a folyamat nev\u00E9t!");
		textFolyNev.setToolTipText(FolyNevAlapszoveg);
		textFolyNev.setHorizontalAlignment(SwingConstants.CENTER);
		textFolyNev.setBounds(18, 12, 664, 28);
		layeredPane.add(textFolyNev);
		textFolyNev.setColumns(10);

		textFieldFOCUSELVEVO = new JTextField();
		textFieldFOCUSELVEVO.setEditable(false);
		textFieldFOCUSELVEVO.setBounds(136, 0, 0, 0);
		layeredPane.add(textFieldFOCUSELVEVO);
		textFieldFOCUSELVEVO.setColumns(10);

		try
		{
			FoClass.FoAblakPeldany.PortokListaFrissit(true);

			for(Port item : FoClass.PortokSzerver)
			{
				if(item.GetSajatIrany() == PortIrany.Be)
				{
					comboBoxEsemPortok.addItem(item.Nev);
				}
				else if(item.GetSajatIrany() == PortIrany.Ki)
				{
					comboBoxEgyMuvPortok.addItem(item.Nev);
				}
			}

		}
		catch (Exception e)
		{
			System.out.println(e.getMessage());
			e.printStackTrace();
		}

		textAreaKod.addMouseMotionListener(new MouseMotionAdapter()
		{
			@Override
			public void mouseMoved(MouseEvent arg0)
			{
				popupMenu.setVisible(false);
			}

		});

		popupMenu = new JPopupMenu();

		mntmSnipBeszur = new JMenuItem("Kódrészlet Generálása");
		mntmAutoFormat = new JMenuItem("Kód Automatikus Formázása");

		mntmSnipBeszur.setHorizontalAlignment(SwingConstants.CENTER);
		mntmSnipBeszur.setHorizontalTextPosition(SwingConstants.CENTER);
		mntmAutoFormat.setHorizontalAlignment(SwingConstants.CENTER);
		mntmAutoFormat.setHorizontalTextPosition(SwingConstants.CENTER);

		popupMenu.add(mntmSnipBeszur);
		popupMenu.add(mntmAutoFormat);

		mntmSnipBeszur.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseReleased(MouseEvent arg0)
			{
				try
				{
					new FolySzerkSnippetkiv().ShowDialog(THIS);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
		mntmAutoFormat.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseReleased(MouseEvent arg0)
			{
				KodAutoFormat(textAreaKod.getCaretLineNumber());
			}
		});
	}

	int GetKarIndex(String szoveg, int sor, int oszlop)
	{
		String[] sorok = szoveg.split("\n");

		int index = 0;
		for(int i = 0; i < sor - 1; ++i)
		{
			index += sorok[i].length();
		}

		return index + oszlop;
	}

	String GetEsemeny()
	{
		String esemeny = "";
		if(radioEsemeny.isSelected())
		{
			switch(comboBoxEsemTip.getSelectedIndex())
			{
				case 0:
				{
					esemeny = "Nyitva(\"";
					break;
				}
				case 1:
				{
					esemeny = "Zárva(\"";
					break;
				}
				case 2:
				{
					esemeny = "Vált(\"";
					break;
				}
			}

			esemeny += (String) comboBoxEsemPortok.getSelectedItem();
			esemeny += "\")";
		}
		else if(radioUtemez.isSelected())
		{
			esemeny = "Ütemez(";

			if(checkBoxEv.isSelected())
			{
				esemeny += spinnerEv.getValue();
			}
			else
			{
				esemeny += "-1";
			}
			esemeny += ",";
			if(checkBoxHonap.isSelected())
			{
				esemeny += spinnerHonap.getValue();
			}
			else
			{
				esemeny += "-1";
			}
			esemeny += ",";
			if(checkBoxHonapNapja.isSelected())
			{
				esemeny += spinnerHonapNapja.getValue();
			}
			else
			{
				esemeny += "-1";
			}
			esemeny += ",";
			if(checkBoxHetNapja.isSelected())
			{
				esemeny += spinnerHetNapja.getValue();
			}
			else
			{
				esemeny += "-1";
			}
			esemeny += ",";
			if(checkBoxOra.isSelected())
			{
				esemeny += spinnerOra.getValue();
			}
			else
			{
				esemeny += "-1";
			}
			esemeny += ",";
			if(checkBoxPerc.isSelected())
			{
				esemeny += spinnerPerc.getValue();
			}
			else
			{
				esemeny += "-1";
			}
			esemeny += ",";
			if(checkBoxSec.isSelected())
			{
				esemeny += spinnerSec.getValue();
			}
			else
			{
				esemeny += "-1";
			}

			esemeny += ")";
		}

		return esemeny;
	}

	String GetKod()
	{
		String kod = "";
		if(radioEgyszeruMuv.isSelected())
		{
			kod = "//EGYSZERU_MUVELETTEL_LETREHOZVA\n";
			switch(EgyMuvTip.getEnum(comboBoxEgyMuvTip.getSelectedIndex()))
			{
				case Bekapcs:
				{
					kod += "Bekapcs(\"" + (String) comboBoxEgyMuvPortok.getSelectedItem() + "\");";
					break;
				}
				case Kikapcs:
				{

					kod += "Kikapcs(\"" + (String) comboBoxEgyMuvPortok.getSelectedItem() + "\");";
					break;
				}
				case Email:
				{
					kod += "EmailKüld(\"" + textFieldEmailCim.getText() + "\",\"" + "SmartParts Email" + "\",\""
							+ textFieldEmailUzenet.getText() + "\");";
					break;
				}
			}

		}
		else if(radioProgramozas.isSelected())
		{
			kod = textAreaKod.getText();
		}

		return kod;
	}

	void SetEsemeny(String esemeny)
	{
		try
		{
			boolean kellstringparam = false;

			if(esemeny.startsWith("Ütemez("))
			{
				checkBoxEv.setSelected(false);
				checkBoxHonap.setSelected(false);
				checkBoxHonapNapja.setSelected(false);
				checkBoxHetNapja.setSelected(false);
				checkBoxOra.setSelected(false);
				checkBoxPerc.setSelected(false);
				checkBoxSec.setSelected(false);

				radioUtemez.doClick();

				String[] parameterek = esemeny.split("\\(");
				parameterek = parameterek[1].split("\\)");
				parameterek = parameterek[0].split(",");

				if(Integer.parseInt(parameterek[0]) != -1)
				{
					try
					{
						checkBoxEv.setSelected(true);
						spinnerEv.setValue(Integer.parseInt(parameterek[0]));
					}
					catch (Exception e)
					{

					}
				}
				if(Integer.parseInt(parameterek[1]) != -1)
				{
					try
					{
						checkBoxHonap.setSelected(true);
						spinnerHonap.setValue(Integer.parseInt(parameterek[1]));
					}
					catch (Exception e)
					{

					}
				}
				if(Integer.parseInt(parameterek[2]) != -1)
				{
					try
					{
						checkBoxHonapNapja.setSelected(true);
						spinnerHonapNapja.setValue(Integer.parseInt(parameterek[2]));
					}
					catch (Exception e)
					{

					}
				}
				if(Integer.parseInt(parameterek[3]) != -1)
				{
					try
					{
						checkBoxHetNapja.setSelected(true);
						spinnerHetNapja.setValue(Integer.parseInt(parameterek[3]));
					}
					catch (Exception e)
					{

					}
				}
				if(Integer.parseInt(parameterek[4]) != -1)
				{
					try
					{
						checkBoxOra.setSelected(true);
						spinnerOra.setValue(Integer.parseInt(parameterek[4]));
					}
					catch (Exception e)
					{

					}
				}
				if(Integer.parseInt(parameterek[5]) != -1)
				{
					try
					{
						checkBoxPerc.setSelected(true);
						spinnerPerc.setValue(Integer.parseInt(parameterek[5]));
					}
					catch (Exception e)
					{

					}
				}
				if(Integer.parseInt(parameterek[6]) != -1)
				{
					try
					{
						checkBoxSec.setSelected(true);
						spinnerSec.setValue(Integer.parseInt(parameterek[6]));
					}
					catch (Exception e)
					{

					}
				}
			}
			else if(esemeny.startsWith("Nyitva("))
			{
				radioEsemeny.doClick();
				comboBoxEsemTip.setSelectedIndex(0);
				kellstringparam = true;
			}
			else if(esemeny.startsWith("Zárva("))
			{
				radioEsemeny.doClick();
				comboBoxEsemTip.setSelectedIndex(1);
				kellstringparam = true;
			}
			else if(esemeny.startsWith("Vált("))
			{
				radioEsemeny.doClick();
				comboBoxEsemTip.setSelectedIndex(2);
				kellstringparam = true;
			}

			if(kellstringparam)
			{
				String port = Eszkozok.FgvHivasbolParamKiszedo(esemeny);
				port = Eszkozok.ParameterSzelerolIdezojelLeszedo(port);
				for(int i = 0; i < comboBoxEsemPortok.getItemCount(); ++i)
				{
					if(comboBoxEsemPortok.getItemAt(i).toString().equals(port))
					{
						comboBoxEsemPortok.setSelectedIndex(i);
					}
				}
			}
		}
		catch (Exception e)
		{

		}
	}

	void SetKod(String kod)
	{
		textAreaKod.setText(kod);
		if(kod.contains("//EGYSZERU_MUVELETTEL_LETREHOZVA"))
		{//Egyszerû mûvelet beállítása
			radioEgyszeruMuv.doClick();
			try
			{
				boolean kellport = false;
				String[] sorok = kod.split("\n");

				if(sorok[1].startsWith("Bekapcs("))
				{
					comboBoxEgyMuvTip.setSelectedIndex(0);
					kellport = true;
				}
				else if(sorok[1].startsWith("Kikapcs("))
				{
					comboBoxEgyMuvTip.setSelectedIndex(1);
					kellport = true;
				}
				else if(sorok[1].startsWith("EmailKüld("))
				{
					comboBoxEgyMuvTip.setSelectedIndex(2);
					List<String> emailparameterek = Eszkozok
							.ParameterSzetvalaszto(Eszkozok.FgvHivasbolParamKiszedo(sorok[1]));
					textFieldEmailCim.setText(Eszkozok.ParameterSzelerolIdezojelLeszedo(emailparameterek.get(0)));
					textFieldEmailUzenet.setText(Eszkozok.ParameterSzelerolIdezojelLeszedo(emailparameterek.get(2)));
				}

				if(kellport)
				{
					String port = Eszkozok.FgvHivasbolParamKiszedo(sorok[1]);

					for(int i = 0; i < comboBoxEgyMuvPortok.getItemCount(); ++i)
					{
						if(comboBoxEgyMuvPortok.getItemAt(i).toString().equals(port))
						{
							comboBoxEgyMuvPortok.setSelectedIndex(i);
						}
					}
				}
				return;//nincs else, ezért visszatér, ha sikeres a beállítás
			}
			catch (Exception e)
			{

			}
		}

		//Nincs else, hogy ha fent a catch blokkba futunk, akkor is lefusson ez a rész
		{
			radioProgramozas.doClick();
		}
	}

	void KodAutoFormat(int kurzorsor)
	{
		try
		{

			String kod = textAreaKod.getText();

			List<Character> KodList = new ArrayList<Character>();

			for(int i = 0; i < kod.length(); ++i)
			{
				KodList.add(kod.charAt(i));
			}

			boolean StringbenVan = false;
			for(int i = 0; i < KodList.size(); ++i)
			{
				if(KodList.get(i) == '"')
				{
					if(i == 0)
					{
						StringbenVan = true;
					}
					else
					{
						if(KodList.get(i - 1) != '\\')
						{
							StringbenVan = !StringbenVan;
						}
					}
				}
				if(StringbenVan)
					continue;

				while(i != 0 && (KodList.get(i) == ',' || KodList.get(i) == ';' || KodList.get(i) == '(' || KodList.get(i) == ')') && IsWhitespace(KodList.get(i - 1)))
				{
					KodList.remove(i - 1);
					--i;
				}

				while(i + 1 < KodList.size() && (KodList.get(i) == ';' || KodList.get(i) == '(' || KodList.get(i) == ')') && IsWhitespace(KodList.get(i + 1)))
				{
					KodList.remove(i + 1);
				}

				if(i + 1 < KodList.size() && KodList.get(i) == ',' && !IsWhitespaceOrNewline(KodList.get(i + 1)))
				{
					KodList.add(i + 1, ' ');
				}

				if(IsOperator(KodList.get(i)))
				{
					if(i != 0)
					{
						if(IsOperator(KodList.get(i - 1)))
						{
							if(i >= 2 && !IsWhitespaceOrNewline(KodList.get(i - 2)))
							{
								KodList.add(i - 1, ' ');
							}
						}
						else if(!IsWhitespaceOrNewline(KodList.get(i - 1)))
						{
							KodList.add(i, ' ');
						}
					}
				}
				if(IsOperator(KodList.get(i)))
				{
					if(i + 1 < KodList.size())
					{
						if(IsOperator(KodList.get(i + 1)))
						{
							if(i + 2 < KodList.size()
									&& !((KodList.get(i) == '+' && KodList.get(i + 1) == '+')
											|| (KodList.get(i) == '-' && KodList.get(i + 1) == '-'))
									&& !IsWhitespaceOrNewline(KodList.get(i + 2)))
							{
								KodList.add(i + 2, ' ');
							}
						}
						else if(!IsWhitespaceOrNewline(KodList.get(i + 1)))
						{
							if(i == 0)
							{
								KodList.add(i + 1, ' ');
							}
							else if(!((KodList.get(i - 1) == '+' && KodList.get(i) == '+')
									|| (KodList.get(i - 1) == '-' && KodList.get(i) == '-')))
							{
								KodList.add(i + 1, ' ');
							}
						}
					}
				}
			}

			kod = "";
			for(int i = 0; i < KodList.size(); ++i)
			{
				kod += KodList.get(i);
			}
			textAreaKod.setText(kod);
		}
		catch (Exception e)
		{}

		try
		{

			String[] sorok = textAreaKod.getText().split("\n");

			for(int i = 0; i < sorok.length; ++i)
			{
				try
				{
					while(sorok[i].length() != 0 && (sorok[i].charAt(0) == ' ' || sorok[i].charAt(0) == '\t'))
					{
						sorok[i] = sorok[i].substring(1, sorok[i].length());
					}
				}
				catch (Exception e)
				{}
			}

			int zarojelszam = 0;
			for(int i = 0; i < sorok.length; ++i)
			{
				int zszamelotte = zarojelszam;
				for(int n = 0; n < zarojelszam; ++n)
				{
					sorok[i] = "\t" + sorok[i];
				}

				int x = 0;
				boolean Stringbenvan = false;
				for(; x < sorok[i].length(); ++x)
				{
					if(sorok[i].charAt(x) == '"')
					{
						try
						{
							if(!Stringbenvan)
							{
								Stringbenvan = true;
							}
							else if(Stringbenvan && sorok[i].charAt(x - 1) != '\\')
							{
								Stringbenvan = false;
							}
						}
						catch (IndexOutOfBoundsException e)
						{

						}
					}
					if(!Stringbenvan)
					{
						if(sorok[i].charAt(x) == '{')
							++zarojelszam;
						else if(sorok[i].charAt(x) == '}')
							--zarojelszam;

						if(zarojelszam == 0)
							break;
					}
				}

				if(zszamelotte > zarojelszam)
				{
					while(sorok[i].length() != 0 && (sorok[i].charAt(0) == ' ' || sorok[i].charAt(0) == '\t'))
					{
						sorok[i] = sorok[i].substring(1, sorok[i].length());
					}
					for(int n = 0; n < zarojelszam; ++n)
					{
						sorok[i] = "\t" + sorok[i];
					}
				}
			}

			String kesz = "";
			int pozicio = 0;
			for(int i = 0; i < sorok.length; ++i)
			{
				kesz += sorok[i] + "\n";
				if(i < kurzorsor)
				{
					pozicio += sorok[i].length() + 1;

				}
			}
			textAreaKod.setText(kesz);
			textAreaKod.setCaretPosition(pozicio);
		}
		catch (Exception e)
		{}
	}

	public boolean IsOperator(char c)
	{
		switch(c)
		{
			case '*':
			case '/':
			case '%':
			case '+':
			case '-':
			case '<':
			case '>':
			case '=':
			case '!':
			case '&':
			case '|':
				return true;

			default:
				return false;
		}
	}

	public boolean IsWhitespaceOrNewline(char c)
	{
		switch(c)
		{
			case ' ':
			case '\t':
			case '\n':
				return true;

			default:
				return false;
		}
	}

	public boolean IsWhitespace(char c)
	{
		switch(c)
		{
			case ' ':
			case '\t':
				return true;

			default:
				return false;
		}
	}

	public void KodBeillesztesVanKurzornalWhitespaceVizsgalo(String BeillesztendoWhitespace)
	{
		try
		{
			if(FoClass.FolySzerkPeldany.textAreaKod.getText()
					.charAt(FoClass.FolySzerkPeldany.textAreaKod.getCaretPosition() - 1) != ' '
					&& FoClass.FolySzerkPeldany.textAreaKod.getText()
							.charAt(FoClass.FolySzerkPeldany.textAreaKod.getCaretPosition() - 1) != '\n'
					&& FoClass.FolySzerkPeldany.textAreaKod.getText()
							.charAt(FoClass.FolySzerkPeldany.textAreaKod.getCaretPosition() - 1) != '\t'
					&& FoClass.FolySzerkPeldany.textAreaKod.getText()
							.charAt(FoClass.FolySzerkPeldany.textAreaKod.getCaretPosition() - 1) != '('
					&& FoClass.FolySzerkPeldany.textAreaKod.getText()
							.charAt(FoClass.FolySzerkPeldany.textAreaKod.getCaretPosition() - 1) != '{')
			{
				FoClass.FolySzerkPeldany.textAreaKod.insert(BeillesztendoWhitespace,
						FoClass.FolySzerkPeldany.textAreaKod.getCaretPosition());
			}
		}
		catch (IndexOutOfBoundsException e)
		{

		}
	}
}
