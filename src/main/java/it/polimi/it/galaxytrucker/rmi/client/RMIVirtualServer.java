package it.polimi.it.galaxytrucker.rmi.client;

import it.polimi.it.galaxytrucker.VirtualServer;
import it.polimi.it.galaxytrucker.rmi.server.RMIVirtualView;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMIVirtualServer extends Remote, VirtualServer {
    void connect(RMIVirtualView client) throws RemoteException;

    @Override
    void add (Integer number) throws RemoteException;

    @Override
    void reset() throws RemoteException;
}
