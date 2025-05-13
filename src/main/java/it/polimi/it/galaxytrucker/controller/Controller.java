package it.polimi.it.galaxytrucker.controller;

import it.polimi.it.galaxytrucker.listeners.Observable;
import it.polimi.it.galaxytrucker.exceptions.GameFullException;
import it.polimi.it.galaxytrucker.model.managers.GameManager;
import it.polimi.it.galaxytrucker.model.managers.Model;
import it.polimi.it.galaxytrucker.model.managers.Player;
import it.polimi.it.galaxytrucker.model.managers.ShipManager;
import it.polimi.it.galaxytrucker.model.utility.Color;
import it.polimi.it.galaxytrucker.networking.server.ClientHandler;
import it.polimi.it.galaxytrucker.view.CLI.ConsoleColors;
// import it.polimi.it.galaxytrucker.networking.rmi.server.RMIServer;

import java.util.*;

public class Controller implements ControllerInterface {
    private final Model model;
    private final UUID uuid;
    private final int level;
    private final int playerNum;
    private int activePlayers;

    private final List<Color> playerColors = new ArrayList<>();

    public Controller(int level, int playerNum, UUID uuid) {
        this.uuid = uuid;
        this.model = new GameManager(level, playerNum);
        this.level = level;
        this.playerNum = playerNum;
        this.activePlayers = 0;

        for (Color color : Color.values()) {
            playerColors.add(color);
        }
    }

    public UUID addPlayer (ClientHandler client) throws GameFullException {
        if (activePlayers >= playerNum) {
            System.out.println(ConsoleColors.CONTROLLER_DEBUG + "Player '" + client.getUsername() + "' tried to join full game" + ConsoleColors.RESET);
            throw new GameFullException("Game " + uuid + " is already full");
        }

        UUID newId = UUID.randomUUID();
        Color playerColor = playerColors.remove(new Random().nextInt(playerColors.size()));

        Player newPlayer = new Player(newId, client.getUsername(), playerColor, new ShipManager(level));

        ((Observable) model).addListener(client);
        (newPlayer).addListener(client);

        model.addPlayer(newPlayer);
        activePlayers++;

        return newId;
    }


    /**
     * Returns the unique UUID of the controller
     * @return a {@code UUID} representing the identity of the controller
     */
    public UUID getUuid() {
        return uuid;
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
        return new GenericGameData(level, playerNum, activePlayers, uuid);
    }

    @Override
    public void placeComponentTile(UUID playerId, int col, int row, int rotation) {
        model.placeComponentTile(playerId, row, col, rotation);
    }

    @Override
    public void requestNewComponentTile(UUID playerId) {
        model.drawComponentTile(playerId);
    }

    @Override
    public void requestSavedComponentTiles (UUID playerId) {
        model.getSavedComponentTiles(playerId);
    }

    @Override
    public void requestDiscardedComponentTiles (UUID playerId){
        model.getDiscardedComponentTiles(playerId);
    }

    @Override
    public void saveComponentTile (UUID playerId) {
        model.saveComponentTile(playerId);
    }

    @Override
    public void discardComponentTile (UUID playerId) {
        model.discardComponentTile(playerId);
    }

    @Override
    public void requestShipBoard (UUID playerId) {
        model.getPlayerShipBoard(playerId);
    }

    @Override
    public void selectSavedComponentTile(UUID playerId, int index){
        model.selectSavedComponentTile(playerId, index);
    }

    @Override
    public void selectDiscardedComponentTile(UUID playerId, int index){
        model.selectDiscardedComponentTile(playerId, index);
    }

    @Override
    public void getCardPile (UUID playerId, int pileIndex) {
        model.getAdventureDeck().getStack(pileIndex);
    }

    @Override
    public UUID getControllerUuid() {
        return uuid;
    }

    @Override
    public void startBuildPhaseTimer() {
        model.startBuildPhaseTimer();
    }

}
