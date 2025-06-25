package it.polimi.it.galaxytrucker.view.GUI.controllers;

import it.polimi.it.galaxytrucker.messages.clientmessages.UserInput;
import it.polimi.it.galaxytrucker.messages.clientmessages.UserInputType;
import it.polimi.it.galaxytrucker.model.utility.Coordinates;
import it.polimi.it.galaxytrucker.view.GUI.GUIView;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.*;

public class GUIAddCrewmateController extends GUIViewState{

    private static GUIAddCrewmateController instance;
    @FXML
    private Label humanCounter,alienCounterBrown,alienCounterPurple,errorLabel;
    @FXML
    private Button alienButton1,alienButton2;
    @FXML private PlayerShipElementController shipController;


    private int hcounter=0, totAlienCounter=0, bacounter=0,pacounter=0,alienPlaced = 0;

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
            updateShip();
        });
    }


    @FXML
    public void addHuman(){
        if(hcounter<2){
            hcounter++;
            humanCounter.setText(String.valueOf(hcounter));
        }

    }



    @FXML
    public void addBrownAlien(){
        if(alienPlaced<3) {
            if (bacounter < 1) {
                bacounter++;
                alienCounterBrown.setText(String.valueOf(bacounter));
            }
        }
    }

    @FXML
    public void addPurpleAlien(){
        if(alienPlaced<3){
            if(pacounter<1){
                pacounter++;
                alienCounterPurple.setText(String.valueOf(pacounter));
            }
        }
    }

    @FXML
    public void removeHumans(){
        if(hcounter>0){
                hcounter--;
                humanCounter.setText(String.valueOf(hcounter));
            }
        }


    @FXML
    public void removePurpleAlien(){
         if(pacounter>0){
            pacounter--;
            alienCounterPurple.setText(String.valueOf(pacounter));
        }
    }

    @FXML
    public void removeBrownAlien(){
        if(bacounter>0){
            bacounter--;
            alienCounterBrown.setText(String.valueOf(bacounter));
        }
    }




    @FXML
    public void placeCrewmates(){
        errorLabel.setVisible(false);
        Coordinates c = new Coordinates(shipController.selectedRow +5,shipController.selectedColumn+4);
        String es = shipController.tileSelectedTypeString(shipController.selectedRow ,shipController.selectedColumn);
        System.out.println(es);
        List<Boolean> typeCrewmates = new ArrayList<>();
        Map<Coordinates,List<Boolean>> crewmates = new HashMap<>();

        pacounter = pacounter*2;
        bacounter =bacounter*2;

        int currentTotCounter = pacounter + bacounter + hcounter;
        System.out.println("Placing: "+currentTotCounter+" crewmates");

        System.out.println("Element selected: "+shipController.selectedRow + shipController.selectedColumn);
        System.out.println("Type: "+shipController.tileSelectedTypeString(shipController.selectedRow ,shipController.selectedColumn));

        System.out.println("Border 0: " + shipController.tileSelectedTypeString(shipController.selectedRow +1,shipController.selectedColumn));
        System.out.println("Border 1: " + shipController.tileSelectedTypeString(shipController.selectedRow,shipController.selectedColumn+1));
        System.out.println("Border 2: " + shipController.tileSelectedTypeString(shipController.selectedRow -1,shipController.selectedColumn));
        System.out.println("Border 3: " + shipController.tileSelectedTypeString(shipController.selectedRow ,shipController.selectedColumn-1));

        List<String> borders = new ArrayList<>();
        borders.add(shipController.tileSelectedTypeString(shipController.selectedRow +1,shipController.selectedColumn));
        borders.add(shipController.tileSelectedTypeString(shipController.selectedRow,shipController.selectedColumn+1));
        borders.add(shipController.tileSelectedTypeString(shipController.selectedRow -1,shipController.selectedColumn));
        borders.add(shipController.tileSelectedTypeString(shipController.selectedRow ,shipController.selectedColumn-1));


        if(es.equals("CabinModule") && currentTotCounter<3){
            for(int i = 0; i< hcounter; i++){
                typeCrewmates.add(true);
                crewmates.put(c, typeCrewmates);
                typeCrewmates.clear();
                errorLabel.setVisible(false);
            }
            alienPlaced = alienPlaced + pacounter + bacounter;
            if(alienPlaced<5 && (pacounter>0 || bacounter>0) ){
                for(String s : borders){
                    if(s.equals("LifeSupportPURPLEALIEN"))
                    {
                        for (int i=0;i<pacounter;i++){
                            typeCrewmates.add(false);
                            typeCrewmates.add(true);
                            crewmates.put(c, typeCrewmates);
                            typeCrewmates.clear();
                        }
                        alienCounterPurple.setText(String.valueOf(pacounter));

                    }else if(s.equals("LifeSupportBROWNALIEN")){
                        for (int i=0;i<bacounter;i++){
                            typeCrewmates.add(false);
                            typeCrewmates.add(false);
                            crewmates.put(c, typeCrewmates);
                            typeCrewmates.clear();
                        }
                        alienCounterBrown.setText(String.valueOf(bacounter));

                    }else{
                        errorLabel.setText("Aliens need a life support!!");
                        errorLabel.setVisible(true);
                    }
                }
            }

            System.out.println("List crewmates: " + crewmates);

        }else{
            errorLabel.setText("Cant place for some reason!!");
            errorLabel.setVisible(true);
        }

        System.out.println("Humans: " + hcounter);
        System.out.println("Pacounter: " + pacounter/2);
        System.out.println("bacounter: " + bacounter/2);

        hcounter = 0;
        bacounter = 0;
        pacounter = 0;
        currentTotCounter = 0;
        humanCounter.setText(String.valueOf(hcounter));
        alienCounterBrown.setText(String.valueOf(bacounter));
        alienCounterPurple.setText(String.valueOf(pacounter));
    }


    public void endBuilding(){
            GUIView.getInstance().getClient().receiveUserInput(
                    new UserInput.UserInputBuilder(UserInputType.CONFIRM_BUILDING_END)
                            .build()
            );
    }

    @FXML
    public void updateShip(){
        shipController.displayShip();
    }

}
