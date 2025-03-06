package it.polimi.it.galaxytrucker.gameStates;

import java.util.List;

public class TestingClass implements Lobby,LegalityCheck,Fixing,EndGame{

    @Override
    public int countPlayerCredits(int PlayerID) {
        return 0;
    }

    @Override
    public List<Integer> makeLeaderboard() {
        return List.of();
    }

    @Override
    public void playerRemovesComponent(int playerID, int xCoordinate, int yCoordinate) {

    }

    @Override
    public void playerFinishedFixing(int playerID) {

    }

    @Override
    public boolean checkIfPlayerShipIsLegal(int playerID) {
        return false;
    }

    @Override
    public void setNumberOfPlayers(int numberOfPlayers) {

    }

    @Override
    public void setGameLevel(int gameLevel) {

    }

    @Override
    public void addPlayer(int playerID) {

    }
}
