package it.polimi.it.galaxytrucker.model.managers;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import it.polimi.it.galaxytrucker.model.adventurecards.AdventureDeck;
import it.polimi.it.galaxytrucker.model.componenttiles.ComponentTile;
import it.polimi.it.galaxytrucker.model.componenttiles.TileData;
import it.polimi.it.galaxytrucker.model.design.statePattern.State;
import it.polimi.it.galaxytrucker.model.exceptions.InvalidActionException;
import it.polimi.it.galaxytrucker.networking.messages.GameUpdate;;

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
    public void getSavedComponentTiles(UUID playerId);
    public void getDiscardedComponentTiles(UUID playerId);
    public void getPlayerShipBoard(UUID playerId);


    //SETTER
    public void changeState(State nextState);
    public UUID addPlayer(String name) throws InvalidActionException;
    public void removePlayer(UUID id);

    //ACTIONS
    public void drawComponentTile(UUID playerId) throws InvalidActionException;

    public void placeComponentTile(UUID playerID, int row, int column);
    public void rotateComponentTile(UUID playerID, int row, int column);
    public void finishBuilding(UUID playerID);
    public void saveComponentTile(UUID playerID);
    public void discardComponentTile(UUID playerId);

    public void selectSavedComponentTile (UUID playerId, int index);
    public void selectDiscardedComponentTile (UUID playerId, int index);

}
