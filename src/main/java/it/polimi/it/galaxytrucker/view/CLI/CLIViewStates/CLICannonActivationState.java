package it.polimi.it.galaxytrucker.view.CLI.CLIViewStates;

import it.polimi.it.galaxytrucker.messages.clientmessages.UserInput;
import it.polimi.it.galaxytrucker.messages.clientmessages.UserInputType;
import it.polimi.it.galaxytrucker.model.componenttiles.BatteryComponent;
import it.polimi.it.galaxytrucker.model.componenttiles.DoubleCannon;
import it.polimi.it.galaxytrucker.model.componenttiles.TileData;
import it.polimi.it.galaxytrucker.model.utility.Coordinates;
import it.polimi.it.galaxytrucker.view.CLI.CLIInputReader;
import it.polimi.it.galaxytrucker.view.CLI.ConsoleColors;

import java.util.ArrayList;
import java.util.List;

public class CLICannonActivationState extends CLIViewState {

    private final List<Coordinates> cannonCoords = new ArrayList<>();
    private final List<Coordinates> batteryCoords = new ArrayList<>();
    private final List<List<Coordinates>> cannonAndBatteryCoord = new ArrayList<>();

    @Override
    public void executeState() {
        view.executorService.submit(() -> {
            System.out.print("""
                    \nChoose an option:
                    [1]: Choose a double cannon to activate
                    [2]: Choose a battery to consume
                    [3]: Confirm activated cannons
                    >\s""");

            int option = CLIInputReader.readInt();
            int column;
            int row;

            switch (option) {
                case 1:
                    System.out.println("\nWhich double cannon would you like to activate?");
                    System.out.print("Column> ");
                    column = CLIInputReader.readInt();
                    System.out.print("Row> ");
                    row = CLIInputReader.readInt();

                    System.out.println("sus");
                    addCannon(row, column);
                    System.out.println("sus");
                    executeState();
                    break;
                case 2:
                    System.out.println("\nWhich battery would you like to activate?");
                    System.out.print("Column> ");
                    column = CLIInputReader.readInt();
                    System.out.print("Row> ");
                    row = CLIInputReader.readInt();

                    addBattery(row, column);
                    executeState();
                    break;
                case 3:
                    if (!activateCannon()) {
                        executeState();
                    }
                    break;
                default:
                    System.out.println(ConsoleColors.YELLOW + "That's not a valid option. Please try again" + ConsoleColors.RESET);
                    executeState();
                    break;
            }
        });
    }

    private void addCannon(int row, int col) {
        if((row<5) || (row>9) || (col<4) || (col>10)){
            System.out.println(ConsoleColors.YELLOW + "Oops, that's not a double cannon" + ConsoleColors.RESET);
            return;
        }

        TileData tile = view.getClient().getModel().getPlayerShips(view.getClient().getModel().getMyData().getPlayerId()).get(row - 5).get(col - 4);

        if (tile != null && tile.type().equals(DoubleCannon.class.getSimpleName())) {
            Coordinates cannonCoord = new Coordinates(row, col);
            if (!cannonCoords.contains(cannonCoord)) {
                cannonCoords.add(new Coordinates(row, col));
            } else {
                System.out.println(ConsoleColors.YELLOW + "Oops, that cannon's already been selected" + ConsoleColors.RESET);
            }
        } else {
            System.out.println(ConsoleColors.YELLOW + "Oops, that's not a double cannon" + ConsoleColors.RESET);
        }
    }

    private void addBattery(int row, int col){
        if((row<5) || (row>9) || (col<4) || (col>10)){
            System.out.println(ConsoleColors.YELLOW + "Oops, that's not a battery" + ConsoleColors.RESET);
            return;
        }

        TileData tile = view.getClient().getModel().getPlayerShips(view.getClient().getModel().getMyData().getPlayerId()).get(row - 5).get(col - 4);
        if (tile != null && tile.type().equals(BatteryComponent.class.getSimpleName())) {
            Coordinates batteryCoord = new Coordinates(row, col);
            if (!batteryCoords.contains(batteryCoord)) {
                cannonCoords.add(new Coordinates(row, col));
            } else {
                System.out.println(ConsoleColors.YELLOW + "Oops, that battery's already been selected" + ConsoleColors.RESET);
            }
        } else {
            System.out.println(ConsoleColors.YELLOW + "Oops, that's not a battery" + ConsoleColors.RESET);
        }
    }

    private boolean activateCannon(){
        boolean result = false;
        if(!cannonCoords.isEmpty() && !batteryCoords.isEmpty() && (cannonCoords.size()== batteryCoords.size())){
            cannonAndBatteryCoord.add(cannonCoords);
            cannonAndBatteryCoord.add(batteryCoords);

            view.getClient().receiveUserInput(
                    new UserInput.UserInputBuilder(UserInputType.ACTIVATE_CANNON)
                            .setComponentsForActivation(cannonAndBatteryCoord)
                            .build()
            );

            result = true;
        } else{
            if(cannonCoords.isEmpty() && batteryCoords.isEmpty() ) {
                cannonAndBatteryCoord.clear();
                view.getClient().receiveUserInput(
                        new UserInput.UserInputBuilder(UserInputType.ACTIVATE_CANNON)
                                .setComponentsForActivation(cannonAndBatteryCoord)
                                .build()
                );

                result = true;
            } else {
                System.out.println(ConsoleColors.YELLOW + "Oops, you seem to have selected a different amount of cannons (" + cannonCoords.size() + ") and batteries (" + batteryCoords.size() + ")" + ConsoleColors.RESET);
                System.out.println(ConsoleColors.YELLOW + "Please try again" + ConsoleColors.RESET);
            }
        }

        cannonAndBatteryCoord.clear();
        cannonCoords.clear();
        batteryCoords.clear();

        return result;
    }
}
