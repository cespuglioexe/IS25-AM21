package it.polimi.it.galaxytrucker.managers;

import java.util.UUID;

import it.polimi.it.galaxytrucker.utility.Color;

public class Player {
    private UUID PlayerID;
    private String PlayerName;
    private int credits;
    private Color color;
    private ShipManager shipManager;

    public Player(UUID playerID, String playerName, int credits, Color color,ShipManager shipManager) {
        this.PlayerID = playerID;
        this.PlayerName = playerName;
        this.credits = credits;
        this.color = color;
        this.shipManager = shipManager;
    }

    public Player(UUID playerID, String playerName, int credits, Color color) {
        this.PlayerID = playerID;
        this.PlayerName = playerName;
        this.credits = credits;
        this.color = color;
    }

    public UUID getPlayerID() {
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

    public void createShip(int level) {
        this.shipManager = new ShipManager(level);
    }
}
