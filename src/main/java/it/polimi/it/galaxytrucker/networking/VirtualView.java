package it.polimi.it.galaxytrucker.networking;

public interface VirtualView {
    void showUpdate (Integer number) throws Exception;
    void reportError (String message) throws Exception;
}
