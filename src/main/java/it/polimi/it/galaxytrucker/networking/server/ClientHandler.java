package it.polimi.it.galaxytrucker.networking.server;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import it.polimi.it.galaxytrucker.commands.UserInput;
import it.polimi.it.galaxytrucker.commands.servercommands.GameUpdate;
import it.polimi.it.galaxytrucker.commands.servercommands.GameUpdateType;
import it.polimi.it.galaxytrucker.controller.ControllerInterface;
import it.polimi.it.galaxytrucker.controller.GenericGameData;
import it.polimi.it.galaxytrucker.exceptions.GameFullException;
import it.polimi.it.galaxytrucker.listeners.Listener;
import it.polimi.it.galaxytrucker.model.json.Json;
import it.polimi.it.galaxytrucker.networking.CommunicationType;
import it.polimi.it.galaxytrucker.networking.VirtualClient;
import it.polimi.it.galaxytrucker.networking.client.rmi.RMIVirtualServer;
import it.polimi.it.galaxytrucker.networking.server.rmi.RMIVirtualClient;
import it.polimi.it.galaxytrucker.view.CLI.ConsoleColors;

import java.io.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Handles communication and interactions for a single connected client on the server side.
 * This class acts as an intermediary between the core server logic ({@link ServerInterface})
 * and a specific client, managing message queues, handling both RMI and Socket communication,
 * and processing client input. It implements {@link Listener} to receive updates from the
 * game model and {@link RMIVirtualServer} to expose remote methods callable by RMI clients.
 *
 * @author giacomoamaducci
 * @version 2.0
 */
public class ClientHandler extends UnicastRemoteObject implements Listener, RMIVirtualServer {
    /**
     * Username of the associated client.
     */
    private String clientName = "";
    /**
     * Unique identifier for the client.
     */
    private UUID clientUuid;
    /**
     * Reference to the controller for the match the client is part of.
     */
    private ControllerInterface controller;
    /**
     * Reference to the server, needed for some setup.
     */
    private final ServerInterface server;
    /**
     * Reference to the client, used for RMI clients. This is the remote object
     * that allows the server to invoke methods on the client.
     */
    private VirtualClient client;
    /**
     * The type of communication used (e.g., RMI or socket).
     */
    private final CommunicationType communicationType;
    /**
     * Queue where messages from the model are stored before being sent to the client.
     */
    private final BlockingQueue<GameUpdate> updatesForClientQueue = new LinkedBlockingQueue<>();
    /**
     * Input stream used only if acting as a socket handler.
     */
    private BufferedReader socketInput;
    /**
     * Output stream used only if acting as a socket handler.
     */
    private PrintWriter socketOutput;


    /**
     * Constructs a new ClientHandler for an RMI client connection.
     *
     * @param server The main server instance.
     * @param communicationType The type of communication (expected to be RMI).
     * @param client The remote reference to the client's {@link VirtualClient} implementation.
     * @throws RemoteException If an RMI-related error occurs during object export.
     */
    protected ClientHandler(ServerInterface server, CommunicationType communicationType, VirtualClient client) throws RemoteException {
        super();
        this.server = server;
        this.client = client;
        this.communicationType = communicationType;
    }

    /**
     * Constructs a new ClientHandler for a Socket client connection.
     *
     * @param server The main server instance.
     * @param communicationType The type of communication (expected to be SOCKET).
     * @param socketRx The input stream reader for the socket.
     * @param socketTx The output stream writer for the socket.
     * @throws RemoteException If an RMI-related error occurs during object export.
     */
    protected ClientHandler(ServerInterface server, CommunicationType communicationType, InputStreamReader socketRx, OutputStreamWriter socketTx) throws RemoteException {
        super();
        this.server = server;
        this.socketInput = new BufferedReader(socketRx);
        this.socketOutput = new PrintWriter(socketTx);
        this.communicationType = communicationType;
    }

    /**
     * Sets the game controller associated with this client handler.
     * The controller manages the specific game instance the client is participating in.
     * This method ensures the controller is set only once.
     *
     * @param controller The {@link ControllerInterface} instance for the client's game.
     */
    public void setController(ControllerInterface controller) {
        if (this.controller == null)
            this.controller = controller;
    }

    /**
     * Gets the username of the client associated with this handler.
     *
     * @return The client's username.
     */
    public String getUsername() {
        return clientName;
    }

    /**
     * Gets the {@code UUID} of the client associated with this handler.
     *
     * @return The client's {@code UUID}.
     */
    public UUID getUuid() {
        return clientUuid;
    }

    /**
     * Receives a game update from the model and adds it to the queue
     * for sending to the client. This method implements the {@link Listener#notify(GameUpdate)}
     * interface method. It ensures the update is added to the queue, retrying if necessary.
     *
     * @param update The {@link GameUpdate} object received from the model.
     */
    @Override
    public void notify(GameUpdate update) {
        boolean result;
        do {
            result = updatesForClientQueue.offer(update);
        } while (!result);
    }

