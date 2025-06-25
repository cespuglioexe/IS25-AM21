package it.polimi.it.galaxytrucker.view.CLI.CLIViewStates.cardInputStates;

import it.polimi.it.galaxytrucker.messages.clientmessages.UserInput;
import it.polimi.it.galaxytrucker.messages.clientmessages.UserInputType;
import it.polimi.it.galaxytrucker.view.CLI.CLIInputReader;
import it.polimi.it.galaxytrucker.view.CLI.CLIViewStates.CLIViewState;
import it.polimi.it.galaxytrucker.view.GUI.GUIView;
import javafx.fxml.FXML;

public class CLIPartecipationAcceptState extends CLIViewState {

    @Override
    public void executeState() {
        view.executorService.submit(() -> {
            System.out.println("Do you want to participate in the card?");

            System.out.print("""
                    \nChoose an option:
                    [1]: Yes
                    [2]: No
                    >\s""");

            int option = CLIInputReader.readInt();

            switch (option) {
                case 1 -> view.getClient().receiveUserInput(
                        new UserInput.UserInputBuilder(UserInputType.PARTICIPATION)
                                .setParticipation(true)
                                .setParticipationChoice(0)
                                .build()
                );
                case 2 -> view.getClient().receiveUserInput(
                        new UserInput.UserInputBuilder(UserInputType.PARTICIPATION)
                                .setParticipation(false)
                                .setParticipationChoice(-1)
                                .build()
                );
                default -> {
                    System.out.println("That's not a valid option, please choose another");
                    executeState();
                }
            }
        });
    }
}
