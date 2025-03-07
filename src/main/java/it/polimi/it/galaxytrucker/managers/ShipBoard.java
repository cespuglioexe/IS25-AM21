package it.polimi.it.galaxytrucker.managers;

import it.polimi.it.galaxytrucker.componenttiles.ComponentTile;
import it.polimi.it.galaxytrucker.componenttiles.EmptyTile;
import it.polimi.it.galaxytrucker.exceptions.IllegalComponentPositionException;
import it.polimi.it.galaxytrucker.componenttiles.TileEdge;

import java.util.ArrayList;
import java.util.Set;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

//TODO: metodi privati e aggiungere la cabina centrale
public class ShipBoard {
    private List<List<Optional<ComponentTile>>> tileMatrix;
    private Map<Class<? extends ComponentTile>, Set<List<Integer>>> componentTilesPosition;

    public ShipBoard() {
        final int COLUMNS = 7;
        final int ROWS = 5;

        this.tileMatrix = IntStream.range(0, ROWS)
            .mapToObj(_ -> IntStream.range(0, COLUMNS)
                .mapToObj(_ -> Optional.<ComponentTile>empty())
                .collect(Collectors.toList())
            )
            .collect(Collectors.toList());

        this.componentTilesPosition = new HashMap<>();
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
     * Sets the boundaries of the ship by marking positions outside the valid ship structure 
     * with {@code EmptyTile} components based on the specified level.
     *
     * @param level The level of the ship (e.g., 1 or 2).
     */
    public void setShipBounds(int level) {
        for (int i = 0; i < this.tileMatrix.size(); i++) {
            for (int j = 0; j < this.tileMatrix.get(i).size(); j++) {
                if (isOutsideTheShip(level, i, j)) {
                    try {
                        this.addComponentTile(i, j, new EmptyTile());
                    } catch (IllegalComponentPositionException e) {
                        e.printStackTrace();
                    }
                }
            }
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
     * Adds a component to the specified position on the board.
     *
     * @param row       The row index where the component should be placed.
     * @param column    The column index where the component should be placed.
     * @param component The {@code ComponentTile} to be added.
     * @throws IllegalComponentPositionException If a component is already present at the specified position or if the specified position is outside the ship.
     */
    public void addComponentTile(int row, int column, ComponentTile component) throws IllegalComponentPositionException {
        if (this.getComponent(row, column).isPresent() && this.getComponent(row, column).get().getClass().equals(EmptyTile.class)) {
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
     * @throws IllegalComponentPositionException If there is no component at the specified position.
     */
    public void removeComponentTile(int row, int column) throws IllegalComponentPositionException {
        if (this.getComponent(row, column).isEmpty() || 
            (this.getComponent(row, column).isPresent() && this.getComponent(row, column).get().getClass().equals(EmptyTile.class))) {
            throw new IllegalComponentPositionException("There is no element here");
        }

        Class<? extends ComponentTile> componentType = this.getComponent(row, column).get().getClass();

        Set<List<Integer>> positions = this.componentTilesPosition.get(componentType);

        this.tileMatrix.get(row).set(column, Optional.<ComponentTile>empty());

        positions.removeIf(coord -> coord.equals(List.of(row, column)));
        if (positions.isEmpty()) {
            this.componentTilesPosition.remove(componentType);
        }
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
            (component.isPresent() && component.get().getClass().equals(EmptyTile.class))) {
            return 0;
        }
        List<TileEdge> edges = component.get().getTileEdges();

        List<Optional<ComponentTile>> neighbours = this.getNeighbourComponents(row, column);

        int exposedConnectors = 0;

        for (int i = 0; i < edges.size(); i++) {
            if (neighbours.get(i).isEmpty() || neighbours.get(i).get().getClass().equals(EmptyTile.class)) {
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
                    if (component.getClass().equals(EmptyTile.class)) {
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
