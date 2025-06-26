package it.polimi.it.galaxytrucker.messages.clientmessages;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.it.galaxytrucker.messages.Message;
import it.polimi.it.galaxytrucker.model.utility.Coordinates;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * A data class used for communicating client actions to the server.
 *
 * @author giacomoamaducci
 * @version 1.1
 */
public class UserInput extends Message {
    private final UserInputType type;

    private final String serverName;
    private final String playerName;
    private final UUID playerUuid;

    private final int gameLevel;
    private final int gamePlayers;
    private final UUID gameId;
    private final int selectedTileIndex;
    private final int cardPileIndex;

    private final List<List<Coordinates>> componentsForActivation;
    private final HashMap<Integer,Coordinates> acceptedCargo;
    private final HashMap<String,Coordinates> placedCrewmate;
    private final List<Coordinates> removedCrewmate;
    private final boolean creditChoice;
    private final boolean participation;
    private final int participationChoice;

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
            @JsonProperty("rotation") int rotation,
            @JsonProperty("componentsForActivation") List<List<Coordinates>> componentsForActivation,
            @JsonProperty("acceptedCargo") HashMap<Integer,Coordinates> acceptedCargo,
            @JsonProperty("creditChoice") boolean creditChoice,
            @JsonProperty("removedCrewmate") List<Coordinates> removedCrewmate,
            @JsonProperty("participation") boolean participation,
            @JsonProperty("participationChoice") int participationChoice,
            @JsonProperty("placedCrewmate") HashMap<String,Coordinates> placedCrewmate){
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
        this.componentsForActivation = componentsForActivation;
        this.acceptedCargo = acceptedCargo;
        this.creditChoice = creditChoice;
        this.removedCrewmate = removedCrewmate;
        this.participation = participation;
        this.participationChoice = participationChoice;
        this.placedCrewmate = placedCrewmate;
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
        this.componentsForActivation = builder.componentsForActivation;
        this.acceptedCargo = builder.acceptedCargo;
        this.creditChoice = builder.creditChoice;
        this.removedCrewmate = builder.removedCrewmate;
        this.participation = builder.participation;
        this.participationChoice = builder.participationChoice;
        this.placedCrewmate = builder.placedCrewmate;
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

    public List<List<Coordinates>> getComponentsForActivation() {
        return componentsForActivation;
    }

    public HashMap<Integer, Coordinates> getAcceptedCargo() {
        return acceptedCargo;
    }

    public boolean getCreditChoice() {
        return creditChoice;
    }

    public List<Coordinates> getRemovedCrewmate() {
        return removedCrewmate;
    }

    public boolean getParticipation() {
        return participation;
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

    public HashMap<String, Coordinates> getPlacedCrewmate() {
        return placedCrewmate;
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

    public int getParticipationChoice() {
        return participationChoice;
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
        private List<List<Coordinates>> componentsForActivation = new ArrayList<>();
        private HashMap<Integer,Coordinates> acceptedCargo = new HashMap<>();
        private boolean creditChoice = false;
        private List<Coordinates> removedCrewmate = new ArrayList<>();
        private HashMap<String,Coordinates> placedCrewmate = new HashMap<>();
        private boolean participation = false;
        private int participationChoice = 0;

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

        public UserInputBuilder setPlacedCrewmate(HashMap<String, Coordinates> placedCrewmate) {
            this.placedCrewmate = placedCrewmate;
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

        public UserInputBuilder setComponentsForActivation(List<List<Coordinates>> componentsForActivation) {
            this.componentsForActivation = componentsForActivation;
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

        public UserInputBuilder setAcceptedCargo(HashMap<Integer, Coordinates> acceptedCargo) {
            this.acceptedCargo = acceptedCargo;
            return this;
        }

        public UserInputBuilder setCreditChoice(boolean creditChoice) {
            this.creditChoice = creditChoice;
            return this;
        }

        public UserInputBuilder setRemovedCrewmate(List<Coordinates> removedCrewmate) {
            this.removedCrewmate = removedCrewmate;
            return this;
        }

        public UserInputBuilder setParticipation(boolean participation) {
            this.participation = participation;
            return this;
        }

        public UserInputBuilder setParticipationChoice(int choice) {
            this.participationChoice = choice;
            return this;
        }

        public UserInput build() {
            return new UserInput(this);
        }
    }
}