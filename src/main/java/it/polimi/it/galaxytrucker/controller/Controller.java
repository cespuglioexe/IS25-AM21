package it.polimi.it.galaxytrucker.controller;

import it.polimi.it.galaxytrucker.model.exceptions.InvalidActionException;
import it.polimi.it.galaxytrucker.model.managers.GameManager;
import it.polimi.it.galaxytrucker.model.managers.Model;
// import it.polimi.it.galaxytrucker.networking.rmi.server.RMIServer;

import java.util.UUID;

public class Controller {
    private Model model; // MAKE FINAL!
    private final String nickname;
    private final int level;
    private final int playerNum;
    private int activePlayers;

    public Controller(int level, int playerNum, String nickname /*, RMIServer server */ ) {
        this.nickname = nickname;
        // this.model = new GameManager(level, playerNum, server, nickname);
        this.level = level;
        this.playerNum = playerNum;
        this.activePlayers = 0;
    }

    /**
     * Adds a player to the game associated with the controller
     *
     * @param playerName name of the player to be added
     */
    public UUID addPlayer (String playerName) throws InvalidActionException {
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

    public void placeComponentTile (UUID playerId, int col, int row ,int rotation) {
        model.placeComponentTile(playerId, row, col);
        for(int i=0; i<rotation; i++) {
            model.rotateComponentTile(playerId, row, col);
        }
    }

    public void requestNewComponentTile (UUID playerId) {
        model.drawComponentTile(playerId);
    }

    public void requestSavedComponentTiles (UUID playerId) {
        model.getSavedComponentTiles(playerId);
    }

    public void requestDiscardedComponentTiles (UUID playerId){
        // model.getDiscardedComponentTiles(playerId);
    }

    public void saveComponentTile (UUID playerId) {
        model.saveComponentTile(playerId);
    }

    public void discardComponentTile (UUID playerId) {
        model.discardComponentTile(playerId);
    }

    public void requestShipBoard (UUID playerId) {
        model.getPlayerShipBoard(playerId);
    }

    public void selectSavedComponentTile(UUID playerId, int index){
        System.out.println("Selected saved tile (controller)");
        model.selectSavedComponentTile(playerId, index);
    }

    public void selectDiscardedComponentTile(UUID playerId, int index){
        model.selectDiscardedComponentTile(playerId, index);
    }
    
    public void getCardPile (UUID playerId, int pileIndex) {
        model.getAdventureDeck().getStack(pileIndex);
    }

}
