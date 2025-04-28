package it.polimi.it.galaxytrucker.networking.server.socket;


import it.polimi.it.galaxytrucker.networking.messages.GameUpdate;
import it.polimi.it.galaxytrucker.networking.server.Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Questa classe implementa la logica di interazione tra il server e un singolo client.
 */
public class SocketClientHandler implements SocketVirtualView {

    final Server server;
    final BufferedReader input;
    final PrintWriter output;

    public SocketClientHandler(Server server, BufferedReader input, PrintWriter output) {
        this.server = server;
        this.input = input;
        this.output = output;
    }

    //comunicazione dal client al server
    public void runVirtualView() throws IOException {
        String line;

        //TODO. Attenzione, qui non sto sfruttando il paradigma object oriented!
        while ((line = input.readLine()) != null) {
            // Reflection
            // Protocollo di serializzazione
            switch (line) {
                case "add" -> {
                    System.err.println("add request received");
                }
                case "reset" -> {
                    System.err.println("reset request received");
                }
                default -> System.err.println("[INVALID MESSAGE]");
            }
        }
    }

    @Override
    public void sendMessageToClient(GameUpdate update) throws Exception {

    }
}
