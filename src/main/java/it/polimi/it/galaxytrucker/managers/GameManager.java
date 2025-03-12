package it.polimi.it.galaxytrucker.managers;

import it.polimi.it.galaxytrucker.utility.ComponentTileData;
import it.polimi.it.galaxytrucker.utility.TileLoader;

import java.io.IOException;
import java.util.List;

public class GameManager {

    private FlightBoardState flightBoardState;

    
    public void makeComponentTilePool() {
        try {
            List<ComponentTileData> tiles = TileLoader.loadTiles("tiles.json");

            for (ComponentTileData tile : tiles) {
                System.out.println("Loaded tile: " + tile.getType() + " with edges " + tile.getEdges());
            }
        } catch (IOException e) {
            System.err.println("Failed to load tiles: " + e.getMessage());
        }
    }

    public FlightBoardState getFlightBoardState() {
        return flightBoardState;
    }

    // INIZIO IMPLEMENTAZIONE LUNEDI
}
