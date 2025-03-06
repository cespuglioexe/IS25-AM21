package it.polimi.it.galaxytrucker.componenttiles;

import java.util.ArrayList;
import java.util.List;

public abstract class ComponentTile {
    private final List<TileEdge> tileEdges;
    private int rotation;

    public ComponentTile(TileEdge edge1, TileEdge edge2, TileEdge edge3, TileEdge edge4) {
        rotation = 0;
        tileEdges = List.of(edge1, edge2, edge3, edge4); // Immutable list
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

    public List<TileEdge> getTileEdges() {
        return tileEdges;
    }
}
