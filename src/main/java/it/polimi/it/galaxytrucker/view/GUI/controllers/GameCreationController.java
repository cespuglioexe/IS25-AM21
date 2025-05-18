package it.polimi.it.galaxytrucker.view.GUI.controllers;

import it.polimi.it.galaxytrucker.commands.UserInput;
import it.polimi.it.galaxytrucker.commands.UserInputType;
import it.polimi.it.galaxytrucker.controller.GenericGameData;
import it.polimi.it.galaxytrucker.view.CLI.ConsoleColors;
import it.polimi.it.galaxytrucker.view.GUI.GUIView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.rmi.RemoteException;
import java.util.List;


public class GameCreationController extends GUIView {
    @FXML
    private TextField numberOfPlayersInput;
    @FXML
    private TextField setLevelInput;

    @FXML
    private ListView<String> GamesAvailableList;

    private List<GenericGameData> availableGames;


    private static GameCreationController GameCreationInstance;


    public static GameCreationController getInstance() {
        System.out.println("ConnectionController get instance");
        synchronized (SetUsernameController.class) {
            if (GameCreationInstance == null) {
                GameCreationInstance = new GameCreationController();
            }
            return GameCreationInstance;
        }
    }

    @FXML
    public void gameCreationFunction(){
        int level = Integer.parseInt(setLevelInput.getText());
        int playerNum = Integer.parseInt(numberOfPlayersInput.getText());

        GUIView.getGUIView().getClient().receiveUserInput(
                new UserInput.UserInputBuilder(null, UserInputType.GAME_CREATION)
                        .setGameLevel(level)
                        .setGamePlayers(playerNum)
                        .build()
        );

    }

    @Override
    public void gameCreationSuccess(boolean success) {
        if (success) {
            System.out.println(ConsoleColors.GREEN + "Game created successfully. Waiting for players to join..." + ConsoleColors.RESET);
        } else {
            System.out.println(ConsoleColors.RED + "Game creation failed, please try again" + ConsoleColors.RESET);
        }
    }

    @FXML
    public void initialize() {
        ObservableList<String> gamesList = FXCollections.observableArrayList();
        String compositionString= " ";
        int games = 1;
        try {
             availableGames =  GUIView.getGUIView().getClient().getActiveGames();
             for (GenericGameData game : availableGames) {
                compositionString +=("[ " + (game.activePlayers() == game.playerNum() ? "X" : games) + " ]");
                compositionString +=("     ");
                compositionString +=("Players: " + game.activePlayers() + "/" + game.playerNum());
                compositionString +=("     ");
                compositionString +=("Level: " + game.level());
                games++;
                gamesList.add(compositionString);
                compositionString = "";
            }
            GamesAvailableList.setItems(FXCollections.observableArrayList(gamesList));

        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }

        /*GamesAvailableList.setOnMouseClicked(event -> {
            int selectedIndex = GamesAvailableList.getSelectionModel().getSelectedIndex();
            String selectedItem = GamesAvailableList.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                joinGame(selectedItem,selectedIndex,availableGames);
            }
        });*/
    }

    @FXML
    private void handleSelection() {
        int selectedIndex = GamesAvailableList.getSelectionModel().getSelectedIndex();
        String selectedItem = GamesAvailableList.getSelectionModel().getSelectedItem();

        if (selectedItem != null) {
            joinGame(selectedItem,selectedIndex,availableGames);
        }
    }

    private void joinGame(String selectedItem, int selectedIndex, List<GenericGameData> activeGames) {
        GUIView.getGUIView().getClient().receiveUserInput(
                new UserInput.UserInputBuilder(null, UserInputType.GAME_SELECTION)
                        .setGameId(activeGames.get(selectedIndex).gameId())
                        .build());
    }

}
