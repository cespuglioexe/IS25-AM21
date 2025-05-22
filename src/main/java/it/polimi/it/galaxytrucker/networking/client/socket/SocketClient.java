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
import it.polimi.it.galaxytrucker.networking.client.ClientInterface;
import it.polimi.it.galaxytrucker.networking.client.clientmodel.ClientModel;
import it.polimi.it.galaxytrucker.networking.server.socket.SocketVirtualClient;
import it.polimi.it.galaxytrucker.view.CLI.ConsoleColors;
import it.polimi.it.galaxytrucker.view.View;

import java.io.*;
import java.net.Socket;
import java.rmi.RemoteException;
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
     * Constructs a new {@code SocketClient} instance.
     *
     * @param view The {@link View} implementation for user interaction.
     * @throws RemoteException if there is an RMI problem, this should never happen
     * with a socket client.
     */
    public SocketClient(View view) throws RemoteException {
        super(view);
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
   @Override
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