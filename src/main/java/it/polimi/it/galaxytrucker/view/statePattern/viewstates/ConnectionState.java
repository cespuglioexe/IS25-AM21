package it.polimi.it.galaxytrucker.view.statePattern.viewstates;

import it.polimi.it.galaxytrucker.networking.messages.UserInput;
import it.polimi.it.galaxytrucker.networking.messages.UserInputType;
import it.polimi.it.galaxytrucker.networking.rmi.client.RMIClient;
import it.polimi.it.galaxytrucker.view.CLIView;
import it.polimi.it.galaxytrucker.view.listeners.EventListener;
import it.polimi.it.galaxytrucker.view.listeners.EventType;
import it.polimi.it.galaxytrucker.view.listeners.StringEventListener;
import it.polimi.it.galaxytrucker.view.statePattern.State;
import it.polimi.it.galaxytrucker.view.statePattern.StateMachine;

public class ConnectionState extends State {

    public ConnectionState(CLIView view) {
        super(view);
    }

    @Override
    public void enter(StateMachine fsm) {
        System.out.print("Insert server name\n> ");
        String server = scanner.nextLine();
        view.getClient().receiveUserInput(
                new UserInput.UserInputBuilder(null, UserInputType.CONNECT_SERVER)
                        .setServerName(server)
                        .build());
    }

    @Override
    public void update(StateMachine fsm, boolean repeat) {
        if (repeat) {
            this.enter(fsm);
        }
        else {
            fsm.changeState(new NameInput(view));
        }
    }

    @Override
    public void exit(StateMachine fsm) {

    }
}
