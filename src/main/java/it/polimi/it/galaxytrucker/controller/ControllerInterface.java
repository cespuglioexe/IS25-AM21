package it.polimi.it.galaxytrucker.controller;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public interface ControllerInterface {
    int getLevel();
    void placeComponentTile(UUID playerId, int col, int row, int rotation);
    void requestNewComponentTile (UUID playerId);
    void requestSavedComponentTiles (UUID playerId);
    void requestDiscardedComponentTiles (UUID playerId);
    void saveComponentTile (UUID playerId);
    void discardComponentTile (UUID playerId);
    void requestShipBoard (UUID playerId);
    void selectSavedComponentTile(UUID playerId, int index);
    void selectDiscardedComponentTile(UUID playerId, int index);
    UUID getControllerUuid();
    void activateComponent(UUID playerId, HashMap<List<Integer>, List<Integer>> activationHashmap);
    void removeComponentTile(UUID playerId,int col, int row);

    void startBuildPhaseTimer();

    void disconnectPlayer(UUID clientUuid);

    void endPlayerBuilding(UUID clientUuid);
}
