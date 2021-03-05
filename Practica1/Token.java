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
		IF              = 13,
		ELSE            = 14,
		FI              = 15,
		PRINT           = 16,
		BLQ		= 17,
		FBLQ		= 18,
		ID		= 19,
		NUMINT		= 20,
		NUMREAL		= 21,
		EOF		= 22;

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
				return "'if'";
			case 14:
				return "'else'";
			case 15:
				return "'fi'";
			case 16:
				return "'print'";
			case 17:
				return "'blq'";
			case 18:
				return "'fblq'";
			case 19:
				return "identifcador";
			case 20:
				return "numint";
			case 21:
				return "numreal";
		}
		return "";
	}
}

