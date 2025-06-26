package it.polimi.it.galaxytrucker.view.CLI.CLIViewStates.cardInputStates;

import it.polimi.it.galaxytrucker.messages.clientmessages.UserInput;
import it.polimi.it.galaxytrucker.messages.clientmessages.UserInputType;
import it.polimi.it.galaxytrucker.model.componenttiles.CargoHold;
import it.polimi.it.galaxytrucker.model.componenttiles.SpecialCargoHold;
import it.polimi.it.galaxytrucker.model.componenttiles.TileData;
import it.polimi.it.galaxytrucker.model.utility.Cargo;
import it.polimi.it.galaxytrucker.model.utility.Color;
import it.polimi.it.galaxytrucker.model.utility.Coordinates;
import it.polimi.it.galaxytrucker.networking.client.clientmodel.ClientModel;
import it.polimi.it.galaxytrucker.view.CLI.CLIInputReader;
import it.polimi.it.galaxytrucker.view.CLI.CLIViewStates.CLIViewState;
import it.polimi.it.galaxytrucker.view.CLI.ConsoleColors;
import it.polimi.it.galaxytrucker.view.GUI.GUIView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CLICargoChoiceState extends CLIViewState {

    private List<Cargo> cargoReward = null;
    private final Map<Cargo, Integer> cargoToIndex = new HashMap<>();
    private final HashMap<Integer,Coordinates> cargoCoords = new HashMap<>();
    private int i = 0;

    private String cargoToEmoji(Cargo cargo) {
        return switch (cargo.getColor()) {
            case RED ->     "🟥";
            case YELLOW ->  "🟨";
            case GREEN ->   "🟩";
            case BLUE ->    "🟦";
        };
    }

    @Override
    public void executeState() {
        if (cargoReward == null) {
            loadCardDetails();

            view.executorService.submit(() -> {
                System.out.print("Your cargo reward is ");
                for (Cargo cargo : cargoReward) {
                    System.out.println(cargoToEmoji(cargo));
                }
                System.out.println();
            });
        }

        view.executorService.execute(() -> {

            for (Cargo cargo : cargoReward) {
                System.out.println("Do you want to accept this cargo: " + cargoToEmoji(cargoReward.get(i)) + "?");
                System.out.println("\tNote: special cargo (🟥) can only be place in special cargo holds!");

                System.out.print("""
                        \nChoose an option:
                        [1]: Yes
                        [2]: No
                        >\s""");

                int option = CLIInputReader.readInt();
                int column;
                int row;

                switch (option) {
                    case 1 -> {
                        do {
                            System.out.println("\nIn which cargo hold do you want to place it?");
                            System.out.print("Column> ");
                            column = CLIInputReader.readInt();
                            System.out.print("Row> ");
                            row = CLIInputReader.readInt();
                        } while (!addCargo(row, column));
                    }
                    case 2 -> {
                        // Do nothing
                    }
                    default -> {
                        System.out.println("That's not a valid option, please choose another");
                        executeState();
                    }
                }
            }

            acceptReward();
        });
    }

    private void loadCardDetails() {
        ClientModel model = view.getClient().getModel();
        List<Cargo> rewardList = new ArrayList<>();

        if (model.getCurrentCard().equals("Planets")) {
            int selectedPlanet = model.getCardDetail("selectedPlanet", Integer.class);
            List<List<String>> serializedCargoRewards = model.getUnsafeCardDetail("rewards");

            for (String cargoValue : serializedCargoRewards.get(selectedPlanet)) {
                Cargo cargo = new Cargo(Color.valueOf(cargoValue));

                rewardList.add(cargo);
            }

        } else {
            List<String> serializedCargoReward = model.getUnsafeCardDetail("cargoReward");

            for (String cargoValue : serializedCargoReward) {
                Cargo cargo = new Cargo(Color.valueOf(cargoValue));

                rewardList.add(cargo);
            }
        }
        setCargoReward(rewardList);
    }

    public void setCargoReward(List<Cargo> cargoReward) {
        this.cargoReward = cargoReward;
        for (int j = 0; j < cargoReward.size(); j++) {
            cargoToIndex.put(cargoReward.get(j), j);
        }
    }

    private boolean addCargo(int row, int col) {
        TileData tile = view.getClient().getModel().getPlayerShips(view.getClient().getModel().getMyData().getPlayerId()).get(row - 5).get(col - 4);
        if(!cargoReward.isEmpty()) {
            if ((row < 5) || (row > 9) || (col < 4) || (col > 10)) {
                System.out.println(ConsoleColors.YELLOW + "Those aren't valid coordinated, please try again" + ConsoleColors.RESET);
                return false;
            }

            if (tile != null && (tile.type().equals(CargoHold.class.getSimpleName()) || tile.type().equals(SpecialCargoHold.class.getSimpleName()))) {
                if (cargoReward.get(i).isSpecial()) {
                    if (tile.type().equals(SpecialCargoHold.class.getSimpleName())) {
                        cargoCoords.put(cargoToIndex.get(cargoReward.get(i)), new Coordinates(col, row));
                        cargoReward.remove(cargoReward.get(i));
                    } else {
                        System.out.println(ConsoleColors.YELLOW + "Oops, special cargo can only go in a special cargo hold" + ConsoleColors.RESET);
                        return false;
                    }
                } else {
                    cargoCoords.put(cargoToIndex.get(cargoReward.get(i)), new Coordinates(col, row));
                    cargoReward.remove(cargoReward.get(i));
                }
            } else {
                System.out.println(ConsoleColors.YELLOW + "Oops, that's not a cargo hold" + ConsoleColors.RESET);
                return false;
            }
        } else {
            System.out.println("You've loaded all the cargo, there's non left");
            return false;
        }

        i++;
        return true;
    }

    private void acceptReward() {
        if(!cargoReward.isEmpty()) {
            for(Cargo cargo: cargoReward) {
                if(cargo.equals(cargoToIndex.get(cargo))) {
                    cargoCoords.put(cargoToIndex.get(cargo),new Coordinates(0,0));
                }
            }
        }
        view.getClient().receiveUserInput(
                new UserInput.UserInputBuilder(UserInputType.CARGO_REWARD)
                        .setAcceptedCargo(cargoCoords)
                        .build()
        );
    }
}
