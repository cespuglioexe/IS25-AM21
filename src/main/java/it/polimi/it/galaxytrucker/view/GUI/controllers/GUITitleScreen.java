package it.polimi.it.galaxytrucker.view.GUI.controllers;

import it.polimi.it.galaxytrucker.view.GUI.GUIView;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class GUITitleScreen extends GUIViewState {

    private static GUITitleScreen instance;

    @FXML public ImageView backgroundImage;
    @FXML public ImageView titleImage;

    public static GUITitleScreen getInstance() {
        if (instance == null) {
            instance = new GUITitleScreen();
        }
        return instance;
    }

    private GUITitleScreen() {
        try {
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(GUITitleScreen.class.getResource("/it/polimi/it/galaxytrucker/fxmlstages/titleScreen.fxml")));
            loader.setController(this);
            root = loader.load();
            backgroundImage.setImage(new Image(Objects.requireNonNull(GUITitleScreen.class.getResourceAsStream("/it/polimi/it/galaxytrucker/graphics/general/background.png"))));
            titleImage.setImage(new Image(Objects.requireNonNull(GUITitleScreen.class.getResourceAsStream("/it/polimi/it/galaxytrucker/graphics/general/title.png"))));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void displayScene() {
        Platform.runLater(() -> {
            stage = (Stage) GUIView.stage.getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);

            scene.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.ENTER) {
                    GUIView.getGUIView().nameSelectionScene();
                }
            });

            stage.show();
        });

    }
}