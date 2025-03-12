package it.polimi.it.galaxytrucker.managers;

import it.polimi.it.galaxytrucker.componenttiles.ComponentTile;
import it.polimi.it.galaxytrucker.componenttiles.DoubleEngine;
import it.polimi.it.galaxytrucker.componenttiles.LifeSupport;
import it.polimi.it.galaxytrucker.componenttiles.OutOfBoundsTile;
import it.polimi.it.galaxytrucker.componenttiles.SingleEngine;
import it.polimi.it.galaxytrucker.componenttiles.SpecialCargoHold;
import it.polimi.it.galaxytrucker.componenttiles.TileEdge;
import it.polimi.it.galaxytrucker.crewmates.Alien;
import it.polimi.it.galaxytrucker.crewmates.Human;
import it.polimi.it.galaxytrucker.crewmates.Crewmate;
import it.polimi.it.galaxytrucker.componenttiles.BatteryComponent;
import it.polimi.it.galaxytrucker.componenttiles.CabinModule;
import it.polimi.it.galaxytrucker.componenttiles.CargoHold;
import it.polimi.it.galaxytrucker.componenttiles.CentralCabin;
import it.polimi.it.galaxytrucker.utility.Cargo;
import it.polimi.it.galaxytrucker.utility.Color;
import it.polimi.it.galaxytrucker.exceptions.IllegalComponentPositionException;
import it.polimi.it.galaxytrucker.exceptions.InvalidActionException;

