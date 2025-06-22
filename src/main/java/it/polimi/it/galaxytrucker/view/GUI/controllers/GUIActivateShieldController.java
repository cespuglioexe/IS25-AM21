package it.polimi.it.galaxytrucker.view.GUI.controllers;

import it.polimi.it.galaxytrucker.messages.clientmessages.UserInput;
import it.polimi.it.galaxytrucker.messages.clientmessages.UserInputType;
import it.polimi.it.galaxytrucker.model.componenttiles.*;
import it.polimi.it.galaxytrucker.view.GUI.GUIView;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.*;

public class GUIActivateShieldController extends GUIViewState{

    private Map<String, ImageView> imageTiles = new HashMap<>();
    private List<List<Integer>> shieldCoords = new ArrayList<>();
    private List<List<Integer>> batteryCoord = new ArrayList<>();
    private HashMap<List<Integer>,List<Integer>> shieldAndBatteryCoord = new HashMap<>();

    @FXML
    private Label incorrectCoord1,incorrectCoord2,incorrectValue;

    @FXML private PlayerShipElementController shipController;

    private static GUIActivateShieldController instance;

    public static GUIActivateShieldController getInstance() {
        synchronized (GUIActivateEngineController.class) {
            if (instance == null) {
                instance = new GUIActivateShieldController();
            }
            return instance;
        }
    }

    public void updateShip(){
        shipController.displayShip();
    }

    public GUIActivateShieldController() {
        try {
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(GUIActivateShieldController.class.getResource("/it/polimi/it/galaxytrucker/fxmlstages/activateShield.fxml")));
            loader.setController(this);
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void addShield(){
        int col = shipController.selectedColumn;
        int row = shipController.selectedRow;
        if((row<5) || (row>9) || (col<4) || (col>10)){
            incorrectCoord1.setVisible(true);
        }
        if(GUIView.getInstance().getClient().getModel().getPlayerShips(GUIView.getInstance().getClient().getModel().getMyData().getPlayerId()).get(row).get(col).type().equals(Shield.class.getSimpleName())){
            shieldCoords.add(List.of(row,col));
            incorrectCoord1.setVisible(false);
        } else incorrectCoord1.setVisible(true);
    }

    @FXML
    private void addBattery(){
        int col = shipController.selectedColumn;
        int row = shipController.selectedRow;
        if((row<5) || (row>9) || (col<4) || (col>10)){
            incorrectCoord1.setVisible(true);
        }
        if(GUIView.getInstance().getClient().getModel().getPlayerShips(GUIView.getInstance().getClient().getModel().getMyData().getPlayerId()).get(row).get(col).type().equals(BatteryComponent.class.getSimpleName())){
            batteryCoord.add(List.of(row,col));
            incorrectCoord1.setVisible(false);
        } else incorrectCoord1.setVisible(true);
    }

    public void resetCoord(){
        incorrectCoord1.setVisible(false);
        incorrectCoord2.setVisible(false);
        incorrectValue.setVisible(true);
        for(String key:imageTiles.keySet()){
            imageTiles.get(key).setStyle("");
        }
    }

    @FXML
    private void activateEngine(){
        if(!shieldCoords.isEmpty() && !batteryCoord.isEmpty() && (shieldCoords.size()==batteryCoord.size())){
            for(int i=0;i<shieldCoords.size();i++){
                shieldAndBatteryCoord.put(shieldCoords.get(i),batteryCoord.get(i));
            }
            GUIView.getInstance().getClient().receiveUserInput(
                    new UserInput.UserInputBuilder(UserInputType.ACTIVATE_COMPONENT)
                            .setActivationHashmap(shieldAndBatteryCoord)
                            .build()
            );
        } else{
            if(shieldCoords.isEmpty() && batteryCoord.isEmpty() ) {
                shieldAndBatteryCoord.put(List.of(), List.of());
                GUIView.getInstance().getClient().receiveUserInput(
                        new UserInput.UserInputBuilder(UserInputType.ACTIVATE_COMPONENT)
                                .setActivationHashmap(shieldAndBatteryCoord)
                                .build()
                );
            }
            else resetCoord();
        }
    }

    @Override
    public void displayScene() {
        Platform.runLater(() -> {
            stage = (Stage) GUIView.stage.getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

            updateShip();
        });
    }
}
