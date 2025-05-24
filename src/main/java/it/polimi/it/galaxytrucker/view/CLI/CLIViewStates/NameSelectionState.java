package it.polimi.it.galaxytrucker.view.CLI.CLIViewStates;

import it.polimi.it.galaxytrucker.messages.clientmessages.UserInput;
import it.polimi.it.galaxytrucker.messages.clientmessages.UserInputType;
import it.polimi.it.galaxytrucker.view.CLI.CLIInputReader;
import it.polimi.it.galaxytrucker.view.CLI.ConsoleColors;

public class NameSelectionState extends CLIViewState {
    @Override
    public void executeState() {
        view.executorService.submit(() -> {
            System.out.print("Insert username\n> ");
            String name = CLIInputReader.readString();

            view.getClient().receiveUserInput(
                    new UserInput.UserInputBuilder(UserInputType.SET_PLAYER_USERNAME)
                            .setPlayerName(name)
                            .build());
        });
    }

    @Override
    public void nameNotAvailable() {
        System.out.println(ConsoleColors.RED + "That username is not available. Please choose another one." + ConsoleColors.RESET);
        executeState();
    }
}
