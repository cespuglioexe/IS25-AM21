package it.polimi.it.galaxytrucker.networking.messages;

import it.polimi.it.galaxytrucker.networking.rmi.server.RMIVirtualView;

import java.io.Serializable;
import java.util.List;

public class UserInput implements Serializable {
    private final UserInputType type;
    private final RMIVirtualView client;

    private final RequestType requestType;

    private final String serverName;
    private final String playerName;

    private final int gameLevel;
    private final int gamePlayers;
    private final int gameIndex;

    private final List<Integer> coords;
    private final int rotation;

    public UserInput(UserInputBuilder builder) {
        this.type = builder.type;
        this.client = builder.client;
        this.serverName = builder.serverName;
        this.playerName = builder.playerName;
        this.gameLevel = builder.gameLevel;
        this.gamePlayers = builder.gamePlayers;
        this.gameIndex = builder.gameIndex;
        this.requestType = builder.requestType;
        this.coords = builder.coords;
        this.rotation = builder.rotation;
    }

    public UserInputType getType() {
        return type;
    }

    public RMIVirtualView getClient() {
        return client;
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

    public int getGameIndex() {
        return gameIndex;
    }

    public RequestType getRequestType() {
        return requestType;
    }
    
    public List<Integer> getCoords() {
        return coords;
    }

    public int getRotation() { return rotation; }

    /**
     * Builder class for creating instances of the UserInput class.
     * This class provides a convenient way to configure all optional and required parameters
     * of a UserInput object before its creation.
     */
    public static class UserInputBuilder {
        // Required parameters
        private final UserInputType type;
        private final RMIVirtualView client;

        // Optional parameters
        private RequestType requestType = RequestType.EMPTY;

        private String serverName = "";
        private String playerName = "";

        private int gameLevel = 0;
        private int gamePlayers = 0;
        private int gameIndex = 0;
        
        private List<Integer> coords = null;

        private int rotation = 0;

        public UserInputBuilder(RMIVirtualView client, UserInputType type) {
            this.client = client;
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

        public UserInputBuilder setGameLevel(int gameLevel) {
            this.gameLevel = gameLevel;
            return this;
        }

        public UserInputBuilder setGamePlayers(int gamePlayers) {
            this.gamePlayers = gamePlayers;
            return this;
        }

        public UserInputBuilder setGameIndex(int gameIndex) {
            this.gameIndex = gameIndex;
            return this;
        }

        public UserInputBuilder setRequestType(RequestType requestType) {
            this.requestType = requestType;
            return this;
        }
        
        public UserInputBuilder setCoords(int x, int y) {
            this.coords = List.of(y, x);
            return this;
        }

        public UserInputBuilder setRotation(int rotation) {
            this.rotation = rotation;
            return this;
        }

        /*public UserInputBuilder setRotation(int x, int y) {
            this.rotation = rotation;
            return this;
        }*/

        public UserInput build() {
            return new UserInput(this);
        }
    }
}