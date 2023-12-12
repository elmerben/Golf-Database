package kanta;

/**
 * @author elias
 * @version 12.12.2023
 * HCP tarkistusluokka
 */
public class HcpTarkistus {
    
    /**
     * Sallitut numerot
     */
    public static final String NUMEROT = "0123456789";
    /**
     * Sallitut desimaalit
     */
    public static final String DESIMAALIT = "0123456789";

    
    /**
     * Tarkastaa tasoituksen oikeellisuuden.
     * Ehtona ennen desimaalia täytyy olla yksi tai kaksi numeroa
     * ja desimaalin jälkeen yksi numero.
     * Se ei myöskään voi olla suurempi kuin 54.0
     * @param tasoitus Tarkastettava tasoitus merkkijonona.
     * @return null jos menee läpi, muuten virhe.
     */
    public String tarkista(String tasoitus) {
        int pituus = tasoitus.length();
        if(pituus > 4) return "Tasoituksessa liikaa merkkejä.";
        String kirjoitusasu = "\\d{1,2}\\.\\d{1}";
        if (!tasoitus.matches(kirjoitusasu)) {
            return "Virheellinen syöte tasoituksessa.";
        }
        if (tasoitus.charAt(pituus - 2) != '.') return "Erotinmerkkinä voi olla vain piste.";
        double summa = Double.parseDouble(tasoitus);
        if (summa > 54.0) {
            return "Tasoitus liian suuri.";
        }
        for(int i = 0; i < tasoitus.length(); i++) {
        if (!NUMEROT.contains(String.valueOf(tasoitus.charAt(i))) && tasoitus.charAt(i) != '.') {
            return "Sisältää vääriä merkkejä";
        }
    }
        return null;


    }
}
