package it.polimi.it.galaxytrucker.view.statePattern.viewstates;

import it.polimi.it.galaxytrucker.controller.GenericGameData;
import it.polimi.it.galaxytrucker.networking.messages.UserInput;
import it.polimi.it.galaxytrucker.networking.messages.UserInputType;
import it.polimi.it.galaxytrucker.view.CLIView;
import it.polimi.it.galaxytrucker.view.ConsoleColors;
import it.polimi.it.galaxytrucker.view.statePattern.State;
import it.polimi.it.galaxytrucker.view.statePattern.StateMachine;

import java.rmi.RemoteException;
import java.util.InputMismatchException;
import java.util.List;

public class GameSelection extends State {

    public GameSelection(CLIView view) {
        super(view);
    }

    private int printAvailableGames(StateMachine fsm) {
        List<GenericGameData> activeGames;
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
                        .setGameIndex(gameNum - 1)
                        .build());
    }

    @Override
    public void update(StateMachine fsm, boolean repeat) {
        if (repeat) {
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
                            .setGameIndex(gameNum - 1)
                            .build());
        }
    }

    @Override
    public void exit(StateMachine fsm) {

    }
}
