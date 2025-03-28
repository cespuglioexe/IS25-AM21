package it.polimi.it.galaxytrucker.managers;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.JsonNode;
import it.polimi.it.galaxytrucker.aventurecard.AdventureDeck;
import it.polimi.it.galaxytrucker.componenttiles.*;
import it.polimi.it.galaxytrucker.design.statePattern.StateMachine;
import it.polimi.it.galaxytrucker.exceptions.InvalidActionException;
import it.polimi.it.galaxytrucker.exceptions.NotFoundException;
import it.polimi.it.galaxytrucker.gameStates.StartState;
import it.polimi.it.galaxytrucker.json.Json;
import it.polimi.it.galaxytrucker.utility.Color;;

public class GameManager extends StateMachine implements Model {
    private Integer level;
    private Integer numberOfPlayers;
    private List<Player> players;
    private Set<ComponentTile> components;
    private FlightBoardState flightBoard;
    private AdventureDeck adventureDeck;

    public GameManager() {
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
    public FlightBoardState getFlightBoard() {
        return this.flightBoard;
    }

    @Override
    public AdventureDeck getAdventureDeck() {
        return this.adventureDeck;
    }

    @Override
    public void setLevel(int level) {
        this.level = level;

        updateState();
    }

    @Override
    public void setNumberOfPlayers(int numberOfPlayers) {
        this.numberOfPlayers = numberOfPlayers;

        updateState();
    }

    @Override
    public UUID addNewPlayer(String name) throws InvalidActionException {
        if (this.players.stream().anyMatch(player -> player.getPlayerName().equals(name))) {
            throw new InvalidActionException("Another player named " + name + " is already playing");
        }
        
         Color playerColor = Arrays.stream(Color.values())
             .filter(color -> players.stream()
                 .noneMatch(player -> player.getColor().equals(color)))
             .findFirst()
             .orElseThrow(() -> new InvalidActionException("No available color"));

        Player newPlayer = new Player(UUID.randomUUID(), name, 0, playerColor);
        players.add(newPlayer);
        
        return newPlayer.getPlayerID();
    }

    @Override
    public void removePlayer(UUID id) throws NotFoundException {
        Player playerToRemove = this.players.stream()
            .filter(player -> player.getPlayerID().equals(id))
            .findFirst()
            .orElseThrow(() -> new NotFoundException("Cannot find the player"));

        this.players.remove(playerToRemove);
    }

    public void initializeGameSpecifics() {
        this.players = new ArrayList<>(this.numberOfPlayers);
    }

    public void initializeFlightBoard() {
        this.flightBoard = new FlightBoardState(this.level);
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

    public void initializeAdventureDeck() {
        //TODO from JSON file
    }
}
