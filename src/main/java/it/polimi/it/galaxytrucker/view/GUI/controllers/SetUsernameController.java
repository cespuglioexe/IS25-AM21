package it.polimi.it.galaxytrucker.view.GUI.controllers;

import it.polimi.it.galaxytrucker.commands.UserInput;
import it.polimi.it.galaxytrucker.commands.UserInputType;
import it.polimi.it.galaxytrucker.view.GUI.GUIView;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class SetUsernameController {

    @FXML
    private TextField namefield;

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
    //---------------------------------------------------------//


    @FXML
    public void checkUsernameFunction(){
        System.out.println("Username: " + namefield.getText());
        System.out.println("View.getClient: "+ GUIView.getGUIView().getClient());
        GUIView.getGUIView().getClient().receiveUserInput(
                new UserInput.UserInputBuilder(null, UserInputType.SET_USERNAME)
                        .setPlayerName(namefield.getText())
                        .build());

        if(!GUIView.getGUIView().getNameIsCorrectCheck()){
            nameError();
        }
    }

    @FXML
    private Label usernameError;

    @FXML
    public void nameError(){
        usernameError.setVisible(true);
    }

}
