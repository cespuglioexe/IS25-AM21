package it.polimi.it.galaxytrucker.model.managers;

import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import it.polimi.it.galaxytrucker.model.adventurecards.AdventureDeck;
import it.polimi.it.galaxytrucker.model.componenttiles.ComponentTile;
import it.polimi.it.galaxytrucker.model.design.statePattern.State;
import it.polimi.it.galaxytrucker.exceptions.InvalidActionException;
import it.polimi.it.galaxytrucker.model.utility.Coordinates;
;

public interface Model {
    //GETTER
    State getCurrentState();
    Integer getLevel();
    Integer getNumberOfPlayers();
    List<Player> getPlayers();
    Player getPlayerByID(UUID id);
    List<Player> getPlayerRank();
    boolean allPlayersConnected();
    ShipManager getPlayerShip(UUID id);
    Set<Player> getPlayersWithIllegalShips();
    Set<ComponentTile> getComponentTiles();
    FlightBoard getFlightBoard();
    AdventureDeck getAdventureDeck();
    void getSavedComponentTiles(UUID playerId);
    List<ComponentTile> getDiscardedComponentTiles(UUID playerId);
    void getPlayerShipBoard(UUID playerId);

    //SETTER
     void changeState(State nextState);
     void addPlayer(Player player) throws InvalidActionException;
     void removePlayer(UUID id, int col, int row);

    //ACTIONS
     void drawComponentTile(UUID playerId) throws InvalidActionException;

     void placeComponentTile(UUID playerID, int row, int column, int rotation);
     void rotateComponentTile(UUID playerID, int row, int column);
     void finishBuilding(UUID playerID);
     void saveComponentTile(UUID playerID);
     void discardComponentTile(UUID playerId);

     void selectSavedComponentTile (UUID playerId, int index);
     void selectDiscardedComponentTile (UUID playerId, int index);

     void deleteComponentTile(UUID playerID, int row, int column);
     public void deleteBranch(UUID playerID, Set<List<Integer>> branch);

     void startBuildPhaseTimer();
     void activateCannon(UUID playerID, List<List<Coordinates>> activationHashmap);
     void activateEngine(UUID playerID, List<List<Coordinates>> activationHashmap);
     void activateShield(UUID playerID, List<List<Coordinates>> activationHashmap);
     void manageAcceptedCargo(UUID playerId,HashMap<Integer,Coordinates> acceptedCargo);
     void manageCreditChoice(UUID playerId,boolean creditChoice);
     void manageRemovedCrewmate(UUID  playerId, List<Coordinates> removedCrewmate);
     void manageParticipation(UUID  playerId, boolean participation, int choice);

}
