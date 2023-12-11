package kerho;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;



/**
 * @author elias
 * @version 11.12.2023
 * Kierrokset-luokka.
 */
public class Kierrokset implements Iterable<Kierros> {

    
    
    private String tiedostonPerusNimi = "data/kierrokset";
    private boolean muutettu = false;    
    private final Collection<Kierros> alkiot = new ArrayList<Kierros>();


    /**
     * Konstruktori.
     */
    public Kierrokset() {
        //
    }
    
    /**
     * @param indeksi valittu indeksi
     * @return Palauttaa kierroksen indeksistä.
     */
    public Kierros anna(int indeksi) {
        if (indeksi >= 0 && indeksi < alkiot.size()) {
            return new ArrayList<>(alkiot).get(indeksi);
        }
        return null;
    }    

    /**
     * @param runi lisää uuden kierroksen.
     */
    public void lisaa(Kierros runi) {
        alkiot.add(runi);
        muutettu = true;
    }

    /**
     * @return Palauttaa tallennustiedoston nimen
     */
    public String getTiedostonNimi() {
        if (!tiedostonPerusNimi.endsWith(".dat")) {
            return tiedostonPerusNimi + ".dat";
        }
        return tiedostonPerusNimi;  
    }
    
    /**
     * @return asettaa tiedoston nimen.
     */
    public String setSheesh() {
        return tiedostonPerusNimi;
    }
    
    /**
     * @param tied asettaa tiedostonimen,
     */
    public void setTiedostonPerusNimi(String tied) {
        tiedostonPerusNimi = tied;
    }
    
    /**
     * @return palauttaa alkioiden lukumäärän.
     */
    public int getKierrostenLkm() {
        return alkiot.size();
    }    

    /**
     * @return palauttaa varmuuskopiotiedostonimen.
     */
    public String getBakNimi() {
        return tiedostonPerusNimi + ".bak";
    }
    
    /**
     * @param tied tiedostonimi.
     * @throws SailoException Virhekäsittely.
     * Lukee kierrokset tiedostosta.
     */
    public void lueTiedostosta(String tied) throws SailoException {
        setTiedostonPerusNimi(tied);
        File tiedosto = new File(getTiedostonNimi()); // 
        try (BufferedReader fi = new BufferedReader(new FileReader(tiedosto))) {
            String rivi;
            while ((rivi = fi.readLine()) != null) {
                rivi = rivi.trim();
                if ("".equals(rivi) || rivi.charAt(0) == ';')
                    continue;
                Kierros kierros = new Kierros();
                kierros.parse(rivi);
                lisaa(kierros);
        }
            muutettu = false;
        }catch (IOException e) {
            throw new SailoException("Ongelmia tiedoston kanssa: " + e.getMessage());
        }
    }    
    
    /**
     * Lukee kierrokset tiedostosta.
     * @throws SailoException Heittää poikkeuksen virheestä.
     */
    public void lueTiedostosta() throws SailoException {
        lueTiedostosta(getTiedostonNimi());
    }    
    
    /**
     * @param poistettavaKierros kierros, joka poistetaan.
     * @return Poistaa kyseisen kierroksen.
     */
    public boolean poista(Kierros poistettavaKierros) {
        if (poistettavaKierros == null) return false;
        boolean poistettu = false;
        Iterator<Kierros> iteraattori = alkiot.iterator();
        while (iteraattori.hasNext()) {
            Kierros kierros = iteraattori.next();
            if (kierros.equals(poistettavaKierros)) {
                iteraattori.remove();
                poistettu = true;
                muutettu = true;
            }
        }
        return poistettu;
    }    

    /**
     * @throws SailoException Virhekäsittely.
     * Tallentaa kierrokset jos muutoksia tapahtuu.
     */
    public void tallenna() throws SailoException {
        if(!muutettu) return;   
        File fbak = new File(getBakNimi());
        File ftied = new File(getTiedostonNimi() );
        fbak.delete();
        ftied.renameTo(fbak);    
        try (PrintWriter kirjaaja = new PrintWriter(new FileWriter(ftied.getCanonicalPath()))) {
            kirjaaja.println(alkiot.size());
                for (Kierros runi : alkiot) {
                    kirjaaja.println(runi.toString());
                }
    } catch (FileNotFoundException ex1) {
        throw new SailoException("Tiedosto " + ftied.getName() + " ei aukea.");
    } catch (IOException ex) {
        throw new SailoException ("Tiedoston " + ftied.getName() + " kirjoittaminen ei onnistunut.");
    }
        muutettu = false;
    }

    /**
     * @return palauttaa alkioiden lkm.
     */
    public int getLkm() {
        return alkiot.size();
    }

    /*
     * Iteraattori kierrosten käymiseen läpi.
     */
    @Override
    public Iterator<Kierros> iterator() {
        return alkiot.iterator();
    }    
    
    /**
     * @param tunnusnro etsitään tunnusnumeron perusteella.
     * @return Palauttaa listalla jäsenen kierrokset
     * tunnusnumeroon perustuen.
     */
    public List<Kierros> annaKierrokset(int tunnusnro) {
        List<Kierros> loydetyt = new ArrayList<Kierros>();
        for (Kierros runi : alkiot)
            if (runi.getJasenNro() == tunnusnro) loydetyt.add(runi);
        return loydetyt;
    }

    /**
     * @param args mainfunktio.
     */
    public static void main(String[] args) {
        Kierrokset rundit = new Kierrokset();
        
        try {
            rundit.lueTiedostosta();
        } catch (SailoException ex) {
            System.err.println(ex.getMessage());
        }        
        Kierros rundi1 = new Kierros();
        rundit.lisaa(rundi1); 
        List<Kierros> kierrokset2 = rundit.annaKierrokset(1);

        for (Kierros runi : kierrokset2) {
            System.out.print(runi.getJasenNro() + " ");
            runi.tulosta(System.out);
        }        
        try {
            rundit.tallenna();
            
        } catch (SailoException e) {
            e.printStackTrace();
        }
    }
}
