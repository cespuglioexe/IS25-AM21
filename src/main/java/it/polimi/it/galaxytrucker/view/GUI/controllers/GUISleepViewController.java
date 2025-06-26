package it.polimi.it.galaxytrucker.view.GUI.controllers;

import it.polimi.it.galaxytrucker.view.GUI.GUIView;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class GUISleepViewController extends GUIViewState {

    @FXML
    private ImageView activeCard;
    private static GUISleepViewController instance;
    public static GUISleepViewController getInstance() {
        synchronized (GUISleepViewController.class) {
            if (instance == null) {
                instance = new GUISleepViewController();
            }
            return instance;
        }


    }
    public GUISleepViewController() {
        try {
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(GUISleepViewController.class.getResource("/it/polimi/it/galaxytrucker/fxmlstages/sleepView.fxml")));
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
                        GUIFixingShipController.class.getResource("/it/polimi/it/galaxytrucker/fxmlstages/sleepView.fxml")
                ));
                loader.setController(this);
                Parent newRoot = loader.load();

                activeCard.setImage(new Image(Objects.requireNonNull(GUIGameTurn.class.getResourceAsStream("/it/polimi/it/galaxytrucker/graphics/cards/" + GUIView.getInstance().getClient().getModel().getActiveCardGraphicPath()))));

                stage = (Stage) GUIView.stage.getScene().getWindow();
                scene = new Scene(newRoot);
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
