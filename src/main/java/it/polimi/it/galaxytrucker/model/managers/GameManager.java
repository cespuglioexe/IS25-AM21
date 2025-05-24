package it.polimi.it.galaxytrucker.model.managers;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.JsonNode;

import it.polimi.it.galaxytrucker.listeners.Listener;
import it.polimi.it.galaxytrucker.listeners.Observable;
import it.polimi.it.galaxytrucker.model.adventurecards.AdventureDeck;
import it.polimi.it.galaxytrucker.model.componenttiles.ComponentTile;
import it.polimi.it.galaxytrucker.model.design.statePattern.StateMachine;
import it.polimi.it.galaxytrucker.model.gamestates.GameState;
import it.polimi.it.galaxytrucker.model.gamestates.StartState;
import it.polimi.it.galaxytrucker.exceptions.IllegalComponentPositionException;
import it.polimi.it.galaxytrucker.exceptions.InvalidActionException;
import it.polimi.it.galaxytrucker.exceptions.NotFoundException;
import it.polimi.it.galaxytrucker.model.json.Json;
import it.polimi.it.galaxytrucker.messages.servermessages.GameUpdate;
import it.polimi.it.galaxytrucker.messages.servermessages.GameUpdateType;
// import it.polimi.it.galaxytrucker.networking.rmi.server.RMIServer;
;

public class GameManager extends StateMachine implements Model, Observable {
    private final Integer level;
    private final Integer numberOfPlayers;
    private final List<Player> players;
    private Set<ComponentTile> components;
    private final FlightBoard flightBoard;
    private final AdventureDeck adventureDeck;

    private final List<Listener> listeners;

    public GameManager(int level, int numberOfPlayers) {
        this.level = level;
        this.numberOfPlayers = numberOfPlayers;
        this.flightBoard = new FlightBoard(level);
        this.adventureDeck = new AdventureDeck();
        this.players = new ArrayList<>();

        this.listeners = new ArrayList<>();

        start(new StartState());
    }

    @Override
    public Integer getLevel() {
        return this.level;
    }

    @Override
    public Integer getNumberOfPlayers() {
        return this.numberOfPlayers;
    }

    @Override
    public List<Player> getPlayers() {
        return this.players;
    }

    @Override
    public Player getPlayerByID(UUID id) {
        return this.players.stream()
            .filter(player -> player.getPlayerID().equals(id))
            .findFirst()
            .orElse(null);
    }

    @Override
    public boolean allPlayersConnected() {
        return this.players.size() == this.numberOfPlayers;
    }

    @Override
    public ShipManager getPlayerShip(UUID id) {
        return Optional.ofNullable(this.getPlayerByID(id).getShipManager())
            .orElse(null);
    }

    @Override
    public Set<Player> getPlayersWithIllegalShips() {
        return this.players.stream()
            .filter(player -> !player.getShipManager().isShipLegal())
            .collect(Collectors.toSet());
    }

    @Override
    public Set<ComponentTile> getComponentTiles() {
        return this.components;
    }

    @Override
    public FlightBoard getFlightBoard() {
        return this.flightBoard;
    }

    @Override
    public AdventureDeck getAdventureDeck() {
        return this.adventureDeck;
    }

    @Override
    public void getSavedComponentTiles(UUID playerId) {
//        sendGameUpdateToSinglePlayer(playerId,
//                new GameUpdate.GameUpdateBuilder(GameUpdateType.TILE_LIST)
//                        .setTileList(getPlayerByID(playerId).getShipManager().getSavedComponentTiles())
//                        .build()
//        );
    }

    @Override
    public List<ComponentTile> getDiscardedComponentTiles(UUID playerId) {
        GameState gameState = (GameState) this.getCurrentState();

        return gameState.getDiscardedComponentTiles();
    }


    @Override
    public void getPlayerShipBoard(UUID playerId) {
//        sendGameUpdateToSinglePlayer(
//                playerId,
//                new GameUpdate.GameUpdateBuilder(GameUpdateType.SHIP_DATA)
//                        .setShipBoard(getPlayerShip(playerId).getShipBoard())
//                        .build()
//        );
    }


    /**
     * Adds a new player to the game.
     *
     * @throws InvalidActionException if the game is already full
     */
    @Override
    public void addPlayer(Player player) throws InvalidActionException {
        GameState gameState = (GameState) this.getCurrentState();
        gameState.addPlayer(this, player);
    }

