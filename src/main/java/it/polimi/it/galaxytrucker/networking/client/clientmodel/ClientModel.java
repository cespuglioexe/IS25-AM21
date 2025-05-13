package it.polimi.it.galaxytrucker.networking.client.clientmodel;


import it.polimi.it.galaxytrucker.model.componenttiles.ComponentTile;
import it.polimi.it.galaxytrucker.model.componenttiles.TileData;

import java.util.ArrayList;
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
    private final PlayerData myData;
    /** A hash map associating each players UUID to their ship board */
    private final HashMap<UUID, List<List<TileData>>> playerShips = new HashMap<>();
    /** The players credit */
    private int credits = 0;

    private final List<List<Integer>> cardPiles = new ArrayList<>();

    private final List<TileData> savedTiles = new ArrayList<>();

    private final List<TileData> discardedTiles = new ArrayList<>();

    public ClientModel() {
        myData = new PlayerData();
    }

    public ClientModel(PlayerData player) {
        this.myData = player;
    }

    public void initializePlayerShips(List<UUID> playerIds) {
        for (UUID id : playerIds) {
            playerShips.put(id, new ArrayList<>());
        }
    }

    public void updatePlayerShip(UUID playerId, List<List<TileData>> playerShip) {
        playerShips.put(playerId, playerShip);
    }

    public PlayerData getMyData() {
        return myData;
    }

    public HashMap<UUID, List<List<TileData>>> getAllPlayerShips() {
        return playerShips;
    }

    public List<List<TileData>> getPlayerShips(UUID playerId) {
        return playerShips.get(playerId);
    }

    public int getCredits() {
        return credits;
    }

    public void addCredits(int credits) {
        this.credits += credits;
    }

    public List<Integer> getCardPile(int index) {
        return cardPiles.get(index);
    }

    public void setCardPiles(List<List<Integer>> cardPiles) {
        this.cardPiles.clear();
        this.cardPiles.addAll(cardPiles);
    }

    public List<TileData> getSavedTiles() {
        return savedTiles;
    }

    public void setSavedTiles(List<TileData> savedTiles) {
        this.savedTiles.clear();
        this.savedTiles.addAll(savedTiles);
    }

    public List<TileData> getDiscardedTiles() {
        return discardedTiles;
    }

    public void setDiscardedTiles(List<TileData> discardedTiles) {
        this.discardedTiles.clear();
        this.discardedTiles.addAll(discardedTiles);
    }
}