package it.polimi.it.galaxytrucker.view.GUI.controllers;

import it.polimi.it.galaxytrucker.commands.RequestType;
import it.polimi.it.galaxytrucker.commands.UserInput;
import it.polimi.it.galaxytrucker.commands.UserInputType;
import it.polimi.it.galaxytrucker.model.componenttiles.TileData;
import it.polimi.it.galaxytrucker.networking.VirtualClient;
import it.polimi.it.galaxytrucker.networking.client.clientmodel.ClientModel;
import it.polimi.it.galaxytrucker.view.GUI.GUIView;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import java.util.List;

public class BuildingController {


    private static BuildingController instance;
    private int seconds = 0;

    @FXML
    private TextField xCoordText,yCoordText, rotationInput;
    @FXML
    private Pane displayShipPane;
    @FXML
    private Label timerSeconds,showTile;

    public static BuildingController getInstance() {
        System.out.println("ConnectionController get instance");
        synchronized (SetUsernameController.class) {
            if (instance == null) {
                instance = new BuildingController();
            }
            return instance;
        }
    }

    public void initialize() {
        startTimer();
    }


    @FXML
    public void newRandomTile(){
        GUIView.getGUIView().getClient().receiveUserInput(
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

        GUIView.getGUIView().getClient().receiveUserInput(
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
        ClientModel model = GUIView.getGUIView().getClient().getModel();
        GUIView.getGUIView().displayShip(model.getPlayerShips(model.getMyData().getPlayerId()));
    }

    @FXML
    public void closeDisplayShip(){
        displayShipPane.setVisible(false);
    }

    @FXML
    public void saveTile(){
        GUIView.getGUIView().getClient().receiveUserInput(
                new UserInput.UserInputBuilder(UserInputType.SAVE_SELECTED_COMPONENT)
                        .build()
        );

        System.out.println("tile saved");
    }

    @FXML
    public void discardTile(){
        GUIView.getGUIView().getClient().receiveUserInput(
                new UserInput.UserInputBuilder(UserInputType.DISCARD_SELECTED_COMPONENT)
                        .build()
        );
        System.out.println("tile discarded");
    }

    @FXML
    public void startTimer() {
        if (!GUIView.getGUIView().getClient().isBuildingTimerIsActive()) {
            GUIView.getGUIView().getClient().receiveUserInput(
                    new UserInput.UserInputBuilder(UserInputType.RESTART_BUILDING_TIMER)
                            .build()
            );
        }
    }


    public void showSavedTile(){
        ClientModel model = GUIView.getGUIView().getClient().getModel();
        List<TileData> savedTiles = model.getSavedTiles();
        GUIView.getGUIView().displayTiles(savedTiles);
        showTile.setText(savedTiles.toString());
    }


    public void showDiscardedTile(){
        ClientModel model = GUIView.getGUIView().getClient().getModel();
        List<TileData> discardedTiles = model.getDiscardedTiles();
        GUIView.getGUIView().displayTiles(discardedTiles);
        showTile.setText(discardedTiles.toString());
    }




}
