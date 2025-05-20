package it.polimi.it.galaxytrucker.networking.client.socket;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import it.polimi.it.galaxytrucker.commands.UserInput;
import it.polimi.it.galaxytrucker.commands.UserInputType;
import it.polimi.it.galaxytrucker.commands.servercommands.GameUpdate;
import it.polimi.it.galaxytrucker.exceptions.InvalidFunctionCallInState;
import it.polimi.it.galaxytrucker.model.componenttiles.TileData;
import it.polimi.it.galaxytrucker.model.json.Json;
import it.polimi.it.galaxytrucker.networking.client.Client;
import it.polimi.it.galaxytrucker.networking.client.clientmodel.ClientModel;
import it.polimi.it.galaxytrucker.networking.server.socket.SocketVirtualClient;
import it.polimi.it.galaxytrucker.view.CLI.ConsoleColors;
import it.polimi.it.galaxytrucker.view.View;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * This class represents the client logic implemented using Socket technology.
 * It handles communication with a server over a pre-established socket connection,
 * sending user inputs and receiving game updates.
 * <p>
 * This client implements {@link SocketVirtualClient} (representing the client-side
 * of a server's logical connection), {@link Runnable} to manage its listening
 * thread, and {@link Client} to provide a standardized client interface.
 *
 * @author giacomoamaducci
 * @version 1.2
 */
public class SocketClient implements SocketVirtualClient, Runnable, Client {
    /**
     * Reader for receiving messages from the socket's input stream.
     */
    private BufferedReader input;
    /**
     * Writer for sending messages to the socket's output stream.
     */
    private PrintWriter output;

    /**
     * The client-side data model, storing the game state as perceived by this client.
     */
    private final ClientModel model;
    /**
     * The user interface that this client interacts with.
     */
    private final View view;

    /**
     * An executor service to send user input commands to the server asynchronously.
     * This prevents blocking the main thread during network I/O operations.
     */
    private final ExecutorService commandSenderExecutor = Executors.newSingleThreadExecutor();

    /**
     * A flag indicating whether the building phase timer is currently active.
     * This state is usually updated based on messages from the server.
     */
    private boolean buildingTimerIsActive;

    /**
     * Constructs a new {@code SocketClient} instance.
     *
     * @param view The {@link View} implementation for user interaction.
     */
    public SocketClient(View view) {
        this.view = view;
        view.setClient(this);
        this.model = new ClientModel();
    }

    /**
     * Connects the client to a server socket, initializes communication streams,
     * starts a background thread for reading server messages, sends an initial handshake,
     * and starts the view.
     * <p>
     * This method prompts the user for the server IP address and port number before
     * attempting to establish the connection. If the connection fails, it will prompt
     * the user for the IP address and port number again until a successful connection
     * is established.
     * </p>
     *
     * @throws RuntimeException if an I/O error occurs during socket connection or stream setup.
     */
    public void run() {
        Scanner scanner = new Scanner(System.in);
        boolean connected = false;
        do {
            System.out.println("Insert server IP address: ");
            String host = scanner.nextLine().trim();

            System.out.println("Insert port number: ");
            int port = scanner.nextInt();

            try {
                Socket serverSocket = new Socket(host, port);

                InputStreamReader socketRx = new InputStreamReader(serverSocket.getInputStream());
                OutputStreamWriter socketTx = new OutputStreamWriter(serverSocket.getOutputStream());

                this.input = new BufferedReader(socketRx);
                this.output = new PrintWriter(socketTx);

                connected = true;
            } catch (IOException e) {
                System.out.println(ConsoleColors.RED + "Failed to connect to " + host + ":" + port + "! Please try again." + ConsoleColors.RESET);
            }
        } while (!connected);

        new Thread(() -> {
            try {
                serverMessageReader();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).start();

        // Send an initial handshake message.
        receiveUserInput(new UserInput.UserInputBuilder(UserInputType.HANDSHAKE)
                .setPlayerUuid(model.getMyData().getPlayerId())
                .build()
        );

        view.begin();
    }

    /**
     * Reads messages from the server's input stream in a loop.
     * Each line is expected to be a JSON string representing a {@link GameUpdate}.
     * The JSON is parsed and the resulting command is passed to
     * {@link #executeServerMessage(GameUpdate)} for processing.
     * <p>
     * This method is designed to run in its own thread. It will terminate if
     * the input stream is closed (e.g., server disconnects) or if an
     * {@link IOException} occurs.
     *
     * @throws IOException if an I/O error occurs while reading from the input stream.
     */
    private void serverMessageReader() throws IOException {
        String line;
        while ((line = input.readLine()) != null) {
            JsonNode node = Json.parse(line);
            GameUpdate command = Json.fromJson(node, GameUpdate.class);

            executeServerMessage(command);
        }
    }

    /**
     * Receives a game update message from the server and processes it.
     * The method updates the client's model and view based on the type of update received.
     *
     * @param update The {@link GameUpdate} object containing information from the server.
     */
    public void executeServerMessage(GameUpdate update) {
        System.out.println(ConsoleColors.CLIENT_DEBUG + "received update of type " + update.getInstructionType() + ConsoleColors.RESET);
        switch (update.getInstructionType()) {
            case SET_USERNAME_RESULT:
                if (update.isSuccessfulOperation()) {
                    model.getMyData().setNickname(update.getPlayerName());
                    view.gameSelectionScreen();
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
                        // It can be ignored as it has no adverse effects
                    }
                } else {
                    view.gameCreationSuccess(false);
                }
                break;

            case JOIN_GAME_RESULT:
                if (update.isSuccessfulOperation()) {
                    model.getMyData().setMatchId(update.getGameUuid());
                    model.setGameLevel(update.getGameLevel());
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
                        model.setGameLevel(update.getGameLevel());
                        HashMap<UUID, List<List<TileData>>> ships = update.getAllPlayerShipBoard();
                        System.out.println(ConsoleColors.CLIENT_HANDLER_DEBUG + ships.values().toString());
                        for (UUID playerId : ships.keySet()) {
                            model.updatePlayerShip(playerId, ships.get(playerId));
                        }
                        // model.setCardPiles(update.getCardPileCompositions());
                        // TODO: piles

                        view.buildingStarted();
                        break;
                    default:
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
                // Unhandled instruction types are currently ignored.
                break;
        }
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
     * {@inheritDoc}
     * <p>
     * This implementation serializes the provided {@link UserInput} object into a JSON string.
     * The JSON string is then sent to the server asynchronously via the socket's output stream
     * using an {@link java.util.concurrent.ExecutorService}.
     * <p>
     * If an {@link IOException} occurs during serialization or network transmission, it is
     * caught and re-thrown as a {@link RuntimeException}.
     *
     * @param input The {@link UserInput} object to be serialized and sent to the server.
     * This parameter corresponds to the {@code input} parameter in the
     * inherited documentation from the {@code Client} interface.
     */
    @Override
    public void receiveUserInput(UserInput input) {
        commandSenderExecutor.submit(() -> {
            ObjectWriter ow = new ObjectMapper().writer();
            try {
                String jsonMessage = ow.writeValueAsString(input);
                System.out.println(ConsoleColors.CLIENT_DEBUG + "sending message" + jsonMessage + ConsoleColors.RESET);
                this.output.println(jsonMessage);
                this.output.flush();
                System.out.println(ConsoleColors.CLIENT_DEBUG + "printed message to socket" + ConsoleColors.RESET);
            } catch (IOException e) {
                // TODO: handle serialization errors
                throw new RuntimeException(e);
            }
        });
    }
}