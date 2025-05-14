package it.polimi.it.galaxytrucker.networking.server;

import it.polimi.it.galaxytrucker.controller.Controller;
import it.polimi.it.galaxytrucker.controller.GenericGameData;
import it.polimi.it.galaxytrucker.exceptions.GameFullException;
import it.polimi.it.galaxytrucker.networking.CommunicationType;
import it.polimi.it.galaxytrucker.networking.server.rmi.RMIServer;
import it.polimi.it.galaxytrucker.networking.server.rmi.RMIVirtualClient;
import it.polimi.it.galaxytrucker.view.CLI.ConsoleColors;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

public class Server implements RMIServer, Runnable, ServerInterface {
    String serverIPAddress = "localhost";

    final List<ClientHandler> clients = new ArrayList<>();
    private ServerSocket listenSocket;

    private final int BUILDING_TIMER_LENGTH = 10; // DA METTERE A 60 SECONDI

    private final Map<UUID, Controller> controllers = new HashMap<>();

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
            System.out.println("\n---------------------------------------------\n");

            while ((clientSocket = this.listenSocket.accept()) != null) {
                System.out.println(ConsoleColors.MAIN_SERVER_DEBUG + "incoming socket connection" + ConsoleColors.RESET);

                InputStreamReader inReader = new InputStreamReader(clientSocket.getInputStream());
                OutputStreamWriter outWriter = new OutputStreamWriter(clientSocket.getOutputStream());

                ClientHandler handler = new ClientHandler(this, CommunicationType.SOCKET, inReader, outWriter);

                synchronized (this.clients) {
                    clients.add(handler);
                }

                new Thread(handler::run).start();
            }
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Accepts an incoming connection from an RMI client.
     *
     * @param client The client to connect
     * @throws RemoteException If there is a problem
     */
    public void connect(RMIVirtualClient client) throws RemoteException {
        System.out.println(ConsoleColors.MAIN_SERVER_DEBUG + "incoming RMI connection" + ConsoleColors.RESET);

        ClientHandler handler = new ClientHandler(this, CommunicationType.RMI, client);
        synchronized (this.clients) {
            clients.add(handler);
        }
        client.setHandler(handler);
        new Thread(handler::run).start();
    }

    @Override
    public List<GenericGameData> getActiveGames() {
        List<GenericGameData> activeGames = new ArrayList<>();
        synchronized (this.controllers) {
            for (Controller controller : controllers.values()) {
                activeGames.add(controller.getGameData());
            }
        }
        return activeGames;
    }

    @Override
    public boolean setUsername(ClientHandler client, String username) {
        synchronized (this.clients) {
            return isUsernameUnique(username);
        }
    }

    private boolean isUsernameUnique(String username) {
        for (ClientHandler client : this.clients) {
            if (client.getUsername().equals(username))
                return false;
        }
        return true;
    }

    @Override
    public void addPlayerToGame(ClientHandler client, UUID gameId) throws GameFullException {
        synchronized (this.controllers) {
            controllers.get(gameId).addPlayer(client);
            client.setController(controllers.get(gameId));
        }
    }

    @Override
    public UUID createNewGame(int players, int level) {
        UUID gameId = UUID.randomUUID();
        synchronized (this.controllers) {
            controllers.put(gameId, new Controller(level, players, gameId));
        }
        return gameId;
    }
}
