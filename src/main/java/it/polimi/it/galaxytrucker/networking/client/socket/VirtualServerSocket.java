package it.polimi.it.galaxytrucker.networking.client.socket;


import it.polimi.it.galaxytrucker.networking.VirtualServer;

/**
 * Questa interfaccia specializza l'interfaccia VirtualServer per la tecnologia Socket
 */
public interface VirtualServerSocket extends VirtualServer {
    public void add(Integer number);

    public void reset();
}
