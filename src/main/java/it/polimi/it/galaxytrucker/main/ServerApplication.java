package it.polimi.it.galaxytrucker.main;

import it.polimi.it.galaxytrucker.networking.server.Server;

/**
 * Handles startup for the server
 */
public class ServerApplication {
    public static void main(String[] args) {
        Server server = Server.getInstance();

        server.run();
    }
}
