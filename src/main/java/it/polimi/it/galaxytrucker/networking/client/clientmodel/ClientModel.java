package it.polimi.it.galaxytrucker.networking.client.clientmodel;

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
 * @version 1.0
 */
public class ClientModel {
    /** A data object containing information about the player */
    private final PlayerData myData;
    /** A hash map associating each players UUID to their ship board */
    private final HashMap<UUID, List<List<TileData>>> playerShips = new HashMap<>();
    /** The players credit */
    private int credits = 0;
    /**
     * Composition of the card piles visible during the building phase
     */
    private final List<List<String>> cardPiles = new ArrayList<>();
    /**
     * Saved tiles, visible only to this player
     */
    private final List<TileData> savedTiles = new ArrayList<>();
    /**
     * Discarded component, visible to all players
     */
    private final List<TileData> discardedTiles = new ArrayList<>();
    /**
     * Path to the card currently in execution
     */
    private String activeCardGraphicPath;
    /**
     * Position of each player on the flight board
     */
    private HashMap<UUID, Integer> playerMarkerPositions = new HashMap<>();
    

    private int gameLevel;
    
    public ClientModel() {
        myData = new PlayerData();
    }

    public ClientModel(PlayerData player) {
        this.myData = player;
    }

    public synchronized void setActiveCardGraphicPath(String activeCardGraphicPath) {
        this.activeCardGraphicPath = activeCardGraphicPath;
    }

    public synchronized String getActiveCardGraphicPath() {
        return activeCardGraphicPath;
    }

    /**
     * Sets the clients local copy of the specified players ship board.
     *
     * @param playerId the {@code UUID} of the player whose ship needs to be updated.
     * @param playerShip the {@code List<List<TileData>>} representing the ship board.
     */
    public void updatePlayerShip(UUID playerId, List<List<TileData>> playerShip) {
        playerShips.put(playerId, playerShip);
    }

    public int getGameLevel() {
        return gameLevel;
    }

    public void setGameLevel(int gameLevel) {
        this.gameLevel = gameLevel;
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

    /**
     * Adds the specified number of credits to the player's total.
     * For subtracting credits, a negative number should be used.
     *
     * @param credits number of credits to add.
     */
    public void addCredits(int credits) {
        this.credits += credits;
    }

    public List<String> getCardPile(int index) {
        return cardPiles.get(index);
    }

    public void setCardPiles(List<List<String>> cardPiles) {
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

    public HashMap<UUID, Integer> getPlayerMarkerPositions() {
        return playerMarkerPositions;
    }

    public void setPlayerMarkerPositions(HashMap<UUID, Integer> playerMarkerPositions) {
        this.playerMarkerPositions = playerMarkerPositions;
    }
}