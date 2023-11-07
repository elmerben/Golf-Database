package harkkatyo;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import kerho.Jasen;
import kerho.Kierros;
import kerho.Klubben;
import kerho.SailoException;
import fi.jyu.mit.fxgui.*;

import java.io.PrintStream;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;


/**
 * @author elias
 * @version 21.2.2023
 *
 */
public class KlubbenGUIController implements Initializable {


    private String kerhonnimi = "data"; // OLI PELAAJAREKISTERI 
    
    @FXML private TitledPane hakuEhto;
    @FXML private TextField hakuKentta;
    @FXML private MenuButton kierrosPituus;
    @FXML private Button lisaaKentta;
    @FXML private GridPane gridJasen;

    @FXML private TextField lyontiMaara;
    @FXML private Menu ohje;
    @FXML private DatePicker paivamaara;
    @FXML private TextField editHcp;
    @FXML private TextField editNimi;

    @FXML private CheckBox tasoitusKierros;
    @FXML private Menu tiedosto;
    @FXML private MenuButton valitseKentta;
    @FXML private Font x1;
    @FXML private Color x2;
    @FXML private Font x3;
    @FXML private Color x4;
    @FXML private Button tallennus;
    @FXML private ScrollPane panelJasen;
    @FXML private StringGrid<Kierros> tableKierrokset;
    
    @FXML private ListChooser<Jasen> chooserJasenet;
    

    @FXML
    void keyPressed(KeyEvent event) {
        Dialogs.showMessageDialog("Voitit pelin");
    }


    @Override
    public void initialize(URL url, ResourceBundle bundle) {
        alusta();
        naytaJasen();
    }

    
    @FXML
    void handleTallenna() {
        tallenna();
    }
    
    @FXML
    void handlePeruuta(ActionEvent event) {
        peruuta();
    }
    
    @FXML
    void handleLisaaKentta(ActionEvent event) {
        LisaaKentta();

    }

    @FXML private void handleMuokkaaJasen() {
        muokkaa();
    }
    
    @FXML
    void handleLisaaPelaaja() {
        uusiPelaaja();

    }
    @FXML
    void handleLisaaTulos(ActionEvent event) {
        uusiTulos();
    }
    
   // _____________________________________________________

    
    
//    public boolean avaa() {
//        String uusinimi = KerhonNimiController.kysyNimi(null,kerhonnimi);
//        if(uusinimi == null) return false;
//        lueTiedosto(uusiNimi);
//        return true;
//    }
    
    
    
    protected void lueTiedosto(String nimi) { // OLI PROTECTED STRING
        kerhonnimi = nimi;
        try {
            klubben.lueTiedostosta(nimi);
            hae(0);
           // return null;
        }catch (SailoException e) {
           // hae(0);
            // String virhe = e.getMessage();
           // if(virhe != null) Dialogs.showMessageDialog(virhe);
            // return virhe;
            Dialogs.showMessageDialog(e.getMessage());
        }
    }
    


    private Klubben klubben;
    private TextField[] edits;
    
    private void alusta() {
       // panelJasen.setContent(areaJasen);
        panelJasen.setFitToHeight(true);
        chooserJasenet.clear();
        chooserJasenet.addSelectionListener(e -> naytaJasen());
        edits = JasenDialogController.luoKentat(gridJasen);

    }
    
    protected void naytaJasen() {
        Jasen jasen = chooserJasenet.getSelectedObject();       
        if (jasen == null) return; 
        
        JasenDialogController.naytaJasen(edits, jasen);
        naytaKierrokset(jasen);
        

        
//        editNimi.setText(jasen.getNimi());
//        editHcp.setText(String.format("%.2f", jasen.getHcp()));
     // editSeura.setText(jasen.getSeura());
       // naytaKierrokset(jasen); // WTF

        
        
        
    }
    
