package it.polimi.it.galaxytrucker.view.CLI.CLIViewStates;

import it.polimi.it.galaxytrucker.messages.clientmessages.UserInput;
import it.polimi.it.galaxytrucker.messages.clientmessages.UserInputType;
import it.polimi.it.galaxytrucker.view.CLI.CLIInputReader;
import it.polimi.it.galaxytrucker.view.CLI.ConsoleColors;

import java.util.InputMismatchException;

public class GameCreationState extends CLIViewState {
    @Override
    public void executeState() {
        view.executorService.submit(() -> {
            System.out.println(ConsoleColors.YELLOW_BACKGROUND + "" + ConsoleColors.BLACK_BOLD + "==> GAME CREATION <==" + ConsoleColors.RESET);

            int playerNum = 0;
            System.out.println("How many players do you want in the game? (" + ConsoleColors.BLUE_UNDERLINED + "1" + ConsoleColors.RESET + ", 2, 3, 4)");
            System.out.print("> ");

            while (true) {
                try {
                    playerNum = CLIInputReader.readInt();

                    // SHOULD BE playerNum >= 2! 1 IS FOR MAKING TESTING FASTER
                    if (playerNum >= 1 && playerNum <= 4) {
                        break;
                    }

                    System.out.println(ConsoleColors.YELLOW + "Invalid number of players, please choose a valid number" + ConsoleColors.RESET);
                    System.out.print("> ");
                } catch (NumberFormatException | InputMismatchException e) {
                    System.out.println(ConsoleColors.RED + "Invalid number format, please enter a valid number" + ConsoleColors.RESET);
                    System.out.print("> ");

                    // Consume any leftover characters and reset the variable
                    playerNum = 0;
                }
            }

            int level = 0;
            System.out.println("What level game do you want? (1, 2)");
            System.out.print("> ");

            while (true) {
                try {
                    level = CLIInputReader.readInt();

                    if (level >= 1 && level <= 2) {
                        break;
                    }

                    System.out.println(ConsoleColors.YELLOW + "Invalid level, please choose a valid number" + ConsoleColors.RESET);
                    System.out.print("> ");
                } catch (NumberFormatException | InputMismatchException e) {
                    System.out.println(ConsoleColors.RED + "Invalid number format, please enter a valid number" + ConsoleColors.RESET);
                    System.out.print("> ");

                    // Consume any leftover characters and reset the variable
                    level = 0;
                }
            }

            view.getClient().receiveUserInput(
                    new UserInput.UserInputBuilder(UserInputType.CREATE_NEW_GAME)
                            .setGameLevel(level)
                            .setGamePlayers(playerNum)
                            .build()
            );
        });
    }

    @Override
    public void gameCreationSuccess(boolean success) {
        if (success) {
            System.out.println(ConsoleColors.GREEN + "Game created successfully. Waiting for players to join..." + ConsoleColors.RESET);
        } else {
            System.out.println(ConsoleColors.RED + "Game creation failed, please try again" + ConsoleColors.RESET);
            currentState.executeState();
        }
    }
}
