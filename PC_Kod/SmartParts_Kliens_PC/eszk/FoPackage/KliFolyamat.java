package FoPackage;

public class KliFolyamat
{
	public String Nev = "", Esemeny = "", Kod = "";
	public FolyStatusz Statusz = FolyStatusz.NA;

	public KliFolyamat(String nev, String esemeny, String kod, FolyStatusz statusz)
	{
		Nev = nev;
		Esemeny = esemeny;
		Kod = kod;
		Statusz = statusz;
	}
	
	public KliFolyamat(String nev, String esemeny, String kod)
	{
		Nev = nev;
		Esemeny = esemeny;
		Kod = kod;
	}
	
	public static FolyStatusz GetEnumFromString(String be)
	{
		switch(be)
		{
			case "Fut":
			{
				return FolyStatusz.Fut;
			}
			case "Sz�netel":
			{
				return FolyStatusz.Sz�netel;
			}
			case "�ll":
			{
				return FolyStatusz.�ll;
			}
			case "NA":
			{
				return FolyStatusz.NA;
			}
			default:
			{
				return null;
			}
		}
	}
}
