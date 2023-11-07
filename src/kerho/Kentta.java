package kerho;

import java.io.OutputStream;
import java.io.PrintStream;

import fi.jyu.mit.ohj2.Mjonot;
import kanta.tunnusTarkistus;

public class Kentta {
    
    private int kenttaNro;
    private String lyhenne;
    private String nimi;
    private int parEtu;
    private int parTaka;
    private int parKoko;

    
    private static int seuraavaNro = 1;
    
    public Kentta(int kenttaNro) {
        this.kenttaNro = kenttaNro;
    }
    
    public Kentta() {
        //
    }
    
    public int rekisteroi() {
        kenttaNro = seuraavaNro;
        seuraavaNro++;
        return kenttaNro;
    }
    
    private void setKenttaNro(int nr) {
        kenttaNro = nr;
        if(kenttaNro >= seuraavaNro) seuraavaNro = kenttaNro + 1;
    }
    
    
    public void parse(String rivi) {
        StringBuffer sb = new StringBuffer(rivi);
        setKenttaNro(Mjonot.erota(sb, '|', getkenttaNro()));
        lyhenne = Mjonot.erota(sb, '|', lyhenne);
        nimi = Mjonot.erota(sb, '|', nimi);
        parEtu = Mjonot.erota(sb, '|', parEtu);
        parTaka = Mjonot.erota(sb, '|', parTaka);
        parKoko = Mjonot.erota(sb, '|', parKoko);
    }
    
    
    
    public void vastaaKentta(int nro) {
//        tunnusNro = tunnusTarkistus.rand(1, 2000);
//        kotiseura = "LPG ";
//        tasoitus = tunnusTarkistus.rand2(0.0, 52.0);
        
        kenttaNro = seuraavaNro;
        lyhenne = "JEK";
        nimi = "Jekku Jee";
        parEtu = tunnusTarkistus.rand(20, 40);
        parTaka = tunnusTarkistus.rand(20, 40);
        parKoko = parEtu + parTaka;
        
    }
    
    
    
    public int getkenttaNro() {
        return kenttaNro;
    }
    
    
    @Override
    public String toString() {
        return "" + getkenttaNro() + "|" + lyhenne + "|" + nimi + "|" + parEtu + "|" + parTaka + "|" + parKoko;
    }

    

    
    public void tulosta(PrintStream out) {
        out.println(String.format("Kenttanumero: " + "%04d", kenttaNro) + " |" + " Nimi: " + nimi + " |" + " Par etu: " + parEtu + " |" + " parTaka: " + parTaka 
                + " |" + "Par yhteensa: " + (parEtu + parTaka) + " |");
    }
    
    public void tulosta(OutputStream os) {
        tulosta(new PrintStream(os));
    }
    
    
    public static void main(String[] args) {

        Kentta Jes = new Kentta();
        Kentta Toinen = new Kentta();
        Jes.rekisteroi();
        Toinen.rekisteroi();
        Toinen.vastaaKentta(1);
        Toinen.tulosta(System.out);
        Jes.vastaaKentta(2);
        Jes.tulosta(System.out);

    }

}
