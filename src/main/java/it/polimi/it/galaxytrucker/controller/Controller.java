package it.polimi.it.galaxytrucker.controller;

import it.polimi.it.galaxytrucker.networking.rmi.server.RMIVirtualView;

public class Controller {
    private final Model model;

    public Controller(int level, int playerNum) {
        System.out.println("Starting controller");
        this.model = new Model(level, playerNum);
    }

    public Model getModel() {
        return model;
    }

    public boolean addPlayer (RMIVirtualView player) {
        return model.addPlayer(player);
    }

    @Override
    public String toString() {
        return "Level: " + model.getLevel() + "\nCurrent state: " + model.getState();
    }
}
