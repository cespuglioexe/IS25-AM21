package it.polimi.it.galaxytrucker.model.managers;

import it.polimi.it.galaxytrucker.model.componenttiles.ComponentTile;
import it.polimi.it.galaxytrucker.model.componenttiles.DoubleCannon;
import it.polimi.it.galaxytrucker.model.componenttiles.DoubleEngine;
import it.polimi.it.galaxytrucker.model.componenttiles.EnergyConsumer;
import it.polimi.it.galaxytrucker.model.componenttiles.LifeSupport;
import it.polimi.it.galaxytrucker.model.componenttiles.OutOfBoundsTile;
import it.polimi.it.galaxytrucker.model.componenttiles.Shield;
import it.polimi.it.galaxytrucker.model.componenttiles.SingleCannon;
import it.polimi.it.galaxytrucker.model.componenttiles.SingleEngine;
import it.polimi.it.galaxytrucker.model.componenttiles.SpecialCargoHold;
import it.polimi.it.galaxytrucker.model.componenttiles.TileEdge;
import it.polimi.it.galaxytrucker.model.crewmates.Alien;
import it.polimi.it.galaxytrucker.model.crewmates.Crewmate;
import it.polimi.it.galaxytrucker.model.crewmates.Human;
import it.polimi.it.galaxytrucker.model.componenttiles.BatteryComponent;
import it.polimi.it.galaxytrucker.model.componenttiles.CabinModule;
import it.polimi.it.galaxytrucker.model.componenttiles.CargoHold;
import it.polimi.it.galaxytrucker.model.componenttiles.CentralCabin;
import it.polimi.it.galaxytrucker.model.utility.Cargo;
import it.polimi.it.galaxytrucker.model.utility.Color;
import it.polimi.it.galaxytrucker.model.utility.Direction;
import it.polimi.it.galaxytrucker.model.utility.AlienType;
import it.polimi.it.galaxytrucker.exceptions.IllegalComponentPositionException;
import it.polimi.it.galaxytrucker.exceptions.InvalidActionException;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

/**
 * The {@code ShipManager} class is responsible for managing the components and
 * crew
 * of a spaceship in the Galaxy Trucker game. It provides high-level operations
 * for
 * adding, removing, and managing ship components while ensuring game rules and
 * constraints are met.
 *
 * <p>
 * This class acts as a controller that interacts with the underlying
 * {@link ShipBoard},
 * translating board coordinates into tile matrix coordinates, handling the
 * placement and
 * removal of components, and enforcing constraints related to crew, aliens,
 * engines, and cargo.
 * </p>
 *
 * <p>
 * Main functionalities include:
 * </p>
 * <ul>
 * <li>Managing the ship's grid layout based on game level.</li>
 * <li>Adding and removing components while ensuring legal placement.</li>
 * <li>Handling crew members, including human and alien placement with life
 * support checks.</li>
 * <li>Tracking energy sources such as batteries.</li>
 * <li>Calculating ship attributes such as firepower and engine power.</li>
 * </ul>
 *
 * <p>
 * The ship is represented internally by an instance of {@link ShipBoard}, which
 * maintains the actual grid structure and components.
 * </p>
 *
 * <h2>Example of usage:</h2>
 * 
 * <pre>
 * ShipManager manager = new ShipManager(1); // Initialize for level 1
 * manager.addComponentTile(2, 3, new EngineModule());
 * manager.addCrewmate(2, 3, new Human());
 * boolean isValid = manager.isShipLegal();
 * </pre>
 *
 * @author Stefano Carletto
 * @version 1.4
 */
public class ShipManager {
    private final ShipBoard ship;
    private final List<ComponentTile> discardedTile;
    private final HashMap<AlienType, Boolean> hasAlien;

    static private final int ROWS = 5;
    static private final int COLUMNS = 7;
    static private final int STARTOFBOARDROWS = 5;
    static private final int STARTOFBOARDCOLUMNS = 4;
    static private final int SAVE_LIMIT = 2;

    public ShipManager(int level) {
        this.ship = new ShipBoard(level);
        addCrewmatesToCentralCabin();
        discardedTile = new ArrayList<>(2);
        this.hasAlien = new HashMap<>();
    }
    private void addCrewmatesToCentralCabin() {
        List<Integer> centralCabinCoords = ship.getAllComponentsPositionOfType(CentralCabin.class).stream()
            .findFirst()
            .orElseThrow(() -> new IllegalStateException("No central cabin found"));
        
        CentralCabin cabin = (CentralCabin) ship.getComponent(centralCabinCoords.get(0), centralCabinCoords.get(1)).get();
        cabin.addCrewmate(new Human());
        cabin.addCrewmate(new Human());
    }

    public List<List<ComponentTile>> getShipBoard() {
        return ship.getShipBoard();
    }

    public void saveComponentTile(ComponentTile componentTile) throws InvalidActionException {
        if (discardedTile.size() >= SAVE_LIMIT) {
            throw new InvalidActionException("Cannot save more than " + SAVE_LIMIT + " components");
        }
        discardedTile.add(componentTile);
    }

    public List<ComponentTile> getSavedComponentTiles() {
        return discardedTile;
    }

    public ComponentTile getSavedComponentTile(int index) {
        ComponentTile comp = discardedTile.get(index);
        discardedTile.remove(index);
        return comp;
    }

