package it.polimi.it.galaxytrucker.gameStates.states;

import java.util.Optional;
import java.util.List;

import it.polimi.it.galaxytrucker.componenttiles.ComponentTile;
import it.polimi.it.galaxytrucker.exceptions.IllegalComponentPositionException;
import it.polimi.it.galaxytrucker.managers.GameManager;
import it.polimi.it.galaxytrucker.managers.Player;

public class Building extends State {
    private Optional<Player> player;
    private ComponentTile placedComponent;
    private List<Integer> coord;

    public Building(GameManager gameManager) {
        super(gameManager);
    }

    @Override
    public void enter() {
        gameManager.getPlayers().stream()
            .forEach(player -> player.createShip(gameManager.getLevel()));
    }

    @Override
    public void update() throws IndexOutOfBoundsException, IllegalComponentPositionException {
        this.player.ifPresent(p -> {
            p.getShipManager().addComponentTile(this.coord.get(0), 0, this.placedComponent);
        });
    }

    @Override
    public void exit() {

    }

    public void setPlayer(int id) {
        this.player = Optional.ofNullable(gameManager.getPlayerByID(id));
    }

    public void setPlacedComponent(ComponentTile placedComponent) {
        this.placedComponent = placedComponent;
    }

    public void setComponentCoord(List<Integer> coord) {
        this.coord = coord;
    }

    public void placeComponent(List<Integer> coord, ComponentTile component) {
        this.setComponentCoord(coord);
        this.setPlacedComponent(component);
        this.update();
    }
}
