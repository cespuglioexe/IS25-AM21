package it.polimi.it.galaxytrucker.networking.rmi.server;

import it.polimi.it.galaxytrucker.controller.Controller;
import it.polimi.it.galaxytrucker.controller.GenericGameData;
import it.polimi.it.galaxytrucker.model.exceptions.InvalidActionException;
import it.polimi.it.galaxytrucker.networking.messages.GameUpdate;
import it.polimi.it.galaxytrucker.networking.messages.UserInput;
import it.polimi.it.galaxytrucker.networking.messages.UserInputType;
import it.polimi.it.galaxytrucker.networking.rmi.client.RMIVirtualServer;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import it.polimi.it.galaxytrucker.view.ConsoleColors;

import static org.fusesource.jansi.Ansi.ansi;

public class RMIServer extends UnicastRemoteObject implements RMIVirtualServer {
    // List of active games, each one has a nickname
    private final List<Controller> controllers = new ArrayList<>();
    // Each game is associated with the list of participating players
    private final HashMap<String, List<RMIVirtualView>> playingClients = new HashMap<>();
    // Temporary storage for clients that aren't in a game
    private final List<RMIVirtualView> connectedClients = new ArrayList<>();

    private final ExecutorService executorService = Executors.newCachedThreadPool();

    public RMIServer() throws RemoteException {
        super();
    }

    public static void main(String[] args) throws RemoteException {
        final String serverName = "server";

        RMIVirtualServer server = new RMIServer();
        int port = 1234;
        Registry registry = LocateRegistry.createRegistry(port);

        registry.rebind(serverName, server);

        System.out.println(ConsoleColors.GREEN + "Server ready\n>\tName: " + serverName + "\n>\tPort: " + port + ConsoleColors.RESET);
    }

    @Override
    public List<GenericGameData> getControllers() throws RemoteException {
        List<GenericGameData> list = new ArrayList<>();
        for (Controller c : controllers) {
            list.add(c.getGameData());
        }
        return list;
    }

    /**
     * Accepts an incoming connection from a client.
     * <p>
     * The client reference is temporarily stored in {@code connectingClients} until
     * the client's username is received and checked.
     *
     * @param client The client to connect
     * @throws RemoteException If there is a problem
     */
    @Override
    public void connect(RMIVirtualView client) throws RemoteException {
        synchronized (this.connectedClients) {
            this.connectedClients.add(client);
        }
        System.out.println("\nIncoming connection");
    }

    /**
     * Checks whether the provided username is unique among the connected clients and those in the process of connecting.
     *
     * @param client The {@code RMIVirtualView} instance representing the client requesting the username validation.
     * @param username The username to be checked for uniqueness.
     * @return {@code true} if the username is unique and not already taken; {@code false} otherwise.
     * @throws RemoteException If a remote communication error occurs.
     */
    @Override
    public boolean checkUsernameIsUnique(RMIVirtualView client, String username) throws RemoteException {
        System.out.println("Checking username");
        synchronized (this.playingClients) {
            for (List<RMIVirtualView> c : this.playingClients.values()) {
                for (RMIVirtualView v : c) {
                    if (v.getName().equals(username)) {
                        System.out.println("Username already taken");
                        return false;
                    }
                }
            }
        }
        synchronized (this.connectedClients) {
            for (RMIVirtualView c : this.connectedClients) {
                if (!c.equals(client) && c.getName().equals(username)) {
                    System.out.println("Username already taken");
                    return false;
                }
            }
        }
        System.out.println("Username is unique");
        return true;
    }

