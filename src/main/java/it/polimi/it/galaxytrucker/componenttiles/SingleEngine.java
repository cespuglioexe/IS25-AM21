package it.polimi.it.galaxytrucker.componenttiles;

public class SingleEngine extends ComponentTile {
    private final int enginePower;

    public SingleEngine(TileEdge top, TileEdge right, TileEdge bottom, TileEdge left) {
        super(top, right, bottom, left);
        enginePower = 1;
    }

    /*
     *  Returns the power of the engine, for single engines this is always 1
     *
     *  @return An {@code int} representing the power of the engine
     */
    public int getEnginePower() {
        return enginePower;
    }
}
