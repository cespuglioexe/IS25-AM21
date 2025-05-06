package it.polimi.it.galaxytrucker.networking.socket.server;

import it.polimi.it.galaxytrucker.networking.server.SocketVirtualView;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Questa classe rappresenta la logica del server implementata con tecnologia Socket.
 */
public class SocketServer {

    final ServerSocket listenSocket;
    final AdderController controller;
    final List<SocketClientHandler> clients = new ArrayList<>();

    public SocketServer(ServerSocket listenSocket) {
        this.listenSocket = listenSocket;
        this.controller = new AdderController();
    }

    private void runServer() throws IOException {
        Socket clientSocket = null;
        while ((clientSocket = this.listenSocket.accept()) != null) {
            InputStreamReader socketRx = new InputStreamReader(clientSocket.getInputStream());
            OutputStreamWriter socketTx = new OutputStreamWriter(clientSocket.getOutputStream());

            SocketClientHandler handler = new SocketClientHandler(
                    this.controller,
                    this,
                    new BufferedReader(socketRx),
                    new PrintWriter(socketTx)
            );

            synchronized (this.clients){
                clients.add(handler);
            }

            new Thread(() -> {
                try {
                    handler.runVirtualView();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }).start();
        }
    }

    public void broadcastUpdate(Integer value) {
        synchronized (this.clients) {
            for (var client : this.clients) {
                client.showUpdate(value);
            }
        }
    }

    public void broadcastError() {
        synchronized (this.clients) {
            for (SocketVirtualView client : clients) {
                client.reportError("already reset");
            }
        }
    }


    public static void main(String[] args) throws IOException {
        String host = args[0];
        int port = Integer.parseInt(args[1]);

        ServerSocket listenSocket = new ServerSocket(port);

        new SocketServer(listenSocket).runServer();

    }
}
