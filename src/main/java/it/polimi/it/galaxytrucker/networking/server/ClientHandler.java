package it.polimi.it.galaxytrucker.networking.server;

import it.polimi.it.galaxytrucker.messages.Message;
import it.polimi.it.galaxytrucker.messages.clientmessages.HeartBeatMessage;
import it.polimi.it.galaxytrucker.messages.clientmessages.UserInput;
import it.polimi.it.galaxytrucker.messages.servermessages.GameUpdate;
import it.polimi.it.galaxytrucker.messages.servermessages.GameUpdateType;
import it.polimi.it.galaxytrucker.controller.ControllerInterface;
import it.polimi.it.galaxytrucker.controller.GenericGameData;
import it.polimi.it.galaxytrucker.exceptions.GameFullException;
import it.polimi.it.galaxytrucker.listeners.Listener;
import it.polimi.it.galaxytrucker.model.utility.Color;
import it.polimi.it.galaxytrucker.networking.client.rmi.RMIVirtualServer;
import it.polimi.it.galaxytrucker.networking.utils.ServerDetails;
import it.polimi.it.galaxytrucker.view.CLI.ConsoleColors;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.*;

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
public abstract class ClientHandler extends UnicastRemoteObject implements Listener {
    /**
     * Username of the associated client.
     */
    protected String clientName = "";
    /**
     * Unique identifier for the client.
     */
    protected UUID clientUuid;
    /**
     * Flag to indicate whether the client is actively connected or not.
     * An active connection is determined by the client sending
     * periodic {@link HeartBeatMessage}s.
     */
    private boolean clientConnected;
    /**
     * The timestamp at which the last {@link HeartBeatMessage} was received
     * from the client.
     */
    private Instant lastHeartbeatTime;
    /**
     * Reference to the controller for the match the client is part of.
     */
    private ControllerInterface controller;
    /**
     * Reference to the server, needed for some setup.
     */
    private final ServerInterface server;
    /**
     * Queue where messages from the model are stored before being sent to the client.
     */
    protected final BlockingQueue<GameUpdate> updatesForClientQueue = new LinkedBlockingQueue<>();

    /**
     * Constructs a new ClientHandler for an RMI client connection.
     *
     * @param server The main server instance.
     * @throws RemoteException If an RMI-related error occurs during object export.
     */
    protected ClientHandler(ServerInterface server) throws RemoteException {
        super();
        this.server = server;

        // Start the thread that checks client activity. If this thread finds the client hasn't
        // sent a heartbeat message in some time, it marks the client as disconnected.
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(() -> {
            if (lastHeartbeatTime == null || !clientConnected) return;

            long elapsed = Duration.between(lastHeartbeatTime, Instant.now()).toSeconds();
            if (elapsed > ServerDetails.CONNECTION_TIMEOUT) {
                clientConnected = false;
                System.out.println(ConsoleColors.CLIENT_HANDLER_DEBUG.tag(clientName) + "Haven't heard from client in " + elapsed + " seconds. Marking as disconnected." + ConsoleColors.RESET);
                controller.disconnectPlayer(clientUuid);
            }
        }, 0, ServerDetails.HEARTBEAT_FREQUENCY, TimeUnit.SECONDS);
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
    public abstract void run();

    public void processUserInput(Message message) throws RemoteException {
        if (message instanceof HeartBeatMessage) {
            lastHeartbeatTime = Instant.now();

        } else if (message instanceof UserInput userInput) {
            System.out.println(ConsoleColors.CLIENT_HANDLER_DEBUG.tag(clientName) + "processing input of type " + userInput.getType() + ConsoleColors.RESET);
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
                            .setGameLevel(controller.getLevel())
                            .build()
                    );
                    break;

                case JOIN_ACTIVE_GAME:
                    try {
                        Color color = server.addPlayerToGame(this, userInput.getGameId());

                        notify(new GameUpdate.GameUpdateBuilder(GameUpdateType.JOIN_GAME_RESULT)
                                .setSuccessfulOperation(true)
                                .setGameUuid(userInput.getGameId())
                                .setGameLevel(controller.getLevel())
                                .setPlayerColor(color)
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

                case CONFIRM_BUILDING_END:
                    controller.endPlayerBuilding(clientUuid);
                    break;

                case REMOVE_COMPONENT:
                    controller.removeComponentTile(clientUuid, userInput.getCoords().get(0), userInput.getCoords().get(1));
                    break;
                case ACTIVATE_CANNON:
                    controller.activateCannon(clientUuid, userInput.getComponentsForActivation());
                    break;
                case ACTIVATE_ENGINE:
                    controller.activateEngine(clientUuid, userInput.getComponentsForActivation());
                    break;
                case ACTIVATE_SHIELD:
                    controller.activateShield(clientUuid, userInput.getComponentsForActivation());
                    break;
                case CARGO_REWARD:
                    controller.manageAcceptedCargo(clientUuid, userInput.getAcceptedCargo());
                    break;
                case CREDIT_REWARD:
                    controller.manageCreditChoice(clientUuid, userInput.getCreditChoice());
                    break;
                case CREWMATE_PENALTY:
                    controller.manageRemovedCrewmate(clientUuid, userInput.getRemovedCrewmate());
                    break;
                case PARTICIPATION:
                    controller.manageParticipation(clientUuid, userInput.getParticipation(), userInput.getParticipationChoice());
                    break;
                default:
                    System.out.println(ConsoleColors.CLIENT_HANDLER_DEBUG.tag(clientName) + "received input" + ConsoleColors.RESET);
            }
        }
    }
}
