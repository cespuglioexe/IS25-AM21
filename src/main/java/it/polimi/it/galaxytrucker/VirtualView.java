package it.polimi.it.galaxytrucker;

public interface VirtualView {
    void showUpdate (Integer number) throws Exception;
    void reportError (String message) throws Exception;
}
