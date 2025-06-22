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

public class GUICrewmatePenaltyController extends GUIViewState{

    private Map<String, ImageView> imageTiles = new HashMap<>();
    private List<List<Integer>> crewmateCoord = new ArrayList<>();
    int numberOfCrewmates;

    @FXML
    private Label incorrectCoord1,incorrectValue;

    @FXML private PlayerShipElementController shipController;


    private static GUICrewmatePenaltyController instance;

    public static GUICrewmatePenaltyController getInstance() {
        synchronized (GUICrewmatePenaltyController.class) {
            if (instance == null) {
                instance = new GUICrewmatePenaltyController();
            }
            return instance;
        }
    }

    public void updateShip(){
        shipController.displayShip();
    }

    public GUICrewmatePenaltyController() {
        try {
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(GUICrewmatePenaltyController.class.getResource("/it/polimi/it/galaxytrucker/fxmlstages/crewmatePenalty.fxml")));
            loader.setController(this);
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void removeCrewmate(){
        int col = shipController.selectedColumn;
        int row = shipController.selectedRow;
        if((row<5) || (row>9) || (col<4) || (col>10)){
            incorrectCoord1.setVisible(true);
        }
        if(GUIView.getInstance().getClient().getModel().getPlayerShips(GUIView.getInstance().getClient().getModel().getMyData().getPlayerId()).get(row).get(col).type().equals(CabinModule.class.getSimpleName())){
            crewmateCoord.add(List.of(row,col));
            incorrectCoord1.setVisible(false);
            numberOfCrewmates--;
        } else{
            if(GUIView.getInstance().getClient().getModel().getPlayerShips(GUIView.getInstance().getClient().getModel().getMyData().getPlayerId()).get(row).get(col).type().equals(CentralCabin.class.getSimpleName())) {
                crewmateCoord.add(List.of(row, col));
                incorrectCoord1.setVisible(false);
                numberOfCrewmates--;
            } else incorrectCoord1.setVisible(true);
        }
    }

    @FXML
    private void applyPenalty(){
     /*  if(numberOfCrewmates==0){
           GUIView.getInstance().getClient().receiveUserInput(
                   new UserInput.UserInputBuilder(UserInputType.ACTIVATE_COMPONENT)
                           .setActivationHashmap(cannonAndBatteryCoord)
                           .build()
           );
       }else incorrectValue.setVisible(true);*/
    }


    @Override
    public void displayScene() {
        Platform.runLater(() -> {
            stage = (Stage) GUIView.stage.getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

            updateShip();
            numberOfCrewmates = crewmateCoord.size();
        });
    }
}
