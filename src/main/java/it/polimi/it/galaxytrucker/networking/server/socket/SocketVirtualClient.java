package it.polimi.it.galaxytrucker.networking.server.socket;


import it.polimi.it.galaxytrucker.networking.VirtualClient;

/**
 * Represents a virtual client specifically for Socket-based connections.
 * This interface is primarily for structural and aesthetic purposes, allowing
 * Socket clients to be treated similarly to other virtual client types
 * without requiring additional methods specific to sockets within this
 * interface itself.
 *
 * @author giacomoamaducci
 * @version 1.0
 */
public interface SocketVirtualClient extends VirtualClient {
    // This interface serves mainly as a type marker.
}
