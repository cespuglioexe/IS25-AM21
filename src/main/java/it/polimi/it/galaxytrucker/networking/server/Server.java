package it.polimi.it.galaxytrucker.networking.server;

import it.polimi.it.galaxytrucker.controller.GenericGameData;
import it.polimi.it.galaxytrucker.model.exceptions.InvalidActionException;
import it.polimi.it.galaxytrucker.networking.messages.UserInput;
import it.polimi.it.galaxytrucker.networking.server.rmi.RMIServer;
import it.polimi.it.galaxytrucker.networking.server.rmi.RMIVirtualView;
import it.polimi.it.galaxytrucker.networking.server.socket.SocketClientHandler;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Server implements RMIServer, Runnable {

    private final static Server instance = new Server();

    String serverIPAddress = "localhost";

    final List<SocketClientHandler> clients = new ArrayList<>();
    private ServerSocket listenSocket;

    protected Server() {
        super();
    }

    public void run () {

        // Setup RMI server

        System.setProperty("java.rmi.server.hostname", serverIPAddress);
        Registry registry;
        try {
            registry = LocateRegistry.createRegistry(1234);
            registry.rebind("server", this);
            UnicastRemoteObject.exportObject(this, 1234);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }

        System.out.println("RMI server ready. Registry: " + registry + "\n>\tName: " + serverIPAddress + "\n>\tPort: " + 1234);

        // Setup socket server

        try {
            this.listenSocket = new ServerSocket(2345);
            Socket clientSocket = null;

            System.out.println("Socket server ready. Listening on: " + listenSocket.getInetAddress().getHostAddress());

            while ((clientSocket = this.listenSocket.accept()) != null) {
                System.out.println("Accepted connection from: " + clientSocket.getInetAddress().getHostAddress());

                InputStreamReader socketRx = new InputStreamReader(clientSocket.getInputStream());
                OutputStreamWriter socketTx = new OutputStreamWriter(clientSocket.getOutputStream());

                SocketClientHandler handler = new SocketClientHandler(
                        this,
                        new BufferedReader(socketRx),
                        new PrintWriter(socketTx)
                );

                synchronized (this.clients) {
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
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void sendMessageToGame(UUID playerId, UserInput input, int gameIndex) throws RemoteException {

    }

    @Override
    public UUID addPlayerToGame(RMIVirtualView client, int gameIndex) throws RemoteException {
        return null;
    }

    @Override
    public boolean checkUsernameIsUnique(RMIVirtualView client, String username) throws RemoteException, Exception {
        return false;
    }

    @Override
    public List<GenericGameData> getControllers() throws RemoteException {
        return List.of();
    }

    public void connect(RMIVirtualView client) throws RemoteException {
        System.out.println("\nIncoming connection");
    }

    @Override
    public UUID addPlayerToGame(RMIVirtualView client, String gameNickname) throws RemoteException, InvalidActionException {
        return null;
    }

    @Override
    public void newGame(String gameNickname, int players, int level) throws RemoteException {

    }

    @Override
    public void startBuildingPhaseTimer(int gamIndex) throws RemoteException {

    }

    public static Server getInstance() {
        return instance;
    }
}
