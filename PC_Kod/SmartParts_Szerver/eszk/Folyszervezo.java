import java.util.*;

public class Folyszervezo extends Thread
{

	public void run()
	{
		IdoEsemeny();
	}

	public void IdoEsemeny()
	{
		List<String> Kulcsok = new ArrayList<String>();

		int elozosec = new GregorianCalendar().get(Calendar.SECOND) / 10;//Minden lekéréskor új példány kell a naptárból, mert csak akkor frissíti az idõt
		int[] startIdo = new int[7];//ememény String:   Ütemez(Év,hónap,Hónapnapja,Hétnapja,óra,perc,sec)//sec-nél lebutítás:10 secenkénti ellenõrzés //Amelyik helyén -1 van, abból az összes lehetõség megfelel
		while(true)
		{

			Kulcsok.clear();
			Kulcsok.addAll(FoClass.FolyamatokMap.keySet());

			try
			{
				for(int i = 0; i < Kulcsok.size(); ++i)//végigmegy az összes folyamaton
				{
					try
					{
						startIdo = IdoesemErtelmezo(FoClass.FolyamatokMap.get(Kulcsok.get(i)).Esemeny);
						if(startIdo != null)//Ha a folyamat eseménye megegyezik a bekövetkezettel
						{
							if(IdoegyezesTeszt(startIdo))
							{
								if(!FoClass.FolyamatokMap.get(Kulcsok.get(i)).isAlive())
								{
									String esem = FoClass.FolyamatokMap.get(Kulcsok.get(i)).Esemeny;
									String kod = FoClass.FolyamatokMap.get(Kulcsok.get(i)).KodBe;
									FoClass.FolyamatokMap.remove(Kulcsok.get(i));

									FoClass.FolyamatokMap.put(Kulcsok.get(i),
											new KodFeldolgozo(esem, kod, Kulcsok.get(i)));
									FoClass.FolyamatokMap.get(Kulcsok.get(i)).start();
								}
							}
						}
					}
					catch (ConcurrentModificationException e)
					{
						--i;
					}
					catch (NullPointerException e)
					{
						e.printStackTrace();
					}
				}
			}
			catch (Exception e)
			{}
			while(elozosec == new GregorianCalendar().get(Calendar.SECOND) / 10)
			{
				try
				{
					Thread.sleep(2000);
				}
				catch (InterruptedException e)
				{

				}
			}
			elozosec = new GregorianCalendar().get(Calendar.SECOND) / 10;

		}
	}

	int[] IdoesemErtelmezo(String esemeny)
	{
		int[] ki = new int[7];

		try
		{
			if(esemeny.length() > 7)
			{
				if(esemeny.charAt(0) == 'Ü' && esemeny.charAt(1) == 't' && esemeny.charAt(2) == 'e'
						&& esemeny.charAt(3) == 'm' && esemeny.charAt(4) == 'e' && esemeny.charAt(5) == 'z'
						&& esemeny.charAt(6) == '(')
				{
					String[] parameterek = esemeny.split(",");
					parameterek[0] = parameterek[0].substring(7, parameterek[0].length());

					parameterek[parameterek.length - 1] = parameterek[parameterek.length - 1].substring(0,
							parameterek[parameterek.length - 1].length() - 1);
					for(int i = 0; i < parameterek.length; ++i)
					{
						ki[i] = Integer.parseInt(parameterek[i]);
					}

					return ki;
				}
			}
		}
		catch (Exception e)
		{}

		return null;
	}

