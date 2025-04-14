package it.polimi.it.galaxytrucker.view.statePattern.viewstates;

import it.polimi.it.galaxytrucker.networking.messages.UserInput;
import it.polimi.it.galaxytrucker.networking.messages.UserInputType;
import it.polimi.it.galaxytrucker.networking.rmi.client.RMIClient;
import it.polimi.it.galaxytrucker.view.CLIView;
import it.polimi.it.galaxytrucker.view.ConsoleColors;
import it.polimi.it.galaxytrucker.view.listeners.EventListener;
import it.polimi.it.galaxytrucker.view.listeners.EventType;
import it.polimi.it.galaxytrucker.view.listeners.StringEventListener;
import it.polimi.it.galaxytrucker.view.statePattern.State;
import it.polimi.it.galaxytrucker.view.statePattern.StateMachine;

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
