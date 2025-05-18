package it.polimi.it.galaxytrucker.main;

import it.polimi.it.galaxytrucker.networking.server.Server;

import java.rmi.RemoteException;

/**
 * Handles startup for the server
 */
public class ServerApplication {
    public static void main(String[] args) {
        Server server;
        try {
            server = new Server();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        server.run();
    }
}
