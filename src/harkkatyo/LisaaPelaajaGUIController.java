package harkkatyo;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import fi.jyu.mit.fxgui.*;
/**
 * @author elias
 * @version 11.12.2023
 *
 */
public class LisaaPelaajaGUIController {

    //@FXML
    //private TextField editNimi;

    @FXML
    private TextField hcp;

    @FXML
    private MenuButton kotiseura;

    @FXML
    private Button peruuta;

    @FXML
    private TextField sukunimi;

    @FXML
    private Button tallenna;

    @FXML
    void keyPressed(@SuppressWarnings("unused") KeyEvent event) {
        Dialogs.showMessageDialog("Ei tapahdu mitään.");

    }

}
