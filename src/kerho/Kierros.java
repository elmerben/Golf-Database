 package kerho;


import java.io.*;

import fi.jyu.mit.ohj2.Mjonot;

import static kanta.tunnusTarkistus.rand;


public class Kierros {

    private int pelaajaNro;
    private String pvm;
    private int kierrostunnus;
    private int kenttaID;
    private int tulosEtu;
    private int tulosTaka;
    private int tulosYht;

    private static int seuraavaNro = 1;

    
    
    public Kierros() {
        //
    }
    
    public Kierros(int pelaajaNro) {
        this.pelaajaNro = pelaajaNro;
        rekisteroi();
    }
    
    
    private void setTunnusNro(int nr) {
        kierrostunnus = nr;
        if ( kierrostunnus >= seuraavaNro ) seuraavaNro = kierrostunnus + 1;
    }
    

    
    public void parse(String rivi) {
        StringBuffer sb = new StringBuffer(rivi);
        setTunnusNro(Mjonot.erota(sb, '|', getTunnusNro()));
        pelaajaNro = Mjonot.erota(sb, '|', pelaajaNro);
        pvm = Mjonot.erota(sb, '|', pvm);
        kierrostunnus = Mjonot.erota(sb, '|', kierrostunnus);
        kenttaID = Mjonot.erota(sb, '|', kenttaID);
        tulosEtu = Mjonot.erota(sb, '|', tulosEtu);
        tulosTaka = Mjonot.erota(sb, '|', tulosTaka);
        tulosYht = Mjonot.erota(sb, '|', tulosYht);


    }

    
    public void vastaaKierros(int nro) {
        pelaajaNro = nro;
        pvm = (rand(1, 30) + "." + rand(1, 12)+ "." + rand(1900, 2100));
        kierrostunnus = rand(1, 1000);
        kenttaID = rand(1, 1000);
        tulosEtu = rand(20, 50);
        tulosTaka = rand(20, 50);
        tulosYht = tulosEtu + tulosTaka;
    }
    
    
    public int getKenttia() {
        return 5;
    }
    public String PaivaM() {
        String PaivaMaara = (rand(1, 30) + "." + rand(1, 12)+ "." + rand(1900, 2100));
        return PaivaMaara;
    }
    
    public int getKierrosID() {
        int numero = seuraavaNro;
        if ( kierrostunnus >= seuraavaNro ) seuraavaNro = kierrostunnus + 1;
        return numero;

    }
    
    
    public String anna(int k) {
        switch (k) {
        case 0: return "" + getKierrosID();
        case 1: return "" + PaivaM();
        case 2: return "" + kierrostunnus;
        case 3: return Integer.toString(kenttaID);
        case 4: return Integer.toString(tulosYht);
        default: return "Ääliö";

        }
    }
    
    
    @Override
    public String toString() {
        
        StringBuffer sb = new StringBuffer("");
        String erotin = "";
        for (int k = 0; k < getKenttia(); k++) {
            sb.append(erotin);
            sb.append(anna(k));
            erotin = "|";
        }
        return sb.toString();
    }   
   
    public void tulosta(PrintStream out) {
        out.println("Paivamaara: " + pvm + " Tulos yht: " + tulosYht);
    }
    
    public void tulosta(OutputStream os) {
        tulosta(new PrintStream(os));
    }

    public int rekisteroi() {
        kierrostunnus = seuraavaNro;
        seuraavaNro++;
        return kierrostunnus;
    }

    public int getTunnusNro() {
        return kierrostunnus;
    }
    
    public int getJasenNro() {
        return pelaajaNro;
    }



    
    public static void main(String[] args) {
        Kierros rundi = new Kierros();
        rundi.vastaaKierros(2);
        rundi.tulosta(System.out);
    }
    
}
