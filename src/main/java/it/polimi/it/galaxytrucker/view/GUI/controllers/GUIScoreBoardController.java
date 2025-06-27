package it.polimi.it.galaxytrucker.view.GUI.controllers;

import it.polimi.it.galaxytrucker.messages.clientmessages.UserInput;
import it.polimi.it.galaxytrucker.messages.clientmessages.UserInputType;
import it.polimi.it.galaxytrucker.model.managers.Player;
import it.polimi.it.galaxytrucker.model.utility.Color;
import it.polimi.it.galaxytrucker.view.GUI.GUIView;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.*;

public class GUIScoreBoardController extends GUIViewState{

    @FXML
    private Label nick0,nick1,nick2,nick3;
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
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(GUIScoreBoardController.class.getResource("/it/polimi/it/galaxytrucker/fxmlstages/scoreBoard.fxml")));
            loader.setController(this);
            Parent newroot = null;
            try {
                newroot = loader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            stage = (Stage) GUIView.stage.getScene().getWindow();
            scene = new Scene(newroot);
            stage.setScene(scene);
            stage.show();

            updateScoreboard();
        });
    }

    @FXML
    public void updateScoreboard(){
        HashMap<UUID, String> playersNicks = GUIView.getInstance().getClient().getModel().getAllPlayersNickname();
        List<String> rankings = new ArrayList<>();
        System.out.println(playersNicks);

        for (Map.Entry<UUID, String> entry : playersNicks.entrySet()) {
            rankings.add(entry.getValue());
        }


        nick0.setText(rankings.size() > 0 ? rankings.get(0) : "");
        nick1.setText(rankings.size() > 1 ? rankings.get(1) : "");
        nick2.setText(rankings.size() > 2 ? rankings.get(2) : "");
        nick3.setText(rankings.size() > 3 ? rankings.get(3) : "");

    }

}
