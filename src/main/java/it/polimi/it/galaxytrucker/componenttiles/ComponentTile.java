package it.polimi.it.galaxytrucker.componenttiles;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * This abstract class represents a component tile.
 * Component tiles are the basic building blocks of a spaceship, each with four edges
 * that can be rotated to connect with other tiles.
 *
 * @author Giacomo Amaducci
 * @version 1.2
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "componentType")
@JsonSubTypes({
        @JsonSubTypes.Type(value = BatteryComponent.class,  name = "BatteryComponent"),
        @JsonSubTypes.Type(value = CabinModule.class,       name = "CabinModule"),
        @JsonSubTypes.Type(value = CargoHold.class,         name = "CargoHold"),
        @JsonSubTypes.Type(value = CentralCabin.class,      name = "CentralCabin"),
        @JsonSubTypes.Type(value = DoubleCannon.class,      name = "DoubleCannon"),
        @JsonSubTypes.Type(value = DoubleEngine.class,      name = "DoubleEngine"),
        @JsonSubTypes.Type(value = LifeSupport.class,       name = "LifeSupport"),
        @JsonSubTypes.Type(value = Shield.class,            name = "Shield"),
        @JsonSubTypes.Type(value = SingleCannon.class,      name = "SingleCannon"),
        @JsonSubTypes.Type(value = SingleEngine.class,      name = "SingleEngine"),
        @JsonSubTypes.Type(value = SpecialCargoHold.class,  name = "SpecialCargoHold"),
        @JsonSubTypes.Type(value = StructuralModule.class,  name = "StructuralModule")
})
public abstract class ComponentTile {
    /** The list of edges for this tile in the order: top, right, bottom, left. Must contain exactly four elements. */
    private final List<TileEdge> tileEdges;

    /** The current rotation of the tile (0-3, where 0 is the initial orientation). */
    private int rotation;

    /**
     * Constructs a new component tile with the specified edges.
     * Initial rotation is set to 0.
     *
     * @param edges the edges for the Component Tile, must contain exactly four elements.
     */
    public ComponentTile(List<TileEdge> edges) {
        rotation = 0;
        tileEdges = edges;
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
     * @return an {@code int} between 0 and 3 which indicates the current rotation.
     */
    public int getRotation() {
        return rotation;
    }

    /**
     * Returns the 4 edges of the tile, accounting for the tile's rotation.
     * <p>
     * The edges are reordered based on the rotation value by splitting the list
     * at the index {@code tileEdges.size() - rotation} and swapping the two parts.
     *
     * @return a {@code List<TileEdge>} representing the edges in their rotated order.
     */
    public List<TileEdge> getTileEdges() {
        List<TileEdge> rotatedEdges = new ArrayList<>(4);

        rotatedEdges.addAll(tileEdges.subList(4 - rotation, 4));
        rotatedEdges.addAll(tileEdges.subList(0, 4 - rotation));

        return rotatedEdges;
    }
}
