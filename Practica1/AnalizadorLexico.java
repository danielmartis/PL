import java.io.RandomAccessFile;
import java.io.FileNotFoundException;
import java.io.IOException;

class AnalizadorLexico {
    int fil;
    int col;
    int bytes;
    RandomAccessFile archivo;
    Token t;
    public AnalizadorLexico(RandomAccessFile file){
        fil = 1;
        col = 1;
        bytes = 0;
        archivo = file;
        t = new Token();
    }

    public Token siguienteToken(){
        int estado = 1;
        int b =0;
        StringBuilder tok = new StringBuilder();
        char leido = '\n';
        try{
            b = archivo.read();
        }catch(IOException ie){
            System.out.println("Error");
        }
        if (b == -1){
            t.tipo = 22;
            return t;
        }
        leido = (char) b;
        //System.out.println(leido);
        switch(estado){
            case 1:
                t.fila = fil;
                t.columna = col;
                fil++;
                bytes++;
                tok.append(leido);
                if(leido == '(' ){
                    t.tipo = 0;
                    estado = 2;
                    t.lexema = tok.toString();
                }
                if(leido == ')'){
                    t.tipo = 1;
                    estado = 3;
                    t.lexema = tok.toString();
                }
                if(leido == ':'){
                    t.tipo = 2;
                    estado = 4;
                    t.lexema = tok.toString();
                }
                if(leido == '='){
                    t.tipo = 3;
                    estado = 5;
                    t.lexema = tok.toString();
                }
                if(leido == ';'){
                    t.tipo = 4;
                    estado = 6;
                    t.lexema = tok.toString();
                }
                if(leido == '-'){
                    estado = 7;
                }
                else {
                    t.lexema = tok.toString();
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
            case 7:
                
        }
        return t;
    }
}