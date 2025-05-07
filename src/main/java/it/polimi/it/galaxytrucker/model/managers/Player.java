package it.polimi.it.galaxytrucker.model.managers;

import java.awt.*;
import java.util.UUID;

import it.polimi.it.galaxytrucker.model.componenttiles.ComponentTile;
import it.polimi.it.galaxytrucker.model.utility.Color;

public class Player {
    private final UUID playerID;
    private final String playerName;
    private final Color color;
    private final ShipManager shipManager;
    
    private ComponentTile heldComponent;

    private boolean isConnected;

    private int credits;

    public Player(UUID id, String playerName, Color color, ShipManager shipManager) {
        this.playerID = id;
        this.playerName = playerName;
        this.color = color;
        this.shipManager = shipManager;

        this.credits = 0;
    }

    public ComponentTile getHeldComponent() {
        return heldComponent;
    }

    public void setHeldComponent(ComponentTile heldComponent) {
        this.heldComponent = heldComponent;
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

    public boolean isEmpty(){
        return this.playerID == null && this.playerName == null && this.color == null;
    }
    
}
