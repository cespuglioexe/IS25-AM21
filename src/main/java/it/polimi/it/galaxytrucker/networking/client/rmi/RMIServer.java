package it.polimi.it.galaxytrucker.networking.client.rmi;

import it.polimi.it.galaxytrucker.networking.server.rmi.RMIVirtualClient;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Represents the main remote interface for the RMI server, used by clients
 * to establish an initial connection. Implementations of this interface are
 * typically the first remote objects looked up by clients.
 *
 * @author giacomoamaducci
 * @version 1.0
 */
public interface RMIServer extends Remote {
    /**
     * Allows an RMI client to establish a connection to the RMI server
     * implementing this method. The client passes a remote reference to itself
     * (an {@link RMIVirtualClient} implementation) to the server, allowing
     * the server to invoke methods on the client remotely.
     *
     * @param client The {@link RMIVirtualClient} that acts as a remote reference
     * to the connected client. This reference is used by the server
     * to send updates or errors back to the client.
     * @throws RemoteException if a communication problem occurs during the remote method invocation.
     */
    void connect(RMIVirtualClient client) throws RemoteException;
}
