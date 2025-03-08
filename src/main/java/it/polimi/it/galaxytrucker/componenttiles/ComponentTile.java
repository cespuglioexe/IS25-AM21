package it.polimi.it.galaxytrucker.componenttiles;

import java.util.ArrayList;
import java.util.List;

public abstract class ComponentTile {
    private final List<TileEdge> tileEdges;
    private int rotation;

    TileEdge test = TileEdge.SINGLE;

    public ComponentTile(TileEdge top, TileEdge right, TileEdge bottom, TileEdge left) {
        rotation = 0;
        tileEdges = List.of(top, right, bottom, left);
    }

    /*
    *   Rotates the tile by 90˚, limiting the object to 4 possible rotations by calculating % 4
    */
    public void rotate () {
        rotation++;
        rotation %= 4;
    }

    /*
    *   Returns the current rotation of the tile.
    *
    *   @return An {@code int} between 0 and 4 which indicates the current rotation.
    */
    public int getRotation() {
        return rotation;
    }

    /*
    *   Returns the 4 edges of the tile, accounting for the tile's rotation.
    *   The rotation is achieved by splitting the list in two based on the rotation:
    *       (rotation = 1): [TOP, RIGHT, BOTTOM, LEFT] --> [TOP, RIGHT, BOTTOM], [LEFT]
    *   and then swapping the two sub-lists into a new list:
    *       [TOP, RIGHT, BOTTOM], [LEFT] --> [LEFT, TOP, RIGHT, BOTTOM]
    *
    *   @return A {@code List<TileEdge>} which is made by swapping the two sub-lists obtained by splitting
    *           {@code tileEdges} at the position {@code tileEdges.size() - rotation}
    */
    public List<TileEdge> getTileEdges() {
        List<TileEdge> rotatedEdges = new ArrayList<>(4);

        rotatedEdges.addAll(tileEdges.subList(4 - rotation, 4));
        rotatedEdges.addAll(tileEdges.subList(0, 4 - rotation));

        return rotatedEdges;
    }
}
