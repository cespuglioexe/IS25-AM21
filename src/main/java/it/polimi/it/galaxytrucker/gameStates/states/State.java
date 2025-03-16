package it.polimi.it.galaxytrucker.gameStates.states;

import it.polimi.it.galaxytrucker.managers.GameManager;

public abstract class State {
    protected final GameManager gameManager;

    public State(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    public abstract void enter();
    public abstract void update();
    public abstract void exit();
}