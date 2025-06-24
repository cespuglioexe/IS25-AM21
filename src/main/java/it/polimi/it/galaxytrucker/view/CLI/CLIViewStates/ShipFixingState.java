package it.polimi.it.galaxytrucker.view.CLI.CLIViewStates;

import it.polimi.it.galaxytrucker.messages.clientmessages.UserInput;
import it.polimi.it.galaxytrucker.messages.clientmessages.UserInputType;
import it.polimi.it.galaxytrucker.networking.client.clientmodel.ClientModel;
import it.polimi.it.galaxytrucker.view.CLI.CLIInputReader;
import it.polimi.it.galaxytrucker.view.CLI.CLIView;
import it.polimi.it.galaxytrucker.view.CLI.ConsoleColors;

public class ShipFixingState extends CLIViewState {
    private boolean displayedTitle = false;

    @Override
    public void executeState() {
        if (!displayedTitle) {
            System.out.println(ConsoleColors.CLIENT_DEBUG + "Executing ship fixing state" + ConsoleColors.RESET);
            System.out.println(ConsoleColors.YELLOW_BOLD + "Oh no! It seems your ship doesn't follow regulations.\nPlease fix it before we can proceed!" + ConsoleColors.RESET);
            displayedTitle = true;
        }

        view.executorService.submit(() -> {
            view.displayShip(view.getClient().getModel().getPlayerShips(view.getClient().getModel().getMyData().getPlayerId()));

            System.out.print("""
                    \nChoose an option:
                    [1]: Remove a tile
                    [2]: Confirm your ship
                    >\s""");

            int option = CLIInputReader.readInt();

            switch (option) {
                case 1:
                    System.out.println("\nWhich tile do you want to remove?");
                    System.out.print("Column> ");
                    int column = CLIInputReader.readInt();
                    System.out.print("Row> ");
                    int row = CLIInputReader.readInt();

                    view.getClient().receiveUserInput(
                            new UserInput.UserInputBuilder(UserInputType.REMOVE_COMPONENT)
                                    .setCoords(column, row)
                                    .build()
                    );

                    currentState = new ShipFixingState();
                    currentState.executeState();

                    break;
                case 2:
                    view.getClient().receiveUserInput(
                            new UserInput.UserInputBuilder(UserInputType.CONFIRM_BUILDING_END)
                                    .build()
                    );
                    break;
            }
        });
    }

    @Override
    public void displayPlayerShip() {
        ClientModel model = view.getClient().getModel();
        view.displayShip(model.getPlayerShips(model.getMyData().getPlayerId()));
    }
}