	boolean IdoegyezesTeszt(int[] be)
	{
		Calendar calendar = new GregorianCalendar();
		
		if(be[0] != -1)
		{
			if(be[0] != calendar.get(Calendar.YEAR))
				return false;
		}
		if(be[1] != -1)
		{
			if(be[1] != calendar.get(Calendar.MONTH) + 1)
				return false;
		}
		if(be[2] != -1)
		{
			if(be[2] != calendar.get(Calendar.DAY_OF_MONTH))
				return false;
		}
		if(be[3] != -1)
		{
			if(calendar.get(Calendar.DAY_OF_WEEK) == 1)
			{
				if(be[3] != 7)
					return false;
			}
			else if(be[3] != calendar.get(Calendar.DAY_OF_WEEK) - 1)
				return false;
		}
		if(be[4] != -1)
		{
			if(be[4] != calendar.get(Calendar.HOUR_OF_DAY))
				return false;
		}
		if(be[5] != -1)
		{
			if(be[5] != calendar.get(Calendar.MINUTE))
				return false;
		}
		if(be[6] != -1)
		{
			if((int) (be[6] / 10) != (int) (calendar.get(Calendar.SECOND) / 10))
				return false;
		}
		return true;
	}

	public static void EsemenyTeszt(String esemeny)//MINDEN bekövetkezõ eseménykor meg kell hívni
	{
		try
		{
			List<String> Kulcsok = new ArrayList<String>();

			Kulcsok.clear();
			Kulcsok.addAll(FoClass.FolyamatokMap.keySet());

			String PORT = Eszkozok.EsemenybolPortKiszedo(esemeny);

			for(int i = 0; i < Kulcsok.size(); ++i)//végigmegy az összes folyamaton
			{
				try
				{
					boolean mehet = false;
					if(Eszkozok.EsemenybolPortKiszedo(FoClass.FolyamatokMap.get(Kulcsok.get(i)).Esemeny).equals(PORT))//A folyamat portja megegyezik-e az aktuális esmény portjával?
					{
						if(esemeny.startsWith("Nyitva("))
						{
							if(FoClass.FolyamatokMap.get(Kulcsok.get(i)).Esemeny.startsWith("Nyitva(")
									|| FoClass.FolyamatokMap.get(Kulcsok.get(i)).Esemeny.startsWith("Vált("))
							{
								mehet = true;
							}
						}
						else if(esemeny.startsWith("Zárva("))
						{
							if(FoClass.FolyamatokMap.get(Kulcsok.get(i)).Esemeny.startsWith("Zárva(")
									|| FoClass.FolyamatokMap.get(Kulcsok.get(i)).Esemeny.startsWith("Vált("))
							{
								mehet = true;
							}
						}

						if(mehet)
						{
							if(!FoClass.FolyamatokMap.get(Kulcsok.get(i)).isAlive())
							{
								String esem = FoClass.FolyamatokMap.get(Kulcsok.get(i)).Esemeny;
								String kod = FoClass.FolyamatokMap.get(Kulcsok.get(i)).KodBe;
								FoClass.FolyamatokMap.remove(Kulcsok.get(i));

								FoClass.FolyamatokMap.put(Kulcsok.get(i), new KodFeldolgozo(esem, kod, Kulcsok.get(i)));
								FoClass.FolyamatokMap.get(Kulcsok.get(i)).start();
							}
						}
					}
				}
				catch (ConcurrentModificationException e)
				{
					--i;
				}
				catch (NullPointerException e)
				{
					System.out.println(e.getMessage());
					e.printStackTrace();
				}
				catch(Exception e)
				{
					System.out.println(e.getMessage());
					e.printStackTrace();
				}
			}
			System.out.println("--APPLICATION: ESEMÉNYTESZTELVE: " + esemeny);
		}
		catch (Exception e)
		{
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}

	public static boolean PortValtozott(String Bekovetkezett, String FolyEsem)
	{
		if(FolyEsem.startsWith("Vált("))
		{
			String[] buff1 = FolyEsem.split("\\(");
			buff1 = buff1[1].split("\\)");

			String[] buff2 = Bekovetkezett.split("\\(");
			buff2 = buff2[1].split("\\)");

			if(buff1[0].equals(buff2[0]))
				return true;
		}

		return false;
	}
}
