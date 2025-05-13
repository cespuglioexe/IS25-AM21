package it.polimi.it.galaxytrucker.commands.servercommands;

import it.polimi.it.galaxytrucker.model.componenttiles.ComponentTile;
import it.polimi.it.galaxytrucker.model.componenttiles.TileData;
import it.polimi.it.galaxytrucker.view.CLI.ConsoleColors;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class GameUpdate implements Serializable {
    private final GameUpdateType instructionType;
    private final UUID interestedPlayerId;
    private final String newSate;

    private final List<UUID> playerIds;
    private final List<List<Integer>> cardPileCompositions;

    private final TileData newTile;
    private final List<TileData> tileList;

    private final List<List<TileData>> shipBoard;
    private final HashMap<UUID, List<List<TileData>>> allPlayerShipBoards;

    public GameUpdate(GameUpdateBuilder builder) {
        this.instructionType = builder.instructionType;
        this.newSate = builder.newSate;
        this.newTile = builder.newTile;
        this.shipBoard = builder.shipBoard;
        this.tileList = builder.tileList;
        this.interestedPlayerId = builder.interestedPlayerId;
        this.playerIds = builder.playerIds;
        this.cardPileCompositions = builder.cardPileCompositions;
        this.allPlayerShipBoards = builder.allPlayerShipBoards;
    }

    public GameUpdateType getInstructionType() {
        return instructionType;
    }

    public String getNewSate() {
        return newSate;
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

    public List<List<Integer>> getCardPileCompositions() {
        return cardPileCompositions;
    }

    public HashMap<UUID, List<List<TileData>>> getAllPlayersShipboard(){
        return allPlayerShipBoards;
    }

    public static class GameUpdateBuilder {
        // Required fields
        private final GameUpdateType instructionType;
        private final UUID interestedPlayerId;

        // Optional fields
        private String newSate;
        private TileData newTile;
        private List<List<TileData>> shipBoard;
        private List<TileData> tileList;
        private List<UUID> playerIds;
        private List<List<Integer>> cardPileCompositions;
        private HashMap<UUID, List<List<TileData>>> allPlayerShipBoards;


        public GameUpdateBuilder(GameUpdateType instructionType, UUID interestedPlayerId) {
            this.instructionType = instructionType;
            this.interestedPlayerId = interestedPlayerId;
        }

        public GameUpdateBuilder setAllPlayerShipBoards(HashMap<UUID, List<List<TileData>>> allPlayerShipBoards) {
            this.allPlayerShipBoards = allPlayerShipBoards;
            return this;
        }

        public GameUpdateBuilder setNewSate(String newSate) {
            this.newSate = newSate;
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

        public GameUpdateBuilder setCardPileCompositions(List<List<Integer>> cardPileCompositions) {
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

        public GameUpdate build() {
            return new GameUpdate(this);
        }
    }
}
