import java.io.*;
import java.net.*;
import java.util.*;

class TCPServer extends Thread
{
	public void run()
	{
		while(true)
		{
			ServerSocket srvr = null;
			try
			{
				srvr = new ServerSocket(FoClass.TCPPortSzam);
				srvr.setSoTimeout(30000);

				Socket skt;
				try
				{
					skt = srvr.accept();
				}
				catch (SocketTimeoutException e)
				{
					try
					{
						srvr.close();
					}
					catch (Exception ex)
					{}
					continue;
				}

				System.out.println("Szerver kapcsolatban");

				BufferedReader in = new BufferedReader(new InputStreamReader(skt.getInputStream(), "Cp1250"));
				PrintWriter out = new PrintWriter(new OutputStreamWriter(skt.getOutputStream(), "Cp1250"), true);

				long idokezdet = System.currentTimeMillis();
				boolean idotullepes = false;
				while(!in.ready())
				{
					if(System.currentTimeMillis() - idokezdet > 500)
					{
						idotullepes = true;
						break;
					}
				}
				if(idotullepes)
				{
					try
					{
						in.close();
					}
					catch (Exception e)
					{}
					try
					{
						out.close();
					}
					catch (Exception e)
					{}
					try
					{
						skt.close();
					}
					catch (Exception e)
					{}
					try
					{
						srvr.close();
					}
					catch (Exception e)
					{}

					continue;
				}

				String elsosor = in.readLine();
				System.out.println(elsosor);
				switch(elsosor)
				{
					case "Folyamatlekeres":
					{
						out.println(FoClass.FolyamatokMap.keySet().size() * 4 * 2 + 2);//4 sorra bontva egyet, 2-szer minden sort, +2 méret
						out.println(FoClass.FolyamatokMap.keySet().size() * 4 * 2 + 2);

						for(String item : FoClass.FolyamatokMap.keySet())
						{
							out.println(item);
							out.println(item);
							out.println(FoClass.FolyamatokMap.get(item).Esemeny);
							out.println(FoClass.FolyamatokMap.get(item).Esemeny);
							out.println(FoClass.FolyamatokMap.get(item).KodBe.replaceAll("\r", "").replace("\n",
									"²´¢/TCPUjs"));
							out.println(FoClass.FolyamatokMap.get(item).KodBe.replaceAll("\r", "").replace("\n",
									"²´¢/TCPUjs"));

							if(FoClass.FolyamatokMap.get(item).isAlive())
							{
								if(FoClass.FolyamatokMap.get(item).Felfuggesztve)
								{
									out.println("Szünetel");
									out.println("Szünetel");
								}
								else
								{
									out.println("Fut");
									out.println("Fut");
								}
							}
							else
							{
								out.println("Áll");
								out.println("Áll");
							}
						}
						break;
					}
					case "Portlekeres":
					{
						out.println(FoClass.PortokMap.keySet().size() * 4 * 2 + 2);//4 sorra bontva egyet, 2-szer minden sort, +2 méret
						out.println(FoClass.PortokMap.keySet().size() * 4 * 2 + 2);

						for(String item : FoClass.PortokMap.keySet())
						{
							out.println(FoClass.PortokMap.get(item).getPortTipusa().name());
							out.println(FoClass.PortokMap.get(item).getPortTipusa().name());
							out.println(FoClass.PortokMap.get(item).RFAzonosito);
							out.println(FoClass.PortokMap.get(item).RFAzonosito);
							out.println(item);
							out.println(item);
							out.println(FoClass.PortokMap.get(item).Ertek.toString());
							out.println(FoClass.PortokMap.get(item).Ertek.toString());
						}
						break;
					}
					case "FolyamatInditas":
					case "FolyamatLeallitas":
					{
						boolean UjraKezd = false;
						List<String> UzenetBe = new ArrayList<String>();

						int hossz = 9999999;
						int x = 0;
						while(x < hossz)
						{
							String buff = (String) UzenetKovsorVar(in, 500);
							if(buff == null && (UzenetBe.size() > 0 || x > 2))// Az elsõ 3 próbálkozásnál nem baj, ha null jön vissza, de utána hibának veszi
							{
								UjraKezd = true;
								break;
							}
							else if(buff != null)
							{
								UzenetBe.add(buff);
								System.out.println(buff);
							}

							if(UzenetBe.size() == 2)
							{
								if(UzenetBe.get(0).equals(UzenetBe.get(1)))
								{
									hossz = Integer.parseInt(UzenetBe.get(0));
									x = 1;
								}
								else
								{
									UjraKezd = true;
									break;
								}
							}

							++x;
						}

						if(UjraKezd)
						{
							try
							{
								in.close();
							}
							catch (Exception e)
							{}
							try
							{
								out.close();
							}
							catch (Exception e)
							{}
							try
							{
								skt.close();
							}
							catch (Exception e)
							{}
							break;
						}

						UzenetBe.remove(0);// Hossz Eltávolítása
						UzenetBe.remove(0);

						for(int a = 0; a + 1 < UzenetBe.size(); ++a)// Sorok ellenõrzése páronként (CRC helyett)
						{
							if(!UzenetBe.get(a).equals(UzenetBe.get(a + 1)))
							{
								UjraKezd = true;
								break;
							}
							UzenetBe.remove(a);
						}
						if(UjraKezd)
							break;

						out.println(UzenetBe.size() * 2 + 2);
						out.println(UzenetBe.size() * 2 + 2);

						for(String item : UzenetBe)
						{
							out.println(item);
							out.println(item);
						}

						for(String item : UzenetBe)
						{
							if(FoClass.FolyamatokMap.keySet().contains(item))
							{
								try
								{
									if(elsosor.equals("FolyamatInditas"))
									{
										if(!FoClass.FolyamatokMap.get(item).isAlive())
										{
											String esem = FoClass.FolyamatokMap.get(item).Esemeny;
											String kod = FoClass.FolyamatokMap.get(item).KodBe;
											FoClass.FolyamatokMap.remove(item);

											FoClass.FolyamatokMap.put(item, new KodFeldolgozo(esem, kod, item));
											FoClass.FolyamatokMap.get(item).start();
										}
									}
									else if(elsosor.equals("FolyamatLeallitas"))
									{
										if(FoClass.FolyamatokMap.get(item).isAlive())
										{
											FoClass.FolyamatokMap.get(item).Stop();
										}
									}
								}
								catch (Exception e)
								{}
							}
						}

						break;
					}

					case "FolyamatFelfuggesztes":
					case "FolyamatFolytatas":
					{
						boolean UjraKezd = false;
						List<String> UzenetBe = new ArrayList<String>();

						int hossz = 9999999;
						int x = 0;
						while(x < hossz)
						{
							String buff = (String) UzenetKovsorVar(in, 500);
							if(buff == null && (UzenetBe.size() > 0 || x > 2))// Az elsõ 3 próbálkozásnál nem baj, ha null jön vissza, de utána hibának veszi
							{
								UjraKezd = true;
								break;
							}
							else if(buff != null)
							{
								UzenetBe.add(buff);
								System.out.println(buff);
							}

							if(UzenetBe.size() == 2)
							{
								if(UzenetBe.get(0).equals(UzenetBe.get(1)))
								{
									hossz = Integer.parseInt(UzenetBe.get(0));
									x = 1;
								}
								else
								{
									UjraKezd = true;
									break;
								}
							}

							++x;
						}

						if(UjraKezd)
						{
							try
							{
								in.close();
							}
							catch (Exception e)
							{}
							try
							{
								out.close();
							}
							catch (Exception e)
							{}
							try
							{
								skt.close();
							}
							catch (Exception e)
							{}
							break;
						}

						UzenetBe.remove(0);// Hossz Eltávolítása
						UzenetBe.remove(0);

						for(int a = 0; a + 1 < UzenetBe.size(); ++a)// Sorok ellenõrzése páronként (CRC helyett)
						{
							if(!UzenetBe.get(a).equals(UzenetBe.get(a + 1)))
							{
								UjraKezd = true;
								break;
							}
							UzenetBe.remove(a);
						}
						if(UjraKezd)
							break;

						out.println(UzenetBe.size() * 2 + 2);
						out.println(UzenetBe.size() * 2 + 2);

						for(String item : UzenetBe)
						{
							out.println(item);
							out.println(item);
						}
						for(String item : UzenetBe)
						{
							if(FoClass.FolyamatokMap.keySet().contains(item))
							{
								try
								{
									if(FoClass.FolyamatokMap.get(item).isAlive())
									{
										if(elsosor.equals("FolyamatFelfuggesztes"))
										{
											FoClass.FolyamatokMap.get(item).Felfuggeszt();
										}
										else if(elsosor.equals("FolyamatFolytatas"))
										{
											FoClass.FolyamatokMap.get(item).Folytat();
										}
									}
								}
								catch (Exception e)
								{}
							}
						}

						break;
					}
					case "FolyamatTorles":
					{
						boolean UjraKezd = false;
						List<String> UzenetBe = new ArrayList<String>();

						int hossz = 9999999;
						int x = 0;
						while(x < hossz)
						{
							String buff = (String) UzenetKovsorVar(in, 500);
							if(buff == null && (UzenetBe.size() > 0 || x > 2))// Az elsõ 3 próbálkozásnál nem baj, ha null jön vissza, de utána hibának veszi
							{
								UjraKezd = true;
								break;
							}
							else if(buff != null)
							{
								UzenetBe.add(buff);
								System.out.println(buff);
							}

							if(UzenetBe.size() == 2)
							{
								if(UzenetBe.get(0).equals(UzenetBe.get(1)))
								{
									hossz = Integer.parseInt(UzenetBe.get(0));
									x = 1;
								}
								else
								{
									UjraKezd = true;
									break;
								}
							}

							++x;
						}

						if(UjraKezd)
						{
							try
							{
								in.close();
							}
							catch (Exception e)
							{}
							try
							{
								out.close();
							}
							catch (Exception e)
							{}
							try
							{
								skt.close();
							}
							catch (Exception e)
							{}
							break;
						}

						UzenetBe.remove(0);// Hossz Eltávolítása
						UzenetBe.remove(0);

						for(int a = 0; a + 1 < UzenetBe.size(); ++a)// Sorok ellenõrzése páronként (CRC helyett)
						{
							if(!UzenetBe.get(a).equals(UzenetBe.get(a + 1)))
							{
								UjraKezd = true;
								break;
							}
							UzenetBe.remove(a);
						}
						if(UjraKezd)
							break;

						out.println(UzenetBe.size() * 2 + 2);
						out.println(UzenetBe.size() * 2 + 2);

						for(String item : UzenetBe)
						{
							out.println(item);
							out.println(item);
						}

						for(String item : UzenetBe)
						{
							if(FoClass.FolyamatokMap.keySet().contains(item))
							{
								try
								{
									if(FoClass.FolyamatokMap.get(item).isAlive())
									{
										FoClass.FolyamatokMap.get(item).Stop();
									}
								}
								catch (Exception e)
								{}

								FoClass.FolyamatokMap.remove(item);
							}
						}
						synchronized(FoClass.SegedSzalTH)
						{
							FoClass.SegedSzalTH.FolyamatMentes = true;
							FoClass.SegedSzalTH.notify();
						}
						break;
					}
					case "FolyamatHozzaadas":
					{
						boolean UjraKezd = false;
						List<String> UzenetBe = new ArrayList<String>();

						int hossz = 9999999;
						int x = 0;
						while(x < hossz)
						{
							String buff = (String) UzenetKovsorVar(in, 500);
							if(buff == null && (UzenetBe.size() > 0 || x > 2))// Az elsõ 3 próbálkozásnál nem baj, ha null jön vissza, de utána hibának veszi
							{
								UjraKezd = true;
								break;
							}
							else if(buff != null)
							{
								UzenetBe.add(buff);
								System.out.println(buff);
							}

							if(UzenetBe.size() == 2)
							{
								if(UzenetBe.get(0).equals(UzenetBe.get(1)))
								{
									hossz = Integer.parseInt(UzenetBe.get(0));
									x = 1;
								}
								else
								{
									UjraKezd = true;
									break;
								}
							}

							++x;
						}

						if(UjraKezd)
						{
							try
							{
								in.close();
							}
							catch (Exception e)
							{}
							try
							{
								out.close();
							}
							catch (Exception e)
							{}
							try
							{
								skt.close();
							}
							catch (Exception e)
							{}
							break;
						}

						UzenetBe.remove(0);// Hossz Eltávolítása
						UzenetBe.remove(0);

						for(int a = 0; a + 1 < UzenetBe.size(); ++a)// Sorok ellenõrzése páronként (CRC helyett)
						{
							if(!UzenetBe.get(a).equals(UzenetBe.get(a + 1)))
							{
								UjraKezd = true;
								break;
							}
							UzenetBe.remove(a);
						}
						if(UjraKezd)
							break;

						out.println(UzenetBe.size() * 2 + 2);
						out.println(UzenetBe.size() * 2 + 2);

						for(String item : UzenetBe)
						{
							out.println(item);
							out.println(item);
						}

						for(int j = 0; j < UzenetBe.size(); j += 3)
						{
							try
							{
								try
								{
									if(FoClass.FolyamatokMap.containsKey(UzenetBe.get(j)))
									{
										FoClass.FolyamatokMap.get(UzenetBe.get(j)).Stop();
									}
								}
								catch (Exception e)
								{}
								FoClass.FolyamatokMap.put(UzenetBe.get(j), new KodFeldolgozo(UzenetBe.get(j + 1),
										UzenetBe.get(j + 2).replace("²´¢/TCPUjs", "\n"), UzenetBe.get(j)));

							}
							catch (Exception e)
							{}

						}
						synchronized(FoClass.SegedSzalTH)
						{
							FoClass.SegedSzalTH.FolyamatMentes = true;
							FoClass.SegedSzalTH.notify();
						}
						break;
					}
					case "PortHozzaadas":
					{
						boolean UjraKezd = false;
						List<String> UzenetBe = new ArrayList<String>();

						int hossz = 9999999;
						int x = 0;
						while(x < hossz)
						{
							String buff = (String) UzenetKovsorVar(in, 500);
							if(buff == null && (UzenetBe.size() > 0 || x > 2))// Az elsõ 3 próbálkozásnál nem baj, ha null jön vissza, de utána hibának veszi
							{
								UjraKezd = true;
								break;
							}
							else if(buff != null)
							{
								UzenetBe.add(buff);
								System.out.println(buff);
							}

							if(UzenetBe.size() == 2)
							{
								if(UzenetBe.get(0).equals(UzenetBe.get(1)))
								{
									hossz = Integer.parseInt(UzenetBe.get(0));
									x = 1;
								}
								else
								{
									UjraKezd = true;
									break;
								}
							}

							++x;
						}

						if(UjraKezd)
						{
							try
							{
								in.close();
							}
							catch (Exception e)
							{}
							try
							{
								out.close();
							}
							catch (Exception e)
							{}
							try
							{
								skt.close();
							}
							catch (Exception e)
							{}
							break;
						}

						UzenetBe.remove(0);// Hossz Eltávolítása
						UzenetBe.remove(0);

						for(int a = 0; a + 1 < UzenetBe.size(); ++a)// Sorok ellenõrzése páronként (CRC helyett)
						{
							if(!UzenetBe.get(a).equals(UzenetBe.get(a + 1)))
							{
								UjraKezd = true;
								break;
							}
							UzenetBe.remove(a);
						}
						if(UjraKezd)
							break;

						out.println(UzenetBe.size() * 2 + 2);
						out.println(UzenetBe.size() * 2 + 2);

						for(String item : UzenetBe)
						{
							out.println(item);
							out.println(item);
						}

						for(int j = 0; j < UzenetBe.size(); j += 3)
						{
							try
							{
								FoClass.PortokMap.put(UzenetBe.get(j), new Port(PortTipus.valueOf(UzenetBe.get(j + 1)),
										Integer.parseInt(UzenetBe.get(j + 2)), UzenetBe.get(j)));

							}
							catch (Exception e)
							{}

						}
						synchronized(FoClass.SegedSzalTH)
						{
							FoClass.SegedSzalTH.PortMentes = true;
							FoClass.SegedSzalTH.notify();
						}
						break;
					}
					case "PortTorles":
					{
						boolean UjraKezd = false;
						List<String> UzenetBe = new ArrayList<String>();

						int hossz = 9999999;
						int x = 0;
						while(x < hossz)
						{
							String buff = (String) UzenetKovsorVar(in, 500);
							if(buff == null && (UzenetBe.size() > 0 || x > 2))// Az elsõ 3 próbálkozásnál nem baj, ha null jön vissza, de utána hibának veszi
							{
								UjraKezd = true;
								break;
							}
							else if(buff != null)
							{
								UzenetBe.add(buff);
								System.out.println(buff);
							}

							if(UzenetBe.size() == 2)
							{
								if(UzenetBe.get(0).equals(UzenetBe.get(1)))
								{
									hossz = Integer.parseInt(UzenetBe.get(0));
									x = 1;
								}
								else
								{
									UjraKezd = true;
									break;
								}
							}

							++x;
						}

						if(UjraKezd)
						{
							try
							{
								in.close();
							}
							catch (Exception e)
							{}
							try
							{
								out.close();
							}
							catch (Exception e)
							{}
							try
							{
								skt.close();
							}
							catch (Exception e)
							{}
							break;
						}

						UzenetBe.remove(0);// Hossz Eltávolítása
						UzenetBe.remove(0);

						for(int a = 0; a + 1 < UzenetBe.size(); ++a)// Sorok ellenõrzése páronként (CRC helyett)
						{
							if(!UzenetBe.get(a).equals(UzenetBe.get(a + 1)))
							{
								UjraKezd = true;
								break;
							}
							UzenetBe.remove(a);
						}
						if(UjraKezd)
							break;

						out.println(UzenetBe.size() * 2 + 2);
						out.println(UzenetBe.size() * 2 + 2);

						for(String item : UzenetBe)
						{
							out.println(item);
							out.println(item);
						}

						for(String item : UzenetBe)
						{
							if(FoClass.PortokMap.keySet().contains(item))
							{
								FoClass.PortokMap.remove(item);
							}
						}
						synchronized(FoClass.SegedSzalTH)
						{
							FoClass.SegedSzalTH.PortMentes = true;
							FoClass.SegedSzalTH.notify();
						}
						break;
					}

					case "SzerverUjraindit":
					{
						out.println("SzerverUjraindit");
						Eszkozok.UjrainditApp();
						break;
					}
					case "KockaHozzaadasI2C":
					{
						boolean UjraKezd = false;
						List<String> UzenetBe = new ArrayList<String>();

						int hossz = 9999999;
						int x = 0;
						while(x < hossz)
						{
							String buff = (String) UzenetKovsorVar(in, 500);
							if(buff == null && (UzenetBe.size() > 0 || x > 2))// Az elsõ 3 próbálkozásnál nem baj, ha null jön vissza, de utána hibának veszi
							{
								UjraKezd = true;
								break;
							}
							else if(buff != null)
							{
								UzenetBe.add(buff);
								System.out.println(buff);
							}

							if(UzenetBe.size() == 2)
							{
								if(UzenetBe.get(0).equals(UzenetBe.get(1)))
								{
									hossz = Integer.parseInt(UzenetBe.get(0));
									x = 1;
								}
								else
								{
									UjraKezd = true;
									break;
								}
							}

							++x;
						}

						if(UjraKezd)
						{
							try
							{
								in.close();
							}
							catch (Exception e)
							{}
							try
							{
								out.close();
							}
							catch (Exception e)
							{}
							try
							{
								skt.close();
							}
							catch (Exception e)
							{}
							break;
						}

						UzenetBe.remove(0);// Hossz Eltávolítása
						UzenetBe.remove(0);

						for(int a = 0; a + 1 < UzenetBe.size(); ++a)// Sorok ellenõrzése páronként (CRC helyett)
						{
							if(!UzenetBe.get(a).equals(UzenetBe.get(a + 1)))
							{
								UjraKezd = true;
								break;
							}
							UzenetBe.remove(a);
						}
						if(UjraKezd)
							break;

						out.println(UzenetBe.size() * 2 + 2);
						out.println(UzenetBe.size() * 2 + 2);

						for(String item : UzenetBe)
						{
							out.println(item);
							out.println(item);
						}

						FoClass.SegedSzalTH.Kapcsolandok
								.add(new UARTPortMuvelet(new Port(UzenetBe.get(0)), UARTPortMuveletTipus.HozzaadI2C));
						synchronized(FoClass.SegedSzalTH)
						{
							FoClass.SegedSzalTH.notify();
						}
						break;
					}
					case "PortErtekadas":
					{
						boolean UjraKezd = false;
						List<String> UzenetBe = new ArrayList<String>();

						int hossz = 9999999;
						int x = 0;
						while(x < hossz)
						{
							String buff = (String) UzenetKovsorVar(in, 500);
							if(buff == null && (UzenetBe.size() > 0 || x > 2))// Az elsõ 3 próbálkozásnál nem baj, ha null jön vissza, de utána hibának veszi
							{
								UjraKezd = true;
								break;
							}
							else if(buff != null)
							{
								UzenetBe.add(buff);
								System.out.println(buff);
							}

							if(UzenetBe.size() == 2)
							{
								if(UzenetBe.get(0).equals(UzenetBe.get(1)))
								{
									hossz = Integer.parseInt(UzenetBe.get(0));
									x = 1;
								}
								else
								{
									UjraKezd = true;
									break;
								}
							}

							++x;
						}

						if(UjraKezd)
						{
							try
							{
								in.close();
							}
							catch (Exception e)
							{}
							try
							{
								out.close();
							}
							catch (Exception e)
							{}
							try
							{
								skt.close();
							}
							catch (Exception e)
							{}
							break;
						}

						UzenetBe.remove(0);// Hossz Eltávolítása
						UzenetBe.remove(0);

						for(int a = 0; a + 1 < UzenetBe.size(); ++a)// Sorok ellenõrzése páronként (CRC helyett)
						{
							if(!UzenetBe.get(a).equals(UzenetBe.get(a + 1)))
							{
								UjraKezd = true;
								break;
							}
							UzenetBe.remove(a);
						}
						if(UjraKezd)
							break;

						out.println(UzenetBe.size() * 2 + 2);
						out.println(UzenetBe.size() * 2 + 2);

						for(String item : UzenetBe)
						{
							out.println(item);
							out.println(item);
						}

						for(int j = 0; j < UzenetBe.size(); j += 2)
						{
							try
							{
								FoClass.SegedSzalTH.Kapcsolandok.add(new UARTPortMuvelet(
										new Port(FoClass.PortokMap.get(UzenetBe.get(j)).getPortTipusa(),
												FoClass.PortokMap.get(UzenetBe.get(j)).RFAzonosito,
												FoClass.PortokMap.get(UzenetBe.get(j)).Nev, UzenetBe.get(j + 1)),
										UARTPortMuveletTipus.Kapcsol));//Hozzáadás a kapcsolási várósorhoz
							}
							catch (Exception e)
							{
								e.printStackTrace();
							}

						}
						synchronized(FoClass.SegedSzalTH)
						{
							FoClass.SegedSzalTH.notify();
						}
						break;
					}

				}

				//				System.out.println("FOGADOTT: " + in.readLine());
				//				for(int i = 0; i < 40; ++i)
				//				{
				//					System.out.println("Sending: " + i);
				//					out.println(i);
				//					Thread.sleep(500);
				//				}

				try
				{
					in.close();
				}
				catch (Exception e)
				{}
				try
				{
					out.close();
				}
				catch (Exception e)
				{}
				try
				{
					skt.close();
				}
				catch (Exception e)
				{}
				try
				{
					srvr.close();
				}
				catch (Exception e)
				{}
			}
			catch (Exception e)
			{
				try
				{
					srvr.close();
				}
				catch (Exception ex)
				{}

				System.out.print("HIBA: " + e.getMessage());
				e.printStackTrace();
			}
		}
	}

	public static Object UzenetKovsorVar(BufferedReader in, long timeout)
	{

		try
		{
			long idokezdet = System.currentTimeMillis();
			boolean idotullepes = false;
			while(!in.ready())
			{
				if(System.currentTimeMillis() - idokezdet > timeout)
				{
					idotullepes = true;
					break;
				}

				try
				{
					Thread.sleep(1);
				}
				catch (InterruptedException e)
				{}
			}
			if(idotullepes)
			{
				return null;
			}

			return in.readLine();
		}
		catch (IOException e)
		{}
		return null;
	}
}