package it.polimi.it.galaxytrucker.model.componenttiles;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TileData implements Serializable {
    private int rotation;
    private String type;

    private TileEdge top;
    private TileEdge right;
    private TileEdge bottom;
    private TileEdge left;

    @JsonCreator
    public TileData (
            @JsonProperty("rotation") int rotation,
            @JsonProperty("type") String type,
            @JsonProperty("top") TileEdge top,
            @JsonProperty("right") TileEdge right,
            @JsonProperty("bottom") TileEdge bottom,
            @JsonProperty("left") TileEdge left) {
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
        if (componentTile != null) {
            return new TileData(
                    componentTile.getRotation(),
                    componentTile.getClass().getSimpleName(),
                    componentTile.getTileEdges().get(0),
                    componentTile.getTileEdges().get(1),
                    componentTile.getTileEdges().get(2),
                    componentTile.getTileEdges().get(3)
            );
        }
        return null;
    }

    public static List<List<TileData>> createTileDataShipFromComponentTileShip(List<List<ComponentTile>> ship) {
        List<List<TileData>> result = new ArrayList<>();

        for (List<ComponentTile> row : ship) {
            List<TileData> rowData = new ArrayList<>();
            for (ComponentTile componentTile : row) {
                rowData.add(TileData.createTileDataFromComponentTile(componentTile));
            }
            result.add(rowData);
        }
        return result;
    }

}
