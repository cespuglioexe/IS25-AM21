package it.polimi.it.galaxytrucker.gameStates;

public interface Lobby {
    void setNumberOfPlayers(int numberOfPlayers);
    void setGameLevel(int gameLevel);
    void addPlayer(int playerID);
}
