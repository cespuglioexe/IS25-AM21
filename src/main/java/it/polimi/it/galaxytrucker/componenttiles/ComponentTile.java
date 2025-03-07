package it.polimi.it.galaxytrucker.componenttiles;

import java.util.ArrayList;
import java.util.List;

public abstract class ComponentTile {
    private final List<TileEdge> tileEdges;
    private int rotation;

    TileEdge test = TileEdge.SINGLE;


    public ComponentTile(TileEdge top, TileEdge right, TileEdge bottom, TileEdge left) {
        rotation = 0;
        tileEdges = List.of(right, top, left, bottom);
    }

    // each tile can have 4 possible rotation positions
    // %4 keeps the value within bounds
    public void rotate () {
        rotation++;
        rotation %= 4;
    }

    public int getRotation() {
        return rotation;
    }

    /*
    *   Returns the list of edges for this component
    */
    public List<TileEdge> getTileEdges() {
        return tileEdges;
    }
}
