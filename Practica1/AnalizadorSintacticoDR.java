import java.io.RandomAccessFile;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

class AnalizadorSintacticoDR{
    AnalizadorLexico al;
    Token t = new Token();
    StringBuilder reglas = new StringBuilder();
    public AnalizadorSintacticoDR(AnalizadorLexico alt){
        al = alt;
        t = al.siguienteToken();
    }

    public void comprobarFinFichero(){
        System.out.println(reglas.toString());
    }

    public  void errorSintaxis(ArrayList<Integer> tk){
        StringBuilder posibles = new StringBuilder();
        Token p = new Token();
        for(int i = 0; i<tk.size(); i++){
            p.tipo = tk.get(i);  
            posibles.append(p.toString());
            posibles.append(" ");
        }
        System.out.println("Error sintactico (" + t.fila + "," + t.columna +"): encontrado " + t.lexema + ", esperaba " + posibles.toString());
        System.exit(-1);
    }

    public final void emparejar(int tokEsperado){
        if(t.tipo == tokEsperado){
            t = al.siguienteToken();
        }
        else{
            ArrayList<Integer> tk = new ArrayList();
            tk.add(tokEsperado);
            errorSintaxis(tk);
        }
    }

    public void S(){
        if(t.tipo == Token.FN){
            //System.out.println("1 ");
            reglas.append("1 ");
            Sp();
        }
        else{
            ArrayList<Integer> tk = new ArrayList();
            tk.add(Token.FN);
            errorSintaxis(tk);
        }
    }

    public void Sp(){
        if(t.tipo == Token.FN){
            //System.out.println("2 ");
            reglas.append("2 ");
            Fun();
            Spp();
        }
        else{
            ArrayList<Integer> tk = new ArrayList();
            tk.add(Token.FN);
            errorSintaxis(tk);
        }
    }

    public void Spp(){
        if(t.tipo == Token.FN){
            //System.out.println("3 ");
            reglas.append("3 ");
            Fun();
            Spp();
        }
        else if(t.tipo == Token.LET || t.tipo == Token.PRINT || t.tipo == Token.IF || t.tipo == Token.BLQ){
            reglas.append("4 ");
        }
        else{
            ArrayList<Integer> tk = new ArrayList();
            tk.add(Token.FN);
            tk.add(Token.LET);
            tk.add(Token.PRINT);
            tk.add(Token.IF);
            tk.add(Token.BLQ);
            errorSintaxis(tk);
        }
    }

    public void Fun(){
        if(t.tipo == Token.FN){
            //System.out.println("5 ");
            reglas.append("5 ");
            emparejar(Token.FN);
            emparejar(Token.ID);
            A();
            Rt();
            Spp();
            Cod();
            emparejar(Token.ENDFN);
        }
        else{
            ArrayList<Integer> tk = new ArrayList();
            tk.add(Token.FN);
            errorSintaxis(tk);
        }
    }

    public void A(){
        if(t.tipo == Token.PARI){
            //System.out.println("6 ");
            reglas.append("6 ");
            emparejar(Token.PARI);
            Arg();
            emparejar(Token.PARD);
        }
        else{
            ArrayList<Integer> tk = new ArrayList();
            tk.add(Token.PARI);
            errorSintaxis(tk);
        }
    }

    public void Arg(){
        //System.out.println(t.tipo);
        if(t.tipo == Token.ID){
            reglas.append("7 ");
            //System.out.println("8 ");
            emparejar(Token.ID);
            emparejar(Token.DOSP);
            Type();
            Arg();
        }
        else if(t.tipo == Token.PARD){
            reglas.append("8 ")
        }
        else{
            ArrayList<Integer> tk = new ArrayList();
            tk.add(Token.ID);
            tk.add(Token.PARD);
            errorSintaxis(tk);
        }
    }

