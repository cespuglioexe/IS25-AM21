package it.polimi.it.galaxytrucker.networking.server.rmi;

import it.polimi.it.galaxytrucker.networking.VirtualClient;
import it.polimi.it.galaxytrucker.commands.servercommands.GameUpdate;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMIVirtualClient extends Remote, VirtualClient {
    @Override
    void sendMessageToClient(GameUpdate update) throws RemoteException;
    String getName() throws RemoteException;
}
