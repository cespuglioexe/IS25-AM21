package it.polimi.it.galaxytrucker.view.GUI.controllers;

import it.polimi.it.galaxytrucker.view.GUI.GUIView;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class GUIShowOthers extends GUIViewState{
    private static GUIShowOthers instance;
    @FXML
    private PlayerShipElementController shipController;
    @FXML
    private Label nickname;

    public static GUIShowOthers getInstance() {
        synchronized (GUIShowOthers.class) {
            if (instance == null) {
                instance = new GUIShowOthers();
            }
            return instance;
        }
    }

    public void updateShip(){
        shipController.displayShip();
    }

    public GUIShowOthers() {
        try {

            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(GUIActivateCannonController.class.getResource("/it/polimi/it/galaxytrucker/fxmlstages/activateCannon.fxml")));
            loader.setController(this);
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void displayScene() {
        Platform.runLater(() -> {
            try {
                FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(
                        GUIActivateCannonController.class.getResource("/it/polimi/it/galaxytrucker/fxmlstages/showOthersShip.fxml")
                ));
                loader.setController(this);
                Parent newRoot = loader.load();

                stage = (Stage) GUIView.stage.getScene().getWindow();
                scene = new Scene(newRoot);
                stage.setScene(scene);
                stage.show();

                nickname.setText("");

                updateShip();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
