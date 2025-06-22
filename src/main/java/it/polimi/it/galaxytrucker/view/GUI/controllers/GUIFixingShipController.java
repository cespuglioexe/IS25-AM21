package it.polimi.it.galaxytrucker.view.GUI.controllers;

import it.polimi.it.galaxytrucker.messages.clientmessages.UserInput;
import it.polimi.it.galaxytrucker.messages.clientmessages.UserInputType;
import it.polimi.it.galaxytrucker.model.componenttiles.OutOfBoundsTile;
import it.polimi.it.galaxytrucker.model.componenttiles.TileData;
import it.polimi.it.galaxytrucker.view.GUI.GUIView;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class GUIFixingShipController extends GUIViewState {

    private static GUIFixingShipController instance;
    @FXML private PlayerShipElementController shipController;

    public static GUIFixingShipController getInstance() {
        synchronized (GUIFixingShipController.class) {
            if (instance == null) {
                instance = new GUIFixingShipController();
            }
            return instance;
        }
    }

    public GUIFixingShipController() {
        try {
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(GUIFixingShipController.class.getResource("/it/polimi/it/galaxytrucker/fxmlstages/fixingShip.fxml")));
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

            shipController.displayShip();
        });
    }

    public void updateShip() {
        System.out.println("updateShip");
        shipController.displayShip();
    }

    @FXML
    public void removeSelectedTile() {
        int column = shipController.selectedColumn;
        int row = shipController.selectedRow;

        System.out.println("Removing: column " + column + ", row " + row);

        if (column != -1 && row != -1) {
            GUIView.getInstance().getClient().receiveUserInput(
                    new UserInput.UserInputBuilder(UserInputType.REMOVE_COMPONENT)
                            .setCoords(column, row)
                            .build()
            );
        }

        shipController.resetSelectedTile();
    }

    public void endFixing() {
        GUIView.getInstance().getClient().receiveUserInput(
                new UserInput.UserInputBuilder(UserInputType.CONFIRM_BUILDING_END)
                        .build()
        );
    }
}


