package it.polimi.it.galaxytrucker.networking.client.rmi;

import it.polimi.it.galaxytrucker.messages.clientmessages.HeartBeatMessage;
import it.polimi.it.galaxytrucker.messages.servermessages.GameError;
import it.polimi.it.galaxytrucker.messages.clientmessages.UserInput;
import it.polimi.it.galaxytrucker.messages.clientmessages.UserInputType;
import it.polimi.it.galaxytrucker.messages.servermessages.GameUpdate;
import it.polimi.it.galaxytrucker.networking.client.Client;
import it.polimi.it.galaxytrucker.networking.client.ClientInterface;
import it.polimi.it.galaxytrucker.networking.server.rmi.RMIVirtualClient;
import it.polimi.it.galaxytrucker.networking.utils.ServerDetails;
import it.polimi.it.galaxytrucker.view.View;
import it.polimi.it.galaxytrucker.view.CLI.ConsoleColors;


import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Represents the RMI client.
 * <p>
 *     This class is responsible for establishing a connection with an RMI server,
 *     sending user inputs to the server, and receiving game updates and errors from the server.
 *     It implements {@link RMIVirtualClient} to be remotely accessible by the server,
 *     {@link Runnable} to handle its own connection logic in a separate thread, and
 *     {@link ClientInterface} to provide a common interface for client operations.
 * </p>
 *
 * @author giacomoamaducci
 * @version 1.5
 */
public class RMIClient extends Client implements RMIVirtualClient {
    /**
     * The remote reference to the server's virtual client handler.
     * This is used to send messages to the server.
     */
    private RMIVirtualServer server;

    String serverIp;
    String serverName;

    /**
     * Constructs a new RMIClient.
     * Initializes the client model and associates it with the provided view.
     *
     * @param view The user interface implementation for this client.
     * @throws RemoteException if an RMI communication error occurs during object export.
     */
    public RMIClient (View view) throws RemoteException {
        super(view);
    }

    @Override
    protected void initiateServerConnection() {
        boolean connected = false;
        Scanner scanner = new Scanner(System.in);

        do {
            System.out.print("Insert server IP address (leave empty for 'localhost')\n> ");
            String inputIp = scanner.nextLine();
            serverIp = inputIp.isEmpty() ? ServerDetails.DEFAULT_IP : inputIp;

            System.out.print("Insert server name (leave empty for default 'server')\n> ");
            String inputName = scanner.nextLine();
            serverName = inputName.isEmpty() ? ServerDetails.DEFAULT_RMI_NAME : inputName;

            try {
                Registry registry = LocateRegistry.getRegistry(serverIp, ServerDetails.RMI_DEFAULT_PORT);
                RMIServer connectionServer = ((RMIServer) registry.lookup(serverName));
                connectionServer.connect(this);
                connected = true;
            } catch (Exception e) {
                System.out.println(ConsoleColors.RED + "Failed to connect to '" + serverName + "'" + ConsoleColors.RESET);
            }
        } while (!connected);
    }

    @Override
    protected void sendHeartbeat() {
        try {
            server.receiveUserInput(new HeartBeatMessage());
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void sendMessageToClient(GameUpdate update) throws RemoteException {
        processServerUpdate(update);
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
     * Reports an error message received from the server to the client.
     *
     * @param error The {@link GameError} object containing details about the error.
     * @throws RemoteException if an RMI communication error occurs (though typically handled by the RMI runtime).
     */
    @Override
    public void reportErrorToClient(GameError error) throws RemoteException {
        // TODO: implement client error reporting logic
        System.err.println(ConsoleColors.RED + "Error from server: " + error.toString() + ConsoleColors.RESET);
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
            System.out.println(ConsoleColors.CLIENT_DEBUG + "sending message to server of type: " + input.getType() + ConsoleColors.RESET);
            try {
                if (server != null) {
                    server.receiveUserInput(input);
                } else {
                    System.err.println(ConsoleColors.RED + "Cannot send message, server handler is not set." + ConsoleColors.RESET);
                }
            } catch (RemoteException e) {
                System.err.println(ConsoleColors.RED + "Failed to send message to server" + ConsoleColors.RESET);
                e.printStackTrace();
                connectedToServer = false;



                ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
                scheduler.scheduleAtFixedRate(() -> {
                    try {
                        Registry registry = LocateRegistry.getRegistry(serverIp, ServerDetails.RMI_DEFAULT_PORT);
                        RMIServer connectionServer = ((RMIServer) registry.lookup(serverName));
                        connectionServer.connect(this);
                        connectedToServer = true;
                    } catch (Exception e1) {
                        System.out.println(ConsoleColors.RED + "Reconnection to server failed... Trying again." + ConsoleColors.RESET);
                    }
                    return;
                }, 0, 3, TimeUnit.SECONDS);

            }
        });
    }
}