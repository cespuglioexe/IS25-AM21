package it.polimi.it.galaxytrucker.gameStates.states;

import java.util.Optional;

import it.polimi.it.galaxytrucker.managers.GameManager;
import it.polimi.it.galaxytrucker.managers.Player;

public class Connection extends State {
    private Optional<Player> player;
    
    public Connection(GameManager gameManager) {
        super(gameManager);
        this.player = Optional.empty();
    }

    @Override
    public void enter() {

    }

    @Override
    public void update() {
        this.player.ifPresentOrElse(
            player -> {
                if (gameManager.getPlayers().contains(player)) {
                    gameManager.removePlayer(player);
                } else {
                    gameManager.addNewPlayer(player);
                }
            }, 
            () -> {}
            );
    }

    @Override
    public void exit() {

    }

    public void setPlayer(Player player) {
        this.player = Optional.of(player);
    }
}
