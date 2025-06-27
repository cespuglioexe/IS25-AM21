package it.polimi.it.galaxytrucker.view.CLI.CLIViewStates.cardInputStates;

import it.polimi.it.galaxytrucker.messages.clientmessages.UserInput;
import it.polimi.it.galaxytrucker.messages.clientmessages.UserInputType;
import it.polimi.it.galaxytrucker.model.componenttiles.BatteryComponent;
import it.polimi.it.galaxytrucker.model.componenttiles.DoubleEngine;
import it.polimi.it.galaxytrucker.model.componenttiles.TileData;
import it.polimi.it.galaxytrucker.model.utility.Coordinates;
import it.polimi.it.galaxytrucker.view.CLI.CLIInputReader;
import it.polimi.it.galaxytrucker.view.CLI.CLIViewStates.CLIViewState;
import it.polimi.it.galaxytrucker.view.CLI.ConsoleColors;

import java.util.ArrayList;
import java.util.List;

public class CLIEngineActivationState extends CLIViewState {
    private final List<Coordinates> engineCoords = new ArrayList<>();
    private final List<Coordinates> batteryCoords = new ArrayList<>();
    private final List<List<Coordinates>> engineAndBatteryCoord = new ArrayList<>();

    @Override
    public void executeState() {
        view.executorService.submit(() -> {
            System.out.print("""
                    \nChoose an option:
                    [1]: Choose a double engine to activate
                    [2]: Choose a battery to consume
                    [3]: Confirm activated engines
                    >\s""");

            int option = CLIInputReader.readInt();
            int column;
            int row;

            switch (option) {
                case 1:
                    System.out.println("\nWhich double engine would you like to activate?");
                    System.out.print("Column> ");
                    column = CLIInputReader.readInt();
                    System.out.print("Row> ");
                    row = CLIInputReader.readInt();

                    addEngine(row, column);
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
                    if (!activateEngine()) {
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

    private void addEngine(int row, int col) {
        if((row<5) || (row>9) || (col<4) || (col>10)){
            System.out.println(ConsoleColors.YELLOW + "Oops, that's not a double engine" + ConsoleColors.RESET);
            return;
        }

        TileData tile = view.getClient().getModel().getPlayerShips(view.getClient().getModel().getMyData().getPlayerId()).get(row - 5).get(col - 4);

        if (tile != null && tile.type().equals(DoubleEngine.class.getSimpleName())) {
            Coordinates engineCoord = new Coordinates(row, col);
            if (!engineCoords.contains(engineCoord)) {
                engineCoords.add(new Coordinates(row, col));
            } else {
                System.out.println(ConsoleColors.YELLOW + "Oops, that engine's already been selected" + ConsoleColors.RESET);
            }
        } else {
            System.out.println(ConsoleColors.YELLOW + "Oops, that's not a double engine" + ConsoleColors.RESET);
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
                engineCoords.add(new Coordinates(row, col));
            } else if (tile.batteryCharge() <= 0) {
                System.out.println(ConsoleColors.YELLOW + "That battery's empty! Please select a different one" + ConsoleColors.RESET);
            } else {
                System.out.println(ConsoleColors.YELLOW + "Oops, that battery's already been selected" + ConsoleColors.RESET);
            }
        } else {
            System.out.println(ConsoleColors.YELLOW + "Oops, that's not a battery" + ConsoleColors.RESET);
        }
    }

    private boolean activateEngine(){
        boolean result = false;
        if(!engineCoords.isEmpty() && !batteryCoords.isEmpty() && (engineCoords.size()== batteryCoords.size())){
            engineAndBatteryCoord.add(engineCoords);
            engineAndBatteryCoord.add(batteryCoords);

            view.getClient().receiveUserInput(
                    new UserInput.UserInputBuilder(UserInputType.ACTIVATE_ENGINE)
                            .setComponentsForActivation(engineAndBatteryCoord)
                            .build()
            );

            result = true;
        } else{
            if(engineCoords.isEmpty() && batteryCoords.isEmpty() ) {
                engineAndBatteryCoord.clear();
                view.getClient().receiveUserInput(
                        new UserInput.UserInputBuilder(UserInputType.ACTIVATE_ENGINE)
                                .setComponentsForActivation(engineAndBatteryCoord)
                                .build()
                );

                result = true;
            } else {
                System.out.println(ConsoleColors.YELLOW + "Oops, you seem to have selected a different amount of engines (" + engineCoords.size() + ") and batteries (" + batteryCoords.size() + ")" + ConsoleColors.RESET);
                System.out.println(ConsoleColors.YELLOW + "Please try again" + ConsoleColors.RESET);
            }
        }

        engineAndBatteryCoord.clear();
        engineCoords.clear();
        batteryCoords.clear();

        return result;
    }
}
