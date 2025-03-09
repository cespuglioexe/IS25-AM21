package it.polimi.it.galaxytrucker.managers;

import it.polimi.it.galaxytrucker.utility.ComponentTileData;
import it.polimi.it.galaxytrucker.utility.TileLoader;

import java.io.IOException;
import java.util.List;

public class GameManager {
    public GameManager() {
    }

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

    // INIZIO IMPLEMENTAZIONE LUNEDI
}
