package it.polimi.it.galaxytrucker.view.GUI;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;

import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.awt.event.ActionEvent;
import java.io.IOException;

public class GUIApplication extends Application {
    private static Stage mainStage;

    @Override
    public void start(Stage stage) throws Exception {
            GUIApplication.setStage(stage);
            GUIApplication.switchScene("/view/SetUsernameScene.fxml", "Set username window");

    }

    public static void setStage(Stage stage) {
        mainStage = stage;
    }

    public static Stage getStage() {
        return mainStage;
    }


    public static void switchScene(String fxmlPath, String title) throws IOException {

            Platform.runLater(() -> {
                try{
                System.out.println("Switching scene to " + fxmlPath);
                FXMLLoader loader = new FXMLLoader(GUIApplication.class.getResource(fxmlPath));
                Parent root = loader.load();
                mainStage.setScene(new Scene(root));
                mainStage.setTitle(title);
                mainStage.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });


    }

    public void showGameCreationWindow() throws IOException {
        GUIApplication.switchScene("/view/GameCreationScene.fxml", "Set username window");
    }


    public void showBuildingWindow() throws IOException {
        GUIApplication.switchScene("/view/BuildingScene.fxml", "Building window");
    }
}
