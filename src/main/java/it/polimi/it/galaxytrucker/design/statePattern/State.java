package it.polimi.it.galaxytrucker.design.statePattern;

public abstract class State {
    public abstract void enter(StateMachine fsm);

    public abstract void update(StateMachine fsm);

    public abstract void exit(StateMachine fsm);
    
    protected void changeState(StateMachine fsm, State nextState) {
        fsm.changeState(nextState);
    }
}
