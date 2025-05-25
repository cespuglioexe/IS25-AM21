package it.polimi.it.galaxytrucker.networking.server.rmi;

import it.polimi.it.galaxytrucker.messages.servermessages.GameError;
import it.polimi.it.galaxytrucker.networking.VirtualClient;
import it.polimi.it.galaxytrucker.messages.servermessages.GameUpdate;
import it.polimi.it.galaxytrucker.networking.client.rmi.RMIVirtualServer;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Represents a virtual client accessible via RMI.
 * This interface extends both {@link java.rmi.Remote} to enable remote method calls
 * and {@link VirtualClient} to provide the specific methods callable remotely by the
 * server. Implementations of this interface run on the client side but are exposed
 * to the server for invoking client-side operations related to game updates,
 * server handler registration, and error reporting.
 *
 * @author giacomoamaducci
 * @version 1.3
 */
public interface RMIVirtualClient extends Remote, VirtualClient {

    /**
     * Sends a game update from the server to this virtual client.
     *
     * @param update The {@link GameUpdate} object containing the information
     * to be sent to the client.
     * @throws RemoteException If a communication error occurs during the
     * remote method invocation.
     */
    void sendMessageToClient(GameUpdate update) throws RemoteException;

    /**
     * Sets the remote handler (server object) for this virtual client.
     * This allows the client implementation to hold a reference to the server
     * object and make remote calls back to the server if needed.
     *
     * @param handler The remote reference to the {@link RMIVirtualServer}
     * instance handling this client.
     * @throws RemoteException If a communication error occurs during the
     * remote method invocation.
     */
    void setHandler(RMIVirtualServer handler) throws RemoteException;

    /**
     * Reports a game-related error from the server to this virtual client.
     * This method is used by the server to inform the client about errors
     * or exceptional conditions that occurred on the server side.
     *
     * @param error The {@link GameError} object describing the error.
     * @throws RemoteException If a communication error occurs during the
     * remote method invocation.
     */
    void reportErrorToClient(GameError error) throws RemoteException;
}
