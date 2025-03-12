package it.polimi.it.galaxytrucker.managers;

import it.polimi.it.galaxytrucker.utility.Color;
import it.polimi.it.galaxytrucker.managers.ShipManager;

public class Player {
    private int PlayerID;
    private String PlayerName;
    private int credits;
    private Color color;
    private ShipManager shipManager;

    public Player(int playerID, String playerName, int credits, Color color, ShipManager shipManager) {
        this.PlayerID = playerID;
        this.PlayerName = playerName;
        this.credits = credits;
        this.color = color;
        this.shipManager = shipManager;
    }

    public int getPlayerID() {
        return PlayerID;
    }

    public String getPlayerName() {
        return PlayerName;
    }

    public int getCredits() {
        return credits;
    }

    public Color getColor() {
        return color;
    }

    public ShipManager getShipManager() {
        return shipManager;
    }

    public void addCredits(int credits) {
        this.credits = this.credits + credits;
    }
}
