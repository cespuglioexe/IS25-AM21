package it.polimi.it.galaxytrucker.gameStates;

import it.polimi.it.galaxytrucker.managers.GameManager;
import it.polimi.it.galaxytrucker.managers.Player;

public class Connection extends State {
    public Connection(GameManager gameManager) {
        super(gameManager);
    }

    @Override
    public void enter() {
        
    }

    @Override
    public void exit() {
        for (Player player : gameManager.getPlayers()) {
            player.createShip(gameManager.getLevel());
        }
    }
}
