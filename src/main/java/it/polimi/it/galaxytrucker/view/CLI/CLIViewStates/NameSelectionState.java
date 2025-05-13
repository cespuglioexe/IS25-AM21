package it.polimi.it.galaxytrucker.view.CLI.CLIViewStates;

import it.polimi.it.galaxytrucker.commands.UserInput;
import it.polimi.it.galaxytrucker.commands.UserInputType;
import it.polimi.it.galaxytrucker.view.CLI.ConsoleColors;

public class NameSelectionState extends CLIViewState {

    @Override
    public void executeState() {
        System.out.print("Insert username\n> ");
        String name = scanner.nextLine();

        view.getClient().receiveUserInput(
                new UserInput.UserInputBuilder(null, UserInputType.SET_USERNAME)
                        .setPlayerName(name)
                        .build());
    }

    @Override
    public void nameNotAvailable() {
        System.out.println(ConsoleColors.RED + "That username is not available. Please choose another one." + ConsoleColors.RESET);
        executeState();
    }
}
