package it.polimi.it.galaxytrucker.gameStates;

import it.polimi.it.galaxytrucker.aventurecard.AdventureCard;

public interface GameTurn {
    AdventureCard drawAdventureCard();
    void playerPartecipate(int playerID);

}
