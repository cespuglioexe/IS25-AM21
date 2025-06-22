package it.polimi.it.galaxytrucker.view.GUI.controllers;

import it.polimi.it.galaxytrucker.view.GUI.GUIView;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class GUIParticipationPlanetController extends GUIViewState{

    private static GUIParticipationPlanetController instance;

    public static GUIParticipationPlanetController getInstance() {
        synchronized (GUIParticipationPlanetController.class) {
            if (instance == null) {
                instance = new GUIParticipationPlanetController();
            }
            return instance;
        }
    }

    public GUIParticipationPlanetController() {
        try {
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(GUIParticipationPlanetController.class.getResource("/it/polimi/it/galaxytrucker/fxmlstages/participationPlanetChoice.fxml")));
            loader.setController(this);
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void acceptPlanet1(){
    /*    GUIView.getInstance().getClient().receiveUserInput(
                new UserInput.UserInputBuilder(UserInputType.CARGO_REWARD)
                        .set(true)
                        .build()
        );*/
    }

    @FXML
    private void acceptPlanet2(){
    /*    GUIView.getInstance().getClient().receiveUserInput(
                new UserInput.UserInputBuilder(UserInputType.CARGO_REWARD)
                        .set(true)
                        .build()
        );*/
    }

    @FXML
    private void acceptPlanet3(){
    /*    GUIView.getInstance().getClient().receiveUserInput(
                new UserInput.UserInputBuilder(UserInputType.CARGO_REWARD)
                        .set(true)
                        .build()
        );*/
    }

    @FXML
    private void acceptPlanet4(){
    /*    GUIView.getInstance().getClient().receiveUserInput(
                new UserInput.UserInputBuilder(UserInputType.CARGO_REWARD)
                        .set(true)
                        .build()
        );*/
    }

    @FXML
    private void declinePlanet(){
    /*    GUIView.getInstance().getClient().receiveUserInput(
                new UserInput.UserInputBuilder(UserInputType.CARGO_REWARD)
                        .set(true)
                        .build()
        );*/
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