    /**
     * Converts the given row and/or column from board coordinates to tile matrix
     * coordinates.
     *
     * <p>
     * This method adjusts the given row and column indices by subtracting the
     * board's
     * starting indices, as defined in {@link ShipManager#STARTOFBOARDROWS} and
     * {@link ShipManager#STARTOFBOARDCOLUMNS}, to map them correctly within the
     * ship's tile matrix.
     * </p>
     *
     * <p>
     * If a row or column index is out of bounds, an
     * {@link IndexOutOfBoundsException} is thrown.
     * </p>
     *
     * @param row    An {@link Optional} containing the row index to convert, or
     *               empty if not provided.
     * @param column An {@link Optional} containing the column index to convert, or
     *               empty if not provided.
     * @return A list containing two {@link Optional} elements: the converted row
     *         and column indices.
     *
     * @throws IndexOutOfBoundsException If the row or column is out of the ship's
     *                                   tile matrix bounds.
     */
    private List<Optional<Integer>> toTileMatrixCoord(Optional<Integer> row, Optional<Integer> column)
            throws IndexOutOfBoundsException {
        Optional<Integer> tileMatrixRow = row.map(r -> r - ShipManager.STARTOFBOARDROWS);
        Optional<Integer> tileMatrixColumn = column.map(c -> c - ShipManager.STARTOFBOARDCOLUMNS);

        // checking matrix bound
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
     * Converts the given row and/or column from tile matrix coordinates to board
     * coordinates.
     *
     * <p>
     * This method adjusts the given row and column indices by adding the board's
     * starting indices,
     * as defined in {@link ShipManager#STARTOFBOARDROWS} and
     * {@link ShipManager#STARTOFBOARDCOLUMNS},
     * to map them correctly within the board's coordinate system.
     * </p>
     *
     * <p>
     * If a row or column index is out of bounds, an
     * {@link IndexOutOfBoundsException} is thrown.
     * </p>
     *
     * @param row    An {@link Optional} containing the row index to convert, or
     *               empty if not provided.
     * @param column An {@link Optional} containing the column index to convert, or
     *               empty if not provided.
     * @return A list containing two {@link Optional} elements: the converted row
     *         and column indices.
     *
     * @throws IndexOutOfBoundsException If the row or column is out of the board's
     *                                   valid bounds.
     */
    private List<Optional<Integer>> toBoardCoord(Optional<Integer> row, Optional<Integer> column)
            throws IndexOutOfBoundsException {
        Optional<Integer> boardRow = row.map(r -> r + ShipManager.STARTOFBOARDROWS);
        Optional<Integer> boardColumn = column.map(c -> c + ShipManager.STARTOFBOARDCOLUMNS);

        // checking matrix bound
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

    public static int getStartRow() {
        return STARTOFBOARDROWS;
    }

    public static int getStartColumn() {
        return STARTOFBOARDCOLUMNS;
    }

    /**
     * Returns the total number of rows in the ship's grid.
     *
     * <p>
     * This method provides access to the number of rows defined in the
     * {@link ShipManager}.
     * </p>
     *
     * @return The total number of rows in the ship's grid.
     */
    public static int getRows() {
        return ShipManager.ROWS;
    }

    /**
     * Returns the total number of columns in the ship's grid.
     *
     * <p>
     * This method provides access to the number of columns defined in the
     * {@link ShipManager}.
     * </p>
     *
     * @return The total number of columns in the ship's grid.
     */
    public static int getColumns() {
        return ShipManager.COLUMNS;
    }

    /**
     * Retrieves the component located at the specified position in the ship's grid.
     *
     * <p>
     * This method returns an {@link Optional} containing the {@link ComponentTile}
     * at the given row and column coordinates, if present. If no component exists
     * at
     * the specified location, an empty {@code Optional} is returned.
     * </p>
     *
     * <p>
     * If the specified row or column is out of the valid bounds of the ship,
     * an {@link IndexOutOfBoundsException} is thrown.
     * </p>
     *
     * @param row    The row index of the component to retrieve.
     * @param column The column index of the component to retrieve.
     * @return An {@link Optional} containing the {@link ComponentTile} if present,
     *         or empty if no component exists.
     * @throws IndexOutOfBoundsException If the specified row or column is out of
     *                                   the ship's bounds.
     */
    public Optional<ComponentTile> getComponent(int row, int column) throws IndexOutOfBoundsException {
        List<Integer> tileCoords = new ArrayList<>();
        tileCoords.add(this.toTileMatrixCoord(Optional.of(row), Optional.empty()).get(0).get());
        tileCoords.add(this.toTileMatrixCoord(Optional.empty(), Optional.of(column)).get(1).get());

        return ship.getComponent(tileCoords.get(0), tileCoords.get(1));
    }

    public boolean isOutside(int row, int column) throws IndexOutOfBoundsException {
        List<Integer> tileCoords = new ArrayList<>();
        tileCoords.add(this.toTileMatrixCoord(Optional.of(row), Optional.empty()).get(0).get());
        tileCoords.add(this.toTileMatrixCoord(Optional.empty(), Optional.of(column)).get(1).get());

        Optional<ComponentTile> component =  ship.getComponent(tileCoords.get(0), tileCoords.get(1));

        return component
            .map(c -> c.getClass().equals(OutOfBoundsTile.class))
            .orElse(false);
    }

    /**
     * Retrieves all components located in the specified row of the ship's grid.
     *
     * <p>
     * This method returns a list of {@link Optional} elements, each representing a
     * {@link ComponentTile} located at the given row and every column in the ship's
     * grid.
     * If a position does not contain a component, the corresponding
     * {@code Optional} will be empty.
     * </p>
     *
     * <p>
     * If the specified row is out of the valid bounds of the ship,
     * an {@link IndexOutOfBoundsException} is thrown.
     * </p>
     *
     * @param row The row index for which to retrieve all components.
     * @return A list of {@link Optional<ComponentTile>} representing the components
     *         in the specified row.
     * @throws IndexOutOfBoundsException If the specified row is out of the ship's
     *                                   bounds.
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
     * <p>
     * This method returns a list of {@link Optional} elements, each representing a
     * {@link ComponentTile} located at the given column and every row in the ship's
     * grid.
     * If a position does not contain a component, the corresponding
     * {@code Optional} will be empty.
     * </p>
     *
     * <p>
     * If the specified column is out of the valid bounds of the ship,
     * an {@link IndexOutOfBoundsException} is thrown.
     * </p>
     *
     * @param column The column index for which to retrieve all components.
     * @return A list of {@link Optional<ComponentTile>} representing the components
     *         in the specified column.
     * @throws IndexOutOfBoundsException If the specified column is out of the
     *                                   ship's bounds.
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
     * <p>
     * This method returns a {@link Set} containing the {@link Class} objects of all
     * {@link ComponentTile} types currently placed in the ship. Each entry in the
     * set represents
     * a unique type of component found on the ship's grid.
     * </p>
     *
     * <p>
     * If no components are present in the ship, this method returns an empty set.
     * </p>
     *
     * @return A {@link Set} of {@link Class} objects representing the distinct
     *         component types in the ship,
     *         or an empty set if no components are present.
     */
    public Set<Class<? extends ComponentTile>> getAllComponentsType() {
        return ship.getAllComponentsTypes();
    }

    /**
     * Retrieves the positions of all components of a specified type in board
     * coordinates.
     *
     * <p>
     * This method returns a {@link Set} of positions where components of the given
     * type
     * are located. Each position is represented as a {@link List} of two integers
     * (row and column).
     * The positions are converted from tile matrix coordinates to board coordinates
     * using {@link #toBoardCoord(Optional, Optional)}.
     * </p>
     *
     * <p>
     * If no components of the specified type are found, an empty set is returned.
     * </p>
     *
     * @param componentType The {@link Class} type of the {@link ComponentTile} to
     *                      search for.
     * @return A {@link Set} of {@link List} elements representing the positions of
     *         the matching components in board coordinates.
     */
    public Set<List<Integer>> getAllComponentsPositionOfType(Class<? extends ComponentTile> componentType) {
        Optional<Set<List<Integer>>> tileComponentPositions = Optional
                .ofNullable(ship.getAllComponentsPositionOfType(componentType));

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
     * Retrieves the disconnected branches of the ship, represented in board
     * coordinates.
     *
     * <p>
     * A disconnected branch is a set of tiles that are not connected to the main
     * ship structure.
     * This method calls {@link ShipBoard#getDisconnectedBranches()} to obtain the
     * branches in
     * tile matrix coordinates and then converts them into board coordinates.
     * </p>
     *
     * <p>
     * The result is a list of sets, where each set represents a disconnected
     * branch.
     * Each position in the set is a list of two integers: the row and column of a
     * component
     * in board coordinates.
     * </p>
     *
     * <p>
     * <b>Example of usage:</b>
     * </p>
     * 
     * <pre>
     * {@code
     * List<Set<List<Integer>>> branches = shipManager.getDisconnectedBranches();
     * for (Set<List<Integer>> branch : branches) {
     *     System.out.println("Disconnected branch: " + branch);
     * }
     * </pre>
     *
     * @return A list of sets, where each set represents a disconnected branch.
     */
    public List<Set<List<Integer>>> getDisconnectedBranches() {
        List<Set<List<Integer>>> disconnectedBranches = this.ship.getDisconnectedBranches();
        List<Set<List<Integer>>> DisconnectedBrachesInBoardCoord = new ArrayList<>();

        for (Set<List<Integer>> branch : disconnectedBranches) {
            Set<List<Integer>> branchInBoardCoord = new HashSet<>();

            for (List<Integer> coord : branch) {
                List<Optional<Integer>> boardCoord = this.toBoardCoord(Optional.of(coord.get(0)),
                        Optional.of(coord.get(1)));
                branchInBoardCoord.add(List.of(boardCoord.get(0).get(), boardCoord.get(1).get()));
            }

            DisconnectedBrachesInBoardCoord.add(branchInBoardCoord);
        }

        return DisconnectedBrachesInBoardCoord;
    }

    /**
     * Adds a component tile to the specified position in the ship's grid.
     *
     * <p>
     * This method places a given {@link ComponentTile} at the specified row and
     * column
     * coordinates. If the position is out of bounds or if the component cannot be
     * placed
     * in the given location due to structural constraints, an exception is thrown.
     * </p>
     *
     * @param row       The row index where the component should be placed.
     * @param column    The column index where the component should be placed.
     * @param component The {@link ComponentTile} to be added to the ship.
     *
     * @throws IndexOutOfBoundsException         If the specified row or column is
     *                                           out of the ship's bounds.
     * @throws IllegalComponentPositionException If the component is typing to be
     *                                           placed outside the ship.
     */
    public void addComponentTile(int row, int column, ComponentTile component)
            throws IndexOutOfBoundsException, IllegalComponentPositionException {
        List<Integer> tileCoords = new ArrayList<>();
        tileCoords.add(this.toTileMatrixCoord(Optional.of(row), Optional.empty()).get(0).get());
        tileCoords.add(this.toTileMatrixCoord(Optional.empty(), Optional.of(column)).get(1).get());

        ship.addComponentTile(tileCoords.get(0), tileCoords.get(1), component);
    }

    /**
     * Removes a component tile from the specified position in the ship's grid.
     *
     * <p>
     * This method removes the {@link ComponentTile} located at the given row and
     * column
     * coordinates. If the removed component affects other game elements,
     * appropriate updates
     * are made.
     * </p>
     *
     * <p>
     * Special cases:
     * </p>
     * <ul>
     * <li>If the removed component is a {@link CentralCabin} containing a crew, the
     * crew is removed.</li>
     * <li>If the removed component is a {@link LifeSupport} module, any nearby
     * aliens that depend on it are also removed.</li>
     * </ul>
     *
     * @param row    The row index of the component to be removed.
     * @param column The column index of the component to be removed.
     *
     * @throws IndexOutOfBoundsException         If the specified row or column is
     *                                           out of the ship's bounds.
     * @throws IllegalComponentPositionException If there is no component at the
     *                                           specified position.
     */
    public void removeComponentTile(int row, int column)
            throws IndexOutOfBoundsException, IllegalComponentPositionException {
        ComponentTile removedComponent;
        List<Integer> tileCoords = new ArrayList<>();
        tileCoords.add(this.toTileMatrixCoord(Optional.of(row), Optional.empty()).get(0).get());
        tileCoords.add(this.toTileMatrixCoord(Optional.empty(), Optional.of(column)).get(1).get());

        // removing the component
        removedComponent = ship.removeComponentTile(tileCoords.get(0), tileCoords.get(1));
        this.discardedTile.add(removedComponent);

        // If was a cabin had an alien, removing aliens from it
        if (removedComponent instanceof CentralCabin) {
            CentralCabin removedCabin = (CentralCabin) removedComponent;

            if (removedCabin.getCrewmates().size() == 1) {
                Crewmate crewmate = removedCabin.getCrewmates().get(0);

                if (crewmate.requiresLifeSupport()) {
                    Alien alien = (Alien) crewmate;
                    this.hasAlien.put(alien.getAlienType(), false);
                }
            }
        }

        // If it was a LifeSupport module, removing all nearby aliens
        if (removedComponent instanceof LifeSupport) {
            for (Optional<ComponentTile> neighbor : ship.getNeighbourComponents(tileCoords.get(0), tileCoords.get(1))) {
                if (neighbor.isPresent() && neighbor.get().getClass().equals(CabinModule.class)) {
                    CabinModule cabin = (CabinModule) neighbor.get();

                    if (cabin.getCrewmates().size() == 1) {
                        Crewmate crewmate = cabin.getCrewmates().get(0);

                        if (crewmate.requiresLifeSupport()) {
                            cabin.removeCrewmate();

                            Alien alien = (Alien) crewmate;
                            this.hasAlien.put(alien.getAlienType(), false);
                        }
                    }
                }
            }
        }
    }

    /**
     * Removes a specified branch of components from the ship.
     *
     * <p>
     * This method takes a set of coordinates representing a disconnected branch
     * in board coordinates and removes the corresponding components from the ship.
     * </p>
     *
     * <p>
     * The given board coordinates are first converted to tile matrix coordinates
     * before calling {@link ShipBoard#removeBranch(Set)} to remove the branch.
     * </p>
     *
     * <p>
     * <b>Example of usage:</b>
     * </p>
     * 
     * <pre>
     * {@code
     * Set<List<Integer>> branch = Set.of(
     *         List.of(5, 6),
     *         List.of(5, 7),
     *         List.of(6, 7));
     * shipManager.removeBranch(branch);
     * }
     * </pre>
     *
     * @param branch A set of positions representing the branch to remove,
     *               where each position is a list containing two integers (row and
     *               column) in board coordinates.
     *
     * @throws IndexOutOfBoundsException         If any coordinate is out of the
     *                                           ship's valid bounds.
     * @throws IllegalComponentPositionException If a specified coordinate does not
     *                                           correspond to a valid component.
     */
    public void removeBranch(Set<List<Integer>> branch)
            throws IndexOutOfBoundsException, IllegalComponentPositionException {
        Set<List<Integer>> tileBranch = new HashSet<>();

        for (List<Integer> coord : branch) {
            List<Optional<Integer>> tileCoord = this.toTileMatrixCoord(Optional.of(coord.get(0)),
                    Optional.of(coord.get(1)));

            tileBranch.add(List.of(tileCoord.get(0).get(), tileCoord.get(1).get()));
        }

        this.ship.removeBranch(tileBranch);
    }

    /**
     * Determines whether the ship's construction is legal according to the game
     * rules.
     *
     * <p>
     * The method verifies several conditions to ensure that the ship meets the
     * requirements:
     * <ul>
     * <li>The ship must not be divided into disconnected sections.</li>
     * <li>All {@link SingleEngine} and {@link DoubleEngine} components must be
     * oriented backwards (rotation must be 0).</li>
     * <li>All connectors between adjacent components must be compatible.</li>
     * </ul>
     * If any of these conditions are not met, the ship is considered illegal.
     * </p>
     *
     * @return {@code true} if the ship is legal, {@code false} otherwise.
     */
    public boolean isShipLegal() {
        // if the ship is divided into disconnected sections it's not legal
        if (ship.getDisconnectedBranches().size() > 1) {
            return false;
        }

        // checking if single engines are all pointing backwards: if not then it's not
        // legal
        Set<List<Integer>> singleEnginePositions = Optional
                .ofNullable(ship.getAllComponentsPositionOfType(SingleEngine.class))
                .orElse(Collections.emptySet());
        for (List<Integer> coord : singleEnginePositions) {
            if (ship.getComponent(coord.get(0), coord.get(1)).get().getRotation() != 0) {
                return false;
            }
        }

        // checking if double engines are all pointing backwards: if not then it's not
        // legal
        Set<List<Integer>> doubleEnginePositions = Optional
                .ofNullable(ship.getAllComponentsPositionOfType(DoubleEngine.class))
                .orElse(Collections.emptySet());
        for (List<Integer> coord : doubleEnginePositions) {
            if (ship.getComponent(coord.get(0), coord.get(0)).get().getRotation() != 0) {
                return false;
            }
        }

        // checking if connectors match: if not then it's not legal
        Set<Class<? extends ComponentTile>> componentTypes = ship.getAllComponentsTypes();
        for (Class<? extends ComponentTile> componentType : componentTypes) {
            for (List<Integer> coord : ship.getAllComponentsPositionOfType(componentType)) {

                ComponentTile component = ship.getComponent(coord.get(0), coord.get(1)).get();
                List<Optional<ComponentTile>> neighbors = ship.getNeighbourComponents(coord.get(0), coord.get(1));
                Optional<ComponentTile> neighbor;

                TileEdge componentConnector;
                TileEdge neighborConnector;

                // up direction
                neighbor = neighbors.get(0);
                if (neighbor.isPresent() && !neighbor.get().getClass().equals(OutOfBoundsTile.class)) {
                    componentConnector = component.getTileEdges().get(0);
                    neighborConnector = neighbor.get().getTileEdges().get(2);

                    if (!componentConnector.isCompatible(neighborConnector)) {
                        return false;
                    }
                }

                // right direction
                neighbor = neighbors.get(1);
                if (neighbor.isPresent() && !neighbor.get().getClass().equals(OutOfBoundsTile.class)) {
                    componentConnector = component.getTileEdges().get(1);
                    neighborConnector = neighbor.get().getTileEdges().get(3);

                    if (!componentConnector.isCompatible(neighborConnector)) {
                        return false;
                    }
                }

                // down direction
                neighbor = neighbors.get(2);
                if (neighbor.isPresent() && !neighbor.get().getClass().equals(OutOfBoundsTile.class)) {
                    componentConnector = component.getTileEdges().get(2);
                    neighborConnector = neighbor.get().getTileEdges().get(0);

                    if (!componentConnector.isCompatible(neighborConnector)) {
                        return false;
                    }
                }

                // left direction
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
     * Adds a human crewmate to a specified position within the ship.
     *
     * <p>
     * This method attempts to place a given {@link Human} crewmate at the specified
     * row and column coordinates on the ship. The following conditions are checked:
     * </p>
     * <ul>
     * <li>The target position must contain a valid {@link CentralCabin} instance;
     * otherwise,
     * an {@link IllegalComponentPositionException} is thrown.</li>
     * <li>If no component exists at the specified position, an
     * {@link IllegalComponentPositionException} is thrown.</li>
     * </ul>
     *
     * <p>
     * If all conditions are met, the human crewmate is added to the cabin.
     * </p>
     *
     * @param row      The row index where the crewmate should be placed.
     * @param column   The column index where the crewmate should be placed.
     * @param crewmate The {@link Human} crewmate to be added to the cabin.
     *
     * @throws IndexOutOfBoundsException         If the specified row or column is
     *                                           out of the ship's bounds.
     * @throws IllegalComponentPositionException If the target position does not
     *                                           contain a valid cabin module.
     * @throws InvalidActionException            If the cabin is full.
     */
    public void addCrewmate(int row, int column, Human crewmate)
            throws IndexOutOfBoundsException, IllegalComponentPositionException, InvalidActionException {
        List<Integer> tileCoords = new ArrayList<>();
        tileCoords.add(this.toTileMatrixCoord(Optional.of(row), Optional.empty()).get(0).get());
        tileCoords.add(this.toTileMatrixCoord(Optional.empty(), Optional.of(column)).get(1).get());

        // check if is empty or it's not a cabin module
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
     * <p>
     * This method attempts to place a given {@link Alien} crewmate at the specified
     * row and column coordinates on the ship. Before adding the alien, the
     * following
     * conditions are checked:
     * </p>
     * <ul>
     * <li>An alien of the same type has not already been placed. If an alien of
     * this
     * type is already present, an {@link InvalidActionException} is thrown.</li>
     * <li>The target position must contain a valid {@link CabinModule}; otherwise,
     * an {@link IllegalComponentPositionException} is thrown.</li>
     * <li>A compatible {@link LifeSupport} module must be adjacent to the cabin. If
     * not,
     * an {@link InvalidActionException} is thrown.</li>
     * </ul>
     *
     * <p>
     * If all conditions are met, the alien is added to the cabin, and its type is
     * recorded
     * to prevent placing another alien of the same type.
     * </p>
     *
     * @param row      The row index where the crewmate should be placed.
     * @param column   The column index where the crewmate should be placed.
     * @param crewmate The {@link Alien} crewmate to be added to the cabin.
     *
     * @throws IndexOutOfBoundsException         If the specified row or column is
     *                                           out of the ship's bounds.
     * @throws IllegalComponentPositionException If the target position does not
     *                                           contain a valid cabin module.
     * @throws InvalidActionException            If an alien of this type is already
     *                                           present or if no
     *                                           compatible life support module is
     *                                           adjacent or if the cabin is full.
     */
    public void addCrewmate(int row, int column, Alien crewmate)
            throws IndexOutOfBoundsException, IllegalComponentPositionException, InvalidActionException {
        List<Integer> tileCoords = new ArrayList<>();
        tileCoords.add(this.toTileMatrixCoord(Optional.of(row), Optional.empty()).get(0).get());
        tileCoords.add(this.toTileMatrixCoord(Optional.empty(), Optional.of(column)).get(1).get());

        // check if the alien color is already present
        if (this.hasAlien(crewmate.getAlienType())) {
            throw new InvalidActionException("You already placed an alien of this color");
        }

        // check if is empty or it's not a cabin module
        ComponentTile component = ship.getComponent(tileCoords.get(0), tileCoords.get(1))
                .orElseThrow(() -> new IllegalComponentPositionException("There is no element here"));
        if (!(component instanceof CentralCabin)) {
            throw new IllegalComponentPositionException("Not a cabin module at [" + row + "][" + column + "]");
        }

        CabinModule cabin = (CabinModule) component;

        // check for life support module near the cabin
        List<Optional<ComponentTile>> neighbors = this.ship.getNeighbourComponents(tileCoords.get(0),
                tileCoords.get(1));

        boolean hasLifeSupport = neighbors.stream()
                .flatMap(Optional::stream)
                .filter(neighbor -> neighbor instanceof LifeSupport)
                .map(neighbor -> (LifeSupport) neighbor)
                .anyMatch(l -> l.getSupportedAlienType().equals(crewmate.getAlienType()));

        if (!hasLifeSupport) {
            throw new InvalidActionException("The cabin module has not a life support module nearby");
        }

        cabin.addCrewmate(crewmate);
        this.hasAlien.put(crewmate.getAlienType(), true);
    }

    /**
     * Removes a crewmate from the specified position within the ship.
     *
     * <p>
     * This method attempts to remove a crewmate from the cabin located at the
     * specified
     * row and column coordinates. The following conditions are checked:
     * </p>
     * <ul>
     * <li>The target position must contain a valid {@link CentralCabin} instance;
     * otherwise,
     * an {@link IllegalComponentPositionException} is thrown.</li>
     * <li>If no component exists at the specified position, an
     * {@link IllegalComponentPositionException} is thrown.</li>
     * </ul>
     *
     * <p>
     * If the removed crewmate is an alien requiring life support, the alien type is
     * marked as no longer present in the ship.
     * </p>
     *
     * @param row    The row index of the cabin from which the crewmate should be
     *               removed.
     * @param column The column index of the cabin from which the crewmate should be
     *               removed.
     *
     * @throws IndexOutOfBoundsException         If the specified row or column is
     *                                           out of the ship's bounds.
     * @throws IllegalComponentPositionException If there is no component at the
     *                                           specified position
     *                                           or if the component is not an
     *                                           instance of {@link CentralCabin}.
     * @throws InvalidActionException            If the cabin has no crew.
     */
    public void removeCrewmate(int row, int column)
            throws IndexOutOfBoundsException, IllegalComponentPositionException, InvalidActionException {
        List<Integer> tileCoords = new ArrayList<>();
        tileCoords.add(this.toTileMatrixCoord(Optional.of(row), Optional.empty()).get(0).get());
        tileCoords.add(this.toTileMatrixCoord(Optional.empty(), Optional.of(column)).get(1).get());

        // check if is empty or it's not a cabin module
        ComponentTile component = ship.getComponent(tileCoords.get(0), tileCoords.get(1))
                .orElseThrow(() -> new IllegalComponentPositionException("There is no element here"));
        if (!(component instanceof CentralCabin)) {
            throw new IllegalComponentPositionException("Not a cabin module at [" + row + "][" + column + "]");
        }

        CentralCabin cabin = (CentralCabin) component;
        Crewmate crewmate = cabin.removeCrewmate();
        if (crewmate.requiresLifeSupport()) {
            Alien alien = (Alien) crewmate;
            this.hasAlien.put(alien.getAlienType(), false);
        }
    }

    /**
     * Adds a cargo item to a cargo holder at the specified position within the
     * ship.
     *
     * <p>
     * This method attempts to place a given {@link Cargo} at the specified row and
     * column
     * coordinates on the ship. If the cargo is special, it must be placed in a
     * {@link SpecialCargoHold}.
     * If the target position does not contain the correct type of cargo holder, an
     * exception is thrown.
     * </p>
     *
     * @param row    The row index where the cargo should be placed.
     * @param column The column index where the cargo should be placed.
     * @param cargo  The {@link Cargo} to be added to the cargo hold.
     *
     * @throws IndexOutOfBoundsException         If the specified row or column is
     *                                           out of the ship's bounds.
     * @throws IllegalComponentPositionException If the target position does not
     *                                           contain a cargo holder or
     *                                           if the cargo type is not compatible
     *                                           with the cargo holder type.
     * @throws InvalidActionException            If the cargo holder is full.
     */
    public void addCargo(int row, int column, Cargo cargo)
            throws IndexOutOfBoundsException, IllegalComponentPositionException, InvalidActionException {
        CargoHold cargoHoldComponent;
        List<Integer> tileCoords = new ArrayList<>();
        tileCoords.add(this.toTileMatrixCoord(Optional.of(row), Optional.empty()).get(0).get());
        tileCoords.add(this.toTileMatrixCoord(Optional.empty(), Optional.of(column)).get(1).get());

        // check if is empty
        ComponentTile component = ship.getComponent(tileCoords.get(0), tileCoords.get(1))
                .orElseThrow(() -> new IllegalComponentPositionException("There is no element here"));
        // check the cargo hold type depending on cargo specialty
        if (cargo.isSpecial()) {
            if (!component.getClass().equals(SpecialCargoHold.class)) {
                throw new IllegalComponentPositionException(
                        "Not a special cargo hold module at [" + row + "][" + column + "]");
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
     * Removes a cargo item of the specified color from a cargo hold at the given
     * position.
     *
     * <p>
     * This method attempts to remove a {@link Cargo} item that matches the given
     * {@link Color}
     * from the cargo hold located at the specified row and column coordinates on
     * the ship.
     * If the cargo color is red, the method expects a {@link SpecialCargoHold}.
     * If the correct type of cargo hold is not present, or if no cargo of the
     * specified color is found, an exception is thrown.
     * </p>
     *
     * @param row    The row index of the cargo hold.
     * @param column The column index of the cargo hold.
     * @param color  The {@link Color} of the cargo to be removed.
     *
     * @throws IndexOutOfBoundsException         If the specified row or column is
     *                                           out of the ship's bounds.
     * @throws IllegalComponentPositionException If the target position does not
     *                                           contain a cargo hold or
     *                                           if the cargo type is not compatible
     *                                           with the cargo hold type.
     * @throws InvalidActionException            If no cargo of the specified color
     *                                           is found or if the cargo holder is
     *                                           empty.
     */
    public void removeCargo(int row, int column, Color color)
            throws IndexOutOfBoundsException, IllegalComponentPositionException, InvalidActionException {
        CargoHold cargoHoldComponent;
        List<Integer> tileCoords = new ArrayList<>();
        tileCoords.add(this.toTileMatrixCoord(Optional.of(row), Optional.empty()).get(0).get());
        tileCoords.add(this.toTileMatrixCoord(Optional.empty(), Optional.of(column)).get(1).get());

        // check if is empty
        ComponentTile component = ship.getComponent(tileCoords.get(0), tileCoords.get(1))
                .orElseThrow(() -> new IllegalComponentPositionException("There is no element here"));
        // check the cargo hold type depending on cargo specialty
        if (color.equals(Color.RED)) {
            if (!component.getClass().equals(SpecialCargoHold.class)) {
                throw new IllegalComponentPositionException(
                        "Not a special cargo hold module at [" + row + "][" + column + "]");
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
     * <p>
     * This method attempts to remove a battery from the module located at the
     * specified
     * row and column coordinates on the ship. If the component does not exist or is
     * not a {@link BatteryComponent},
     * an exception is thrown.
     * </p>
     *
     * <p>
     * The method does not physically remove the battery component but instead
     * triggers energy consumption within it.
     * </p>
     *
     * @param row    The row index of the battery module.
     * @param column The column index of the battery module.
     *
     * @throws IllegalComponentPositionException If there is no component at the
     *                                           specified position or
     *                                           if the component is not a
     *                                           {@link BatteryComponent}.
     * @throws InvalidActionException            If the component has no battery
     *                                           left.
     */
    public void removeBattery(int row, int column) throws IllegalComponentPositionException, InvalidActionException {
        BatteryComponent batteryComponent;
        List<Integer> tileCoords = new ArrayList<>();
        tileCoords.add(this.toTileMatrixCoord(Optional.of(row), Optional.empty()).get(0).get());
        tileCoords.add(this.toTileMatrixCoord(Optional.empty(), Optional.of(column)).get(1).get());

        // check if is empty or it's not a battery component
        ComponentTile component = ship.getComponent(tileCoords.get(0), tileCoords.get(1))
                .orElseThrow(() -> new IllegalComponentPositionException("There is no element here"));
        if (!component.getClass().equals(BatteryComponent.class)) {
            throw new IllegalComponentPositionException("Not a battery hold module at [" + row + "][" + column + "]");
        }

        batteryComponent = (BatteryComponent) component;
        batteryComponent.consumeEnergy();
    }

    /**
     * Retrieves the positions of all cargo items aboard the ship, grouped by color.
     *
     * <p>This method scans all cargo hold components ({@link CargoHold} and {@link SpecialCargoHold})
     * and collects the positions of cargo items based on their color.</p>
     *
     * <p>The result is a {@link HashMap} where each key is a {@link Color} representing a cargo type,
     * and the corresponding value is a set of positions (row and column) where cargo of that color is stored.</p>
     *
     * <h3>Functionality:</h3>
     * <ul>
     *     <li>Iterates over all colors defined in {@link Color}.</li>
     *     <li>Scans all {@link SpecialCargoHold} components for special cargo.</li>
     *     <li>Scans all {@link CargoHold} components for non-special cargo.</li>
     *     <li>Groups the cargo positions by color.</li>
     * </ul>
     *
     * <p>Example of usage:</p>
     * <pre>
     *     HashMap<Color, Set<List<Integer>>> cargoPositions = shipManager.getCargoPosition();
     *     for (Map.Entry<Color, Set<List<Integer>>> entry : cargoPositions.entrySet()) {
     *         System.out.println("Color: " + entry.getKey() + ", Positions: " + entry.getValue());
     *     }
     * </pre>
     *
     * @return A {@link HashMap} where the key is a {@link Color}, and the value is a {@link Set} 
     *         of positions (row, column) where cargo of that color is stored.
     */
    public HashMap<Color, Set<List<Integer>>> getCargoPositon() {
        HashMap<Color, Set<List<Integer>>> cargoPosition = new HashMap<>();

        for (Color color : Color.values()) {
            Set<List<Integer>> positions = new HashSet<>();
            cargoPosition.put(color, positions);

            Set<List<Integer>> specialCargoHoldComponent = this.getAllComponentsPositionOfType(SpecialCargoHold.class);

            for (List<Integer> coord : specialCargoHoldComponent) {
                SpecialCargoHold specialCargoHold = (SpecialCargoHold) this.getComponent(coord.get(0), coord.get(1)).get();

                List<Cargo> containedCargo = specialCargoHold.getContainedCargo();

                for (Cargo cargo : containedCargo) {
                    if (cargo.getColor().equals(color)) {
                        positions.add(coord);
                    }
                }
            }

            Cargo specialCargo = new Cargo(color);

            if (specialCargo.isSpecial()) {
                continue;
            }

            Set<List<Integer>> cargoHoldComponent = this.getAllComponentsPositionOfType(CargoHold.class);

            for (List<Integer> coord : cargoHoldComponent) {
                CargoHold cargoHold = (CargoHold) this.getComponent(coord.get(0), coord.get(1)).get();

                List<Cargo> containedCargo = cargoHold.getContainedCargo();

                for (Cargo cargo : containedCargo) {
                    if (cargo.getColor().equals(color)) {
                        positions.add(coord);
                    }
                }
            }
        }

        return cargoPosition;
    }

    /**
     * Counts the number of exposed connectors of the component at the specified
     * position.
     *
     * <p>
     * This method determines how many connectors of the {@link ComponentTile} at
     * the given
     * row and column are exposed, meaning they are not connected to a compatible
     * neighbor.
     * </p>
     *
     * <p>
     * If the specified row or column is out of bounds, an
     * {@link IndexOutOfBoundsException} is thrown.
     * </p>
     *
     * @param row    The row index of the component.
     * @param column The column index of the component.
     * @return The number of exposed connectors of the component at the specified
     *         position. If the position is empty or outside the board, returns 0
     * @throws IndexOutOfBoundsException If the specified row or column is out of
     *                                   the ship's bounds.
     */
    public int countExposedConnectorsOf(int row, int column) throws IndexOutOfBoundsException {
        List<Integer> tileCoords = new ArrayList<>();
        tileCoords.add(this.toTileMatrixCoord(Optional.of(row), Optional.empty()).get(0).get());
        tileCoords.add(this.toTileMatrixCoord(Optional.empty(), Optional.of(column)).get(1).get());

        return ship.countExposedConnectors(tileCoords.get(0), tileCoords.get(1));
    }

    /**
     * Counts the total number of exposed connectors across all components in the
     * ship.
     *
     * <p>
     * This method iterates over all component types present in the ship and
     * accumulates the number of exposed connectors for each component. A connector
     * is
     * considered exposed if it is not connected to a compatible neighboring
     * component.
     * </p>
     *
     * <p>
     * Components of type {@link OutOfBoundsTile} are ignored in this calculation.
     * </p>
     *
     * @return The total number of exposed connectors in the ship.
     * @throws IndexOutOfBoundsException If an invalid coordinate is encountered
     *                                   during the computation.
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

    public boolean hasExposedConnectorAtDirection(int row, int column, Direction direction) {
        int tileRow = toTileMatrixCoord(Optional.of(row), Optional.empty()).get(0).get();
        int tileCol = toTileMatrixCoord(Optional.empty(), Optional.of(column)).get(1).get();
        int i = direction.ordinal();
    
        Optional<ComponentTile> maybeComponent = ship.getComponent(tileRow, tileCol);
    
        if (maybeComponent.isEmpty()) return false;
    
        ComponentTile component = maybeComponent.get();
    
        if (component instanceof OutOfBoundsTile) return false;
    
        Optional<ComponentTile> neighbor = ship.getNeighbourComponents(tileRow, column).get(i);
        if (neighbor.isEmpty() || neighbor.get() instanceof OutOfBoundsTile) {
            TileEdge edge = component.getTileEdges().get(i);
            return TileEdge.isAConnector(edge);
        }
        return false;
    }

    /**
     * Counts the total number of human crewmates aboard the ship.
     *
     * <p>
     * This method iterates through all {@link CentralCabin} and {@link CabinModule}
     * components
     * to count the number of human crewmates present. A crewmate is considered
     * human if
     * they do not require life support.
     * </p>
     *
     * <p>
     * The method first collects all crewmates in {@link CentralCabin} components,
     * as they are
     * exclusively human. Then, it checks each crewmate in {@link CabinModule}
     * components and
     * includes only those that do not require life support.
     * </p>
     *
     * @return The total number of human crewmates aboard the ship.
     */
    public int countHumans() {
        Set<List<Integer>> centralCabinsCoords = this.getAllComponentsPositionOfType(CentralCabin.class);
        Set<List<Integer>> cabinCoords = this.getAllComponentsPositionOfType(CabinModule.class);
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

    public boolean hasAlien(AlienType type) {
        if (Optional.ofNullable(this.hasAlien.get(type)).isPresent() && this.hasAlien.get(type)) {
            return true;
        }
        return false;
    }

    /**
     * Counts the total number of crewmates aboard the ship.
     *
     * <p>
     * This method iterates through all {@link CentralCabin} and {@link CabinModule}
     * components
     * to count the number of crewmates present. Crewmates can be either human or
     * alien.
     * </p>
     *
     * <p>
     * The method first collects all crewmates from {@link CentralCabin} components,
     * then
     * continues by counting those in {@link CabinModule} components.
     * </p>
     *
     * @return The total number of crewmates aboard the ship.
     */
    public int countCrewmates() {
        Set<List<Integer>> centralCabinsCoords = this.getAllComponentsPositionOfType(CentralCabin.class);
        Set<List<Integer>> cabinCoords = this.getAllComponentsPositionOfType(CabinModule.class);
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

    /**
     * Calculates the total firepower of the ship.
     *
     * <p>This method computes the total firepower by summing the base firepower of all 
     * {@link SingleCannon} components present on the ship.</p>
     *
     * <h3>Firepower Calculation:</h3>
     * <ul>
     *     <li>Retrieves all {@link SingleCannon} components aboard the ship.</li>
     *     <li>Sum their individual firepower values.</li>
     *     <li>If a {@link AlienType#PURPLEALIEN} is present, an additional bonus of +2 firepower is applied.</li>
     * </ul>
     *
     * <p>Example of usage:</p>
     * <pre>
     *     double totalFirePower = shipManager.calculateFirePower();
     *     System.out.println("Total firepower: " + totalFirePower);
     * </pre>
     *
     * @return The total firepower of the ship. Returns {@code 0.0} if no cannons are present.
     */
    public double calculateFirePower() {
        Set<List<Integer>> singleCannonCoords = this.getAllComponentsPositionOfType(SingleCannon.class);
        double firePower = 0.0;

        for (List<Integer> coord : singleCannonCoords) {
            SingleCannon cannon = (SingleCannon) this.getComponent(coord.get(0), coord.get(1)).get();
            firePower += cannon.getFirePower();
        }

        if (firePower > 0.0 && this.hasAlien(AlienType.PURPLEALIEN)) {
            firePower += 2;
        }

        return firePower;
    }

    /**
     * Calculates the total engine power of the ship.
     *
     * <p>This method computes the total engine power by summing the power values of all
     * {@link SingleEngine} components present on the ship.</p>
     *
     * <h3>Engine Power Calculation:</h3>
     * <ul>
     *     <li>Retrieves all {@link SingleEngine} components aboard the ship.</li>
     *     <li>Summates their individual engine power values.</li>
     *     <li>If a {@link AlienType#BROWNALIEN} is present, an additional bonus of +2 engine power is applied.</li>
     * </ul>
     *
     * <p>Example of usage:</p>
     * <pre>
     *     int totalEnginePower = shipManager.calculateEnginePower();
     *     System.out.println("Total engine power: " + totalEnginePower);
     * </pre>
     *
     * @return The total engine power of the ship. Returns {@code 0} if no engines are present.
     */
    public int calculateEnginePower() {
        Set<List<Integer>> singleEngineCoords = this.getAllComponentsPositionOfType(SingleEngine.class);
        int enginePower = 0;

        for (List<Integer> coord : singleEngineCoords) {
            SingleEngine engine = (SingleEngine) this.getComponent(coord.get(0), coord.get(1)).get();

            enginePower += engine.getEnginePower();
        }

        if (enginePower > 0.0 && this.hasAlien(AlienType.BROWNALIEN)) {
            enginePower += 2;
        }

        return enginePower;
    }

    /**
     * Activates a set of components using specified battery components.
     *
     * <p>This method allows activating multiple {@link EnergyConsumer} components by consuming energy from 
     * associated {@link BatteryComponent} modules. Each activation is represented by a mapping between 
     * a component's position and the corresponding battery's position.</p>
     *
     * <h3>Activation Process:</h3>
     * <ul>
     *     <li>Iterates over the provided mapping of component coordinates to battery coordinates.</li>
     *     <li>Verifies that both the target component and the battery exist at their respective positions.</li>
     *     <li>Ensures that the target component implements {@link EnergyConsumer}.</li>
     *     <li>Activates the component and accumulates the resulting power.</li>
     *     <li>Consumes energy from the specified {@link BatteryComponent}.</li>
     * </ul>
     *
     * <p>Example of usage:</p>
     * <pre>
     *     HashMap<List<Integer>, List<Integer>> activations = new HashMap<>();
     *     activations.put(List.of(2, 3), List.of(1, 4)); // Activate component at (2,3) using battery at (1,4)
     *
     *     double totalPower = shipManager.activateComponent(activations);
     *     System.out.println("Total activated power: " + totalPower);
     * </pre>
     *
     * @param componentAndBatteries A mapping between the coordinates of components to be activated 
     *                              and the coordinates of their corresponding battery components.
     * @return The total power generated by all activated components.
     * @throws IllegalComponentPositionException If any of the specified positions do not contain valid components.
     * @throws InvalidActionException If any component is not an {@link EnergyConsumer}, 
     *                                if the battery component is invalid, or if activation fails.
     * @throws IllegalComponentPositionException If any of the specified positions does not contain a valid component.
     */
    public double activateComponent(HashMap<List<Integer>, List<Integer>> componentAndBatteries) throws IndexOutOfBoundsException, InvalidActionException, IllegalComponentPositionException {
        double power = 0;

        for (List<Integer> componentCoord : componentAndBatteries.keySet()) {
            List<Integer> batteryCoord = componentAndBatteries.get(componentCoord);

            //if one of the specified coordinates is empty
            ComponentTile component = this.getComponent(componentCoord.get(0), componentCoord.get(1))
                .orElseThrow(() -> new IllegalComponentPositionException(
                    "The component at [" + componentCoord.get(0) + "][" + componentCoord.get(1) + "] is empty"
                ));
            ComponentTile batteryComponent = this.getComponent(batteryCoord.get(0), batteryCoord.get(1))
                .orElseThrow(() -> new IllegalComponentPositionException(
                    "The component at [" + batteryCoord.get(0) + "][" + batteryCoord.get(1) + "] is empty"
                ));

            //if the specified component is not activable
            if (component instanceof EnergyConsumer) {
                EnergyConsumer consumer = (EnergyConsumer) component;
                power += consumer.activate();
            } else {
                throw new InvalidActionException("The component can't use a battery");
            }
            //if the specified component is a battery component
            if (batteryComponent instanceof BatteryComponent) {
                this.removeBattery(batteryCoord.get(0), batteryCoord.get(1));
            } else {
                throw new InvalidActionException("The component is not a battery");
            }
        }

        return power;
    }

    /**
     * Calculates the total firepower of the ship, including activated double cannons.
     *
     * <p>This method computes the base firepower from all {@link SingleCannon} components 
     * and adds the additional firepower generated by activating {@link DoubleCannon} components 
     * using batteries.</p>
     *
     * <h3>Firepower Calculation:</h3>
     * <ul>
     *     <li>Retrieves the base firepower from existing {@link SingleCannon} components.</li>
     *     <li>Checks if any firepower is present before activation.</li>
     *     <li>Activates {@link DoubleCannon} components using the provided battery mappings.</li>
     *     <li>If no cannons were active before activation, but at least one was activated, 
     *         and the ship has a {@link AlienType#PURPLEALIEN}, an additional +2 firepower bonus is applied.</li>
     * </ul>
     *
     * <h3>Example of usage:</h3>
     * <pre>
     *     HashMap<List<Integer>, List<Integer>> activations = new HashMap<>();
     *     activations.put(List.of(2, 3), List.of(1, 4)); // Activate double cannon at (2,3) using battery at (1,4)
     *
     *     double totalFirepower = shipManager.calculateFirePower(activations);
     *     System.out.println("Total firepower: " + totalFirepower);
     * </pre>
     *
     * @param doubleCannonsAndBatteries A mapping between the coordinates of {@link DoubleCannon} components 
     *                                  and the coordinates of their corresponding battery components.
     * @return The total firepower of the ship, including activated cannons and any alien bonuses.
     * @throws IndexOutOfBoundsException If any of the specified coordinates are out of bounds.
     * @throws InvalidActionException If any of the components cannot be activated 
     *                                or if an invalid battery is provided.
     * @throws IllegalComponentPositionException If any of the specified positions do not contain valid components.
    */
    public double calculateFirePower(HashMap<List<Integer>, List<Integer>> doubleCannonsAndBatteries) throws IndexOutOfBoundsException, InvalidActionException, IllegalComponentPositionException {
        boolean activedAliens = false;
        double firePower = this.calculateFirePower();

        if (firePower > 0.0) {
            activedAliens = true;
        }

        firePower += this.activateComponent(doubleCannonsAndBatteries);

        if (!activedAliens && firePower > 0 && this.hasAlien(AlienType.PURPLEALIEN)) {
            firePower += 2;
        }

        return firePower;
    }

    /**
     * Calculates the total engine power of the ship, including activated double engines.
     *
     * <p>This method computes the base engine power from all {@link SingleEngine} components 
     * and adds the additional power generated by activating {@link DoubleEngine} components 
     * using batteries.</p>
     *
     * <h3>Engine Power Calculation:</h3>
     * <ul>
     *     <li>Retrieves the base engine power from existing {@link SingleEngine} components.</li>
     *     <li>Checks if any engine power is present before activation.</li>
     *     <li>Activates {@link DoubleEngine} components using the provided battery mappings.</li>
     *     <li>If no engines were active before activation, but at least one was activated, 
     *         and the ship has a {@link AlienType#BROWNALIEN}, an additional +2 engine power bonus is applied.</li>
     * </ul>
     *
     * <h3>Example of usage:</h3>
     * <pre>
     *     HashMap<List<Integer>, List<Integer>> activations = new HashMap<>();
     *     activations.put(List.of(3, 2), List.of(2, 3)); // Activate double engine at (3,2) using battery at (2,3)
     *
     *     int totalEnginePower = shipManager.calculateEnginePower(activations);
     *     System.out.println("Total engine power: " + totalEnginePower);
     * </pre>
     *
     * @param doubleEnginesAndBatteries A mapping between the coordinates of {@link DoubleEngine} components 
     *                                  and the coordinates of their corresponding battery components.
     * @return The total engine power of the ship, including activated engines and any alien bonuses.
     * @throws IndexOutOfBoundsException If any of the specified coordinates are out of bounds.
     * @throws InvalidActionException If any of the components cannot be activated 
     *                                or if an invalid battery is provided.
     * @throws IllegalComponentPositionException If any of the specified positions do not contain valid components.
     */
    public int calculateEnginePower(HashMap<List<Integer>, List<Integer>> doubleEnginesAndBatteries) throws IndexOutOfBoundsException, InvalidActionException, IllegalComponentPositionException {
        boolean activedAliens = false;
        int enginePower = this.calculateEnginePower();

        if (enginePower > 0.0) {
            activedAliens = true;
        }

        enginePower += this.activateComponent(doubleEnginesAndBatteries);

        if (!activedAliens && enginePower > 0 && this.hasAlien(AlienType.BROWNALIEN)) {
            enginePower += 2;
        }

        return enginePower;
    }

    /**
     * ?TESTING ONLY
     */
    public void printBoard() {
        System.out.print("    ");
        for (int i = STARTOFBOARDCOLUMNS; i < STARTOFBOARDCOLUMNS + COLUMNS; i++) {
            System.out.print(i + "  ");
        }
        System.out.println();
        for (int i = 0; i < ROWS; i++) {
            System.out.print(this.toBoardCoord(Optional.of(i), Optional.empty()).get(0).get() + "  ");
            for (int j = 0; j < COLUMNS; j++) {
                Optional<ComponentTile> component = ship.getComponent(i, j);

                if (component.isEmpty()) {
                    System.out.print("[ ]");
                }else if (component.get().getClass().equals(OutOfBoundsTile.class)) {
                    System.out.print("   ");
                } else {
                    System.out.print("[" + mapComponentToLetter(component.get()) + "]");
                }
            }
            System.out.println();
        }
    }
    private char mapComponentToLetter(ComponentTile component) {
        if (component.getClass().equals(CabinModule.class)) {
            return 'm';
        }
        if (component.getClass().equals(SingleCannon.class)) {
            return 'c';
        }
        if (component.getClass().equals(DoubleCannon.class)) {
            return 'C';
        }
        if (component.getClass().equals(CargoHold.class)) {
            return 'h';
        }
        if (component.getClass().equals(SpecialCargoHold.class)) {
            return 'H';
        }
        if (component.getClass().equals(LifeSupport.class)) {
            return 'l';
        }
        if (component.getClass().equals(Shield.class)) {
            return 's';
        }
        if (component.getClass().equals(BatteryComponent.class)) {
            return 'b';
        }
        if (component.getClass().equals(SingleEngine.class)) {
            return 'e';
        }
        if (component.getClass().equals(DoubleEngine.class)) {
            return 'E';
        }
        return 'x';
    }
}
