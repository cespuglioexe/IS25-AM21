package it.polimi.it.galaxytrucker.view.statePattern;

import it.polimi.it.galaxytrucker.networking.rmi.client.RMIClient;
import it.polimi.it.galaxytrucker.view.CLIView;
import it.polimi.it.galaxytrucker.view.listeners.EventListener;

import java.util.Scanner;

public abstract class State {
    protected final CLIView view;
    protected Scanner scanner = new Scanner(System.in);

    public State(CLIView view) {
        this.view = view;
    }
    public abstract void enter(StateMachine fsm);

    public abstract void update(StateMachine fsm, boolean repeat);

    public abstract void exit(StateMachine fsm);
    
    protected void changeState(StateMachine fsm, State nextState) {
        fsm.changeState(nextState);
    }
}
