package it.polimi.it.galaxytrucker.controller;

import it.polimi.it.galaxytrucker.networking.server.rmi.RMIVirtualView;

import java.util.ArrayList;
import java.util.List;

public class Model {
    Integer state = 0;
    int level;
    int playerNum;
    final List<RMIVirtualView> players;

    public Model(int level, int playerNum) {
        this.level = level;
        this.playerNum = playerNum;
        System.out.println("Model created: lvl " + this.level + ", pNum " + this.playerNum);
        players = new ArrayList<>();
    }

    public boolean addPlayer(RMIVirtualView player) {
        synchronized (this.players) {
            this.players.add(player);
            System.out.println("Added a player to the model");
            return true;
        }
    }

    public boolean add (Integer num) {
        this.state += num;
        return true;
    }

    public Integer getState () {
        return this.state;
    }

    public int getLevel () {
        return this.level;
    }

    public int getPlayerNum () {
        return this.playerNum;
    }
}
