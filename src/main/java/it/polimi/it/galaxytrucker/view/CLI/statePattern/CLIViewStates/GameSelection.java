package it.polimi.it.galaxytrucker.view.CLI.statePattern.CLIViewStates;

import it.polimi.it.galaxytrucker.controller.GenericGameData;
import it.polimi.it.galaxytrucker.commands.UserInput;
import it.polimi.it.galaxytrucker.commands.UserInputType;
import it.polimi.it.galaxytrucker.view.CLI.CLIView;
import it.polimi.it.galaxytrucker.view.CLI.ConsoleColors;
import it.polimi.it.galaxytrucker.view.CLI.statePattern.CLIViewState;

import java.rmi.RemoteException;
import java.util.InputMismatchException;
import java.util.List;

public class GameSelection extends CLIViewState {

    public GameSelection(CLIView view) {
        super(view);
    }
    List<GenericGameData> activeGames;

    private int printAvailableGames(StateMachine fsm) {
        try {
            activeGames = view.getClient().getActiveGames();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        if (activeGames.isEmpty()) {
            System.out.println("There are no active games, please create a new one.");
            fsm.changeState(new GameCreation(view));
            return 0;
        }

        System.out.println("Available games:");
        int i = 1;
        for (GenericGameData game : activeGames) {
            System.out.print("[" + (game.activePlayers() == game.playerNum() ? "X" : i) + "]: ");
            System.out.println("Players: " + game.activePlayers() + "/" + game.playerNum());
            System.out.print("     ");
            System.out.println("Level: " + game.level());
            i++;
        }
        return i;
    }
    
    @Override
    public void enter(StateMachine fsm) {
        int games = printAvailableGames(fsm);
        
        if (games == 0) {
            return;
        }
        
        System.out.println("Enter which game you'd like to join, or enter 0 to create a new game");
        System.out.print("> ");
        int gameNum = 0;
        while (true) {
            try {
                gameNum = scanner.nextInt();

                if (gameNum >= 0 && gameNum <= games) {
                    break;
                }

                System.out.println(ConsoleColors.YELLOW + "Invalid number of players, please choose a valid number" + ConsoleColors.RESET);
                System.out.print("> ");
            } catch (NumberFormatException | InputMismatchException e) {
                System.out.println(ConsoleColors.RED + "Invalid number format, please enter a valid number" + ConsoleColors.RESET);
                System.out.print("> ");

                // Consume any leftover characters and reset the variable
                scanner.nextLine();
                gameNum = 0;
            }
        }

        if (gameNum == 0) {
            fsm.changeState(new GameCreation(view));
            return;
        }

        view.getClient().receiveUserInput(
                new UserInput.UserInputBuilder(null, UserInputType.GAME_SELECTION)
                        .setGameId(activeGames.get(gameNum - 1).gameId())
                        .build());
    }

    @Override
    public void repromtUser(StateMachine fsm) {
        int games = printAvailableGames(fsm);

        System.out.println("Please try again");
        System.out.print("> ");

        int gameNum = 0;
        while (true) {
            try {
                gameNum = scanner.nextInt();

                if (gameNum >= 0 && gameNum <= games) {
                    break;
                }

                System.out.println(ConsoleColors.YELLOW + "Invalid number of players, please choose a valid number" + ConsoleColors.RESET);
                System.out.print("> ");
            } catch (NumberFormatException | InputMismatchException e) {
                System.out.println(ConsoleColors.RED + "Invalid number format, please enter a valid number" + ConsoleColors.RESET);
                System.out.print("> ");

                // Consume any leftover characters and reset the variable
                scanner.nextLine();
                gameNum = 0;
            }
        }

        if (gameNum == 0) {
            fsm.changeState(new GameCreation(view));
            return;
        }

        view.getClient().receiveUserInput(
                new UserInput.UserInputBuilder(null, UserInputType.GAME_SELECTION)
                        .setGameId(activeGames.get(gameNum - 1).gameId())
                        .build());
    }

    @Override
    public void update(StateMachine fsm) {

    }

    @Override
    public void exit(StateMachine fsm) {

    }
}
