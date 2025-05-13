package it.polimi.it.galaxytrucker.networking.server;

import it.polimi.it.galaxytrucker.controller.Controller;
import it.polimi.it.galaxytrucker.controller.GenericGameData;
import it.polimi.it.galaxytrucker.networking.server.rmi.RMIClientHandler;
import it.polimi.it.galaxytrucker.networking.server.rmi.RMIServer;
import it.polimi.it.galaxytrucker.networking.server.rmi.RMIVirtualClient;
import it.polimi.it.galaxytrucker.networking.server.socket.SocketClientHandler;
import it.polimi.it.galaxytrucker.view.CLI.ConsoleColors;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

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

                SocketClientHandler handler = new SocketClientHandler(
                        this,
                        new BufferedReader(inReader),
                        new PrintWriter(outWriter)
                );

                synchronized (this.clients) {
                    clients.add(handler);
                }

                new Thread(() -> {
                    try {
                        // RUN THE CLIENT HANDLER
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

    /**
     * Accepts an incoming connection from an RMI client.
     *
     * @param client The client to connect
     * @throws RemoteException If there is a problem
     */
    public void connect(RMIVirtualClient client) throws RemoteException {
        System.out.println(ConsoleColors.MAIN_SERVER_DEBUG + "incoming RMI connection" + ConsoleColors.RESET);

        RMIClientHandler handler = new RMIClientHandler(this, client);
        synchronized (this.clients) {
            clients.add(handler);
        }
        client.setHandler(handler);
        new Thread(() -> {
            try {
                handler.run();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).start();
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
        boolean success = false;
        synchronized (this.clients) {
            if (isUsernameUnique(username)) {
                client.setUsername(username);
                success = true;
            }
        }
        return success;
    }

    private boolean isUsernameUnique(String username) {
        for (ClientHandler client : this.clients) {
            if (client.getUsername().equals(username))
                return false;
        }
        return true;
    }

    @Override
    public UUID addPlayerToGame(ClientHandler client, UUID gameId) {
        UUID newPlayerId;
        synchronized (this.controllers) {
            newPlayerId = controllers.get(gameId).addPlayer(client);
            client.setUuid(newPlayerId);
            client.setController(controllers.get(gameId));
        }
        return newPlayerId;
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
