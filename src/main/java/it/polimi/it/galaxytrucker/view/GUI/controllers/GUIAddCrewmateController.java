package it.polimi.it.galaxytrucker.view.GUI.controllers;

import it.polimi.it.galaxytrucker.messages.clientmessages.UserInput;
import it.polimi.it.galaxytrucker.messages.clientmessages.UserInputType;
import it.polimi.it.galaxytrucker.model.componenttiles.TileData;
import it.polimi.it.galaxytrucker.model.componenttiles.TileEdge;
import it.polimi.it.galaxytrucker.model.utility.AlienType;
import it.polimi.it.galaxytrucker.model.utility.Coordinates;
import it.polimi.it.galaxytrucker.view.GUI.GUIView;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.*;

public class GUIAddCrewmateController extends GUIViewState{

    private static GUIAddCrewmateController instance;
    @FXML private VBox leftColumn;
    @FXML private Label errorLabel;
    @FXML private Button brownAliensButton, purpleAliensButton;
    @FXML private PlayerShipElementController shipController;

    private int totalBrownAliens = 0, totalPurpleAliens = 0;
    private List<TileData> tilesToFill = new ArrayList<>();

    public static GUIAddCrewmateController getInstance() {
        synchronized (GUIAddCrewmateController.class) {
            if (instance == null) {
                instance = new GUIAddCrewmateController();
            }
            return instance;
        }
    }

