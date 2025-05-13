package it.polimi.it.galaxytrucker.networking.server.rmi;

import it.polimi.it.galaxytrucker.networking.client.rmi.RMIVirtualServer;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMIServer extends Remote {
    void connect(RMIVirtualClient client) throws RemoteException;
}
