package kanta;

public class HcpTarkistus {
    
    public static final String NUMEROT = "0123456789";
    public static final String DESIMAALIT = "0123456789";

    
    
    public String tarkista(String tasoitus) {
        int pituus = tasoitus.length();
        if(pituus > 4) return "Tasoituksessa liikaa merkkejä.";
        String kirjoitusasu = "\\d{1,2}\\.\\d{1}";
        if (!tasoitus.matches(kirjoitusasu)) {
            return "Virheellinen syöte tasoituksessa.";
        }
        if (tasoitus.charAt(pituus - 2) != '.') return "Erotinmerkkinä voi olla vain piste.";
//        if(tasoitus.charAt(2) != '.') return "Erotinmerkkinä voi olla vain piste.";
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
