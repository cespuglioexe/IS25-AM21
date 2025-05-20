package it.polimi.it.galaxytrucker.networking.client.rmi;

import it.polimi.it.galaxytrucker.commands.GameError;
import it.polimi.it.galaxytrucker.commands.UserInput;
import it.polimi.it.galaxytrucker.commands.UserInputType;
import it.polimi.it.galaxytrucker.exceptions.InvalidFunctionCallInState;
import it.polimi.it.galaxytrucker.model.componenttiles.TileData;
import it.polimi.it.galaxytrucker.commands.servercommands.GameUpdate;
import it.polimi.it.galaxytrucker.networking.client.Client;
import it.polimi.it.galaxytrucker.networking.client.clientmodel.ClientModel;
import it.polimi.it.galaxytrucker.networking.server.rmi.RMIVirtualClient;
import it.polimi.it.galaxytrucker.utils.ServerDetails;
import it.polimi.it.galaxytrucker.view.View;
import it.polimi.it.galaxytrucker.view.CLI.ConsoleColors;


import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Represents the RMI client.
 * <p>
 *     This class is responsible for establishing a connection with an RMI server,
 *     sending user inputs to the server, and receiving game updates and errors from the server.
 *     It implements {@link RMIVirtualClient} to be remotely accessible by the server,
 *     {@link Runnable} to handle its own connection logic in a separate thread, and
 *     {@link Client} to provide a common interface for client operations.
 * </p>
 *
 * @author giacomoamaducci
 * @version 1.5
 */
public class RMIClient extends UnicastRemoteObject implements RMIVirtualClient, Runnable, Client {
    /**
     * The client-side representation of the game model.
     * It stores the game state as perceived by this client.
     */
    private final ClientModel model;
    /**
     * The remote reference to the server's virtual client handler.
     * This is used to send commands to the server.
     */
    private RMIVirtualServer server;
    /**
     * The user interface that this client interacts with.
     * The view is responsible for displaying game information and capturing user input.
     */
    private final View view;
    /**
     * An executor service to send user input commands to the server asynchronously.
     * This ensures that the client's main thread is not blocked while sending data.
     */
    private final ExecutorService commandSenderExecutor = Executors.newSingleThreadExecutor();
    /**
     * A flag indicating whether the building phase timer is currently active.
     */
    private boolean buildingTimerIsActive;

    /**
     * Constructs a new RMIClient.
     * Initializes the client model and associates it with the provided view.
     *
     * @param view The user interface implementation for this client.
     * @throws RemoteException if an RMI communication error occurs during object export.
     */
    public RMIClient (View view) throws RemoteException {
        super();
        this.model = new ClientModel();
        this.view = view;
        view.setClient(this);
    }

    /**
     * The main execution logic for the RMI client.
     * This method attempts to connect to an RMI server specified by the user.
     * Once connected, it initializes the view.
     * The connection attempt is repeated until successful.
     */
    @Override
    public void run() {
        boolean connected = false;
        Scanner scanner = new Scanner(System.in);

        do {
            System.out.print("Insert server name\n> ");
            String serverName = scanner.nextLine();

            try {
                Registry registry = LocateRegistry.getRegistry(ServerDetails.DEFAULT_IP, ServerDetails.RMI_DEFAULT_PORT);
                RMIServer connectionServer = ((RMIServer) registry.lookup(serverName));
                connectionServer.connect(this);
                connected = true;
            } catch (Exception e) {
                System.out.println(ConsoleColors.RED + "Failed to connect to '" + serverName + "'" + ConsoleColors.RESET);
            }
        } while (!connected);

        view.begin();
    }

    @Override
    public ClientModel getModel() {
        return model;
    }

    @Override
    public boolean isBuildingTimerIsActive() {
        return buildingTimerIsActive;
    }

    /**
     * Sets the server's handler for this client.
     * This method is called by the RMI server after a successful connection
     * to provide the client with a remote reference to the server.
     * It also sends an initial handshake message to the server.
     *
     * @param handler The {@link RMIVirtualServer} interface, which acts as a remote reference to the server.
     * @throws RemoteException if an RMI communication error occurs.
     */
    @Override
    public void setHandler(RMIVirtualServer handler) throws RemoteException {
        this.server = handler;
        // Send a handshake message to the server upon establishing the handler
        receiveUserInput(new UserInput.UserInputBuilder(UserInputType.HANDSHAKE)
                .setPlayerUuid(model.getMyData().getPlayerId())
                .build()
        );
    }

