package it.polimi.it.galaxytrucker.networking;

import it.polimi.it.galaxytrucker.commands.servercommands.GameUpdate;

import java.rmi.RemoteException;


public interface VirtualClient {
    void sendMessageToClient (GameUpdate update) throws Exception;
}
