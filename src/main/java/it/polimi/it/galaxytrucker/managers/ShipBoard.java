package it.polimi.it.galaxytrucker.managers;

import it.polimi.it.galaxytrucker.componenttiles.*;
import it.polimi.it.galaxytrucker.exceptions.IllegalComponentPositionException;

import java.util.ArrayList;
import java.util.Set;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * The {@code ShipBoard} class represents the grid-based structure of a spaceship 
 * in the Galaxy Trucker game. It manages the placement, retrieval, and removal 
 * of components on the ship, ensuring structural integrity and connectivity.
 * 
 * <p>This class includes functionalities such as:</p>
 * <ul>
 *     <li>Defining ship boundaries based on different levels.</li>
 *     <li>Adding and removing components.</li>
 *     <li>Identifying and managing connected components.</li>
 *     <li>Checking exposed connectors for connectivity validation.</li>
 * </ul>
 * 
 * <p>The ship is represented as a matrix ({@code tileMatrix}) where each cell 
 * contains an {@code Optional<ComponentTile>}, indicating the presence or absence 
 * of a component.</p>
 * 
 * <p>Example of usage:</p>
 * <pre>
 *     ShipBoard ship = new ShipBoard();
 *     ship.setShipBounds(1); // Initialize ship boundaries for level 1
 *     ship.addComponentTile(2, 3, new EngineModule());
 * </pre>
 * <p>Example of usage:</p>
 * <pre>
 *     ShipBoard ship = new ShipBoard(1); // Create and initialize ship boundaries for level 1
 *     ship.addComponentTile(2, 3, new EngineModule());
 * </pre>
 * 
 * @author Stefano Carletto
 * @version 1.2
 */
public class ShipBoard {
    private List<List<Optional<ComponentTile>>> tileMatrix;
    private Map<Class<? extends ComponentTile>, Set<List<Integer>>> componentTilesPosition;

    /**
     * Initializes a new {@code ShipBoard} instance, representing the ship's component grid.
     * The board is structured to accommodate ship components and maintain their positions.
     *
     * <p>Upon creation, the board is empty, and no components are placed.</p>
     */
    public ShipBoard() {
        final int ROWS = ShipManager.getRows();
        final int COLUMNS = ShipManager.getColumns();

        this.tileMatrix = IntStream.range(0, ROWS)
            .mapToObj(_ -> IntStream.range(0, COLUMNS)
                .mapToObj(_ -> Optional.<ComponentTile>empty())
                .collect(Collectors.toList())
            )
            .collect(Collectors.toList());

        this.componentTilesPosition = new HashMap<>();
    }

    /**
     * Initializes a new {@code ShipBoard} instance for a specified ship level.
     * The board is structured to accommodate ship components and automatically 
     * applies the appropriate structural boundaries based on the given level.
     *
     * <p>Upon creation, the board is empty except for predefined structural constraints {@link OutOfBoundsTile} and the default {@link CentralCabin}.</p>
     *
     * @param level The level of the ship (e.g., 1 or 2), determining its valid structure.
     */
    public ShipBoard(int level) {
        final int COLUMNS = 7;
        final int ROWS = 5;

        this.tileMatrix = IntStream.range(0, ROWS)
            .mapToObj(_ -> IntStream.range(0, COLUMNS)
                .mapToObj(_ -> Optional.<ComponentTile>empty())
                .collect(Collectors.toList())
            )
            .collect(Collectors.toList());

        this.componentTilesPosition = new HashMap<>();
        this.setShipBounds(level);
    }

    /**
     * Determines whether a given position (row, column) is outside the valid ship structure
     * based on the specified level.
     *
     * @param level  The level of the ship (e.g., 1 or 2).
     * @param row    The row index of the position to check.
     * @param column The column index of the position to check.
     * @return {@code true} if the position is outside the ship structure, otherwise {@code false}.
     */
    private boolean isOutsideTheShip(int level, int row, int column) {
        switch (level) {
            case 1:
                return switch (row) {
                    case 0 -> column != 3;
                    case 1 -> column != 2 && column != 3 && column != 4;
                    case 2 -> column < 1 || column > 5;
                    case 3 -> column < 1 || column > 5;
                    case 4 -> column < 1 || column > 5 || column == 3;
                    default -> false;
                };
            case 2:
                return switch (row) {
                    case 0 -> column != 2 && column != 4;
                    case 1 -> column < 1 || column > 5;
                    case 4 -> column == 3;
                    default -> false;
                };
            default:
                return false;
        }
    }

    /**
     * Retrieves the entire board as a matrix of component tiles.
     *
     * @return A {@code List<List<Optional<ComponentTile>>>} representing the board.
     *         Each inner list corresponds to a row, and each {@code Optional<ComponentTile>} 
     *         represents a cell that may contain a component or be empty.
     */
    public List<List<Optional<ComponentTile>>> getBoard() {
        return this.tileMatrix;
    }

