package it.polimi.it.galaxytrucker.view.GUI.controllers;

import it.polimi.it.galaxytrucker.commands.UserInput;
import it.polimi.it.galaxytrucker.commands.UserInputType;
import it.polimi.it.galaxytrucker.view.GUI.GUIView;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class GUIUsernameSelection extends GUIViewState {

    @FXML public TextField namefield;
    @FXML public Button checkUsername;
    @FXML public Label usernameError;
    @FXML public Label usernameSuccess;

    private static GUIUsernameSelection Instance;

    public static GUIUsernameSelection getInstance() {
        synchronized (GUIUsernameSelection.class) {
            if (Instance == null) {
                Instance = new GUIUsernameSelection();
            }
            return Instance;
        }
    }

    private GUIUsernameSelection() {
        try {
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(GUITitleScreen.class.getResource("/it/polimi/it/galaxytrucker/fxmlstages/usernameSelection.fxml")));
            loader.setController(this);
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //---------------------------------------------------------//


    @FXML
    public void submitUsername(){
        GUIView.getInstance().getClient().receiveUserInput(
                new UserInput.UserInputBuilder(UserInputType.SET_PLAYER_USERNAME)
                        .setPlayerName(namefield.getText())
                        .build());
    }

    public void nameError(){
        Platform.runLater(() -> usernameError.setVisible(true));
    }

    public void nameSelectionSuccess() {
        Platform.runLater(() -> usernameSuccess.setVisible(true));
        GUIView.getInstance().gameSelectionScreen();
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
