package it.polimi.it.galaxytrucker.networking.client.rmi;

import it.polimi.it.galaxytrucker.commands.UserInput;
import it.polimi.it.galaxytrucker.controller.Controller;
import it.polimi.it.galaxytrucker.controller.GenericGameData;
import it.polimi.it.galaxytrucker.exceptions.GameFullException;
import it.polimi.it.galaxytrucker.networking.VirtualServer;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.UUID;

public interface RMIVirtualServer extends Remote, VirtualServer {
    /**
     * Requests the server to set the player's name.
     *
     * @param username the {@code String} to set the username to.
     * @return  {@code true} if the name was successfully set on the server-side, or
     *          {@code false} if the name is already used bby another player.
     * @throws RemoteException if there is a communication error.
     */
    boolean setPlayerName(String username) throws RemoteException;

    /**
     * Requests the list of {@link Controller}s attached to active games.
     *
     * @return  a {@code List<Controller>} that includes {@link Controller}s for
     *          games waiting for players to join, as well as games that are ongoing
     *          and can't be joined.
     * @throws RemoteException if there is a communication error.
     */
    List<GenericGameData> getControllers() throws RemoteException;

    /**
     * Requests the server to add the player to the game with the specified {@code UUID}.
     *
     * @param gameId the {@code UUID} of the game to join.
     * @return the {@code UUID} generated for the player.
     * @throws RemoteException if there is a communication error.
     * @throws GameFullException if the chosen game is already full.
     */
    UUID addPlayerToGame(UUID gameId) throws RemoteException, GameFullException;

    /**
     * Requests the server to create a new game with the given parameters.
     *
     * @param players the number of players the game needs to start.
     * @param level the level of the game, which can either be 1 or 2.
     * @return the  {@code UUID} generated for the game.
     * @throws RemoteException
     */
    UUID newGame(int players, int level) throws RemoteException;

    // IMPLEMENTING

    void sendMessageToGame(UserInput input) throws RemoteException;

    void startBuildingPhaseTimer() throws RemoteException;

    // TO BE IMPLEMENTED

//
//    void startBuildingPhaseTimer(int gamIndex) throws RemoteException;
//    void connect(RMIClient rmiClient);
}