    /**
     * Retrieves the component located at the specified position on the board.
     *
     * @param row    The row index of the component.
     * @param column The column index of the component.
     * @return An {@code Optional<ComponentTile>} containing the component if present,
     *         or {@code Optional.empty()} if the cell is empty.
     * @throws IndexOutOfBoundsException If {@code row} or {@code column} are out of bounds.
     */
    public Optional<ComponentTile> getComponent(int row, int column) {
        return this.tileMatrix.get(row).get(column);
    }

    /**
     * Retrieves all unique component types currently placed on the ship.
     *
     * @return A set of classes representing the different types of components present on the board.
     */
    public Set<Class<? extends ComponentTile>> getAllComponentsTypes() {
        return this.componentTilesPosition.keySet();
    }

    /**
     * Retrieves all positions of components of the specified type on the board.
     *
     * @param componentType The class type of the component to search for.
     * @return A {@code Set<List<Integer>>} containing the coordinates of all components 
     *         of the given type, where each coordinate is represented as {@code List<Integer> [row, column]}.
     *         Returns {@code null} if the component type is not found.
     */
    public Set<List<Integer>> getAllComponentsPositionOfType(Class<? extends ComponentTile> componentType) {
        return this.componentTilesPosition.get(componentType);
    }

    /**
     * Retrieves the neighboring components of the specified position on the board.
     * The neighbors are returned in the following order: 
     * <ul>
     *   <li>Upper component</li>
     *   <li>Right component</li>
     *   <li>Lower component</li>
     *   <li>Left component</li>
     * </ul>
     *
     * @param row    The row index of the target component.
     * @param column The column index of the target component.
     * @return A {@code List<Optional<ComponentTile>>} containing the neighboring components.
     *         Each element in the list is {@code Optional.empty()} if the corresponding neighbor does not exist.
     */
    public List<Optional<ComponentTile>> getNeighbourComponents(int row, int column) {
        List<Optional<ComponentTile>> neighbours = new ArrayList<>();
        //top component
        neighbours.add(row - 1 < 0 ? Optional.<ComponentTile>empty() : this.getComponent(row - 1, column));
        //right component
        neighbours.add(column + 1 >= this.tileMatrix.get(row).size() ? Optional.<ComponentTile>empty() : this.getComponent(row, column + 1));
        //down component
        neighbours.add(row + 1 >= this.tileMatrix.size() ? Optional.<ComponentTile>empty() : this.getComponent(row + 1, column));
        //left component
        neighbours.add(column - 1 < 0 ? Optional.<ComponentTile>empty() : this.getComponent(row, column - 1));
        return neighbours;
    }

    /**
     * Recursively identifies and collects all connected components in a ship's grid,
     * starting from a specified position. This method performs a depth-first search (DFS) 
     * to explore all adjacent components that are part of the same connected structure.
     *
     * <p>The method stops under the following conditions:</p>
     * <ul>
     *     <li>If the specified position is outside the grid boundaries.</li>
     *     <li>If the component at the specified position is either empty or marked as an {@code OutOfBoundsTile},
     *         indicating an invalid or unoccupied position.</li>
     *     <li>If the component at the specified position has already been visited and added to the branch.</li>
     * </ul>
     *
     * <p>Once a valid component is found, it is added to the {@code branch} set, 
     * and the method recursively explores its four neighboring components (top, right, bottom, left)
     * to continue the search.</p>
     *
     * @param row    The row index of the component to start the search from.
     * @param column The column index of the component to start the search from.
     * @param branch A set that stores coordinate pairs {@code (row, column)} representing all 
     *               connected components that belong to the same structure.
     */
    public void getBranchOfComponent(int row, int column, Set<List<Integer>> branch) {
        //check boundaries of matrix
        if (row < 0 || row >= this.tileMatrix.size() || column < 0 || column >= this.tileMatrix.get(row).size()) {
            return;
        }
        //check if component is empty or outside the ship
        if (this.getComponent(row, column).isEmpty() || this.getComponent(row, column).get().getClass() == OutOfBoundsTile.class) {
            return;
        }
        //check if component is already added
        if (branch.contains(List.of(row, column))) {
            return;
        }

        //if all test passed, component is connected to the branch, thus added
        branch.add(List.of(row, column));

        //calls all 4 directions
        //top component
        getBranchOfComponent(row - 1, column, branch);
        //right component
        getBranchOfComponent(row, column + 1, branch);
        //down component
        getBranchOfComponent(row + 1, column, branch);
        //left component
        getBranchOfComponent(row, column - 1, branch);
    }

