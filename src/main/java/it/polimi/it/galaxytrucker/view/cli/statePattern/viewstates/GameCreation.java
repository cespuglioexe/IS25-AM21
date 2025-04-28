package it.polimi.it.galaxytrucker.view.cli.statePattern.viewstates;

import it.polimi.it.galaxytrucker.networking.messages.UserInput;
import it.polimi.it.galaxytrucker.networking.messages.UserInputType;
import it.polimi.it.galaxytrucker.view.cli.CLIView;
import it.polimi.it.galaxytrucker.view.ConsoleColors;
import it.polimi.it.galaxytrucker.view.cli.statePattern.State;
import it.polimi.it.galaxytrucker.view.cli.statePattern.StateMachine;

import java.util.InputMismatchException;

import static org.fusesource.jansi.Ansi.ansi;

public class GameCreation extends State {

    public GameCreation(CLIView view) {
        super(view);
    }

    @Override
    public void enter(StateMachine fsm) {
        System.out.println(ConsoleColors.YELLOW_BACKGROUND + "" + ConsoleColors.BLACK_BOLD + "==> GAME CREATION <==" + ConsoleColors.RESET);

        int playerNum = 0;
        System.out.println("How many players do you want in the game? (" + ConsoleColors.BLUE_UNDERLINED + "1" + ConsoleColors.RESET + ", 2, 3, 4)");
        System.out.print("> ");
        while (true) {
            try {
                playerNum = scanner.nextInt();

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
                scanner.nextLine();
                playerNum = 0;
            }
        }

        int level = 0;
        System.out.println("What level game do you want? (1, 2)");
        System.out.print("> ");
        while (true) {
            try {
                level = scanner.nextInt();

                if (level >= 1 && level <= 2) {
                    break;
                }

                System.out.println(ConsoleColors.YELLOW + "Invalid level, please choose a valid number" + ConsoleColors.RESET);
                System.out.print("> ");
            } catch (NumberFormatException | InputMismatchException e) {
                System.out.println(ConsoleColors.RED + "Invalid number format, please enter a valid number" + ConsoleColors.RESET);
                System.out.print("> ");

                // Consume any leftover characters and reset the variable
                scanner.nextLine();
                level = 0;
            }
        }

        view.getClient().receiveUserInput(
                new UserInput.UserInputBuilder(null, UserInputType.GAME_CREATION)
                        .setGameLevel(level)
                        .setGamePlayers(playerNum)
                        .build());
    }

    @Override
    public void update(StateMachine fsm, boolean repeat) {
        if (!repeat) {
            fsm.changeState(new GameSelection(view));
        }
    }

    @Override
    public void exit(StateMachine fsm) {

    }
}
