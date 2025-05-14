package it.polimi.it.galaxytrucker.networking.server.rmi;

import it.polimi.it.galaxytrucker.commands.GameError;
import it.polimi.it.galaxytrucker.commands.UserInput;
import it.polimi.it.galaxytrucker.networking.VirtualClient;
import it.polimi.it.galaxytrucker.commands.servercommands.GameUpdate;
import it.polimi.it.galaxytrucker.networking.client.rmi.RMIVirtualServer;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.concurrent.BlockingQueue;

public interface RMIVirtualClient extends Remote, VirtualClient {
    @Override
    void sendMessageToClient(GameUpdate update) throws RemoteException;

    void setHandler(RMIVirtualServer handler) throws RemoteException;

    void reportErrorToClient(GameError error) throws RemoteException;
}
