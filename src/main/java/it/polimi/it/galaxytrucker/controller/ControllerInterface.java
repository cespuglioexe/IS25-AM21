package it.polimi.it.galaxytrucker.controller;

import java.util.UUID;

public interface ControllerInterface {
    void placeComponentTile(UUID playerId, int col, int row, int rotation);
    void requestNewComponentTile (UUID playerId);
    void requestSavedComponentTiles (UUID playerId);
    void requestDiscardedComponentTiles (UUID playerId);
    void saveComponentTile (UUID playerId);
    void discardComponentTile (UUID playerId);
    void requestShipBoard (UUID playerId);
    void selectSavedComponentTile(UUID playerId, int index);
    void selectDiscardedComponentTile(UUID playerId, int index);
    void getCardPile (UUID playerId, int pileIndex);
    UUID getControllerUuid();

    void startBuildPhaseTimer();
}
