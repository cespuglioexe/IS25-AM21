package it.polimi.it.galaxytrucker.componenttiles;

import java.util.List;

public class SingleCannon extends ComponentTile {

    private int firePower;

    public SingleCannon(TileEdge top, TileEdge right, TileEdge bottom, TileEdge left) {
        super(top, right, bottom, left);
        firePower = 1;
    }

    public int getFirePower() {
        return firePower;
    }
}
