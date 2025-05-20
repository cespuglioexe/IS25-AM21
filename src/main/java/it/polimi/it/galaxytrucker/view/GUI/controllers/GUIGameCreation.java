package it.polimi.it.galaxytrucker.view.GUI.controllers;

import it.polimi.it.galaxytrucker.commands.UserInput;
import it.polimi.it.galaxytrucker.commands.UserInputType;
import it.polimi.it.galaxytrucker.controller.GenericGameData;
import it.polimi.it.galaxytrucker.view.CLI.ConsoleColors;
import it.polimi.it.galaxytrucker.view.GUI.GUIView;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class GUIGameCreation extends GUIViewState {
    @FXML public TextField numberOfPlayersInput;
    @FXML public TextField setLevelInput;
    @FXML public ListView<String> activeGamesList;

    private List<GenericGameData> availableGames;

    private static GUIGameCreation instance;

    public static GUIGameCreation getInstance() {
        synchronized (GUIUsernameSelection.class) {
            if (instance == null) {
                instance = new GUIGameCreation();
            }
            return instance;
        }
    }

    private GUIGameCreation() {
        try {
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(GUITitleScreen.class.getResource("/it/polimi/it/galaxytrucker/fxmlstages/gameCreation.fxml")));
            loader.setController(this);
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void gameCreationFunction(){
        int level = Integer.parseInt(setLevelInput.getText());
        int playerNum = Integer.parseInt(numberOfPlayersInput.getText());

        GUIView.getInstance().getClient().receiveUserInput(
                new UserInput.UserInputBuilder(UserInputType.CREATE_NEW_GAME)
                        .setGameLevel(level)
                        .setGamePlayers(playerNum)
                        .build()
        );
    }

    @FXML
    private void handleSelection() {
        int selectedIndex = activeGamesList.getSelectionModel().getSelectedIndex();
        String selectedItem = activeGamesList.getSelectionModel().getSelectedItem();

        if (selectedItem != null) {
            joinGame(selectedItem,selectedIndex,availableGames);
        }
    }

    private void joinGame(String selectedItem, int selectedIndex, List<GenericGameData> activeGames) {
        GUIView.getInstance().getClient().receiveUserInput(
                new UserInput.UserInputBuilder( UserInputType.JOIN_ACTIVE_GAME)
                        .setGameId(activeGames.get(selectedIndex).gameId())
                        .build());
    }


    public void activeControllers(List<GenericGameData> activeControllers) {
        this.availableGames = activeControllers;
    }


    @Override
    public void displayScene() {
        Platform.runLater(() -> {

            System.out.println("GUIGameCreation.displayScene");

            ObservableList<String> gamesList = FXCollections.observableArrayList();
            StringBuilder compositionString = new StringBuilder();
            int games = 1;


            GUIView.getInstance().getClient().receiveUserInput(
                    new UserInput.UserInputBuilder(UserInputType.SEE_ACTIVE_GAMES)
                            .build()
            );


            while (availableGames == null) {
                Thread.onSpinWait();
            }

            System.out.println("After while waiting");


            if (!availableGames.isEmpty()) {
                for (GenericGameData game : availableGames) {
                    compositionString.append("[ ").append(game.activePlayers() == game.playerNum() ? "X" : games).append(" ]");
                    compositionString.append("     ");
                    compositionString.append("Players: ").append(game.activePlayers()).append("/").append(game.playerNum());
                    compositionString.append("     ");
                    compositionString.append("Level: ").append(game.level());
                    games++;
                    gamesList.add(compositionString.toString());
                    compositionString = new StringBuilder();
                }
                activeGamesList.setItems(FXCollections.observableArrayList(gamesList));
            }



            stage = (Stage) GUIView.stage.getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);

            stage.show();
        });
    }
}
