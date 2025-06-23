package it.polimi.it.galaxytrucker.view.GUI.controllers;

import it.polimi.it.galaxytrucker.model.managers.FlightBoard;
import it.polimi.it.galaxytrucker.view.GUI.GUIView;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.ImageCursor;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class GUIGameTurn extends GUIViewState{


    private FlightBoard flightBoard;
    private static GUIGameTurn instance;
    @FXML private PlayerShipElementController shipController;
    @FXML private ImageView activeCard;
    @FXML private ImageView p00,p01,p02,p03,p04,p05,p06,p07,p08,p09,p010,p011,p012,p013,p014,p015,p016,p017;
    @FXML private Label creditsCount,engineCount;
    @FXML private Pane level1board,level2board;

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
            if (GUIView.getInstance().getClient().getModel().getGameLevel() == 1){
                level1board.setVisible(true);
            }else {
                level2board.setVisible(true);
            }

            activeCard.setImage(new Image(Objects.requireNonNull(GUIFixingShipController.class.getResourceAsStream("/it/polimi/it/galaxytrucker/graphics/cards/" + GUIView.getInstance().getClient().getModel().getActiveCardGraphicPath()))));
            creditsCount.setText(String.valueOf(GUIView.getInstance().getClient().getModel().getCredits()));
            stage.show();

        });
    }

    public void updateBoard(){

    }


    @FXML
    private void viewFlightBoard() {

    }


}
