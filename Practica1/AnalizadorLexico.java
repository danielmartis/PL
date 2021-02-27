import java.io.Random.AccessFile;
import java.io.FileNotFoundException;
import java.io.IOException;

class AnalizadorLexico {
    int fil;
    int col;
    int bytes;
    RandoAccesFile archivo;
    Token t;
    public static void main(String[] args){
        fil = 1;
        col = 1;
        bytes = 0;
        archivo = new RandomAccessFile(args[0], "r");
    }

    public Token siguienteToken(){
        int estado = 1;
        byte b[1];
        StringBuilder tok = new StringBuilder();
        do{
            archivo.read(b,bytes,1);
            if((char)b[0]) == '\t' || (char)b[0] == ' '){
                fil++;
            }
            if((char)b[0] == '\n'){
                col++;
            }
        }while((char)b[0] != '\t' && (char)b[0] != '\n' && (char)b[0] != ' ' );
        swtich(estado){
            case 1:
                t.fila = fil;
                t.columna = col;
                fil++;
                bytes++;
                tok.append((char)b[0]);
                if((char)b[0] == '(' ){
                    t.tipo = 0;
                    estado = 2;
                    t.lexema = (char)b[0];
                }
                if((char)b[0] == ')'){
                    t.tipo = 1;
                    estado = 3;
                    t.lexema = (char)b[0];
                }
                if((char)b[0] == ':'){
                    t.tipo = 2;
                    estado = 4;
                    t.lexema = (char)b[0];
                }
                if((char)b[0] == '='){
                    t.tipo = 3;
                    estado = 5;
                    t.lexema = (char)b[0];
                }
                if((char)b[0] == ';'){
                    t.tipo = 4;
                    estado = 6;
                    t.lexema = (char)b[0];
                }
            case 2:
                break;
            case 3:
                break;
            case 4:
                break;
            case 5:
                break;
            case 6:
                break;
        }
        return t;
    }
}