    @Override
    public void sendMessageToGame(UUID playerId, UserInput input, int gameIndex) throws RemoteException {
       switch (input.getType()) {
           case PLACE_COMPONENT:
               controllers.get(gameIndex).placeComponentTile(playerId, input.getCoords().get(1), input.getCoords().get(0),input.getRotation());
               break;
           case SAVE_COMPONENT:
               controllers.get(gameIndex).saveComponentTile(playerId);
               break;
           case DISCARD_COMPONENT:
               controllers.get(gameIndex).discardComponentTile(playerId);
               break;
           case REQUEST:
               System.out.println(ConsoleColors.CYAN + "Player " + playerId + " requested " + input.getRequestType() + ConsoleColors.RESET);
               switch (input.getRequestType()) {
                   case NEW_TILE:
                       controllers.get(gameIndex).requestNewComponentTile(playerId);
                       break;
                   case SAVED_TILES:
                       controllers.get(gameIndex).requestSavedComponentTiles(playerId);
                       break;
                   case DISCARDED_TILES:
                       controllers.get(gameIndex).requestDiscardedComponentTiles(playerId);
                       break;
                   case SHIP_BOARD:
                       controllers.get(gameIndex).requestShipBoard(playerId);
                       break;
                   case SELECT_SAVED_TILE:
                       controllers.get(gameIndex).selectSavedComponentTile(playerId, input.getSelectedTileIndex());
                       break;
                   case SELECT_DISCARDED_TILE:
                       controllers.get(gameIndex).selectDiscardedComponentTile(playerId, input.getSelectedTileIndex());
                       break;
               }
               break;
       }
    }

    /**
     * Adds a player to the specified game and updates the associated mappings between clients and games.
     *
     * @param client    The {@code RMIVirtualView} instance representing the client to be added to the game
     * @param gameIndex The index of the game to which the player should be added
     * @return The {@code UUID} that was generated for the added player
     * @throws RemoteException If a remote communication error occurs
     */
    @Override
    public UUID addPlayerToGame(RMIVirtualView client, int gameIndex) throws RemoteException, InvalidActionException {
        String matchName;
        // Get the name of the specified game
        synchronized (this.controllers) {
            matchName = controllers.get(gameIndex).getNickname();
        }
        // Associate the player and the game they are playing
        synchronized (this.connectedClients) {
            connectedClients.remove(client);
        }
        synchronized (this.playingClients) {
            List<RMIVirtualView> players = playingClients.get(matchName);
            if (players == null) {
                players = new ArrayList<>();
            }
            players.add(client);
            playingClients.put(matchName, players);
        }
        UUID id;
        // Add the player to the specified game
        synchronized (this.controllers) {
            id = controllers.get(gameIndex).addPlayer(client.getName());
        }
        return id;
    }

    /**
     * Adds a player to a game specified by its nickname. This method searches for the
     * corresponding game index using the game's nickname and then delegates the addition
     * of the player to the game identified by that index.
     *
     * @param client The {@code RMIVirtualView} instance representing the client to be added to the game
     * @param gameNickname The nickname of the game to which the player should be added
     * @throws RemoteException If a remote communication error occurs
     */
    @Override
    public UUID addPlayerToGame(RMIVirtualView client, String gameNickname) throws RemoteException {
        int i = 0;
        for (Controller c : controllers) {
            if (c.getNickname().equals(gameNickname)) {
                i = controllers.indexOf(c);
                break;
            }
        }

        return addPlayerToGame(client, i);
    }

    @Override
    public void newGame(String gameNickname, int players, int level) throws RemoteException {
        synchronized (this.controllers) {
            controllers.add(new Controller(level, players, gameNickname, this));
        }
    }

    public void sendMessageToSinglePlayer(String gameNickname, String clientName, GameUpdate message) throws RemoteException {
        List<RMIVirtualView> players = playingClients.get(gameNickname);
        for (RMIVirtualView v : players) {
            try {
                if (v.getName().equals(clientName)) {
                    v.recieveGameUpdate(message);
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendMessageToAllPlayers(String gameNickname, GameUpdate message) throws RemoteException {
        List<RMIVirtualView> players = playingClients.get(gameNickname);
        System.out.println("Players: " + players.size());
        for (RMIVirtualView v : players) {
            executorService.submit(() -> {
                try {
                    sendMessageToSinglePlayer(gameNickname, v.getName(), message);
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }

}