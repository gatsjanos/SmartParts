package FoPackage;
import java.awt.Font;
import java.awt.MouseInfo;
import java.awt.SystemColor;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import java.awt.BorderLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import FoPackage.Port;
import FoPackage.TCPCom;

public class FoAblak
{

	JFrame frame;
	private JPopupMenu popupMenu;
	private JMenuItem mntmSzerkesztes;
	private JMenuItem mntmTorles;
	private JMenuItem mntmManualkapcs;
	private JMenuItem mntmUjport;
	JTable tablePort;

	private JPopupMenu popupManualMenu;
	private JMenuItem manmntBekapcs;
	private JMenuItem manmntKikapcs;
	private JMenuItem manmntPeriodizal;

	private PortTipus KivModositandoTip = PortTipus.KIRele;

	/**
	 * Create the application.
	 */
	public FoAblak()
	{
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize()
	{
		frame = new JFrame();
		frame.setTitle("\u00C1ttekint\u0151");
		frame.setBounds(100, 100, 450, 546);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JLayeredPane layeredPane = new JLayeredPane();
		frame.getContentPane().add(layeredPane, BorderLayout.CENTER);

		JButton btnFolyKez = new JButton("Folyamatok Kezel\u00E9se");
		FoAblak THIS = this;//Hogy a MouseListener-ben is el lehessen érni
		btnFolyKez.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseClicked(MouseEvent arg0)
			{
				try
				{
					FoClass.FolyListazoPeldany = new FolyamatListazo();
					FoClass.FolyListazoPeldany.ShowDialog(THIS);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
		btnFolyKez.setBounds(178, 6, 160, 28);
		layeredPane.add(btnFolyKez);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(6, 73, 422, 428);
		layeredPane.add(scrollPane);

		tablePort = new JTable()
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
						return String.class;
					default:
						return Object.class;
				}
			}
		};
		tablePort.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
		tablePort.setFillsViewportHeight(true);
		tablePort.setAutoCreateRowSorter(true);
		tablePort.setFont(new Font("Tahoma", Font.PLAIN, 11));
		tablePort.setGridColor(SystemColor.controlHighlight);
		tablePort.setSurrendersFocusOnKeystroke(true);
		tablePort.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		tablePort.setShowVerticalLines(false);
		tablePort.setBorder(null);
		tablePort.setModel(
				new DefaultTableModel(new Object[][] {}, new String[] { "Nev", "N\u00E9v", "Típus", "Érték" }));
		tablePort.getColumnModel().getColumn(0).setMinWidth(0);
		tablePort.getColumnModel().getColumn(0).setMaxWidth(0);
		tablePort.setRowHeight(22);
		tablePort.getTableHeader().setReorderingAllowed(false);
		scrollPane.setViewportView(tablePort);

		JButton btnPortFriss = new JButton("Okoskock\u00E1k Firss\u00EDt\u00E9se");
		btnPortFriss.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseClicked(MouseEvent arg0)
			{
				PortokListaFrissit(true);
			}
		});
		btnPortFriss.setBounds(6, 6, 160, 28);
		layeredPane.add(btnPortFriss);

		JButton btnNewButton = new JButton("Szerver \u00DAjraind\u00EDt\u00E1sa");
		btnNewButton.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseClicked(MouseEvent arg0)
			{
				if(!TCPCom.SzerverUjraindit())
				{
					String[] ODoptionsX = new String[1];
					ODoptionsX[0] = new String("Tovább");
					JOptionPane.showOptionDialog(frame, "A szerver nem érhetõ el.", "Hiba a kapcsolatban",
							JOptionPane.OK_OPTION, JOptionPane.ERROR_MESSAGE, null, ODoptionsX, ODoptionsX[0]);
				}
			}
		});
		btnNewButton.setBounds(6, 35, 160, 28);
		layeredPane.add(btnNewButton);

		JButton btnBeallitasok = new JButton("Be\u00E1ll\u00EDt\u00E1sok");
		btnBeallitasok.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseReleased(MouseEvent arg0)
			{
				try
				{
					try
					{
						if(FoClass.BeallitasokPeldany != null)
						{
							FoClass.BeallitasokPeldany.frame.dispose();
						}
					}
					catch (Exception e)
					{

					}
					FoClass.BeallitasokPeldany = new Beallitasok();
					FoClass.BeallitasokPeldany.frame.setVisible(true);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
		btnBeallitasok.setBounds(249, 35, 89, 28);
		layeredPane.add(btnBeallitasok);
		tablePort.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseReleased(MouseEvent e)
			{
				popupMenu = new JPopupMenu();
				if(tablePort.getSelectedRowCount() != 0)
				{
					PortTipus egybuff = null;
					boolean ugyanolyanok = true;
					for(int item : tablePort.getSelectedRows())
					{
						for(int i = 0; i < FoClass.PortokSzerver.size(); ++i)
						{
							if(tablePort.getValueAt(item, 0).equals(FoClass.PortokSzerver.get(i).Nev))
							{
								if(egybuff != null)
								{
									if(egybuff != FoClass.PortokSzerver.get(i).getPortTipusa())
									{
										ugyanolyanok = false;
										break;
									}
								}
								else
								{
									egybuff = FoClass.PortokSzerver.get(i).getPortTipusa();
									break;
								}
							}
						}
						if(!ugyanolyanok)
							break;
					}

					if(ugyanolyanok && Port.GetIrany(egybuff) == PortIrany.Ki)
					{
						KivModositandoTip = egybuff;
						popupMenu.add(mntmManualkapcs);
						popupMenu.add(new JPopupMenu.Separator());
					}
				}
				if(tablePort.getSelectedRowCount() == 1)
				{
					popupMenu.add(mntmSzerkesztes);
				}
				if(tablePort.getSelectedRowCount() != 0)
				{
					popupMenu.add(mntmTorles);
					popupMenu.add(new JPopupMenu.Separator());
				}

				popupMenu.add(mntmUjport);
				popupMenu.setVisible(true);
				popupMenu.show(e.getComponent(), e.getX() - 20, e.getY() - 20);
			}
		});
		tablePort.addMouseMotionListener(new MouseMotionAdapter()
		{
			@Override
			public void mouseMoved(MouseEvent arg0)
			{
				popupMenu.setVisible(false);
				popupManualMenu.setVisible(false);
			}

		});
		popupMenu = new JPopupMenu();
		mntmSzerkesztes = new JMenuItem("Szerkesztés");
		mntmTorles = new JMenuItem("Törlés");
		mntmManualkapcs = new JMenuItem("Manuális Értékadás");
		mntmUjport = new JMenuItem("Új Okoskocka Hozzáadása");

		mntmSzerkesztes.setHorizontalAlignment(SwingConstants.CENTER);
		mntmSzerkesztes.setHorizontalTextPosition(SwingConstants.CENTER);
		mntmTorles.setHorizontalAlignment(SwingConstants.CENTER);
		mntmTorles.setHorizontalTextPosition(SwingConstants.CENTER);
		mntmManualkapcs.setHorizontalAlignment(SwingConstants.CENTER);
		mntmManualkapcs.setHorizontalTextPosition(SwingConstants.CENTER);
		mntmUjport.setHorizontalAlignment(SwingConstants.CENTER);
		mntmUjport.setHorizontalTextPosition(SwingConstants.CENTER);

		popupMenu.add(mntmSzerkesztes);
		popupMenu.add(mntmTorles);
		popupMenu.add(mntmManualkapcs);
		popupMenu.add(mntmUjport);

		mntmSzerkesztes.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseReleased(MouseEvent arg0)
			{
				try
				{
					PortSzerkeszto window = new PortSzerkeszto(
							(String) tablePort.getValueAt(tablePort.getSelectedRows()[0], 1));
					window.ShowDialog(THIS);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
		mntmUjport.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseReleased(MouseEvent arg0)
			{
				if(PortokListaFrissit(true))
				{
					PortSzerkeszto window = new PortSzerkeszto();
					window.ShowDialog(THIS);
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
					if(JOptionPane.showOptionDialog(frame, "Bizosan törli a kijelölt porto(ka)t?", "Port(ok) törlése",
							0, JOptionPane.WARNING_MESSAGE, null, ODoptions, ODoptions[1]) == JOptionPane.YES_OPTION)
					{
						List<String> torlendok = new ArrayList<String>();

						for(int item : tablePort.getSelectedRows())
						{
							for(int i = 0; i < FoClass.PortokSzerver.size(); ++i)
							{
								if(FoClass.PortokSzerver.get(i).Nev.equals((String) tablePort.getValueAt(item, 0)))
								{
									torlendok.add(FoClass.PortokSzerver.get(i).Nev);
									break;
								}
							}
						}

						if(!TCPCom.PortTorlo(torlendok))
						{
							String[] ODoptionsX = new String[1];
							ODoptionsX[0] = new String("Tovább");
							JOptionPane.showOptionDialog(frame, "A port(ok) törlése sikertelen.\nPróbálja újra!",
									"Hiba a kapcsolatban", JOptionPane.OK_OPTION, JOptionPane.ERROR_MESSAGE, null,
									ODoptionsX, ODoptionsX[0]);
						}
						else
						{
							PortokListaFrissit(true);
						}
					}
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
		mntmManualkapcs.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseReleased(MouseEvent e)
			{
				try
				{
					switch(KivModositandoTip)
					{
						case KIRele:
						{
							popupManualMenu.setVisible(true);
							popupManualMenu.show(frame,
									(int) (MouseInfo.getPointerInfo().getLocation().getX()
											- frame.getLocationOnScreen().getX()) - 50,
									(int) (MouseInfo.getPointerInfo().getLocation().getY()
											- frame.getLocationOnScreen().getY()) - 20);
							break;
						}
					}
				}

				catch (Exception ex)
				{
					ex.printStackTrace();
					System.out.println(ex.getMessage());
				}
			}
		});

		popupManualMenu = new JPopupMenu();

		manmntBekapcs = new JMenuItem("Bekapcsolás");
		manmntKikapcs = new JMenuItem("Kikapcsolás");
		manmntPeriodizal = new JMenuItem("Periodizálás");

		manmntBekapcs.setHorizontalAlignment(SwingConstants.CENTER);
		manmntBekapcs.setHorizontalTextPosition(SwingConstants.CENTER);
		manmntKikapcs.setHorizontalAlignment(SwingConstants.CENTER);
		manmntKikapcs.setHorizontalTextPosition(SwingConstants.CENTER);
		manmntPeriodizal.setHorizontalAlignment(SwingConstants.CENTER);
		manmntPeriodizal.setHorizontalTextPosition(SwingConstants.CENTER);

		popupManualMenu.add(manmntBekapcs);
		popupManualMenu.add(manmntKikapcs);
		popupManualMenu.add(manmntPeriodizal);

		manmntPeriodizal.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseReleased(MouseEvent e)
			{
				new RelePeriodizalo().ShowDialog(THIS);
			}
		});
		manmntBekapcs.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseReleased(MouseEvent e)
			{
				ObjTipus buffCelTip = ObjTipus.Int;//Érték típusa az értékadáshoz 
				int bufffertek = 1;
				try
				{
					HashMap<String, Object> modositandok = new LinkedHashMap<String, Object>();

					for(int item : tablePort.getSelectedRows())
					{
						for(int i = 0; i < FoClass.PortokSzerver.size(); ++i)
						{
							if(FoClass.PortokSzerver.get(i).Nev.equals((String) tablePort.getValueAt(item, 0)))
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
								"Hiba a kapcsolatban", JOptionPane.OK_OPTION, JOptionPane.ERROR_MESSAGE, null,
								ODoptionsX, ODoptionsX[0]);
					}
				}

				catch (Exception ex)
				{
					ex.printStackTrace();
					System.out.println(ex.getMessage());
				}
			}
		});
		manmntKikapcs.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseReleased(MouseEvent e)
			{
				ObjTipus buffCelTip = ObjTipus.Int;//Érték típusa az értékadáshoz 
				int bufffertek = 0;
				try
				{
					HashMap<String, Object> modositandok = new LinkedHashMap<String, Object>();

					for(int item : tablePort.getSelectedRows())
					{
						for(int i = 0; i < FoClass.PortokSzerver.size(); ++i)
						{
							if(FoClass.PortokSzerver.get(i).Nev.equals((String) tablePort.getValueAt(item, 0)))
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
								"Hiba a kapcsolatban", JOptionPane.OK_OPTION, JOptionPane.ERROR_MESSAGE, null,
								ODoptionsX, ODoptionsX[0]);
					}
				}

				catch (Exception ex)
				{
					ex.printStackTrace();
					System.out.println(ex.getMessage());
				}
			}
		});
	}

	protected boolean PortokListaFrissit(boolean KellHibauzenet)
	{
		try
		{
			List<Port> buffport = TCPCom.PortLekero();
			if(buffport == null)
			{
				throw new Exception();
			}
			FoClass.PortokSzerver = buffport;

			int[] kivalasztottsorok = tablePort.getSelectedRows();
			while(tablePort.getRowCount() > 0)
			{
				((DefaultTableModel) tablePort.getModel()).removeRow(0);
			}
			for(Port port : FoClass.PortokSzerver)
			{
				((DefaultTableModel) tablePort.getModel())
						.addRow(new Object[] { port.Nev, port.Nev, port.getPortTipusa().toString(), port.Ertek });
			}

			for(int item : kivalasztottsorok)
			{
				tablePort.getSelectionModel().addSelectionInterval(item, item);
			}
			return true;
		}

		catch (Exception e)
		{
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
