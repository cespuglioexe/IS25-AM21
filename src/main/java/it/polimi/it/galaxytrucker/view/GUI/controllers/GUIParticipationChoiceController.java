package it.polimi.it.galaxytrucker.view.GUI.controllers;

import it.polimi.it.galaxytrucker.messages.clientmessages.UserInput;
import it.polimi.it.galaxytrucker.messages.clientmessages.UserInputType;
import it.polimi.it.galaxytrucker.view.GUI.GUIView;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class GUIParticipationChoiceController extends GUIViewState{

    private static GUIParticipationChoiceController instance;

    public static GUIParticipationChoiceController getInstance() {
        synchronized (GUIParticipationChoiceController.class) {
            if (instance == null) {
                instance = new GUIParticipationChoiceController();
            }
            return instance;
        }
    }

    public GUIParticipationChoiceController() {
        try {
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(GUIParticipationChoiceController.class.getResource("/it/polimi/it/galaxytrucker/fxmlstages/participationChoice.fxml")));
            loader.setController(this);
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void declineParticipation(){
        GUIView.getInstance().getClient().receiveUserInput(
                new UserInput.UserInputBuilder(UserInputType.PARTICIPATION)
                        .setParticipation(false)
                        .setParticipationChoice(-1)
                        .build()
        );
    }

    @FXML
    private void acceptParticipation(){
        GUIView.getInstance().getClient().receiveUserInput(
                new UserInput.UserInputBuilder(UserInputType.PARTICIPATION)
                        .setParticipation(true)
                        .setParticipationChoice(0)
                        .build()
        );
    }

    @Override
    public void displayScene() {
        Platform.runLater(() -> {
            try {
                FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(
                        GUIParticipationChoiceController.class.getResource("/it/polimi/it/galaxytrucker/fxmlstages/participationChoice.fxml")
                ));
                loader.setController(this);
                Parent newRoot = loader.load();

                stage = (Stage) GUIView.stage.getScene().getWindow();
                scene = new Scene(newRoot);
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }

        });
    }
}
