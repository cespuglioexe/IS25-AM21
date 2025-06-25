package it.polimi.it.galaxytrucker.model.componenttiles;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.it.galaxytrucker.model.crewmates.Alien;
import it.polimi.it.galaxytrucker.model.crewmates.Crewmate;
import it.polimi.it.galaxytrucker.model.crewmates.Human;
import it.polimi.it.galaxytrucker.model.utility.Cargo;
import it.polimi.it.galaxytrucker.model.utility.Color;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents the essential data of a game tile, used for serialization and deserialization.
 * This record captures the state of a {@link ComponentTile} in a simplified format.
 *
 * <p>The fields of this record include:</p>
 * <ul>
 * <li>{@code rotation}: The rotation of the tile in 90° increments (0, 1, 2, 3).</li>
 * <li>{@code type}: The string representation of the tile's type (e.g., "BatteryComponent", "CentralCabin").</li>
 * <li>{@code top}: The {@link TileEdge} configuration for the top side of the tile.</li>
 * <li>{@code right}: The {@link TileEdge} configuration for the right side of the tile.</li>
 * <li>{@code bottom}: The {@link TileEdge} configuration for the bottom side of the tile.</li>
 * <li>{@code left}: The {@link TileEdge} configuration for the left side of the tile.</li>
 * <li>{@code graphicPath}: The file path to the graphic representation of the tile.</li>
 * <li>{@code batteryCharge}: The current battery charge of the tile. Applicable only for "BatteryComponent" tiles; otherwise, it is 0.</li>
 * <li>{@code crewmates}: A list of strings representing the types of crewmates on the tile. Applicable for "CentralCabin" and "CabinModule" tiles; otherwise, it is an empty list. Crewmates are represented as "HUMAN" or the uppercase string of the alien type ("PURPLE", "BROWN").</li>
 * <li>{@code cargo}: A list of {@link Color} enums representing the colors of cargo on the tile. Applicable for "CargoHold" and "SpecialCargoHold" tiles; otherwise, it is an empty list.</li>
 * </ul>
 *
 * @author Giacomo Amaducci
 * @version 1.2
 */
public record TileData(int rotation, String type, TileEdge top, TileEdge right, TileEdge bottom, TileEdge left,
                       String graphicPath, int batteryCharge, List<String> crewmates, List<Color> cargo) implements Serializable {

    /**
     * Constructs a {@code TileData} record. This constructor is annotated with {@link JsonCreator}
     * to facilitate deserialization from JSON, mapping JSON properties to record components.
     *
     * @param rotation The rotation of the tile in 90° increments (0, 1, 2, 3).
     * @param type The string representation of the tile's type (e.g., "BatteryComponent", "CentralCabin").
     * @param top The {@link TileEdge} configuration for the top side of the tile.
     * @param right The {@link TileEdge} configuration for the right side of the tile.
     * @param bottom The {@link TileEdge} configuration for the bottom side of the tile.
     * @param left The {@link TileEdge} configuration for the left side of the tile.
     * @param graphicPath The file path to the graphic representation of the tile.
     * @param batteryCharge The current battery charge of the tile, if applicable (0 otherwise).
     * @param crewmates A list of strings representing the types of crewmates on the tile ("HUMAN" or "PURPLE"/"BROWN" for aliens).
     * @param cargo A list of {@link Color} enums representing the colors of cargo on the tile.
     */
    @JsonCreator
    public TileData(
            @JsonProperty("rotation") int rotation,
            @JsonProperty("type") String type,
            @JsonProperty("top") TileEdge top,
            @JsonProperty("right") TileEdge right,
            @JsonProperty("bottom") TileEdge bottom,
            @JsonProperty("left") TileEdge left,
            @JsonProperty("graphicPath") String graphicPath,
            @JsonProperty("batteryCharge") int batteryCharge,
            @JsonProperty("crewmates") List<String> crewmates,
            @JsonProperty("cargo") List<Color> cargo) {
        this.rotation = rotation;
        this.type = type;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
        this.left = left;
        this.graphicPath = graphicPath;
        this.batteryCharge = batteryCharge;
        this.crewmates = crewmates;
        this.cargo = cargo;
    }

    /**
     * Converts a {@link ComponentTile} object to a {@link TileData} record object.
     * This static factory method extracts relevant data from a {@code ComponentTile}
     * and populates a new {@code TileData} instance. It handles specific component
     * types (e.g., {@code BatteryComponent}, {@code CentralCabin}, {@code CargoHold})
     * to correctly extract their unique properties.
     *
     * @param componentTile The component to be converted. Can be {@code null}.
     * @return The {@code TileData} representation of the given component, or {@code null} if the input component is {@code null}.
     */
    public static TileData createTileDataFromComponentTile(ComponentTile componentTile) {
        if (componentTile != null) {

            int batteryCharge = 0;
            List<String> crewmateList = new ArrayList<>();
            List<Color> cargoColors = new ArrayList<>();

            switch (componentTile.getClass().getSimpleName()) {
                case "BatteryComponent" -> {
                    batteryCharge = ((BatteryComponent) componentTile).getBatteryCharge();
                }

                case "CentralCabin", "CabinModule" -> {
                    for (Crewmate crewmate : ((CentralCabin) componentTile).getCrewmates()) {
                        crewmateList.add(crewmate instanceof Human ? "HUMAN" : ((Alien) crewmate).getAlienType().toString().toUpperCase());
                    }
                }

                case "CargoHold", "SpecialCargoHold" -> {
                    for (Cargo cargo : ((CargoHold) componentTile).getContainedCargo()) {
                        cargoColors.add(cargo.getColor());
                    }
                }
            }

            return new TileData(
                    componentTile.getRotation(),
                    componentTile.getClass().getSimpleName(),
                    componentTile.getTileEdges().get(0),
                    componentTile.getTileEdges().get(1),
                    componentTile.getTileEdges().get(2),
                    componentTile.getTileEdges().get(3),
                    componentTile.getGraphicPath(),
                    batteryCharge,
                    crewmateList,
                    cargoColors
            );
        }
        return null;
    }

    /**
     * Converts a matrix (list of lists) of {@link ComponentTile} objects to a matrix of {@link TileData} objects.
     * This method iterates through the provided ship structure, converting each {@code ComponentTile}
     * into its {@code TileData} equivalent using {@link #createTileDataFromComponentTile(ComponentTile)}.
     *
     * @param ship The matrix of {@code ComponentTile}s to be converted. Representing the ship layout.
     * @return A {@code List<List<TileData>>} representing the converted ship matrix. Each inner list represents a row of tiles.
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