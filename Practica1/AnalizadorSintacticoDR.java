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
        if(t.tipo == Token.EOF){
            System.err.println("Error sintáctico: encontrado fichero, esperaba  " + posibles.toString());
        }
        System.err.println("Error sintactico (" + t.fila + "," + t.columna +"): encontrado '" + t.lexema + "', esperaba " + posibles.toString());
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
            reglas.append(" 1");
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
            reglas.append(" 2");
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
            reglas.append(" 3");
            Fun();
            Spp();
        }
        else if(t.tipo == Token.LET || t.tipo == Token.PRINT || t.tipo == Token.IF || t.tipo == Token.BLQ || t.tipo == Token.EOF){
            //System.out.println("4 ");
            reglas.append(" 4");
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
            reglas.append(" 5");
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
            reglas.append(" 6");
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
        if(t.tipo == Token.ID){
            //System.out.println("7 ");
            reglas.append(" 7");
            emparejar(Token.ID);
            emparejar(Token.DOSP);
            Type();
            Arg();
        }
        else if(t.tipo == Token.PARD){
            //System.out.println("8 ");
            reglas.append(" 8");
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
            //System.out.println("9 ");
            reglas.append(" 9");
            emparejar(Token.RET);
            Type();
        }
        else if(t.tipo == Token.FN || t.tipo == Token.LET || t.tipo == Token.PRINT ||t.tipo == Token.IF ||t.tipo == Token.BLQ){
            //System.out.println("10 ");
            reglas.append(" 10");
        }
        else{
            ArrayList<Integer> tk = new ArrayList();
            tk.add(Token.FN);
            tk.add(Token.RET);
            tk.add(Token.LET);
            tk.add(Token.PRINT);
            tk.add(Token.IF);
            tk.add(Token.BLQ);
            errorSintaxis(tk);
        }
    }

    public void Type(){
        if(t.tipo == Token.INT){
            //System.out.println("11 ");
            reglas.append(" 11");
            emparejar(Token.INT);
        }
        else if(t.tipo == Token.REAL){
            //System.out.println("12 ");
            reglas.append(" 12");
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
        //System.out.println("13 ");
        reglas.append(" 13");
        I();
        Codp();
    }

    public void Codp(){
        if(t.tipo == Token.PYC){
            //System.out.println("14 ");
            reglas.append(" 14");
            emparejar(Token.PYC);
            I();
            Codp();
        }
        else if (t.tipo == Token.ENDFN || t.tipo == Token.FBLQ){
            //System.out.println("15 ");
            reglas.append(" 15");
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
            //System.out.println("17 ");
            reglas.append(" 17");
            emparejar(Token.LET);
            emparejar(Token.ID);
            IT();
        }
        else if(t.tipo == Token.PRINT){
            //System.out.println("18 ");
            reglas.append(" 18");
            emparejar(Token.PRINT);
            E();
        }

        else if(t.tipo == Token.IF){
            //System.out.println("19 ");
            reglas.append(" 19");
            emparejar(Token.IF);
            E();
            I();
            Ip();
        }
        else if (t.tipo == Token.BLQ){
            //System.out.println("16 ");
            //emparejar(Token.BLQ);
            reglas.append(" 16");
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
            //System.out.println("20 ");
            reglas.append(" 20");
            emparejar(Token.ELSE);
            I();
            emparejar(Token.FI);
        }
        else if(t.tipo == Token.FI){
            //System.out.println("21 ");
            reglas.append(" 21");
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
            //System.out.println("22 ");
            reglas.append(" 22");
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
            //System.out.println("23 ");
            reglas.append(" 23");
            emparejar(Token.DOSP);
            Type();
        }
        else if(t.tipo == Token.ASIG){
            //System.out.println("24 ");
            reglas.append(" 24");
            emparejar(Token.ASIG);
            E();
            Ifa();
        }
        else if(t.tipo == Token.PYC || t.tipo == Token.ENDFN ||t.tipo == Token.FBLQ ||t.tipo == Token.ELSE || t.tipo == Token.FI){
            //System.out.println("25 ");
            reglas.append(" 25");
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
       if(t.tipo == Token.IF){
           //System.out.println("26 ");
           reglas.append(" 26");
           emparejar(Token.IF);
           E();
       } 
       else if(t.tipo == Token.PYC || t.tipo == Token.ENDFN ||t.tipo == Token.FBLQ ||t.tipo == Token.ELSE || t.tipo == Token.FI){
           //System.out.println("27 ");
            reglas.append(" 27");
       }
       else{
           ArrayList<Integer> tk = new ArrayList();
           tk.add(Token.ENDFN);
           tk.add(Token.PYC);
           tk.add(Token.IF);
           tk.add(Token.ELSE);
           tk.add(Token.FI);
           tk.add(Token.FBLQ);
           errorSintaxis(tk);
       }
    }

    public void E(){
        //System.out.println("28 ");
        reglas.append(" 28");
        T();
        Ep();
    }

    public void Ep(){
        if(t.tipo == Token.OPAS){
            //System.out.println("29 ");
            reglas.append(" 29");
            emparejar(Token.OPAS);
            T();
            Ep();
        }
        else if(t.tipo == Token.PYC || t.tipo == Token.ENDFN ||t.tipo == Token.FBLQ ||t.tipo == Token.ELSE || t.tipo == Token.FI || t.tipo == Token.BLQ || t.tipo == Token.LET || t.tipo == Token.PRINT || t.tipo == Token.IF ||  t.tipo == Token.PARD){
            //System.out.println("30 ");
            reglas.append(" 30" );
        }
        else{
            ArrayList<Integer> tk = new ArrayList();
           tk.add(Token.ENDFN);
           tk.add(Token.PARD);
           tk.add(Token.PYC);
           tk.add(Token.LET);
           tk.add(Token.PRINT);
           tk.add(Token.IF);
           tk.add(Token.ELSE);
           tk.add(Token.FI);
           tk.add(Token.BLQ);
           tk.add(Token.FBLQ);
           tk.add(Token.OPAS);
           errorSintaxis(tk);
        }
    }

    public void T(){
        //System.out.println("31 ");
        reglas.append(" 31");
        F();
        Tp();
    }

    public void Tp(){
        if(t.tipo == Token.OPMD){
            //System.out.println("32 ");
            reglas.append(" 32");
            emparejar(Token.OPMD);
            F();
            Tp();
        }
        else if(t.tipo == Token.OPAS || t.tipo == Token.PYC || t.tipo == Token.ENDFN ||t.tipo == Token.FBLQ ||t.tipo == Token.ELSE || t.tipo == Token.FI || t.tipo == Token.BLQ || t.tipo == Token.LET || t.tipo == Token.PRINT || t.tipo == Token.IF ||  t.tipo == Token.PARD){
            //System.out.println("33 ");
            reglas.append(" 33");
        }
        else{
            ArrayList<Integer> tk = new ArrayList();
           tk.add(Token.ENDFN);
           tk.add(Token.PARD);
           tk.add(Token.PYC);
           tk.add(Token.LET);
           tk.add(Token.PRINT);
           tk.add(Token.IF);
           tk.add(Token.ELSE);
           tk.add(Token.FI);
           tk.add(Token.BLQ);
           tk.add(Token.FBLQ);
           tk.add(Token.OPAS);
           tk.add(Token.OPMD);
           errorSintaxis(tk);
        }
    }

    public void F(){
        if(t.tipo == Token.NUMINT){
            //System.out.println("34 ");
            reglas.append(" 34");
            emparejar(Token.NUMINT);
        }
        else if(t.tipo ==  Token.NUMREAL){
            //System.out.println("35 ");
            reglas.append(" 35");
            emparejar(Token.NUMREAL);
        }
        else if(t.tipo == Token.ID){
            //System.out.println("36 ");
            reglas.append(" 36");
            emparejar(Token.ID);
        }
        else if(t.tipo == Token.PARI){
            //System.out.println("37 ");
            reglas.append(" 37");
            emparejar(Token.PARI);
            E();
            emparejar(Token.PARD);
        }
        else {
           ArrayList<Integer> tk = new ArrayList();
           tk.add(Token.ID);
           tk.add(Token.PARI);
           tk.add(Token.NUMINT);
           tk.add(Token.NUMREAL);
           errorSintaxis(tk);
        }
    }
    
}