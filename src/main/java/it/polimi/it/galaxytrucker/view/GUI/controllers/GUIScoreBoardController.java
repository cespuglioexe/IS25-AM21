package it.polimi.it.galaxytrucker.view.GUI.controllers;

import it.polimi.it.galaxytrucker.view.GUI.GUIView;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class GUIScoreBoardController extends GUIViewState{

    private static GUIScoreBoardController instance;
    public static GUIScoreBoardController getInstance() {
        synchronized (GUIScoreBoardController.class) {
            if (instance == null) {
                instance = new GUIScoreBoardController();
            }
            return instance;
        }


    }
    public GUIScoreBoardController() {
        try {
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(GUIScoreBoardController.class.getResource("/it/polimi/it/galaxytrucker/fxmlstages/scoreBoard.fxml")));
            loader.setController(this);
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void displayScene() {
        Platform.runLater(() -> {
            stage = (Stage) GUIView.stage.getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        });
    }
}
