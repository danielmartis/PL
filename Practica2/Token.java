import java.util.ArrayList;

public class Token {

	public int fila;
	public int columna;

	public String lexema;

	public int tipo;		// tipo es: ID, ENTERO, REAL ...

	public static final int
		PARI 		= 0,
		PARD		= 1,
		DOSP            = 2,
		RET             = 3,
		ASIG		= 4,
		PYC		= 5,
		OPAS		= 6,
		OPMD		= 7,
		FN		= 8,
		ENDFN		= 9,
		INT		= 10,
		REAL		= 11,
		LET		= 12,
		PRINT           = 13,
		ID		= 14,
		NUMINT		= 15,
		NUMREAL		= 16,
		EOF		= 17;

	public String toString(){
	    switch(tipo){
			case 0:
				return "(";
			case 1: 
				return ")";
			case 2:
				return ":";
			case 3:
				return "->";
			case 4:
				return "=";
			case 5:
				return ";";
			case 6:
				return "+ -";
			case 7:
				return "* /";
			case 8:
				return "'fn'";
			case 9:
				return "'endfn'";
			case 10:
				return "'int'";
			case 11:
				return "'real'";
			case 12:
				return  "'let'";
			case 13:
				return "'print'";
			case 14:
				return "identificador";
			case 15:
				return "numero entero";
			case 16:
				return "numero real";
			case 17:
				return "fin de fichero";
		}
		return "";
	}
}

