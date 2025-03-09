package it.polimi.it.galaxytrucker.componenttiles;

/**
 * This class represents a Double Cannon component tile.
 * Double Cannons provide increased firepower but require activation.
 *
 * @author Giacomo Amaducci
 * @version 1.0
 */
public class DoubleCannon extends SingleCannon implements EnergyConsumer {

    /**
     * Constructs a new {@code DoubleCannon} with the specified edges.
     *
     * @param top the type of the top edge of this tile
     * @param right the type of the right edge of this tile
     * @param bottom the type of the bottom edge of this tile
     * @param left the type of the left edge of this tile
     */
    public DoubleCannon(TileEdge top, TileEdge right, TileEdge bottom, TileEdge left) {
        super(top, right, bottom, left);
    }

    /**
     * Activates the double cannon by setting its firepower to 2 if it is currently inactive.
     */
    @Override
    public void activate() {
        if (getFirePower() == 0) {
            setFirePower(2);
        }
    }

    /**
     * Determines if the cannon can be activated.
     *
     * @return always {@code false}, as this implementation does not allow reactivation.
     */
    @Override
    public boolean canBeActivated() {
        return false;
    }
}