    /**
     * Identifies and groups all disconnected branches of connected components within the ship's grid.
     * A branch is defined as a set of connected components that form a continuous structure.
     *
     * <p>This method iterates through the grid and performs a depth-first search (DFS) to find 
     * all disconnected clusters of components. Each unique branch is stored as a set of coordinate 
     * pairs representing the connected components.</p>
     *
     * <p>The method follows these steps:</p>
     * <ul>
     *     <li>Iterate through all positions in the {@code tileMatrix}.</li>
     *     <li>Ignore empty tiles or tiles marked as {@code OutOfBoundsTile}, as they are not part of a branch.</li>
     *     <li>If a component has not been visited, perform a recursive search using 
     *         {@link #getBranchOfComponent(int, int, Set)} to collect all connected components.</li>
     *     <li>Add the discovered branch to the set of all branches.</li>
     * </ul>
     *
     * <p>Each branch is represented as a {@code Set<List<Integer>>}, where each list contains 
     * two integers: {@code (row, column)}.</p>
     *
     * @return A list of disconnected branches, where each branch is a set of coordinate pairs 
     *         representing connected components.
     */
    public List<Set<List<Integer>>> getDisconnectedBranches() {
        List<Set<List<Integer>>> branches = new ArrayList<>();
        Set<List<Integer>> visitedComponents = new HashSet<>();

        for (int i = 0; i < this.tileMatrix.size(); i++) {
            for (int j = 0; j < this.tileMatrix.get(i).size(); j++) {
                if (this.getComponent(i, j).isEmpty()) {
                    continue;
                }
                if (this.getComponent(i, j).get().getClass().equals(OutOfBoundsTile.class)) {
                    continue;
                }

                List<Integer> component = List.of(i, j);

                if (!visitedComponents.contains(component)) {
                    Set<List<Integer>> newBranch = new HashSet<>();

                    this.getBranchOfComponent(i, j, newBranch);
                    visitedComponents.addAll(newBranch);
                    branches.add(newBranch);
                }
            }
        }
        return branches;
    }

