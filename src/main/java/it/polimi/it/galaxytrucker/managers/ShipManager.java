package it.polimi.it.galaxytrucker.managers;

import it.polimi.it.galaxytrucker.componenttiles.ComponentTile;
import it.polimi.it.galaxytrucker.componenttiles.DoubleEngine;
import it.polimi.it.galaxytrucker.componenttiles.LifeSupport;
import it.polimi.it.galaxytrucker.componenttiles.OutOfBoundsTile;
import it.polimi.it.galaxytrucker.componenttiles.SingleCannon;
import it.polimi.it.galaxytrucker.componenttiles.SingleEngine;
import it.polimi.it.galaxytrucker.componenttiles.SpecialCargoHold;
import it.polimi.it.galaxytrucker.componenttiles.TileEdge;
import it.polimi.it.galaxytrucker.crewmates.Alien;
import it.polimi.it.galaxytrucker.crewmates.Crewmate;
import it.polimi.it.galaxytrucker.crewmates.Human;
import it.polimi.it.galaxytrucker.componenttiles.BatteryComponent;
import it.polimi.it.galaxytrucker.componenttiles.CabinModule;
import it.polimi.it.galaxytrucker.componenttiles.CargoHold;
import it.polimi.it.galaxytrucker.componenttiles.CentralCabin;
import it.polimi.it.galaxytrucker.utility.Cargo;
import it.polimi.it.galaxytrucker.utility.Color;
import it.polimi.it.galaxytrucker.exceptions.IllegalComponentPositionException;
import it.polimi.it.galaxytrucker.exceptions.InvalidActionException;

