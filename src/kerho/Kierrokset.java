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
import java.util.*;



public class Kierrokset implements Iterable<Kierros> {

    
    
    private String tiedostonPerusNimi = "data/kierrokset";
    private boolean muutettu = false;    
    private final Collection<Kierros> alkiot = new ArrayList<Kierros>();



    public Kierrokset() {
        //
    }
    
    public Kierros anna(int indeksi) {
        if (indeksi >= 0 && indeksi < alkiot.size()) {
            return new ArrayList<>(alkiot).get(indeksi);
        }
        return null;

    }
    

    public void lisaa(Kierros runi) {
        alkiot.add(runi);
        muutettu = true;
    }

    public String getTiedostonNimi() {
        if (!tiedostonPerusNimi.endsWith(".dat")) {
            return tiedostonPerusNimi + ".dat";
        }
        return tiedostonPerusNimi;  
    }
    
    public String setSheesh() {
        return tiedostonPerusNimi;
    }
    
    public void setTiedostonPerusNimi(String tied) {
        tiedostonPerusNimi = tied;
    }

    
    public int getKierrostenLkm() {
        return alkiot.size();
    }
    

    public String getBakNimi() {
        return tiedostonPerusNimi + ".bak";
    }
    
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
    
    public void lueTiedostosta() throws SailoException {
        lueTiedostosta(getTiedostonNimi());
    }
    
    
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


    public int getLkm() {
        return alkiot.size();
    }

    @Override
    public Iterator<Kierros> iterator() {
        return alkiot.iterator();
    }
    
    
    public List<Kierros> annaKierrokset(int tunnusnro) {
        List<Kierros> loydetyt = new ArrayList<Kierros>();
        for (Kierros runi : alkiot)
            if (runi.getJasenNro() == tunnusnro) loydetyt.add(runi);
        return loydetyt;
    }

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
