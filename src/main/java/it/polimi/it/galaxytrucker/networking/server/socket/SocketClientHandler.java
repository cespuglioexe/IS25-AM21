package it.polimi.it.galaxytrucker.networking.server.socket;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import it.polimi.it.galaxytrucker.commands.UserInput;
import it.polimi.it.galaxytrucker.commands.servercommands.GameUpdate;
import it.polimi.it.galaxytrucker.model.json.Json;
import it.polimi.it.galaxytrucker.networking.server.ClientHandler;
import it.polimi.it.galaxytrucker.networking.server.ServerInterface;

import java.io.*;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.UUID;

public class SocketClientHandler extends ClientHandler {
    /**
     * Input stream used only if acting as a socket handler.
     */
    private final BufferedReader socketInput;
    /**
     * Output stream used only if acting as a socket handler.
     */
    private final PrintWriter socketOutput;

    /**
     * Constructs a new ClientHandler for a Socket client connection.
     *
     * @param server            The main server instance.
     * @throws RemoteException If an RMI-related error occurs during object export.
     */
    public SocketClientHandler(ServerInterface server, InputStreamReader socketInput, OutputStreamWriter socketOutput) throws RemoteException {
        super(server);
        this.socketInput = new BufferedReader(socketInput);
        this.socketOutput = new PrintWriter(socketOutput);
    }


    @Override
    public void run() {
        // Thread for grabbing incoming messages from the model and forwarding
        // them to the connected client. The method used for sending the
        // messages is based on the communication technology chosen by the
        // client when starting their connection to the server.
        new Thread(() -> {
        while (true) {
            try {
                GameUpdate message = updatesForClientQueue.take();

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

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        }).start();

        // Thread for asynchronously reading the socket input stream for
        // commands sent by a client using socket technology. The received
        // input is converted into a UserInput object and passed to the
        // same execution function used by RMI clients.
        new Thread(() -> {
            while (true) {
                try {
                    String jsonCommand = socketInput.readLine();
                    JsonNode node = Json.parse(jsonCommand);
                    UserInput command = Json.fromJson(node, UserInput.class);

                    processUserInput(command);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }
}
