package FoPackage;
import java.util.List;
import java.util.ArrayList;

public class Epar
{
	public boolean EBool = false;
	public String EString = "";
	public List<Character> ECharList = new ArrayList<Character>();
	public Integer EInt = 0;
	public double EDouble = 0;

	public Epar(String estring, Integer eint)
	{
		EString = estring;
		EInt = eint;
	}
}
