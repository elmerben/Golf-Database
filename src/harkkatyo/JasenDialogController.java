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
//
//    @FXML private TextField editNimi;
//    @FXML private TextField editSuku;
//    @FXML private TextField editHcp;
//    @FXML private TextField editSeura;
    @FXML private Label labelVirhe;
    @FXML private GridPane gridJasen;

    
    
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        alusta();
    }

    @FXML private void handleOk() {
        if (jasenKohdalla != null && jasenKohdalla.getNimi().trim().equals("")) {
            naytaVirhe("Nimi ei saa olla tyhjä.");
            return;
        }
        ModalController.closeStage(gridJasen);
        }

    
    
    @FXML private void handleCancel() {
        jasenKohdalla = null;
        ModalController.closeStage(gridJasen);
    }
    
    
    @Override
    public Jasen getResult() {
        return jasenKohdalla;
    }

    @Override
    public void handleShown() {
        //
    }
    
    
    private void naytaVirhe(String virhe) { //WTF
        if(virhe == null || virhe.isEmpty()) {
            labelVirhe.setText("");
            labelVirhe.getStyleClass().removeAll("virhe");
            return;
        }
        labelVirhe.setText(virhe);
        labelVirhe.getStyleClass().add("virhe");
    }

    @Override
    public void setDefault(Jasen oletus) {
        jasenKohdalla = oletus;
        naytaJasen(edits, jasenKohdalla);
    }
// 
    
    
    private Jasen jasenKohdalla;  
    private TextField[] edits;
    private static Jasen apujasen = new Jasen();
    
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
    
    private void alusta() {
        edits = luoKentat(gridJasen);
        for(TextField edit : edits)
            if(edit != null) {
                edit.setOnKeyReleased(e -> {
                    kasitteleMuutosJaseneen((TextField) (e.getSource()));
                    kasitteleMuutosHcp((TextField) (e.getSource()));
                });
//                edit.setOnKeyReleased(e -> kasitteleMuutosJaseneen((TextField)(e.getSource())));
                // edit.setOnKeyReleased(e -> kasitteleMuutosHcp((TextField)(e.getSource())));
            }

        
//        edits = new TextField[] {editNimi, editHcp, editSeura};
//        editNimi.setOnKeyReleased(e -> kasitteleMuutosJaseneen(editNimi));
//        editHcp.setOnKeyReleased(e -> kasitteleMuutosHcp(editHcp));
//
    }
    
    
    public static void naytaJasen(TextField[] edits, Jasen jasen) {
        if (jasen == null) return;
//        edits[0].setText(jasen.getNimi());
//        edits[1].setText(String.format("%.2f", jasen.getHcp()));  
        for (int k = jasen.ekaKentta(); k < jasen.getKenttia(); k++) {
            edits[k].setText(jasen.anna(k));
        }
    }
    
    public static int getFieldId(Object obj, int oletus) {
        if ( !(obj instanceof Node)) return oletus;
        Node node = (Node)obj;
        return Mjonot.erotaInt(node.getId().substring(1), oletus);
    }
    
    
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
    
    private void kasitteleMuutosHcp(TextField edit) {
        if(jasenKohdalla == null) return;
        String s = edit.getText();
        String virhe = null;
        virhe  = jasenKohdalla.setHcp(s);
        if(virhe == null) {
            Dialogs.setToolTipText(edit,"");
            naytaVirhe(virhe);
           // edit.getStyleClass().add("normaali");
        } else {
            Dialogs.setToolTipText(edit,virhe);
            naytaVirhe(virhe);
           // edit.getStyleClass().add("virhe");
        }

        
        
//        try {
//            double hcp = Double.parseDouble(s);
//            String virhe = jasenKohdalla.setHcp(hcp);
//            if(virhe == null) {
//                Dialogs.setToolTipText(edit, "");
//                edit.getStyleClass().add("normaali");
//                //System.err.println(virhe);
//                labelVirhe.setText("");
//            } else {
//                Dialogs.setToolTipText(edit, "");
//                naytaVirhe(virhe);
//                edit.getStyleClass().add("virhe");
//                labelVirhe.setText(virhe);
//
//                
//            }
//        } catch (NumberFormatException e) {
//            System.err.println("Virheellinen tasoitus.");
//            labelVirhe.setText("Virhe");

        
//        String s = edit.getText();
//        double virhe = 0;
//        virhe = jasenKohdalla.setHcp(s);
//        if(virhe == 0) { // WTF
//           System.err.println("JFJAFJAJF");
//        } else {
//            System.err.println("JFJAFJAJF");
//        }
    }

    
    
    
    public static Jasen kysyJasen(Stage modalityStage, Jasen oletus) {
       return ModalController.showModal(KlubbenGUIController.class.getResource("LisaaPelaajaGUIView.fxml"), "Jäsen", modalityStage, oletus);
       
       
    }
}
