package it.polimi.it.galaxytrucker.networking;

import it.polimi.it.galaxytrucker.networking.messages.GameUpdate;


public interface VirtualView {
    void sendMessageToClient (GameUpdate update) throws Exception;
}
