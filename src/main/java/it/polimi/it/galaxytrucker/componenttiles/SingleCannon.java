package it.polimi.it.galaxytrucker.componenttiles;

import java.util.List;

public class SingleCannon extends ComponentTile {

    private final int firePower;

    public SingleCannon(TileEdge top, TileEdge right, TileEdge bottom, TileEdge left) {
        super(top, right, bottom, left);
        firePower = 1;
    }

    /*
     *  Returns the firepower of the cannon, for single cannons this is always 1
     *
     *  @return An {@code int} representing the firepower of the cannon
     */
    public int getFirePower() {
        return firePower;
    }
}