    /**
     * Starts the communication threads for the client handler.
     * This method launches a thread to continuously take messages from the
     * {@code updatesForClientQueue} and send them to the client using the
     * appropriate communication method (RMI or Socket).
     * If the communication type is SOCKET, it also starts a separate thread
     * to read incoming messages from the socket input stream and process them
     * as {@link UserInput}.
     */
    public void run() {
        // Thread for grabbing incoming messages from the model and forwarding
        // them to the connected client. The method used for sending the
        // messages is based on the communication technology chosen by the
        // client when starting their connection to the server.
        new Thread(() -> {
            while (true) {
                try {
                    GameUpdate message = updatesForClientQueue.take();
                    System.out.println(ConsoleColors.CLIENT_HANDLER_DEBUG.tag(clientName) + "sending update of type " + message.getInstructionType());
                    switch (communicationType) {
                        case RMI    -> {
                            try {
                                ((RMIVirtualClient) client).sendMessageToClient(message);
                            } catch (RemoteException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        case SOCKET -> {
                            ObjectWriter ow = new ObjectMapper().writer();
                            HashMap<UUID, ?> map = message.getAllPlayerShipBoard();
                            if (map != null) {
                                map.entrySet().removeIf(entry -> entry.getKey() == null || entry.getValue() == null);
                            }
                            try {
                                String jsonMessage = ow.writeValueAsString(message);
                                socketOutput.println(jsonMessage);
                                socketOutput.flush();
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                } catch (InterruptedException e) {
                    System.out.println(ConsoleColors.CLIENT_HANDLER_DEBUG.tag(clientName) + ConsoleColors.RED_BOLD + "NO LONGER READING FROM MESSAGE QUEUE" + ConsoleColors.RESET);
                    throw new RuntimeException(e);
                }
            }
        }).start();

        // Thread for asynchronously reading the socket input stream for
        // commands sent by a client using socket technology. The received
        // input is converted into a UserInput object and passed to the
        // same execution function used by RMI clients.
        if (communicationType == CommunicationType.SOCKET) {
            new Thread(() -> {
                while (true) {
                    try {
                        String jsonCommand = socketInput.readLine();
                        System.out.println(ConsoleColors.CLIENT_HANDLER_DEBUG.tag(clientName) + "received command " + jsonCommand + ConsoleColors.RESET);
                        JsonNode node = Json.parse(jsonCommand);
                        UserInput command = Json.fromJson(node, UserInput.class);

                        receiveUserInput(command);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }).start();
        }
    }


    /**
     * Receives and processes a {@link UserInput} object from the client.
     * It delegates game-specific actions to the associated {@link ControllerInterface}.
     * This method implements the {@link RMIVirtualServer#receiveUserInput(UserInput)}
     * interface method and is also called internally for socket input.
     *
     * @param userInput The {@link UserInput} object containing the client's command.
     * @throws RemoteException If an RMI-related error occurs during the processing
     * or delegation of the input (relevant for RMI calls).
     */
    @Override
    public void receiveUserInput(UserInput userInput) throws RemoteException {
        switch (userInput.getType()) {
            case HANDSHAKE:
                clientUuid = userInput.getPlayerUuid();
                break;

            case SET_PLAYER_USERNAME:
                if (server.setUsername(this, userInput.getPlayerName())) {
                    clientName = userInput.getPlayerName(); // Set the local copy of the name
                    notify(new GameUpdate.GameUpdateBuilder(GameUpdateType.SET_USERNAME_RESULT)
                            .setSuccessfulOperation(true)
                            .setPlayerName(userInput.getPlayerName())
                            .build()
                    );
                } else {
                    notify(new GameUpdate.GameUpdateBuilder(GameUpdateType.SET_USERNAME_RESULT)
                            .setSuccessfulOperation(false)
                            .build()
                    );
                }
                break;

            case CREATE_NEW_GAME:
                UUID ng_gameUuid = server.createNewGame(userInput.getGamePlayers(), userInput.getGameLevel());
                server.addPlayerToGame(this, ng_gameUuid);

                notify(new GameUpdate.GameUpdateBuilder(GameUpdateType.CREATE_GAME_RESULT)
                        .setSuccessfulOperation(true)
                        .setGameUuid(ng_gameUuid)
                        .build()
                );
                break;

            case JOIN_ACTIVE_GAME:
                try {
                    server.addPlayerToGame(this, userInput.getGameId());

                    notify(new GameUpdate.GameUpdateBuilder(GameUpdateType.JOIN_GAME_RESULT)
                            .setSuccessfulOperation(true)
                            .setGameUuid(userInput.getGameId())
                            .setGameLevel(controller.getLevel())
                            .build()
                    );
                } catch (GameFullException e) {
                    notify(new GameUpdate.GameUpdateBuilder(GameUpdateType.JOIN_GAME_RESULT)
                            .setSuccessfulOperation(false)
                            .setOperationMessage("The game was full")
                            .build()

                    );
                }
                break;

            case SEE_ACTIVE_GAMES:
                List<GenericGameData> controllers = server.getActiveGames();
                notify(new GameUpdate.GameUpdateBuilder(GameUpdateType.LIST_ACTIVE_CONTROLLERS)
                        .setActiveControllers(controllers)
                        .build()
                );
                break;

            case SELECT_RANDOM_COMPONENT:
                controller.requestNewComponentTile(clientUuid);
                break;

            case SELECT_SAVED_COMPONENT:
                controller.selectSavedComponentTile(clientUuid, userInput.getSelectedTileIndex());
                break;

            case SELECT_DISCARDED_COMPONENT:
                controller.selectDiscardedComponentTile(clientUuid, userInput.getSelectedTileIndex());
                break;

            case PLACE_COMPONENT:
                controller.placeComponentTile(clientUuid, userInput.getCoords().get(0), userInput.getCoords().get(1), userInput.getRotation());
                break;

            case SAVE_SELECTED_COMPONENT:
                controller.saveComponentTile(clientUuid);
                break;

            case DISCARD_SELECTED_COMPONENT:
                controller.discardComponentTile(clientUuid);
                break;

            case RESTART_BUILDING_TIMER:
                controller.startBuildPhaseTimer();
                break;

            default:
                System.out.println(ConsoleColors.CLIENT_HANDLER_DEBUG.tag(clientName) + "received input" + ConsoleColors.RESET);
        }
    }
}