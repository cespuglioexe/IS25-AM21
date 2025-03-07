package it.polimi.it.galaxytrucker.gameStates;

import it.polimi.it.galaxytrucker.aventurecard.AdventureCard;
import it.polimi.it.galaxytrucker.componenttiles.ComponentTile;

import java.util.List;

public interface ShipAssembly {
    ComponentTile playerDrawsComponent(int playerID);
    void playerPlacesComponent(int playerID, ComponentTile component);
    void playerDiscardComponent(int playerID, ComponentTile component);
    ComponentTile playerSetsAsideComponent(int playerID, ComponentTile component);
    List<AdventureCard> playerLooksAtCardStack(int playerID, int stackNumber);
    void playerFinishedAssembly(int playerID);
}
