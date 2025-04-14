package it.polimi.it.galaxytrucker.networking.rmi.client;

import it.polimi.it.galaxytrucker.controller.GenericGameData;
import it.polimi.it.galaxytrucker.networking.VirtualServer;
import it.polimi.it.galaxytrucker.networking.messages.ClientInstruction;
import it.polimi.it.galaxytrucker.networking.rmi.server.RMIVirtualView;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.UUID;

public interface RMIVirtualServer extends Remote, VirtualServer {
    UUID addPlayerToGame(RMIVirtualView client, int gameIndex) throws RemoteException;
    void sendMessageToGame (ClientInstruction instruction) throws RemoteException;
    boolean checkUsernameIsUnique(RMIVirtualView client, String username) throws RemoteException, Exception;

    List<GenericGameData> getControllers() throws RemoteException;

    void connect(RMIVirtualView client) throws RemoteException;

    UUID addPlayerToGame(RMIVirtualView client, String gameNickname) throws RemoteException;

    void newGame(String gameNickname, int players, int level) throws RemoteException;
}
