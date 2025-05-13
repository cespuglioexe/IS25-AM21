package it.polimi.it.galaxytrucker.view.CLI.statePattern.CLIViewStates;

import it.polimi.it.galaxytrucker.commands.RequestType;
import it.polimi.it.galaxytrucker.commands.UserInput;
import it.polimi.it.galaxytrucker.commands.UserInputType;
import it.polimi.it.galaxytrucker.networking.VirtualClient;
import it.polimi.it.galaxytrucker.networking.client.clientmodel.ClientModel;
import it.polimi.it.galaxytrucker.view.CLI.CLIView;
import it.polimi.it.galaxytrucker.view.CLI.ConsoleColors;
import it.polimi.it.galaxytrucker.view.CLI.statePattern.CLIViewState;

public class BuildingStateMenu extends CLIViewState {

    private volatile boolean waiting = true;

    public BuildingStateMenu(CLIView view) {
        super(view);
    }

    @Override
    public void enter(StateMachine fsm) {
       // Display ship
        ClientModel model = view.getClient().getModel();
        view.displayShip(model.getPlayerShips(model.getMyData().getPlayerId()));

        System.out.print("""
                \nChoose an option:
                [1]: Choose a tile
                [2]: Look a pile of cards
                """);
        if (!view.getClient().isBuildingTimerIsActive()){
            System.out.println("[3]: Restart timer");
        }
        System.out.print("> ");

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
                                new UserInput.UserInputBuilder((VirtualClient) view.getClient(), UserInputType.REQUEST)
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
                        break;
                }

                fsm.changeState(new TileActions(view));

                break;
            case 2:
                System.out.println("Which card pile do you want to see? (1, 2, 3)");
                System.out.print("> ");
                int pile = scanner.nextInt();

                view.getClient().receiveUserInput(
                        new UserInput.UserInputBuilder(view.getClient(), UserInputType.REQUEST)
                                .setRequestType(RequestType.CARD_PILE)
                                .setCardPileIndex(pile - 1)
                                .build()
                );

                fsm.changeState(new BuildingStateMenu(view));

                break;
            case 3:
                System.out.println("");
                if(!view.getClient().isBuildingTimerIsActive()){
                    view.getClient().receiveUserInput(
                            new UserInput.UserInputBuilder(view.getClient(), UserInputType.START_TIMER)
                                    .build()
                    );
                }

                fsm.changeState(new BuildingStateMenu(view));

                break;

            default:
                System.out.println(ConsoleColors.YELLOW + "That's not a valid option. Please try again" + ConsoleColors.RESET);
            }

            System.out.println("End of enter");
    }

    @Override
    public void repromtUser(StateMachine fsm) {

    }

    @Override
    public void update(StateMachine fsm) {
        waiting = false;
    }

    @Override
    public void exit(StateMachine fsm) {

    }
}
