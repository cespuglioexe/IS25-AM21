package it.polimi.it.galaxytrucker.networking.messages;

import it.polimi.it.galaxytrucker.model.componenttiles.ComponentTile;
import it.polimi.it.galaxytrucker.model.componenttiles.TileData;
import it.polimi.it.galaxytrucker.model.componenttiles.TileEdge;
import it.polimi.it.galaxytrucker.view.ConsoleColors;

import javax.naming.TimeLimitExceededException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GameUpdate implements Serializable {
    private final GameUpdateType instructionType;
    private final String newSate;

    private final TileData newTile;
    private final List<TileData> tileList;

    private final List<List<TileData>> shipBoard;

    public GameUpdate(GameUpdateBuilder builder) {
        this.instructionType = builder.instructionType;
        this.newSate = builder.newSate;
        this.newTile = builder.newTile;
        this.shipBoard = builder.shipBoard;
        this.tileList = builder.tileList;
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

    public static class GameUpdateBuilder {
        // Required fields
        private final GameUpdateType instructionType;

        // Optional fields
        private String newSate;
        private TileData newTile;
        private List<List<TileData>> shipBoard;
        private List<TileData> tileList;

        public GameUpdateBuilder(GameUpdateType instructionType) {
            this.instructionType = instructionType;
        }

        public GameUpdateBuilder setNewSate(String newSate) {
            this.newSate = newSate;
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
