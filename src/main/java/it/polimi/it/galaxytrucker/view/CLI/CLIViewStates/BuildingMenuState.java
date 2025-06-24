package it.polimi.it.galaxytrucker.view.CLI.CLIViewStates;

import it.polimi.it.galaxytrucker.messages.clientmessages.UserInput;
import it.polimi.it.galaxytrucker.messages.clientmessages.UserInputType;
import it.polimi.it.galaxytrucker.model.componenttiles.TileData;
import it.polimi.it.galaxytrucker.networking.client.clientmodel.ClientModel;
import it.polimi.it.galaxytrucker.view.CLI.CLIInputReader;
import it.polimi.it.galaxytrucker.view.CLI.ConsoleColors;

import java.util.List;

public class BuildingMenuState extends CLIViewState {

    boolean lookingAtDiscardedComponents = false;

    @Override
    public void executeState() {
        System.out.println(ConsoleColors.CLIENT_DEBUG + "Executing building menu state" + ConsoleColors.RESET);

        view.executorService.submit(() -> {
            ClientModel model = view.getClient().getModel();
            view.displayShip(model.getPlayerShips(model.getMyData().getPlayerId()));

            System.out.print("\nChoose an option:\n  [1]: Choose a tile\n  [2]: Look a pile of cards\n");
            if (!view.getClient().isBuildingTimerIsActive()) {
                System.out.println("  [3]: Restart timer");
            } else {
                System.out.println(ConsoleColors.WHITE + "  [x]: Restart timer" + ConsoleColors.RESET);
            }
            System.out.println("  [4]: Finish building");
            System.out.print("> ");

            int mainOption;
            while (true) {
                mainOption = CLIInputReader.readInt();

                if (mainOption == 1 || mainOption == 2 || (mainOption == 3 && !view.getClient().isBuildingTimerIsActive()) || mainOption == 4) {
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
                            >\s""");

                    int opt_tile;
                    while (true) {
                        opt_tile = CLIInputReader.readInt();

                        if (opt_tile == 1 || opt_tile == 2 || opt_tile == 3) {
                            break;
                        }

                        System.out.println(ConsoleColors.YELLOW + "That is not a valid option. Please try again." + ConsoleColors.RESET);
                        System.out.print("> ");
                    }

                    int chosenTile;
                    switch (opt_tile) {
                        case 1:
                            view.getClient().receiveUserInput(
                                    new UserInput.UserInputBuilder(UserInputType.SELECT_RANDOM_COMPONENT)
                                            .build()
                            );
                            break;

                        case 2:
                            List<TileData> savedTiles = model.getSavedTiles();
                            view.displayTiles(savedTiles);

                            System.out.println("Which saved tile do you want to choose?");
                            System.out.print("> ");

                            while (true) {
                                chosenTile = CLIInputReader.readInt() - 1;
                                if (chosenTile >= 0 && chosenTile < savedTiles.size())
                                    break;
                                System.out.println(ConsoleColors.YELLOW + "Invalid choice. Please choose another." + ConsoleColors.RESET);
                                System.out.print("> ");
                            }

                            view.getClient().receiveUserInput(
                                    new UserInput.UserInputBuilder(UserInputType.SELECT_SAVED_COMPONENT)
                                            .setSelectedTileIndex(chosenTile)
                                            .build()
                            );
                            break;
                        case 3:
                            lookingAtDiscardedComponents = true;
                            List<TileData> discardedTiles = model.getDiscardedTiles();
                            view.displayTiles(discardedTiles);

                            System.out.println("Which discarded tile do you want to choose?");
                            System.out.print("> ");

                            while (true) {
                                chosenTile = CLIInputReader.readInt() - 1;

                                if (chosenTile >= 0 && chosenTile < discardedTiles.size())
                                    break;
                                System.out.println(ConsoleColors.YELLOW + "Invalid choice. Please choose another." + ConsoleColors.RESET);
                                System.out.print("> ");
                            }

                            lookingAtDiscardedComponents = false;

                            view.getClient().receiveUserInput(
                                    new UserInput.UserInputBuilder(UserInputType.SELECT_DISCARDED_COMPONENT)
                                            .setSelectedTileIndex(chosenTile)
                                            .build()
                            );
                            break;
                    }
                    break;

                case 2:
                    System.out.println("Which card pile do you want to see? (1, 2, 3)");
                    System.out.print("> ");
                    int pile = CLIInputReader.readInt();


                    view.displayCards(model.getCardPile(pile));

                    currentState = new BuildingMenuState();
                    currentState.executeState();

                    break;
                case 3:
                    if (!view.getClient().isBuildingTimerIsActive()) {
                        view.getClient().receiveUserInput(
                                new UserInput.UserInputBuilder(UserInputType.RESTART_BUILDING_TIMER)
                                        .build()
                        );
                    }

                    currentState = new BuildingMenuState();
                    currentState.executeState();

                    break;

                case 4:
                    view.getClient().receiveUserInput(
                            new UserInput.UserInputBuilder(UserInputType.CONFIRM_BUILDING_END)
                                    .build()
                    );
                    break;

                default:
                    System.out.println(ConsoleColors.YELLOW + "That's not a valid option. Please try again" + ConsoleColors.RESET);
                    executeState();
                    break;
            }
        });
    }

    @Override
    public void displayComponentTile(TileData newTile) {
        view.printSingleComponent(newTile);
        currentState = new TileActionState();
        currentState.executeState();
    }

    @Override
    public void displaySavedComponents () {
        ClientModel model = view.getClient().getModel();
        view.displayTiles(model.getSavedTiles());
    }

    @Override
    public void displayDiscardedComponents () {
        ClientModel model = view.getClient().getModel();
        view.displayTiles(model.getDiscardedTiles());
    }

    @Override
    public void displayPlayerShip() {
        ClientModel model = view.getClient().getModel();
        view.displayShip(model.getPlayerShips(model.getMyData().getPlayerId()));
    }

    @Override
    public void discardedComponentsUpdated() {
        if (lookingAtDiscardedComponents) {
            System.out.println(ConsoleColors.YELLOW + "There are different discarded components available!" + ConsoleColors.RESET);
            executeState();
        }
    }
}
