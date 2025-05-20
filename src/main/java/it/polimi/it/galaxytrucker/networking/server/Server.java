package it.polimi.it.galaxytrucker.networking.server;

import it.polimi.it.galaxytrucker.controller.Controller;
import it.polimi.it.galaxytrucker.controller.GenericGameData;
import it.polimi.it.galaxytrucker.exceptions.GameFullException;
import it.polimi.it.galaxytrucker.networking.CommunicationType;
import it.polimi.it.galaxytrucker.networking.client.rmi.RMIServer;
import it.polimi.it.galaxytrucker.networking.server.rmi.RMIVirtualClient;
import it.polimi.it.galaxytrucker.utils.ServerDetails;
import it.polimi.it.galaxytrucker.view.CLI.ConsoleColors;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

/**
 * The main server class responsible for handling incoming RMI and Socket connections,
 * managing connected clients via {@link ClientHandler} instances, and overseeing
 * active game instances managed by {@link Controller} objects. It implements the
 * {@link RMIServer} interface to allow RMI clients to connect and {@link Runnable}
 * to run the main server loop (specifically for socket connections). It also implements
 * {@link ServerInterface} to provide core server functionalities to other components
 * like {@link ClientHandler}.
 *
 * @author giacomoamaducci
 * @version 1.2
 */
public class Server extends UnicastRemoteObject implements RMIServer, Runnable, ServerInterface {
    /**
     * The IP address the server is running on. Used for RMI setup.
     */
    String serverIPAddress = ServerDetails.DEFAULT_IP;

    /**
     * A list of all active {@link ClientHandler} instances, each representing a connected client.
     * Access to this list is synchronized.
     */
    final List<ClientHandler> clients = new ArrayList<>();
    /**
     * The server socket used for listening for incoming Socket connections.
     */
    private ServerSocket listenSocket;

    /**
     * The default length of the building phase timer in seconds.
     */
    private final int BUILDING_TIMER_LENGTH = 10; // DA METTERE A 60 SECONDI

    /**
     * A map storing active game controllers, keyed by their unique game UUIDs.
     * Access to this map is synchronized.
     */
    private final Map<UUID, Controller> controllers = new HashMap<>();

    /**
     * Constructs a new Server instance.
     *
     * @throws RemoteException If an RMI-related error occurs during object export.
     */
    public Server() throws RemoteException {
        super();
    }

    /**
     * Starts the main server loop, setting up both the RMI registry and the Socket server.
     * The RMI server is bound to a specific port and name. The Socket server listens
     * for incoming connections on a different port, creating a new {@link ClientHandler}
     * for each incoming socket connection and starting a thread to handle it.
     * This method is intended to be run in its own thread.
     *
     * @throws RuntimeException if an error occurs during RMI setup or socket operations.
     */
    public void run () {

        System.out.println("Insert IP address to use (leave empty for 'localhost'): ");
        Scanner scanner = new Scanner(System.in);
        String ip = scanner.nextLine();

        serverIPAddress = ip.isEmpty() ? ServerDetails.DEFAULT_IP : ip;

        // Setup RMI server

        System.setProperty("java.rmi.server.hostname", serverIPAddress);
        Registry registry;
        try {
            // Create RMI registry on the specified port
            registry = LocateRegistry.createRegistry(ServerDetails.RMI_DEFAULT_PORT);
            // Rebind this server object in the registry under the name "server"
            registry.rebind(ServerDetails.DEFAULT_RMI_NAME, this);
            // Export this server object to make it available for remote calls
            // UnicastRemoteObject.exportObject(this, 1234); // This line might be redundant after rebind, depending on RMI setup specifics
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }

        System.out.println("RMI server ready. Registry: " + registry + "\n>\tName: " + ServerDetails.DEFAULT_RMI_NAME + "\n>\tPort: " + ServerDetails.RMI_DEFAULT_PORT);

        // Setup socket server


        try {
            // Create a server socket that listens on the specified port
            this.listenSocket = new ServerSocket(ServerDetails.SOCKET_DEFAULT_PORT, 0, InetAddress.getByName(serverIPAddress));
            Socket clientSocket = null;

            System.out.println("Socket server ready\n>\tIP: " + listenSocket.getInetAddress().getHostAddress() + "\n>\tPort: " + ServerDetails.SOCKET_DEFAULT_PORT);
            System.out.println("\n---------------------------------------------\n");

            // Loop indefinitely to accept incoming socket connections
            while ((clientSocket = this.listenSocket.accept()) != null) {
                System.out.println(ConsoleColors.MAIN_SERVER_DEBUG + "incoming socket connection" + ConsoleColors.RESET);

                // Get input and output streams for the connected socket
                InputStreamReader socketRx = new InputStreamReader(clientSocket.getInputStream());
                OutputStreamWriter socketTx = new OutputStreamWriter(clientSocket.getOutputStream());

                // Create a new ClientHandler for the socket connection
                ClientHandler handler = new ClientHandler(this, CommunicationType.SOCKET, socketRx, socketTx);

                // Add the new handler to the list of clients (synchronized access)
                synchronized (this.clients) {
                    clients.add(handler);
                }

                // Start a new thread to run the ClientHandler's main loop
                new Thread(handler::run).start();
            }
        }
        catch (IOException e) {
            // Wrap and rethrow IOException as RuntimeException
            throw new RuntimeException(e);
        }
    }

