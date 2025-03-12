package it.polimi.it.galaxytrucker.managers;

import it.polimi.it.galaxytrucker.componenttiles.ComponentTile;
import it.polimi.it.galaxytrucker.componenttiles.DoubleEngine;
import it.polimi.it.galaxytrucker.componenttiles.OutOfBoundsTile;
import it.polimi.it.galaxytrucker.componenttiles.SingleEngine;
import it.polimi.it.galaxytrucker.componenttiles.TileEdge;
import it.polimi.it.galaxytrucker.exceptions.IllegalComponentPositionException;

import java.util.Set;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * * colonna e riga con int
 * //ShipManager.toTileMatrixCoord();
 * //ShipManager.toBoardShipCoord();
 * 
 * * tenere solo coordinate di gioco
 * * rimuove crewmate: coord
 * * carico/scarico merci: addCargo(Set<Cargo>) -> cargo pieno cambiare (domani)
 * 
 * * consumo batteria (domani)
 */
public class ShipManager {
    private ShipBoard ship;
    private List<ComponentTile> discardedTile;
    static private final int ROWS = 5;
    static private final int COLUMNS = 7;
    static private final int STARTOFBOARDROWS = 5;
    static private final int STARTOFBOARDCOLUMNS = 4;

    public ShipManager(int level) {
        this.ship = new ShipBoard(level);
        discardedTile = new ArrayList<>(2);
    }

    private List<Optional<Integer>> toTileMatrixCoord(Optional<Integer> row, Optional<Integer> column) throws IndexOutOfBoundsException {
        Optional<Integer> tileMatrixRow = row.map(r -> r - ShipManager.STARTOFBOARDROWS);
        Optional<Integer> tileMatrixColumn = column.map(c -> c - ShipManager.STARTOFBOARDCOLUMNS);
        
        //checking matrix bound
        tileMatrixRow.ifPresent(r -> {
            if (r < 0 || r >= ShipManager.ROWS) {
                throw new IndexOutOfBoundsException("The specified row is out of the board's bounds.");
            }
        });
        tileMatrixColumn.ifPresent(c -> {
            if (c < 0 || c >= ShipManager.COLUMNS) {
                throw new IndexOutOfBoundsException("The specified column is out of the board's bounds.");
            }
        });
    
        return List.of(tileMatrixRow, tileMatrixColumn);
    }

    private List<Optional<Integer>> toBoardCoord(Optional<Integer> row, Optional<Integer> column) throws IndexOutOfBoundsException {
        Optional<Integer> boardRow = row.map(r -> r + ShipManager.STARTOFBOARDROWS);
        Optional<Integer> boardColumn = column.map(c -> c + ShipManager.STARTOFBOARDCOLUMNS);
        
        //checking matrix bound
        boardRow.ifPresent(r -> {
            if (r < ShipManager.STARTOFBOARDROWS || r >= ShipManager.STARTOFBOARDROWS + ShipManager.ROWS) {
                throw new IndexOutOfBoundsException("the specified row is out of the board's bound");
            }
        });
        boardColumn.ifPresent(c -> {
            if (c < ShipManager.STARTOFBOARDCOLUMNS || c >= ShipManager.STARTOFBOARDCOLUMNS + ShipManager.COLUMNS) {
                throw new IndexOutOfBoundsException("the specified column is out of the board's bound");
            }
        });

        return List.of(boardRow, boardColumn);
    }

    public static int getRows() {
        return ShipManager.ROWS;
    }

    public static int getColumns() {
        return ShipManager.COLUMNS;
    }

    public Set<Class<? extends ComponentTile>> getAllComponentsType() {
        return ship.getAllComponentsTypes();
    }

    public Optional<Set<List<Integer>>> getAllComponentsPositionOfType(Class<? extends ComponentTile> componentType) {
        return Optional.<Set<List<Integer>>>of(ship.getAllComponentsPositionOfType(componentType));
    }

    public void addComponentTile(int row, int column, ComponentTile component) throws IndexOutOfBoundsException{
        List<Integer> tileCoords = new ArrayList<>();
        tileCoords.add(this.toTileMatrixCoord(Optional.of(row), Optional.empty()).get(0).get());
        tileCoords.add(this.toTileMatrixCoord(Optional.empty(), Optional.of(column)).get(1).get());

        try {
            ship.addComponentTile(tileCoords.get(0), tileCoords.get(1), component);
        } catch (IllegalComponentPositionException e) {
            System.err.println(e);
        }
    }

    public void removeComponentTile(int row, int column) {
        //TODO e controllare anche branches
    }

    public boolean isShipLegal() {
        //if the ship is divided into disconnected sections it's not legal
        if (ship.getDisconnectedBranches().size() > 1) {
            return false;
        }

        //checking if single engines are all pointing backwards: if not then it's not legal
        Set<List<Integer>> singleEnginePositions = Optional
            .ofNullable(ship.getAllComponentsPositionOfType(SingleEngine.class))
            .orElse(Collections.emptySet());
        for (List<Integer> coord : singleEnginePositions) {
            if (ship.getComponent(coord.get(0), coord.get(1)).get().getRotation() != 0) {
                return false;
            }
        }

        //checking if double engines are all pointing backwards: if not then it's not legal
        Set<List<Integer>> doubleEnginePositions = Optional
            .ofNullable(ship.getAllComponentsPositionOfType(DoubleEngine.class))
            .orElse(Collections.emptySet());
        for (List<Integer> coord : doubleEnginePositions) {
            if (ship.getComponent(coord.get(0), coord.get(0)).get().getRotation() != 0) {
                return false;
            }
        }

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
                if (neighbor.isPresent() && !neighbor.get().getClass().equals(OutOfBoundsTile.class)) {
                    componentConnector = component.getTileEdges().get(0);
                    neighborConnector = neighbor.get().getTileEdges().get(2);

                    if (!componentConnector.isCompatible(neighborConnector)) {
                        return false;
                    }
                }

                //right direction
                neighbor = neighbors.get(1);
                if (neighbor.isPresent() && !neighbor.get().getClass().equals(OutOfBoundsTile.class)) {
                    componentConnector = component.getTileEdges().get(1);
                    neighborConnector = neighbor.get().getTileEdges().get(3);

                    if (!componentConnector.isCompatible(neighborConnector)) {
                        return false;
                    }
                }

                //down direction
                neighbor = neighbors.get(2);
                if (neighbor.isPresent() && !neighbor.get().getClass().equals(OutOfBoundsTile.class)) {
                    componentConnector = component.getTileEdges().get(2);
                    neighborConnector = neighbor.get().getTileEdges().get(0);

                    if (!componentConnector.isCompatible(neighborConnector)) {
                        return false;
                    }
                }

                //left direction
                neighbor = neighbors.get(3);
                if (neighbor.isPresent() && !neighbor.get().getClass().equals(OutOfBoundsTile.class)) {
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

    public double calculateFirePower(){
        double firePower = 0;
        return firePower;
    }

    public int countCrewmates() {
        int crewmates = 0;
        return crewmates;
    }

    public int calculateEnginePower() {
        int enginePower = 0;
        return enginePower;
    }
}
