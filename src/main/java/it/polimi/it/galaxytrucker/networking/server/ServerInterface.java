package it.polimi.it.galaxytrucker.networking.server;

import it.polimi.it.galaxytrucker.controller.GenericGameData;

import java.rmi.RemoteException;
import java.util.List;
import java.util.UUID;

public interface ServerInterface {
    List<GenericGameData> getActiveGames();
    boolean setUsername(ClientHandler client, String username);
    UUID addPlayerToGame(ClientHandler client, UUID gameId);

    UUID createNewGame(int players, int level);
}
