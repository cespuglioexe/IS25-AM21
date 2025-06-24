package it.polimi.it.galaxytrucker.view.GUI.controllers;

import it.polimi.it.galaxytrucker.messages.clientmessages.UserInput;
import it.polimi.it.galaxytrucker.messages.clientmessages.UserInputType;
import it.polimi.it.galaxytrucker.model.componenttiles.*;
import it.polimi.it.galaxytrucker.model.utility.Coordinates;
import it.polimi.it.galaxytrucker.networking.client.clientmodel.ClientModel;
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

public class GUICrewmatePenaltyController extends GUIViewState implements GUIErrorHandler {

    private Map<String, ImageView> imageTiles = new HashMap<>();
    private List<Coordinates> crewmateCoord = new ArrayList<>();
    int numberOfCrewmates;
    int crewmatePenalty;

    @FXML
    private Label incorrectCoord1, incorrectValue, remainCrewmateLabel;

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
    public void initialize() {
        loadCardDetails();
        updateShip();
    }
    private void loadCardDetails() {
        ClientModel model = GUIView.getInstance().getClient().getModel();

        numberOfCrewmates = model.getCardDetail("crewmatePenalty", Integer.class);
        crewmatePenalty = numberOfCrewmates;
        updateCrewmatePenaltyCountGUI();
    }
    
    private void updateCrewmatePenaltyCountGUI() {
        remainCrewmateLabel.setText("SELECT " + numberOfCrewmates + " MORE CREWMATES");
    }

    @FXML
    private void removeCrewmate(){
        int col = shipController.selectedColumn;
        int row = shipController.selectedRow;
        if((row<5) || (row>9) || (col<4) || (col>10)){
            incorrectCoord1.setVisible(true);
        }else {
            if (GUIView.getInstance().getClient().getModel().getPlayerShips(GUIView.getInstance().getClient().getModel().getMyData().getPlayerId()).get(row - 5).get(col - 4) != null) {
                if (GUIView.getInstance().getClient().getModel().getPlayerShips(GUIView.getInstance().getClient().getModel().getMyData().getPlayerId()).get(row-5).get(col-4).type().equals(CabinModule.class.getSimpleName())) {
                    crewmateCoord.add(new Coordinates(col, row));
                    incorrectCoord1.setVisible(false);
                    numberOfCrewmates--;
                } else {
                    if (GUIView.getInstance().getClient().getModel().getPlayerShips(GUIView.getInstance().getClient().getModel().getMyData().getPlayerId()).get(row-5).get(col-4).type().equals(CentralCabin.class.getSimpleName())) {
                        crewmateCoord.add(new Coordinates(col, row));
                        incorrectCoord1.setVisible(false);
                        numberOfCrewmates--;
                    } else incorrectCoord1.setVisible(true);
                }
            }
        }
    }

    @FXML
    private void applyPenalty(){
        if(numberOfCrewmates != 0){
            for(int i=0;i<numberOfCrewmates;i++){
                crewmateCoord.add(new Coordinates(0, 0));
            }
        }
       GUIView.getInstance().getClient().receiveUserInput(
               new UserInput.UserInputBuilder(UserInputType.CREWMATE_PENALTY)
                       .setRemovedCrewmate(crewmateCoord)
                       .build()
       );
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

    @Override
    public void inputError() {
        incorrectValue.setVisible(true);
        numberOfCrewmates = crewmatePenalty;
        updateCrewmatePenaltyCountGUI();
    }
}
