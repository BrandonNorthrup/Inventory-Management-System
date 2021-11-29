/*
Brandon Northrup
Student ID #001177877
Software I - Java - C482
*/

package inventory.management;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class InventoryManagement extends Application {

    // Set the main window (stage) using FMXLLoader - References the primary FXML file in this case
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("Inventory.fxml"));
        Scene scene = new Scene(root);
        stage.setTitle("IMS");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}