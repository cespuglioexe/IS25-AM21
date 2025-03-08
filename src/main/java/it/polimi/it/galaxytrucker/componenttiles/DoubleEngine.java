package it.polimi.it.galaxytrucker.componenttiles;

public class DoubleEngine extends SingleEngine implements EnergyConsumer {

    public DoubleEngine(TileEdge top, TileEdge right, TileEdge bottom, TileEdge left) {
        super(top, right, bottom, left);
    }

    @Override
    public int getEnginePower () {
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
