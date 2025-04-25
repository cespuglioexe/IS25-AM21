package it.polimi.it.galaxytrucker.networking.server.rmi;

import it.polimi.it.galaxytrucker.networking.VirtualView;
import it.polimi.it.galaxytrucker.networking.messages.GameUpdate;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMIVirtualView extends Remote, VirtualView {
    @Override
    void sendMessageToClient(GameUpdate update) throws RemoteException;
    String getName() throws RemoteException;
}