import java.util.Set;
import java.util.stream.Collectors;
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

    /**
     * Converts the given row and/or column from tile matrix coordinates to board coordinates.
     *
     * <p>This method adjusts the given row and column indices by adding the board's starting indices, 
     * as defined in {@link ShipManager#STARTOFBOARDROWS} and {@link ShipManager#STARTOFBOARDCOLUMNS}, 
     * to map them correctly within the board's coordinate system.</p>
     *
     * <p>If a row or column index is out of bounds, an {@link IndexOutOfBoundsException} is thrown.</p>
     *
     * @param row    An {@link Optional} containing the row index to convert, or empty if not provided.
     * @param column An {@link Optional} containing the column index to convert, or empty if not provided.
     * @return A list containing two {@link Optional} elements: the converted row and column indices.
     *
     * @throws IndexOutOfBoundsException If the row or column is out of the board's valid bounds.
     */
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

    /**
     * Returns the total number of rows in the ship's grid.
     *
     * <p>This method provides access to the number of rows defined in the {@link ShipManager}.</p>
     *
     * @return The total number of rows in the ship's grid.
     */
    public static int getRows() {
        return ShipManager.ROWS;
    }

    /**
     * Returns the total number of columns in the ship's grid.
     *
     * <p>This method provides access to the number of columns defined in the {@link ShipManager}.</p>
     *
     * @return The total number of columns in the ship's grid.
     */
    public static int getColumns() {
        return ShipManager.COLUMNS;
    }

    /**
     * Retrieves the component located at the specified position in the ship's grid.
     *
     * <p>This method returns an {@link Optional} containing the {@link ComponentTile} 
     * at the given row and column coordinates, if present. If no component exists at 
     * the specified location, an empty {@code Optional} is returned.</p>
     *
     * <p>If the specified row or column is out of the valid bounds of the ship, 
     * an {@link IndexOutOfBoundsException} is thrown.</p>
     *
     * @param row    The row index of the component to retrieve.
     * @param column The column index of the component to retrieve.
     * @return An {@link Optional} containing the {@link ComponentTile} if present, or empty if no component exists.
     * @throws IndexOutOfBoundsException If the specified row or column is out of the ship's bounds.
     */
    public Optional<ComponentTile> getComponent(int row, int column) throws IndexOutOfBoundsException {
        List<Integer> tileCoords = new ArrayList<>();
        tileCoords.add(this.toTileMatrixCoord(Optional.of(row), Optional.empty()).get(0).get());
        tileCoords.add(this.toTileMatrixCoord(Optional.empty(), Optional.of(column)).get(1).get());

        return ship.getComponent(tileCoords.get(0), tileCoords.get(1));
    }

    /**
     * Retrieves all components located in the specified row of the ship's grid.
     *
     * <p>This method returns a list of {@link Optional} elements, each representing a 
     * {@link ComponentTile} located at the given row and every column in the ship's grid.
     * If a position does not contain a component, the corresponding {@code Optional} will be empty.</p>
     *
     * <p>If the specified row is out of the valid bounds of the ship, 
     * an {@link IndexOutOfBoundsException} is thrown.</p>
     *
     * @param row The row index for which to retrieve all components.
     * @return A list of {@link Optional<ComponentTile>} representing the components in the specified row.
     * @throws IndexOutOfBoundsException If the specified row is out of the ship's bounds.
     */
    public List<Optional<ComponentTile>> getComponentsAtRow(int row) throws IndexOutOfBoundsException {
        int tileRow = this.toTileMatrixCoord(Optional.of(row), Optional.empty()).get(0).get();
        List<Optional<ComponentTile>> components = new ArrayList<>();

        for (int i = 0; i < ShipManager.COLUMNS; i++) {
            components.add(ship.getComponent(tileRow, i));
        }
        return components;
    }

    /**
     * Retrieves all components located in the specified column of the ship's grid.
     *
     * <p>This method returns a list of {@link Optional} elements, each representing a 
     * {@link ComponentTile} located at the given column and every row in the ship's grid.
     * If a position does not contain a component, the corresponding {@code Optional} will be empty.</p>
     *
     * <p>If the specified column is out of the valid bounds of the ship, 
     * an {@link IndexOutOfBoundsException} is thrown.</p>
     *
     * @param column The column index for which to retrieve all components.
     * @return A list of {@link Optional<ComponentTile>} representing the components in the specified column.
     * @throws IndexOutOfBoundsException If the specified column is out of the ship's bounds.
     */
    public List<Optional<ComponentTile>> getComponentsAtColumn(int column) throws IndexOutOfBoundsException {
        int tileColumn = this.toTileMatrixCoord(Optional.empty(), Optional.of(column)).get(1).get();
        List<Optional<ComponentTile>> components = new ArrayList<>();

        for (int i = 0; i < ShipManager.ROWS; i++) {
            components.add(ship.getComponent(i, tileColumn));
        }
        return components;
    }

    /**
     * Retrieves the set of all distinct component types present in the ship.
     *
     * <p>This method returns a {@link Set} containing the {@link Class} objects of all 
     * {@link ComponentTile} types currently placed in the ship. Each entry in the set represents 
     * a unique type of component found on the ship's grid.</p>
     *
     * <p>If no components are present in the ship, this method returns an empty set.</p>
     *
     * @return A {@link Set} of {@link Class} objects representing the distinct component types in the ship,
     *         or an empty set if no components are present.
     */
    public Set<Class<? extends ComponentTile>> getAllComponentsType() {
        return ship.getAllComponentsTypes();
    }

    /**
     * Retrieves the positions of all components of a specified type in board coordinates.
     *
     * <p>This method returns a {@link Set} of positions where components of the given type 
     * are located. Each position is represented as a {@link List} of two integers (row and column). 
     * The positions are converted from tile matrix coordinates to board coordinates 
     * using {@link #toBoardCoord(Optional, Optional)}.</p>
     *
     * <p>If no components of the specified type are found, an empty set is returned.</p>
     *
     * @param componentType The {@link Class} type of the {@link ComponentTile} to search for.
     * @return A {@link Set} of {@link List} elements representing the positions of the matching components in board coordinates.
     */
    public Set<List<Integer>> getAllComponentsPositionOfType(Class<? extends ComponentTile> componentType) {
        Optional<Set<List<Integer>>> tileComponentPositions = Optional.ofNullable(ship.getAllComponentsPositionOfType(componentType));
        
        return tileComponentPositions
            .map(positions -> positions.stream()
                .map(coord -> this.toBoardCoord(Optional.of(coord.get(0)), Optional.of(coord.get(1))))
                .map(optionalList -> optionalList.stream()
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .toList())
                .collect(Collectors.toSet()))
            .orElse(Set.of());
    }

    /**
     * Adds a component tile to the specified position in the ship's grid.
     *
     * <p>This method places a given {@link ComponentTile} at the specified row and column 
     * coordinates. If the position is out of bounds or if the component cannot be placed 
     * in the given location due to structural constraints, an exception is thrown.</p>
     *
     * @param row       The row index where the component should be placed.
     * @param column    The column index where the component should be placed.
     * @param component The {@link ComponentTile} to be added to the ship.
     *
     * @throws IndexOutOfBoundsException         If the specified row or column is out of the ship's bounds.
     * @throws IllegalComponentPositionException If the component is typing to be placed outside the ship.
     */
    public void addComponentTile(int row, int column, ComponentTile component) throws IndexOutOfBoundsException, IllegalComponentPositionException {
        List<Integer> tileCoords = new ArrayList<>();
        tileCoords.add(this.toTileMatrixCoord(Optional.of(row), Optional.empty()).get(0).get());
        tileCoords.add(this.toTileMatrixCoord(Optional.empty(), Optional.of(column)).get(1).get());

        ship.addComponentTile(tileCoords.get(0), tileCoords.get(1), component);
    }

    /**
     * Removes a component tile from the specified position in the ship's grid.
     *
     * <p>This method removes the {@link ComponentTile} located at the given row and column 
     * coordinates. The removed component is added to the discarded tile collection. 
     * If the position is out of bounds or does not contain a component, an exception is thrown.</p>
     *
     * @param row    The row index of the component to be removed.
     * @param column The column index of the component to be removed.
     *
     * @throws IndexOutOfBoundsException         If the specified row or column is out of the ship's bounds.
     * @throws IllegalComponentPositionException If there is no component at the specified position.
     */
    public void removeComponentTile(int row, int column) throws IndexOutOfBoundsException, IllegalComponentPositionException {
        ComponentTile removedComponent;
        List<Integer> tileCoords = new ArrayList<>();
        tileCoords.add(this.toTileMatrixCoord(Optional.of(row), Optional.empty()).get(0).get());
        tileCoords.add(this.toTileMatrixCoord(Optional.empty(), Optional.of(column)).get(1).get());

        removedComponent = ship.removeComponentTile(tileCoords.get(0), tileCoords.get(1));
        this.discardedTile.add(removedComponent);
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

    /**
     * Counts the number of exposed connectors of the component at the specified position.
     *
     * <p>This method determines how many connectors of the {@link ComponentTile} at the given 
     * row and column are exposed, meaning they are not connected to a compatible neighbor.</p>
     *
     * <p>If the specified row or column is out of bounds, an {@link IndexOutOfBoundsException} is thrown.</p>
     *
     * @param row    The row index of the component.
     * @param column The column index of the component.
     * @return The number of exposed connectors of the component at the specified position. If the position is empty or outside the board, returns 0
     * @throws IndexOutOfBoundsException If the specified row or column is out of the ship's bounds.
     */
    public int countExposedConnectorsOf(int row, int column) throws IndexOutOfBoundsException {
        List<Integer> tileCoords = new ArrayList<>();
        tileCoords.add(this.toTileMatrixCoord(Optional.of(row), Optional.empty()).get(0).get());
        tileCoords.add(this.toTileMatrixCoord(Optional.empty(), Optional.of(column)).get(1).get());

        return ship.countExposedConnectors(tileCoords.get(0), tileCoords.get(1));
    }

    /**
     * Counts the total number of exposed connectors across all components in the ship.
     *
     * <p>This method iterates over all component types present in the ship and 
     * accumulates the number of exposed connectors for each component. A connector is 
     * considered exposed if it is not connected to a compatible neighboring component.</p>
     *
     * <p>Components of type {@link OutOfBoundsTile} are ignored in this calculation.</p>
     *
     * @return The total number of exposed connectors in the ship.
     * @throws IndexOutOfBoundsException If an invalid coordinate is encountered during the computation.
     */
    public int countAllExposedConnectors() throws IndexOutOfBoundsException {
        int exposedConnectors = 0;
        Set<Class<? extends ComponentTile>> componentTypes = ship.getAllComponentsTypes();

        for (Class<? extends ComponentTile> componentType : componentTypes) {
            if (componentType != OutOfBoundsTile.class) {
                for (List<Integer> coord : ship.getAllComponentsPositionOfType(componentType)) {
                    exposedConnectors += ship.countExposedConnectors(coord.get(0), coord.get(1));
                }
            }
        }
        return exposedConnectors;
    }

    /**
     * Counts the total number of human crewmates aboard the ship.
     *
     * <p>This method iterates through all {@link CentralCabin} and {@link CabinModule} components
     * to count the number of human crewmates present. A crewmate is considered human if 
     * they do not require life support.</p>
     *
     * <p>The method first collects all crewmates in {@link CentralCabin} components, as they are 
     * exclusively human. Then, it checks each crewmate in {@link CabinModule} components and 
     * includes only those that do not require life support.</p>
     *
     * @return The total number of human crewmates aboard the ship.
     */
    public int countHumans() {
        Set<List<Integer>> centralCabinsCoords = this.getAllComponentsPositionOfType(CentralCabin.class);
        Set<List<Integer>> cabinCoords  = this.getAllComponentsPositionOfType(CabinModule.class);
        int humans = 0;

        for (List<Integer> coord : centralCabinsCoords) {
            CentralCabin cabin = (CentralCabin) this.getComponent(coord.get(0), coord.get(1)).get();

            humans += cabin.getCrewmates().size();
        }

        for (List<Integer> coord : cabinCoords) {
            CabinModule cabin = (CabinModule) this.getComponent(coord.get(0), coord.get(1)).get();

            List<Crewmate> crewmates = cabin.getCrewmates();
            for (Crewmate crewmate : crewmates) {
                if (!crewmate.requiresLifeSupport()) {
                    humans++;
                }
            }
        }
        return humans;
    }

    /**
     * Counts the total number of crewmates aboard the ship.
     *
     * <p>This method iterates through all {@link CentralCabin} and {@link CabinModule} components
     * to count the number of crewmates present. Crewmates can be either human or alien.</p>
     *
     * <p>The method first collects all crewmates from {@link CentralCabin} components, then 
     * continues by counting those in {@link CabinModule} components.</p>
     *
     * @return The total number of crewmates aboard the ship.
     */
    public int countCrewmates() {
        Set<List<Integer>> centralCabinsCoords = this.getAllComponentsPositionOfType(CentralCabin.class);
        Set<List<Integer>> cabinCoords  = this.getAllComponentsPositionOfType(CabinModule.class);
        int crewmates = 0;

        for (List<Integer> coord : centralCabinsCoords) {
            CentralCabin cabin = (CentralCabin) this.getComponent(coord.get(0), coord.get(1)).get();

            crewmates += cabin.getCrewmates().size();
        }

        for (List<Integer> coord : cabinCoords) {
            CabinModule cabin = (CabinModule) this.getComponent(coord.get(0), coord.get(1)).get();

            crewmates += cabin.getCrewmates().size();
        }
        return crewmates;
    }

    public double calculateFirePower(){
        Set<List<Integer>> singleCannonCords = this.getAllComponentsPositionOfType(CentralCabin.class);
        double baseFirePower = 0;
        double additionalFirePower = 0;

        for (List<Integer> coord : singleCannonCords) {
            SingleCannon cannon = (SingleCannon) this.getComponent(coord.get(0), coord.get(1)).get();

            baseFirePower += cannon.getFirePower();
        }

        //TODO: CAMBIARE STATO E OTTENERE COMPONENTI SCARTANDO ENERGIA
        return baseFirePower + additionalFirePower;
    }

    public int calculateEnginePower() {
        Set<List<Integer>> singleEngineCoords = this.getAllComponentsPositionOfType(SingleEngine.class);
        int baseEnginePower = 0;
        int additionalEnginePower = 0;

        for (List<Integer> coord : singleEngineCoords) {
            SingleEngine engine = (SingleEngine) this.getComponent(coord.get(0), coord.get(1)).get();

            baseEnginePower += engine.getEnginePower();
        }

        //TODO: CAMBIARE STATO E OTTENERE COMPONENTI SCARTANDO ENERGIA
        return baseEnginePower + additionalEnginePower;
    }
}
