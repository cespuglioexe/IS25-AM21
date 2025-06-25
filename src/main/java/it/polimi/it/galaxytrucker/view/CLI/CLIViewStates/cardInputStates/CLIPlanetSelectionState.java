package it.polimi.it.galaxytrucker.view.CLI.CLIViewStates.cardInputStates;

import it.polimi.it.galaxytrucker.messages.clientmessages.UserInput;
import it.polimi.it.galaxytrucker.messages.clientmessages.UserInputType;
import it.polimi.it.galaxytrucker.model.utility.Cargo;
import it.polimi.it.galaxytrucker.networking.client.clientmodel.ClientModel;
import it.polimi.it.galaxytrucker.view.CLI.CLIInputReader;
import it.polimi.it.galaxytrucker.view.CLI.CLIViewStates.CLIViewState;
import it.polimi.it.galaxytrucker.view.CLI.ConsoleColors;
import it.polimi.it.galaxytrucker.view.GUI.GUIView;
import javafx.fxml.FXML;

import java.util.*;

public class CLIPlanetSelectionState extends CLIViewState {

    private Set<Integer> occupiedPlanets = new HashSet<>();

    private HashMap<Integer, HashMap<String, Integer>> planetsAndCargo;

    private String cargoToEmoji(String cargo) {
        return switch (cargo) {
            case "RED" ->     "🟥";
            case "YELLOW" ->  "🟨";
            case "GREEN" ->   "🟩";
            case "BLUE" ->    "🟦";
            default -> "❌";
        };
    }
    
    @Override
    public void executeState() {
        if (planetsAndCargo == null) {
            loadCardDetails();

            view.executorService.submit(() -> {
                System.out.println("The available planets are:");
                System.out.println("[0]: Skip this card");
                for (int i = 0; i < planetsAndCargo.size(); i++) {
                    System.out.print("[" + (occupiedPlanets.contains(i) ? "X" : i - 1) + "]: ");
                    for (String type : planetsAndCargo.get(i).keySet()) {
                        System.out.print(cargoToEmoji(type).repeat(planetsAndCargo.get(i).get(type)));
                    }
                    System.out.println();
                }
                System.out.println();
            });
        }

        view.executorService.submit(() -> {
            System.out.println("Which planet do you want to land on?");
            System.out.println("\tNote: 'X' mean you can't land. You can also skip this card.");
            System.out.print("> ");

            int option = CLIInputReader.readInt();

            if (option == 0) {
                skipTurn();
            } else if (option >= 1 && option < planetsAndCargo.size()) {
                if (occupiedPlanets.contains(option)) {
                    System.out.println(ConsoleColors.YELLOW + "Oops, you can't land on that planet. It's occupied" + ConsoleColors.RESET);
                    executeState();
                } else {
                    System.out.println("You landed on planet " + option + ".");
                    landOnPlanet(option);
                }
            } else {
                System.out.println(ConsoleColors.YELLOW + "That's not a valid option" + ConsoleColors.RESET);
                executeState();
            }
        });
    }

    private void loadCardDetails() {
        ClientModel model = view.getClient().getModel();

        int numOfPlanets = model.getCardDetail("planets", Integer.class);
        List<List<String>> cargoRewards = model.getUnsafeCardDetail("rewards");

        planetsAndCargo = new HashMap<>();
        for (int i = 0; i < numOfPlanets; i++) {
            HashMap<String, Integer> cargoNumber = new HashMap<>();

            for (String cargo : cargoRewards.get(i)) {
                int count = cargoNumber.getOrDefault(cargo, 0) + 1;
                cargoNumber.put(cargo, count);
            }
            planetsAndCargo.put(i, cargoNumber);
        }
    }

    public void updateOccupiedPlanets() {
        ClientModel model = view.getClient().getModel();

        Map<Integer, UUID> occupiedPlanetsAndPlayers = model.getUnsafeCardDetail("occupiedPlanets");
        occupiedPlanets = occupiedPlanetsAndPlayers.keySet();
    }
    
    private void landOnPlanet(int index) {
        ClientModel model = view.getClient().getModel();

        model.putCardDetail("selectedPlanet", index);
        view.getClient().receiveUserInput(
                new UserInput.UserInputBuilder(UserInputType.PARTICIPATION)
                        .setParticipation(true)
                        .setParticipationChoice(index)
                        .build()
        );
    }
    
    private void skipTurn() {
        view.getClient().receiveUserInput(
                new UserInput.UserInputBuilder(UserInputType.PARTICIPATION)
                        .setParticipation(false)
                        .setParticipationChoice(-1)
                        .build()
        );
    }
}
