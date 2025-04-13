package it.polimi.it.galaxytrucker.model.managers;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.JsonNode;
import it.polimi.it.galaxytrucker.model.aventurecard.AdventureDeck;
import it.polimi.it.galaxytrucker.model.componenttiles.ComponentTile;
import it.polimi.it.galaxytrucker.model.design.statePattern.StateMachine;
import it.polimi.it.galaxytrucker.model.exceptions.IllegalComponentPositionException;
import it.polimi.it.galaxytrucker.model.exceptions.InvalidActionException;
import it.polimi.it.galaxytrucker.model.exceptions.NotFoundException;
import it.polimi.it.galaxytrucker.model.gameStates.StartState;
import it.polimi.it.galaxytrucker.model.json.Json;
import it.polimi.it.galaxytrucker.model.utility.Color;;

public class GameManager extends StateMachine implements Model {
    private final Integer level;
    private final Integer numberOfPlayers;
    private final List<Player> players;
    private Set<ComponentTile> components;
    private final FlightBoard flightBoard;
    private final AdventureDeck adventureDeck;

    public GameManager(int level, int numberOfPlayers){
        this.level = level;
        this.numberOfPlayers = numberOfPlayers;
        this.flightBoard = new FlightBoard(level);
        this.adventureDeck = new AdventureDeck();
        this.players = new ArrayList<>();

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
    public UUID addPlayer(String name) throws InvalidActionException {
        // ensureNameIsUnique(name);
        System.out.println("Adding player " + name + "(model)");
        Color playerColor = findFirstAvailableColor();

        System.out.println("Create new player");
        Player newPlayer = new Player(UUID.randomUUID(), name, playerColor, new ShipManager(level));
        System.out.println("before add player to game");
        players.add(newPlayer);

        System.out.println("Players: " + players.stream().map(Player::getPlayerName).collect(Collectors.joining(", ")));

        updateState();

        System.out.println("Added player " + name + " with color " + playerColor);
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

    @Override
    public void removePlayer(UUID id) throws NotFoundException {
        Player playerToRemove = this.players.stream()
            .filter(player -> player.getPlayerID().equals(id))
            .findFirst()
            .orElseThrow(() -> new NotFoundException("Cannot find the player"));

        this.players.remove(playerToRemove);
    }

//    public void initializeGameSpecifics() {
//        this.players = new ArrayList<>(this.numberOfPlayers);
//    }

    public void initializeComponentTiles() {
        File file = new File("src/main/resources/it/polimi/it/galaxytrucker/json/componenttiles.json");

        try {
            JsonNode node = Json.parse(file);
            components = Json.fromJsonSet(node, ComponentTile.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ComponentTile drawComponentTile() throws InvalidActionException {
        if (components.isEmpty()) {
            throw new InvalidActionException("There are no components left");
        }

        int randomIndex = getRandomIndex(components.size());
        return removeComponentTileAtIndex(randomIndex);
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

    public void placeComponentTile(UUID playerID, ComponentTile component, int row, int column) throws IndexOutOfBoundsException, IllegalComponentPositionException {
        Player player = this.getPlayerByID(playerID);
        ShipManager ship = player.getShipManager();

        ship.addComponentTile(row, column, component);
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
    }

    public void finishBuilding(UUID playerID) {
        updateState();
    }
}
