package it.polimi.it.galaxytrucker.gameStates;

public interface Fixing {
    void playerRemovesComponent(int playerID, int xCoordinate, int yCoordinate);
    void playerFinishedFixing(int playerID);
}
