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
    private Direction orientation;

    public Shield(Direction dir, TileEdge top, TileEdge right, TileEdge bottom, TileEdge left) {
        super(top, right, bottom, left);
        this.orientation = dir;
    }

    @Override
    public void activate() {
        // Allows deflection of small cannon shots and meteors
    }

    @Override
    public boolean canBeActivated() {
        return false;
    }

    public Direction getOrientation() {
        return orientation;
    }
}
