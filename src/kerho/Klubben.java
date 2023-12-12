package kerho;

import java.io.File;
import java.util.List;

/**
 * @author elias
 * @version 11.12.2023
 * Klubben-luokka, joka hallitsee jäsenet- ja kierrokset-luokkia.
 */
public class Klubben {

    
    private Jasenet jasenet = new Jasenet();
    private Kierrokset kierrokset = new Kierrokset();
    private String hakemisto = "data";
    
    /**
     * @return palauttaa jasenet-lukumäärän.
     */
    public int getJasenia() {
        return jasenet.getLkm();
    }
    
    /**
     * @param i haettu jäsen
     * @return palauttaa jäsenen tiedot Jäsenet-luokasta.
     */
    public Jasen annaJasen(int i) {
        return jasenet.anna(i);
    }
    
    /**
     * @param jasen palauttaa jäsenen indeksin perusteella.
     */
    public void lisaa(Jasen jasen) {
        jasenet.lisaa(jasen);
    }
    
    /*
     * Lisää jäsenen.
     */
    /**
     * @param rundi lisää kierroksen
     */
    public void lisaa(Kierros rundi) {
        kierrokset.lisaa(rundi);
    }
    
    /**
     * @param jasen Korvaa tai lisää jäsenen
     */
    public void korvaaTaiLisaa(Jasen jasen) {
        jasenet.korvaaTaiLisaa(jasen);
    }
    
    /**
     * @param jasen Jasen-olio
     * @return palauttaa annetun jäsenen kierrokset
     */
    public List<Kierros> annaKierrokset(Jasen jasen) {
        return kierrokset.annaKierrokset(jasen.getTunnusNro());
    }
    
    /**
     * @param nimi tiedoston nimi.
     * Asettaa tiedostosijainnin.
     */
    public void setTiedosto(String nimi) {
        File dir = new File(nimi);
        dir.mkdirs();
        String hakemistonNimi = hakemisto;
        if(!nimi.isEmpty()) hakemistonNimi = nimi + "/";
        jasenet.setTiedostonPerusNimi(hakemistonNimi + "pelaajat");
        kierrokset.setTiedostonPerusNimi(hakemistonNimi + "kierrokset");
    } 
    
    /**
     * @throws SailoException epäonnistuessa heittää poikkeuksen.
     * Muutoin lataa jäsenen ja kierroksen kustakin tiedostosta.
     */
    public void lataa() throws SailoException {
        jasenet = new Jasenet();
        kierrokset = new Kierrokset();
        jasenet.lueTiedostosta();
        kierrokset.lueTiedostosta();
    }
    
    /**
     * @param poistettavaKierros poistettava kierros.
     * @throws SailoException virheenkäsittely
     * Poistaa kierroksen tiedostoista.
     */
    public void poistaKierros(Kierros poistettavaKierros) throws SailoException {
        if (poistettavaKierros == null) return;

        kierrokset.poista(poistettavaKierros);
        kierrokset.tallenna();
    }
    
    /**
     * @param poistettavaJasen Jäsen, keneltä poistetaan.
     * @throws SailoException virheenkäsittely.
     * Poistaa sekä jäsenen, että häneen liitetyt kierrokset tiedostoista.
     */
    public void poistaJasen(Jasen poistettavaJasen) throws SailoException {
        if (poistettavaJasen == null) return;
        
        List<Kierros> poistettavatKierrokset = annaKierrokset(poistettavaJasen);
        for (Kierros kierros : poistettavatKierrokset) {
            this.kierrokset.poista(kierros);
        }       
        boolean poistettu = jasenet.poista(poistettavaJasen);
        if (!poistettu) throw new SailoException("Jäsenen poisto epäonnistui");
        jasenet.tallenna();        
    }    
    
    /**
     * @param nimi Jäsenen tiedot.
     * @throws SailoException virheenkäsittely.
     * Lataa jäsenen, sekä kierrosten tiedot tiedostosta.
     */
    public void lueTiedostosta(String nimi) throws SailoException {
        File dir = new File(nimi);
        dir.mkdir();
        jasenet = new Jasenet();
        kierrokset = new Kierrokset();
        
        hakemisto = nimi;
        setTiedosto(nimi);
        jasenet.lueTiedostosta();
        kierrokset.lueTiedostosta();
        
    }    
    
    /**
     * @throws SailoException Virheenkäsittely
     * Tallentaa jäsentiedot, sekä kierrostiedot tiedostoon.
     */
    public void tallenna() throws SailoException {
        String virhe = "";
        try {
            jasenet.tallenna();
        } catch (SailoException ex) {
            virhe = ex.getMessage();
        }
        try {            
            kierrokset.tallenna();
        } catch (SailoException ex) {
            virhe = ex.getMessage();
        }
        if (!"".equals(virhe)) throw new SailoException(virhe);
        
    } 
    
    /**
     * @param args Pääohjelma.
     */
    public static void main(String[] args) {
        Klubben klubben = new Klubben();
        
        try {
            klubben.lataa();            
        } catch (SailoException ex) {
            System.out.println(ex.getMessage());
        }
        try {            
            for (int i = 0; i < klubben.getJasenia(); i++) {
                Jasen jasen = klubben.annaJasen(i);
                jasen.tulosta(System.out);
            }
            klubben.tallenna();
            
        } catch (SailoException e) {
            System.err.println(e.getMessage());
        }       
        }
    }


