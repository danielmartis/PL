import java.io.RandomAccessFile;
import java.io.FileNotFoundException;
import java.io.IOException;

class AnalizadorLexico {
    int fil;
    int col;
    int bytes;
    RandomAccessFile archivo;
    Token t;
    char almacenado;

    public AnalizadorLexico(RandomAccessFile file){
        fil = 1;
        col = 1;
        bytes = 0;
        archivo = file;
        t = new Token();
        almacenado = 0;
    }

    public int leerCaracter(){
        int leido = 0;
        try{
            leido = archivo.read();
        }catch(IOException ie){
            System.out.println("Error");
        }
        return leido;
    }
    public boolean Espacio(char l){
        if(l == ' ' || l == '\n' || l == '\t')
            return true;
        return false;
    }

    public boolean letraDigito(int car){
        if((car >= 48 && car <= 57) || (car >= 65 && car<=90) || (car >= 97 && car <= 122)){
            return true;
        }
        return false;
    }
    public Token siguienteToken(){
        int estado = 1;
        int b =0;
        StringBuilder tok = new StringBuilder();
        char leido = '\n';
        boolean asterisco = false;

        if(almacenado != 0){
            leido = almacenado;
            //ystem.out.println(leido);
            almacenado = 0;
            t.fila = fil;
            t.columna = col;
        }
        else{
            b = leerCaracter();
            if (b == -1){
                t.tipo = 22;
                return t;
            }
            t.fila = fil;
            t.columna = col;
            col++;
            leido = (char) b;
        }

        
        if(leido == ' ' || leido == '\t'){
            //col++;
            siguienteToken();
            estado = -1;
        }
        else if (leido == '\n'){
            fil++;
            col = 1;
            siguienteToken();
            estado = -1;
        }
        switch(estado){
            case 1:
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
                    tok.append(leido);
                    estado = 11;
                    t.lexema = tok.toString();
                }
                //Modificar.
                else if(leido == '/'){
                    /*t.tipo = 7;
                    t.lexema = tok.toString();*/
                    estado = 12;
                }
                else if(leido == '-'){
                    estado = 7;
                }
                else if(leido == 'f'){
                    estado = 13;                    
                }
                else if(leido == 'e'){
                    estado = 25;
                }
                else {
                    t.tipo = 19;
                    t.lexema = tok.toString();
                    estado = 16;
                }
                
            case 7:
                if (estado == 7){
                    b = leerCaracter();
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

            case 12:
                if(estado == 12){
                    char nuevoLeido;
                    b = leerCaracter();
                    leido = (char) b;
                    nuevoLeido = leido;
                    boolean terminado = false;
                    if(leido == '*'){
                        asterisco = true;
                        do{
                            b = leerCaracter();
                            leido = (char) b;
                            if(leido == '\n'){
                                col = 1;
                                fil++;
                            }
                            else{
                                col++;
                            }
                            if(asterisco){
                                if(leido == '*'){
                                }
                                else if (leido == '/'){
                                    terminado = true;
                                    almacenado = 0;
                                    siguienteToken();
                                }
                                else{
                                    asterisco = false;
                                }
                            }
                            else{
                                if(leido == '*'){
                                    asterisco = true;
                                }
                            }
                        }while(b != -1 && !terminado);
                    }
                    if(nuevoLeido != '*'){
                        t.tipo = 7;
                        t.lexema = tok.toString();
                        almacenado = leido;
                        return t;
                    }
                }
            case 13:
                if(estado == 13){

                    b = leerCaracter();
                    leido = (char) b;
                    col++;
                    if(Espacio(leido)){
                        t.tipo = 19;
                        t.lexema = tok.toString();
                        almacenado = leido;
                        return t;
                    }
                    else if(letraDigito(b)){
                        tok.append(leido);
                    }
                    if(leido == 'n'){
                        estado = 14;
                    }
                    else if(leido == 'b'){
                        estado = 18;
                    }
                    else if(leido == 'i'){
                        estado = 23;
                    }
                    else if(letraDigito(b)){
                        estado = 16;
                        t.tipo = 19;
                        t.lexema = tok.toString();
                    }
                    else{
                        estado = 16;
                        t.tipo = 19;
                        t.lexema = tok.toString();
                    }
                }

            case 14:
                if(estado == 14){
                    b = leerCaracter();
                    col++;
                    leido = (char) b;
                    if(letraDigito(b)){
                        tok.append(leido);
                        estado = 16;
                    }
                    else {
                        t.tipo = 8;
                        t.lexema = tok.toString();
                        almacenado = leido;
                        return t;
                    }
                }
            
            case 18:
                if(estado == 18){
                    b = leerCaracter();
                    leido = (char) b;
                    col++;
                    if(Espacio(leido)){
                        t.tipo = 19;
                        t.lexema = tok.toString();
                    }
                    else{
                        tok.append(leido);
                    }
                    if(leido == 'l'){
                        estado = 19;
                    }
                    else if(letraDigito(b)){
                        estado = 16;
                        t.tipo = 19;
                        t.lexema = tok.toString();
                    }
                }
            
            case 19:
                if(estado == 19){
                    b = leerCaracter();
                    leido = (char) b;
                    col++;
                    if(Espacio(leido)){
                        t.tipo = 19;
                        t.lexema = tok.toString();
                    }
                    else{
                        tok.append(leido);
                    }
                    if(leido == 'q'){
                        estado = 20;
                    }
                    else if(letraDigito(b)){
                        estado = 16;
                    }
                }
            case 20:
                if(estado == 20){
                    b = leerCaracter();
                    leido = (char) b;
                    col++;
                    if(letraDigito(b)){
                        estado = 16;
                    }
                    else{
                        t.tipo = 18;
                        t.lexema = tok.toString();
                        almacenado = leido;
                        return t;
                    }
                }
            case 23:
                if(estado == 23){
                    b = leerCaracter();
                    leido = (char) b;
                    col++;
                    if(letraDigito(b)){
                        estado = 16;
                    }
                    else{
                        t.tipo = 15;
                        t.lexema = tok.toString();
                        almacenado = leido;
                        return t;
                    }
                }
            case 25:
                if(estado == 25){
                    b = leerCaracter();
                    leido = (char) b;
                    col++;
                    if(Espacio(leido)){
                        t.tipo = 19;
                        t.lexema = tok.toString();
                    }
                    else{
                        tok.append(leido);
                    }
                    if(leido == 'n'){
                        estado = 26;
                    }
                }
            case 26:
                if(estado == 26){
                    b = leerCaracter();
                    leido = (char) b;
                    col++;
                    if(Espacio(leido)){
                        t.tipo = 19;
                        t.lexema = tok.toString();
                    }
                }
            case 16:
                if(estado == 16){
                    do{
                        b = leerCaracter();
                        leido = (char) b;
                        if(letraDigito(leido)){
                            tok.append(leido);
                        }
                    }while(letraDigito(leido));
                    t.lexema = tok.toString();
                    almacenado = leido;
                }
                

        }
        return t;
    }
}