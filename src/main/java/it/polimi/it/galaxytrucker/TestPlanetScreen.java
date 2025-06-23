package it.polimi.it.galaxytrucker;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class TestPlanetScreen extends Application {
   @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/it/polimi/it/galaxytrucker/fxmlstages/planetTest.fxml"));

        Scene scene = new Scene(root);
        primaryStage.setTitle("Planet Selection - TEST");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    } 
}