    /**
     * Receives a game update message from the server and processes it.
     * The method updates the client's model and view based on the type of update received.
     *
     * @param update The {@link GameUpdate} object containing information from the server.
     * @throws RemoteException if an RMI communication error occurs (though typically handled by the RMI runtime).
     */
    @Override
    public void sendMessageToClient(GameUpdate update) throws RemoteException {
        System.out.println(ConsoleColors.CLIENT_DEBUG + "received update of type " + update.getInstructionType() + ConsoleColors.RESET);
        switch (update.getInstructionType()) {
            case SET_USERNAME_RESULT:
                if (update.isSuccessfulOperation()) {
                    model.getMyData().setNickname(update.getPlayerName());
                    view.nameSelectionSuccess();
                } else {
                    view.nameNotAvailable();
                }
                break;

            case CREATE_GAME_RESULT:
                if (update.isSuccessfulOperation()) {
                    model.getMyData().setMatchId(update.getGameUuid());
                    try {
                        view.gameCreationSuccess(true);
                    } catch (InvalidFunctionCallInState e) {
                        // This error is only thrown when creating a 1-player game
                        // and can be ignored as it has no adverse effects.
                        System.err.println("RMIClient: Ignored InvalidFunctionCallInState during game creation success for 1 player game. Details: " + e.getMessage());
                    }
                } else {
                    view.gameCreationSuccess(false);
                }
                break;

            case JOIN_GAME_RESULT:
                if (update.isSuccessfulOperation()) {
                    model.getMyData().setMatchId(update.getGameUuid());
                } else if (update.getOperationMessage().equals("The game was full")) {
                    view.joinedGameIsFull();
                }
                break;

            case LIST_ACTIVE_CONTROLLERS:
                view.activeControllers(update.getActiveControllers());
                break;

            case NEW_STATE:
                switch (update.getNewSate()) {
                    case "BuildingState":
                        HashMap<UUID, List<List<TileData>>> ships = update.getAllPlayerShipBoard();
                        System.out.println(ConsoleColors.CLIENT_HANDLER_DEBUG + ships.values().toString());
                        for (Map.Entry<UUID, List<List<TileData>>> entry : ships.entrySet()) {
                            model.updatePlayerShip(entry.getKey(), entry.getValue());
                        }
                        // model.setCardPiles(update.getCardPileCompositions());
                        // TODO: piles
                        view.buildingStarted();
                        break;
                    default:
                        System.out.println(ConsoleColors.CLIENT_DEBUG + "Received unknown state: " + update.getNewSate() + ConsoleColors.RESET);
                        break;
                }
                break;
            case DRAWN_TILE:
                view.componentTileReceived(update.getNewTile());
                break;
            case SAVED_COMPONENTS_UPDATED:
                model.setSavedTiles(update.getTileList());
                view.savedComponentsUpdated();
                break;
            case DISCARDED_COMPONENTS_UPDATED:
                model.setDiscardedTiles(update.getTileList());
                view.discardedComponentsUpdated();
                break;
            case PLAYER_SHIP_UPDATED:
                model.updatePlayerShip(update.getInterestedPlayerId(), update.getShipBoard());
                view.shipUpdated(update.getInterestedPlayerId());
                break;
            case TIMER_START:
                buildingTimerIsActive = true;
                view.displayTimerStarted();
                break;
            case TIMER_END:
                buildingTimerIsActive = false;
                view.displayTimerEnded();
                break;
            default:
                System.out.println(ConsoleColors.CLIENT_DEBUG + "Received unhandled game update instruction type: " + update.getInstructionType() + ConsoleColors.RESET);
                break;
        }
    }

    /**
     * Reports an error message received from the server to the client.
     *
     * @param error The {@link GameError} object containing details about the error.
     * @throws RemoteException if an RMI communication error occurs (though typically handled by the RMI runtime).
     */
    @Override
    public void reportErrorToClient(GameError error) throws RemoteException {
        // TODO: implement client error reporting logic
        System.err.println(ConsoleColors.RED + "Error from server: " + error.toString() + ConsoleColors.RESET); // Basic error logging
    }

    /**
     * {@inheritDoc}
     * <p>
     *     This implementation forwards the {@link UserInput} object to the server,
     *     delegating the sending process to an {@link ExecutorService} to avoid
     *     blocking the client thread.
     * </p>
     * @param input the {@link UserInput} object to be interpreted.
     */
    @Override
    public void receiveUserInput(UserInput input) {
        commandSenderExecutor.submit(() -> {
            try {
                if (server != null) {
                    server.receiveUserInput(input);
                } else {
                    System.err.println(ConsoleColors.RED + "RMIClient: Cannot send user input, server handler is not set." + ConsoleColors.RESET);
                }
            } catch (RemoteException e) {
                System.err.println(ConsoleColors.RED + "RMIClient: Failed to send user input to server: " + e.getMessage() + ConsoleColors.RESET);
                throw new RuntimeException(e);
            }
        });
    }
}