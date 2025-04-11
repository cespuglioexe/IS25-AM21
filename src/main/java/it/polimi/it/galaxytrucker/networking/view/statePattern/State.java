package it.polimi.it.galaxytrucker.networking.view.statePattern;

import it.polimi.it.galaxytrucker.networking.view.listeners.EventListener;

import java.util.Scanner;

public abstract class State {
    protected final EventListener listener;
    protected Scanner scanner = new Scanner(System.in);

    public State(EventListener listener) {
        this.listener = listener;
    }
    public abstract void enter(StateMachine fsm);

    public abstract void update(StateMachine fsm, boolean repeat);

    public abstract void exit(StateMachine fsm);
    
    protected void changeState(StateMachine fsm, State nextState) {
        fsm.changeState(nextState);
    }
}
