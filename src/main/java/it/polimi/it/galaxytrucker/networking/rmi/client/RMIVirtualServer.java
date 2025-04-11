package it.polimi.it.galaxytrucker.networking.rmi.client;

import it.polimi.it.galaxytrucker.networking.VirtualServer;
import it.polimi.it.galaxytrucker.networking.rmi.server.RMIVirtualView;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMIVirtualServer extends Remote, VirtualServer {
    void connect(RMIVirtualView client) throws RemoteException;

    void setUsername(RMIVirtualView client, String username) throws RemoteException, Exception;

    @Override
    void add (Integer number) throws RemoteException;

    @Override
    void reset() throws RemoteException;

}
