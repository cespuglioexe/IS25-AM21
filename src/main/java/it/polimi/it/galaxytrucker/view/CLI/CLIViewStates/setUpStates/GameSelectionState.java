package it.polimi.it.galaxytrucker.view.CLI.CLIViewStates.setUpStates;

import it.polimi.it.galaxytrucker.messages.clientmessages.UserInput;
import it.polimi.it.galaxytrucker.messages.clientmessages.UserInputType;
import it.polimi.it.galaxytrucker.controller.GenericGameData;
import it.polimi.it.galaxytrucker.view.CLI.CLIInputReader;
import it.polimi.it.galaxytrucker.view.CLI.CLIViewStates.CLIViewState;
import it.polimi.it.galaxytrucker.view.CLI.ConsoleColors;

import java.util.InputMismatchException;
import java.util.List;

public class GameSelectionState extends CLIViewState {
    volatile List<GenericGameData> activeGames;
    
    @Override
    public void executeState() {
        view.executorService.submit(() -> {
            view.getClient().receiveUserInput(
                    new UserInput.UserInputBuilder(UserInputType.SEE_ACTIVE_GAMES)
                            .build()
            );

            while (activeGames == null) {
                Thread.onSpinWait();
            }

            if (activeGames.isEmpty()) {
                System.out.println("There are no active games, please create a new one.");
                currentState = new GameCreationState();
                currentState.executeState();
                return;
            }

            System.out.println("Available games:");
            int games = 1;
            for (GenericGameData game : activeGames) {
                System.out.print("[" + (game.activePlayers() == game.playerNum() ? "X" : games) + "]: ");
                System.out.println("Players: " + game.activePlayers() + "/" + game.playerNum());
                System.out.print("     ");
                System.out.println("Level: " + game.level());
                games++;
            }

            if (games == 0) {
                return;
            }

            System.out.println("Enter which game you'd like to join, or enter 0 to create a new game");
            System.out.print("> ");
            int gameNum = 0;
            while (true) {
                try {
                    gameNum = CLIInputReader.readInt();

                    if (gameNum >= 0 && gameNum <= games) {
                        break;
                    }

                    System.out.println(ConsoleColors.YELLOW + "Invalid number of players, please choose a valid number" + ConsoleColors.RESET);
                    System.out.print("> ");
                } catch (NumberFormatException | InputMismatchException e) {
                    System.out.println(ConsoleColors.RED + "Invalid number format, please enter a valid number" + ConsoleColors.RESET);
                    System.out.print("> ");

                    // Consume any leftover characters and reset the variable
                    gameNum = 0;
                }
            }

            if (gameNum == 0) {
                currentState = new GameCreationState();
                currentState.executeState();
                return;
            }

            view.getClient().receiveUserInput(
                    new UserInput.UserInputBuilder(UserInputType.JOIN_ACTIVE_GAME)
                            .setGameId(activeGames.get(gameNum - 1).gameId())
                            .build());
        });
    }

    @Override
    public void joinedGameIsFull() {
        System.out.println(ConsoleColors.RED + "The game you tried to join is already full. Please choose a different one or create a new one." + ConsoleColors.RESET);
        executeState();
    }

    @Override
    public void remoteExceptionThrown() {
        System.out.println(ConsoleColors.RED + "There was an error joining the game. Please try again." + ConsoleColors.RESET);
        executeState();
    }
    
    @Override
    public void activeControllers(List<GenericGameData> activeControllers) {
        this.activeGames = activeControllers;
    }
}
