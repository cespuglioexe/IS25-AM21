package it.polimi.it.galaxytrucker.view.GUI.controllers;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;

import java.io.IOException;
import java.util.Objects;

public class GUIActivateCannonController extends GUIViewState {

    private static GUIActivateCannonController instance;

    public static GUIActivateCannonController getInstance() {
        synchronized (GUIActivateCannonController.class) {
            if (instance == null) {
                instance = new GUIActivateCannonController();
            }
            return instance;
        }
    }

    public GUIActivateCannonController() {
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

        });
    }
}
