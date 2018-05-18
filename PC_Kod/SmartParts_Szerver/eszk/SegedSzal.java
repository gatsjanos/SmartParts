import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import javax.print.attribute.standard.MediaSize.Other;
import javax.xml.crypto.dsig.keyinfo.KeyValue;

public class SegedSzal extends Thread
{
	boolean FolyamatMentes = false, PortMentes = false;
	List<UARTPortMuvelet> Kapcsolandok = new LinkedList<UARTPortMuvelet>();
	List<String> Sikertelenek = new LinkedList<String>();

	public void run()
	{
		while(true)
		{
			try
			{
				if(Kapcsolandok.size() != 0)
				{
					try
					{
						try
						{
							List<Integer> torlendok = new ArrayList<Integer>();
							for(int i = Kapcsolandok.size() - 1; i >= 0; --i)
							{
								for(int x = i - 1; x >= 0; --x)
								{
									if(Kapcsolandok.get(x).Kocka.Nev.equals(Kapcsolandok.get(i).Kocka.Nev))
									{
										if(!torlendok.contains(x))
										{
											torlendok.add(x);
										}
										else
										{
											break;//csak akkor, ha az adott portot m�r vizsg�lta
										}
									}
								}
							}

							torlendok.sort((o1, o2) -> o2.compareTo(o1));//cs�kken� sorrendbe rendez�s

							for(int item : torlendok)
							{
								Kapcsolandok.remove(item);
							}

						}
						catch (Exception e)
						{}

						try
						{
							for(int i = 0; i < Kapcsolandok.size(); ++i)//I2C Hozz�ad�sokat az elej�re rendezi
							{
								if(Kapcsolandok.get(i).MuveletTipus == UARTPortMuveletTipus.HozzaadI2C)
								{
									Kapcsolandok.add(0, Kapcsolandok.get(i));
									Kapcsolandok.remove(i);
								}
							}
						}
						catch (Exception e)
						{}
						try
						{
							for(int i = 0; i < Kapcsolandok.size(); ++i)//Sikerteleneket a v�g�re rendezi
							{
								for(String item : Sikertelenek)
								{
									if(Kapcsolandok.get(i).Kocka.Nev.equals(item))
									{
										Kapcsolandok.add(Kapcsolandok.get(i));
										Kapcsolandok.remove(i);
									}
								}
							}
						}
						catch (Exception e)
						{}
						try
						{
							for(int i = 0; i < Kapcsolandok.size(); ++i)//�rt�klek�r�seket a v�g�re rendezi
							{
								if(Kapcsolandok.get(i).MuveletTipus == UARTPortMuveletTipus.Frissit)
								{
									Kapcsolandok.add(Kapcsolandok.get(i));
									Kapcsolandok.remove(i);
								}
							}
						}
						catch (Exception e)
						{}

						List<String> markapcsolva = new ArrayList<String>();
						for(int i = 0; i < Kapcsolandok.size();)//Minden k�rben v�gigmegy a Kapcsolando-kon �s ami sikertelen, azt kihagyja
						{
							if(!markapcsolva.contains(Kapcsolandok.get(i).Kocka.Nev))
							{
								markapcsolva.add(Kapcsolandok.get(i).Kocka.Nev);

								if(Kapcsolandok.get(i).MuveletTipus == UARTPortMuveletTipus.HozzaadI2C)
								{
									AVRCom.KockaHozzaadI2C(Kapcsolandok.get(i).Kocka);
									Kapcsolandok.remove(i);
								}
								else if(FoClass.PortokMap.containsKey(Kapcsolandok.get(i).Kocka.Nev))//Ha nincs ilyen nev� port, elt�vol�tja a list�b�l
								{
									if(Kapcsolandok.get(i).MuveletTipus == UARTPortMuveletTipus.Kapcsol)
									{
										if(AVRCom.Kapcsol(Kapcsolandok.get(i).Kocka))
										{
											FoClass.PortokMap.get(Kapcsolandok.get(i).Kocka.Nev)
													.setErtek(Kapcsolandok.get(i).Kocka.Ertek);
											for(int k = 0; k < Sikertelenek.size(); k++)
											{
												if(Sikertelenek.get(k).equals(Kapcsolandok.get(i).Kocka.Nev))
												{
													Sikertelenek.remove(k);
													--k;
												}
											}

											Kapcsolandok.remove(i);
										}
										else
										{
											if(!Sikertelenek.contains(Kapcsolandok.get(i).Kocka.Nev))
											{
												Sikertelenek.add(Kapcsolandok.get(i).Kocka.Nev);
											}
											++i;
										}
									}
									else if(Kapcsolandok.get(i).MuveletTipus == UARTPortMuveletTipus.Frissit)
									{
										if(AVRCom.PortErtekFrissit(Kapcsolandok.get(i).Kocka))
										{
											FoClass.PortokMap.get(Kapcsolandok.get(i).Kocka.Nev)
													.setErtek(Kapcsolandok.get(i).Kocka.Ertek);
											for(int k = 0; k < Sikertelenek.size(); k++)
											{
												if(Sikertelenek.get(k).equals(Kapcsolandok.get(i).Kocka.Nev))
												{
													Sikertelenek.remove(k);
													--k;
												}
											}

											Kapcsolandok.remove(i);
										}
										else
										{
											if(!Sikertelenek.contains(Kapcsolandok.get(i).Kocka.Nev))
											{
												Sikertelenek.add(Kapcsolandok.get(i).Kocka.Nev);
											}
											++i;
										}
									}

								}
								else
								{
									Kapcsolandok.remove(i);
								}
							}
							else
							{
								++i;
							}
						}
					}
					catch (Exception e)
					{}
				}
				if(FoClass.SorosPortAVR.HibaSzamlalo >= 4)
				{
					FoClass.SorosPortAVR.Kezfogas();
				}
				try
				{
					synchronized(this)
					{
						if(Kapcsolandok.size() == 0)//csak akkkor megy a notify-os v�rakoz�sba, ha minden kapcsol�s siker�lt
						{
							wait();//Notify-olni kell a sz�lat, hogy tov�bb fusson tesztelje a benne l�v� ciklusokat
						}
						else
						{
							wait(2000);
						}
					}
				}
				catch (InterruptedException e)
				{}
				try
				{
					if(FolyamatMentes)
					{
						FolyamatMentes = false;

						FajlKezelo.FolyamatKiir(FoClass.FolyamatokMap);
						System.out.println("---APPLICATION: SEGEDSZAL: Folyamatok mentve");
					}
				}
				catch (Exception e)
				{}
				try
				{
					if(PortMentes)
					{
						PortMentes = false;

						FajlKezelo.PortKiir(FoClass.PortokMap);
						System.out.println("---APPLICATION: SEGEDSZAL: Portok mentve");
					}
				}
				catch (Exception e)
				{}

			}
			catch (Exception ex)
			{
				System.out.println(ex.getMessage());
				ex.printStackTrace();
			}
		}
	}
}
