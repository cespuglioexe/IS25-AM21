package it.polimi.it.galaxytrucker.model.managers;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import it.polimi.it.galaxytrucker.commands.servercommands.GameUpdate;
import it.polimi.it.galaxytrucker.commands.servercommands.GameUpdateType;
import it.polimi.it.galaxytrucker.listeners.*;
import it.polimi.it.galaxytrucker.model.componenttiles.ComponentTile;
import it.polimi.it.galaxytrucker.model.utility.Color;

public class Player implements Observable {
    private final UUID playerID;
    private final String playerName;
    private final Color color;
    private final ShipManager shipManager;
    
    private ComponentTile heldComponent;

    private boolean isConnected;

    private int credits;

    private final List<Listener> listeners;

    public Player(UUID id, String playerName, Color color, ShipManager shipManager) {
        this.playerID = id;
        this.playerName = playerName;
        this.color = color;
        this.shipManager = shipManager;
        this.listeners = new ArrayList<>();
        this.credits = 0;
    }

    public ComponentTile getHeldComponent() {
        return heldComponent;
    }

    public void resetHeldComponent() {
        heldComponent = null;
    }

    public void setHeldComponent(ComponentTile heldComponent) {
        this.heldComponent = heldComponent;
        // TODO: do in thread
        updateListeners(
                new GameUpdate.GameUpdateBuilder(GameUpdateType.DRAWN_TILE, playerID)
                        .setNewTile(heldComponent)
                        .build()
        );
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

    public void updateListeners(GameUpdate gameUpdate) {
        synchronized (listeners) {
            for (Listener listener : listeners) {
                listener.notify(gameUpdate);
            }
        }
    }

    @Override
    public void addListener(Listener listener) {
        synchronized (listeners) {
            listeners.add(listener);
        }
    }

    @Override
    public void removeListener(Listener listener) {
        synchronized (listeners) {
            listeners.remove(listener);
        }
    }
}
