package harkkatyo;

import java.net.URL;
import java.util.ResourceBundle;


import fi.jyu.mit.fxgui.ModalController;
import fi.jyu.mit.fxgui.ModalControllerInterface;
import fi.jyu.mit.ohj2.Mjonot;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import kerho.Kierros;

public class KierrosDialogController implements ModalControllerInterface<Kierros>, Initializable {

    
    
    @FXML TextField editNimi;
    @FXML TextField editHetu;
    @FXML TextField editKatuosoite;
    @FXML TextField editPostinumero;
    @FXML Label labelVirhe;
    @FXML GridPane gridHarrastus;
    @FXML ScrollPane panelHarrastus;

    
    
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        alusta();
    }

    @Override
    public Kierros getResult() {
        return kierrosKohdalla;
    }

    @Override
    public void handleShown() {
        kentta = Math.max(apukierros.ekaKentta(), Math.min(kentta, apukierros.getKenttia()-1)); 
        edits[kentta].requestFocus(); 
    }

    @Override
    public void setDefault(Kierros oletus) {
        this.kierrosKohdalla = oletus;
        naytaHarrastus(edits, kierrosKohdalla);
    }

    @FXML private void handleOK() {
        if ( kierrosKohdalla != null && kierrosKohdalla.anna(apukierros.ekaKentta()).trim().equals("") ) {
            naytaVirhe("Nimi ei saa olla tyhjä");
            return;
        }
        ModalController.closeStage(labelVirhe);
    }

    @FXML private void handleCancel() {
        kierrosKohdalla = null;
        ModalController.closeStage(labelVirhe);
    }

    private Kierros kierrosKohdalla;
    private TextField[] edits;
    private static Kierros apukierros = new Kierros();
    private int kentta = 0;  // mikä kenttä aktivoidaan kun dialogi aukaistaan
    
    

    public static int getFieldId(Object obj, int oletus) {
        if ( !( obj instanceof Node)) return oletus;
        Node node = (Node)obj;
        return Mjonot.erotaInt(node.getId().substring(1),oletus);
    }
 
    public static TextField[] luoKentat(GridPane gridHarrastus) {
        gridHarrastus.getChildren().clear();
        TextField[] edits = new TextField[apukierros.getKenttia()];
        
        for (int i=0, k = apukierros.ekaKentta(); k < apukierros.getKenttia(); k++, i++) {
            Label label = new Label(apukierros.getKysymys(k));
            gridHarrastus.add(label, 0, i);
            TextField edit = new TextField();
            edits[k] = edit;
            edit.setId("e"+k);
            gridHarrastus.add(edit, 1, i);
        }
        return edits;
    }
    
    
    private void alusta() {
        edits = luoKentat(gridHarrastus);
        for (TextField edit : edits)
            if ( edit != null )
                edit.setOnKeyReleased( e -> kasitteleMuutosHarrastukseen((TextField)(e.getSource())));
        panelHarrastus.setFitToHeight(true);
    }

    
    private void setKentta(int kentta) {
       this.kentta = kentta; 
    }

    
    private void kasitteleMuutosHarrastukseen(TextField edit) {
        if (kierrosKohdalla == null) return;
        String s = edit.getText();
        int k = getFieldId(edit,apukierros.ekaKentta());
        String virhe = kierrosKohdalla.aseta(k, s);
        
        if (virhe != null) {
           // Dialogs.setToolTipText(edit,virhe); 
            edit.getStyleClass().add("virhe");
            naytaVirhe(virhe);
        } else {
           // Dialogs.setToolTipText(edit,""); 
            edit.getStyleClass().removeAll("virhe");
            naytaVirhe(virhe);
        }
    }
    

    public static void naytaKierros(TextField[] edits, Kierros kierros) {
        if (kierros == null) return;
        for (int k = kierros.ekaKentta(); k < kierros.getKenttia(); k++) {
            edits[k].setText(kierros.anna(k));
        }
    }
    
  
    private void naytaVirhe(String virhe) {
        if ( virhe == null || virhe.isEmpty() ) {
            labelVirhe.setText("");
            labelVirhe.getStyleClass().removeAll("virhe");
            return;
        }
        labelVirhe.setText(virhe);
        labelVirhe.getStyleClass().add("virhe");
    }
    
    public static Kierros kysyHarrastus(Stage modalityStage, Kierros oletus, int kentta) {
        return ModalController.<Kierros, KierrosDialogController>showModal(
                KierrosDialogController.class.getResource("HarrastusDialogView.fxml"), 
                  "Harrastus", 
                  modalityStage, oletus,
                  ctrl -> ctrl.setKentta(kentta));
    }



    
}
