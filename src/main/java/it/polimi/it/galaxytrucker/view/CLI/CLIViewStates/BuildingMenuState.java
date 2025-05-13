package it.polimi.it.galaxytrucker.view.CLI.CLIViewStates;

import it.polimi.it.galaxytrucker.commands.RequestType;
import it.polimi.it.galaxytrucker.commands.UserInput;
import it.polimi.it.galaxytrucker.commands.UserInputType;
import it.polimi.it.galaxytrucker.model.componenttiles.TileData;
import it.polimi.it.galaxytrucker.networking.VirtualClient;
import it.polimi.it.galaxytrucker.networking.client.clientmodel.ClientModel;
import it.polimi.it.galaxytrucker.view.CLI.CLIView;
import it.polimi.it.galaxytrucker.view.CLI.ConsoleColors;

import java.util.List;

public class BuildingMenuState extends CLIViewState {

    @Override
    public void executeState() {
        ClientModel model = view.getClient().getModel();
        view.displayShip(model.getPlayerShips(model.getMyData().getPlayerId()));

        System.out.print("""
                \nChoose an option:
                [1]: Choose a tile
                [2]: Look a pile of cards
                """);
        if (!view.getClient().isBuildingTimerIsActive()) {
            System.out.println("[3]: Restart timer");
        }
        System.out.print("> ");

        int mainOption;
        while (true) {
            mainOption = scanner.nextInt();

            if (mainOption == 1 || mainOption == 2 || (mainOption == 3 && !view.getClient().isBuildingTimerIsActive())) {
                break;
            }

            System.out.println(ConsoleColors.YELLOW + "That is not a valid option. Please try again." + ConsoleColors.RESET);
            System.out.print("> ");
        }

        switch (mainOption) {
            case 1:
                System.out.print("""
                        \nChoose an option:
                        [1]: Pick a new random tile
                        [2]: Choose a saved tile
                        [3]: Choose a discarded tile
                        > """);

                int opt_tile = scanner.nextInt();
                int chosenTile;
                switch (opt_tile) {
                    case 1:
                        view.getClient().receiveUserInput(
                                new UserInput.UserInputBuilder((VirtualClient) view.getClient(), UserInputType.REQUEST)
                                        .setRequestType(RequestType.NEW_TILE)
                                        .build()
                        );
                        break;
                    case 2:
                        List<TileData> savedTiles = model.getSavedTiles();
                        view.displayTiles(savedTiles);

                        System.out.println("Which saved tile do you want to choose?");
                        System.out.print("> ");

                        while (true) {
                            chosenTile = scanner.nextInt() - 1;
                            if (chosenTile >= 0 && chosenTile < savedTiles.size())
                                break;
                            System.out.println(ConsoleColors.YELLOW + "Invalid choice. Please choose another." + ConsoleColors.RESET);
                            System.out.print("> ");
                        }

                        view.getClient().receiveUserInput(
                                new UserInput.UserInputBuilder((VirtualClient) view.getClient(), UserInputType.REQUEST)
                                        .setRequestType(RequestType.SELECT_SAVED_TILE)
                                        .setSelectedTileIndex(chosenTile)
                                        .build()
                        );
                        break;
                    case 3:
                        List<TileData> discardedTiles = model.getDiscardedTiles();
                        view.displayTiles(discardedTiles);

                        System.out.println("Which discarded tile do you want to choose?");
                        System.out.print("> ");

                        while (true) {
                            chosenTile = scanner.nextInt() - 1;
                            if (chosenTile >= 0 && chosenTile < discardedTiles.size())
                                break;
                            System.out.println(ConsoleColors.YELLOW + "Invalid choice. Please choose another." + ConsoleColors.RESET);
                            System.out.print("> ");
                        }

                        view.getClient().receiveUserInput(
                                new UserInput.UserInputBuilder((VirtualClient) view.getClient(), UserInputType.REQUEST)
                                        .setRequestType(RequestType.SELECT_DISCARDED_TILE)
                                        .setSelectedTileIndex(chosenTile)
                                        .build()
                        );
                        break;
                }

                currentState = new TileActionState();
                currentState.executeState();

                break;
            case 2:
                System.out.println("Which card pile do you want to see? (1, 2, 3)");
                System.out.print("> ");
                int pile = scanner.nextInt();


                view.displayCards(model.getCardPile(pile));

                currentState = new BuildingMenuState();
                currentState.executeState();

                break;
            case 3:
                if (!view.getClient().isBuildingTimerIsActive()) {
                    view.getClient().receiveUserInput(
                            new UserInput.UserInputBuilder((VirtualClient) view.getClient(), UserInputType.START_TIMER)
                                    .build()
                    );
                }

                currentState = new BuildingMenuState();
                currentState.executeState();

                break;

            default:
                System.out.println(ConsoleColors.YELLOW + "That's not a valid option. Please try again" + ConsoleColors.RESET);
                executeState();
                break;
        }
    }

    @Override
    public void displayComponentTile(TileData newTile) {
        ((CLIView) view).printSingleComponent(newTile);
    }
}
