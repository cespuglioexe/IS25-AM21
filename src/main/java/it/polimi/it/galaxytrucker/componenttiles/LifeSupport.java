package it.polimi.it.galaxytrucker.componenttiles;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.it.galaxytrucker.utility.AlienType;

import java.util.List;

/**
 * This class represents a Life Support component tile.
 * Life Support tiles provide the necessary environment for a specific
 * alien type in adjacent {@link CabinModule}.
 *
 * @author Giacomo Amaducci
 * @version 1.1
 */
public class LifeSupport extends ComponentTile {
    /** The type of alien that this life support system can sustain */
    private final AlienType supportedAlienType;

    /**
     * Constructs a new Life Support tile with the specified alien type and edges.
     *
     * @param type the {@code AlienType} that this life support system supports
     * @param edges a {@code List} of {@link TileEdge} objects representing the four edges of this tile
     */
    @JsonCreator
    public LifeSupport(
            @JsonProperty("alienType") AlienType type,
            @JsonProperty("edges") List<TileEdge> edges) {
        super(edges);
        this.supportedAlienType = type;
    }

    /**
     * Returns the type of alien that this life support system can sustain.
     *
     * @return the {@code AlienType} that this life support supports
     */
    public AlienType getSupportedAlienType() {
        return supportedAlienType;
    }
}