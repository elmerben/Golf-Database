 package kerho;


import java.io.*;

import fi.jyu.mit.ohj2.Mjonot;


/**
 * @author elias
 * @version 11.12.2023
 * Kierros-luokka.
 */
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

    
    
    /**
     * @param pelaajaNro pelaajan pelaajanumero.
     */
    public void setPelaajaNro(int pelaajaNro) {
        this.pelaajaNro = pelaajaNro;
    }
    
    /**
     * @param pvm Asettaa päivämäärän.
     */
    public void setPvm(String pvm) {
        this.pvm = pvm;
    }

    /**
     * @return Palauttaa kierroksen päivämäärän merkkijonona.
     */
    public String getPvm() {
        return this.pvm;
    }

    /**
     * @param tulosEtu etuysin tulos.
     */
    public void setTulosEtu(int tulosEtu) {
        this.tulosEtu = tulosEtu;
        laskeYhteistulos();
    }

    /**
     * @param tulosTaka takaysin tulos.
     */
    public void setTulosTaka(int tulosTaka) {
        this.tulosTaka = tulosTaka;
        laskeYhteistulos();
    }

    /**
     * @param tulosYht lyöntien yhteistulos.
     */
    public void setTulosYht(int tulosYht) {
        this.tulosYht = tulosYht;
    }
    
    /**
     * @return palauttaa yhteistuloksen.
     */
    public int getTulosYht() {
        return this.tulosYht;
    }
    
    /*
     * Laskee etuysin ja takaysin tulokset yhteen kokonaistulokseksi.
     */
    /**
     * Testaa yhteistuloksen laskentaa etu- ja takaysin tuloksille.
     * @example
     * <pre name="test">
     *  Kierros kierros = new Kierros();
     *  kierros.setTulosEtu(35);
     *  kierros.setTulosTaka(40);
     *  kierros.getTulosYht() === 75;
     * </pre>
     */
    private void laskeYhteistulos() {
        this.tulosYht = this.tulosEtu + this.tulosTaka;
    }    
    
    /**
     * @param kenttaLyhenne Kentän lyhenne.
     * Asettaa lyhenteen kentälle, jonka maksimipituus
     * on kuusi merkkiä.
     */
    public void setKenttaLyhenne(String kenttaLyhenne) {
        if (kenttaLyhenne.length() <= 6) {
            this.kenttaLyhenne = kenttaLyhenne;
        } else {
            this.kenttaLyhenne = kenttaLyhenne.substring(0, 6);
        }
    }    
    
    /**
     * Konstruktori.
     */
    public Kierros() {
        //
    }
    
    /**
     * @param pelaajaNro pelaajanumero.
     * Konstruktori, jossa on pelaajanumero.
     */
    public Kierros(int pelaajaNro) {
        this.pelaajaNro = pelaajaNro;
        rekisteroi();
    }    
    
    /*
     * Asettaa kierrokselle tunnusnumeron.
     */
    @SuppressWarnings("unused")
    private void setTunnusNro(int nr) {
        kierrostunnus = nr;
        if ( kierrostunnus >= seuraavaNro ) seuraavaNro = kierrostunnus + 1;
    }
    

    /**
     * @param rivi Kierroksen tiedot.
     * Jäsentelee sopivaksi tiedot merkkijonon perusteella.
     */
    public void parse(String rivi) {        
        StringBuffer sb = new StringBuffer(rivi);
        pvm = Mjonot.erota(sb, '|', pvm);
        kenttaLyhenne = Mjonot.erota(sb, '|', kenttaLyhenne);
        tulosEtu = Mjonot.erota(sb, '|', tulosEtu);
        tulosTaka = Mjonot.erota(sb, '|', tulosTaka);
        tulosYht = Mjonot.erota(sb, '|', tulosYht);
        pelaajaNro = Mjonot.erota(sb, '|', pelaajaNro);
    }
    
    /**
     * @return palauttaa kenttien lukumäärän.
     */
    public int getKenttia() {
        return 5;
    }

    /**
     * @return palauttaa kunkin kierroksen tunnusnumeron.
     */
    public int getKierrosID() {
        int numero = seuraavaNro;
        if ( kierrostunnus >= seuraavaNro ) seuraavaNro = kierrostunnus + 1;
        return numero;

    }
    
    /**
     * @param k Valitun kentän tiedot.
     * @return Palauttaa kierroksen tiedot kunkin kentän pohjalta.
     */
    public String anna(int k) {
        switch (k) {
        case 0: return "" + getKierrosID();
        case 1: return "" + pvm;
        case 2: return "" + kierrostunnus;
        case 3: return Integer.toString(kenttaID);
        case 4: return Integer.toString(tulosYht);
        default: return "Ääliö";
        }
    }
    
    /*
     * Palauttaa kierroksen tiedot merkkijonona.
     */
    @Override
    public String toString() {
        return  pvm + "|" + kenttaLyhenne + "|" + tulosEtu + "|" + tulosTaka + "|" + tulosYht + "|" + pelaajaNro ;
    }    

    /**
     * @param out Tulostaa kierroksen.
     */
    public void tulosta(PrintStream out) {
        out.println("Päivämäärä: " + pvm);
        out.println("Kenttä: " + kenttaLyhenne);
        out.println("Tulos etu: " + tulosEtu);
        out.println("Tulos taka: " + tulosTaka);
        out.println("Tulos yhteensä: " + tulosYht); 
    }
    
    /**
     * @param os tulostaa kierroksen OutputStreamiin.
     */
    public void tulosta(OutputStream os) {
        tulosta(new PrintStream(os));
    }

    /**
     * @return Rekisteröi kierroksen. Palauttaa myös sen tunnuksen.
     * Testaa Kierros-luokan rekisteroi-metodia.
     * @example
     * <pre name="test">
     *   Kierros kierros1 = new Kierros();
     *   kierros1.getTunnusNro() === 0;
     *   kierros1.rekisteroi();
     *   Kierros kierros2 = new Kierros();
     *   kierros2.rekisteroi();
     *   int n1 = kierros1.getTunnusNro();
     *   int n2 = kierros2.getTunnusNro();
     *   n1 === n2-1;
     *   n2 === n1+1;
     * </pre>
     */
    public int rekisteroi() {
        kierrostunnus = seuraavaNro;
        seuraavaNro++;
        return kierrostunnus;
    }

    /**
     * @return palauttaa kierrostunnuksen.
     */
    public int getTunnusNro() {
        return kierrostunnus;
    }
    
    /**
     * @return palauttaa jäsennumeron.
     */
    public int getJasenNro() {
        return pelaajaNro;
    }
    
    /**
     * @param args pääohjelma.
     */
    public static void main(String[] args) {
        Kierros rundi = new Kierros();
        rundi.rekisteroi();
        rundi.tulosta(System.out);
    }
    
}