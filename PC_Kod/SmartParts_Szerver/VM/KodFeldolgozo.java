import java.util.*;

public class KodFeldolgozo extends Thread
{
	public String KodBe = "", Esemeny = "", Nev = "";
	public boolean AlljmegFlag = false, Felfuggesztve = false;
	private HashMap<String, Double> ValtozokMap = new HashMap<String, Double>();

	public KodFeldolgozo(String esemeny, String kodbe, String nev)
	{
		KodBe = kodbe;
		Esemeny = esemeny;
		Nev = nev;
	}

	public KodFeldolgozo()
	{}

	public void Stop()
	{
		AlljmegFlag = true;
		this.stop();
	}

	public void Felfuggeszt()
	{
		Felfuggesztve = true;
	}

	public void Folytat()
	{
		Felfuggesztve = false;
	}

	public void run()
	{
		Feldolgoz(KodBe);
	}

	private void Feldolgoz(String kodbe)
	{

		List<Character> KodList = new ArrayList<Character>();

		try
		{
			KodList = Eszkozok.KodElokeszitoVegrehajtasra(kodbe, this);
		}
		catch (Exception e)
		{
			System.out.println("!!!-HIBA A FOLYAMAT K�DJ�NAK EL�K�SZ�T�SE SOR�N: FOLYAMAT NEVE: '" + Nev + "' HIBA: "
					+ e.getMessage());
			e.printStackTrace();
		}

		System.out.println("--KODFELDOLGOZO: Indul: " + this.Nev);
		for(char item : KodList)
		{
			System.out.print(item);
		}
		System.out.println();
		try
		{
			Vegrehajt(KodList);
		}
		catch (Exception e)
		{
			System.out
					.println("!!!-HIBA A FOLYAMAT FUT�SA SOR�N: FOLYAMAT NEVE: '" + Nev + "' HIBA: " + e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * @param KodList
	 * @return
	 * @throws Exception
	 */
	/**
	 * @param KodList
	 * @return
	 * @throws Exception
	 */
	private String Vegrehajt(String KodString) throws Exception
	{

		List<Character> KodList = new ArrayList<Character>();

		KodList.add('{');//Hogy indexel�sn�l az egyel kisebb indexb�l ne legyen Exception

		for(int i = 0; i < KodString.length(); ++i)
		{
			KodList.add(KodString.charAt(i));
		}

		KodList.add('}');//index Exception miatti z�r�jel p�rja
		return Vegrehajt(KodList);
	}

	private String Vegrehajt(List<Character> KodList) throws Exception
	{
		boolean StringbenVan = false;
		for(int i = 0; i < KodList.size(); ++i)
		{
			while(AlljmegFlag || Felfuggesztve)
			{
				if(AlljmegFlag)
				{
					return "Kil�p";
				}
				try
				{
					Thread.sleep(2);
				}
				catch (InterruptedException e)
				{}
			}

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

			// !<<Ism�teld()>>!!!////////////////////////////////////////////////////////////////////////////////////////////
			if(Eszkozok.IgyKezdodik("Ism�teld(", KodList, i))
			{
				i += "Ism�teld(".length();
				--i;

				Epar buff = Eszkozok.FgvHivasbolParamKiszedo(KodList, i);
				String parameter = buff.EString;
				i = buff.EInt;
				++i;

				List<String> parameterek = Eszkozok.ParameterSzetvalaszto(parameter);
				int IsmSzam = (int) Math.round(Konvertal.ToDouble(ErtekLekero(parameterek.get(0), this).Ertek));
				if(parameterek.size() >= 2)
				{
					ValtozokMap.put(parameterek.get(1), (double) 0);
				}

				if(KodList.get(i) == '{')
				{
					Epar buff2 = Eszkozok.KapcsosZarojelBlokkKiszedo(KodList, i);
					List<Character> Vegrehajtando = Eszkozok.StringbolCharlist(buff2.EString);
					i = buff2.EInt;

					if(parameterek.size() >= 2 && ValtozokMap.containsKey(parameterek.get(1)))
					{
						while(ValtozokMap.get(parameterek.get(1)) < IsmSzam)
						{
							String Visszateres = Vegrehajt(Vegrehajtando);
							if(Visszateres == "T�rd")
								break;
							else if(Visszateres == "Tov�bb")
								continue;
							else if(Visszateres == "Kil�p")
								return "Kil�p";

							ValtozokMap.put(parameterek.get(1), (double) (ValtozokMap.get(parameterek.get(1)) + 1));//Ism�tl�s v�ltoz� �ll�t�sa

							IsmSzam = (int) Math.round(Konvertal.ToDouble(ErtekLekero(parameterek.get(0), this).Ertek));//IsmSzam a ciklus alatt megv�ltozhat, ha nem egzakt sz�m, hanem egy v�ltoz�
						}
					}
					else
					{
						for(int x = 0; x < IsmSzam; ++x)
						{
							String Visszateres = Vegrehajt(Vegrehajtando);
							if(Visszateres == "T�rd")
								break;
							else if(Visszateres == "Tov�bb")
								continue;
							else if(Visszateres == "Kil�p")
								return "Kil�p";

							IsmSzam = (int) Math.round(Konvertal.ToDouble(ErtekLekero(parameterek.get(0), this).Ertek));//IsmSzam a ciklus alatt megv�ltozhat, ha nem egzakt sz�m, hanem egy v�ltoz�
						}
					}

				}

			}
			// !<<Bekapcs()>>!!!////////////////////////////////////////////////////////////////////////////////////////////
			if(Eszkozok.IgyKezdodik("Bekapcs(", KodList, i))
			{
				i += "Bekapcs(".length();

				String Parameter = "";

				boolean Stringbenvan = false;
				int zarojelszam = 1;
				for(;; ++i)
				{
					if(KodList.get(i) == '"')
					{

						if(!Stringbenvan)
						{
							Stringbenvan = true;
						}
						else if(Stringbenvan && KodList.get(i - 1) != '\\')
						{
							Stringbenvan = false;
						}
					}
					if(!Stringbenvan)
					{
						if(KodList.get(i) == '(')
							++zarojelszam;
						else if(KodList.get(i) == ')')
							--zarojelszam;

						if(zarojelszam == 0)
							break;
					}
					Parameter += KodList.get(i);
				}
				Bekapcs(Parameter);
			}
			// !<<Kikapcs()>>!!!////////////////////////////////////////////////////////////////////////////////////////////
			if(Eszkozok.IgyKezdodik("Kikapcs(", KodList, i))
			{
				i += "Kikapcs(".length();

				String Parameter = "";

				boolean Stringbenvan = false;
				int zarojelszam = 1;
				for(;; ++i)
				{
					if(KodList.get(i) == '"')
					{

						if(!Stringbenvan)
						{
							Stringbenvan = true;
						}
						else if(Stringbenvan && KodList.get(i - 1) != '\\')
						{
							Stringbenvan = false;
						}
					}
					if(!Stringbenvan)
					{
						if(KodList.get(i) == '(')
							++zarojelszam;
						else if(KodList.get(i) == ')')
							--zarojelszam;

						if(zarojelszam == 0)
							break;
					}
					Parameter += KodList.get(i);
				}
				Kikapcs(Parameter);
			}
			// !<<KockaFriss�t()>>!!!////////////////////////////////////////////////////////////////////////////////////////////
			if(Eszkozok.IgyKezdodik("KockaFriss�t(", KodList, i))
			{
				i += "KockaFriss�t(".length();

				String Parameter = "";

				boolean Stringbenvan = false;
				int zarojelszam = 1;
				for(;; ++i)
				{
					if(KodList.get(i) == '"')
					{

						if(!Stringbenvan)
						{
							Stringbenvan = true;
						}
						else if(Stringbenvan && KodList.get(i - 1) != '\\')
						{
							Stringbenvan = false;
						}
					}
					if(!Stringbenvan)
					{
						if(KodList.get(i) == '(')
							++zarojelszam;
						else if(KodList.get(i) == ')')
							--zarojelszam;

						if(zarojelszam == 0)
							break;
					}
					Parameter += KodList.get(i);
				}
				PortFrissit(Parameter);

			}
			// !<<Konzol�r()>>!!!////////////////////////////////////////////////////////////////////////////////////////////
			if(Eszkozok.IgyKezdodik("Konzol�r(", KodList, i))
			{
				i += "Konzol�r(".length();

				String Parameter = "";

				boolean Stringbenvan = false;
				int zarojelszam = 1;
				for(;; ++i)
				{
					if(KodList.get(i) == '"')
					{

						if(!Stringbenvan)
						{
							Stringbenvan = true;
						}
						else if(Stringbenvan && KodList.get(i - 1) != '\\')
						{
							Stringbenvan = false;
						}
					}
					if(!Stringbenvan)
					{
						if(KodList.get(i) == '(')
							++zarojelszam;
						else if(KodList.get(i) == ')')
							--zarojelszam;

						if(zarojelszam == 0)
							break;
					}
					Parameter += KodList.get(i);
				}
				KonzolIr(Parameter);

			}
			// !<<FolyamatInd�t()>>!!!////////////////////////////////////////////////////////////////////////////////////////////
			if(Eszkozok.IgyKezdodik("FolyamatInd�t(", KodList, i))
			{
				i += "FolyamatInd�t(".length();

				String Parameter = "";

				boolean Stringbenvan = false;
				int zarojelszam = 1;
				for(;; ++i)
				{
					if(KodList.get(i) == '"')
					{

						if(!Stringbenvan)
						{
							Stringbenvan = true;
						}
						else if(Stringbenvan && KodList.get(i - 1) != '\\')
						{
							Stringbenvan = false;
						}
					}
					if(!Stringbenvan)
					{
						if(KodList.get(i) == '(')
							++zarojelszam;
						else if(KodList.get(i) == ')')
							--zarojelszam;

						if(zarojelszam == 0)
							break;
					}
					Parameter += KodList.get(i);
				}

				Parameter = Eszkozok.ParameterSzelerolIdezojelLeszedo(Parameter);

				if(FoClass.FolyamatokMap.containsKey(Parameter))
				{
					if(!FoClass.FolyamatokMap.get(Parameter).isAlive())
					{
						String esem = FoClass.FolyamatokMap.get(Parameter).Esemeny;
						String kod = FoClass.FolyamatokMap.get(Parameter).KodBe;
						FoClass.FolyamatokMap.remove(Parameter);

						FoClass.FolyamatokMap.put(Parameter, new KodFeldolgozo(esem, kod, Parameter));
						FoClass.FolyamatokMap.get(Parameter).start();
					}
				}
			}
			// !<<FolyamatLe�ll�t()>>!!!////////////////////////////////////////////////////////////////////////////////////////////
			if(Eszkozok.IgyKezdodik("FolyamatLe�ll�t(", KodList, i))
			{
				i += "FolyamatLe�ll�t(".length();

				String Parameter = "";

				boolean Stringbenvan = false;
				int zarojelszam = 1;
				for(;; ++i)
				{
					if(KodList.get(i) == '"')
					{
						if(!Stringbenvan)
						{
							Stringbenvan = true;
						}
						else if(Stringbenvan && KodList.get(i - 1) != '\\')
						{
							Stringbenvan = false;
						}
					}
					if(!Stringbenvan)
					{
						if(KodList.get(i) == '(')
							++zarojelszam;
						else if(KodList.get(i) == ')')
							--zarojelszam;

						if(zarojelszam == 0)
							break;
					}
					Parameter += KodList.get(i);
				}

				Parameter = Eszkozok.ParameterSzelerolIdezojelLeszedo(Parameter);

				if(FoClass.FolyamatokMap.containsKey(Parameter))
				{
					if(FoClass.FolyamatokMap.get(Parameter).isAlive())
					{
						FoClass.FolyamatokMap.get(Parameter).Stop();
					}
				}
			}
			// !<<Folyamat�jraind�t()>>!!!////////////////////////////////////////////////////////////////////////////////////////////
			if(Eszkozok.IgyKezdodik("Folyamat�jraind�t(", KodList, i))
			{
				i += "Folyamat�jraind�t(".length();

				String Parameter = "";

				boolean Stringbenvan = false;
				int zarojelszam = 1;
				for(;; ++i)
				{
					if(KodList.get(i) == '"')
					{

						if(!Stringbenvan)
						{
							Stringbenvan = true;
						}
						else if(Stringbenvan && KodList.get(i - 1) != '\\')
						{
							Stringbenvan = false;
						}
					}
					if(!Stringbenvan)
					{
						if(KodList.get(i) == '(')
							++zarojelszam;
						else if(KodList.get(i) == ')')
							--zarojelszam;

						if(zarojelszam == 0)
							break;
					}
					Parameter += KodList.get(i);
				}

				Parameter = Eszkozok.ParameterSzelerolIdezojelLeszedo(Parameter);

				if(FoClass.FolyamatokMap.containsKey(Parameter))
				{
					if(FoClass.FolyamatokMap.get(Parameter).isAlive())
					{
						FoClass.FolyamatokMap.get(Parameter).Stop();

						int szamlalo = 0;
						while(FoClass.FolyamatokMap.get(Parameter).isAlive() && szamlalo < 5000 / 2)
						{
							try
							{
								Thread.sleep(2);
							}
							catch (InterruptedException e)
							{}
						}

						if(!FoClass.FolyamatokMap.get(Parameter).isAlive())
						{
							String esem = FoClass.FolyamatokMap.get(Parameter).Esemeny;
							String kod = FoClass.FolyamatokMap.get(Parameter).KodBe;
							FoClass.FolyamatokMap.remove(Parameter);

							FoClass.FolyamatokMap.put(Parameter, new KodFeldolgozo(esem, kod, Parameter));
							FoClass.FolyamatokMap.get(Parameter).start();
						}
					}
					else
					{
						String esem = FoClass.FolyamatokMap.get(Parameter).Esemeny;
						String kod = FoClass.FolyamatokMap.get(Parameter).KodBe;
						FoClass.FolyamatokMap.remove(Parameter);

						FoClass.FolyamatokMap.put(Parameter, new KodFeldolgozo(esem, kod, Parameter));
						FoClass.FolyamatokMap.get(Parameter).start();
					}
				}

			}
			// !<<FolyamatSzinkronV�grehajt()>>!!!////////////////////////////////////////////////////////////////////////////////////////////
			if(Eszkozok.IgyKezdodik("FolyamatSzinkronV�grehajt(", KodList, i))
			{
				i += "FolyamatSzinkronV�grehajt(".length();

				String Parameter = "";

				boolean Stringbenvan = false;
				int zarojelszam = 1;
				for(;; ++i)
				{
					if(KodList.get(i) == '"')
					{

						if(!Stringbenvan)
						{
							Stringbenvan = true;
						}
						else if(Stringbenvan && KodList.get(i - 1) != '\\')
						{
							Stringbenvan = false;
						}
					}
					if(!Stringbenvan)
					{
						if(KodList.get(i) == '(')
							++zarojelszam;
						else if(KodList.get(i) == ')')
							--zarojelszam;

						if(zarojelszam == 0)
							break;
					}
					Parameter += KodList.get(i);
				}

				Parameter = Eszkozok.ParameterSzelerolIdezojelLeszedo(Parameter);

				if(FoClass.FolyamatokMap.containsKey(Parameter))
				{
					List<Character> szinkronKodList = new ArrayList<Character>();

					try
					{
						HashMap<String, Double> sajatMentettValtozokMap = Eszkozok.DeepCopyValtozokMap(ValtozokMap);

						ValtozokMap.clear();
						szinkronKodList = Eszkozok
								.KodElokeszitoVegrehajtasra(FoClass.FolyamatokMap.get(Parameter).KodBe, this);

						Vegrehajt(szinkronKodList);

						ValtozokMap = sajatMentettValtozokMap;

					}
					catch (Exception e)
					{}

				}

			}
			// !<<FolyamatFelf�ggeszt()>>!!!////////////////////////////////////////////////////////////////////////////////////////////
			if(Eszkozok.IgyKezdodik("FolyamatFelf�ggeszt(", KodList, i))
			{
				i += "FolyamatFelf�ggeszt(".length();

				String Parameter = "";

				boolean Stringbenvan = false;
				int zarojelszam = 1;
				for(;; ++i)
				{
					if(KodList.get(i) == '"')
					{

						if(!Stringbenvan)
						{
							Stringbenvan = true;
						}
						else if(Stringbenvan && KodList.get(i - 1) != '\\')
						{
							Stringbenvan = false;
						}
					}
					if(!Stringbenvan)
					{
						if(KodList.get(i) == '(')
							++zarojelszam;
						else if(KodList.get(i) == ')')
							--zarojelszam;

						if(zarojelszam == 0)
							break;
					}
					Parameter += KodList.get(i);
				}

				Parameter = Eszkozok.ParameterSzelerolIdezojelLeszedo(Parameter);

				if(FoClass.FolyamatokMap.containsKey(Parameter))
				{
					if(FoClass.FolyamatokMap.get(Parameter).isAlive())
					{
						FoClass.FolyamatokMap.get(Parameter).Felfuggeszt();
					}
				}
			}
			// !<<FolyamatFolytat()>>!!!////////////////////////////////////////////////////////////////////////////////////////////
			if(Eszkozok.IgyKezdodik("FolyamatFolytat(", KodList, i))
			{
				i += "FolyamatFolytat(".length();

				String Parameter = "";

				boolean Stringbenvan = false;
				int zarojelszam = 1;
				for(;; ++i)
				{
					if(KodList.get(i) == '"')
					{

						if(!Stringbenvan)
						{
							Stringbenvan = true;
						}
						else if(Stringbenvan && KodList.get(i - 1) != '\\')
						{
							Stringbenvan = false;
						}
					}
					if(!Stringbenvan)
					{
						if(KodList.get(i) == '(')
							++zarojelszam;
						else if(KodList.get(i) == ')')
							--zarojelszam;

						if(zarojelszam == 0)
							break;
					}
					Parameter += KodList.get(i);
				}

				Parameter = Eszkozok.ParameterSzelerolIdezojelLeszedo(Parameter);

				if(FoClass.FolyamatokMap.containsKey(Parameter))
				{
					if(FoClass.FolyamatokMap.get(Parameter).isAlive())
					{
						FoClass.FolyamatokMap.get(Parameter).Folytat();
					}
				}
			}
			// !<<EmailK�ld(c�m,t�rgy,�zenet)>>!!!////////////////////////////////////////////////////////////////////////////////////////////
			if(Eszkozok.IgyKezdodik("EmailK�ld(", KodList, i))
			{
				i += "EmailK�ld(".length();

				String Parameter = "";

				boolean Stringbenvan = false;
				int zarojelszam = 1;
				for(;; ++i)
				{
					if(KodList.get(i) == '"')
					{

						if(!Stringbenvan)
						{
							Stringbenvan = true;
						}
						else if(Stringbenvan && KodList.get(i - 1) != '\\')
						{
							Stringbenvan = false;
						}
					}
					if(!Stringbenvan)
					{
						if(KodList.get(i) == '(')
							++zarojelszam;
						else if(KodList.get(i) == ')')
							--zarojelszam;

						if(zarojelszam == 0)
							break;
					}
					Parameter += KodList.get(i);
				}
				EmailKuld(Parameter);

			}
			// !<<V�rj()>>!!!////////////////////////////////////////////////////////////////////////////////////////////
			if(Eszkozok.IgyKezdodik("V�rj(", KodList, i))
			{
				i += "V�rj(".length();

				String Parameter = "";

				boolean Stringbenvan = false;
				int zarojelszam = 1;
				for(;; ++i)
				{
					if(KodList.get(i) == '"')
					{

						if(!Stringbenvan)
						{
							Stringbenvan = true;
						}
						else if(Stringbenvan && KodList.get(i - 1) != '\\')
						{
							Stringbenvan = false;
						}
					}
					if(!Stringbenvan)
					{
						if(KodList.get(i) == '(')
							++zarojelszam;
						else if(KodList.get(i) == ')')
							--zarojelszam;

						if(zarojelszam == 0)
							break;
					}
					Parameter += KodList.get(i);
				}

				try
				{
					Thread.sleep(Math.round(Konvertal.ToDouble(ErtekLekero(Parameter, this).Ertek)));
				}
				catch (NumberFormatException | InterruptedException e)
				{
					System.out.println(e.getMessage());
					e.printStackTrace();
				}

			}
			// !<<Tov�bb>>!!!////////////////////////////////////////////////////////////////////////////////////////////
			if(Eszkozok.IgyKezdodik("Tov�bb;", KodList, i))
			{
				i += "Tov�bb;".length();

				return "Tov�bb";
			}
			// !<<T�rd>>!!!////////////////////////////////////////////////////////////////////////////////////////////
			if(Eszkozok.IgyKezdodik("T�rd;", KodList, i))
			{
				i += "T�rd;".length();

				return "T�rd";
			}
			// !<<Kil�p;>>!!!////////////////////////////////////////////////////////////////////////////////////////////
			if(Eszkozok.IgyKezdodik("Kil�p;", KodList, i))
			{
				i += "Kil�p;".length();

				return "Kil�p";

			}
			// !<<Ha()>>!!!////////////////////////////////////////////////////////////////////////////////////////////
			if(Eszkozok.IgyKezdodik("Ha(", KodList, i))
			{
				i += "Ha(".length();
				--i;
				Epar buff = Eszkozok.FgvHivasbolParamKiszedo(KodList, i);
				List<Character> Feltetel = Eszkozok.StringbolCharlist(buff.EString);
				i = buff.EInt;
				++i;

				Epar BlokkErt = Eszkozok.KapcsosZarojelBlokkKiszedo(KodList, i);

				i = BlokkErt.EInt;

				boolean kellelse = true;
				if(Felteteles(Feltetel, this))
				{
					kellelse = false;
					String visszateres = Vegrehajt(BlokkErt.EString);
					switch(visszateres)
					{
						case "T�rd":
						case "Tov�bb":
						case "Kil�p":
						{
							return visszateres;//csak akkor t�r vissza, ha ki kell ugrani egy ciklusb�l , vagy Kil�pni
						}
					}
				}

				if(Eszkozok.IgyKezdodik("Egy�bk�nt{", KodList, i + 1))//+1: az eredeti i az el�z� blokk bez�r� z�r�jele
				{
					i += "Egy�bk�nt{".length() + 1;
					--i;

					Epar elseBlokkErt = Eszkozok.KapcsosZarojelBlokkKiszedo(KodList, i);

					i = elseBlokkErt.EInt;

					if(kellelse)
					{
						String visszateres = Vegrehajt(elseBlokkErt.EString);
						switch(visszateres)
						{
							case "T�rd":
							case "Tov�bb":
							case "Kil�p":
							{
								return visszateres;//csak akkor t�r vissza, ha ki kell ugrani egy ciklusb�l, vagy Kil�pni
							}
						}
					}
				}
			}
			// !<<Am�g()>>!!!////////////////////////////////////////////////////////////////////////////////////////////
			if(Eszkozok.IgyKezdodik("Am�g(", KodList, i))
			{
				i += "Am�g(".length();
				--i;
				Epar buff = Eszkozok.FgvHivasbolParamKiszedo(KodList, i);
				List<Character> Feltetel = Eszkozok.StringbolCharlist(buff.EString);
				i = buff.EInt;
				++i;

				if(KodList.get(i) == '{')
				{
					Epar buff2 = Eszkozok.KapcsosZarojelBlokkKiszedo(KodList, i);
					List<Character> Vegrehajtando = Eszkozok.StringbolCharlist(buff2.EString);
					i = buff2.EInt;

					while(Felteteles(Feltetel, this))
					{
						String Visszateres = Vegrehajt(Vegrehajtando);
						if(Visszateres == "T�rd")
							break;
						else if(Visszateres == "Tov�bb")
							continue;
						else if(Visszateres == "Kil�p")
							return "Kil�p";
					}
				}

			}
			// !<<Sz�m:>>!!!////////////////////////////////////////////////////////////////////////////////////////////( sz�m:n�v1,n�v2,n�v3; )
			if(KodList.size() > i + 4)
			{
				if((KodList.get(i) == 'S' || KodList.get(i) == 's') && KodList.get(i + 1) == 'z'
						&& KodList.get(i + 2) == '�' && KodList.get(i + 3) == 'm' && KodList.get(i + 4) == ':')
				{
					boolean ervenyes = false;
					if(i != 0)
					{
						if(KodList.get(i - 1) == ' ' || KodList.get(i - 1) == '{' || KodList.get(i - 1) == '}'
								|| KodList.get(i - 1) == ';')
						{
							ervenyes = true;
						}
					}
					else
					{
						ervenyes = true;
					}

					if(ervenyes)
					{
						i += 5;

						String Parameter = "";

						while(KodList.get(i) != ';')
						{
							Parameter += KodList.get(i);
							++i;
						}

						String[] nevek = Parameter.split(",");

						double ertek = 0;
						String nev = "";
						for(String item : nevek)
						{
							if(item.contains("="))
							{
								try
								{
									String[] par = item.split("=");
									nev = par[0];

									Kiftip buff = ErtekLekero(par[1], this);
									if(!buff.Stringe)
									{
										ertek = Konvertal.ToDouble(buff.Ertek);
									}
									else
									{
										ertek = 0;
									}
								}
								catch (Exception e)
								{}
							}
							else
							{
								ertek = 0;
								nev = item;
							}
							ValtozokMap.put(nev, ertek);
						}
					}
				}
			}
			// !<<V�ltoz� In/Dekrement�l�s>>!!!////////////////////////////////////////////////////////////////////////////////////////////
			if(i > 1)
			{
				if((KodList.get(i - 1) == '+' && KodList.get(i - 2) == '+')
						|| (KodList.get(i - 1) == '-' && KodList.get(i - 2) == '-'))
				{

					for(String valtnev : ValtozokMap.keySet())
					{
						boolean egyezik = true;
						for(int x = 0; x < valtnev.length(); ++x)
						{
							try
							{
								if(KodList.get(i + x) != valtnev.charAt(x))
								{
									egyezik = false;
									break;
								}
							}
							catch (IndexOutOfBoundsException e)
							{
								egyezik = false;
								break;
							}
						}
						try
						{
							if(egyezik)
							{
								switch(KodList.get(i - 1))
								{
									case '+':
									{
										if(KodList.get(i + valtnev.length()) == ';')
										{
											i += valtnev.length();

											ValtozokMap.put(valtnev, ValtozokMap.get(valtnev) + 1);
										}
										break;
									}
									case '-':
									{

										if(KodList.get(i + valtnev.length()) == ';')
										{
											i += valtnev.length();

											ValtozokMap.put(valtnev, ValtozokMap.get(valtnev) - 1);
										}
										break;
									}
								}

							}
						}
						catch (IndexOutOfBoundsException e)
						{

						}
					}
				}
			}

			// !<<V�ltoz� �rt�kad�s>>!!!////////////////////////////////////////////////////////////////////////////////////////////
			boolean ervenyesErtekadas = false;
			if(i != 0)
			{
				if(KodList.get(i - 1) == ' ' || KodList.get(i - 1) == '{' || KodList.get(i - 1) == '}'
						|| KodList.get(i - 1) == ';')
				{
					ervenyesErtekadas = true;
				}
			}
			else
			{
				ervenyesErtekadas = true;
			}

			if(ervenyesErtekadas)
			{
				for(String valtnev : ValtozokMap.keySet())
				{
					boolean egyezik = true;
					for(int x = 0; x < valtnev.length(); ++x)
					{
						try
						{
							if(KodList.get(i + x) != valtnev.charAt(x))
							{
								egyezik = false;
								break;
							}
						}
						catch (IndexOutOfBoundsException e)
						{
							egyezik = false;
							break;
						}
					}
					try
					{
						if(egyezik)
						{
							switch(KodList.get(i + valtnev.length()))
							{
								case '=':
								{
									if(KodList.get(i + valtnev.length() + 1) != '=')//Hogy ne == oper�tor legyen logikai �rt�kk�r�shez
									{
										i += valtnev.length() + 1;
										String Parameter = "";

										while(KodList.get(i) != ';')
										{
											Parameter += KodList.get(i);
											++i;
										}

										Kiftip vissza = ErtekLekero(Parameter, this);
										if(!vissza.Stringe)
											ValtozokMap.put(valtnev, Konvertal.ToDouble(vissza.Ertek));
									}
									break;
								}

								case '+':
								{
									if(KodList.get(i + valtnev.length() + 1) == '=')
									{
										i += valtnev.length() + 2;
										String Parameter = "";

										while(KodList.get(i) != ';')
										{
											Parameter += KodList.get(i);
											++i;
										}

										Kiftip vissza = ErtekLekero(Parameter, this);
										if(!vissza.Stringe)
											ValtozokMap.put(valtnev,
													ValtozokMap.get(valtnev) + Konvertal.ToDouble(vissza.Ertek));
									}
									break;
								}
								case '-':
								{
									if(KodList.get(i + valtnev.length() + 1) == '=')
									{
										i += valtnev.length() + 2;
										String Parameter = "";

										while(KodList.get(i) != ';')
										{
											Parameter += KodList.get(i);
											++i;
										}

										Kiftip vissza = ErtekLekero(Parameter, this);
										if(!vissza.Stringe)
											ValtozokMap.put(valtnev,
													ValtozokMap.get(valtnev) - Konvertal.ToDouble(vissza.Ertek));
									}
									break;
								}
								case '*':
								{
									if(KodList.get(i + valtnev.length() + 1) == '=')
									{
										i += valtnev.length() + 2;
										String Parameter = "";

										while(KodList.get(i) != ';')
										{
											Parameter += KodList.get(i);
											++i;
										}

										Kiftip vissza = ErtekLekero(Parameter, this);
										if(!vissza.Stringe)
											ValtozokMap.put(valtnev,
													ValtozokMap.get(valtnev) * Konvertal.ToDouble(vissza.Ertek));
									}
									break;
								}
								case '/':
								{
									if(KodList.get(i + valtnev.length() + 1) == '=')
									{
										i += valtnev.length() + 2;
										String Parameter = "";

										while(KodList.get(i) != ';')
										{
											Parameter += KodList.get(i);
											++i;
										}

										Kiftip vissza = ErtekLekero(Parameter, this);
										if(!vissza.Stringe)
											ValtozokMap.put(valtnev,
													ValtozokMap.get(valtnev) / Konvertal.ToDouble(vissza.Ertek));
									}
									break;
								}
								case '%':
								{
									if(KodList.get(i + valtnev.length() + 1) == '=')
									{
										i += valtnev.length() + 2;
										String Parameter = "";

										while(KodList.get(i) != ';')
										{
											Parameter += KodList.get(i);
											++i;
										}

										Kiftip vissza = ErtekLekero(Parameter, this);
										if(!vissza.Stringe)
											ValtozokMap.put(valtnev,
													ValtozokMap.get(valtnev) % Konvertal.ToDouble(vissza.Ertek));
									}
									break;
								}
							}

						}
					}
					catch (IndexOutOfBoundsException e)
					{

					}
				}
			}
		}

		return "";

	}

	private void PortFrissit(String parameter)
	{
		try
		{
			parameter = Eszkozok.StringParameterFeldolgozo(parameter, this);

			if(FoClass.PortokMap.containsKey(parameter))
			{
				FoClass.SegedSzalTH.Kapcsolandok.add(new UARTPortMuvelet(
						new Port(FoClass.PortokMap.get(parameter).getPortTipusa(),
								FoClass.PortokMap.get(parameter).RFAzonosito, FoClass.PortokMap.get(parameter).Nev),
						UARTPortMuveletTipus.Frissit));
				synchronized(FoClass.SegedSzalTH)
				{
					FoClass.SegedSzalTH.notify();
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private void EmailKuld(String parameter)
	{
		try
		{
			List<String> parameterek = Eszkozok.ParameterSzetvalaszto(parameter);

			Email.SMTPKuld(Eszkozok.StringParameterFeldolgozo(parameterek.get(0), this),
					Eszkozok.StringParameterFeldolgozo(parameterek.get(1), this),
					Eszkozok.StringParameterFeldolgozo(parameterek.get(2), this));
		}
		catch (Exception e)
		{
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}

	private void KonzolIr(String parameter)
	{
		try
		{
			System.out.println(Eszkozok.StringParameterFeldolgozo(parameter, this));
		}
		catch (Exception e)
		{}
	}

	private void Bekapcs(String parameter)
	{
		try
		{
			parameter = Eszkozok.StringParameterFeldolgozo(parameter, this);

			if(FoClass.PortokMap.containsKey(parameter))
			{
				if(FoClass.PortokMap.get(parameter).getPortTipusa() == PortTipus.KIRele)
				{
					FoClass.SegedSzalTH.Kapcsolandok
							.add(new UARTPortMuvelet(new Port(FoClass.PortokMap.get(parameter).getPortTipusa(),
									FoClass.PortokMap.get(parameter).RFAzonosito, FoClass.PortokMap.get(parameter).Nev,
									1), UARTPortMuveletTipus.Kapcsol));
					synchronized(FoClass.SegedSzalTH)
					{
						FoClass.SegedSzalTH.notify();
					}
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private void Kikapcs(String parameter)
	{
		try
		{
			parameter = Eszkozok.StringParameterFeldolgozo(parameter, this);

			if(FoClass.PortokMap.containsKey(parameter))
			{
				if(FoClass.PortokMap.get(parameter).getPortTipusa() == PortTipus.KIRele)
				{
					FoClass.SegedSzalTH.Kapcsolandok
							.add(new UARTPortMuvelet(new Port(FoClass.PortokMap.get(parameter).getPortTipusa(),
									FoClass.PortokMap.get(parameter).RFAzonosito, FoClass.PortokMap.get(parameter).Nev,
									0), UARTPortMuveletTipus.Kapcsol));
					synchronized(FoClass.SegedSzalTH)
					{
						FoClass.SegedSzalTH.notify();
					}
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public static boolean Felteteles(List<Character> Feltetel, KodFeldolgozo KF) throws Exception
	{
		while(KF.AlljmegFlag || KF.Felfuggesztve)
		{
			if(KF.AlljmegFlag)
			{
				return false;
			}
			try
			{
				Thread.sleep(2);
			}
			catch (InterruptedException e)
			{}
		}

		List<List<Character>> Tagok = new ArrayList<List<Character>>();
		List<Character> Muveletek = new ArrayList<Character>();

		boolean kimenet;
		if(Feltetel.contains('&') || Feltetel.contains('|'))
		{
			int zarojelszam = 0;
			for(int i = 0; i < Feltetel.size(); ++i)
			{
				if(Feltetel.get(i) == '(')
					++zarojelszam;
				else if(Feltetel.get(i) == ')')
					--zarojelszam;

				if(zarojelszam == 0)
				{
					if(Feltetel.get(i) == '&' || Feltetel.get(i) == '|' || i + 1 == Feltetel.size())
					{
						List<Character> TagBuff = new ArrayList<Character>();

						for(int x = 0; x < i; ++x)
						{
							TagBuff.add(Feltetel.get(0));
							Feltetel.remove(0);
						}
						if(Feltetel.size() == 1)
						{
							TagBuff.add(Feltetel.remove(Feltetel.size() - 1));
						}

						if(TagBuff.get(0) == '(')
						{
							TagBuff.remove(0);
							if(TagBuff.get(TagBuff.size() - 1) == ')')
								TagBuff.remove(TagBuff.size() - 1);
						}

						Tagok.add(TagBuff);
						if(Feltetel.size() != 0)
						{
							Muveletek.add(Feltetel.get(0));
							Feltetel.remove(0);
							i = -1;
						}
						else
						{
							break;
						}
					}
				}

			}
			if(Tagok.get(0).get(0) != '!')
			{
				kimenet = Felteteles(Tagok.get(0), KF);
			}
			else
			{
				Tagok.get(0).remove(0);
				kimenet = !Felteteles(Tagok.get(0), KF);
			}
			while(Muveletek.size() != 0)
			{
				if(Muveletek.get(0) == '&')
				{
					if(Tagok.get(1).get(0) != '!')
					{
						kimenet = kimenet && Felteteles(Tagok.get(1), KF);
					}
					else
					{
						Tagok.get(1).remove(0);
						kimenet = kimenet && !Felteteles(Tagok.get(1), KF);
					}
				}
				else if(Muveletek.get(0) == '|')
				{
					if(Tagok.get(1).get(0) != '!')
					{
						kimenet = kimenet || Felteteles(Tagok.get(1), KF);
					}
					else
					{
						Tagok.get(1).remove(0);
						kimenet = kimenet || !Felteteles(Tagok.get(1), KF);
					}
				}
				Tagok.remove(0);
				Muveletek.remove(0);
			}
			return kimenet;
		}
		else
		{
			if(Feltetel.get(0) == '(')
			{

				boolean fejtheto = true;
				int zarojelszam = 1;
				for(int i = 1; i < Feltetel.size(); ++i)
				{
					if(Feltetel.get(i) == '(')
						++zarojelszam;
					else if(Feltetel.get(i) == ')')
						--zarojelszam;

					if(zarojelszam == 0 && i != Feltetel.size() - 1)
					{
						fejtheto = false;
						break;
					}
				}
				if(fejtheto)
				{
					Feltetel.remove(0);
					Feltetel.remove(Feltetel.size() - 1);

					return Felteteles(Feltetel, KF);
				}
			}
			// !<<Nyitva()>>!!!////////////////////////////////////////////////////////////////////////////////////////////
			if(Eszkozok.IgyKezdodik("Nyitva(", Feltetel, 0))
			{
				int i = "Nyitva(".length();

				String Parameter = "";

				boolean Stringbenvan = false;
				int zarojelszam = 1;
				for(;; ++i)
				{
					if(Feltetel.get(i) == '"')
					{

						if(!Stringbenvan)
						{
							Stringbenvan = true;
						}
						else if(Stringbenvan && Feltetel.get(i - 1) != '\\')
						{
							Stringbenvan = false;
						}
					}
					if(!Stringbenvan)
					{
						if(Feltetel.get(i) == '(')
							++zarojelszam;
						else if(Feltetel.get(i) == ')')
							--zarojelszam;

						if(zarojelszam == 0)
							break;
					}
					Parameter += Feltetel.get(i);
				}
				Parameter = Eszkozok.ParameterSzelerolIdezojelLeszedo(Parameter);
				return (boolean) FoClass.PortokMap.get(Parameter).Ertek;

			}
			// !<<Z�rva()>>!!!////////////////////////////////////////////////////////////////////////////////////////////
			if(Eszkozok.IgyKezdodik("Z�rva(", Feltetel, 0))
			{
				int i = "Z�rva(".length();

				String Parameter = "";

				boolean Stringbenvan = false;
				int zarojelszam = 1;
				for(;; ++i)
				{
					if(Feltetel.get(i) == '"')
					{

						if(!Stringbenvan)
						{
							Stringbenvan = true;
						}
						else if(Stringbenvan && Feltetel.get(i - 1) != '\\')
						{
							Stringbenvan = false;
						}
					}
					if(!Stringbenvan)
					{
						if(Feltetel.get(i) == '(')
							++zarojelszam;
						else if(Feltetel.get(i) == ')')
							--zarojelszam;

						if(zarojelszam == 0)
							break;
					}
					Parameter += Feltetel.get(i);
				}
				Parameter = Eszkozok.ParameterSzelerolIdezojelLeszedo(Parameter);
				return !(boolean) FoClass.PortokMap.get(Parameter).Ertek;
			}
			// !<<FolyamatFut()>>!!!////////////////////////////////////////////////////////////////////////////////////////////
			if(Eszkozok.IgyKezdodik("FolyamatFut(", Feltetel, 0))
			{
				int i = "FolyamatFut(".length();

				String Parameter = "";

				boolean Stringbenvan = false;
				int zarojelszam = 1;
				for(;; ++i)
				{
					if(Feltetel.get(i) == '"')
					{

						if(!Stringbenvan)
						{
							Stringbenvan = true;
						}
						else if(Stringbenvan && Feltetel.get(i - 1) != '\\')
						{
							Stringbenvan = false;
						}
					}
					if(!Stringbenvan)
					{
						if(Feltetel.get(i) == '(')
							++zarojelszam;
						else if(Feltetel.get(i) == ')')
							--zarojelszam;

						if(zarojelszam == 0)
							break;
					}
					Parameter += Feltetel.get(i);
				}
				Parameter = Eszkozok.ParameterSzelerolIdezojelLeszedo(Parameter);

				if(FoClass.FolyamatokMap.containsKey(Parameter))
				{
					return FoClass.FolyamatokMap.get(Parameter).isAlive();
				}
				else
				{
					return false;
				}

			}
			// !<<FolyamatSz�netel()>>!!!////////////////////////////////////////////////////////////////////////////////////////////
			if(Eszkozok.IgyKezdodik("FolyamatSz�netel(", Feltetel, 0))
			{
				int i = "FolyamatSz�netel(".length();

				String Parameter = "";

				boolean Stringbenvan = false;
				int zarojelszam = 1;
				for(;; ++i)
				{
					if(Feltetel.get(i) == '"')
					{

						if(!Stringbenvan)
						{
							Stringbenvan = true;
						}
						else if(Stringbenvan && Feltetel.get(i - 1) != '\\')
						{
							Stringbenvan = false;
						}
					}
					if(!Stringbenvan)
					{
						if(Feltetel.get(i) == '(')
							++zarojelszam;
						else if(Feltetel.get(i) == ')')
							--zarojelszam;

						if(zarojelszam == 0)
							break;
					}
					Parameter += Feltetel.get(i);
				}
				Parameter = Eszkozok.ParameterSzelerolIdezojelLeszedo(Parameter);

				if(FoClass.FolyamatokMap.containsKey(Parameter))
				{
					return FoClass.FolyamatokMap.get(Parameter).Felfuggesztve;
				}
				else
				{
					return false;
				}

			}
			// !<<I/igaz>>!!!////////////////////////////////////////////////////////////////////////////////////////////
			if(Eszkozok.IgyKezdodik("Igaz", Feltetel, 0) || Eszkozok.IgyKezdodik("igaz", Feltetel, 0))
			{
				return true;
			}
			// !<<H/hamis>>!!!////////////////////////////////////////////////////////////////////////////////////////////
			if(Eszkozok.IgyKezdodik("Hamis", Feltetel, 0) || Eszkozok.IgyKezdodik("hamis", Feltetel, 0))
			{
				return false;
			}
			// !<<REL�CI�!!!////////////////////////////////////////////////////////////////////////////////////////////
			if(Feltetel.contains('<') || Feltetel.contains('>') || Feltetel.contains('='))
			{

				String OperandA = "", OperandB = "", Operator = "";
				byte OperandIndex = 0;

				for(int i = 0; i < Feltetel.size(); ++i)
				{
					if(Feltetel.get(i) == '<' || Feltetel.get(i) == '>' || Feltetel.get(i) == '='
							|| (Feltetel.get(i) == '!' && Feltetel.get(i + 1) == '='))
					{
						++OperandIndex;
						Operator += Feltetel.get(i);

					}
					else
					{
						if(OperandIndex == 0)// Els� Operandus
						{
							OperandA += Feltetel.get(i);
						}
						else// M�sodik Operandus
						{
							OperandB += Feltetel.get(i);
						}
					}
				}

				Kiftip OpA = ErtekLekero(OperandA, KF);
				Kiftip OpB = ErtekLekero(OperandB, KF);

				if(OpA.Stringe || OpB.Stringe)
				{
					return false;
				}
				else
				{
					switch(Operator)
					{
						case "<":
						{
							return (double) OpA.Ertek < (double) OpB.Ertek;
						}
						case "<=":
						{
							return (double) OpA.Ertek <= (double) OpB.Ertek;
						}
						case "==":
						{
							return (double) OpA.Ertek == (double) OpB.Ertek;
						}
						case "!=":
						{
							return (double) OpA.Ertek != (double) OpB.Ertek;
						}
						case ">=":
						{
							return (double) OpA.Ertek >= (double) OpB.Ertek;
						}
						case ">":
						{
							return (double) OpA.Ertek > (double) OpB.Ertek;
						}
					}
				}
			}
		}
		return false;
	}

	public static Kiftip ErtekLekero(String Fuggveny, KodFeldolgozo KF)
	{
		Calendar calendar = new GregorianCalendar();

		if(Fuggveny.startsWith("\"") && Fuggveny.endsWith("\""))//String bet�t, nem f�ggv�ny
		{
			return new Kiftip(true, Eszkozok.ParameterSzelerolIdezojelLeszedo(Fuggveny));
		}
		else if(KF.ValtozokMap.containsKey(Fuggveny))
		{
			return new Kiftip(false, KF.ValtozokMap.get(Fuggveny));
		}
		else if(Fuggveny.equals("�ra"))// F�ggv�nyek
		{
			return new Kiftip(false, (double) calendar.get(Calendar.HOUR_OF_DAY));
		}
		else if(Fuggveny.equals("Perc"))// F�ggv�nyek
		{
			return new Kiftip(false, (double) calendar.get(Calendar.MINUTE));
		}
		else if(Fuggveny.equals("M�sodperc"))// F�ggv�nyek
		{
			return new Kiftip(false, (double) calendar.get(Calendar.SECOND));
		}
		else if(Fuggveny.equals("�v"))// F�ggv�nyek
		{
			return new Kiftip(false, (double) calendar.get(Calendar.YEAR));
		}
		else if(Fuggveny.equals("H�nap"))// F�ggv�nyek
		{
			return new Kiftip(false, (double) calendar.get(Calendar.MONTH) + 1);
		}
		else if(Fuggveny.equals("H�napNapja"))// F�ggv�nyek
		{
			return new Kiftip(false, (double) calendar.get(Calendar.DAY_OF_MONTH));
		}
		else if(Fuggveny.equals("H�tNapja"))// F�ggv�nyek
		{

			if(calendar.get(Calendar.DAY_OF_WEEK) == 1)
			{
				return new Kiftip(false, (double) 7);
			}
			else
				return new Kiftip(false, (double) calendar.get(Calendar.DAY_OF_WEEK) - 1);
		}
		else if(Fuggveny.startsWith("Gy�k(") && ParameterenKivulNincsAlgebra(Fuggveny))// F�ggv�nyek
		{

			List<Character> Kifejezes = new ArrayList<Character>();

			int zarojelszam = 1;
			for(int i = 5;; ++i)
			{
				if(Fuggveny.charAt(i) == '(')
					++zarojelszam;
				else if(Fuggveny.charAt(i) == ')')
					--zarojelszam;

				if(zarojelszam == 0)
					break;

				Kifejezes.add(Fuggveny.charAt(i));
			}

			String kitevo = "", kif = "";
			boolean voltvesszo = false;
			zarojelszam = 0;
			for(int i = 0; i < Kifejezes.size(); ++i)////////// -1, hogy a bez�r� z�r�jel ne sz�m�tson
			{
				if(Kifejezes.get(i) == '(')
					++zarojelszam;
				else if(Kifejezes.get(i) == ')')
					--zarojelszam;

				if(zarojelszam == 0 && Kifejezes.get(i) == ',')
				{
					voltvesszo = true;
					continue;
				}
				if(!voltvesszo)
					kif += Kifejezes.get(i);
				else
					kitevo += Kifejezes.get(i);

			}
			return new Kiftip(false,
					Math.pow((double) ErtekLekero(kif, KF).Ertek, (double) 1 / (double) ErtekLekero(kitevo, KF).Ertek));
		}
		else if(Fuggveny.startsWith("Hatv�ny(") && ParameterenKivulNincsAlgebra(Fuggveny))// F�ggv�nyek
		{

			List<Character> Kifejezes = new ArrayList<Character>();

			int zarojelszam = 1;
			for(int i = 8;; ++i)
			{
				if(Fuggveny.charAt(i) == '(')
					++zarojelszam;
				else if(Fuggveny.charAt(i) == ')')
					--zarojelszam;

				if(zarojelszam == 0)
					break;

				Kifejezes.add(Fuggveny.charAt(i));
			}

			String kitevo = "", kif = "";
			boolean voltvesszo = false;
			zarojelszam = 0;
			for(int i = 0; i < Kifejezes.size(); ++i)////////// -1, hogy a bez�r� z�r�jel ne sz�m�tson
			{
				if(Kifejezes.get(i) == '(')
					++zarojelszam;
				else if(Kifejezes.get(i) == ')')
					--zarojelszam;

				if(zarojelszam == 0 && Kifejezes.get(i) == ',')
				{
					voltvesszo = true;
					continue;
				}
				if(!voltvesszo)
					kif += Kifejezes.get(i);
				else
					kitevo += Kifejezes.get(i);

			}
			return new Kiftip(false,
					Math.pow((double) ErtekLekero(kif, KF).Ertek, (double) ErtekLekero(kitevo, KF).Ertek));
		}
		else if(KF.ValtozokMap.containsKey(Fuggveny))
		{
			return new Kiftip(false, KF.ValtozokMap.get(Fuggveny));
		}
		// TODO Anal�g �rt�klek�r�s, f�ggv�nyfeldolgoz�s meg�r�sa (pl.: �rt�k("portneve") �rt�k�vel visszat�r�
		else // Sz�mok
		{
			try
			{
				List<Character> FuggvenyList = new ArrayList<Character>();

				for(int i = 0; i < Fuggveny.length(); ++i)
				{
					FuggvenyList.add(Fuggveny.charAt(i));
				}
				if(Eszkozok.ContainsStringNEMSZOVEGBEN(FuggvenyList, "+")
						|| Eszkozok.ContainsStringNEMSZOVEGBEN(FuggvenyList, "-")
						|| Eszkozok.ContainsStringNEMSZOVEGBEN(FuggvenyList, "*")
						|| Eszkozok.ContainsStringNEMSZOVEGBEN(FuggvenyList, "/")
						|| Eszkozok.ContainsStringNEMSZOVEGBEN(FuggvenyList, "%"))
				{
					return Algebra(FuggvenyList, KF);
				}
				else
					return new Kiftip(false, Konvertal.ToDouble(Fuggveny));
			}
			catch (NumberFormatException e)
			{
				e.printStackTrace();
			}
		}
		return new Kiftip(false, 0);
	}

	private static boolean ParameterenKivulNincsAlgebra(String Kifejezes)
	{
		boolean voltmarzarojel = false;
		int zarojelszam = 0;
		for(int i = 0; i < Kifejezes.length(); ++i)
		{
			if(Kifejezes.charAt(i) == '(')
			{
				++zarojelszam;
				voltmarzarojel = true;
			}
			else if(Kifejezes.charAt(i) == ')')
				--zarojelszam;

			if(zarojelszam == 0 && voltmarzarojel)
			{
				if(i == Kifejezes.length() - 1)
					return true;
				else
					return false;

				//break;
			}
		}
		return true;
	}

	private static Kiftip Algebra(List<Character> Kifejezes, KodFeldolgozo KF)
	{
		while(KF.AlljmegFlag || KF.Felfuggesztve)
		{
			if(KF.AlljmegFlag)
			{
				return new Kiftip(false, 0);
			}
			try
			{
				Thread.sleep(2);
			}
			catch (InterruptedException e)
			{}
		}
		List<List<Character>> Tagok = new ArrayList<List<Character>>();
		List<Character> Muveletek = new ArrayList<Character>();

		if(Eszkozok.ContainsStringNEMSZOVEGBEN(Kifejezes, "+") || Eszkozok.ContainsStringNEMSZOVEGBEN(Kifejezes, "-")
				|| Eszkozok.ContainsStringNEMSZOVEGBEN(Kifejezes, "*")
				|| Eszkozok.ContainsStringNEMSZOVEGBEN(Kifejezes, "/")
				|| Eszkozok.ContainsStringNEMSZOVEGBEN(Kifejezes, "%"))
		{
			int zarojelszam = 0;
			for(int i = 0; i < Kifejezes.size(); ++i)
			{
				if(Kifejezes.get(i) == '(')
					++zarojelszam;
				else if(Kifejezes.get(i) == ')')
					--zarojelszam;

				if(zarojelszam == 0)
				{
					if(Kifejezes.get(i) == '+' || Kifejezes.get(i) == '-' || Kifejezes.get(i) == '*'
							|| Kifejezes.get(i) == '/' || Kifejezes.get(i) == '%' || i + 1 == Kifejezes.size())
					{
						if(i == 0 && (Kifejezes.get(0) == '+' || Kifejezes.get(0) == '-'))
						{
							Kifejezes.add(0, '0');
							++i;
							continue;//NEM oper�tor, hanem negat�v (vagy pozit�v) sz�m, ez�rt el�tesz egy 0-t, mintha abb�l vonn� ki (vagy adn� hozz�). Majd a k�vetkez� k�r beteszi a k�vetkez� tagba
						}

						List<Character> TagBuff = new ArrayList<Character>();

						for(int x = 0; x < i; ++x)
						{
							TagBuff.add(Kifejezes.get(0));
							Kifejezes.remove(0);
						}
						if(Kifejezes.size() == 1)
						{
							TagBuff.add(Kifejezes.remove(Kifejezes.size() - 1));
						}

						if(TagBuff.get(0) == '(' && TagBuff.get(TagBuff.size() - 1) == ')')
						{
							TagBuff.remove(0);
							TagBuff.remove(TagBuff.size() - 1);
						}

						Tagok.add(TagBuff);
						if(Kifejezes.size() != 0)
						{
							Muveletek.add(Kifejezes.get(0));
							Kifejezes.remove(0);
							i = -1;
						}
						else
						{
							break;
						}
					}
				}

			}

			List<Kiftip> TagokErtekei = new ArrayList<Kiftip>();

			for(int i = 0; i < Tagok.size(); ++i)
			{
				TagokErtekei.add(Algebra(Tagok.get(i), KF));
			}

			for(int i = 0; i < Muveletek.size(); ++i)
			{
				if(Muveletek.get(i) == '*' || Muveletek.get(i) == '/' || Muveletek.get(i) == '%')
				{
					if(TagokErtekei.get(i).Stringe || TagokErtekei.get(i + 1).Stringe)
					{
						if(TagokErtekei.get(i).Stringe && TagokErtekei.get(i + 1).Stringe)
						{
							TagokErtekei.add(i, new Kiftip(true,
									(String) TagokErtekei.get(i).Ertek + (String) TagokErtekei.get(i + 1).Ertek));
						}
						else if(TagokErtekei.get(i).Stringe)
						{
							TagokErtekei.add(i, new Kiftip(true, (String) TagokErtekei.get(i).Ertek
									+ Eszkozok.DecFormat((double) TagokErtekei.get(i + 1).Ertek)));
						}
						else if(TagokErtekei.get(i + 1).Stringe)
						{
							TagokErtekei.add(i, new Kiftip(true, Eszkozok.DecFormat((double) TagokErtekei.get(i).Ertek)
									+ (String) TagokErtekei.get(i + 1).Ertek));
						}
					}
					else
					{
						switch(Muveletek.get(i))
						{
							case '*':
							{
								TagokErtekei.add(i, new Kiftip(false,
										(double) TagokErtekei.get(i).Ertek * (double) TagokErtekei.get(i + 1).Ertek));
								break;
							}
							case '/':
							{
								TagokErtekei.add(i, new Kiftip(false,
										(double) TagokErtekei.get(i).Ertek / (double) TagokErtekei.get(i + 1).Ertek));
								break;
							}
							case '%':
							{
								TagokErtekei.add(i, new Kiftip(false,
										(double) TagokErtekei.get(i).Ertek % (double) TagokErtekei.get(i + 1).Ertek));
								break;
							}
						}
					}
					TagokErtekei.remove(i + 1);
					TagokErtekei.remove(i + 1);
					Muveletek.remove(i);
					--i;
				}
			}

			for(int i = 0; i < Muveletek.size(); ++i)
			{
				if(Muveletek.get(i) == '+' || Muveletek.get(i) == '-')
				{
					if(TagokErtekei.get(i).Stringe || TagokErtekei.get(i + 1).Stringe)
					{
						if(TagokErtekei.get(i).Stringe && TagokErtekei.get(i + 1).Stringe)
						{
							TagokErtekei.add(i, new Kiftip(true,
									(String) TagokErtekei.get(i).Ertek + (String) TagokErtekei.get(i + 1).Ertek));
						}
						else if(TagokErtekei.get(i).Stringe)
						{
							TagokErtekei.add(i, new Kiftip(true, (String) TagokErtekei.get(i).Ertek
									+ Eszkozok.DecFormat((double) TagokErtekei.get(i + 1).Ertek)));
						}
						else if(TagokErtekei.get(i + 1).Stringe)
						{
							TagokErtekei.add(i, new Kiftip(true, Eszkozok.DecFormat((double) TagokErtekei.get(i).Ertek)
									+ (String) TagokErtekei.get(i + 1).Ertek));
						}
					}
					else
					{
						switch(Muveletek.get(i))
						{
							case '+':
							{
								TagokErtekei.add(i, new Kiftip(false,
										(double) TagokErtekei.get(i).Ertek + (double) TagokErtekei.get(i + 1).Ertek));
								break;
							}
							case '-':
							{
								TagokErtekei.add(i, new Kiftip(false,
										(double) TagokErtekei.get(i).Ertek - (double) TagokErtekei.get(i + 1).Ertek));
								break;
							}
						}
					}
					TagokErtekei.remove(i + 1);
					TagokErtekei.remove(i + 1);
					Muveletek.remove(i);
					--i;
				}
			}

			return TagokErtekei.get(0);
		}
		else
		{
			if(Kifejezes.get(0) == '(')
			{

				boolean fejtheto = true;
				int zarojelszam = 1;
				for(int i = 1; i < Kifejezes.size(); ++i)
				{
					if(Kifejezes.get(i) == '(')
						++zarojelszam;
					else if(Kifejezes.get(i) == ')')
						--zarojelszam;

					if(zarojelszam == 0 && i != Kifejezes.size() - 1)
					{
						fejtheto = false;
						break;
					}
				}
				if(fejtheto)
				{
					Kifejezes.remove(0);
					Kifejezes.remove(Kifejezes.size() - 1);

					return Algebra(Kifejezes, KF);
				}
			}

			String buff = "";
			for(Character item : Kifejezes)
			{
				buff += item;
			}

			return ErtekLekero(buff, KF);

		}
		// return 0;//Unreachable
	}
}
