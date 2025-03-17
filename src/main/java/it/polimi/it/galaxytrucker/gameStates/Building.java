package it.polimi.it.galaxytrucker.gameStates;

import it.polimi.it.galaxytrucker.managers.GameManager;

public class Building extends State {
    public Building(GameManager gameManager) {
        super(gameManager);
    }

    @Override
    public void enter() {
        gameManager.initializeFlightBoard();
        gameManager.initializeAdventureDeck();
    }

    @Override
    public void exit() {
        
    }
}
