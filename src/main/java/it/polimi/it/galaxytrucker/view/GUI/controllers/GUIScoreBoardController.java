package it.polimi.it.galaxytrucker.view.GUI.controllers;

import it.polimi.it.galaxytrucker.messages.clientmessages.UserInput;
import it.polimi.it.galaxytrucker.messages.clientmessages.UserInputType;
import it.polimi.it.galaxytrucker.model.managers.Player;
import it.polimi.it.galaxytrucker.view.GUI.GUIView;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class GUIScoreBoardController extends GUIViewState{

    @FXML
    private Label rank0,rank1,rank2,rank3,nick0,nick1,nick2,nick3,color0,color1,color2,color3;
    @FXML
    private GridPane scoreBoard;

    private static GUIScoreBoardController instance;
    public static GUIScoreBoardController getInstance() {
        synchronized (GUIScoreBoardController.class) {
            if (instance == null) {
                instance = new GUIScoreBoardController();
            }
            return instance;
        }


    }
    public GUIScoreBoardController() {
        try {
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(GUIScoreBoardController.class.getResource("/it/polimi/it/galaxytrucker/fxmlstages/scoreBoard.fxml")));
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
        });
    }

    @FXML
    public void updateScoreboard(){
        nick0.setText(GUIView.getInstance().getClient().getModel().getMyData().getNickname());

    }

    public void endAddCrewmates(){
        GUIView.getInstance().getClient().receiveUserInput(
                new UserInput.UserInputBuilder(UserInputType.CONFIRM_BUILDING_END)
                        .build()
        );
    }
}
