package it.polimi.it.galaxytrucker.model.managers;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import it.polimi.it.galaxytrucker.model.aventurecard.AdventureDeck;
import it.polimi.it.galaxytrucker.model.componenttiles.ComponentTile;
import it.polimi.it.galaxytrucker.model.design.statePattern.State;;

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
    public FlightBoardState getFlightBoard();
    public AdventureDeck getAdventureDeck();

    //SETTER
    public void changeState(State nextState);
    public void setLevel(int level);
    public void setNumberOfPlayers(int numberOfPlayers);
    public UUID addPlayer(String name);
    public void removePlayer(UUID id);

    //ACTIONS
    public ComponentTile drawComponentTile();
    public void placeComponentTile(UUID playerID, ComponentTile component, int row, int column);
    public void rotateComponentTile(UUID playerID, int row, int column);
    public void finishBuilding(UUID playerID);
}
