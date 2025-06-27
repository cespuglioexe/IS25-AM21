package it.polimi.it.galaxytrucker.view;

import it.polimi.it.galaxytrucker.controller.GenericGameData;
import it.polimi.it.galaxytrucker.model.componenttiles.TileData;
import it.polimi.it.galaxytrucker.networking.client.ClientInterface;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public abstract class View {
    private ClientInterface client = null;

    public void setClient(ClientInterface client) {
        if (this.client == null)
            this.client = client;
    }

    public ClientInterface getClient() {
        return client;
    }

    public abstract void repromptState();

    public abstract void begin();

    public abstract void titleScreen();

    public abstract void displayShip(List<List<TileData>> ship);

    public abstract void displayTiles(List<TileData> tiles);

    public abstract void displayCards(List<String> cards);

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

    public abstract void activeControllers(List<GenericGameData> activeControllers);

    public abstract void shipUpdated(UUID interestedPlayerId);

    public abstract void componentTileReceived(TileData newTile);

    public abstract void savedComponentsUpdated();

    public abstract void discardedComponentsUpdated();

    public abstract void nameSelectionSuccess();

    public abstract void shipFixingState();

    public abstract void waitingForGameState();

    public abstract void newCardStartedExecution();

    public abstract void displayInputOptions(String card, String cardState);

    public abstract void showScoreBoard();

    public abstract void showSleepView();

    public abstract void addCrewmates();

    public abstract void displayCardUpdates(String card, String cardState, Map<String, Object> cardDetails);

    public abstract void manageInputError();

    public abstract void startNewTurn();

    public abstract void loadingScreen();

    public abstract void showOtherShip();
}