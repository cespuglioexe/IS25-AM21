package it.polimi.it.galaxytrucker.view.CLI.CLIViewStates;

import it.polimi.it.galaxytrucker.commands.UserInput;
import it.polimi.it.galaxytrucker.commands.UserInputType;
import it.polimi.it.galaxytrucker.model.componenttiles.TileData;
import it.polimi.it.galaxytrucker.networking.VirtualClient;
import it.polimi.it.galaxytrucker.view.CLI.CLIInputReader;
import it.polimi.it.galaxytrucker.view.CLI.CLIView;

public class TileActionState extends CLIViewState{
    @Override
    public void executeState() {
        view.executorService.submit(() -> {
            System.out.print("""
                    \nChoose an option:
                    [1]: Place the tile
                    [2]: Save the tile
                    [3]: Discard the tile
                    >\s""");

            int option = CLIInputReader.readInt();

            switch (option) {
                case 1:
                    System.out.println("\nWhere do you want to place the tile?");
                    System.out.print("Column> ");
                    int x = CLIInputReader.readInt();
                    System.out.print("Row> ");
                    int y = CLIInputReader.readInt();

                    System.out.println("\nIn which direction do you want to rotate the tile?");
                    System.out.println("[0]: ↑, [1]: →, [2]: ↓, [3]: ←");
                    System.out.print("> ");

                    int rotation = CLIInputReader.readInt();

                    view.getClient().receiveUserInput(
                            new UserInput.UserInputBuilder((VirtualClient) view.getClient(), UserInputType.PLACE_COMPONENT)
                                    .setCoords(x, y)
                                    .setRotation(rotation)
                                    .build()
                    );
                    break;
                case 2:
                    view.getClient().receiveUserInput(
                            new UserInput.UserInputBuilder((VirtualClient) view.getClient(), UserInputType.SAVE_SELECTED_COMPONENT)
                                    .build()
                    );
                    break;
                case 3:

                    view.getClient().receiveUserInput(
                            new UserInput.UserInputBuilder((VirtualClient) view.getClient(), UserInputType.DISCARD_SELECTED_COMPONENT)
                                    .build()
                    );
                    break;
            }

            currentState = new BuildingMenuState();
            currentState.executeState();
        });
    }

    @Override
    public void displayComponentTile(TileData newTile) {
        ((CLIView) view).printSingleComponent(newTile);
    }
}