    public GUIAddCrewmateController() {
        try {
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(GUIAddCrewmateController.class.getResource("/it/polimi/it/galaxytrucker/fxmlstages/addCrewmate.fxml")));
            loader.setController(this);
            root = loader.load();

            leftColumn.maxHeightProperty().bind(shipController.shipBgImage.fitHeightProperty());
            shipController.setHighlightPredicate(tile -> tile != null && tile.type().equals("CabinModule") && !tile.crewmates().isEmpty());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void displayScene() {
        List<List<TileData>> ship = GUIView.getInstance().getClient().getModel().getPlayerShips(GUIView.getInstance().getClient().getModel().getMyData().getPlayerId());
        for (List<TileData> row : ship) {
            for (TileData tileData : row) {
                if (tileData != null && tileData.type().equals("CabinModule")) tilesToFill.add(tileData);
            }
        }

        Platform.runLater(() -> {
            stage = (Stage) GUIView.stage.getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            updateShip();
        });
    }

    @FXML
    private void addHuman(){
        if (placeCrewmates("human")) {
            HashMap<String, Coordinates> map = new HashMap<>();
            map.put("HUMAN", new Coordinates(shipController.selectedColumn, shipController.selectedRow));

            GUIView.getInstance().getClient().receiveUserInput(
                    new UserInput.UserInputBuilder(UserInputType.PLACE_CREWMATE)
                            .setPlacedCrewmate(map)
                            .build()
            );

            GUIView.getInstance().getClient().receiveUserInput(
                    new UserInput.UserInputBuilder(UserInputType.PLACE_CREWMATE)
                            .setPlacedCrewmate(map)
                            .build()
            );
        }
    }

    @FXML
    public void addBrownAlien(){
        if (placeCrewmates("brownAlien")) {
            totalBrownAliens++;

            HashMap<String, Coordinates> map = new HashMap<>();
            map.put(AlienType.BROWNALIEN.toString(), new Coordinates(shipController.selectedColumn, shipController.selectedRow));

            GUIView.getInstance().getClient().receiveUserInput(
                    new UserInput.UserInputBuilder(UserInputType.PLACE_CREWMATE)
                            .setPlacedCrewmate(map)
                            .build()
            );
        }

        if(totalBrownAliens > 0) {
            brownAliensButton.setDisable(true);
        }
    }

    @FXML
    public void addPurpleAlien(){
        if (placeCrewmates("purpleAlien")) {
            totalPurpleAliens++;

            HashMap<String, Coordinates> map = new HashMap<>();
            map.put(AlienType.PURPLEALIEN.toString(), new Coordinates(shipController.selectedColumn, shipController.selectedRow));

            GUIView.getInstance().getClient().receiveUserInput(
                    new UserInput.UserInputBuilder(UserInputType.PLACE_CREWMATE)
                            .setPlacedCrewmate(map)
                            .build()
            );
        }

        if(totalPurpleAliens > 0){
            purpleAliensButton.setDisable(true);
        }
    }

    private boolean placeCrewmates(String type){
        errorLabel.setVisible(false);

        List<List<TileData>> ship = GUIView.getInstance().getClient().getModel().getPlayerShips(GUIView.getInstance().getClient().getModel().getMyData().getPlayerId());
        TileData tile = ship.get(shipController.selectedRow - 5).get(shipController.selectedColumn - 4);

        if(tile == null || (!tile.type().equals("CabinModule") && !tile.type().equals("CentralCabin"))) {
            errorLabel.setText("You can only add a crewmates to a cabin module");
            errorLabel.setVisible(true);
            return false;
        }

        if (tile.crewmates().contains("PURPLEALIEN") || tile.crewmates().contains("BROWNALIEN") || tile.crewmates().size() >= 2 || tile.type().equals("CentralCabin")) {
            errorLabel.setText("This cabin is already full!");
            errorLabel.setVisible(true);
            return false;
        }
        
        if (type.equals("human")) {
            tilesToFill.remove(tile);
            return true;
        }

        boolean hasBrownLifeSupport = false;
        boolean hasPurpleLifeSupport = false;

        int row = shipController.selectedRow - 5;
        int col = shipController.selectedColumn - 4;
        int maxRows = ship.size();
        int maxCols = ship.get(0).size();

        // UP
        if (row > 0) {
            TileData tempTile = ship.get(row - 1).get(col);
            if (tempTile != null && !tempTile.type().equals("OutOfBoundsTile")) {
                hasBrownLifeSupport |= tempTile.type().equals("LifeSupport") && tempTile.alienSupport().equals("BROWNALIEN") && tempTile.bottom().isCompatible(tile.top()) && TileEdge.isAConnector(tile.top());
                hasPurpleLifeSupport |= tempTile.type().equals("LifeSupport") && tempTile.alienSupport().equals("PURPLEALIEN") && tempTile.bottom().isCompatible(tile.top()) && TileEdge.isAConnector(tile.top());
            }
        }

        // DOWN
        if (row < maxRows - 1) {
            TileData tempTile = ship.get(row + 1).get(col);
            if (tempTile != null && !tempTile.type().equals("OutOfBoundsTile")) {
                hasBrownLifeSupport |= tempTile.type().equals("LifeSupport") && tempTile.alienSupport().equals("BROWNALIEN") && tempTile.top().isCompatible(tile.bottom()) && TileEdge.isAConnector(tile.bottom());
                hasPurpleLifeSupport |= tempTile.type().equals("LifeSupport") && tempTile.alienSupport().equals("PURPLEALIEN") && tempTile.top().isCompatible(tile.bottom()) && TileEdge.isAConnector(tile.bottom());
            }
        }

        // LEFT
        if (col > 0) {
            TileData tempTile = ship.get(row).get(col - 1);
            if (tempTile != null && !tempTile.type().equals("OutOfBoundsTile")) {
                hasBrownLifeSupport |= tempTile.type().equals("LifeSupport") && tempTile.alienSupport().equals("BROWNALIEN") && tempTile.right().isCompatible(tile.left()) && TileEdge.isAConnector(tile.left());
                hasPurpleLifeSupport |= tempTile.type().equals("LifeSupport") && tempTile.alienSupport().equals("PURPLEALIEN") && tempTile.right().isCompatible(tile.left()) && TileEdge.isAConnector(tile.left());
            }
        }

        // RIGHT
        if (col < maxCols - 1) {
            TileData tempTile = ship.get(row).get(col + 1);
            if (tempTile != null && !tempTile.type().equals("OutOfBoundsTile")) {
                hasBrownLifeSupport |= tempTile.type().equals("LifeSupport") && tempTile.alienSupport().equals("BROWNALIEN") && tempTile.left().isCompatible(tile.right()) && TileEdge.isAConnector(tile.right());
                hasPurpleLifeSupport |= tempTile.type().equals("LifeSupport") && tempTile.alienSupport().equals("PURPLEALIEN") && tempTile.left().isCompatible(tile.right()) && TileEdge.isAConnector(tile.right());
            }
        }

        if ((type.equals("brownAlien") && !hasBrownLifeSupport) || (type.equals("purpleAlien") && !hasPurpleLifeSupport)) {
            errorLabel.setText("Aliens need matching life support to be added");
            errorLabel.setVisible(true);
            return false;
        }

        tilesToFill.remove(tile);
        return true;
    }

    @FXML
    private void endBuilding(){
        if (!tilesToFill.isEmpty()) {
            errorLabel.setText("You still have cabins to fill!");
            errorLabel.setVisible(true);
        } else {
            GUILoadingViewController.getInstance().displayScene();
            
            GUIView.getInstance().getClient().receiveUserInput(
                    new UserInput.UserInputBuilder(UserInputType.CONFIRM_BUILDING_END)
                            .build()
            );
        }
    }

    public void updateShip(){
        shipController.displayShip();
    }

}
