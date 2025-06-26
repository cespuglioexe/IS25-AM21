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

public class GUILoadingViewController extends GUIViewState{

    private static GUILoadingViewController instance;
    @FXML
    private ImageView currentCard;

    public static GUILoadingViewController getInstance() {
        synchronized (GUILoadingViewController.class) {
            if (instance == null) {
                instance = new GUILoadingViewController();
            }
            return instance;
        }


    }
    public GUILoadingViewController() {
        try {
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(GUILoadingViewController.class.getResource("/it/polimi/it/galaxytrucker/fxmlstages/loadingView.fxml")));
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
                        GUIFixingShipController.class.getResource("/it/polimi/it/galaxytrucker/fxmlstages/loadingView.fxml")
                ));
                loader.setController(this);
                Parent newRoot = loader.load();

                stage = (Stage) GUIView.stage.getScene().getWindow();
                scene = new Scene(newRoot);
                stage.setScene(scene);
                currentCard.setImage(new Image(Objects.requireNonNull(GUILoadingViewController.class.getResourceAsStream("/it/polimi/it/galaxytrucker/graphics/cards/" + GUIView.getInstance().getClient().getModel().getActiveCardGraphicPath()))));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
