package it.polimi.it.galaxytrucker.messages.servermessages;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.it.galaxytrucker.controller.GenericGameData;
import it.polimi.it.galaxytrucker.model.componenttiles.ComponentTile;
import it.polimi.it.galaxytrucker.model.componenttiles.TileData;
import it.polimi.it.galaxytrucker.model.utility.Color;
import it.polimi.it.galaxytrucker.view.CLI.ConsoleColors;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class GameUpdate implements Serializable {
    private final GameUpdateType instructionType;
    private final UUID interestedPlayerId;
    private final String newSate;
    private final Color playerColor;

    private final List<UUID> playerIds;
    private final List<List<String>> cardPileCompositions;

    private final TileData newTile;
    private final List<TileData> tileList;

    private final List<List<TileData>> shipBoard;
    private final HashMap<UUID, List<List<TileData>>> allPlayerShipBoard;
    private final HashMap<UUID, Integer> playerMarkerPositions;

    // Server-sent update contents
    private final String playerName;
    private final UUID gameUuid;
    private final UUID playerUuid;
    private final List<GenericGameData> activeControllers;
    private final boolean successfulOperation;
    private final String operationMessage;
    private final int gameLevel;
    private final Map<String, Object> cardDetail;

    @Deprecated
    @JsonCreator
    public GameUpdate(
            @JsonProperty("instructionType") GameUpdateType instructionType,
            @JsonProperty("interestedPlayerId") UUID interestedPlayerId,
            @JsonProperty("newSate") String newSate,
            @JsonProperty("playerIds") List<UUID> playerIds,
            @JsonProperty("cardPileCompositions") List<List<String>> cardPileCompositions,
            @JsonProperty("newTile") TileData newTile,
            @JsonProperty("tileList") List<TileData> tileList,
            @JsonProperty("shipBoard") List<List<TileData>> shipBoard,
            @JsonProperty("allPlayerShipBoard") HashMap<UUID, List<List<TileData>>> allPlayerShipBoard,
            @JsonProperty("playerName") String playerName,
            @JsonProperty("gameUuid") UUID gameUuid,
            @JsonProperty("playerUuid") UUID playerUuid,
            @JsonProperty("activeControllers") List<GenericGameData> activeControllers,
            @JsonProperty("successfulOperation") boolean successfulOperation,
            @JsonProperty("operationMessage") String operationMessage,
            @JsonProperty("gameLevel") int gameLevel,
            @JsonProperty("cardDetail") Map<String, Object> cardDetail,
            @JsonProperty("playerMarkerPositions") HashMap<UUID, Integer> playerMarkerPositions,
            @JsonProperty("playerColor") Color playerColor) {
        this.instructionType = instructionType;
        this.interestedPlayerId = interestedPlayerId;
        this.newSate = newSate;
        this.playerIds = playerIds;
        this.cardPileCompositions = cardPileCompositions;
        this.newTile = newTile;
        this.tileList = tileList;
        this.shipBoard = shipBoard;
        this.allPlayerShipBoard = allPlayerShipBoard;
        this.playerName = playerName;
        this.gameUuid = gameUuid;
        this.playerUuid = playerUuid;
        this.activeControllers = activeControllers;
        this.successfulOperation = successfulOperation;
        this.operationMessage = operationMessage;
        this.gameLevel = gameLevel;
        this.cardDetail = cardDetail;
        this.playerMarkerPositions = playerMarkerPositions;
        this.playerColor = playerColor;
    }

    public GameUpdate(GameUpdateBuilder builder) {
        this.instructionType = builder.instructionType;
        this.newSate = builder.newSate;
        this.newTile = builder.newTile;
        this.shipBoard = builder.shipBoard;
        this.tileList = builder.tileList;
        this.interestedPlayerId = builder.interestedPlayerId;
        this.playerIds = builder.playerIds;
        this.cardPileCompositions = builder.cardPileCompositions;
        this.allPlayerShipBoard = builder.allPlayerShipBoard;
        this.playerName = builder.playerName;
        this.gameUuid = builder.gameUuid;
        this.activeControllers = builder.activeControllers;
        this.playerUuid = builder.playerUuid;
        this.successfulOperation = builder.successfulOperation;
        this.operationMessage = builder.operationMessage;
        this.gameLevel = builder.gameLevel;
        this.cardDetail = builder.cardDetail;
        this.playerMarkerPositions = builder.playerMarkerPositions;
        this.playerColor = builder.playerColor;
    }

    public GameUpdateType getInstructionType() {
        return instructionType;
    }

    public String getNewSate() {
        return newSate;
    }

    public HashMap<UUID, Integer> getPlayerMarkerPositions() {
        return playerMarkerPositions;
    }

    public Color getPlayerColor() {
        return playerColor;
    }

    public TileData getNewTile() {
        return newTile;
    }

    public List<List<TileData>> getShipBoard() {
        return shipBoard;
    }

    public List<TileData> getTileList() {
        return tileList;
    }

    public UUID getInterestedPlayerId() {
        return interestedPlayerId;
    }

    public List<UUID> getPlayerIds() {
        return playerIds;
    }

    public List<List<String>> getCardPileCompositions() {
        return cardPileCompositions;
    }

    public HashMap<UUID, List<List<TileData>>> getAllPlayerShipBoard(){
        return allPlayerShipBoard;
    }

    public String getPlayerName() {
        return playerName;
    }

    public UUID getGameUuid() {
        return gameUuid;
    }

    public List<GenericGameData> getActiveControllers() {
        return activeControllers;
    }

    public UUID getPlayerUuid() {
        return playerUuid;
    }

    public boolean isSuccessfulOperation() {
        return successfulOperation;
    }

    public int getGameLevel() {return gameLevel;}

    public String getOperationMessage() {
        return operationMessage;
    }

    public Map<String, Object> getCardDetail() {
        return cardDetail;
    }

    public static class GameUpdateBuilder {
        // Required fields
        private final GameUpdateType instructionType;

        // Optional fields
        private UUID interestedPlayerId;
        private String newSate;
        private TileData newTile;
        private List<List<TileData>> shipBoard;
        private List<TileData> tileList;
        private List<UUID> playerIds;
        private List<List<String>> cardPileCompositions;
        private HashMap<UUID, List<List<TileData>>> allPlayerShipBoard;
        private HashMap<UUID, Integer> playerMarkerPositions;

        private String playerName;
        private UUID gameUuid;
        private UUID playerUuid;
        private List<GenericGameData> activeControllers;
        private boolean successfulOperation;
        private String operationMessage;
        private int gameLevel;
        private Map<String, Object> cardDetail;
        private Color playerColor;

        public GameUpdateBuilder(GameUpdateType instructionType) {
            this.instructionType = instructionType;
        }

        public GameUpdateBuilder setOperationMessage(String operationMessage) {
            this.operationMessage = operationMessage;
            return this;
        }

        public GameUpdateBuilder setPlayerMarkerPositions(HashMap<UUID, Integer> playerMarkerPositions) {
            this.playerMarkerPositions = playerMarkerPositions;
            return this;
        }

        public GameUpdateBuilder setPlayerColor(Color playerColor) {
            this.playerColor = playerColor;
            return this;
        }

        public GameUpdateBuilder setInterestedPlayerId(UUID interestedPlayerId) {
            this.interestedPlayerId = interestedPlayerId;
            return this;
        }

        public GameUpdateBuilder setSuccessfulOperation(boolean successfulOperation) {
            this.successfulOperation = successfulOperation;
            return this;
        }

        public GameUpdateBuilder setPlayerUuid(UUID playerUuid) {
            this.playerUuid = playerUuid;
            return this;
        }

        public GameUpdateBuilder setPlayerName(String playerName) {
            this.playerName = playerName;
            return this;
        }

        public GameUpdateBuilder setGameUuid(UUID gameUuid) {
            this.gameUuid = gameUuid;
            return this;
        }

        public GameUpdateBuilder setActiveControllers(List<GenericGameData> activeControllers) {
            this.activeControllers = activeControllers;
            return this;
        }

        public GameUpdateBuilder setAllPlayerShipBoards(HashMap<UUID, List<List<TileData>>> allPlayerShipBoards) {
            this.allPlayerShipBoard = allPlayerShipBoards;
            return this;
        }

        public GameUpdateBuilder setNewSate(String newSate) {
            this.newSate = newSate;
            return this;
        }

        public GameUpdateBuilder setCardDetail(Map<String, Object> detail) {
            this.cardDetail = detail;
            return this;
        }

        // It's recommended to use the overloaded function {@code #setNewTile(ComponentTile)}
        @Deprecated
        public GameUpdateBuilder setNewTile(TileData newTile) {
            this.newTile = newTile;
            return this;
        }

        public GameUpdateBuilder setPlayerIds(List<UUID> playerIds) {
            this.playerIds = playerIds;
            return this;
        }

        public GameUpdateBuilder setCardPileCompositions(List<List<String>> cardPileCompositions) {
            this.cardPileCompositions = cardPileCompositions;
            return this;
        }

        public GameUpdateBuilder setNewTile(ComponentTile newTile) {
            this.newTile = TileData.createTileDataFromComponentTile(newTile);
            return this;
        }

        public GameUpdateBuilder setShipBoard(List<List<ComponentTile>> shipBoard) {
            this.shipBoard = new ArrayList<>();

            for (List<ComponentTile> row : shipBoard) {
                int i = 0;
                List<TileData> dataRow = new ArrayList<>();
                for (ComponentTile tile : row) {
                    dataRow.add(TileData.createTileDataFromComponentTile(row.get(i)));
                    i++;
                }
                this.shipBoard.add(dataRow);
            }

            return this;
        }

        public GameUpdateBuilder setTileList(List<ComponentTile> list) {
            this.tileList = new ArrayList<>();
            System.out.println(ConsoleColors.GREEN + "Converting list of ComponentTiles to TileData" + ConsoleColors.RESET);
            for (ComponentTile tile : list) {
                this.tileList.add(TileData.createTileDataFromComponentTile(tile));
            }
            System.out.println(ConsoleColors.GREEN + "After converting list of ComponentTiles to TileData" + ConsoleColors.RESET);
            return this;
        }
        
        public GameUpdateBuilder setGameLevel(int gameLevel) {
            this.gameLevel = gameLevel;
            return this;
        }

        public GameUpdate build() {
            return new GameUpdate(this);
        }
    }
}
