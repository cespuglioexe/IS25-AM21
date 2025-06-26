package it.polimi.it.galaxytrucker.networking.client.clientmodel;

import it.polimi.it.galaxytrucker.model.componenttiles.TileData;
import it.polimi.it.galaxytrucker.model.utility.Color;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * This class is used by clients to store a local version of the
 * game state, as well as information relating to the player themselves
 * (i.e., nickname, game they are in, etc.)
 *
 * @author giacomoamaducci
 * @version 1.0
 */
public class ClientModel {
    /** A data object containing information about the player */
    private final PlayerData myData;
    /** A hash map associating each player's UUID to their ship board */
    private final HashMap<UUID, List<List<TileData>>> playerShips = new HashMap<>();
    /** The player's credit */
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
     * Path to the graphic of card currently in execution
     */
    private String activeCardGraphicPath;

    private final Map<String, Object> currentCardDetails = new HashMap<>();
    /**
     * Position of each player on the flight board
     */
    private final HashMap<UUID, Integer> playerMarkerPositions = new HashMap<>();
    /**
     * Position of color player in the game
     */
    private final HashMap<UUID, Color> playerColors = new HashMap<>();
    /**
     * Level of the game this player is playing in (1 or 2)
     */
    private int gameLevel;
    /**
     * Used for synchronization when working with non-final variables
     */
    private final Object syncFlag = new Object();

    private final HashMap<UUID, String> nicknames = new HashMap<>();

    public ClientModel() {
        myData = new PlayerData();
    }

    public ClientModel(PlayerData player) {
        this.myData = player;
    }

    public void setActiveCardGraphicPath(String activeCardGraphicPath) {
        synchronized (this.syncFlag){
            this.activeCardGraphicPath = activeCardGraphicPath;
        }
    }

    public String getActiveCardGraphicPath() {
        synchronized (this.syncFlag) {
            return activeCardGraphicPath;
        }
    }

    public void setPlayerColors(HashMap<UUID, Color> playerColors) {
        synchronized (this.playerColors) {
            this.playerColors.clear();
            this.playerColors.putAll(playerColors);
        }
    }

    public HashMap<UUID, Color> getPlayerColors() {
        synchronized (this.playerColors) {
            return playerColors;
        }
    }



    /**
     * Sets the clients local copy of the specified players ship board.
     *
     * @param playerId the {@code UUID} of the player whose ship needs to be updated.
     * @param playerShip the {@code List<List<TileData>>} representing the ship board.
     */
    public void updatePlayerShip(UUID playerId, List<List<TileData>> playerShip) {
        synchronized (this.playerShips) {
            playerShips.put(playerId, playerShip);
        }
    }

    public int getGameLevel() {
        synchronized (this.syncFlag) {
            return gameLevel;
        }
    }

    public void setGameLevel(int gameLevel) {
        synchronized (this.syncFlag){
            this.gameLevel = gameLevel;
        }
    }

    public PlayerData getMyData() {
        synchronized (this.myData) {
            return myData;
        }
    }

    public HashMap<UUID, List<List<TileData>>> getAllPlayerShips() {
        synchronized (this.playerShips) {
            return playerShips;
        }
    }

    public List<List<TileData>> getPlayerShips(UUID playerId) {
        synchronized (this.playerShips) {
            return playerShips.get(playerId);
        }
    }

    public int getCredits() {
        synchronized (this.syncFlag) {
            return credits;
        }
    }

    /**
     * Adds the specified number of credits to the player's total.
     * For subtracting credits, a negative number should be used.
     *
     * @param credits number of credits to add.
     */
    public void addCredits(int credits) {
        synchronized (this.syncFlag){
            this.credits += credits;
        }
    }

    public List<String> getCardPile(int index) {
        synchronized (this.cardPiles){
            return cardPiles.get(index);
        }
    }

    public void setCardPiles(List<List<String>> cardPiles) {
        synchronized (this.cardPiles) {
            this.cardPiles.clear();
            this.cardPiles.addAll(cardPiles);
        }
    }

    public List<TileData> getSavedTiles() {
        synchronized (this.savedTiles) {
            return savedTiles;
        }
    }

    public void setSavedTiles(List<TileData> savedTiles) {
        synchronized (this.savedTiles) {
            this.savedTiles.clear();
            this.savedTiles.addAll(savedTiles);
        }
    }

    public List<TileData> getDiscardedTiles() {
        synchronized (this.discardedTiles) {
            return discardedTiles;
        }
    }

    public void setDiscardedTiles(List<TileData> discardedTiles) {
        synchronized (this.discardedTiles) {
            this.discardedTiles.clear();
            this.discardedTiles.addAll(discardedTiles);
        }
    }

    public String getCurrentCard() {
        synchronized (this.currentCardDetails) {
            return String.valueOf(currentCardDetails.getOrDefault("card", "Margarozzo"));
        }
    }

    public void putCardDetail(String key, Object value) {
        synchronized (this.currentCardDetails) {
            currentCardDetails.put(key, value);
        }
    }

    public void setCardDetail(Map<String, Object> newCardDetails) {
        synchronized (this.currentCardDetails) {
            this.currentCardDetails.clear();
            this.currentCardDetails.putAll(newCardDetails);
        }
    }

    public <T> T getCardDetail(String key, Class<T> clazz) {
        synchronized (this.currentCardDetails) {
            Object value = currentCardDetails.get(key);

            if (!clazz.isInstance(value)) {
                throw new IllegalArgumentException("Expected type " + clazz + " for key '" + key + "'");
            }

            return clazz.cast(value);
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T getUnsafeCardDetail(String key) {
        synchronized (this.currentCardDetails) {
            return (T) currentCardDetails.get(key);
        }
    }

    public boolean hasCardDetail(String key) {
        synchronized (this.currentCardDetails) {
            return currentCardDetails.containsKey(key);
        }
    }

    public HashMap<UUID, Integer> getPlayerMarkerPositions() {
        synchronized (this.playerMarkerPositions) {
            return playerMarkerPositions;
        }
    }

    public void setPlayerMarkerPositions(HashMap<UUID, Integer> playerMarkerPositions) {
        synchronized (this.playerMarkerPositions) {
            this.playerMarkerPositions.clear();
            this.playerMarkerPositions.putAll(playerMarkerPositions);
        }
    }

    public void setAllPlayersNickname(HashMap<UUID, String> nicknames) {
        synchronized (this.nicknames) {
            this.playerMarkerPositions.clear();
            this.nicknames.putAll(nicknames);
        }
    }

    public HashMap<UUID, String> getAllPlayersNickname(){
        synchronized (this.nicknames) {
            return nicknames;
        }
    }

}