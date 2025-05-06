package it.polimi.it.galaxytrucker.view.cli.statePattern.viewstates;

import it.polimi.it.galaxytrucker.commands.UserInput;
import it.polimi.it.galaxytrucker.commands.UserInputType;
import it.polimi.it.galaxytrucker.view.cli.CLIView;
import it.polimi.it.galaxytrucker.view.cli.ConsoleColors;
import it.polimi.it.galaxytrucker.view.cli.statePattern.State;
import it.polimi.it.galaxytrucker.view.cli.statePattern.StateMachine;

public class NameInput extends State {
    public NameInput(CLIView view) {
        super(view);
    }

    @Override
    public void enter(StateMachine fsm) {
        System.out.print("Insert username\n> ");
        String name = scanner.nextLine();
        view.getClient().receiveUserInput(
                new UserInput.UserInputBuilder(null, UserInputType.SET_USERNAME)
                        .setPlayerName(name)
                        .build());
    }

    @Override
    public void update(StateMachine fsm, boolean repeat) {
        if (repeat) {
            System.out.println(ConsoleColors.RED + "! That name is already in use, please choose a different one" + ConsoleColors.RESET);
            System.out.print("> ");
            String name = scanner.nextLine();
            view.getClient().receiveUserInput(
                    new UserInput.UserInputBuilder(null, UserInputType.SET_USERNAME)
                            .setPlayerName(name)
                            .build());
        }
    }

    @Override
    public void exit(StateMachine fsm) {

    }
}
