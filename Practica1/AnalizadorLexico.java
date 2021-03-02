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
    char ultimo;        
    int ultcol;
    boolean ejecutado = false;

    public AnalizadorLexico(RandomAccessFile file){
        fil = 1;
        col = 1;
        bytes = 0;
        archivo = file;
        t = new Token();
        almacenado = 0;
        ultimo = 0;
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

    public boolean letraDigito(char car){
        if((car >= 48 && car <= 57) || (car >= 65 && car<=90) || (car >= 97 && car <= 122)){
            return true;
        }
        return false;
    }

    public boolean esNumero(char num){
        if(num >= 48 && num<= 57){
            return true;
        }
        return false;
    }

    public boolean esLetra(char let){
        if((let >= 65 && let <=90) || (let >= 97 && let <= 122)){
            return true;
        }
        else
            return false;
    }


    public Token siguienteToken(){
        int estado = 1;
        int b =0;
        StringBuilder tok = new StringBuilder();
        char leido = '\n';
        boolean asterisco = false;
        boolean alm = false;
        t = new Token();
        String p = new String();
        t.fila = fil;
         t.columna = col;
        if(almacenado != 0){
            leido = almacenado;
            //ystem.out.println(leido);
            almacenado = 0;
            
        }
        else{
            b = leerCaracter();
            if (b == -1){
                t.tipo = 22;
                 
            }
            leido = (char) b;
            almacenado = 0;
        }
        
        if(leido == ' ' || leido == '\t'){
            col++;
            almacenado = 0;
            estado = -1;
            siguienteToken();
        }
        else if (leido == '\n'){
            almacenado = 0;
            fil++;
            col = 1;
            estado = -1;
            siguienteToken();
        }
        else {
            ejecutado = false;
        }
        if(b == -1){
            t.tipo = 22;
            return t;
        }
        switch(estado){
            case -1:
                break;
            case 1:
                tok.append(leido);
                if(Espacio(leido)){
                    break;
                }
                else if(leido == '(' ){
                    t.tipo = 0;
                    estado = 2;
                    t.lexema = tok.toString();
                    //ultimo = '(';
                }
                else if(leido == ')'){
                    t.tipo = 1;
                    estado = 3;
                    t.lexema = tok.toString();
                }
                else if(leido == ':'){
                    t.tipo = 2;
                    estado = 4;
                    t.lexema = tok.toString();
                }
                else if(leido == '='){
                    t.tipo = 4;
                    estado = 5;
                    t.lexema = tok.toString();
                }
                else if(leido == ';'){
                    t.tipo = 5;
                    estado = 6;
                    t.lexema = tok.toString();
                }
                else if(leido == '+'){
                    t.tipo = 6;
                    t.lexema = tok.toString();
                }
                else if(leido == '*'){
                    t.tipo = 7;
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
                else if(leido == 'i'){
                    estado = 31;
                }
                else if(leido == 'r'){
                    estado = 37;
                }
                else if(leido == 'b'){
                    estado = 42;
                }
                else if(leido == 'l'){
                    estado = 46;
                }
                else if(leido == 'p'){
                    estado = 54;
                }
                else if(esNumero(leido)){
                    estado = 60;
                }
                else if(esLetra(leido)){
                    //tok.append(leido);
                    estado = 16;
                }
                else {
                    System.err.println("Error lexico (" + fil + "," + col + "): caracter '" + leido+"' incorrecto");
                    t.tipo = 22;
                    return t;
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
                        almacenado = leido;
                                       
                    } 
                }

            case 12:
                if(estado == 12){
                    char nuevoLeido;
                    StringBuilder comentario = new StringBuilder();
                    comentario.append(leido);
                    b = leerCaracter();
                    leido = (char) b;
                    nuevoLeido = leido;
                    comentario.append(leido);
                    boolean terminado = false;
                    if(leido == '*'){
                        asterisco = true;
                        do{
                            b = leerCaracter();
                            leido = (char) b;
                            comentario.append(leido);
                            if(leido== '\n'){
                                comentario.setLength(0);
                                col = 1;
                                fil++;
                            }
                            /*else{
                                
                            }*/
                            if(asterisco){
                                if(leido == '*'){
                                }
                                else if (leido == '/'){
                                    col += comentario.length();
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
                        if(b == -1){
                            System.err.println("Error lexico: fin de fichero inseperado");
                            t.tipo = 22;
                            return t;
                        }
                    }
                    if(nuevoLeido != '*'){
                        t.tipo = 7;
                        t.lexema = tok.toString();
                        almacenado = leido;
                        
                    }
                }
            case 13:
                if(estado == 13){
                    b = leerCaracter();
                    leido = (char) b;
                    
                    if(Espacio(leido)){
                        t.tipo = 19;
                        t.lexema = tok.toString();
                        almacenado = leido;
                        
                    }
                    else if(letraDigito(leido)){
                        tok.append(leido);
                    }

                    else{
                        t.tipo = 19;
                        t.lexema = tok.toString();
                        almacenado = leido;
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
                    else if(letraDigito(leido)){
                        estado = 16;
                        t.tipo = 19;
                        t.lexema = tok.toString();
                    }
                }

            case 14:
                if(estado == 14){
                    b = leerCaracter();
                    
                    leido = (char) b;
                    if(letraDigito(leido)){
                        tok.append(leido);
                        estado = 16;
                    }
                    else {
                        t.tipo = 8;
                        t.lexema = tok.toString();
                        almacenado = leido;
                        
                    }
                }
            
            case 18:
                if(estado == 18){
                    b = leerCaracter();
                    leido = (char) b;
                    
                    if(Espacio(leido)){
                        t.tipo = 19;
                        t.lexema = tok.toString();
                        almacenado = leido;
                    }
                    else if(letraDigito(leido)){
                        tok.append(leido);
                    }
                    else{
                        t.tipo = 19;
                        t.lexema = tok.toString();
                        almacenado = leido;
                    }
                    if(leido == 'l'){
                        estado = 19;
                    }
                    else{
                        estado = 16;
                        t.tipo = 19;
                        t.lexema = tok.toString();
                    }
                }
            
            case 19:
                if(estado == 19){
                    b = leerCaracter();
                    leido = (char) b;
                    
                    if(Espacio(leido)){
                        t.tipo = 19;
                        t.lexema = tok.toString();
                        almacenado = leido;
                    }
                    else if(letraDigito(leido)){
                        tok.append(leido);
                    }
                    else{
                        t.tipo = 19;
                        t.lexema = tok.toString();
                    }
                    if(leido == 'q'){
                        estado = 20;
                    }
                    else if(letraDigito(leido)){
                        estado = 16;
                    }
                }
            case 20:
                if(estado == 20){
                    b = leerCaracter();
                    leido = (char) b;
                    
                    if(letraDigito(leido)){
                        estado = 16;
                        t.tipo = 19;
                        tok.append(leido);
                    }
                    else {
                        t.tipo = 18;
                        almacenado = leido;
                        t.lexema = tok.toString();
                        
                        
                    }
                }
            case 23:
                if(estado == 23){
                    b = leerCaracter();
                    leido = (char) b;
                    
                    if(letraDigito(leido)){
                        estado = 16;
                        tok.append(leido);
                        almacenado = leido;
                    }
                    else{
                        t.tipo = 15;
                        t.lexema = tok.toString();
                        almacenado = leido;
                        
                        
                    }
                }
            case 25:
                if(estado == 25){
                    b = leerCaracter();
                    leido = (char) b;
                    
                    if(Espacio(leido)){
                        t.tipo = 19;
                        t.lexema = tok.toString();
                        almacenado = leido;
                    }
                    else if (letraDigito(leido)){
                        tok.append(leido);
                    }
                    else {
                        t.tipo = 19;
                        almacenado = leido;
                    }
                    if(leido == 'n'){
                        estado = 26;
                    }
                    else if(leido == 'l'){
                        estado = 50;
                    }
                    else {
                        estado = 16;
                        t.tipo = 19;
                    }
                }
            case 26:
                if(estado == 26){
                    b = leerCaracter();
                    leido = (char) b;
                    
                    if(Espacio(leido)){
                        t.tipo = 19;
                        t.lexema = tok.toString();
                        almacenado =  leido;
                    }
                    else if(letraDigito(leido)){
                        tok.append(leido);
                    }
                    else {
                        t.tipo = 19;
                        almacenado = leido;
                    }
                    if(leido == 'd'){
                        estado = 27;
                    }
                    else {
                        estado = 16;
                    }
                }
            case 27:
                if(estado == 27){
                    b = leerCaracter();
                    leido = (char) b;
                    
                    if(Espacio(leido)){
                        t.tipo = 19;
                        t.lexema = tok.toString();
                        almacenado = leido;
                    }
                    else if(letraDigito(leido)){
                        tok.append(leido);
                    }
                    else{
                        almacenado = leido;
                        t.lexema = tok.toString();
                    }
                    if(leido == 'f'){
                        estado = 28;
                    }
                    else {
                        estado = 16;
                    }
                }
            case 28:
                if(estado == 28){
                    b = leerCaracter();
                    leido = (char) b;
                    
                    if(Espacio(leido)){
                        t.tipo = 19;
                        t.lexema = tok.toString();
                        almacenado = leido;
                    }
                    else if(letraDigito(leido)){
                        tok.append(leido);
                    }
                    else{
                        t.tipo = 19;
                        almacenado = leido;
                        t.lexema = tok.toString();
                    }
                    if(leido == 'n'){
                        estado = 29;
                    }
                    else{
                        t.tipo = 19;
                        estado = 16;
                    }
                }

            case 29:
                if(estado == 29){
                    b = leerCaracter();
                    leido = (char) b;
                    
                    if(letraDigito(leido)){
                        tok.append(leido);
                        t.tipo = 19;
                        estado = 16;
                    }
                    else{
                        t.tipo = 9;
                        almacenado = leido;
                        t.lexema = tok.toString();
                        
                    }
                }

            case 50:
                if(estado == 50){
                    b = leerCaracter();
                    leido = (char) b;
                    
                    if(Espacio(leido)){
                       t.tipo = 19;
                       t.lexema = tok.toString(); 
                       almacenado = leido;
                    }
                    else if(letraDigito(leido)){
                        tok.append(leido);
                    }
                    else{
                        t.tipo = 19;
                        almacenado = leido;
                        t.lexema = tok.toString();
                    }
                    if(leido == 's'){
                        estado = 51;
                    }
                    else if(letraDigito(leido)){
                        estado = 16;
                    }

                }
            case 51:
                if(estado == 51){
                    b= leerCaracter();
                    leido = (char) b;
                    
                    if(Espacio(leido)){
                        t.tipo = 19;
                        t.lexema = tok.toString();
                        almacenado = leido;
                    }
                    else if(letraDigito(leido)){
                        tok.append(leido);
                    }
                    else {
                        t.tipo = 19;
                        almacenado = leido;
                        t.lexema = tok.toString();
                    }
                    if(leido == 'e'){
                        estado = 52;
                    }
                    else if(letraDigito(leido)){
                        estado = 16;
                        t.tipo = 19;
                    }
                }
            
            case 52:
                if(estado == 52){
                    b = leerCaracter();
                    
                    leido = (char) b;
                    if(letraDigito(leido)){
                        tok.append(leido);
                        estado = 16;
                    }
                    else {
                        t.tipo = 14;
                        t.lexema = tok.toString();
                        almacenado = leido;
                        
                    }
                }

            case 31:
                if(estado == 31){
                    b = leerCaracter();
                    leido = (char) b;
                    
                    if(Espacio(leido)){
                        t.tipo = 19;
                        t.lexema = tok.toString();
                        almacenado = leido;
                    }
                    else if(letraDigito(leido)){
                        tok.append(leido);
                    }
                    else{
                        t.tipo = 19;
                        almacenado = leido;
                        t.lexema = tok.toString();
                    }
                    if(leido == 'n'){
                        estado = 32;
                    }
                    else if(leido == 'f'){
                        estado = 35;
                    }
                    else if(letraDigito(leido)){
                        estado = 16;
                        t.tipo = 19;
                    }
                    
                }

            case 32:
                if(estado == 32){
                    b = leerCaracter();
                    leido = (char) b;
                    
                    if(Espacio(leido)){
                        t.tipo = 19;
                        t.lexema = tok.toString();
                        almacenado = leido;
                    }
                    else if (letraDigito(leido)){
                        tok.append(leido);
                    }
                    else {
                        t.tipo = 19;
                        almacenado = leido;
                        t.lexema = tok.toString();
                    }
                    if(leido == 't'){
                        estado = 33;
                    }
                    else {
                        estado = 16;
                        t.tipo = 19;
                    }
                }
            case 33:
                if(estado == 33){
                    b = leerCaracter();
                    leido = (char) b;
                    
                    if(letraDigito(leido)){
                        tok.append(leido);
                        estado = 16;
                        t.tipo = 19;
                    }
                    else {
                        t.tipo = 10;
                        almacenado = leido;
                        t.lexema = tok.toString();
                        
                        
                    }
                }
            
            case 35:
                if(estado == 35){
                    b = leerCaracter();
                    leido = (char) b;
                    
                    if(letraDigito(leido)){
                        tok.append(leido);
                        estado = 16;
                        t.tipo = 19;
                    }
                    else{
                        t.tipo = 13;
                        almacenado = leido;
                        t.lexema = tok.toString();
                        
                    }
                }

            case 37:
                if(estado == 37){
                    b = leerCaracter();
                    leido = (char) b;
                    
                    if(Espacio(leido)){
                        t.tipo = 19;
                        t.lexema = tok.toString();
                        almacenado = leido;
                    }
                    else if(letraDigito(leido)){
                        tok.append(leido);
                    }
                    else {
                        t.tipo = 19;
                        t.lexema = tok.toString();
                        almacenado = leido;
                    }
                    if(leido == 'e'){
                        estado = 38;
                    }
                    else {
                        t.tipo = 19;
                        estado = 16;
                    }
                }
            
            case 38:
                if(estado == 38){
                    b = leerCaracter();
                    leido = (char) b;
                    
                    if(Espacio(leido)){
                        t.tipo = 19;
                        t.lexema = tok.toString();
                        almacenado = leido;
                    }
                    else if(letraDigito(leido)){
                        tok.append(leido);
                    }
                    else{
                        t.tipo = 19;
                        t.lexema = tok.toString();
                        almacenado = leido;
                    }
                    if(leido == 'a'){
                        estado = 39;
                    }
                    else{
                        estado = 16;
                        t.tipo = 19;
                    }
                }

            case 39:
                if(estado == 39){
                    b = leerCaracter();
                    leido = (char) b;
                    
                    if(Espacio(leido)){
                        t.tipo = 19;
                        t.lexema = tok.toString();
                        almacenado = leido;
                    }
                    else if(letraDigito(leido)){
                        tok.append(leido);
                    }
                    else{
                        t.tipo = 19;
                        t.lexema = tok.toString();
                        almacenado = leido;
                    }
                    if(leido == 'l'){
                        estado = 40;
                    }
                    else{
                        estado = 16;
                    }
                }
            case 40:
                if(estado == 40){
                    b = leerCaracter();
                    leido = (char) b;
                    
                    if(letraDigito(leido)){
                        estado = 16;
                        t.tipo = 19;
                        tok.append(leido);
                    }
                    else {
                        t.tipo = 11;
                        almacenado = leido;
                        t.lexema = tok.toString();
                        
                        
                    }
                }

            case 42:
                if(estado == 42){
                    b = leerCaracter();
                    leido = (char) b;
                    
                    if(Espacio(leido)){
                        t.tipo = 19;
                        t.lexema = tok.toString();
                        almacenado = leido;
                    }
                    else if(letraDigito(leido)){
                        tok.append(leido);
                    }
                    else{
                        t.tipo = 19;
                        t.lexema = tok.toString();
                        almacenado = leido;
                        

                    }
                    if(leido == 'l'){
                        estado = 43;
                    }
                    else if(letraDigito(leido)){
                        estado = 16;
                    }
                }
            case 43:
                if(estado == 43){
                    b = leerCaracter();
                    leido = (char) b;
                    
                    if(Espacio(leido)){
                        t.tipo = 19;
                        t.lexema = tok.toString();
                        almacenado = leido;
                    }
                    else if(letraDigito(leido)){
                        tok.append(leido);
                    }
                    else{
                        t.tipo = 19;
                        t.lexema = tok.toString();
                        almacenado = leido;
                    }
                    if(leido == 'q'){
                        estado = 44;
                    }
                    else if(letraDigito(leido)){
                        estado = 16;
                    }
                }
            case 44:
                if(estado == 44){
                    b = leerCaracter();
                    leido = (char) b;
                    
                    if(letraDigito(leido)){
                        estado = 16;
                    }
                    else{
                        t.tipo = 17;
                        t.lexema = tok.toString();
                        almacenado = leido;
                        
                    }
                }
            case 46:
                if(estado == 46){
                    b = leerCaracter();
                    leido = (char) b;
                    
                    if(Espacio(leido)){
                        t.tipo = 19;
                        t.lexema = tok.toString();
                        almacenado = leido;
                    }
                    else if(letraDigito(leido)){
                        tok.append(leido);
                    }
                    else{
                        t.tipo = 19;
                        t.lexema = tok.toString();
                        almacenado = leido;
                    }
                    if(leido == 'e'){
                        estado = 47;
                    }
                    else if(letraDigito(leido)){
                        estado = 16;
                    }
                }
            case 47:
                if(estado == 47){
                    b = leerCaracter();
                    leido = (char) b;
                    
                    if(Espacio(leido)){
                        t.tipo = 19;
                        t.lexema = tok.toString();
                        almacenado = leido;
                    }
                    else if(letraDigito(leido)){
                        tok.append(leido);
                    }
                    else{
                        t.tipo = 19;
                        t.lexema = tok.toString();
                        almacenado = leido;
                    }
                    if(leido == 't'){
                        estado = 48;
                    }
                    else if(letraDigito(leido)){
                        estado = 16;
                    }
                }

            case 48:
                if(estado == 48){
                    b = leerCaracter();
                    leido = (char) b;
                    
                    if(letraDigito(leido)){
                        estado = 16;
                    }
                    else{
                        t.tipo = 12;
                        t.lexema = tok.toString();
                        almacenado = leido;
                        
                    }
                }

            case 54:
                if(estado == 54){
                    b = leerCaracter();
                    leido = (char) b;
                    
                    if(Espacio(leido)){
                        t.tipo = 19;
                        t.lexema = tok.toString();
                        almacenado = leido;
                    }
                    else if(letraDigito(leido)){
                        tok.append(leido);
                    }
                    else{
                        t.tipo = 19;
                        t.lexema = tok.toString();
                        almacenado = leido;
                    }
                    if(leido == 'r'){
                        estado = 55;
                    }
                    else if(letraDigito(leido)){
                        estado = 16;
                    }
                }
            case 55:
                if(estado == 55){
                    b = leerCaracter();
                    leido = (char) b;
                    
                    if(Espacio(leido)){
                        t.tipo = 19;
                        t.lexema = tok.toString();
                        almacenado = leido;
                    }
                    else if(letraDigito(leido)){
                        tok.append(leido);
                    }
                    else{
                        t.tipo = 19;
                        t.lexema = tok.toString();
                        almacenado = leido;
                    }
                    if(leido == 'i'){
                        estado = 56;
                    }
                    else if(letraDigito(leido)){
                        estado = 16;
                    }
                }

            case 56:
                if(estado == 56){
                    b = leerCaracter();
                    leido = (char) b;
                    
                    if(Espacio(leido)){
                        t.tipo = 19;
                        t.lexema = tok.toString();
                        almacenado = leido;
                    }
                    else if(letraDigito(leido)){
                        tok.append(leido);
                    }
                    else{
                        t.tipo = 19;
                        t.lexema = tok.toString();
                        almacenado = leido;
                    }
                    if(leido == 'n'){
                        estado = 57;
                    }
                    else if(letraDigito(leido)){
                        estado = 16;
                    }
                }
            case 57:
                if(estado == 57){
                    b = leerCaracter();
                    leido = (char) b;
                    
                    if(Espacio(leido)){
                        t.tipo = 19;
                        t.lexema = tok.toString();
                        almacenado = leido;
                    }
                    else if(letraDigito(leido)){
                        tok.append(leido);
                    }
                    else{
                        t.tipo = 19;
                        t.lexema = tok.toString();
                        almacenado = leido;
                    }
                    if(leido == 't'){
                        estado = 58;
                    }
                    else if(letraDigito(leido)){
                        estado = 16;
                    }
                }

            case 58:
                if(estado == 58){
                    b = leerCaracter();
                    leido = (char) b;
                    
                    if(letraDigito(leido)){
                        estado = 16;
                        t.tipo = 19;
                        tok.append(leido);
                    }
                    else {
                        t.tipo = 16;
                        almacenado = leido;
                        t.lexema = tok.toString();
                    }
                }

            case 60:
                if(estado == 60){
                    b = leerCaracter();
                    leido = (char) b;
                    
                    while(esNumero(leido)){
                        tok.append(leido);
                        b = leerCaracter();
                        leido = (char) b;
                        
                    }
                    //t.lexema=tok.toString();
                    if(leido == '.'){
                        tok.append(leido);
                        estado = 61;
                    }

                    else if(!esLetra(leido)){
                        t.tipo = 20;
                        t.lexema = tok.toString();
                        almacenado = leido;
                        
                    }
                    else{
                        System.err.println("Error lexico (" + fil + "," + col + "): caracter '" + leido+"' incorrecto");
                        t.tipo = 22;
                        return t;
                    }
                }
            case 61:
                if(estado == 61){
                    b = leerCaracter();
                    leido =(char) b;
                    
                    while(esNumero(leido)){
                        tok.append(leido);
                        b = leerCaracter();
                        leido = (char)b;
                    }
                    if(esLetra(leido)){
                        System.err.println("Error lexico (" + fil + "," + col + "): caracter '" + leido+"' incorrecto");
                        t.tipo = 22;
                        return t;
                    }

                    else{
                        t.lexema = tok.toString();
                        t.tipo =21;
                        almacenado = leido;
                        
                    }
                }
            case 16:
                if(estado == 16){
                    if(letraDigito(leido)){
                        do{
                            if(leido != ' ' && leido != '\t'){
                                b = leerCaracter();
                                leido = (char) b; 
                                if(letraDigito(leido) || Espacio(leido)){
                                    if(!Espacio(leido))
                                        tok.append(leido);
                                    
                                }
                                else{
                                    almacenado = leido;
                                }
                            }
                            /*else{
                                
                            }*/
                        }while(letraDigito(leido));
                    }
                    t.tipo = 19;
                    t.lexema = tok.toString();
                    almacenado = leido;
                }
        }
        if(!ejecutado){
            for(int i = 0; i<tok.length(); i++){
                if(!Espacio(tok.charAt(i))){
                    p += tok.charAt(i);
                }
            }
            col += p.length();
            ejecutado = true;
        }   
        return t;
        
    }
}