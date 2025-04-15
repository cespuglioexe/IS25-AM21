package it.polimi.it.galaxytrucker.model.managers;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import it.polimi.it.galaxytrucker.model.adventurecards.AdventureDeck;
import it.polimi.it.galaxytrucker.model.componenttiles.ComponentTile;
import it.polimi.it.galaxytrucker.model.design.statePattern.State;
import it.polimi.it.galaxytrucker.model.exceptions.InvalidActionException;;

public interface Model {
    //GETTER
    public State getCurrentState();
    public Integer getLevel();
    public Integer getNumberOfPlayers();
    public List<Player> getPlayers();
    public Player getPlayerByID(UUID id);
    public boolean allPlayersConnected();
    public ShipManager getPlayerShip(UUID id);
    public Set<Player> getPlayersWithIllegalShips();
    public Set<ComponentTile> getComponentTiles();
    public FlightBoard getFlightBoard();
    public AdventureDeck getAdventureDeck();

    //SETTER
    public void changeState(State nextState);
    public UUID addPlayer(String name) throws InvalidActionException;
    public void removePlayer(UUID id);    

    //ACTIONS
    public ComponentTile drawComponentTile();
    public void placeComponentTile(UUID playerID, ComponentTile component, int row, int column);
    public void rotateComponentTile(UUID playerID, int row, int column);
    public void finishBuilding(UUID playerID);
}
