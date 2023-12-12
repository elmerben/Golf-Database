 package harkkatyo;

import javafx.application.Application;
import javafx.stage.Stage;
import kerho.Klubben;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.fxml.FXMLLoader;



/**
 * @author elias
 * @version 2.2.2023
 * Main-luokka, joka alustaa projektin.
 */
public class HarkkaMain extends Application {
    @Override
    public void start(Stage primaryStage) {
        try {
            final FXMLLoader ldr = new FXMLLoader(getClass().getResource("KlubbenGUIView.fxml"));
            final Pane root = (Pane)ldr.load();
            final KlubbenGUIController KlubbenCtrl = (KlubbenGUIController)ldr.getController();
            
            final Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("harkka.css").toExternalForm());
            primaryStage.setScene(scene);
            primaryStage.setTitle("Golf-Klubben");
            primaryStage.show();            
            primaryStage.setOnCloseRequest((event) -> {
                if ( !KlubbenCtrl.voikoSulkea() ) event.consume();
            });            
            Klubben klubben = new Klubben();
            klubben.lataa();
            KlubbenCtrl.setKerho(klubben);
            primaryStage.show();
            
            Application.Parameters params = getParameters();
            if(params.getRaw().size() > 0)
                KlubbenCtrl.lueTiedosto(params.getRaw().get(0));
        } catch(Exception e) {
            e.printStackTrace();
        }      
    }

    /**
     * @param args Ei k�yt�ss�
     */
    public static void main(String[] args) {
        launch(args);
    }
}