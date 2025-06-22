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
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.*;

public class GUIActivateEngineController extends GUIViewState {

    private Map<String, ImageView> imageTiles = new HashMap<>();
    private List<List<Integer>> engineCoords = new ArrayList<>();
    private List<List<Integer>> batteryCoord = new ArrayList<>();
    private HashMap<List<Integer>,List<Integer>> engineAndBatteryCoord = new HashMap<>();

    @FXML
    private Label incorrectCoord1,incorrectCoord2,incorrectValue;

    @FXML private PlayerShipElementController shipController;

    private static GUIActivateEngineController instance;

    public static GUIActivateEngineController getInstance() {
        synchronized (GUIActivateEngineController.class) {
            if (instance == null) {
                instance = new GUIActivateEngineController();
            }
            return instance;
        }
    }

    public void updateShip(){
        shipController.displayShip();
    }

    public GUIActivateEngineController() {
        try {
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(GUIActivateEngineController.class.getResource("/it/polimi/it/galaxytrucker/fxmlstages/activateEngine.fxml")));
            loader.setController(this);
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void addEngine(){
        int col = shipController.selectedColumn;
        int row = shipController.selectedRow;
        if((row<5) || (row>9) || (col<4) || (col>10)){
            incorrectCoord1.setVisible(true);
        }
        if(GUIView.getInstance().getClient().getModel().getPlayerShips(GUIView.getInstance().getClient().getModel().getMyData().getPlayerId()).get(row).get(col).type().equals(DoubleEngine.class.getSimpleName())){
            engineCoords.add(List.of(row,col));
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
        if(!engineCoords.isEmpty() && !batteryCoord.isEmpty() && (engineCoords.size()==batteryCoord.size())){
            for(int i=0;i<engineCoords.size();i++){
                engineAndBatteryCoord.put(engineCoords.get(i),batteryCoord.get(i));
            }
            GUIView.getInstance().getClient().receiveUserInput(
                    new UserInput.UserInputBuilder(UserInputType.ACTIVATE_COMPONENT)
                            .setActivationHashmap(engineAndBatteryCoord)
                            .build()
            );
        } else{
            if(engineCoords.isEmpty() && batteryCoord.isEmpty() ) {
                engineAndBatteryCoord.put(List.of(), List.of());
                GUIView.getInstance().getClient().receiveUserInput(
                        new UserInput.UserInputBuilder(UserInputType.ACTIVATE_COMPONENT)
                                .setActivationHashmap(engineAndBatteryCoord)
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
