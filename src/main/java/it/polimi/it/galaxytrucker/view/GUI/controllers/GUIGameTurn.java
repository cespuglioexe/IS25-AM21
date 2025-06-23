package it.polimi.it.galaxytrucker.view.GUI.controllers;

import it.polimi.it.galaxytrucker.view.GUI.GUIView;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.ImageCursor;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class GUIGameTurn extends GUIViewState{

    private static GUIGameTurn instance;
    @FXML private PlayerShipElementController shipController;

    @FXML private ImageView activeCard;
    @FXML private ImageView p0,p1,p2,p3,p4,p5,p6,p7,p8,p9,p10,p11,p12,p13,p14,p15,p16,p17;

    public static GUIGameTurn getInstance() {
        synchronized (GUIGameTurn.class) {
            if (instance == null) {
                instance = new GUIGameTurn();
            }
            return instance;
        }
    }

    public GUIGameTurn() {
        try {
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(GUIGameTurn.class.getResource("/it/polimi/it/galaxytrucker/fxmlstages/gameTurn.fxml")));
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

            System.out.println(GUIView.getInstance().getClient().getModel().getActiveCardGraphicPath());

            activeCard.setImage(new Image(Objects.requireNonNull(GUIFixingShipController.class.getResourceAsStream("/it/polimi/it/galaxytrucker/graphics/cards/" + GUIView.getInstance().getClient().getModel().getActiveCardGraphicPath()))));

            stage.show();
        });
    }

    @FXML
    private void viewFlightBoard() {

    }


}
