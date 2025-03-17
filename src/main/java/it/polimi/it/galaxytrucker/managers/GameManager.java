package it.polimi.it.galaxytrucker.managers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import it.polimi.it.galaxytrucker.aventurecard.AdventureDeck;
import it.polimi.it.galaxytrucker.componenttiles.ComponentTile;
import it.polimi.it.galaxytrucker.exceptions.InvalidActionException;
import it.polimi.it.galaxytrucker.exceptions.NotFoundException;
import it.polimi.it.galaxytrucker.gameStates.State;
import it.polimi.it.galaxytrucker.gameStates.Start;
import it.polimi.it.galaxytrucker.utility.Color;;

public class GameManager implements Model {
    private State currentState;
    private int level;
    private int numberOfPlayers;
    private List<Player> players;
    private Set<ComponentTile> components;
    private FlightBoardState flightBoard;
    private AdventureDeck adventureDeck;

    public GameManager() {
        this.currentState = new Start(this);
        this.currentState.enter();
    }

    @Override
    public State getCurrentState() {
        return this.currentState;
    }

    @Override
    public int getLevel() {
        return this.level;
    }

    @Override
    public int getNumberOfPlayers() {
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
    public void changeState(State nextState) {
        this.currentState.exit();
        this.currentState = nextState;
        this.currentState.enter();
    }

    @Override
    public void setLevel(int level) {
        this.level = level;
    }

    @Override
    public void setNumberOfPlayers(int numberOfPlayers) {
        this.numberOfPlayers = numberOfPlayers;
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
        //TODO from JSON file
    }

    public void initializeAdventureDeck() {
        //TODO from JSON file
    }
}
