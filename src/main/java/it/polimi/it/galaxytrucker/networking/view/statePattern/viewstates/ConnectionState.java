package it.polimi.it.galaxytrucker.networking.view.statePattern.viewstates;

import it.polimi.it.galaxytrucker.networking.view.listeners.EventListener;
import it.polimi.it.galaxytrucker.networking.view.listeners.EventType;
import it.polimi.it.galaxytrucker.networking.view.listeners.StringEventListener;
import it.polimi.it.galaxytrucker.networking.view.statePattern.State;
import it.polimi.it.galaxytrucker.networking.view.statePattern.StateMachine;

public class ConnectionState extends State {

    public ConnectionState(EventListener listener) {
        super(listener);
    }

    @Override
    public void enter(StateMachine fsm) {
        System.out.print("Insert server name\n> ");
        String server = scanner.nextLine();
        ((StringEventListener)listener).onStringEvent(EventType.SERVER_NAME, server);
    }

    @Override
    public void update(StateMachine fsm, boolean repeat) {
        if (repeat) {
            this.enter(fsm);
        }
        else {
            fsm.changeState(new NameInput(listener));
        }
    }

    @Override
    public void exit(StateMachine fsm) {

    }
}