    public void Rt(){
        if(t.tipo == Token.RET){
            reglas.append("9 ");
            emparejar(Token.RET);
            Type();
        }
        else if(t.tipo == Token.FN || t.tipo == Token.LET || t.tipo == Token.PRINT ||t.tipo == Token.IF ||t.tipo == Token.BLQ){
            reglas.append("10 ")
        }
        else{
            ArrayList<Integer> tk = new ArrayList();
            tk.add(Token.RET);
            tk.add(Token.FN);
            tk.add(Token.LET);
            tk.add(Token.PRINT);
            tk.add(Token.IF);
            tk.add(Token.BLQ);
            errorSintaxis(tk);
        }
    }

    public void Type(){
        if(t.tipo == Token.INT){
            reglas.append("11 ");
            emparejar(Token.INT);
        }
        else if(t.tipo == Token.REAL){
            reglas.append("12 ");
            emparejar(Token.REAL);
        }
        else{
            ArrayList<Integer> tk = new ArrayList();
            tk.add(Token.INT);
            tk.add(Token.REAL);
            errorSintaxis(tk);
        }
    }

    public void Cod(){
        reglas.append("13 ");
        I();
        Codp();
    }

    public void Codp(){
        if(t.tipo == Token.PYC){
            reglas.append("14 ");
            emparejar(Token.PYC);
            I();
            Codp();
        }
        else if (t.tipo == Token.ENDFN || t.tipo == Token.FBLQ){
            reglas.append("15 ");
        }
        else{
            ArrayList<Integer> tk = new ArrayList();
            tk.add(Token.ENDFN);
            tk.add(Token.PYC);
            tk.add(Token.FBLQ);
            errorSintaxis(tk);
        }
    }

    public void I(){
        if(t.tipo == Token.LET){
            reglas.append("17 ");
        }
        else if(t.tipo == Token.PRINT){
            reglas.append("18 ");
        }

        else if(t.tipo == Token.IF){
            reglas.append("19 ");
        }
        else if (t.tipo == Token.BLQ){
            reglas.append("16 ");
            Blq();
        }
        else{
            ArrayList<Integer> tk = new ArrayList();
            tk.add(Token.LET);
            tk.add(Token.PRINT);
            tk.add(Token.IF);
            tk.add(Token.BLQ);
            errorSintaxis(tk);
        }
    }

    public void Ip(){
        if(t.tipo == Token.ELSE){
            reglas.append("20 ");
            emparejar(Token.ELSE);
            I();
            emparejar(Token.FI);
        }
        else if(t.tipo == Token.FI){
            reglas.append("21 ");
            emparejar(Token.FI);
        }
        else{
            ArrayList<Integer> tk = new ArrayList();
            tk.add(Token.ELSE);
            tk.add(Token.FI);
            errorSintaxis(tk);
        }
    }

    public void Blq(){
        if(t.tipo == Token.BLQ){
            reglas.append("22 ");
            emparejar(Token.BLQ);
            Cod();
            emparejar(Token.FBLQ);
        }
        else{
            ArrayList<Integer> tk = new ArrayList();
            tk.add(Token.BLQ);
            errorSintaxis(tk);
        }
    }

    public void IT(){
        if(t.tipo == Token.DOSP){
            reglas.append("23 ");
            Type();
        }
        else if(t.tipo == Token.ASIG){
            reglas.append("24 ");
            emparejar(Token.ASIG);
            E();
            Ifa();
        }
        else if(t.tipo == Token.PYC || t.tipo == Token.ENDFN ||t.tipo == Token.FBLQ ||t.tipo == Token.ELSE || t.tipo == Token.FI){
            reglas.append("25 ");
        }
        else{
            ArrayList<Integer> tk = new ArrayList();
            tk.add(Token.ENDFN);
            tk.add(Token.DOSP);
            tk.add(Token.PYC);
            tk.add(Token.ELSE);
            tk.add(Token.FI);
            tk.add(Token.FBLQ);
            tk.add(Token.ASIG);
            errorSintaxis(tk);
        }
    }

    public void Ifa(){

    }

    public void E(){

    }

    public void Ep(){

    }

    public void T(){

    }

    public void Tp(){

    }

    public void F(){

    }
    
}