    /**
     * Sets the boundaries of the ship by marking positions outside the valid ship structure 
     * with {@code OutOfBoundsTile} components based on the specified level.
     * Also initializes the board placing a {@code CentralCabin} at the center of the ship
     *
     * @param level The level of the ship (e.g., 1 or 2).
     */
    public void setShipBounds(int level) {
        for (int i = 0; i < this.tileMatrix.size(); i++) {
            for (int j = 0; j < this.tileMatrix.get(i).size(); j++) {
                if (isOutsideTheShip(level, i, j)) {
                    try {
                        this.addComponentTile(i, j, new OutOfBoundsTile());
                    } catch (IllegalComponentPositionException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        final int CENTRALCABINROW = 2;
        final int CENTRALCABINCOLUMN = 3;
        
        try {
            this.addComponentTile(CENTRALCABINROW, CENTRALCABINCOLUMN, new CentralCabin(TileEdge.UNIVERSAL, TileEdge.UNIVERSAL, TileEdge.UNIVERSAL, TileEdge.UNIVERSAL));
        } catch (IllegalComponentPositionException e) {
            e.printStackTrace();
        }
    }

    /**
     * Adds a component to the specified position on the board.
     *
     * @param row       The row index where the component should be placed.
     * @param column    The column index where the component should be placed.
     * @param component The {@code ComponentTile} to be added.
     * @throws IndexOutOfBoundsException If the specified row or column is out of the ship's bounds.
     * @throws IllegalComponentPositionException If a component is already present at the specified position or if the specified position is outside the ship.
     */
    public void addComponentTile(int row, int column, ComponentTile component) throws IndexOutOfBoundsException, IllegalComponentPositionException {
        if (this.getComponent(row, column).isPresent() && this.getComponent(row, column).get().getClass().equals(OutOfBoundsTile.class)) {
            throw new IllegalComponentPositionException("You cannot place a component outside the ship");
        }
        if (this.getComponent(row, column).isPresent()) throw new IllegalComponentPositionException("You have already placed a component here");

        this.tileMatrix.get(row).set(column, Optional.of(component));
        this.componentTilesPosition.putIfAbsent(component.getClass(), new HashSet<>());
        this.componentTilesPosition.get(component.getClass()).add(List.of(row, column));
    }

    /**
     * Removes the component from the specified position on the board.
     *
     * @param row    The row index of the component to be removed.
     * @param column The column index of the component to be removed.
     * @return The removed component
     * @throws IndexOutOfBoundsException If the specified row or column is out of the ship's bounds.
     * @throws IllegalComponentPositionException If there is no component at the specified position.
     */
    public ComponentTile removeComponentTile(int row, int column) throws IndexOutOfBoundsException, IllegalComponentPositionException {
        if (this.getComponent(row, column).isEmpty() || 
            (this.getComponent(row, column).isPresent() && this.getComponent(row, column).get().getClass().equals(OutOfBoundsTile.class))) {
            throw new IllegalComponentPositionException("There is no element here");
        }

        ComponentTile component = this.getComponent(row, column).get();
        Class<? extends ComponentTile> componentType = component.getClass();

        Set<List<Integer>> positions = this.componentTilesPosition.get(componentType);

        this.tileMatrix.get(row).set(column, Optional.<ComponentTile>empty());

        positions.removeIf(coord -> coord.equals(List.of(row, column)));
        if (positions.isEmpty()) {
            this.componentTilesPosition.remove(componentType);
        }

        return component;
    }

    /**
     * Removes all components in a given branch from the ship's grid.
     * This method iterates through the provided set of coordinates and removes 
     * each corresponding component by calling {@link #removeComponentTile(int, int)}.
     *
     * <p>If any coordinate in the branch refers to an invalid position or an unremovable component, 
     * the method will throw an {@code IllegalComponentPositionException}.</p>
     *
     * @param branch A set of coordinate pairs {@code (row, column)} representing the components 
     *               to be removed.
     * @return A set containing all removed components
     * @throws IndexOutOfBoundsException If the specified row or column is out of the ship's bounds.
     * @throws IllegalComponentPositionException If the removal of a component at any given coordinate 
     *         is not allowed due to game rules or invalid positioning.
     */
    public Set<ComponentTile> removeBranch(Set<List<Integer>> branch) throws IllegalComponentPositionException {
        Set<ComponentTile> removedComponents = new HashSet<>();
        for (List<Integer> coord : branch) {
            ComponentTile removed = this.removeComponentTile(coord.get(0), coord.get(1));
            removedComponents.add(removed);
        }

        return removedComponents;
    }

    /**
     * Counts the number of exposed connectors for a given component at the specified position.
     * A connector is considered exposed if it is of type {@code SINGLE}, {@code DOUBLE}, or {@code UNIVERSAL}
     * and there is no adjacent component in that direction.
     *
     * @param row    The row index of the component.
     * @param column The column index of the component.
     * @return The number of exposed connectors for the component at the given position. If the position is empty or outside the board, returns 0
     */
    public int countExposedConnectors(int row, int column) {
        Optional<ComponentTile> component = this.getComponent(row, column);
        if (component.isEmpty() || 
            (component.isPresent() && component.get().getClass().equals(OutOfBoundsTile.class))) {
            return 0;
        }
        List<TileEdge> edges = component.get().getTileEdges();

        List<Optional<ComponentTile>> neighbours = this.getNeighbourComponents(row, column);

        int exposedConnectors = 0;

        for (int i = 0; i < edges.size(); i++) {
            if (neighbours.get(i).isEmpty() || neighbours.get(i).get().getClass().equals(OutOfBoundsTile.class)) {
                if (edges.get(i) == TileEdge.SINGLE || edges.get(i) == TileEdge.DOUBLE || edges.get(i) == TileEdge.UNIVERSAL) {
                    exposedConnectors++;
                }
            }
        }
        return exposedConnectors;
    }

    //? TESTING ONLY
    public void printBoard() {
        for (int i = 0; i < this.tileMatrix.size(); i++) {
            for (int j = 0; j < this.tileMatrix.get(i).size(); j++) {
                this.getComponent(i, j).ifPresentOrElse(component -> {
                    if (component.getClass().equals(OutOfBoundsTile.class)) {
                        System.out.print("   ");
                    } else {
                        System.out.print("[x]");
                    }
                }, () -> System.out.print("[ ]"));
            }
            System.out.println();
        }
        System.out.println();
    }

    //?TEST ONLY
    public void printBranch(int level, Set<List<Integer>> branch) {
        ShipBoard branchBoard = new ShipBoard();
        branchBoard.setShipBounds(level);
        try {
            branchBoard.removeComponentTile(2, 3);
        } catch (IllegalComponentPositionException e) {
            e.printStackTrace();
        }
        for (List<Integer> coord : branch) {
            try {
                branchBoard.addComponentTile(coord.get(0), coord.get(1), new SingleCannon(TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE));
            } catch (IllegalComponentPositionException e) {
                e.printStackTrace();
            }
        }

        for (int i = 0; i < branchBoard.getBoard().size(); i++) {
            for (int j = 0; j < branchBoard.getBoard().get(i).size(); j++) {
                branchBoard.getComponent(i, j).ifPresentOrElse(component -> {
                    if (component.getClass().equals(OutOfBoundsTile.class)) {
                        System.out.print("   ");
                    } else {
                        System.out.print("[x]");
                    }
                }, () -> System.out.print("[ ]"));
            }
            System.out.println();
        }
        System.out.println();
    }
}
