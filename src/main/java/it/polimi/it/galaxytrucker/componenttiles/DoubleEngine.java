package it.polimi.it.galaxytrucker.componenttiles;

public class DoubleEngine extends SingleEngine implements EnergyConsumer {

    public DoubleEngine(TileEdge top, TileEdge right, TileEdge bottom, TileEdge left) {
        super(top, right, bottom, left);
    }

    private void setEnginePower(int power) {
        this.enginePower = power;
    }

    @Override
    public void activate() {
        setEnginePower(2);
    }

    @Override
    public boolean canBeActivated() {
        return false;
    }
}