import java.util.Set;
import java.util.stream.IntStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

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

    /**
     * Converts the given row and/or column from board coordinates to tile matrix coordinates.
     *
     * <p>This method adjusts the given row and column indices by subtracting the board's
     * starting indices, as defined in {@link ShipManager#STARTOFBOARDROWS} and 
     * {@link ShipManager#STARTOFBOARDCOLUMNS}, to map them correctly within the ship's tile matrix.</p>
     *
     * <p>If a row or column index is out of bounds, an {@link IndexOutOfBoundsException} is thrown.</p>
     *
     * @param row    An {@link Optional} containing the row index to convert, or empty if not provided.
     * @param column An {@link Optional} containing the column index to convert, or empty if not provided.
     * @return A list containing two {@link Optional} elements: the converted row and column indices.
     *
     * @throws IndexOutOfBoundsException If the row or column is out of the ship's tile matrix bounds.
     */
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

    public Optional<ComponentTile> getComponent(int row, int column) {
        List<Integer> tileCoords = new ArrayList<>();
        tileCoords.add(this.toTileMatrixCoord(Optional.of(row), Optional.empty()).get(0).get());
        tileCoords.add(this.toTileMatrixCoord(Optional.empty(), Optional.of(column)).get(1).get());

        return ship.getComponent(tileCoords.get(0), tileCoords.get(1));
    }

    public List<Optional<ComponentTile>> getComponentsAtRow(int row) throws IndexOutOfBoundsException {
        int tileRow = this.toTileMatrixCoord(Optional.of(row), Optional.empty()).get(0).get();
        List<Optional<ComponentTile>> components = new ArrayList<>();

        for (int i = 0; i < ShipManager.COLUMNS; i++) {
            components.add(ship.getComponent(tileRow, i));
        }
        return components;
    }

    public List<Optional<ComponentTile>> getComponentsAtColumn(int column) throws IndexOutOfBoundsException {
        int tileColumn = this.toTileMatrixCoord(Optional.empty(), Optional.of(column)).get(1).get();
        List<Optional<ComponentTile>> components = new ArrayList<>();

        for (int i = 0; i < ShipManager.ROWS; i++) {
            components.add(ship.getComponent(i, tileColumn));
        }
        return components;
    }

    public Set<Class<? extends ComponentTile>> getAllComponentsType() {
        return ship.getAllComponentsTypes();
    }

    public Optional<Set<List<Integer>>> getAllComponentsPositionOfType(Class<? extends ComponentTile> componentType) {
        return Optional.<Set<List<Integer>>>of(ship.getAllComponentsPositionOfType(componentType));
    }

    public void addComponentTile(int row, int column, ComponentTile component) throws IndexOutOfBoundsException, IllegalComponentPositionException {
        List<Integer> tileCoords = new ArrayList<>();
        tileCoords.add(this.toTileMatrixCoord(Optional.of(row), Optional.empty()).get(0).get());
        tileCoords.add(this.toTileMatrixCoord(Optional.empty(), Optional.of(column)).get(1).get());

        ship.addComponentTile(tileCoords.get(0), tileCoords.get(1), component);
    }

    public void removeComponentTile(int row, int column) throws IndexOutOfBoundsException, IllegalComponentPositionException {
        ComponentTile removedComponent;
        List<Integer> tileCoords = new ArrayList<>();
        tileCoords.add(this.toTileMatrixCoord(Optional.of(row), Optional.empty()).get(0).get());
        tileCoords.add(this.toTileMatrixCoord(Optional.empty(), Optional.of(column)).get(1).get());

        removedComponent = ship.removeComponentTile(tileCoords.get(0), tileCoords.get(1));
        this.discardedTile.add(removedComponent);
    }

    public void removeBranch(Set<List<Integer>> branch) {
        Set<ComponentTile> removedBranch;

        try {
            removedBranch = ship.removeBranch(branch);
            for (ComponentTile component : removedBranch) {
                this.discardedTile.add(component);
            }
        } catch (IllegalComponentPositionException e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * Determines whether the ship's construction is legal according to the game rules.
     *
     * <p>The method verifies several conditions to ensure that the ship meets the requirements:
     * <ul>
     *   <li>The ship must not be divided into disconnected sections.</li>
     *   <li>All {@link SingleEngine} and {@link DoubleEngine} components must be oriented backwards (rotation must be 0).</li>
     *   <li>All connectors between adjacent components must be compatible.</li>
     * </ul>
     * If any of these conditions are not met, the ship is considered illegal.</p>
     *
     * @return {@code true} if the ship is legal, {@code false} otherwise.
     */
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

    /**
     * Adds a crewmate to a specified position within the ship.
     * 
     * <p>This method attempts to place a given {@link Human} crewmate at the specified
     * row and column coordinates on the ship. If the component does not exist or is not a {@link CentralCabin},
     * an exception is thrown.</p>
     *
     * @param row      The row index where the crewmate should be placed.
     * @param column   The column index where the crewmate should be placed.
     * @param crewmate The {@link Human} crewmate to be added to the cabin.
     * 
     * @throws IndexOutOfBoundsException         If the specified row or column is out of the ship's bounds.
     * @throws IllegalComponentPositionException If there is no component at the specified position or 
     *                                           if the component is not an instance of {@link CentralCabin}.
     * @throws InvalidActionException            If the cabin is full.
     */
    public void addCrewmate(int row, int column, Human crewmate) throws IndexOutOfBoundsException, IllegalComponentPositionException, InvalidActionException {
        List<Integer> tileCoords = new ArrayList<>();
        tileCoords.add(this.toTileMatrixCoord(Optional.of(row), Optional.empty()).get(0).get());
        tileCoords.add(this.toTileMatrixCoord(Optional.empty(), Optional.of(column)).get(1).get());

        //check if is empty or it's not a cabin module
        ComponentTile component = ship.getComponent(tileCoords.get(0), tileCoords.get(1))
            .orElseThrow(() -> new IllegalComponentPositionException("There is no element here"));
        if (!(component instanceof CentralCabin)) {
            throw new IllegalComponentPositionException("Not a cabin module at [" + row + "][" + column + "]");
        }

        CentralCabin cabin = (CentralCabin) component;
        cabin.addCrewmate(crewmate);
    }

    /**
     * Adds an alien crewmate to a specified position within the ship.
     *
     * <p>This method attempts to place a given {@link Alien} crewmate at the specified
     * row and column coordinates on the ship. If the component does not exist or is not a {@link CabinModule},
     * an exception is thrown.</p>
     *
     * <p>Additionally, before placing the alien, the method checks whether a 
     * {@link LifeSupport} module is adjacent to the target cabin. The life support module
     * must be compatible with the alien's type; otherwise, the action is not allowed.</p>
     *
     * @param row      The row index where the crewmate should be placed.
     * @param column   The column index where the crewmate should be placed.
     * @param crewmate The {@link Alien} crewmate to be added to the cabin.
     * 
     * @throws IndexOutOfBoundsException         If the specified row or column is out of the ship's bounds.
     * @throws IllegalComponentPositionException If there is no component at the specified position or 
     *                                           if the component is not a {@link CabinModule}.
     * @throws InvalidActionException            If no compatible {@link LifeSupport} module is adjacent
     *                                           to the cabin or if the cabin is full.
     */
    public void addCrewmate(int row, int column, Alien crewmate) throws IndexOutOfBoundsException, IllegalComponentPositionException, InvalidActionException {
        List<Integer> tileCoords = new ArrayList<>();
        tileCoords.add(this.toTileMatrixCoord(Optional.of(row), Optional.empty()).get(0).get());
        tileCoords.add(this.toTileMatrixCoord(Optional.empty(), Optional.of(column)).get(1).get());

        //check if is empty or it's not a cabin module
        ComponentTile component = ship.getComponent(tileCoords.get(0), tileCoords.get(1))
            .orElseThrow(() -> new IllegalComponentPositionException("There is no element here"));
        if (!(component instanceof CentralCabin)) {
            throw new IllegalComponentPositionException("Not a cabin module at [" + row + "][" + column + "]");
        }

        CabinModule cabin = (CabinModule) component;

        //check for life support module near the cabin
        List<Optional<ComponentTile>> neighbors = this.ship.getNeighbourComponents(tileCoords.get(0), tileCoords.get(1));

        boolean hasLifeSupport = neighbors.stream()
            .flatMap(Optional::stream)
            .filter(neighbor -> neighbor instanceof LifeSupport)
            .map(neighbor -> (LifeSupport) neighbor)
            .anyMatch(l -> l.getSupportedAlienType().equals(crewmate.getAlienType()));

        if (!hasLifeSupport) {
            throw new InvalidActionException("The cabin module has not a life support module nearby");
        }

        cabin.addCrewmate(crewmate);
    }

    /**
     * Removes a crewmate from the specified position within the ship.
     *
     * <p>This method attempts to remove a crewmate from the cabin located at the specified
     * row and column coordinates on the ship. If the component does not exist or is not a {@link CentralCabin},
     * an exception is thrown.</p>
     *
     * @param row    The row index of the cabin from which the crewmate should be removed.
     * @param column The column index of the cabin from which the crewmate should be removed.
     *
     * @throws IndexOutOfBoundsException         If the specified row or column is out of the ship's bounds.
     * @throws IllegalComponentPositionException If there is no component at the specified position or 
     *                                           if the component is not an instance of {@link CentralCabin}.
     * @throws InvalidActionException            If the cabin has no crew.
     */
    public void removeCrewmate(int row, int column) throws IndexOutOfBoundsException, IllegalComponentPositionException, InvalidActionException {
        List<Integer> tileCoords = new ArrayList<>();
        tileCoords.add(this.toTileMatrixCoord(Optional.of(row), Optional.empty()).get(0).get());
        tileCoords.add(this.toTileMatrixCoord(Optional.empty(), Optional.of(column)).get(1).get());

        //check if is empty or it's not a cabin module
        ComponentTile component = ship.getComponent(tileCoords.get(0), tileCoords.get(1))
            .orElseThrow(() -> new IllegalComponentPositionException("There is no element here"));
        if (!(component instanceof CentralCabin)) {
            throw new IllegalComponentPositionException("Not a cabin module at [" + row + "][" + column + "]");
        }

        CentralCabin cabin = (CentralCabin) component;
        cabin.removeCrewmate();
    }

    /**
     * Adds a cargo item to a cargo holder at the specified position within the ship.
     *
     * <p>This method attempts to place a given {@link Cargo} at the specified row and column
     * coordinates on the ship. If the cargo is special, it must be placed in a {@link SpecialCargoHold}.
     * If the target position does not contain the correct type of cargo holder, an exception is thrown.</p>
     *
     * @param row    The row index where the cargo should be placed.
     * @param column The column index where the cargo should be placed.
     * @param cargo  The {@link Cargo} to be added to the cargo hold.
     *
     * @throws IndexOutOfBoundsException         If the specified row or column is out of the ship's bounds.
     * @throws IllegalComponentPositionException If the target position does not contain a cargo holder or
     *                                           if the cargo type is not compatible with the cargo holder type.
     * @throws InvalidActionException            If the cargo holder is full.
     */
    public void addCargo(int row, int column, Cargo cargo) throws IndexOutOfBoundsException, IllegalComponentPositionException, InvalidActionException {
        CargoHold cargoHoldComponent;
        List<Integer> tileCoords = new ArrayList<>();
        tileCoords.add(this.toTileMatrixCoord(Optional.of(row), Optional.empty()).get(0).get());
        tileCoords.add(this.toTileMatrixCoord(Optional.empty(), Optional.of(column)).get(1).get());

        //check if is empty
        ComponentTile component = ship.getComponent(tileCoords.get(0), tileCoords.get(1))   
            .orElseThrow(() -> new IllegalComponentPositionException("There is no element here"));
        //check the cargo hold type depending on cargo specialty
        if (cargo.isSpecial()) {
            if (!component.getClass().equals(SpecialCargoHold.class)) {
                throw new IllegalComponentPositionException("Not a special cargo hold module at [" + row + "][" + column + "]");
            }
            cargoHoldComponent = (SpecialCargoHold) component;
        } else {
            if (!(component instanceof CargoHold)) {
                throw new IllegalComponentPositionException("Not a cargo hold module at [" + row + "][" + column + "]");
            }
            cargoHoldComponent = (CargoHold) component;
        }

        cargoHoldComponent.addCargo(cargo);
    }

    /**
     * Removes a cargo item of the specified color from a cargo hold at the given position.
     *
     * <p>This method attempts to remove a {@link Cargo} item that matches the given {@link Color}
     * from the cargo hold located at the specified row and column coordinates on the ship. 
     * If the cargo color is red, the method expects a {@link SpecialCargoHold}. 
     * If the correct type of cargo hold is not present, or if no cargo of the specified color is found, an exception is thrown.</p>
     *
     * @param row    The row index of the cargo hold.
     * @param column The column index of the cargo hold.
     * @param color  The {@link Color} of the cargo to be removed.
     *
     * @throws IndexOutOfBoundsException         If the specified row or column is out of the ship's bounds.
     * @throws IllegalComponentPositionException If the target position does not contain a cargo hold or
     *                                           if the cargo type is not compatible with the cargo hold type.
     * @throws InvalidActionException            If no cargo of the specified color is found or if the cargo holder is empty.
     */
    public void removeCargo(int row, int column, Color color) throws IndexOutOfBoundsException, IllegalComponentPositionException, InvalidActionException {
        CargoHold cargoHoldComponent;
        List<Integer> tileCoords = new ArrayList<>();
        tileCoords.add(this.toTileMatrixCoord(Optional.of(row), Optional.empty()).get(0).get());
        tileCoords.add(this.toTileMatrixCoord(Optional.empty(), Optional.of(column)).get(1).get());

        //check if is empty
        ComponentTile component = ship.getComponent(tileCoords.get(0), tileCoords.get(1))   
            .orElseThrow(() -> new IllegalComponentPositionException("There is no element here"));
        //check the cargo hold type depending on cargo specialty
        if (color.equals(Color.RED)) {
            if (!component.getClass().equals(SpecialCargoHold.class)) {
                throw new IllegalComponentPositionException("Not a special cargo hold module at [" + row + "][" + column + "]");
            }
            cargoHoldComponent = (SpecialCargoHold) component;
        } else {
            if (!(component instanceof CargoHold)) {
                throw new IllegalComponentPositionException("Not a cargo hold module at [" + row + "][" + column + "]");
            }
            cargoHoldComponent = (CargoHold) component;
        }

        List<Cargo> cargo = cargoHoldComponent.getContainedCargo();
        OptionalInt position = IntStream.range(0, cargo.size())
            .filter(i -> cargo.get(i).getColor().equals(color))
            .findFirst();

        if (position.isPresent()) {
            cargo.remove(position.getAsInt());
        } else {
            throw new InvalidActionException("There are not " + color + " cargo at [" + row + "][" + column + "]");
        }
    }

    /**
     * Removes a battery component from the specified position within the ship.
     *
     * <p>This method attempts to remove a battery from the module located at the specified
     * row and column coordinates on the ship. If the component does not exist or is not a {@link BatteryComponent},
     * an exception is thrown.</p>
     *
     * <p>The method does not physically remove the battery component but instead 
     * triggers energy consumption within it.</p>
     *
     * @param row    The row index of the battery module.
     * @param column The column index of the battery module.
     *
     * @throws IllegalComponentPositionException If there is no component at the specified position or 
     *                                           if the component is not a {@link BatteryComponent}.
     * @throws InvalidActionException            If the component has no battery left.
     */
    public void removeBattery(int row, int column) throws IllegalComponentPositionException, InvalidActionException {
        BatteryComponent batteryComponent;
        List<Integer> tileCoords = new ArrayList<>();
        tileCoords.add(this.toTileMatrixCoord(Optional.of(row), Optional.empty()).get(0).get());
        tileCoords.add(this.toTileMatrixCoord(Optional.empty(), Optional.of(column)).get(1).get());

        //check if is empty or it's not a battery component
        ComponentTile component = ship.getComponent(tileCoords.get(0), tileCoords.get(1))   
            .orElseThrow(() -> new IllegalComponentPositionException("There is no element here"));
        if (!component.getClass().equals(BatteryComponent.class)) {
            throw new IllegalComponentPositionException("Not a battery hold module at [" + row + "][" + column + "]");
        }

        batteryComponent = (BatteryComponent) component;
        batteryComponent.consumeEnergy();
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
