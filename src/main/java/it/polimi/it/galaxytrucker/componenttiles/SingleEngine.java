package it.polimi.it.galaxytrucker.componenttiles;

public class SingleEngine extends ComponentTile {
    private final int enginePower;

    public SingleEngine(TileEdge top, TileEdge right, TileEdge bottom, TileEdge left) {
        super(top, right, bottom, left);
        enginePower = 1;
    }

    public int getEnginePower() {
        return enginePower;
    }
}
