package it.polimi.it.galaxytrucker.model.managers;

import java.util.UUID;

import it.polimi.it.galaxytrucker.model.utility.Color;

public class Player {
    private final UUID playerID;
    private final String playerName;
    private final Color color;
    private final ShipManager shipManager;

    private boolean isConnected;
    private boolean isFinishedBuilding;

    private int credits;

    public Player(UUID id, String playerName, Color color, ShipManager shipManager) {
        this.playerID = id;
        this.playerName = playerName;
        this.color = color;
        this.shipManager = shipManager;

        this.credits = 0;
    }

    public UUID getPlayerID() {
        return playerID;
    }

    public String getPlayerName() {
        return playerName;
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
        this.credits += credits;
    }

    public boolean isFinishedBuilding () {
        return isFinishedBuilding;
    }

    public void setIsFinishedBuilding (boolean isFinishedBuilding) {
        this.isFinishedBuilding = isFinishedBuilding;
    }
}
