package it.polimi.it.galaxytrucker.managers;

import java.util.List;

import it.polimi.it.galaxytrucker.gameStates.states.State;

import java.util.ArrayList;

public class GameManager {
    private State currentState;
    private int level;
    private int numberOfPlayers;
    private List<Player> players;
    private FlightBoardState flightBoardState;

    public State getCurrentState() {
        return this.currentState;
    }

    public int getLevel() {
        return this.level;
    }

    public int getNumberOfPlayers() {
        return this.numberOfPlayers;
    }

    public List<Player> getPlayers() {
        return this.players;
    }

    public Player getPlayerByID(int id) {
        return this.players.stream()
            .filter(player -> player.getPlayerID() == id)
            .findFirst()
            .orElse(null);
    }

    public void changeState(State nextState) {
        this.currentState.exit();
        this.currentState = nextState;
        this.currentState.enter();
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setNumberOfPlayers(int numberOfPlayers) {
        this.numberOfPlayers = numberOfPlayers;
        this.players = new ArrayList<>(numberOfPlayers);
    }

    public void addNewPlayer(Player player) {
        this.players.add(player);
    }

    public void removePlayer(Player player) {
        this.players.remove(player);
    }
}
