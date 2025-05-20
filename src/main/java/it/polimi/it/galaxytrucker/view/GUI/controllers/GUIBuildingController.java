package it.polimi.it.galaxytrucker.view.GUI.controllers;

import it.polimi.it.galaxytrucker.commands.UserInput;
import it.polimi.it.galaxytrucker.commands.UserInputType;
import it.polimi.it.galaxytrucker.model.componenttiles.TileData;
import it.polimi.it.galaxytrucker.networking.client.clientmodel.ClientModel;
import it.polimi.it.galaxytrucker.view.GUI.GUIView;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class GUIBuildingController extends GUIViewState {

    @FXML
    private TextField xCoordText,yCoordText, rotationInput;
    @FXML
    private Pane displayShipPane;
    @FXML
    private Label timerSeconds,showTile;
    private static GUIBuildingController instance;

    public static GUIBuildingController getInstance() {
        synchronized (GUIUsernameSelection.class) {
            if (instance == null) {
                instance = new GUIBuildingController();
            }
            return instance;
        }
    }

    public GUIBuildingController() {
       try {
           FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(GUITitleScreen.class.getResource("/it/polimi/it/galaxytrucker/fxmlstages/buildingPhase.fxml")));
           loader.setController(this);
           root = loader.load();
       } catch (IOException e) {
           e.printStackTrace();
       }
    }

    public void initialize() {
        startTimer();
    }


    @FXML
    public void newRandomTile(){
        GUIView.getInstance().getClient().receiveUserInput(
                new UserInput.UserInputBuilder(UserInputType.SELECT_RANDOM_COMPONENT)
                        .build()
        );


    }

    @FXML
    public void placeTile(){
        int x,y,rotation;
        x=Integer.parseInt(xCoordText.getText());
        y=Integer.parseInt(yCoordText.getText());
        rotation = Integer.parseInt(rotationInput.getText());

        GUIView.getInstance().getClient().receiveUserInput(
                new UserInput.UserInputBuilder(UserInputType.PLACE_COMPONENT)
                        .setCoords(x, y)
                        .setRotation(rotation)
                        .build()
        );
        System.out.println("tile placed");
    }

    @FXML
    public void displayShip(){
        displayShipPane.setVisible(true);
        ClientModel model = GUIView.getInstance().getClient().getModel();
        GUIView.getInstance().displayShip(model.getPlayerShips(model.getMyData().getPlayerId()));
    }

    @FXML
    public void closeDisplayShip(){
        displayShipPane.setVisible(false);
    }

    @FXML
    public void saveTile(){
        GUIView.getInstance().getClient().receiveUserInput(
                new UserInput.UserInputBuilder(UserInputType.SAVE_SELECTED_COMPONENT)
                        .build()
        );

        System.out.println("tile saved");
    }

    @FXML
    public void discardTile(){
        GUIView.getInstance().getClient().receiveUserInput(
                new UserInput.UserInputBuilder(UserInputType.DISCARD_SELECTED_COMPONENT)
                        .build()
        );
        System.out.println("tile discarded");
    }

    @FXML
    public void startTimer() {
        if (!GUIView.getInstance().getClient().isBuildingTimerIsActive()) {
            GUIView.getInstance().getClient().receiveUserInput(
                    new UserInput.UserInputBuilder(UserInputType.RESTART_BUILDING_TIMER)
                            .build()
            );
        }
    }


    public void showSavedTile(){
        ClientModel model = GUIView.getInstance().getClient().getModel();
        List<TileData> savedTiles = model.getSavedTiles();
        GUIView.getInstance().displayTiles(savedTiles);
        showTile.setText(savedTiles.toString());
    }


    public void showDiscardedTile(){
        ClientModel model = GUIView.getInstance().getClient().getModel();
        List<TileData> discardedTiles = model.getDiscardedTiles();
        GUIView.getInstance().displayTiles(discardedTiles);
        showTile.setText(discardedTiles.toString());
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
