package it.polimi.it.galaxytrucker.view;

import it.polimi.it.galaxytrucker.model.componenttiles.TileData;
import it.polimi.it.galaxytrucker.networking.client.Client;

import java.util.List;

public abstract class View {
    private Client client = null;

    public void setClient(Client client) {
        if (this.client == null)
            this.client = client;
    }

    public Client getClient() {
        return client;
    }

    public abstract void repromptState();

    public abstract void begin();

    public abstract void titleScreen();

    public abstract void displayShip(List<List<TileData>> ship);

    public abstract void displayTiles(List<TileData> tiles);

    public abstract void displayCards(List<Integer> cards);

    public abstract void nameNotAvailable();

    public abstract void buildingStarted();

    public abstract void gameSelectionScreen();

    public abstract void gameCreationSuccess(boolean success);

    public abstract void joinedGameIsFull();

    public abstract void remoteExceptionThrown();

    public abstract void displayComponentTile(TileData newTile);

    public abstract void tileActions();

    public abstract void displayTimerStarted();

    public abstract void displayTimerEnded();
}