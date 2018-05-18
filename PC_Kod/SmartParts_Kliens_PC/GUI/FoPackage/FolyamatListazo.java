package FoPackage;

import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.table.DefaultTableModel;
import FoPackage.Eszkozok;
import FoPackage.TCPCom;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.*;
import javax.swing.table.*;
import java.awt.Font;
import java.awt.Image;
import java.awt.Point;
import java.awt.SystemColor;
import java.awt.event.MouseMotionAdapter;
import java.awt.Dimension;

public class FolyamatListazo
{
	public JFrame frame;
	private JTable tableSzer;
	private JMenuItem mntmIndtas;
	private JMenuItem mntmLeallitas;
	private JMenuItem mntmFelfuggesztes;
	private JMenuItem mntmFolytatas;
	private JMenuItem mntmSzerkesztes;
	private JMenuItem mntmTorles;
	private JMenuItem mntmKliensre;
	private JMenuItem mntmSzerverre;
	private JMenuItem mntmUjfolyamat;
	public JPopupMenu popupMenu;
	private JTable tableKli;

	/**
	 * Create the application.
	 */
	public FolyamatListazo()
	{
		initialize();
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

	int FokuszaltLista = 0;//1 = Szerver, 2 = Kliens

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize()
	{
		frame = new JFrame();
		frame.getContentPane().setBackground(Color.WHITE);
		frame.setTitle("Folyamatok Kezel\u00E9se");
		frame.setBounds(100, 100, 1012, 479);
		frame.addWindowListener(new java.awt.event.WindowAdapter()
		{
			@Override
			public void windowClosing(java.awt.event.WindowEvent windowEvent)
			{
				Szulo.frame.setEnabled(true);
				frame.dispose();
			}
		});
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setLocation(new Point(500, 50));

		JScrollPane scrollPaneKli = new JScrollPane();
		scrollPaneKli.setLocation(new Point(500, 50));

		JButton btnSzFriss = new JButton();
		btnSzFriss.setMinimumSize(new Dimension(28, 1));
		btnSzFriss.setLayout(new BorderLayout());
		JLabel label1 = new JLabel("Szerver");
		label1.setHorizontalTextPosition(SwingConstants.CENTER);
		label1.setHorizontalAlignment(SwingConstants.CENTER);
		JLabel label2 = new JLabel("Friss\u00EDt\u00E9s");
		label2.setHorizontalAlignment(SwingConstants.CENTER);
		label2.setHorizontalTextPosition(SwingConstants.CENTER);
		btnSzFriss.add(BorderLayout.NORTH, label1);
		btnSzFriss.add(BorderLayout.SOUTH, label2);

		JButton btnKliFriss = new JButton();
		btnKliFriss.setMinimumSize(new Dimension(28, 1));
		btnKliFriss.setLayout(new BorderLayout());
		JLabel label3 = new JLabel("Kliens");
		label3.setHorizontalTextPosition(SwingConstants.CENTER);
		label3.setHorizontalAlignment(SwingConstants.CENTER);
		JLabel label4 = new JLabel("Friss\u00EDt\u00E9s");
		label4.setHorizontalAlignment(SwingConstants.CENTER);
		label4.setHorizontalTextPosition(SwingConstants.CENTER);
		btnKliFriss.add(BorderLayout.NORTH, label3);
		btnKliFriss.add(BorderLayout.SOUTH, label4);

		GroupLayout groupLayout = new GroupLayout(frame.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(btnSzFriss, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnKliFriss, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(scrollPaneKli, GroupLayout.DEFAULT_SIZE, 440, Short.MAX_VALUE)
					.addContainerGap())
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(12, Short.MAX_VALUE))
				.addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(scrollPaneKli, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(btnSzFriss, GroupLayout.PREFERRED_SIZE, 42, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnKliFriss, GroupLayout.PREFERRED_SIZE, 42, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap())
		);

		tableKli = new JTable()
		{
			@Override
			public boolean isCellEditable(int row, int column)
			{
				return false;
			}
		};
		tableKli.addMouseMotionListener(new MouseMotionAdapter()
		{
			@Override
			public void mouseMoved(MouseEvent arg0)
			{
				popupMenu.setVisible(false);
			}
		});
		tableKli.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseReleased(MouseEvent e)
			{
				FokuszaltLista = 2;

				popupMenu = new JPopupMenu();
				if(tableKli.getSelectedRowCount() != 0)
				{
					popupMenu.add(mntmSzerverre);
					popupMenu.add(new JPopupMenu.Separator());

					if(tableKli.getSelectedRowCount() == 1)
						popupMenu.add(mntmSzerkesztes);

					popupMenu.add(mntmTorles);
					popupMenu.add(new JPopupMenu.Separator());
				}
				popupMenu.add(mntmUjfolyamat);

				popupMenu.setVisible(true);
				popupMenu.show(e.getComponent(), e.getX() - 20, e.getY() - 20);

			}
		});
		tableKli.setFillsViewportHeight(true);
		tableKli.setAutoCreateRowSorter(true);
		tableKli.setFont(new Font("Tahoma", Font.PLAIN, 11));
		tableKli.setGridColor(SystemColor.controlHighlight);
		tableKli.setSurrendersFocusOnKeystroke(true);
		tableKli.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		tableKli.setShowVerticalLines(false);
		tableKli.setBorder(null);
		tableKli.setModel(new DefaultTableModel(new Object[][] {}, new String[] { "Nev", "N\u00E9v", "Esem\u00E9ny" }));
		tableKli.getColumnModel().getColumn(0).setMinWidth(0);
		tableKli.getColumnModel().getColumn(0).setMaxWidth(0);
		tableKli.setRowHeight(22);
		tableKli.getTableHeader().setReorderingAllowed(false);
		tableKli.getRowSorter().toggleSortOrder(1);
		scrollPaneKli.setViewportView(tableKli);

