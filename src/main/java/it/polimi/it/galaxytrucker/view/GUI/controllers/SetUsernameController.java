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
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class SetUsernameController extends GUIViewState {

    @FXML public TextField namefield;
    @FXML public Button checkUsername;

    //NOT USED
    private static SetUsernameController Instance;

    public static SetUsernameController getInstance() {
        synchronized (SetUsernameController.class) {
            if (Instance == null) {
                Instance = new SetUsernameController();
            }
            return Instance;
        }
    }

    private SetUsernameController() {
        try {
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(GUITitleScreen.class.getResource("/view/setUsernameScene.fxml")));
            loader.setController(this);
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //---------------------------------------------------------//


    @FXML
    public void checkUsernameFunction(){
        System.out.println("Username: " + namefield.getText());
        System.out.println("View.getClient: "+ GUIView.getGUIView().getClient());
        GUIView.getGUIView().getClient().receiveUserInput(
                new UserInput.UserInputBuilder(UserInputType.SET_PLAYER_USERNAME)
                        .setPlayerName(namefield.getText())
                        .build());

        if(!GUIView.getGUIView().getNameIsCorrectCheck()){
            nameError();
        }

        System.out.println("End username");
    }

    @FXML
    private Label usernameError;

    @FXML
    public void nameError(){
        usernameError.setVisible(true);
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
