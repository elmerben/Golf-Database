package kerho;

import java.io.File;
import java.util.List;

public class Klubben {

    
    private Jasenet jasenet = new Jasenet();
    private Kierrokset kierrokset = new Kierrokset();
    private String hakemisto = "data";
    
    public int getJasenia() {
        return jasenet.getLkm();
    }
    
    public Jasen annaJasen(int i) {
        return jasenet.anna(i);
    }
    
    
    public void lisaa(Jasen jasen) throws SailoException {
        jasenet.lisaa(jasen);
    }
    
    
    public void lisaa(Kierros rundi) {
        kierrokset.lisaa(rundi);
    }
    
    public void korvaaTaiLisaa(Jasen jasen) throws SailoException {
        jasenet.korvaaTaiLisaa(jasen);
        
    }

    
    
    public List<Kierros> annaKierrokset(Jasen jasen) {
        return kierrokset.annaKierrokset(jasen.getTunnusNro());
    }
    
    public void setTiedosto(String nimi) {
        File dir = new File(nimi);
        dir.mkdirs();
        String hakemistonNimi = hakemisto;
        if(!nimi.isEmpty()) hakemistonNimi = nimi + "/";
        jasenet.setTiedostonPerusNimi(hakemistonNimi + "pelaajat");
        kierrokset.setTiedostonPerusNimi(hakemistonNimi + "kierrokset");
    }
    
    
    
    
    public void lataa() throws SailoException {
//        jasenet = new Jasenet();
//        kierrokset = new Kierrokset();
        jasenet.lueTiedostosta(hakemisto);
        kierrokset.lueTiedostosta(hakemisto);
    }
    
    
    public void lueTiedostosta(String nimi) throws SailoException {
//        File dir = new File(nimi);
//        dir.mkdir();
        jasenet = new Jasenet();
        kierrokset = new Kierrokset();
        
        hakemisto = nimi;
        setTiedosto(nimi);
        jasenet.lueTiedostosta();
        kierrokset.lueTiedostosta();
        
    }
    
    
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
    
//    public void lataa() throws SailoException {
//        jasenet = new Jasenet();
//         kierrokset = new Kierrokset();
//        jasenet.lueTiedostosta(hakemisto);
//        kierrokset.lueTiedostosta(hakemisto);
//    }

    
    
    public static void main(String[] args) {
        Klubben klubben = new Klubben();
        
        
        try {
            klubben.lueTiedostosta("data");
        } catch (SailoException ex) {
            System.out.println(ex.getMessage());
        }

        
        Jasen aku = new Jasen();
        Jasen aku2 = new Jasen();
        aku.rekisteroi();
        aku2.rekisteroi();
        
        aku.taytaOletusTiedoilla();
        aku2.taytaOletusTiedoilla();
        
        
        
        try {
            klubben.lisaa(aku);
            klubben.lisaa(aku2);
            
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

