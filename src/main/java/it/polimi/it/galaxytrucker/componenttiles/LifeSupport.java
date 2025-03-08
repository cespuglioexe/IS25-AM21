package it.polimi.it.galaxytrucker.componenttiles;

import it.polimi.it.galaxytrucker.utility.AlienType;

public class LifeSupport extends ComponentTile {
    private final AlienType supportedAlienType;

    public LifeSupport(AlienType type, TileEdge top, TileEdge right, TileEdge bottom, TileEdge left) {
        super(top, right, bottom, left);
        this.supportedAlienType = type;
    }

    /*
     *  Return the type of alien that the cabin supports
     *
     *  @return The {@code AlienType} that this cabin supports
     */
    public AlienType getSupportedAlienType() {
        return supportedAlienType;
    }
}
