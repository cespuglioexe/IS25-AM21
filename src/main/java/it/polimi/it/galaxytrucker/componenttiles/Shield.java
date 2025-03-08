package it.polimi.it.galaxytrucker.componenttiles;

import it.polimi.it.galaxytrucker.utility.Direction;

public class Shield extends ComponentTile implements EnergyConsumer {
    /*
    *   Each shield covers 2 adjacent sides of the spaceship, considering the covered
    *   sides as the following 4 pairs:
    *    - Top+right
    *    - Right-bottom
    *    - Bottom-left
    *    - Left-top
    *   we can indicate which sides are covered by saving the first direction of the pair.
    */
    private final Direction orientation;

    public Shield(Direction dir, TileEdge top, TileEdge right, TileEdge bottom, TileEdge left) {
        super(top, right, bottom, left);
        this.orientation = dir;
    }

    @Override
    public boolean activate() {
        return true;
    }

    @Override
    public boolean canBeActivated() {
        return false;
    }

    /*
     *  Returns the first direction of the pair that is covered by the shield, adjusted for rotation
     *
     *  @return A {@code Direction} representing the first direction of the direction couple covered
     *          by the shield, the second can be calculated by the caller with
     *          {@code Direction.values()[(@result.ordinal() + 1) % 4]}
     */
    public Direction getOrientation() {
        return Direction.values()[(orientation.ordinal() + getRotation()) % 4];
    }
}
