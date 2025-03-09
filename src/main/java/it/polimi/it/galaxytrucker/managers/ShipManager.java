package it.polimi.it.galaxytrucker.managers;

import it.polimi.it.galaxytrucker.componenttiles.ComponentTile;
import it.polimi.it.galaxytrucker.componenttiles.SingleEngine;
import it.polimi.it.galaxytrucker.componenttiles.TileEdge;
import it.polimi.it.galaxytrucker.exceptions.IllegalComponentPositionException;

import java.util.Set;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ShipManager {
    ShipBoard ship;
    List<ComponentTile> discardedTile;

    public ShipManager(int level) {
        this.ship = new ShipBoard(level);
        discardedTile = new ArrayList<>(2);
    }

    public void addComponentTile(int row, int column, ComponentTile component) {
        try {
            this.ship.addComponentTile(row, column, component);
        } catch (IllegalComponentPositionException e) {
            System.err.println(e.getMessage());
        }
    }

    public void removeComponentTile(int row, int column) {
        //TODO
    }

    public boolean isShipLegal() {
        //if the ship is divided into disconnected sections it's not legal
        if (ship.getDisconnectedBranches().size() > 1) {
            return false;
        }

        //checking if engines are all pointing backwards: if not then it's not legal
        //TODO

        //checking if connectors match: if not then it's not legal
        Set<Class<? extends ComponentTile>> componentTypes = ship.getAllComponentsTypes();
        for (Class<? extends ComponentTile> componentType : componentTypes) {
            for (List<Integer> coord : ship.getAllComponentsPositionOfType(componentType)) {

                ComponentTile component = ship.getComponent(coord.get(0), coord.get(1)).get();
                List<Optional<ComponentTile>> neighbors = ship.getNeighbourComponents(coord.get(0), coord.get(1));
                Optional<ComponentTile> neighbor;

                TileEdge componentConnector;
                TileEdge neighborConnector;

                //up direction
                neighbor = neighbors.get(0);
                if (neighbor.isPresent()) {
                    componentConnector = component.getTileEdges().get(0);
                    neighborConnector = neighbor.get().getTileEdges().get(2);

                    if (!componentConnector.isCompatible(neighborConnector)) {
                        return false;
                    }
                }

                //right direction
                neighbor = neighbors.get(1);
                if (neighbor.isPresent()) {
                    componentConnector = component.getTileEdges().get(1);
                    neighborConnector = neighbor.get().getTileEdges().get(3);

                    if (!componentConnector.isCompatible(neighborConnector)) {
                        return false;
                    }
                }

                //down direction
                neighbor = neighbors.get(2);
                if (neighbor.isPresent()) {
                    componentConnector = component.getTileEdges().get(2);
                    neighborConnector = neighbor.get().getTileEdges().get(0);

                    if (!componentConnector.isCompatible(neighborConnector)) {
                        return false;
                    }
                }

                //left direction
                neighbor = neighbors.get(3);
                if (neighbor.isPresent()) {
                    componentConnector = component.getTileEdges().get(3);
                    neighborConnector = neighbor.get().getTileEdges().get(1);

                    if (!componentConnector.isCompatible(neighborConnector)) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    public double calculateFirePower(int PlayerID){
        double firePower = 0;
        return firePower;
    }

    public int calculateCrewmates(int PlayerID) {
        int crewmates = 0;
        return crewmates;
    }

    public int calculateEnginePower(int PlayerID) {
        int enginePower = 0;
        return enginePower;
    }
}
