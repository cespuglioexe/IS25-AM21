package it.polimi.it.galaxytrucker.model.componenttiles;

import java.io.Serializable;

public class TileData implements Serializable {
    private int rotation;
    private String type;

    private TileEdge top;
    private TileEdge right;
    private TileEdge bottom;
    private TileEdge left;

    public TileData (int rotation, String type, TileEdge top, TileEdge right, TileEdge bottom, TileEdge left) {
        this.rotation = rotation;
        this.type = type;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
        this.left = left;
    }

    public int getRotation() {
        return rotation;
    }
    public void setRotation(int rotation) { this.rotation = rotation; }

    public String getType() {
        return type;
    }

    public TileEdge getTop() {
        return top;
    }

    public TileEdge getRight() {
        return right;
    }

    public TileEdge getBottom() {
        return bottom;
    }

    public TileEdge getLeft() {
        return left;
    }

    public static TileData createTileDataFromComponentTile (ComponentTile componentTile) {
        return new TileData(
                componentTile.getRotation(),
                componentTile.getClass().getSimpleName(),
                componentTile.getTileEdges().get(0),
                componentTile.getTileEdges().get(1),
                componentTile.getTileEdges().get(2),
                componentTile.getTileEdges().get(3)
        );
    }
}
