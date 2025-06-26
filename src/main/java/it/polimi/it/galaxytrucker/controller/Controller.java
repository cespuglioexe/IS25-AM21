package it.polimi.it.galaxytrucker.controller;

import it.polimi.it.galaxytrucker.listeners.Observable;
import it.polimi.it.galaxytrucker.exceptions.GameFullException;
import it.polimi.it.galaxytrucker.model.crewmates.Alien;
import it.polimi.it.galaxytrucker.model.crewmates.Crewmate;
import it.polimi.it.galaxytrucker.model.crewmates.Human;
import it.polimi.it.galaxytrucker.model.managers.GameManager;
import it.polimi.it.galaxytrucker.model.managers.Model;
import it.polimi.it.galaxytrucker.model.managers.Player;
import it.polimi.it.galaxytrucker.model.managers.ShipManager;
import it.polimi.it.galaxytrucker.model.utility.AlienType;
import it.polimi.it.galaxytrucker.model.utility.Color;
import it.polimi.it.galaxytrucker.model.utility.Coordinates;
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
        System.out.println(ConsoleColors.CONTROLLER_DEBUG + "NEW GAME OF LEVEL: " + level);
        this.uuid = uuid;
        this.model = new GameManager(level, playerNum);
        this.level = level;
        this.playerNum = playerNum;
        this.activePlayers = 0;

        playerColors.addAll(Arrays.asList(Color.values()));
    }

    public void addPlayer (ClientHandler client) throws GameFullException {
        if (activePlayers >= playerNum) {
            System.out.println(ConsoleColors.CONTROLLER_DEBUG + "Player '" + client.getUsername() + "' tried to join full game" + ConsoleColors.RESET);
            throw new GameFullException("Game " + uuid + " is already full");
        }

        Color playerColor = playerColors.remove(new Random().nextInt(playerColors.size()));

        Player newPlayer = new Player(client.getUuid(), client.getUsername(), playerColor, new ShipManager(level, playerColor));

        ((Observable) model).addListener(client);
        model.getFlightBoard().addListener(client);
        (newPlayer).addListener(client);

        model.addPlayer(newPlayer);
        activePlayers++;
    }


    /**
     * Returns the unique UUID of the controller
     * @return a {@code UUID} representing the identity of the controller
     */
    public UUID getUuid() {
        return uuid;
    }

    @Override
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
    public synchronized void placeComponentTile(UUID playerId, int col, int row, int rotation) {
        model.placeComponentTile(playerId, row, col, rotation);
    }

    @Override
    public synchronized void requestNewComponentTile(UUID playerId) {
        model.drawComponentTile(playerId);
    }

    @Override
    public synchronized void requestSavedComponentTiles (UUID playerId) {
        model.getSavedComponentTiles(playerId);
    }

    @Override
    public synchronized void requestDiscardedComponentTiles (UUID playerId){
        model.getDiscardedComponentTiles(playerId);
    }

    @Override
    public synchronized void saveComponentTile (UUID playerId) {
        model.saveComponentTile(playerId);
    }

    @Override
    public synchronized void discardComponentTile (UUID playerId) {
        model.discardComponentTile(playerId);
    }

    @Override
    public synchronized void requestShipBoard (UUID playerId) {
        model.getPlayerShipBoard(playerId);
    }

    @Override
    public synchronized void selectSavedComponentTile(UUID playerId, int index){
        model.selectSavedComponentTile(playerId, index);
    }

    @Override
    public synchronized void selectDiscardedComponentTile(UUID playerId, int index){
        model.selectDiscardedComponentTile(playerId, index);
    }

    @Override
    public synchronized UUID getControllerUuid() {
        return uuid;
    }

    @Override
    public void activateCannon(UUID playerId, List<List<Coordinates>> activationHashmap) {
        model.activateCannon(playerId,activationHashmap);
    }

    @Override
    public void activateEngine(UUID playerId, List<List<Coordinates>> activationHashmap) {
        model.activateEngine(playerId,activationHashmap);
    }

    @Override
    public void activateShield(UUID playerId, List<List<Coordinates>> activationHashmap) {
        model.activateShield(playerId,activationHashmap);
    }

    @Override
    public void manageAcceptedCargo(UUID playerId,HashMap<Integer,Coordinates> acceptedCargo) {
        model.manageAcceptedCargo(playerId,acceptedCargo);
    }

    @Override
    public void manageCreditChoice(UUID playerId,boolean creditChoice) {
        model.manageCreditChoice(playerId,creditChoice);
    }

    @Override
    public void manageRemovedCrewmate(UUID  playerId, List<Coordinates> removedCrewmate){
        model.manageRemovedCrewmate(playerId,removedCrewmate);
    }

    @Override
    public void manageParticipation(UUID  playerId, boolean participation, int choice){
        model.manageParticipation(playerId, participation, choice);
    }

    @Override
    public synchronized void startBuildPhaseTimer() {
        model.startBuildPhaseTimer();
    }

    @Override
    public void disconnectPlayer(UUID clientUuid) {
        // TODO: mark player as disconnected
    }

    @Override
    public void endPlayerBuilding(UUID clientUuid) {
        model.finishBuilding(clientUuid);
    }

    @Override
    public void addCrewmateToCabin(UUID playerID, HashMap<String, Coordinates> placedCrewmate) {
        for (HashMap.Entry<String, Coordinates> entry : placedCrewmate.entrySet()) {
            System.out.println(entry.getValue().getRow() + " " + entry.getValue().getColumn());
            switch (entry.getKey()) {
                case "HUMAN"        -> model.addCrewmateToCabin(playerID, entry.getValue().getRow(), entry.getValue().getColumn(), new Human());
                case "PURPLEALIEN"  -> model.addCrewmateToCabin(playerID, entry.getValue().getRow(), entry.getValue().getColumn(), new Alien(AlienType.PURPLEALIEN));
                case "BROWNALIEN"   -> model.addCrewmateToCabin(playerID, entry.getValue().getRow(), entry.getValue().getColumn(), new Alien(AlienType.BROWNALIEN));
            }
        }
    }

    @Override
    public void removeComponentTile(UUID playerId, int col, int row){
        model.deleteComponentTile(playerId, row, col);
    }
}
