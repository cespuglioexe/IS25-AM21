package it.polimi.it.galaxytrucker.view.statePattern.viewstates;

import it.polimi.it.galaxytrucker.networking.messages.RequestType;
import it.polimi.it.galaxytrucker.networking.messages.UserInput;
import it.polimi.it.galaxytrucker.networking.messages.UserInputType;
import it.polimi.it.galaxytrucker.view.CLIView;
import it.polimi.it.galaxytrucker.view.ConsoleColors;
import it.polimi.it.galaxytrucker.view.statePattern.State;
import it.polimi.it.galaxytrucker.view.statePattern.StateMachine;

public class BuildingStateMenu extends State {

    public BuildingStateMenu(CLIView view) {
        super(view);
    }

    @Override
    public void enter(StateMachine fsm) {
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
                switch (opt_tile){
                    case 1:
                        view.getClient().receiveUserInput(
                                new UserInput.UserInputBuilder(view.getClient(), UserInputType.REQUEST)
                                        .setRequestType(RequestType.NEW_TILE)
                                        .build()
                        );
                        break;
                    case 2:
                        view.getClient().receiveUserInput(
                                new UserInput.UserInputBuilder(view.getClient(), UserInputType.REQUEST)
                                        .setRequestType(RequestType.EMPTY)
                                        .build()
                        );
                    case 3:
                        view.getClient().receiveUserInput(
                                new UserInput.UserInputBuilder(view.getClient(), UserInputType.REQUEST)
                                        .setRequestType(RequestType.EMPTY)
                                        .build()
                        );
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

    }

    @Override
    public void exit(StateMachine fsm) {

    }
}
