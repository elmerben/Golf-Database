package kerho;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Jasenet {
    
    
    
    private static final int MAX_JASENIA = 5;
    
    private Jasen[] alkiot;
    private String tiedostonPerusNimi = "data/pelaajat";
    private boolean muutettu = false;
    private String kokoNimi = "";
    
    int lkm = 0;
    


    public static void main(String[] args) {
        Jasenet jasenet = new Jasenet();
        try {
            jasenet.lueTiedostosta();
        } catch (SailoException ex) {
            System.err.println(ex.getMessage());
        }        
        try {
            jasenet.tallenna();
        } catch (SailoException e) {
            System.err.println(e.getMessage());
        }

        for (int i = 0; i < jasenet.getLkm(); i++) {
            Jasen jasen = jasenet.anna(i);
            System.out.println("Jäsenindeksi: " + i);
            jasen.tulosta(System.out);
        }
    }
    
    public void lueTiedostosta() throws SailoException {
        lueTiedostosta(getTiedostonPerusNimi());
    }    
    
    public void lueTiedostosta(String tied) throws SailoException {        
        setTiedostonPerusNimi(tied);
        try (BufferedReader fi = new BufferedReader(new FileReader(getTiedostonNimi()))){
            kokoNimi = fi.readLine();
            if(kokoNimi == null) throw new SailoException("Kerhon nimi puuttuu");
            String rivi = fi.readLine();
            if(rivi == null)  throw new SailoException("Maksimikoko puuttuu");
            
            while ( (rivi = fi.readLine()) != null ) {
                rivi = rivi.trim();
                if ( "".equals(rivi) || rivi.charAt(0) == ';' ) continue;
                Jasen jasen = new Jasen();
                jasen.parse(rivi);
                System.out.println("Luettu rivi: " + rivi);
                lisaa(jasen);
            }
            muutettu = false;
            
        } catch ( FileNotFoundException e ) {
            throw new SailoException("Tiedosto " + getTiedostonNimi() + " ei aukea");
        } catch ( IOException e ) {
            throw new SailoException("Ongelmia tiedoston kanssa: " + e.getMessage());
        }
 

        }       
    
    public boolean poista(Jasen poistettavaJasen) {
        if (poistettavaJasen == null) return false;
        int poisto = -1;
        for (int i = 0; i < lkm; i++) {
            if(alkiot[i].equals(poistettavaJasen)) {
                poisto = i;
                break;
            }
        }
        if (poisto == -1) return false;
        for (int i = poisto; i < lkm -1; i++) {
            alkiot[i] = alkiot[i + 1];
        }
        
        lkm--;
        alkiot[lkm] = null;
        muutettu = true;
        return true;
    }
    
    public void talleta() throws SailoException {
        tallenna();
    }    
    
    public void tallenna() throws SailoException {
        if (!muutettu) return;
        
        File fbak = new File(getBakNimi());
        File ftied = new File(getTiedostonNimi());
        fbak.delete();
        ftied.renameTo(fbak);
        
        try (PrintWriter fo = new PrintWriter(new FileWriter(ftied.getCanonicalPath()))) {
            fo.println(getKokoNimi());
                    fo.println(lkm);
                    for(int i = 0; i < lkm; i++) {
                        Jasen jasen = anna(i);
                        fo.println(jasen.toString());
                    }
        } catch ( FileNotFoundException ex ) {
            throw new SailoException("Tiedosto " + ftied.getName() + " ei aukea");
        } catch ( IOException ex ) {
            throw new SailoException("Tiedoston " + ftied.getName() + " kirjoittamisessa ongelmia");
        }
        muutettu = false;
        
    }
    
    public String getBakNimi() {
        return tiedostonPerusNimi + ".bak";
    }
    public String getTiedostonNimi() {
        return getTiedostonPerusNimi() + ".dat";
    }
    
    public String getKokoNimi() {
        return kokoNimi;
    }
    
    public void setTiedostonPerusNimi(String nimi) {
        tiedostonPerusNimi = nimi;
    }
    
    
    public void lisaa(Jasen jasen) {
        if ( lkm >= alkiot.length) alkiot = Arrays.copyOf(alkiot, lkm + 20);
        alkiot[lkm] = jasen;
        lkm++;
        muutettu = true;
    }
    
    public void korvaaTaiLisaa(Jasen jasen) {
        int id = jasen.getTunnusNro();
        for (int i = 0; i < lkm; i++) {
            if (alkiot[i].getTunnusNro() == id) {
                alkiot[i] = jasen;
                muutettu = true;
                return;
            }
        }
        lisaa(jasen);
        
    }
    
    public Jasen anna(int i) throws IndexOutOfBoundsException {
        if (i < 0 || this.lkm <= i)
            throw new IndexOutOfBoundsException("Laiton indeksi: " + i);
        return alkiot[i];
    }
    
    public Jasenet() {
        alkiot = new Jasen[MAX_JASENIA];
    }
        
    public String getTiedostonPerusNimi() {
        return tiedostonPerusNimi;
    }

    public int getLkm() {
        return lkm;
    }



}
