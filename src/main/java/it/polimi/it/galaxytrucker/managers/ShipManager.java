package it.polimi.it.galaxytrucker.managers;

import it.polimi.it.galaxytrucker.componenttiles.ComponentTile;
import it.polimi.it.galaxytrucker.componenttiles.TileEdge;

import java.util.Set;
import java.util.List;
import java.util.Optional;

public class ShipManager {
    ShipBoard ship;

    public ShipManager() {

    }

    private boolean connectorsMatch(TileEdge connector1, TileEdge connector2) {
        switch (connector1) {
            case SINGLE:
                if (connector2 == TileEdge.SINGLE || connector2 == TileEdge.UNIVERSAL || connector2 == TileEdge.SMOOTH) {
                    return true;
                }
                return false;
            case DOUBLE:
                if (connector2 == TileEdge.DOUBLE || connector2 == TileEdge.UNIVERSAL || connector2 == TileEdge.SMOOTH) {
                    return true;
                }
                return false;
            case UNIVERSAL:
            case SMOOTH:
                if (connector2 == TileEdge.INCOMPATIBLE) {
                    return false;
                }
                return true;
            case INCOMPATIBLE:
                return false;
            default:
                return true;
        }
    }

    public boolean isShipLegal() {
        //if the ship is divided into disconnected sections it's not legal
        if (ship.getDisconnectedBranches().size() > 1) {
            return false;
        }

        //checking if connectors match
        Set<Class<? extends ComponentTile>> componentTypes = ship.getAllComponentsTypes();
        for (Class<? extends ComponentTile> componentType : componentTypes) {
            for (List<Integer> coord : ship.getAllComponentsPositionOfType(componentType)) {
                ComponentTile component = ship.getComponent(coord.get(0), coord.get(1)).get();
                List<Optional<ComponentTile>> neighbors = ship.getNeighbourComponents(coord.get(0), coord.get(1));

                TileEdge componentConnector;
                TileEdge neighborConnector;

                //up direction
                //TODO
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
