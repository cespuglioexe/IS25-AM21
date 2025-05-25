package it.polimi.it.galaxytrucker.model.managers;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import it.polimi.it.galaxytrucker.messages.servermessages.GameUpdate;
import it.polimi.it.galaxytrucker.messages.servermessages.GameUpdateType;
import it.polimi.it.galaxytrucker.listeners.*;
import it.polimi.it.galaxytrucker.model.componenttiles.CargoHold;
import it.polimi.it.galaxytrucker.model.componenttiles.ComponentTile;
import it.polimi.it.galaxytrucker.model.componenttiles.SpecialCargoHold;
import it.polimi.it.galaxytrucker.model.utility.Cargo;
import it.polimi.it.galaxytrucker.model.utility.Color;

public class Player implements Observable {
    private final UUID playerID;
    private final String playerName;
    private final Color color;
    private final ShipManager shipManager;
    
    private ComponentTile heldComponent;
    private int credits;

    private boolean defeated = false;
    private boolean active = true;

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
                new GameUpdate.GameUpdateBuilder(GameUpdateType.DRAWN_TILE)
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

    public boolean isDefeated() {
        return defeated;
    }

    public boolean isActive() {
        return active;
    }

    public void defeat() {
        this.defeated = true;

        this.credits += convertCargoToCredits() / 2 ;
    }

    public void disconnect() {
        this.active = false;
    }

    public void connect() {
        this.active = true;
    }

    public int convertCargoToCredits() {
        return countNormalCargoValue() + countSpecialCargoValue();
    }
    private int countNormalCargoValue() {
        Set<List<Integer>> allCargoHold;
        int normalCargoValue = 0;

        allCargoHold = this.shipManager.getAllComponentsPositionOfType(CargoHold.class);

        for (List<Integer> holdCoords : allCargoHold) {
            CargoHold cargoHold = (CargoHold) this.shipManager.getComponent(holdCoords.get(0), holdCoords.get(1)).get();

            List<Cargo> containedCargo = cargoHold.getContainedCargo();
            for (Cargo cargo : containedCargo) {
                normalCargoValue += cargo.getCreditValue();
            }
        }

        return normalCargoValue;
    }
    private int countSpecialCargoValue() {
        Set<List<Integer>> allCargoHold;
        int specialCargoValue = 0;

        allCargoHold = this.shipManager.getAllComponentsPositionOfType(SpecialCargoHold.class);

        for (List<Integer> holdCoords : allCargoHold) {
            CargoHold cargoHold = (CargoHold) this.shipManager.getComponent(holdCoords.get(0), holdCoords.get(1)).get();

            List<Cargo> containedCargo = cargoHold.getContainedCargo();
            for (Cargo cargo : containedCargo) {
                specialCargoValue += cargo.getCreditValue();
            }
        }

        return specialCargoValue;
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
