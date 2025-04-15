package it.polimi.it.galaxytrucker.controller;

import it.polimi.it.galaxytrucker.model.exceptions.InvalidActionException;
import it.polimi.it.galaxytrucker.model.managers.GameManager;
import it.polimi.it.galaxytrucker.networking.messages.ClientInstruction;
import it.polimi.it.galaxytrucker.model.managers.Model;
import it.polimi.it.galaxytrucker.networking.rmi.server.RMIServer;

import java.rmi.RemoteException;
import java.util.UUID;

public class Controller {
    private final Model model;
    private final String nickname;
    private final int level;
    private final int playerNum;
    private int activePlayers;

    public Controller(int level, int playerNum, String nickname, RMIServer server) {
        System.out.println("Starting controller");
        this.nickname = nickname;
        this.model = new GameManager(level, playerNum, server, nickname);
        this.level = level;
        this.playerNum = playerNum;
        this.activePlayers = 0;
    }

    /**
     * Used to send a message to the controller
     * @param instruction instruction to send to the controller
     */
    public void receiveMessage(ClientInstruction instruction) {
        System.out.println("Received instruction: " + instruction.getInstructionType().toString());
    }

    /**
     * Adds a player to the game associated with the controller
     *
     * @param playerName name of the player to be added
     */
    public UUID addPlayer (String playerName) throws InvalidActionException {
        System.out.println("Adding player: " + playerName + "(controller)");
        UUID newId;

        newId = model.addPlayer(playerName);

        activePlayers++;
        return newId;
    }

    /**
     * Returns the nickname of the controller
     * @return a {@code String} representing the nickname of the controller
     */
    public String getNickname() {
        return nickname;
    }

    public int getLevel() {
        return level;
    }

    public int getPlayerNum() {
        return playerNum;
    }

    public int getActivePlayers() {
        return activePlayers;
    }

    public GenericGameData getGameData() {
        return new GenericGameData(level, playerNum, activePlayers);
    }

    public void placeComponentTile (UUID playerId, int col, int row) {
        model.placeComponentTile(playerId, null, row, col);
    }
}
