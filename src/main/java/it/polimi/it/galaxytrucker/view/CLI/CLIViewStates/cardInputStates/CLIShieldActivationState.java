package it.polimi.it.galaxytrucker.view.CLI.CLIViewStates.cardInputStates;

import it.polimi.it.galaxytrucker.messages.clientmessages.UserInput;
import it.polimi.it.galaxytrucker.messages.clientmessages.UserInputType;
import it.polimi.it.galaxytrucker.model.componenttiles.BatteryComponent;
import it.polimi.it.galaxytrucker.model.componenttiles.Shield;
import it.polimi.it.galaxytrucker.model.componenttiles.TileData;
import it.polimi.it.galaxytrucker.model.utility.Coordinates;
import it.polimi.it.galaxytrucker.view.CLI.CLIInputReader;
import it.polimi.it.galaxytrucker.view.CLI.CLIViewStates.CLIViewState;
import it.polimi.it.galaxytrucker.view.CLI.ConsoleColors;

import java.util.ArrayList;
import java.util.List;

public class CLIShieldActivationState extends CLIViewState {
    private final List<Coordinates> shieldCoords = new ArrayList<>();
    private final List<Coordinates> batteryCoords = new ArrayList<>();
    private final List<List<Coordinates>> shieldAndBatteryCoord = new ArrayList<>();

    @Override
    public void executeState() {
        view.executorService.submit(() -> {
            System.out.print("""
                    \nChoose an option:
                    [1]: Choose a double shield to activate
                    [2]: Choose a battery to consume
                    [3]: Confirm activated shields
                    >\s""");

            int option = CLIInputReader.readInt();
            int column;
            int row;

            switch (option) {
                case 1:
                    System.out.println("\nWhich double shield would you like to activate?");
                    System.out.print("Column> ");
                    column = CLIInputReader.readInt();
                    System.out.print("Row> ");
                    row = CLIInputReader.readInt();

                    addShield(row, column);
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
                    if (!activateShield()) {
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

    private void addShield(int row, int col) {
        if((row<5) || (row>9) || (col<4) || (col>10)){
            System.out.println(ConsoleColors.YELLOW + "Oops, that's not a double shield" + ConsoleColors.RESET);
            return;
        }

        TileData tile = view.getClient().getModel().getPlayerShips(view.getClient().getModel().getMyData().getPlayerId()).get(row - 5).get(col - 4);

        if (tile != null && tile.type().equals(Shield.class.getSimpleName())) {
            Coordinates shieldCoord = new Coordinates(row, col);
            if (!shieldCoords.contains(shieldCoord)) {
                shieldCoords.add(new Coordinates(row, col));
            } else {
                System.out.println(ConsoleColors.YELLOW + "Oops, that shield's already been selected" + ConsoleColors.RESET);
            }
        } else {
            System.out.println(ConsoleColors.YELLOW + "Oops, that's not a double shield" + ConsoleColors.RESET);
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
                shieldCoords.add(new Coordinates(row, col));
            } else if (tile.batteryCharge() <= 0) {
                System.out.println(ConsoleColors.YELLOW + "That battery's empty! Please select a different one" + ConsoleColors.RESET);
            } else {
                System.out.println(ConsoleColors.YELLOW + "Oops, that battery's already been selected" + ConsoleColors.RESET);
            }
        } else {
            System.out.println(ConsoleColors.YELLOW + "Oops, that's not a battery" + ConsoleColors.RESET);
        }
    }

    private boolean activateShield(){
        boolean result = false;
        if(!shieldCoords.isEmpty() && !batteryCoords.isEmpty() && (shieldCoords.size()== batteryCoords.size())){
            shieldAndBatteryCoord.add(shieldCoords);
            shieldAndBatteryCoord.add(batteryCoords);

            view.getClient().receiveUserInput(
                    new UserInput.UserInputBuilder(UserInputType.ACTIVATE_CANNON)
                            .setComponentsForActivation(shieldAndBatteryCoord)
                            .build()
            );

            result = true;
        } else{
            if(shieldCoords.isEmpty() && batteryCoords.isEmpty() ) {
                shieldAndBatteryCoord.clear();
                view.getClient().receiveUserInput(
                        new UserInput.UserInputBuilder(UserInputType.ACTIVATE_CANNON)
                                .setComponentsForActivation(shieldAndBatteryCoord)
                                .build()
                );

                result = true;
            } else {
                System.out.println(ConsoleColors.YELLOW + "Oops, you seem to have selected a different amount of shields (" + shieldCoords.size() + ") and batteries (" + batteryCoords.size() + ")" + ConsoleColors.RESET);
                System.out.println(ConsoleColors.YELLOW + "Please try again" + ConsoleColors.RESET);
            }
        }

        shieldAndBatteryCoord.clear();
        shieldCoords.clear();
        batteryCoords.clear();

        return result;
    }
}
