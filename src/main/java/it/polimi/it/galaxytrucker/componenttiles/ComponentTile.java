package it.polimi.it.galaxytrucker.componenttiles;

import java.util.ArrayList;
import java.util.List;

/**
 * This abstract class represents a component tile.
 * Component tiles are the basic building blocks of a spaceship, each with four edges
 * that can be rotated to connect with other tiles.
 *
 * @author Giacomo Amaducci
 * @version 1.0
 */
public abstract class ComponentTile {
    /** The list of edges for this tile in the order: top, right, bottom, left */
    private final List<TileEdge> tileEdges;

    /** The current rotation of the tile (0-3, where 0 is the initial orientation) */
    private int rotation;

    /**
     * Constructs a new component tile with the specified edges.
     * Initial rotation is set to 0.
     *
     * @param top the type of the top edge of this tile
     * @param right the type of the right edge of this tile
     * @param bottom the type of the bottom edge of this tile
     * @param left the type of the left edge of this tile
     */
    public ComponentTile(TileEdge top, TileEdge right, TileEdge bottom, TileEdge left) {
        rotation = 0;
        tileEdges = List.of(top, right, bottom, left);
    }

    /**
     * Rotates the tile clockwise by 90 degrees.
     * The rotation is limited to 4 possible positions (0, 1, 2, 3) by using modulo 4.
     */
    public void rotate() {
        rotation++;
        rotation %= 4;
    }

    /**
     * Returns the current rotation of the tile.
     *
     * @return an {@code int} between 0 and 3 which indicates the current rotation
     */
    public int getRotation() {
        return rotation;
    }

    /**
     * Returns the 4 edges of the tile, accounting for the tile's rotation.
     * <p>
     * The rotation is achieved by splitting the list in two based on the rotation:
     * (rotation = 1): [TOP, RIGHT, BOTTOM, LEFT] --> [TOP, RIGHT, BOTTOM], [LEFT]
     * and then swapping the two sub-lists into a new list:
     * [TOP, RIGHT, BOTTOM], [LEFT] --> [LEFT, TOP, RIGHT, BOTTOM]
     *
     * @return a {@code List<TileEdge>} which is made by swapping the two sub-lists obtained by splitting
     *         {@code tileEdges} at the position {@code tileEdges.size() - rotation}
     */
    public List<TileEdge> getTileEdges() {
        List<TileEdge> rotatedEdges = new ArrayList<>(4);

        rotatedEdges.addAll(tileEdges.subList(4 - rotation, 4));
        rotatedEdges.addAll(tileEdges.subList(0, 4 - rotation));

        return rotatedEdges;
    }
}