package it.polimi.it.galaxytrucker.model.componenttiles;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public record TileData(int rotation, String type, TileEdge top, TileEdge right, TileEdge bottom, TileEdge left,
                       String graphicPath) implements Serializable {
    @JsonCreator
    public TileData(
            @JsonProperty("rotation") int rotation,
            @JsonProperty("type") String type,
            @JsonProperty("top") TileEdge top,
            @JsonProperty("right") TileEdge right,
            @JsonProperty("bottom") TileEdge bottom,
            @JsonProperty("left") TileEdge left,
            @JsonProperty("graphicPath") String graphicPath) {
        this.rotation = rotation;
        this.type = type;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
        this.left = left;
        this.graphicPath = graphicPath;
    }

    /**
     * Converts a {@link ComponentTile} object to a {@link TileData} record object.
     *
     * @param componentTile The component to be converted.
     * @return The conversion of the given component.
     */
    public static TileData createTileDataFromComponentTile(ComponentTile componentTile) {
        if (componentTile != null) {
            return new TileData(
                    componentTile.getRotation(),
                    componentTile.getClass().getSimpleName(),
                    componentTile.getTileEdges().get(0),
                    componentTile.getTileEdges().get(1),
                    componentTile.getTileEdges().get(2),
                    componentTile.getTileEdges().get(3),
                    componentTile.getGraphicPath()
            );
        }
        return null;
    }

    /**
     * Converts a matrix of {@link ComponentTile}s to a matrix of {@link TileData} objects.
     *
     * @param ship The matrix to be converted.
     * @return The {@code List<List<TileData>>} representing the converted matrix.
     */
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
