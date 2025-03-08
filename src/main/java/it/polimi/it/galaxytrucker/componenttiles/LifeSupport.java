package it.polimi.it.galaxytrucker.componenttiles;

import it.polimi.it.galaxytrucker.utility.AlienType;

/**
 * This class represents a Life Support component tile.
 * Life Support tiles provide the necessary environment for a specific
 * alien type in adjacent {@link CabinModule}.
 *
 * @author Giacomo Amaducci
 * @version 1.0
 */
public class LifeSupport extends ComponentTile {
    /** The type of alien that this life support system can sustain */
    private final AlienType supportedAlienType;

    /**
     * Constructs a new Life Support tile with the specified alien type and edges.
     *
     * @param type the {@code AlienType} that this life support system supports
     * @param top the type of the top edge of this tile
     * @param right the type of the right edge of this tile
     * @param bottom the type of the bottom edge of this tile
     * @param left the type of the left edge of this tile
     */
    public LifeSupport(AlienType type, TileEdge top, TileEdge right, TileEdge bottom, TileEdge left) {
        super(top, right, bottom, left);
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