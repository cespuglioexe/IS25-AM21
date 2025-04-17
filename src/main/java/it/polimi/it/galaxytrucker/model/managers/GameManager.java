package it.polimi.it.galaxytrucker.model.managers;

import java.io.File;
import java.io.IOException;
import java.rmi.RemoteException;
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
import it.polimi.it.galaxytrucker.model.gameStates.StartState;
import it.polimi.it.galaxytrucker.model.json.Json;
import it.polimi.it.galaxytrucker.model.utility.Color;
import it.polimi.it.galaxytrucker.networking.messages.GameUpdate;
import it.polimi.it.galaxytrucker.networking.messages.GameUpdateType;
import it.polimi.it.galaxytrucker.networking.rmi.server.RMIServer;
import it.polimi.it.galaxytrucker.view.ConsoleColors;;

public class GameManager extends StateMachine implements Model {
    private final Integer level;
    private final Integer numberOfPlayers;
    private final List<Player> players;
    private Set<ComponentTile> components;
    private final List<ComponentTile> discardedComponents;
    private final FlightBoard flightBoard;
    private final AdventureDeck adventureDeck;


    private final RMIServer server;
    private final String nickname;

    private ExecutorService executors = Executors.newCachedThreadPool();

    public GameManager(int level, int numberOfPlayers, RMIServer server, String nickname) {
        this.level = level;
        this.numberOfPlayers = numberOfPlayers;
        this.flightBoard = new FlightBoard(level);
        this.adventureDeck = new AdventureDeck();
        this.discardedComponents = new ArrayList<>();
        this.players = new ArrayList<>();
        this.server = server;
        this.nickname = nickname;

        start(new StartState());
    }

