package it.polimi.it.galaxytrucker.gameStates;

import it.polimi.it.galaxytrucker.managers.GameManager;

public abstract class State {
    GameManager gameManager;

    public State(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    public abstract void enter();
    public abstract void exit();
}
