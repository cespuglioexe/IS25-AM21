package it.polimi.it.galaxytrucker.model.managers;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.JsonNode;

import it.polimi.it.galaxytrucker.model.adventurecards.AdventureDeck;
import it.polimi.it.galaxytrucker.model.componenttiles.ComponentTile;
import it.polimi.it.galaxytrucker.model.design.statePattern.StateMachine;
import it.polimi.it.galaxytrucker.model.exceptions.IllegalComponentPositionException;
import it.polimi.it.galaxytrucker.model.exceptions.InvalidActionException;
import it.polimi.it.galaxytrucker.model.exceptions.NotFoundException;
import it.polimi.it.galaxytrucker.model.gameStates.GameState;
import it.polimi.it.galaxytrucker.model.gameStates.StartState;
import it.polimi.it.galaxytrucker.model.json.Json;
import it.polimi.it.galaxytrucker.model.utility.Color;
import it.polimi.it.galaxytrucker.commands.servercommands.GameUpdate;
import it.polimi.it.galaxytrucker.commands.servercommands.GameUpdateType;
// import it.polimi.it.galaxytrucker.networking.rmi.server.RMIServer;
import it.polimi.it.galaxytrucker.view.cli.ConsoleColors;;

public class GameManager extends StateMachine implements Model {
    private final Integer level;
    private final Integer numberOfPlayers;
    private final List<Player> players;
    private Set<ComponentTile> components;
    private final FlightBoard flightBoard;
    private final AdventureDeck adventureDeck;


    // private final RMIServer server;
    private final String nickname;

    private ExecutorService executors = Executors.newCachedThreadPool();

    public GameManager(int level, int numberOfPlayers /*, RMIServer server */, String nickname) {
        this.level = level;
        this.numberOfPlayers = numberOfPlayers;
        this.flightBoard = new FlightBoard(level);
        this.adventureDeck = new AdventureDeck();
        this.players = new ArrayList<>();
        // this.server = server;
        this.nickname = nickname;

        start(new StartState());
    }

    public void sendGameUpdateToAllPlayers(GameUpdate gameUpdate) {
//        executors.execute(() -> {
//            try {
//                server.sendMessageToAllPlayers(nickname, gameUpdate);
//            } catch (RemoteException e) {
//                e.printStackTrace();
//            }
//        });
    }

    public void sendGameUpdateToSinglePlayer(UUID playerId, GameUpdate gameUpdate) {
        System.out.println(ConsoleColors.GREEN + "Forwarding message to server of type: " + gameUpdate.getInstructionType() + ConsoleColors.RESET);

//        executors.execute(() -> {
//            try {
//                server.sendMessageToSinglePlayer(nickname, getPlayerByID(playerId).getPlayerName(), gameUpdate);
//            } catch (RemoteException e) {
//                e.printStackTrace();
//            }
//        });
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
        System.out.println(">> Number of connected players: " + players.size());
        return this.players.size() == this.numberOfPlayers ? true : false;
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
        sendGameUpdateToSinglePlayer(playerId,
                new GameUpdate.GameUpdateBuilder(GameUpdateType.TILE_LIST)
                        .setTileList(getPlayerByID(playerId).getShipManager().getSavedComponentTiles())
                        .build()
        );
    }

    @Override
    public List<ComponentTile> getDiscardedComponentTiles() {
        GameState gameState = (GameState) this.getCurrentState();

        return gameState.getDiscardedComponentTiles();
    }


    @Override
    public void getPlayerShipBoard(UUID playerId) {
        sendGameUpdateToSinglePlayer(
                playerId,
                new GameUpdate.GameUpdateBuilder(GameUpdateType.SHIP_DATA)
                        .setShipBoard(getPlayerShip(playerId).getShipBoard())
                        .build()
        );
        System.out.println(ConsoleColors.GREEN + "Sent ShipBoard update to " + getPlayerByID(playerId).getPlayerName() + ConsoleColors.RESET);
    }


    /**
     * Adds a new player to the game. Each player is given a random available color.
     *
     * @param name The name of the new player
     * @return The {@code UUID} generated for the added player
     * @throws InvalidActionException if the game is already full
     */
    @Override
    public UUID addPlayer(String name) throws InvalidActionException {
        GameState gameState = (GameState) this.getCurrentState();
        return gameState.addPlayer(this, name);
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

        // TODO: notify player listeners that a component was drawn
    }

    public void placeComponentTile(UUID playerID, int row, int column) throws IndexOutOfBoundsException, IllegalComponentPositionException {
        GameState gameState = (GameState) this.getCurrentState();
        gameState.placeComponentTile(this, playerID, row, column);

        // TODO: notify all model listeners that a component was drawn
    }

    public void rotateComponentTile(UUID playerID, int row, int column) throws IndexOutOfBoundsException, IllegalComponentPositionException {
        GameState gameState = (GameState) this.getCurrentState();
        gameState.rotateComponentTile(this, playerID, row, column);

        // TODO: notify all model listeners that a component was rotated
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

        // TODO: notify
    }

    @Override
    public void discardComponentTile(UUID playerID) {
        GameState gameState = (GameState) this.getCurrentState();
        gameState.discardComponentTile(this, playerID);

        // TODO: notify
    }

    @Override
    public void selectSavedComponentTile(UUID playerID, int index) {
        GameState gameState = (GameState) this.getCurrentState();
        gameState.selectSavedComponentTile(this, playerID, index);

        // TODO: notify
    }

    @Override
    public void selectDiscardedComponentTile(UUID playerID, int index) {
        GameState gameState = (GameState) this.getCurrentState();
        gameState.selectDiscardedComponentTile(this, playerID, index);

        // TODO: notify
    }

    @Override
    public void deleteComponentTile(UUID playerID, int row, int column) {
        GameState gameState = (GameState) this.getCurrentState();
        gameState.deleteComponentTile(this, playerID, row, column);

        // TODO: notify
    }
}
