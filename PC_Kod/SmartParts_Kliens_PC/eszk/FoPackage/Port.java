package FoPackage;

public class Port
{
	private PortTipus PortTipusa = PortTipus.KIRele;
	public int RFAzonosito = 0;
	public Object Ertek = 0;
	public String Nev;

	public Port(PortTipus porttipusa, int RFazonosito, String nev, Object ertek)
	{
		this.setPortTipusa(porttipusa);
		this.setErtek(ertek);
		RFAzonosito = RFazonosito;
		Nev = nev;

	}

	public Port(PortTipus porttipusa, int RFazonosito, String nev, String ertek)
	{
		this.setPortTipusa(porttipusa);
		this.setErtek(ertek);
		RFAzonosito = RFazonosito;
		Nev = nev;

	}

	public Port(PortTipus porttipusa, int RFazonosito, String nev)
	{
		this.setPortTipusa(porttipusa);
		RFAzonosito = RFazonosito;
		Nev = nev;

	}

	public Port()
	{}

	public void setErtek(String ertek)
	{
		switch(this.GetSajatErtekTipus())
		{
			case Bool:
			{
				this.Ertek = Boolean.parseBoolean(ertek);
				break;
			}
			case Int:
			{
				this.Ertek = Integer.parseInt(ertek);
				break;
			}
		}
	}

	public void setErtek(Object ertek)
	{
		try
		{
			switch(this.GetSajatErtekTipus())
			{
				case Int:
				{
					this.Ertek = (int) ertek;
					break;
				}
				case Bool:
				{
					this.Ertek = (boolean) ertek;
					break;
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public void setErtek(boolean ertek)
	{
		try
		{
			if(this.GetSajatErtekTipus() == ObjTipus.Bool)
			{
				this.Ertek = ertek;
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public void setErtek(int ertek)
	{
		try
		{
			if(this.GetSajatErtekTipus() == ObjTipus.Int)
			{
				this.Ertek = ertek;
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public static PortIrany GetIrany(PortTipus be)
	{
		switch(be)
		{
			case BEAjtoNyitas:
			case BEKapcsolo:
			case BENyomogomb:
			{
				return PortIrany.Be;
			}
			case KIRele:
			case KILed1csat:
			{
				return PortIrany.Ki;
			}
			default:
			{
				return PortIrany.NA;
			}
		}
	}

	public PortIrany GetSajatIrany()
	{
		switch(this.PortTipusa)
		{
			case BEAjtoNyitas:
			case BEKapcsolo:
			case BENyomogomb:
			{
				return PortIrany.Be;
			}
			case KIRele:
			case KILed1csat:
			{
				return PortIrany.Ki;
			}
			default:
			{
				return PortIrany.NA;
			}
		}
	}

	public static ObjTipus GetErtekTipus(PortTipus be)
	{
		switch(be)
		{
			case BEAjtoNyitas:
			case BEKapcsolo:
			case BENyomogomb:
			{
				return ObjTipus.Bool;
			}
			case KIRele:
			case KILed1csat:
			{
				return ObjTipus.Int;
			}
			default:
			{
				return null;
			}
		}
	}

	public ObjTipus GetSajatErtekTipus()
	{
		switch(this.PortTipusa)
		{
			case BEAjtoNyitas:
			case BEKapcsolo:
			case BENyomogomb:
			{
				return ObjTipus.Bool;
			}
			case KIRele:
			case KILed1csat:
			{
				return ObjTipus.Int;
			}
			default:
			{
				return null;
			}
		}
	}

	public PortTipus getPortTipusa()
	{
		return PortTipusa;
	}

	public void setPortTipusa(PortTipus portTipusa)
	{
		PortTipusa = portTipusa;
		switch(this.GetSajatErtekTipus())//Saját típusánka megfelelõ érték az Objectbe
		{
			case Bool:
			{
				this.Ertek = false;
				break;
			}
			case Int:
			{
				this.Ertek = 0;
				break;
			}
		}
	}
}
