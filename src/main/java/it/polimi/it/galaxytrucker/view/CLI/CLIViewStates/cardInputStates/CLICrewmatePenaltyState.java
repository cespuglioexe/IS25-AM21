package it.polimi.it.galaxytrucker.view.CLI.CLIViewStates.cardInputStates;

import it.polimi.it.galaxytrucker.messages.clientmessages.UserInput;
import it.polimi.it.galaxytrucker.messages.clientmessages.UserInputType;
import it.polimi.it.galaxytrucker.model.componenttiles.CabinModule;
import it.polimi.it.galaxytrucker.model.componenttiles.CentralCabin;
import it.polimi.it.galaxytrucker.model.componenttiles.TileData;
import it.polimi.it.galaxytrucker.model.utility.Coordinates;
import it.polimi.it.galaxytrucker.networking.client.clientmodel.ClientModel;
import it.polimi.it.galaxytrucker.view.CLI.CLIInputReader;
import it.polimi.it.galaxytrucker.view.CLI.CLIViewStates.CLIViewState;
import it.polimi.it.galaxytrucker.view.CLI.ConsoleColors;
import it.polimi.it.galaxytrucker.view.GUI.GUIView;
import javafx.fxml.FXML;

import java.util.ArrayList;
import java.util.List;

public class CLICrewmatePenaltyState extends CLIViewState {

    private List<Coordinates> crewmateCoord = null;
    int numberOfCrewmates;

    @Override
    public void executeState() {
        if (crewmateCoord == null) {
            loadCardDetails();
            System.out.println("You need to sacrifice your crewmates to continue!");
        }

        view.executorService.submit(() -> {
            do {
                System.out.println("You need to sacrifice" + numberOfCrewmates + " more crewmates");
                System.out.println("Which cabin do you want to remove a crewmate from?");

                System.out.print("Column> ");
                int column = CLIInputReader.readInt();
                System.out.print("Row> ");
                int row = CLIInputReader.readInt();

                removeCrewmate(row, column);
            } while (numberOfCrewmates > 0);

            applyPenalty();
        });
    }

    private void loadCardDetails() {
        ClientModel model = view.getClient().getModel();
        crewmateCoord = new ArrayList<>();
        numberOfCrewmates = model.getCardDetail("crewmatePenalty", Integer.class);
    }

    private void removeCrewmate(int row, int col){
        if((row<5) || (row>9) || (col<4) || (col>10)){
            System.out.println(ConsoleColors.YELLOW + "Those aren't valid coordinates" + ConsoleColors.RESET);
            return;
        }

        TileData tile = view.getClient().getModel().getPlayerShips(view.getClient().getModel().getMyData().getPlayerId()).get(row - 5).get(col - 4);
        if (tile != null && (tile.type().equals(CabinModule.class.getSimpleName()) || tile.type().equals(CentralCabin.class.getSimpleName()))) {
            if (!tile.crewmates().isEmpty()) {
                crewmateCoord.add(new Coordinates(col, row));
                numberOfCrewmates--;
            } else {
                System.out.println(ConsoleColors.YELLOW + "Oops, that cabin's empty" + ConsoleColors.RESET);
            }
        } else {
            System.out.println(ConsoleColors.YELLOW + "Oops, there aren't any crewmates there" + ConsoleColors.RESET);

        }
    }

    private void applyPenalty(){
        view.getClient().receiveUserInput(
                new UserInput.UserInputBuilder(UserInputType.CREWMATE_PENALTY)
                        .setRemovedCrewmate(crewmateCoord)
                        .build()
        );
    }
}