    public void sendGameUpdateToAllPlayers(GameUpdate gameUpdate) {
        executors.execute(() -> {
            try {
                server.sendMessageToAllPlayers(nickname, gameUpdate);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        });
    }

    public void sendGameUpdateToSinglePlayer(UUID playerId, GameUpdate gameUpdate) {
        System.out.println(ConsoleColors.GREEN + "Forwarding message to server of type: " + gameUpdate.getInstructionType() + ConsoleColors.RESET);

        executors.execute(() -> {
            try {
                server.sendMessageToSinglePlayer(nickname, getPlayerByID(playerId).getPlayerName(), gameUpdate);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        });
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
    public void getDiscardedComponentTiles(UUID playerId) {
        System.out.println(ConsoleColors.GREEN + "Received discarded tile request from " + getPlayerByID(playerId).getPlayerName() + ConsoleColors.RESET);
        sendGameUpdateToSinglePlayer(playerId,
                new GameUpdate.GameUpdateBuilder(GameUpdateType.TILE_LIST)
                .setTileList(discardedComponents)
                .build()
        );
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
        if (isGameFull()) {
            throw new InvalidActionException("The game is full");
        }
        ensureNameIsUnique(name);
        Color playerColor = findFirstAvailableColor();

        Player newPlayer = new Player(UUID.randomUUID(), name, playerColor, new ShipManager(level));
        players.add(newPlayer);

        System.out.println("Players: " + players.stream().map(Player::getPlayerName).collect(Collectors.joining(", ")));

        updateState();

        return newPlayer.getPlayerID();
    }

    private void ensureNameIsUnique(String name) throws InvalidActionException {
        boolean isTaken = this.players.stream()
            .anyMatch(player -> player.getPlayerName().equals(name));
        
        if (isTaken) {
            throw new InvalidActionException("Another player named " + name + " is already playing");
        }
    }
    private Color findFirstAvailableColor() throws InvalidActionException {
        return Arrays.stream(Color.values())
            .filter(color -> players.stream()
                .noneMatch(player -> player.getColor().equals(color)))
            .findFirst()
            .orElseThrow(() -> new InvalidActionException("No available color"));
    }
    private boolean isGameFull() {
        return players.size() == numberOfPlayers;
    }

    @Override
    public void removePlayer(UUID id) throws NotFoundException {
        Player playerToRemove = this.players.stream()
            .filter(player -> player.getPlayerID().equals(id))
            .findFirst()
            .orElseThrow(() -> new NotFoundException("Cannot find the player"));

        this.players.remove(playerToRemove);
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

    public void drawComponentTile(UUID playerId) throws InvalidActionException {
        if (components.isEmpty()) {
            throw new InvalidActionException("There are no components left");
        }
        updateState();

        ComponentTile tile = drawRandomComponentTile();
        getPlayerByID(playerId).setHeldComponent(tile);

        sendGameUpdateToSinglePlayer(
                playerId,
                new GameUpdate.GameUpdateBuilder(GameUpdateType.DRAWN_TILE)
                        .setNewTile(tile)
                        .build()
        );
    }

    private ComponentTile drawRandomComponentTile() {
        int index = getRandomIndex(components.size());

        return removeComponentTileAtIndex(index);
    }

    private int getRandomIndex(int upperBoundExclusive) {
        return new Random().nextInt(upperBoundExclusive);
    }

    private ComponentTile removeComponentTileAtIndex(int index) throws IndexOutOfBoundsException {
        Iterator<ComponentTile> iterator = components.iterator();
        int currentIndex = 0;
    
        while (iterator.hasNext()) {
            ComponentTile tile = iterator.next();

            if (currentIndex++ == index) {
                iterator.remove();
                return tile;
            }
        }
    
        throw new IndexOutOfBoundsException("Index " + index + " is out of bounds");
    }

    public void placeComponentTile(UUID playerID, int row, int column) throws IndexOutOfBoundsException, IllegalComponentPositionException {
        Player player = this.getPlayerByID(playerID);
        ShipManager ship = player.getShipManager();

        ComponentTile comp = player.getHeldComponent();

        ship.addComponentTile(row, column, comp);
        player.setHeldComponent(null);

        System.out.println(ConsoleColors.GREEN + "Placed " + comp.getClass().getSimpleName() + row + ", " + column);
    }

    public void rotateComponentTile(UUID playerID, int row, int column) throws IndexOutOfBoundsException, IllegalComponentPositionException {
        Player player = getPlayerByID(playerID);
        ShipManager ship = player.getShipManager();

        if (ship.isOutside(row, column)) {
            throw new IllegalComponentPositionException("Position (" + row + ", " + column + ") is outside the ship.");
        }

        ship.getComponent(row, column).ifPresentOrElse(
            ComponentTile::rotate,
            () -> {
                throw new IllegalComponentPositionException("No component found at position (" + row + ", " + column + ").");
            }
        );

        System.out.println("Component rotated");
    }

    public void finishBuilding(UUID playerID) {
        updateState();
    }

    @Override
    public void saveComponentTile(UUID playerId) {
        Player player = getPlayerByID(playerId);
        player.getShipManager().saveComponentTile(player.getHeldComponent());
        player.setHeldComponent(null);
    }

    @Override
    public void discardComponentTile(UUID playerId) {
        discardedComponents.add(getPlayerByID(playerId).getHeldComponent());
        System.out.println("Discarded " + getPlayerByID(playerId).getHeldComponent().getClass().getSimpleName() + " component");
        getPlayerByID(playerId).setHeldComponent(null);
        System.out.println(discardedComponents);
    }

    @Override
    public void selectSavedComponentTile(UUID playerId, int index) {
        System.out.println("Selected saved tile (model)");

        Player player = getPlayerByID(playerId);
        ComponentTile comp = player.getShipManager().getSavedComponentTile(index);
        player.setHeldComponent(comp);
    }

    @Override
    public void selectDiscardedComponentTile(UUID playerId, int index) {
        Player player = getPlayerByID(playerId);
        ComponentTile comp = discardedComponents.get(index);
        discardedComponents.remove(index);
        player.setHeldComponent(comp);
    }
}
