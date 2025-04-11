package it.polimi.it.galaxytrucker.networking.rmi.server;

import it.polimi.it.galaxytrucker.networking.VirtualView;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface RMIVirtualView extends Remote, VirtualView {
    @Override
    void showUpdate(Integer number) throws RemoteException;
    @Override
    void reportError(String details) throws RemoteException;

    String getName() throws RemoteException;
}
