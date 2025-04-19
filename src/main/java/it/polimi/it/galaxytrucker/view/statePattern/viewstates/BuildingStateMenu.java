package it.polimi.it.galaxytrucker.view.statePattern.viewstates;

import it.polimi.it.galaxytrucker.networking.Timer;
import it.polimi.it.galaxytrucker.networking.messages.RequestType;
import it.polimi.it.galaxytrucker.networking.messages.UserInput;
import it.polimi.it.galaxytrucker.networking.messages.UserInputType;
import it.polimi.it.galaxytrucker.view.CLIView;
import it.polimi.it.galaxytrucker.view.ConsoleColors;
import it.polimi.it.galaxytrucker.view.statePattern.State;
import it.polimi.it.galaxytrucker.view.statePattern.StateMachine;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class BuildingStateMenu extends State {

    private volatile boolean waiting = true;

    public BuildingStateMenu(CLIView view) {
        super(view);
    }

    @Override
    public void enter(StateMachine fsm) {

        AtomicInteger seconds = new AtomicInteger(0);

        Timer.scheduleAtFixedRate(() -> {
            int sec = seconds.getAndIncrement();
            System.out.print("\rSecondi: " + sec);
        }, 0, 1, TimeUnit.SECONDS);

        System.out.println("Timer avviato!");

        System.out.println("Main thread is free to do other stuff...");

        // Display ship
        view.getClient().receiveUserInput(
                new UserInput.UserInputBuilder(view.getClient(), UserInputType.REQUEST)
                        .setRequestType(RequestType.SHIP_BOARD)
                        .build()
        );

        while (waiting) {

        }
        waiting = true;

        System.out.print("""
                \nChoose an option:
                [1]: Choose a tile
                [2]: Look a pile of cards
                > """);

        int opt_main = scanner.nextInt();
        switch (opt_main){
            case 1:
                System.out.print("""
                \nChoose an option:
                [1]: Pick a new random tile
                [2]: Choose a saved tile
                [3]: Choose a discarded tile
                > """);

                int opt_tile = scanner.nextInt();
                int chosenTile;
                switch (opt_tile){
                    case 1:
                        view.getClient().receiveUserInput(
                                new UserInput.UserInputBuilder(view.getClient(), UserInputType.REQUEST)
                                        .setRequestType(RequestType.NEW_TILE)
                                        .build()
                        );

                        while (waiting) {

                        }
                        waiting = true;

                        break;
                    case 2:
                        view.getClient().receiveUserInput(
                                new UserInput.UserInputBuilder(view.getClient(), UserInputType.REQUEST)
                                        .setRequestType(RequestType.SAVED_TILES)
                                        .build()
                        );

                        while (waiting) {

                        }
                        waiting = true;

                        System.out.println("Which saved tile do you want to choose?");
                        System.out.print("> ");
                        chosenTile = scanner.nextInt();

                        view.getClient().receiveUserInput(
                                new UserInput.UserInputBuilder(view.getClient(), UserInputType.REQUEST)
                                        .setRequestType(RequestType.SELECT_SAVED_TILE)
                                        .setSelectedTileIndex(chosenTile)
                                        .build()
                        );

                        System.out.println("After selected saved tile in buildingStateMenu");
                        break;
                    case 3:
                        view.getClient().receiveUserInput(
                                new UserInput.UserInputBuilder(view.getClient(), UserInputType.REQUEST)
                                        .setRequestType(RequestType.DISCARDED_TILES)
                                        .build()
                        );

                        while (waiting) {

                        }
                        waiting = true;

                        System.out.println("Which discarded tile do you want to choose?");
                        System.out.print("> ");
                        chosenTile = scanner.nextInt();
                         view.getClient().receiveUserInput(
                                    new UserInput.UserInputBuilder(view.getClient(), UserInputType.REQUEST)
                                     .setRequestType(RequestType.SELECT_DISCARDED_TILE)
                                     .setSelectedTileIndex(chosenTile)
                                     .build()
                         );
                        System.out.println("After get client ");

                        break;
                }

                fsm.changeState(new TileActions(view));

                break;
            case 2:
                view.getClient().receiveUserInput(
                        new UserInput.UserInputBuilder(view.getClient(), UserInputType.REQUEST)
                                .setRequestType(RequestType.CARD_PILE)
                                .build()
                );
                break;
            default:
                System.out.println(ConsoleColors.YELLOW + "That's not a valid option. Please try again" + ConsoleColors.RESET);
            }
    }

    @Override
    public void update(StateMachine fsm, boolean repeat) {
        waiting = false;
    }

    @Override
    public void exit(StateMachine fsm) {

    }
}
