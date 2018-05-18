public class Konvertal
{
	public static double ToDouble(String be) throws NumberFormatException
	{
		try
		{
			return Double.parseDouble(be);
		}
		catch(Exception e)
		{
			
		}
		try
		{
			return (double) Integer.parseInt(be);
		}
		catch(Exception e)
		{
			
		}
		
		throw new NumberFormatException("Konverzios hiba.");
	}
	public static double ToDouble(Object be) throws NumberFormatException
	{
		try
		{
			return (double)be;
		}
		catch(Exception e)
		{
			
		}
		try
		{
			return (double)(int)be;
		}
		catch(Exception e)
		{
			
		}
		try
		{
			return ToDouble((String)be);
		}
		catch(Exception e)
		{
			
		}

		throw new NumberFormatException("Konverzios hiba.");
	}
}
