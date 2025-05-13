package it.polimi.it.galaxytrucker.networking.client.socket;

import it.polimi.it.galaxytrucker.commands.servercommands.GameUpdate;
import it.polimi.it.galaxytrucker.networking.server.socket.SocketVirtualClient;
import it.polimi.it.galaxytrucker.view.View;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

/**
 * Questa classe rappresenta la logica del client implementata con tecnologia Socket.
 */
public class SocketClient implements SocketVirtualClient {
    final BufferedReader input;
    final SocketServerHandler output;
    
    private final View view;

    public SocketClient(BufferedReader input, BufferedWriter output, View view) {
        this.input = input;
        this.output = new SocketServerHandler(output);
        this.view = view;
    }

    public void run() {
        new Thread(() -> {
            try {
                runVirtualServer();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).start();
        runCli();
    }

    // comunicazione dal server al client
    private void runVirtualServer() throws IOException {
        String line;
        while ((line = input.readLine()) != null) {
            // Da notare che i metodi sono chiamati nello stesso modo del server
            switch (line) {
                case "update" -> this.showUpdate(Integer.parseInt(input.readLine()));
                case "error" -> this.reportError(input.readLine());
                default -> System.err.println("[INVALID MESSAGE]");
            }
        }
    }

    private void runCli()  {
        System.out.println("Socket client started.");
    }

    public void showUpdate(Integer number) {
        // TODO. Attenzione, questo può causare data race con il thread dell'interfaccia o un altro thread!
        System.out.print("\n= " + number + "\n> ");
    }

    public void reportError(String details) {
        // TODO. Attenzione, questo può causare data race con il thread dell'interfaccia o un altro thread!
        System.err.print("\n[ERROR] " + details + "\n> ");
    }

    @Override
    public void sendMessageToClient(GameUpdate update) throws Exception {

    }
}
