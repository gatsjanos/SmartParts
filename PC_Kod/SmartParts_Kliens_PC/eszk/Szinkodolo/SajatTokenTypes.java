package Szinkodolo;
import java.awt.Font;
import org.fife.ui.rsyntaxtextarea.Style;

public class SajatTokenTypes
{
	static final int NULL = 0;

	static final int Fuggvenyhivas = 1;
	static final int Ciklus = 2;
	static final int Operator = 3;
	static final int Alap = 4;//IDENTIFIER
	static final int Komment = 5;
	static final int Elvalaszto = 6;//pl.: ','
	static final int Zarojel = 7;//7
	static final int KapcsosZarojel = 22;//8
	static final int Terkoz = 9;
	static final int Kulcsszo = 10;
	static final int Szoveg = 11;
	static final int Szam = 12;
	static final int Kilep = 13;
	static final int Modosito = 14;
	static final int ValtozoTipus = 15;
	static final int Pontosvesszo = 16;
	static final int Valtozo = 17;
	

	static final int DEFAULT_NUM_TOKEN_TYPES = 39;

	public static Style[] GetStyleTomb()
	{
		return new Style[] {
				new Style(java.awt.Color.BLACK), //0 //Összes paraméter: Elõtérszín, Háttérszín, Betûtípus, Aláhúzás (nézd meg a Style forrását)
				new Style(new java.awt.Color(255, 135, 15)),//1
				new Style(java.awt.Color.BLUE),//2
				new Style(new java.awt.Color(170, 50, 0)),//3
				new Style(java.awt.Color.BLACK),//4
				new Style(new java.awt.Color(181, 230, 29)), //5
				new Style(java.awt.Color.YELLOW),//6
				new Style(java.awt.Color.RED),//7
				new Style(java.awt.Color.darkGray),//8
				new Style(),//9
				new Style(java.awt.Color.BLUE,new java.awt.Color(0, 150, 255 ,120),new Font("Consolas", Font.BOLD, 14)), //10
				new Style(java.awt.Color.WHITE),//11
				new Style(java.awt.Color.lightGray),//12
				new Style(new java.awt.Color(255, 105, 40),new java.awt.Color(255, 0, 0 ,130),new Font("Consolas", Font.BOLD, 14)), //13
				new Style(new java.awt.Color(0, 75, 255)),//14
				new Style(new java.awt.Color(0, 75, 135),null, new Font("Consolas", Font.BOLD, 14)), //15
				new Style(java.awt.Color.YELLOW),//16
				new Style(java.awt.Color.GREEN),//17
				new Style(java.awt.Color.BLACK),
				new Style(java.awt.Color.BLACK),
				new Style(java.awt.Color.BLACK), //20
				new Style(java.awt.Color.BLACK),
				new Style(java.awt.Color.LIGHT_GRAY),//22
				new Style(java.awt.Color.BLACK),
				new Style(java.awt.Color.BLACK),
				new Style(java.awt.Color.BLACK), //25
				new Style(java.awt.Color.BLACK),
				new Style(java.awt.Color.BLACK),
				new Style(java.awt.Color.BLACK),
				new Style(java.awt.Color.BLACK),
				new Style(java.awt.Color.BLACK),//30
				new Style(java.awt.Color.BLACK),
				new Style(java.awt.Color.BLACK),
				new Style(java.awt.Color.BLACK),
				new Style(java.awt.Color.BLACK),
				new Style(java.awt.Color.BLACK),//35
				new Style(java.awt.Color.BLACK),
				new Style(java.awt.Color.BLACK),
				new Style(java.awt.Color.BLACK),
				new Style(java.awt.Color.BLACK),
				new Style(java.awt.Color.BLACK),//40
		};
	}

}