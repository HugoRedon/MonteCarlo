package montecarlo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 *
 * @author Hugo Redon Rivera
 */
public class MonteCarlo extends Application {

    /*  *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
    @Override
    public void start(Stage primaryStage)throws Exception {
        Pane pane = (Pane) FXMLLoader.load(getClass().getResource("MonteCarloView.fxml"));
        primaryStage.setScene(new Scene(pane));
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("Aqua Ball Red.png")));
        primaryStage.show();
    }
}