    @Override
    public void removePlayer(UUID id) throws NotFoundException {
        GameState gameState = (GameState) this.getCurrentState();
        gameState.removePlayer(this, id);
    }

    public void initializeComponentTiles() {
        File file = new File("src/main/resources/it/polimi/it/galaxytrucker/json/componenttiles.json");

        try {
            JsonNode node = Json.parse(file);
            components = Json.fromJsonSet(node, ComponentTile.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void drawComponentTile(UUID playerID) throws InvalidActionException {
        GameState gameState = (GameState) this.getCurrentState();
        gameState.drawComponentTile(this, playerID);
    }

    @Override
    public void placeComponentTile(UUID playerID, int row, int column, int rotation) throws IndexOutOfBoundsException, IllegalComponentPositionException {
        GameState gameState = (GameState) this.getCurrentState();
        gameState.placeComponentTile(this, playerID, row, column);

        for(int i = 0; i < rotation; i++) {
            gameState.rotateComponentTile(this, playerID, row, column);
        }

        updateListeners(new GameUpdate.GameUpdateBuilder(GameUpdateType.PLAYER_SHIP_UPDATED)
                .setShipBoard(getPlayerShip(playerID).getShipBoard())
                .setInterestedPlayerId(playerID)
                .build()
        );
    }

    public void rotateComponentTile(UUID playerID, int row, int column) throws IndexOutOfBoundsException, IllegalComponentPositionException {
        GameState gameState = (GameState) this.getCurrentState();
        gameState.rotateComponentTile(this, playerID, row, column);
    }

    public void finishBuilding(UUID playerID) {
        GameState gameState = (GameState) this.getCurrentState();
        gameState.finishBuilding(this, playerID);

        // TODO: notify
    }

    @Override
    public void saveComponentTile(UUID playerID) {
        GameState gameState = (GameState) this.getCurrentState();
        gameState.saveComponentTile(this, playerID);
        
        Player player = getPlayerByID(playerID);
        player.updateListeners(
                new GameUpdate.GameUpdateBuilder(GameUpdateType.SAVED_COMPONENTS_UPDATED)
                        .setTileList(player.getShipManager().getSavedComponentTiles())
                        .build()
        );
    }

    @Override
    public void discardComponentTile(UUID playerID) {
        GameState gameState = (GameState) this.getCurrentState();
        gameState.discardComponentTile(this, playerID);

        updateListeners(
                new GameUpdate.GameUpdateBuilder(GameUpdateType.DISCARDED_COMPONENTS_UPDATED)
                        .setTileList(((GameState) getCurrentState()).getDiscardedComponentTiles())
                        .build()
        );
    }

    @Override
    public void selectSavedComponentTile(UUID playerID, int index) {
        GameState gameState = (GameState) this.getCurrentState();
        gameState.selectSavedComponentTile(this, playerID, index);

        Player player = getPlayerByID(playerID);
        player.updateListeners(
                new GameUpdate.GameUpdateBuilder(GameUpdateType.SAVED_COMPONENTS_UPDATED)
                        .setTileList(player.getShipManager().getSavedComponentTiles())
                        .build()
        );
    }

    @Override
    public void selectDiscardedComponentTile(UUID playerID, int index) {
        GameState gameState = (GameState) this.getCurrentState();
        gameState.selectDiscardedComponentTile(this, playerID, index);

        updateListeners(
                new GameUpdate.GameUpdateBuilder(GameUpdateType.DISCARDED_COMPONENTS_UPDATED)
                        .setTileList(((GameState) getCurrentState()).getDiscardedComponentTiles())
                        .build()
        );
    }

    @Override
    public void deleteComponentTile(UUID playerID, int row, int column) {
        GameState gameState = (GameState) this.getCurrentState();
        gameState.deleteComponentTile(this, playerID, row, column);

        // TODO: notify
    }

    @Override
    public void startBuildPhaseTimer() {
        GameState gameState = (GameState) this.getCurrentState();
        gameState.startBuildPhaseTimer(this);
    }

    @Override
    public void addListener(Listener listener) {
        synchronized (listeners) {
            listeners.add(listener);
        }
    }

    @Override
    public void removeListener(Listener listener) {
        synchronized (listeners) {
            listeners.remove(listener);
        }
    }

    public void updateListeners(GameUpdate command) {
        synchronized (listeners) {
            for (Listener listener : listeners) {
                listener.notify(command);
            }
        }
    }
}
