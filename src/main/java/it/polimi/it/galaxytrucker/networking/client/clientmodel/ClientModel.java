package it.polimi.it.galaxytrucker.networking.client.clientmodel;


import it.polimi.it.galaxytrucker.model.componenttiles.ComponentTile;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * This class is used by clients to store a local version of the
 * game state, as well as information relating to the player themselves
 * (i.e., nickname, game they're in, etc.)
 *
 * @author giacomoamaducci
 * @version 0.1
 */
public class ClientModel {
    /** A data object containing information about the player */
    private final PlayerData player;
    /** A hash map associating each players UUID to their ship board */
    private final HashMap<UUID, List<List<ComponentTile>>> playerShips = new HashMap<>();
    /** The players credit */
    private int credits = 0;

    public ClientModel(PlayerData player) {
        this.player = player;
    }

    public void updatePlayerShip(UUID playerId, List<List<ComponentTile>> playerShip) {
        playerShips.put(playerId, playerShip);
    }

    public PlayerData getPlayer() {
        return player;
    }

    public HashMap<UUID, List<List<ComponentTile>>> getPlayerShips() {
        return playerShips;
    }

    public int getCredits() {
        return credits;
    }
}
