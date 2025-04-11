package it.polimi.it.galaxytrucker.networking.view.statePattern.viewstates;

import it.polimi.it.galaxytrucker.networking.view.listeners.EventListener;
import it.polimi.it.galaxytrucker.networking.view.listeners.EventType;
import it.polimi.it.galaxytrucker.networking.view.listeners.StringEventListener;
import it.polimi.it.galaxytrucker.networking.view.statePattern.State;
import it.polimi.it.galaxytrucker.networking.view.statePattern.StateMachine;

public class NameInput extends State {
    public NameInput(EventListener listener) {
        super(listener);
    }

    @Override
    public void enter(StateMachine fsm) {
        System.out.print("Insert username\n> ");
        String server = scanner.nextLine();
        ((StringEventListener)listener).onStringEvent(EventType.USERNAME, server);
    }

    @Override
    public void update(StateMachine fsm, boolean repeat) {
        if (repeat) {
            System.out.print("Insert username\n> ");
            String server = scanner.nextLine();
            ((StringEventListener)listener).onStringEvent(EventType.USERNAME, server);
        }
        else {
            fsm.changeState(new GameSelection(listener));
        }
    }

    @Override
    public void exit(StateMachine fsm) {

    }
}
