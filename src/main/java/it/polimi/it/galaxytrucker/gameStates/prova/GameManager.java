package it.polimi.it.galaxytrucker.gameStates.prova;

import java.util.List;
import java.util.ArrayList;

import it.polimi.it.galaxytrucker.gameStates.prova.states.*;
import it.polimi.it.galaxytrucker.managers.Player;
import it.polimi.it.galaxytrucker.managers.ShipManager;
import it.polimi.it.galaxytrucker.utility.Color;

public class GameManager {
    int level;
    int numberOfPlayers;
    List<Player> players;
    State currentState;
    List<EventListener> observers = new ArrayList<>();

    public GameManager() {
        this.level = 0;
        this.numberOfPlayers = 0;
        this.players = new ArrayList<>();
    }

    public void start() {
        this.currentState = new Start(this);
        this.currentState.enter();
    }

    public void nextState(State state) {
        this.currentState.exit();
        this.currentState = state;
        this.currentState.enter();
    }

    public void addObserver(EventListener observer) {
        observers.add(observer);
    }

    public void notify(Events e) {
        for (EventListener observer : observers) {
            observer.onGameEvent(e);
        }
    }

    public State getCurrentState() {
        return this.currentState;
    }

    public int getLevel() {
        return this.level;
    }

    public int getNumberOfPlayers() {
        return this.numberOfPlayers;
    }

    public List<Player> getConnectedPlayers() {
        return this.players;
    }

    public void setLevel(int level) {
        this.level = level;
        this.notify(Events.RESPONSELEVEL);
    }

    public void setNumberOfPlayers(int numberOfPlayers) {
        this.numberOfPlayers = numberOfPlayers;
        this.notify(Events.RESPONSENUMBEROFPLAYERS);
    }

    public void addNewPlayer(String name) {
        this.players.add(new Player(this.players.size(), name, 0, Color.BLUE, new ShipManager(this.level)));
        this.notify(Events.RESPONSENEWPLAYER);
    }
}
