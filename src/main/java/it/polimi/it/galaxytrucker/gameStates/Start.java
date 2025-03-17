package it.polimi.it.galaxytrucker.gameStates;

import it.polimi.it.galaxytrucker.managers.GameManager;

public class Start extends State {
    public Start(GameManager gameManager) {
        super(gameManager);
    }

    @Override
    public void enter() {
        
    }

    @Override
    public void exit() {
        gameManager.initializeGameSpecifics();
    }
}