    private void muokkaa() {
        Jasen jasen = chooserJasenet.getSelectedObject();
        if (jasen == null) return;
        try {
        jasen = jasen.clone();
        } catch (CloneNotSupportedException e) {
            // ei voi tapahtua
        }
        jasen = JasenDialogController.kysyJasen(null, jasen);
        if (jasen == null) return;
        try {
        klubben.korvaaTaiLisaa(jasen);
        } catch (SailoException e) {
            // TODO näytä dialogi
        }
        hae(jasen.getTunnusNro());
    }
    
    private void naytaKierrokset(Jasen jasen) {
        tableKierrokset.clear();
        if(jasen == null) return;
        List<Kierros> kierrokset = klubben.annaKierrokset(jasen);
        if(kierrokset.size() == 0) return;
        for (Kierros kie : kierrokset)
            naytaKierros(kie);
    }
//    
    private void naytaKierros(Kierros kie) {
        String[] rivi = kie.toString().split("\\|");
        tableKierrokset.add(kie, rivi);
    }
    
    
    public void tulosta(PrintStream os, final Jasen jasen) {
        os.println("-----------------------");
        jasen.tulosta(os);
        os.println("----------------------------");
        List<Kierros> kierrokset = klubben.annaKierrokset(jasen);
        for(Kierros rundi : kierrokset)
            rundi.tulosta(os);
        os.println("------------------------");
        
    }
    
    public void setKerho(Klubben klubben) {
        this.klubben = klubben;
        naytaJasen();
    }
    
    
    public void LisaaKentta() {
        Dialogs.showMessageDialog("Lisää kenttä");

    }

    public void uusiTulos() {
        Jasen jasenKohdalla = chooserJasenet.getSelectedObject();
        if(jasenKohdalla == null) return;
        Kierros rundi = new Kierros();
        rundi.rekisteroi();
        rundi.vastaaKierros(jasenKohdalla.getTunnusNro()); //KOrvaa dialogilla
        klubben.lisaa(rundi);
        hae(jasenKohdalla.getTunnusNro());
    }

    
    public void peruuta() {
        Dialogs.showMessageDialog("Peruuta");

    }
    
    public void tallenna() {
        try {
            klubben.tallenna();
        } catch (SailoException e) {
            Dialogs.showMessageDialog(e.getMessage());
        }
    }
    
    
    
    public void uusiPelaaja() {
        Jasen uusi = new Jasen();
        uusi = JasenDialogController.kysyJasen(null, uusi);
        if ( uusi == null) return;
        uusi.rekisteroi();
        try {
            klubben.lisaa(uusi);
        } catch (SailoException e) {
            Dialogs.showMessageDialog("Ongelmia uuden luomisessa " + e.getMessage());
            return;
        }
        hae(uusi.getTunnusNro());
        

    }
    
    
    public boolean voikoSulkea() {
        tallenna();
        return true;
    }
     
    private void hae(int jnro) {
//        chooserJasenet.clear();
//        int index = 0;
//        for (int i = 0; i < klubben.getJasenia(); i++) {
//            Jasen jasen = klubben.annaJasen(i);
//            if (jasen.getTunnusNro() == jnro) {
//                index = i;
//            }
//            chooserJasenet.add(jasen.getNimi(), jasen);
//        }
//        chooserJasenet.setSelectedIndex(index);
//            
//        
//    }
        chooserJasenet.clear();
         int index = 0;
         for (int i = 0; i < klubben.getJasenia(); i++) {
            Jasen jasen = klubben.annaJasen(i);
            if (jasen.getTunnusNro() == jnro) index = i;
            chooserJasenet.add(""+jasen.getNimi(), jasen);
         }
            
         chooserJasenet.setSelectedIndex(index);
        }



    
    
    protected void uusiJasen() {
        Jasen uusi = new Jasen();
        uusi = JasenDialogController.kysyJasen(null, uusi);
        if(uusi == null) return;
        uusi.rekisteroi();
        try {
            klubben.lisaa(uusi);
        } catch (SailoException e) {
            Dialogs.showMessageDialog("Ongelmia uuden luomisessa " + e.getMessage());            
        }
        hae(uusi.getTunnusNro());
    }



    
    
    
}
