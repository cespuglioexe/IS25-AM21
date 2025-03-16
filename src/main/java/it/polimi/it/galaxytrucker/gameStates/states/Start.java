package it.polimi.it.galaxytrucker.gameStates.states;

import it.polimi.it.galaxytrucker.managers.GameManager;

public class Start extends State {
    private int level;
    private int numberOfPlayers;

    public Start(GameManager gameManager) {
        super(gameManager);
    }

    @Override 
    public void enter() {
        //default match
        this.level = 1;
        this.numberOfPlayers = 4;
    }

    @Override
    public void update() {
        
    }

    @Override
    public void exit() {
        gameManager.setLevel(this.level);
        gameManager.setNumberOfPlayers(this.numberOfPlayers);
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setNumberOfPlayers(int numberOfPlayers) {
        this.numberOfPlayers = numberOfPlayers;
    }
}
