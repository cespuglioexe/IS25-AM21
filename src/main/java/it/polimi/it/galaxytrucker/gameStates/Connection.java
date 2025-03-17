package it.polimi.it.galaxytrucker.gameStates;

import it.polimi.it.galaxytrucker.managers.GameManager;

public class Connection extends State {
    public Connection(GameManager gameManager) {
        super(gameManager);
    }

    @Override
    public void enter() {
        gameManager.getPlayers().getLast().createShip(gameManager.getLevel());
    }

    @Override
    public void exit() {
        
    }
}
