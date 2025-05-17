package it.polimi.it.galaxytrucker.commands;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.it.galaxytrucker.networking.VirtualClient;
import it.polimi.it.galaxytrucker.networking.client.Client;
import it.polimi.it.galaxytrucker.networking.server.rmi.RMIVirtualClient;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * A data class used for communicating client actions to the server.
 *
 * @author giacomoamaducci
 * @version 1.1
 */
public class UserInput implements Serializable {
    private final UserInputType type;
    // private final RequestType requestType;

    private final String serverName;
    private final String playerName;
    private final UUID playerUuid;

    private final int gameLevel;
    private final int gamePlayers;
    private final UUID gameId;
    private final int selectedTileIndex;
    private final int cardPileIndex;

    private final List<Integer> coords;
    private final int rotation;

    @Deprecated
    @JsonCreator
    public UserInput(
            @JsonProperty("type") UserInputType type,
            @JsonProperty("serverName") String serverName,
            @JsonProperty("playerName") String playerName,
            @JsonProperty("playerUuid") UUID playerUuid,
            @JsonProperty("gameLevel") int gameLevel,
            @JsonProperty("gamePlayers") int gamePlayers,
            @JsonProperty("gameId") UUID gameId,
            @JsonProperty("selectedTileIndex") int selectedTileIndex,
            @JsonProperty("cardPileIndex") int cardPileIndex,
            @JsonProperty("coords") List<Integer> coords,
            @JsonProperty("rotation") int rotation) {
        this.type = type;
        this.serverName = serverName;
        this.playerName = playerName;
        this.playerUuid = playerUuid;
        this.gameLevel = gameLevel;
        this.gamePlayers = gamePlayers;
        this.gameId = gameId;
        this.selectedTileIndex = selectedTileIndex;
        this.cardPileIndex = cardPileIndex;
        this.coords = coords;
        this.rotation = rotation;
    }

    public UserInput(UserInputBuilder builder) {
        this.type = builder.type;
        this.serverName = builder.serverName;
        this.playerName = builder.playerName;
        this.gameLevel = builder.gameLevel;
        this.gamePlayers = builder.gamePlayers;
        this.gameId = builder.gameId;
        // this.requestType = builder.requestType;
        this.coords = builder.coords;
        this.rotation = builder.rotation;
        this.selectedTileIndex = builder.selectedTileIndex;
        this.cardPileIndex = builder.cardPileIndex;
        this.playerUuid = builder.playerUuid;
    }

    public UserInputType getType() {
        return type;
    }

    public String getServerName() {
        return serverName;
    }

    public String getPlayerName() {
        return playerName;
    }

    public int getGameLevel() {
        return gameLevel;
    }

    public int getGamePlayers() {
        return gamePlayers;
    }

    public UUID getGameId() {
        return gameId;
    }

//    public RequestType getRequestType() {
//        return requestType;
//    }

    /**
     * Return a list of two integers, representing coordinates on a ship board.
     * The content of the list is {@code [column, row]}.
     *
     * @return a {@code List<Integer>} containing 2 elements.
     */
    public List<Integer> getCoords() {
        return coords;
    }

    public int getRotation() {
        return rotation;
    }

    public int getSelectedTileIndex() {
        return selectedTileIndex;
    }

    public int getCardPileIndex() {
        return cardPileIndex;
    }

    public UUID getPlayerUuid() {
        return playerUuid;
    }

    /**
     * Builder class for creating instances of the UserInput class.
     * This class provides a convenient way to configure all optional and required parameters
     * of a UserInput object before its creation.
     */
    public static class UserInputBuilder {
        // Required parameters
        private final UserInputType type;

        // Optional parameters
        // private RequestType requestType = RequestType.EMPTY;

        private String serverName = "";
        private String playerName = "";
        private UUID playerUuid = new UUID(0L, 0L);


        private int gameLevel = 0;
        private int gamePlayers = 0;
        private UUID gameId = new UUID(0L, 0L);
        private int selectedTileIndex = 0;
        private int cardPileIndex = 0;
        
        private List<Integer> coords = new ArrayList<>();

        private int rotation = 0;

        public UserInputBuilder(UserInputType type) {
            this.type = type;
        }

        public UserInputBuilder setServerName(String serverName) {
            this.serverName = serverName;
            return this;
        }

        public UserInputBuilder setPlayerName(String playerName) {
            this.playerName = playerName;
            return this;
        }

        public UserInputBuilder setPlayerUuid(UUID playerUuid) {
            this.playerUuid = playerUuid;
            return this;
        }

        public UserInputBuilder setGameLevel(int gameLevel) {
            this.gameLevel = gameLevel;
            return this;
        }

        public UserInputBuilder setGamePlayers(int gamePlayers) {
            this.gamePlayers = gamePlayers;
            return this;
        }

        public UserInputBuilder setGameId(UUID gameId) {
            this.gameId = gameId;
            return this;
        }

//        public UserInputBuilder setRequestType(RequestType requestType) {
//            this.requestType = requestType;
//            return this;
//        }
        
        public UserInputBuilder setCoords(int column, int row) {
            this.coords = List.of(column, row);
            return this;
        }

        public UserInputBuilder setRotation(int rotation) {
            this.rotation = rotation;
            return this;
        }

        public UserInputBuilder setSelectedTileIndex(int selectedTileIndex) {
            this.selectedTileIndex = selectedTileIndex;
            return this;
        }

        public UserInputBuilder setCardPileIndex(int cardPileIndex) {
            this.cardPileIndex = cardPileIndex;
            return this;
        }

        public UserInput build() {
            return new UserInput(this);
        }
    }
}