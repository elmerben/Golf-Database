package harkkatyo;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.ResourceBundle;

import fi.jyu.mit.fxgui.Dialogs;
import fi.jyu.mit.fxgui.ModalController;
import fi.jyu.mit.fxgui.ModalControllerInterface;
import fi.jyu.mit.ohj2.Mjonot;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.Stage;
import kerho.Jasen;


/**
 * Hallinnoi jäsenen lisäämistä ja muokkaamista.
 * @author elias
 * @version 7.11.2023
 *
 */
public class JasenDialogController implements ModalControllerInterface<Jasen>, Initializable {
    @FXML private Label labelVirhe;
    @FXML private GridPane gridJasen;

    
    
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        alusta();
    }

    /*
     * OK-painikkeen toiminnallisuus.
     */
    @FXML private void handleOk() {
        if (jasenKohdalla != null && jasenKohdalla.getNimi().trim().equals("")) {
            naytaVirhe("Nimi ei saa olla tyhjä.");
            return;
        }
        ModalController.closeStage(gridJasen);
        }
        
    /*
     * Peruuta-painikkeen toiminnallisuus.
     */
    @FXML private void handleCancel() {
        jasenKohdalla = null;
        ModalController.closeStage(gridJasen);
    }    
    
    /*
     * Palauttaa tuloksen dialogin perusteella.
     */
    @Override
    public Jasen getResult() {
        return jasenKohdalla;
    }

    @Override
    public void handleShown() {
        //
    }    
    
    /*
     * Näyttää virheen.
     */
    private void naytaVirhe(String virhe) {
        if(virhe == null || virhe.isEmpty()) {
            labelVirhe.setText("");
            labelVirhe.getStyleClass().removeAll("virhe");
            return;
        }
        labelVirhe.setText(virhe);
        labelVirhe.getStyleClass().add("virhe");
    }

    /*
     * Asettaa oletusjäsenen dialogiin.
     */
    @Override
    public void setDefault(Jasen oletus) {
        jasenKohdalla = oletus;
        naytaJasen(edits, jasenKohdalla);
    }
        
    private Jasen jasenKohdalla;  
    private TextField[] edits;
    private static Jasen apujasen = new Jasen();
    
    /*
     * Luo tekstikentät dialogiin. Sen jälkeen palauttaa ne.
     */
    public static TextField[] luoKentat(GridPane gridJasen) {
        gridJasen.getChildren().clear();
        TextField[] edits = new TextField[apujasen.getKenttia()];
        
        for (int i =0, k = apujasen.ekaKentta(); k < apujasen.getKenttia(); k++, i++) {
            Label label = new Label (apujasen.getKysymys(k));
            gridJasen.add(label, 0, i);
            TextField edit = new TextField();
            edits[k] = edit;
            edit.setId("e"+k);
            gridJasen.add(edit, 1, i);
        }        
        return edits;        
    }
    
    /*
     * Alustaa dialogin.
     */
    private void alusta() {
        edits = luoKentat(gridJasen);
        for(TextField edit : edits)
            if(edit != null) {
                edit.setOnKeyReleased(e -> {
                    kasitteleMuutosJaseneen((TextField) (e.getSource()));
                    kasitteleMuutosHcp((TextField) (e.getSource()));
                });
            }
    }    
    
    /*
     * Näyttää jäsenen tiedot tekstikentissä.
     */
    public static void naytaJasen(TextField[] edits, Jasen jasen) {
        if (jasen == null) return; 
        for (int k = jasen.ekaKentta(); k < jasen.getKenttia(); k++) {
            edits[k].setText(jasen.anna(k));
        }
    }
    
    /*
     * Palauttaa kentän tunnuksen.
     */
    public static int getFieldId(Object obj, int oletus) {
        if ( !(obj instanceof Node)) return oletus;
        Node node = (Node)obj;
        return Mjonot.erotaInt(node.getId().substring(1), oletus);
    }
    
    
    /*
     * Käsittelee muutokset koskien jäsenen tietoja.
     */
    private void kasitteleMuutosJaseneen(TextField edit) {
        if(jasenKohdalla == null) return;
        String s = edit.getText();
        int k = getFieldId(edit, apujasen.ekaKentta());
        String virhe = jasenKohdalla.aseta(k, s);
        if(virhe == null) { // WTF
            Dialogs.setToolTipText(edit, "");            
            naytaVirhe(virhe);
        } else {
            Dialogs.setToolTipText(edit, virhe);            
            naytaVirhe(virhe);
        }
    }
    
    /*
     * Käsittelee muutokset koskien jäsenen tasoitusta.
     */
    private void kasitteleMuutosHcp(TextField edit) {
        if(jasenKohdalla == null) return;
        String s = edit.getText();
        String virhe = null;
        virhe  = jasenKohdalla.setHcp(s);
        if(virhe == null) {
            Dialogs.setToolTipText(edit,"");
            naytaVirhe(virhe);
        } else {
            Dialogs.setToolTipText(edit,virhe);
            naytaVirhe(virhe);
        }
    }    
    
    /*
     * Näyttää dialogin jäsenen kysymiseen.
     */
    public static Jasen kysyJasen(Stage modalityStage, Jasen oletus) {
       return ModalController.showModal(KlubbenGUIController.class.getResource("LisaaPelaajaGUIView.fxml"), "Jäsen", modalityStage, oletus);
    }
}
