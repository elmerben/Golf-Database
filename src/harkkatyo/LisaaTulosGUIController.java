package harkkatyo;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import kerho.Jasen;
import kerho.Kierros;
import kerho.Klubben;

public class LisaaTulosGUIController {
    
    @FXML
    private TextField editEtuysi;

    @FXML
    private DatePicker editPvm;

    @FXML
    private TextField editTakaysi;

    @FXML
    private TextArea editKentta;
    
    private Jasen jasen;
    
    private Klubben klubben;
    
    private Button tallennaTulos;
    
    private Button peruutaTulos;    
    
    public void setJasen(Jasen jasen) {
        this.jasen = jasen;
    }
    
    public void setKlubben(Klubben klubben) {
        this.klubben = klubben;
    }    
    
    @FXML
    void handlePeruutaTulos(ActionEvent event) {
        Node source = (Node) event.getSource();
        
        Stage stage = (Stage) source.getScene().getWindow();

        stage.close();
    }

    @FXML
    void handleTallennaTulos(ActionEvent event) {
        try {
            Kierros uusiKierros = new Kierros(jasen.getTunnusNro());
            if (editPvm.getValue() == null) {
                return;
            }
            uusiKierros.setPvm(editPvm.getValue().toString());
            uusiKierros.setTulosEtu(Integer.parseInt(editEtuysi.getText()));
            uusiKierros.setTulosTaka(Integer.parseInt(editTakaysi.getText()));
            uusiKierros.setKenttaLyhenne(editKentta.getText());
            uusiKierros.rekisteroi(); 

            klubben.lisaa(uusiKierros);
            Node source = (Node) event.getSource();
            
            Stage stage = (Stage) source.getScene().getWindow();

            stage.close();
            

        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    

}



