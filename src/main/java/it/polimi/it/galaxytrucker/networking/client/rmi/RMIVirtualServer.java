package it.polimi.it.galaxytrucker.networking.client.rmi;

import it.polimi.it.galaxytrucker.messages.Message;
import it.polimi.it.galaxytrucker.messages.clientmessages.UserInput;
import it.polimi.it.galaxytrucker.networking.VirtualServer;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Represents a remote interface for server-side operations that can be
 * invoked by an RMI client.
 *
 * @author giacomoamaducci
 * @version 2.0
 */
public interface RMIVirtualServer extends Remote, VirtualServer {
    /**
     * Passes a {@link UserInput} object from the client to the server
     * to be interpreted and converted into relevant game actions.
     *
     * @param userInput the {@link UserInput} object containing the client's input.
     * @throws RemoteException if a communication error occurs during the remote method invocation.
     */
    void receiveUserInput (Message userInput) throws RemoteException;
}
