package it.polimi.it.galaxytrucker.rmi.server;

import it.polimi.it.galaxytrucker.VirtualView;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMIVirtualView extends Remote, VirtualView {
    @Override
    void showUpdate(Integer number) throws RemoteException;
    @Override
    void reportError(String details) throws RemoteException;
}
