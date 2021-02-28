import java.io.RandomAccessFile;
import java.io.FileNotFoundException;
import java.io.IOException;

class AnalizadorLexico {
    int fil;
    int col;
    int bytes;
    RandomAccessFile archivo;
    Token t;
    StringBuilder almacenado = new StringBuilder();

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
        
        if(leido == ' ' || leido == '\t'){
            col++;
            siguienteToken();
            estado = -1;
        }
        else if (leido == '\n'){
            fil++;
            col = 1;
            siguienteToken();
            estado = -1;
        }
        //System.out.println(leido);
        switch(estado){
            case 1:
                t.fila = fil;
                t.columna = col;
                col++;
                bytes++;
                tok.append(leido);
                if(leido == '(' ){
                    t.tipo = 0;
                    estado = 2;
                    t.lexema = tok.toString();
                    break;
                }
                else if(leido == ')'){
                    t.tipo = 1;
                    estado = 3;
                    t.lexema = tok.toString();
                    break;
                }
                else if(leido == ':'){
                    t.tipo = 2;
                    estado = 4;
                    t.lexema = tok.toString();
                    break;
                }
                else if(leido == '='){
                    t.tipo = 3;
                    estado = 5;
                    t.lexema = tok.toString();
                    break;
                }
                else if(leido == ';'){
                    t.tipo = 4;
                    estado = 6;
                    t.lexema = tok.toString();
                    break;
                }
                else if(leido == '+'){
                    t.tipo = 6;
                    t.lexema = tok.toString();
                }
                else if(leido == '*'){
                    t.tipo = 7;
                    t.lexema = tok.toString();
                }
                else if(leido == '/'){
                    t.tipo = 7;
                    t.lexema = tok.toString();
                }
                else if(leido == '-'){
                    estado = 7;
                }
                else if(leido == 'f'){
                    estado = 13;                    
                }
                else {
                    t.lexema = tok.toString();
                }
                
            case 7:
                if (estado == 7){
                    try{
                        b = archivo.read();
                    }catch(IOException ie){
                        System.out.println("Error");
                    }
                    leido = (char) b;
                    if(leido == '>'){                   
                        tok.append(leido);
                        t.tipo = 3;
                        t.lexema = tok.toString();
                    }
                    else{
                        t.tipo = 6;
                        t.lexema = tok.toString();
                    } 
                    return t;              
                }
                
            case 13:
                if(estado == 13){

                    try{
                        b = archivo.read();
                    }catch(IOException ie){
                        System.out.println("Error");
                    }
                    leido = (char) b;
                    col++;
                    tok.append(leido);
                    if(leido == 'n'){
                        estado = 14;
                    }
                }

            case 14:
                if(estado == 14){

                    try{
                        b = archivo.read();
                    }catch(IOException ie){
                        System.out.println("Error");
                    }
                    col++;
                    leido = (char) b;
                    if((b >= 48 && b<= 57) || (b >= 65 && b<= 90) || (b >= 97 && b <= 122)){
                        if(b != 98 && b!=105){
                            estado = 16;
                        }
                    }
                    else {
                        t.tipo = 8;
                        t.lexema = tok.toString();
                        almacenado.append(leido);
                    }
                }
            case 16:
            /*
                if(estado == 16){
                    tok.append('p');
                    t.lexema = tok.toString();
                }
            */
        }
        return t;
    }
}