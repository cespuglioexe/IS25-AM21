package it.polimi.it.galaxytrucker.componenttiles;

public class DoubleCannon extends SingleCannon implements EnergyConsumer{
    public DoubleCannon(TileEdge top, TileEdge right, TileEdge bottom, TileEdge left) {
        super(top, right, bottom, left);
    }

    @Override
    public int getFirePower () {
        if (activate())
            return 2;
        return 0;
    }

    @Override
    public boolean activate() {
        return true;
    }

    @Override
    public boolean canBeActivated() {
        return false;
    }
}
