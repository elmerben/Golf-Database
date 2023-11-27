package kerho;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.*;


public class Kentat implements Iterable<Kentta> {

    private Collection<Kentta> alkiot = new ArrayList<Kentta>();
    private String tiedostonNimi = "";
    
    public Kentat() {
        //
    }
    
    public void lisaa(Kentta ken) {
        alkiot.add(ken);
    }
    
    public void lueTiedostosta(String hakemisto) throws SailoException {
        String nimi = hakemisto + "/kentat.dat";
        File ftied = new File(nimi);
        try (Scanner fi = new Scanner(new FileInputStream(ftied))) {
            while ( fi.hasNext() ) {
                String s = fi.nextLine().trim();
                if ( "".equals(s) || s.charAt(0) == ';' ) continue;
                Kentta ken = new Kentta();
                ken.parse(s);
                lisaa(ken);
            }
        } catch ( FileNotFoundException e ) {
            throw new SailoException("Ei saa luettua tiedostoa " + nimi);
        }
    }

    public void tallenna(String hakemisto) throws SailoException {
        String nimi = hakemisto + "/kentat.dat";
        File ftied = new File(nimi);
        try (PrintStream fo = new PrintStream(new FileOutputStream(ftied, false))) {
            for (var ken: alkiot) {
                fo.println(ken.toString());
            }
        } catch (FileNotFoundException ex) {
            throw new SailoException("Tiedosto " + ftied.getAbsolutePath() + " ei aukea");
        }        
    }
    
    public List<Kentta> annaKentat(int tunnusnro){
        List<Kentta> loydetyt = new ArrayList<Kentta>();
        for(Kentta ken : alkiot)
            if(ken.getkenttaNro() == tunnusnro) loydetyt.add(ken);
        return loydetyt;
    }   
    
    public static void main(String[] args) {
        
        Kentat pelialustat = new Kentat();        
        try {
            pelialustat.lueTiedostosta("henkilot");
        } catch (SailoException ex) {
            System.err.println(ex.getMessage());
        }    
        Kentta kentta1 = new Kentta();
        kentta1.vastaaKentta(1);
        Kentta kentta2 = new Kentta();
        kentta2.vastaaKentta(2);
    }

    @Override
    public Iterator<Kentta> iterator() {
        return alkiot.iterator();
    }

}
