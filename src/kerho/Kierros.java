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
    private String kenttaLyhenne;

    private static int seuraavaNro = 1;

    
    
    public void setPelaajaNro(int pelaajaNro) {
        this.pelaajaNro = pelaajaNro;
    }
    
    public void setPvm(String pvm) {
        this.pvm = pvm;
    }

    public void setTulosEtu(int tulosEtu) {
        this.tulosEtu = tulosEtu;
        laskeYhteistulos();
    }

    public void setTulosTaka(int tulosTaka) {
        this.tulosTaka = tulosTaka;
        laskeYhteistulos();
    }

    public void setTulosYht(int tulosYht) {
        this.tulosYht = tulosYht;
    }
    
    private void laskeYhteistulos() {
        this.tulosYht = this.tulosEtu + this.tulosTaka;
    }
    
    
    public void setKenttaLyhenne(String kenttaLyhenne) {
        if (kenttaLyhenne.length() <= 6) {
            this.kenttaLyhenne = kenttaLyhenne;
        } else {
            this.kenttaLyhenne = kenttaLyhenne.substring(0, 6);
        }
    }
    
    
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
        pvm = Mjonot.erota(sb, '|', pvm);
        kenttaLyhenne = Mjonot.erota(sb, '|', kenttaLyhenne);
        tulosEtu = Mjonot.erota(sb, '|', tulosEtu);
        tulosTaka = Mjonot.erota(sb, '|', tulosTaka);
        tulosYht = Mjonot.erota(sb, '|', tulosYht);
        pelaajaNro = Mjonot.erota(sb, '|', pelaajaNro);
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
        return  pvm + "|" + kenttaLyhenne + "|" + tulosEtu + "|" + tulosTaka + "|" + tulosYht + "|" + pelaajaNro ;
    }
    

    public void tulosta(PrintStream out) {
//        out.println("Paivamaara: " + pvm + " Tulos yht: " + tulosYht);
        out.println("Päivämäärä: " + pvm);
        out.println("Kenttä: " + kenttaLyhenne); // Olettaen, että tämä on haluttu kentän nimi
        out.println("Tulos etu: " + tulosEtu);
        out.println("Tulos taka: " + tulosTaka);
        out.println("Tulos yhteensä: " + tulosYht); 
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
        rundi.rekisteroi();
//        rundi.vastaaKierros(2);
        rundi.tulosta(System.out);
    }
    
}
