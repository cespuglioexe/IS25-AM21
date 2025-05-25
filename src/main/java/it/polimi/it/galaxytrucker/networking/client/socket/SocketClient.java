package it.polimi.it.galaxytrucker.networking.client.socket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import it.polimi.it.galaxytrucker.messages.clientmessages.HeartBeatMessage;
import it.polimi.it.galaxytrucker.messages.clientmessages.UserInput;
import it.polimi.it.galaxytrucker.messages.clientmessages.UserInputType;
import it.polimi.it.galaxytrucker.messages.servermessages.GameUpdate;
import it.polimi.it.galaxytrucker.model.json.Json;
import it.polimi.it.galaxytrucker.networking.client.Client;
import it.polimi.it.galaxytrucker.networking.client.ClientInterface;
import it.polimi.it.galaxytrucker.networking.server.socket.SocketVirtualClient;
import it.polimi.it.galaxytrucker.utils.ServerDetails;
import it.polimi.it.galaxytrucker.view.CLI.ConsoleColors;
import it.polimi.it.galaxytrucker.view.View;

import java.io.*;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.*;

/**
 * This class represents the client logic implemented using Socket technology.
 * It handles communication with a server over a pre-established socket connection,
 * sending user inputs and receiving game updates.
 * <p>
 * This client implements {@link SocketVirtualClient} (representing the client-side
 * of a server's logical connection), {@link Runnable} to manage its listening
 * thread, and {@link ClientInterface} to provide a standardized client interface.
 *
 * @author giacomoamaducci
 * @version 1.2
 */
public class SocketClient extends Client {
    /**
     * Reader for receiving messages from the socket's input stream.
     */
    private BufferedReader input;
    /**
     * Writer for sending messages to the socket's output stream.
     */
    private PrintWriter output;
    /**
     * Object writer for converting java objects to JSON strings
     * that can be sent over sockets.
     */
    private final ObjectWriter objectWriter = new ObjectMapper().writer();

    /**
     * Constructs a new {@code SocketClient} instance.
     *
     * @param view The {@link View} implementation for user interaction.
     * @throws RemoteException if there is an RMI problem, this should never happen
     * with a socket client.
     */
    public SocketClient(View view) throws RemoteException {
        super(view);
    }

    @Override
    protected void initiateServerConnection() {
        Scanner scanner = new Scanner(System.in);
        boolean connected = false;

        do {
            System.out.print("Insert server IP address (leave empty for 'localhost')\n> ");
            String serverIp = scanner.nextLine().trim();

            int port;
            do {
                System.out.print("Insert port number (leave empty for default port 5002)\n> ");
                String portString = scanner.nextLine().trim();
                port = -1;

                if (portString.isEmpty()) {
                    port = ServerDetails.SOCKET_DEFAULT_PORT;
                } else {
                    try {
                        port = Integer.parseInt(portString);
                        if (port < 0 || port > 65535) {
                            System.out.println(ConsoleColors.RED + "That's not a valid port number. It should be between 0 and 65535." + ConsoleColors.RESET);
                        }
                    } catch (NumberFormatException e) {
                        System.out.println(ConsoleColors.RED + "Please enter a valid input." + ConsoleColors.RESET);
                    }
                }
            } while (port < 0 || port > 65535);

            try {
                Socket serverSocket = new Socket(serverIp.isEmpty() ? ServerDetails.DEFAULT_IP : serverIp, port);

                InputStreamReader socketRx = new InputStreamReader(serverSocket.getInputStream());
                OutputStreamWriter socketTx = new OutputStreamWriter(serverSocket.getOutputStream());

                this.input = new BufferedReader(socketRx);
                this.output = new PrintWriter(socketTx);

                connected = true;
            } catch (IOException e) {
                System.out.println(ConsoleColors.RED + "Failed to connect to " + serverIp + ":" + port + "! Please try again." + ConsoleColors.RESET);
            }
        } while (!connected);

        // Starts a new thread for reading incoming messages without blocking other processes
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
    }

    @Override
    protected void sendHeartbeat() {
        try {
            String jsonMessage = objectWriter.writeValueAsString(new HeartBeatMessage());
            System.out.println(ConsoleColors.CLIENT_DEBUG + "sending message" + jsonMessage + ConsoleColors.RESET);
            this.output.println(jsonMessage);
            this.output.flush();
            System.out.println(ConsoleColors.CLIENT_DEBUG + "printed message to socket" + ConsoleColors.RESET);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Reads messages from the server's input stream in a loop.
     * Each line is expected to be a JSON string representing a {@link GameUpdate}.
     * The JSON is parsed and the resulting command is passed to
     * {@link #processServerUpdate(GameUpdate)} for processing.
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

            processServerUpdate(command);
        }
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
     * inherited documentation from the {@code ClientInterface} interface.
     */
    @Override
    public void receiveUserInput(UserInput input) {
        commandSenderExecutor.submit(() -> {
            try {
                String jsonMessage = objectWriter.writeValueAsString(input);
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