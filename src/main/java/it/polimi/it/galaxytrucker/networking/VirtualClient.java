package it.polimi.it.galaxytrucker.networking;

import it.polimi.it.galaxytrucker.commands.servercommands.GameUpdate;


public interface VirtualClient {
    void sendMessageToClient (GameUpdate update) throws Exception;
}
