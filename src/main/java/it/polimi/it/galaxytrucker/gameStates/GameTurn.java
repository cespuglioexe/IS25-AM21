package it.polimi.it.galaxytrucker.gameStates;

public interface GameTurn {
    AdventureCard drawAdventureCard();
    void playerPartecipate(int playerID);

}
