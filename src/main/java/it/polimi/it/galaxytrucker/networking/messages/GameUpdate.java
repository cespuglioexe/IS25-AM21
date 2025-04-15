package it.polimi.it.galaxytrucker.networking.messages;

import it.polimi.it.galaxytrucker.model.componenttiles.ComponentTile;
import it.polimi.it.galaxytrucker.model.componenttiles.TileData;

import javax.naming.TimeLimitExceededException;
import java.io.Serializable;

public class GameUpdate implements Serializable {
    private final GameUpdateType instructionType;
    private final String newSate;

    private final TileData newTile;

    public GameUpdate(GameUpdateBuilder builder) {
        this.instructionType = builder.instructionType;
        this.newSate = builder.newSate;
        this.newTile = builder.newTile;
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

    public static class GameUpdateBuilder {
        // Required fields
        private final GameUpdateType instructionType;

        // Optional fields
        private String newSate;
        private TileData newTile;

        public GameUpdateBuilder(GameUpdateType instructionType) {
            this.instructionType = instructionType;
        }

        public GameUpdateBuilder setNewSate(String newSate) {
            this.newSate = newSate;
            return this;
        }

        public GameUpdateBuilder setNewTile(ComponentTile newTile) {
            this.newTile = new TileData(
                    newTile.getRotation(),
                    newTile.getClass().toString(),
                    newTile.getTileEdges().get(0),
                    newTile.getTileEdges().get(1),
                    newTile.getTileEdges().get(2),
                    newTile.getTileEdges().get(3)
            );
            return this;
        }

        public GameUpdate build() {
            return new GameUpdate(this);
        }
    }
}
