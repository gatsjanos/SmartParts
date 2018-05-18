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
			System.out.println("!!!-HIBA A FOLYAMAT KÓDJÁNAK ELÕKÉSZÍTÉSE SORÁN: FOLYAMAT NEVE: '" + Nev + "' HIBA: "
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
					.println("!!!-HIBA A FOLYAMAT FUTÁSA SORÁN: FOLYAMAT NEVE: '" + Nev + "' HIBA: " + e.getMessage());
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

		KodList.add('{');//Hogy indexelésnél az egyel kisebb indexbõl ne legyen Exception

		for(int i = 0; i < KodString.length(); ++i)
		{
			KodList.add(KodString.charAt(i));
		}

		KodList.add('}');//index Exception miatti zárójel párja
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
					return "Kilép";
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

			// !<<Ismételd()>>!!!////////////////////////////////////////////////////////////////////////////////////////////
			if(Eszkozok.IgyKezdodik("Ismételd(", KodList, i))
			{
				i += "Ismételd(".length();
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
							if(Visszateres == "Törd")
								break;
							else if(Visszateres == "Tovább")
								continue;
							else if(Visszateres == "Kilép")
								return "Kilép";

							ValtozokMap.put(parameterek.get(1), (double) (ValtozokMap.get(parameterek.get(1)) + 1));//Ismétlés változó állítása

							IsmSzam = (int) Math.round(Konvertal.ToDouble(ErtekLekero(parameterek.get(0), this).Ertek));//IsmSzam a ciklus alatt megváltozhat, ha nem egzakt szám, hanem egy változó
						}
					}
					else
					{
						for(int x = 0; x < IsmSzam; ++x)
						{
							String Visszateres = Vegrehajt(Vegrehajtando);
							if(Visszateres == "Törd")
								break;
							else if(Visszateres == "Tovább")
								continue;
							else if(Visszateres == "Kilép")
								return "Kilép";

							IsmSzam = (int) Math.round(Konvertal.ToDouble(ErtekLekero(parameterek.get(0), this).Ertek));//IsmSzam a ciklus alatt megváltozhat, ha nem egzakt szám, hanem egy változó
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
			// !<<KockaFrissít()>>!!!////////////////////////////////////////////////////////////////////////////////////////////
			if(Eszkozok.IgyKezdodik("KockaFrissít(", KodList, i))
			{
				i += "KockaFrissít(".length();

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
			// !<<KonzolÍr()>>!!!////////////////////////////////////////////////////////////////////////////////////////////
			if(Eszkozok.IgyKezdodik("KonzolÍr(", KodList, i))
			{
				i += "KonzolÍr(".length();

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
			// !<<FolyamatIndít()>>!!!////////////////////////////////////////////////////////////////////////////////////////////
			if(Eszkozok.IgyKezdodik("FolyamatIndít(", KodList, i))
			{
				i += "FolyamatIndít(".length();

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
			// !<<FolyamatLeállít()>>!!!////////////////////////////////////////////////////////////////////////////////////////////
			if(Eszkozok.IgyKezdodik("FolyamatLeállít(", KodList, i))
			{
				i += "FolyamatLeállít(".length();

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
			// !<<FolyamatÚjraindít()>>!!!////////////////////////////////////////////////////////////////////////////////////////////
			if(Eszkozok.IgyKezdodik("FolyamatÚjraindít(", KodList, i))
			{
				i += "FolyamatÚjraindít(".length();

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
			// !<<FolyamatSzinkronVégrehajt()>>!!!////////////////////////////////////////////////////////////////////////////////////////////
			if(Eszkozok.IgyKezdodik("FolyamatSzinkronVégrehajt(", KodList, i))
			{
				i += "FolyamatSzinkronVégrehajt(".length();

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
			// !<<FolyamatFelfüggeszt()>>!!!////////////////////////////////////////////////////////////////////////////////////////////
			if(Eszkozok.IgyKezdodik("FolyamatFelfüggeszt(", KodList, i))
			{
				i += "FolyamatFelfüggeszt(".length();

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
			// !<<EmailKüld(cím,tárgy,üzenet)>>!!!////////////////////////////////////////////////////////////////////////////////////////////
			if(Eszkozok.IgyKezdodik("EmailKüld(", KodList, i))
			{
				i += "EmailKüld(".length();

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
			// !<<Várj()>>!!!////////////////////////////////////////////////////////////////////////////////////////////
			if(Eszkozok.IgyKezdodik("Várj(", KodList, i))
			{
				i += "Várj(".length();

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
			// !<<Tovább>>!!!////////////////////////////////////////////////////////////////////////////////////////////
			if(Eszkozok.IgyKezdodik("Tovább;", KodList, i))
			{
				i += "Tovább;".length();

				return "Tovább";
			}
			// !<<Törd>>!!!////////////////////////////////////////////////////////////////////////////////////////////
			if(Eszkozok.IgyKezdodik("Törd;", KodList, i))
			{
				i += "Törd;".length();

				return "Törd";
			}
			// !<<Kilép;>>!!!////////////////////////////////////////////////////////////////////////////////////////////
			if(Eszkozok.IgyKezdodik("Kilép;", KodList, i))
			{
				i += "Kilép;".length();

				return "Kilép";

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
						case "Törd":
						case "Tovább":
						case "Kilép":
						{
							return visszateres;//csak akkor tér vissza, ha ki kell ugrani egy ciklusból , vagy Kilépni
						}
					}
				}

				if(Eszkozok.IgyKezdodik("Egyébként{", KodList, i + 1))//+1: az eredeti i az elõzõ blokk bezáró zárójele
				{
					i += "Egyébként{".length() + 1;
					--i;

					Epar elseBlokkErt = Eszkozok.KapcsosZarojelBlokkKiszedo(KodList, i);

					i = elseBlokkErt.EInt;

					if(kellelse)
					{
						String visszateres = Vegrehajt(elseBlokkErt.EString);
						switch(visszateres)
						{
							case "Törd":
							case "Tovább":
							case "Kilép":
							{
								return visszateres;//csak akkor tér vissza, ha ki kell ugrani egy ciklusból, vagy Kilépni
							}
						}
					}
				}
			}
			// !<<Amíg()>>!!!////////////////////////////////////////////////////////////////////////////////////////////
			if(Eszkozok.IgyKezdodik("Amíg(", KodList, i))
			{
				i += "Amíg(".length();
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
						if(Visszateres == "Törd")
							break;
						else if(Visszateres == "Tovább")
							continue;
						else if(Visszateres == "Kilép")
							return "Kilép";
					}
				}

			}
			// !<<Szám:>>!!!////////////////////////////////////////////////////////////////////////////////////////////( szám:név1,név2,név3; )
			if(KodList.size() > i + 4)
			{
				if((KodList.get(i) == 'S' || KodList.get(i) == 's') && KodList.get(i + 1) == 'z'
						&& KodList.get(i + 2) == 'á' && KodList.get(i + 3) == 'm' && KodList.get(i + 4) == ':')
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
			// !<<Változó In/Dekrementálás>>!!!////////////////////////////////////////////////////////////////////////////////////////////
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

			// !<<Változó Értékadás>>!!!////////////////////////////////////////////////////////////////////////////////////////////
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
									if(KodList.get(i + valtnev.length() + 1) != '=')//Hogy ne == operátor legyen logikai értékkéréshez
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
			// !<<Zárva()>>!!!////////////////////////////////////////////////////////////////////////////////////////////
			if(Eszkozok.IgyKezdodik("Zárva(", Feltetel, 0))
			{
				int i = "Zárva(".length();

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
			// !<<FolyamatSzünetel()>>!!!////////////////////////////////////////////////////////////////////////////////////////////
			if(Eszkozok.IgyKezdodik("FolyamatSzünetel(", Feltetel, 0))
			{
				int i = "FolyamatSzünetel(".length();

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
			// !<<RELÁCIÓ!!!////////////////////////////////////////////////////////////////////////////////////////////
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
						if(OperandIndex == 0)// Elsõ Operandus
						{
							OperandA += Feltetel.get(i);
						}
						else// Második Operandus
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

		if(Fuggveny.startsWith("\"") && Fuggveny.endsWith("\""))//String betét, nem függvény
		{
			return new Kiftip(true, Eszkozok.ParameterSzelerolIdezojelLeszedo(Fuggveny));
		}
		else if(KF.ValtozokMap.containsKey(Fuggveny))
		{
			return new Kiftip(false, KF.ValtozokMap.get(Fuggveny));
		}
		else if(Fuggveny.equals("Óra"))// Függvények
		{
			return new Kiftip(false, (double) calendar.get(Calendar.HOUR_OF_DAY));
		}
		else if(Fuggveny.equals("Perc"))// Függvények
		{
			return new Kiftip(false, (double) calendar.get(Calendar.MINUTE));
		}
		else if(Fuggveny.equals("Másodperc"))// Függvények
		{
			return new Kiftip(false, (double) calendar.get(Calendar.SECOND));
		}
		else if(Fuggveny.equals("Év"))// Függvények
		{
			return new Kiftip(false, (double) calendar.get(Calendar.YEAR));
		}
		else if(Fuggveny.equals("Hónap"))// Függvények
		{
			return new Kiftip(false, (double) calendar.get(Calendar.MONTH) + 1);
		}
		else if(Fuggveny.equals("HónapNapja"))// Függvények
		{
			return new Kiftip(false, (double) calendar.get(Calendar.DAY_OF_MONTH));
		}
		else if(Fuggveny.equals("HétNapja"))// Függvények
		{

			if(calendar.get(Calendar.DAY_OF_WEEK) == 1)
			{
				return new Kiftip(false, (double) 7);
			}
			else
				return new Kiftip(false, (double) calendar.get(Calendar.DAY_OF_WEEK) - 1);
		}
		else if(Fuggveny.startsWith("Gyök(") && ParameterenKivulNincsAlgebra(Fuggveny))// Függvények
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
			for(int i = 0; i < Kifejezes.size(); ++i)////////// -1, hogy a bezáró zárójel ne számítson
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
		else if(Fuggveny.startsWith("Hatvány(") && ParameterenKivulNincsAlgebra(Fuggveny))// Függvények
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
			for(int i = 0; i < Kifejezes.size(); ++i)////////// -1, hogy a bezáró zárójel ne számítson
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
		// TODO Analóg értéklekérés, függvényfeldolgozás megírása (pl.: Érték("portneve") értékével visszatérõ
		else // Számok
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
							continue;//NEM operátor, hanem negatív (vagy pozitív) szám, ezért elétesz egy 0-t, mintha abból vonná ki (vagy adná hozzá). Majd a következõ kör beteszi a következõ tagba
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
