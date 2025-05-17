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
    void receiveUserInput (UserInput userInput) throws RemoteException;
}
