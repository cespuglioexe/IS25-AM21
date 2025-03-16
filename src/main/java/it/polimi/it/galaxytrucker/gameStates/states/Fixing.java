package it.polimi.it.galaxytrucker.gameStates.states;

import java.util.List;
import java.util.Optional;

import it.polimi.it.galaxytrucker.managers.GameManager;
import it.polimi.it.galaxytrucker.managers.Player;

public class Fixing extends State {
    private Optional<Player> player;
    private List<Integer> coord;

    public Fixing(GameManager gameManager) {
        super(gameManager);
    }

    @Override
    public void enter() {
        
    }

    @Override
    public void update() {
        player.ifPresent(p -> {
            p.getShipManager().removeComponentTile(this.coord.get(0), this.coord.get(1));
        });
    }

    @Override
    public void exit() {

    }

    public void setPlayer(int id) {
        this.player = Optional.ofNullable(gameManager.getPlayerByID(id));
    }

    public void setComponentCoord(List<Integer> coord) {
        this.coord = coord;
    }

    public void removeComponent(List<Integer> coord) {
        this.setComponentCoord(coord);
        this.update();
    }
}
