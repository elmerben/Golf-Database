package kerho;

import java.io.OutputStream;
import java.io.PrintStream;

import fi.jyu.mit.ohj2.Mjonot;
import kanta.HcpTarkistus;
import kanta.tunnusTarkistus;

/**
 * Jäsen-luokka kuvaa kerhon jäsentä.
 * @author elias
 * @version 21.2.2023
 *
 */
public class Jasen implements Cloneable {

    
    private int     tunnusNro;
    private String  nimi        = "";
    private String  kotiseura   = "";
    private String tasoitus    = "0.0";
    
    private static int seuraavaNro = 1;
    
    /*
     * Palauttaa jäsenen tunnusnumeron hash-koodina.
     */
    @Override
    public int hashCode() {
        return tunnusNro;
    }
    /*
     * Palauttaa jäsenen tietokenttien lukumäärän.
     */
    public int getKenttia() {
        return 4;
    }
    
    /*
     * Asettaa jäsenelle nimen ja palauttaa null jos asettaminen onnistui.
     */
    public String setNimi(String s) {
        nimi = s;
        return null;        
    }

    /*
     * Vertaa jäsentä toiseen jäseneen käyden jokaisen kentän läpi.
     */
    public boolean equals(Jasen jasen) {
        if ( jasen == null ) return false;
        for (int k = 0; k < getKenttia(); k++)
            if ( !anna(k).equals(jasen.anna(k)) ) return false;
        return true;
    }    
    /*
     * Overridaa tai "ylikirjoittaa Object-luokan equals-metodin jäsenien vertailussa.
     */
    @Override
    public boolean equals(Object jasen) {
        if ( jasen instanceof Jasen ) return equals((Jasen)jasen);
        return false;
    }
    
    /*
     * Palauttaa jäsenen nimen.
     */
    public String getNimi() {
        return this.nimi;
    }
    
    /*
     * Palauttaa ensimmäisen kentän indeksin.
     */
    public int ekaKentta() {
        return 1;
    }
    
    /*
     * Palauttaa kysymyksen, joka liittyy kunkin kentän indeksiin.
     */
    public String getKysymys(int k) {
        switch (k) {
        case 0: return "tunnusNro";
        case 1: return "nimi";
        case 2: return "kotiseura";
        case 3: return "tasoitus";
        default: return "Ääliö";
        }
    }

    /*
     * Palauttaa annetun kentän arvon.
     */
    public String anna(int k) {
        switch (k) {
        case 0: return "" + tunnusNro;
        case 1: return "" + nimi;
        case 2: return "" + kotiseura;
        case 3: return tasoitus;
        default: return "Ääliö";
        }
    }
    
    private HcpTarkistus tasurit = new HcpTarkistus();
    
    /*
     * Asettaa annetun kentän arvoksi merkkijonon ja virheen kohdalla
     * palauttaa virheen.
     */
    public String aseta(int k, String jono) {
        String tjono = jono.trim();
        StringBuffer sb = new StringBuffer(tjono);
        switch (k) {
        case 0:
        setTunnusNro(Mjonot.erota(sb, '§', getTunnusNro()));
        return null;
        case 1:
            nimi = tjono;
            return null;
        case 2: 
            kotiseura = tjono;
            return null;
        case 3: 
            String virhe = tasurit.tarkista(tjono);
            if (virhe != null) return virhe;
            tasoitus = tjono;
            return null;
        default:
            return "Virhe indeksin kanssa.";
        }
    }
    
    /**
     * Konstruktori.
     */
    public Jasen() {
        
    }
    
    /** tulostetaan henkilön tiedot.
     * @param out tietovirta johon tulostetaan
     */
    public void tulosta(PrintStream out) {
        out.println(String.format("Tunnusnro: " + "%04d", tunnusNro) + " |" + " Nimi: " + nimi + " |" + " Kotiseura: " + kotiseura + " |" + " Tasoitus: " + tasoitus + " |");
    }
    
    /**
     * Overridaa Object-luokan toString-metodin. Palauttaa jäsenen merkkijonona.
     */
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
    
    /**
     * Palauttaa jäsenen tunnusnumeron.
     * @return tunnusnro
     */
    public int getTunnusNro() {
        return this.tunnusNro;
    }    
    
    /**
     * Asettaa jäsenelle tunnuksen ja samalla
     * päivittää seuraavan vapaan numeron.
     * @param nr
     */
    private void setTunnusNro(int nr) {
        tunnusNro = nr;
        if(tunnusNro >= seuraavaNro) seuraavaNro = tunnusNro + 1;
    }
    
    /**
     * @return palauttaa jäsenen uuden tunnusnro:n
     * @example asdasd
     * <pre name="test">
     *   Jasen aku1 = new Jasen();
     *   aku1.getTunnusNro() === 0;
     *   aku1.rekisteroi();
     *   Jasen aku2 = new Jasen();
     *   aku2.rekisteroi();
     *   int n1 = aku1.getTunnusNro();
     *   int n2 = aku2.getTunnusNro();
     *   n1 === n2-1;
     *   n2 === n1+1 ;
     * </pre>
     */
    public int rekisteroi() {
        this.tunnusNro = seuraavaNro;
        seuraavaNro++;
        return this.tunnusNro;
    }    
    
    /**
     * Tulostaa jäsenen tiedot Outputsream-olioon.
     */
    public void tulosta(OutputStream os) {
        tulosta(new PrintStream(os));
    }    

    /*
     * Palauttaa jäsenen seuran.
     */
    public String getSeura() {
        return kotiseura;
    }
    
    /*
     * Palauttaa tasoituksen.
     */
    public String getHcp() {
        return tasoitus;
    }    

    /*
     * Antaa jäsenelle uuden tasoituksen sekä 
     * tarvittaessa palauttaa virheen 
     */
    public String setHcp(String s) {
        HcpTarkistus tarkistaja = new HcpTarkistus();
        String virhe = tarkistaja.tarkista(s);
        if(virhe!= null) return virhe;
        tasoitus = s;
        return null;
    }
    
    /**
     * @param args ei käytössä
     */
    public static void main(String[] args) {
        Jasen aku = new Jasen();
        Jasen aku2 = new Jasen();
        aku.rekisteroi();
        aku2.rekisteroi();
        
        aku.tulosta(System.out);
        aku.tulosta(System.out);
        aku2.tulosta(System.out);
    }    

    /*
     * Lukee jäsenen tiedot merkkijonosta.
     */
    public void parse(String rivi) {
        StringBuffer sb = new StringBuffer(rivi);
        for(int k = 0; k < getKenttia(); k++)
            aseta(k, Mjonot.erota(sb, '|'));

    }

    /*
     * Kloonaa jäsen-olion.
     */
    @Override
    public Jasen clone() throws CloneNotSupportedException {
        Jasen uusi;
        uusi = (Jasen)super.clone();
        return uusi;
    }
    }



