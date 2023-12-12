package kerho;

/**
 * @author elias
 * @version 11.12.2023
 * Virheiden käsittelyä varten tehty luokka.
 */
public class SailoException extends Exception {


        private static final long serialVersionUID = 1L;

        
        /**
         * @param viesti virheviesti virhetilanteessa.
         */
        public SailoException(String viesti) {
            super(viesti);
        }

}