    /**
     * Accepts an incoming connection from an RMI client. This method is called
     * remotely by an RMI client via the {@link RMIServer} interface. It creates
     * a {@link ClientHandler} for the RMI client, adds it to the list of clients,
     * sets the handler on the client's remote object, and starts the handler's thread.
     *
     * @param client The {@link RMIVirtualClient} remote reference that acts as a
     * proxy to the connected RMI client.
     * @throws RemoteException If a communication problem occurs during the RMI call
     * or during the creation/export of the ClientHandler.
     */
    public void connect(RMIVirtualClient client) throws RemoteException {
        System.out.println(ConsoleColors.MAIN_SERVER_DEBUG + "incoming RMI connection" + ConsoleColors.RESET);

        // Create a new ClientHandler for the RMI client
        ClientHandler handler = new ClientHandler(this, CommunicationType.RMI, client);
        // Add the new handler to the list of clients (synchronized access)
        synchronized (this.clients) {
            clients.add(handler);
        }
        // Set the handler on the client's remote object, allowing the client
        // to call methods on the handler (which implements RMIVirtualServer)
        client.setHandler(handler);
        // Start a new thread to run the ClientHandler's main loop
        new Thread(handler::run).start();
    }

    /**
     * Retrieves a list of data for all currently active games.
     *
     * @return A {@link List} of {@link GenericGameData} objects, each containing
     * summary information about an active game. Access to the controllers
     * map is synchronized.
     */
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

    /**
     * Sets the username for a given client handler if the desired username is unique.
     *
     * @param client The {@link ClientHandler} for which the username is to be set.
     * @param username The desired username.
     * @return {@code true} if the username was successfully set (i.e., it was unique),
     * {@code false} otherwise. Access to the clients list is synchronized.
     */
    @Override
    public boolean setUsername(ClientHandler client, String username) {
        synchronized (this.clients) {
            return isUsernameUnique(username);
        }
    }

    /**
     * Checks if a given username is currently unique among all connected clients.
     *
     * @param username The username to check for uniqueness.
     * @return {@code true} if the username is not currently used by any connected client,
     * {@code false} otherwise.
     */
    private boolean isUsernameUnique(String username) {
        for (ClientHandler client : this.clients) {
            if (client.getUsername().equals(username))
                return false;
        }
        return true;
    }

    /**
     * Adds a player (represented by their {@link ClientHandler}) to a specific game.
     *
     * @param client The {@link ClientHandler} of the player to add.
     * @param gameId The UUID of the game to join.
     * @throws GameFullException If the game identified by {@code gameId} is already full.
     * Access to the controllers map is synchronized.
     */
    @Override
    public void addPlayerToGame(ClientHandler client, UUID gameId) throws GameFullException {
        synchronized (this.controllers) {
            // Add the player to the game controller
            controllers.get(gameId).addPlayer(client);
            // Set the controller reference on the client handler
            client.setController(controllers.get(gameId));
        }
    }

    /**
     * Creates a new game instance with the specified number of players and game level.
     * A unique UUID is generated for the new game.
     *
     * @param players The maximum number of players for the new game.
     * @param level The difficulty level for the new game.
     * @return The UUID of the newly created game. Access to the controllers map is synchronized.
     */
    @Override
    public UUID createNewGame(int players, int level) {
        UUID gameId = UUID.randomUUID();
        synchronized (this.controllers) {
            // Create a new Controller instance for the game and add it to the map
            controllers.put(gameId, new Controller(level, players, gameId));
        }
        return gameId;
    }
}
