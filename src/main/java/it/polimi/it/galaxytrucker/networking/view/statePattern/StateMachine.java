package it.polimi.it.galaxytrucker.networking.view.statePattern;

public class StateMachine {
    private State currentState;

    public void start(State startState) {
        currentState = startState;
        currentState.enter(this);
    }

    public void updateState(boolean repeat) {
        currentState.update(this, repeat);
    }

    public void changeState(State nextState) {
        currentState.exit(this);
        currentState = nextState;
        currentState.enter(this);
    }

    public State getCurrentState() {
        return currentState;
    }
}
