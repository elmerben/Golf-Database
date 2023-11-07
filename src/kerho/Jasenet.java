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
    private String tiedostonPerusNimi = "pelaajat";
    private boolean muutettu = false;
    private String kokoNimi = "";
    
    int lkm = 0;
    


    public static void main(String[] args) {
        

        
        Jasenet jasenet = new Jasenet();
        Jasen aku = new Jasen();
        Jasen aku2 = new Jasen();
        Jasen aku3 = new Jasen();
        Jasen aku4 = new Jasen();
        
        try {
            jasenet.lueTiedostosta("data");
        } catch (SailoException ex) {
            System.err.println(ex.getMessage());
        }
        
        aku.rekisteroi();
        aku.taytaOletusTiedoilla();
        aku2.rekisteroi();
        aku2.taytaOletusTiedoilla();
        aku3.rekisteroi();
        aku3.taytaOletusTiedoilla();
        aku4.rekisteroi();
        aku4.taytaOletusTiedoilla();
        
        jasenet.lisaa(aku);
        jasenet.lisaa(aku2);
        jasenet.lisaa(aku3);
        jasenet.lisaa(aku4);

        
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
    
//    public void tallenna(String hakemisto) throws SailoException {
//        File ftied = new File(hakemisto + "/nimet.dat");
//        
//        //
//    }
    
    
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
                jasen.parse(rivi); // voisi olla virhekäsittely
                lisaa(jasen);
            }
            muutettu = false;
            
        } catch ( FileNotFoundException e ) {
            throw new SailoException("Tiedosto " + getTiedostonNimi() + " ei aukea");
        } catch ( IOException e ) {
            throw new SailoException("Ongelmia tiedoston kanssa: " + e.getMessage());
        }
 

        }
        
        
//        tiedostonPerusNimi = tied + "/pelaajat.dat";
//        // String nimi = tiedostonNimi; //__________________________________________
//        File ftied = new File(tiedostonPerusNimi);
//        
//        try (Scanner fi = new Scanner(new FileInputStream(ftied))) {
//            while ( fi.hasNext() ) {
//                String s = fi.nextLine();
//                if (s == null || "".equals(s) || s.charAt(0) == ';') continue;
//                Jasen jasen = new Jasen();
//                jasen.parse(s); 
//                lisaa(jasen);
//            }
//            muutettu = false;
//        } catch ( FileNotFoundException e ) {
//            throw new SailoException("Ei saa luettua tiedostoa " + tiedostonPerusNimi);
//        }
//
//    }

    
    
    
    //
    
//    public Jasen next() throws NoSuchElementException {
//        if(!hasNext()) throw new NoSuchElementException("Ei ole");
//        return anna(kohdalla++);
//    }
//    
//    public boolean hasNext() {
//        return kohdalla < getLkm();
//    }
    
    
    public void talleta() throws SailoException {
        tallenna();
    }
    
    
    public void tallenna() throws SailoException {
       // if (!muutettu) return;
        
        File fbak = new File(getBakNimi());
        File ftied = new File(getTiedostonNimi());
        fbak.delete();
        ftied.renameTo(fbak);
        try (PrintWriter fo = new PrintWriter(new FileWriter(ftied.getCanonicalPath()))) {
            fo.println(getKokoNimi());
                    fo.println(alkiot.length);
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
