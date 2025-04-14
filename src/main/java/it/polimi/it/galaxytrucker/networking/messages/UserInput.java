package it.polimi.it.galaxytrucker.networking.messages;

import it.polimi.it.galaxytrucker.networking.rmi.server.RMIVirtualView;

public class UserInput {
    private final UserInputType type;
    private final RMIVirtualView client;

    private final String serverName;
    private final String playerName;

    private final int gameLevel;
    private final int gamePlayers;
    private final int gameIndex;

    public UserInput(UserInputBuilder builder) {
        this.type = builder.type;
        this.client = builder.client;
        this.serverName = builder.serverName;
        this.playerName = builder.playerName;
        this.gameLevel = builder.gameLevel;
        this.gamePlayers = builder.gamePlayers;
        this.gameIndex = builder.gameIndex;
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

    /**
     * Builder class for creating instances of the UserInput class.
     * This class provides a convenient way to configure all optional and required parameters
     * of a UserInput object before its creation.
     */
    public static class UserInputBuilder {
        // Required parameters
        private UserInputType type;
        private RMIVirtualView client;

        // Optional parameters
        private String serverName = "";
        private String playerName = "";

        private int gameLevel = 0;
        private int gamePlayers = 0;
        private int gameIndex = 0;

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

        public UserInput build() {
            return new UserInput(this);
        }
    }
}