		popupMenu = new JPopupMenu();
		tableSzer = new JTable()
		{
			@Override
			public boolean isCellEditable(int row, int column)
			{
				return false;
			}

			@Override
			public Class<?> getColumnClass(int column)
			{
				switch(column)
				{
					case 0:
					case 1:
					case 2:
					case 4:
						return String.class;
					case 3:
						return ImageIcon.class;
					default:
						return Object.class;
				}
			}
		};
		tableSzer.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
		tableSzer.addMouseMotionListener(new MouseMotionAdapter()
		{
			@Override
			public void mouseMoved(MouseEvent arg0)
			{
				popupMenu.setVisible(false);
			}

		});
		tableSzer.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseReleased(MouseEvent e)
			{
				FokuszaltLista = 1;
				popupMenu = new JPopupMenu();
				if(tableSzer.getSelectedRowCount() == 1)
				{
					if((String) tableSzer.getValueAt(tableSzer.getSelectedRows()[0], 1) == "Áll")
					{
						popupMenu.add(mntmIndtas);
					}
					else
					{
						popupMenu.add(mntmLeallitas);
						if((String) tableSzer.getValueAt(tableSzer.getSelectedRows()[0], 1) == "Szünetel")
						{
							popupMenu.add(mntmFolytatas);
						}
						else
						{
							popupMenu.add(mntmFelfuggesztes);
						}
					}
				}
				else if(tableSzer.getSelectedRowCount() != 0)
				{
					popupMenu.add(mntmIndtas);
					popupMenu.add(mntmLeallitas);
				}
				if(tableSzer.getSelectedRowCount() != 0)
				{
					popupMenu.add(new JPopupMenu.Separator());
					popupMenu.add(mntmKliensre);
					popupMenu.add(new JPopupMenu.Separator());
					popupMenu.add(mntmTorles);
				}
				popupMenu.setVisible(true);
				popupMenu.show(e.getComponent(), e.getX() - 20, e.getY() - 20);
			}
		});
		tableSzer.setFillsViewportHeight(true);
		tableSzer.setAutoCreateRowSorter(true);
		tableSzer.setFont(new Font("Tahoma", Font.PLAIN, 11));
		tableSzer.setGridColor(SystemColor.controlHighlight);
		tableSzer.setSurrendersFocusOnKeystroke(true);
		tableSzer.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		tableSzer.setShowVerticalLines(false);
		tableSzer.setBorder(null);
		tableSzer.setModel(new DefaultTableModel(new Object[][] {},
				new String[] { "Nev", "Allapot", "N\u00E9v", "\u00C1llapot", "Esem\u00E9ny" }));
		tableSzer.getColumnModel().getColumn(0).setMinWidth(0);
		tableSzer.getColumnModel().getColumn(0).setMaxWidth(0);
		tableSzer.getColumnModel().getColumn(1).setMinWidth(0);
		tableSzer.getColumnModel().getColumn(1).setMaxWidth(0);
		tableSzer.setRowHeight(22);
		tableSzer.getTableHeader().setReorderingAllowed(false);
		tableSzer.getRowSorter().toggleSortOrder(2);

		Comparator<ImageIcon> comparatorImageIcon = new Comparator<ImageIcon>()
		{

			@Override
			public int compare(ImageIcon a, ImageIcon b)
			{
				BufferedImage A = (BufferedImage) a.getImage();
				BufferedImage B = (BufferedImage) b.getImage();
				int atlagA = 0;
				for(int x = 0; x < A.getWidth(); x += 2)
				{
					for(int y = 0; y < A.getHeight(); y += 2)
					{
						atlagA += A.getRGB(x, y);
					}
				}

				atlagA /= A.getWidth() * A.getHeight();
				int atlagB = 0;
				for(int x = 0; x < B.getWidth(); x += 2)
				{
					for(int y = 0; y < B.getHeight(); y += 2)
					{
						atlagB += B.getRGB(x, y);
					}
				}

				atlagB /= B.getWidth() * B.getHeight();

				if(atlagA > atlagB)
					return 1;
				else if(atlagA < atlagB)
					return -1;
				else
					return 0;
			}
		};
		((TableRowSorter) tableSzer.getRowSorter()).setComparator(3, comparatorImageIcon);
		scrollPane.setViewportView(tableSzer);

		mntmIndtas = new JMenuItem("Ind\u00EDt\u00E1s");
		mntmIndtas.setHorizontalAlignment(SwingConstants.CENTER);
		mntmIndtas.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseReleased(MouseEvent e)
			{
				List<String> nevek = new LinkedList<String>();

				int[] indexek = tableSzer.getSelectedRows();

				for(int ind : indexek)
				{
					nevek.add((String) tableSzer.getValueAt(ind, 0));
				}

				TCPCom.FolyamatInditLeall(true, nevek);
			}
		});

		mntmLeallitas = new JMenuItem("Le\u00E1ll\u00EDt\u00E1s");
		mntmLeallitas.setHorizontalAlignment(SwingConstants.CENTER);
		mntmLeallitas.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseReleased(MouseEvent arg0)
			{

				List<String> nevek = new LinkedList<String>();

				int[] indexek = tableSzer.getSelectedRows();

				for(int ind : indexek)
				{
					nevek.add((String) tableSzer.getValueAt(ind, 0));
				}

				TCPCom.FolyamatInditLeall(false, nevek);
			}
		});

		mntmFelfuggesztes = new JMenuItem("Felfüggesztés");
		mntmFelfuggesztes.setHorizontalAlignment(SwingConstants.CENTER);
		mntmFelfuggesztes.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseReleased(MouseEvent e)
			{
				List<String> nevek = new LinkedList<String>();

				int[] indexek = tableSzer.getSelectedRows();

				for(int ind : indexek)
				{
					nevek.add((String) tableSzer.getValueAt(ind, 0));
				}

				TCPCom.FolyamatSzunetFolytat(false, nevek);
			}
		});
		
		mntmFolytatas = new JMenuItem("Folytatás");
		mntmFolytatas.setHorizontalAlignment(SwingConstants.CENTER);
		mntmFolytatas.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseReleased(MouseEvent e)
			{
				List<String> nevek = new LinkedList<String>();

				int[] indexek = tableSzer.getSelectedRows();

				for(int ind : indexek)
				{
					nevek.add((String) tableSzer.getValueAt(ind, 0));
				}

				TCPCom.FolyamatSzunetFolytat(true, nevek);
			}
		});
		
		mntmSzerkesztes = new JMenuItem("Szerkesztés");
		mntmTorles = new JMenuItem("Törlés");
		mntmKliensre = new JMenuItem(">>>>");
		mntmSzerverre = new JMenuItem("<<<<");
		mntmUjfolyamat = new JMenuItem("Új Folyamat");

		mntmSzerkesztes.setHorizontalAlignment(SwingConstants.CENTER);
		mntmSzerkesztes.setHorizontalTextPosition(SwingConstants.CENTER);
		mntmTorles.setHorizontalAlignment(SwingConstants.CENTER);
		mntmTorles.setHorizontalTextPosition(SwingConstants.CENTER);
		mntmKliensre.setHorizontalAlignment(SwingConstants.CENTER);
		mntmKliensre.setHorizontalTextPosition(SwingConstants.CENTER);
		mntmSzerverre.setHorizontalAlignment(SwingConstants.CENTER);
		mntmSzerverre.setHorizontalTextPosition(SwingConstants.CENTER);
		mntmUjfolyamat.setHorizontalAlignment(SwingConstants.CENTER);
		mntmUjfolyamat.setHorizontalTextPosition(SwingConstants.CENTER);

		mntmSzerverre.setToolTipText("Másolás a szerverre...");
		mntmKliensre.setToolTipText("Másolás a kliensre...");

		popupMenu.add(mntmIndtas);
		popupMenu.add(mntmLeallitas);
		popupMenu.add(mntmFelfuggesztes);
		popupMenu.add(mntmFolytatas);

		popupMenu.add(mntmSzerkesztes);
		popupMenu.add(mntmTorles);
		popupMenu.add(mntmKliensre);
		popupMenu.add(mntmSzerverre);
		popupMenu.add(mntmUjfolyamat);

		FolyamatListazo THIS = this;//Hogy a MouseListener-ben is el lehessen érni
		mntmUjfolyamat.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseReleased(MouseEvent arg0)
			{
				try
				{
					FoClass.FolySzerkPeldany = new FolySzerkeszto();
					FoClass.FolySzerkPeldany.ShowDialog(THIS);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
		mntmSzerkesztes.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseReleased(MouseEvent arg0)
			{
				try
				{
					FoClass.FolySzerkPeldany = new FolySzerkeszto((String) tableKli.getValueAt(tableKli.getSelectedRows()[0], 1));
					FoClass.FolySzerkPeldany.ShowDialog(THIS);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
		mntmTorles.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseReleased(MouseEvent arg0)
			{
				try
				{
					String[] ODoptions = new String[2];
					ODoptions[0] = new String("Végleges törlés");
					ODoptions[1] = new String("Mégsem");
					if(JOptionPane.showOptionDialog(frame, "Bizosan törli a kijelölt folyamato(ka)t?",
							"Folyamat(ok) törlése", 0, JOptionPane.WARNING_MESSAGE, null, ODoptions,
							ODoptions[1]) == JOptionPane.YES_OPTION)
					{
						if(FokuszaltLista == 2)//KLIENS TÖRLÉS
						{
							for(int item : tableKli.getSelectedRows())
							{
								for(int i = 0; i < FoClass.FolyamatokKliens.size(); ++i)
								{
									if(FoClass.FolyamatokKliens.get(i).Nev
											.equals((String) tableKli.getValueAt(item, 0)))
									{
										FoClass.FolyamatokKliens.remove(i);
										break;
									}
								}
							}
							KliensListaFrissit();
							FajlKezelo.FolyamatKiir(FoClass.FolyamatokKliens);
						}
						else if(FokuszaltLista == 1)//SZERVER TÖRLÉS
						{
							List<String> torlendok = new ArrayList<String>();

							for(int item : tableSzer.getSelectedRows())
							{
								for(int i = 0; i < FoClass.FolyamatokSzerver.size(); ++i)
								{
									if(FoClass.FolyamatokSzerver.get(i).Nev
											.equals((String) tableSzer.getValueAt(item, 0)))
									{
										torlendok.add(FoClass.FolyamatokSzerver.get(i).Nev);
										break;
									}
								}
							}

							if(!TCPCom.FolyamatTorlo(torlendok))
							{
								String[] ODoptionsX = new String[1];
								ODoptionsX[0] = new String("Tovább");
								JOptionPane.showOptionDialog(frame,
										"A folyamat(ok) törlése sikertelen.\nPróbálja újra!", "Hiba a kapcsolatban",
										JOptionPane.OK_OPTION, JOptionPane.ERROR_MESSAGE, null, ODoptionsX,
										ODoptionsX[0]);
							}
							else
							{
								SzerverListaFrissit(true);
							}
						}
					}
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
		mntmSzerverre.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseReleased(MouseEvent arg0)
			{
				if(FokuszaltLista == 2)//KLIENS LISTA
				{
					try
					{
						if(SzerverListaFrissit(true))
						{
							List<KliFolyamat> hozzaadandok = new ArrayList<KliFolyamat>();

							for(int item : tableKli.getSelectedRows())
							{
								for(int i = 0; i < FoClass.FolyamatokKliens.size(); ++i)
								{
									if(FoClass.FolyamatokKliens.get(i).Nev
											.equals((String) tableKli.getValueAt(item, 0)))
									{
										hozzaadandok.add(FoClass.FolyamatokKliens.get(i));
										break;
									}
								}
							}

							boolean mindetkihagy = false, mindetfelulir = false;
							for(KliFolyamat item : FoClass.FolyamatokSzerver)
							{
								for(int i = 0; i < hozzaadandok.size(); ++i)
								{
									if(hozzaadandok.get(i).Nev.equals(item.Nev))
									{
										if(mindetkihagy)
										{
											hozzaadandok.remove(i);
										}
										else if(mindetfelulir)
										{

										}
										else
										{
											String[] ODoptions = new String[4];
											ODoptions[0] = new String("Minden egyezõ folyamat felülírása");
											ODoptions[1] = new String("Ezen folyamat felülírása");
											ODoptions[2] = new String("Minden egyezõ folyamat kihagyása");
											ODoptions[3] = new String("Ezen folyamat kihagyása");
											switch(JOptionPane.showOptionDialog(frame,
													"Ilyen nevû folyamat már létezik a szerveren:\n"
															+ hozzaadandok.get(i).Nev + "\n\nMit kíván tenni?",
													"Névegyezés", 0, JOptionPane.QUESTION_MESSAGE, null, ODoptions,
													ODoptions[1]))
											{
												case 0:
												{
													mindetfelulir = true;
													break;
												}
												case 1:
												{
													break;
												}
												case 2:
												{
													mindetkihagy = true;
													hozzaadandok.remove(i);
													break;
												}
												case 3:
												{
													hozzaadandok.remove(i);
													break;
												}
												default://kérdés bezárásakor
												{
													hozzaadandok.remove(i);//Jelenlegi kihagyása
													break;
												}
											}
										}

										break;
									}
								}
							}
							if(!TCPCom.FolyamatHozzaad(hozzaadandok))
							{
								String[] ODoptionsX = new String[1];
								ODoptionsX[0] = new String("Tovább");
								JOptionPane.showOptionDialog(frame,
										"Folyama(ok) hozzáadása sikertelen.\nPróbálja újra!", "Hiba a kapcsolatban",
										JOptionPane.OK_OPTION, JOptionPane.ERROR_MESSAGE, null, ODoptionsX,
										ODoptionsX[0]);
							}

							SzerverListaFrissit(true);
						}
					}
					catch (Exception e)
					{
						String[] ODoptionsX = new String[1];
						ODoptionsX[0] = new String("Tovább");
						JOptionPane.showOptionDialog(frame, "A folyamat(ok) feltöltése sikertelen:\n" + e.getMessage(),
								"Hiba történt", JOptionPane.OK_OPTION, JOptionPane.ERROR_MESSAGE, null, ODoptionsX,
								ODoptionsX[0]);
					}
				}
			}
		});
		mntmKliensre.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseReleased(MouseEvent arg0)
			{
				if(FokuszaltLista == 1)//SZERVER LISTA
				{
					try
					{
						if(KliensListaFrissit())
						{
							List<KliFolyamat> hozzaadandok = new ArrayList<KliFolyamat>();

							for(int item : tableSzer.getSelectedRows())
							{
								for(int i = 0; i < FoClass.FolyamatokSzerver.size(); ++i)
								{
									if(FoClass.FolyamatokSzerver.get(i).Nev
											.equals((String) tableSzer.getValueAt(item, 0)))
									{
										hozzaadandok.add(FoClass.FolyamatokSzerver.get(i));
										break;
									}
								}
							}

							boolean mindetkihagy = false, mindetfelulir = false;
							for(KliFolyamat item : FoClass.FolyamatokKliens)
							{
								for(int i = 0; i < hozzaadandok.size(); ++i)
								{
									if(hozzaadandok.get(i).Nev.equals(item.Nev))
									{
										if(mindetkihagy)
										{
											hozzaadandok.remove(i);
										}
										else if(mindetfelulir)
										{

										}
										else
										{
											String[] ODoptions = new String[4];
											ODoptions[0] = new String("Minden egyezõ folyamat felülírása");
											ODoptions[1] = new String("Ezen folyamat felülírása");
											ODoptions[2] = new String("Minden egyezõ folyamat kihagyása");
											ODoptions[3] = new String("Ezen folyamat kihagyása");
											switch(JOptionPane.showOptionDialog(frame,
													"Ilyen nevû folyamat már létezik a szerveren:\n"
															+ hozzaadandok.get(i).Nev + "\n\nMit kíván tenni?",
													"Névegyezés", 0, JOptionPane.QUESTION_MESSAGE, null, ODoptions,
													ODoptions[1]))
											{
												case 0:
												{
													mindetfelulir = true;
													break;
												}
												case 1:
												{
													break;
												}
												case 2:
												{
													mindetkihagy = true;
													hozzaadandok.remove(i);
													break;
												}
												case 3:
												{
													hozzaadandok.remove(i);
													break;
												}
												default://kérdés bezárásakor
												{
													hozzaadandok.remove(i);//Jelenlegi kihagyása
													break;
												}
											}
										}

										break;
									}
								}
							}

							for(KliFolyamat item : hozzaadandok)//Bennelévõ azonos nevûek eltávolítása (felülírás miatt)
							{
								for(int i = 0; i < FoClass.FolyamatokKliens.size(); ++i)
								{
									if(FoClass.FolyamatokKliens.get(i).Nev.equals(item.Nev))
									{
										FoClass.FolyamatokKliens.remove(i);
									}
								}
							}

							FoClass.FolyamatokKliens.addAll(hozzaadandok);

							KliensListaFrissit();
							FajlKezelo.FolyamatKiir(FoClass.FolyamatokKliens);
						}
					}
					catch (Exception e)
					{
						String[] ODoptionsX = new String[1];
						ODoptionsX[0] = new String("Tovább");
						JOptionPane.showOptionDialog(frame, "A folyamat(ok) másolása sikertelen:\n" + e.getMessage(),
								"Hiba történt", JOptionPane.OK_OPTION, JOptionPane.ERROR_MESSAGE, null, ODoptionsX,
								ODoptionsX[0]);
					}
				}
			}
		});
		frame.getContentPane().setLayout(groupLayout);

		btnSzFriss.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseClicked(MouseEvent arg0)
			{
				SzerverListaFrissit(true);

			}
		});
		btnKliFriss.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseClicked(MouseEvent e)
			{
				KliensListaFrissit();
			}
		});

		synchronized(FoClass.TCPFrissitoSzalTH)//3x kell, mert van benne 2db 300-as várakozás a frissítések után is és lehet, hogy épp abban van
		{
			FoClass.TCPFrissitoSzalTH.notify();
		}
		synchronized(FoClass.TCPFrissitoSzalTH)
		{
			FoClass.TCPFrissitoSzalTH.notify();
		}
		synchronized(FoClass.TCPFrissitoSzalTH)
		{
			FoClass.TCPFrissitoSzalTH.notify();
		}
		
		KliensListaFrissit();
	}

	protected boolean KliensListaFrissit()
	{
		try
		{

			while(tableKli.getRowCount() > 0)
			{
				((DefaultTableModel) tableKli.getModel()).removeRow(0);
			}
			for(KliFolyamat folyamat : FoClass.FolyamatokKliens)
			{
				((DefaultTableModel) tableKli.getModel())
						.addRow(new Object[] { folyamat.Nev, folyamat.Nev, folyamat.Esemeny });
			}
			return true;
		}
		catch (Exception e)
		{
			String[] ODoptionsX = new String[1];
			ODoptionsX[0] = new String("Tovább");
			JOptionPane.showOptionDialog(frame, "A helyi listák nem frissíthetõk.", "Hiba történt",
					JOptionPane.OK_OPTION, JOptionPane.ERROR_MESSAGE, null, ODoptionsX, ODoptionsX[0]);
			return false;
		}
	}

	protected boolean SzerverListaFrissit(boolean KellHibauzenet)
	{
		try
		{
			List<KliFolyamat> bufffoly = TCPCom.FolyamatLekero();
			if(bufffoly == null)
			{
				throw new Exception();
			}
			FoClass.FolyamatokSzerver = bufffoly;

			int[] kivalasztottsorok = tableSzer.getSelectedRows();
			while(tableSzer.getRowCount() > 0)
			{
				((DefaultTableModel) tableSzer.getModel()).removeRow(0);
			}
			for(KliFolyamat folyamat : FoClass.FolyamatokSzerver)
			{
				Image buff = null;
				switch(folyamat.Statusz)//TODO Normális ikonok, és a beolvasás nem itt, hanem a program indulásakor
				{
					case Fut:
						buff = Eszkozok.getScaledImage(
								ImageIO.read(new File("res" + System.getProperty("file.separator") + "folyFut.jpg")),
								18, 20);
						break;
					case Szünetel:
						buff = Eszkozok.getScaledImage(
								ImageIO.read(new File("res" + System.getProperty("file.separator") + "folySzun.jpg")),
								12, 20);
						break;
					case Áll:
						buff = Eszkozok.getScaledImage(
								ImageIO.read(new File("res" + System.getProperty("file.separator") + "folyAll.jpg")),
								12, 20);
						break;
					default:
						buff = Eszkozok.getScaledImage(
								ImageIO.read(new File("res" + System.getProperty("file.separator") + "folyNA.jpg")), 20,
								20);
						break;
				}
				((DefaultTableModel) tableSzer.getModel()).addRow(new Object[] {
						folyamat.Nev,
						folyamat.Statusz.toString(),
						folyamat.Nev,
						new ImageIcon(buff),
						folyamat.Esemeny });
			}
			for(int item : kivalasztottsorok)
			{
				tableSzer.getSelectionModel().addSelectionInterval(item, item);
			}
			return true;
		}

		catch (Exception e)
		{
			System.out.println(e.getMessage());
			e.printStackTrace();

			if(KellHibauzenet)
			{
				String[] ODoptionsX = new String[1];
				ODoptionsX[0] = new String("Tovább");
				JOptionPane.showOptionDialog(frame, "A szerver nem érhetõ el.", "Hiba a kapcsolatban",
						JOptionPane.OK_OPTION, JOptionPane.ERROR_MESSAGE, null, ODoptionsX, ODoptionsX[0]);
			}
			return false;
		}
	}
}
