package it.polimi.it.galaxytrucker.componenttiles;

public class DoubleCannon extends SingleCannon implements EnergyConsumer{
    public DoubleCannon(TileEdge top, TileEdge right, TileEdge bottom, TileEdge left) {
        super(top, right, bottom, left);
    }

    void setFirePower(int power) {
        firePower = power;
    }

    @Override
    public void activate() {
        setFirePower(2);
    }

    @Override
    public boolean canBeActivated() {
        return false;
    }
}
