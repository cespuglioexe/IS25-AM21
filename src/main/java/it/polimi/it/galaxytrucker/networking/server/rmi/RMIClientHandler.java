package it.polimi.it.galaxytrucker.networking.server.rmi;

import it.polimi.it.galaxytrucker.messages.Message;
import it.polimi.it.galaxytrucker.messages.clientmessages.UserInput;
import it.polimi.it.galaxytrucker.messages.servermessages.GameUpdate;
import it.polimi.it.galaxytrucker.networking.client.rmi.RMIVirtualServer;
import it.polimi.it.galaxytrucker.networking.server.ClientHandler;
import it.polimi.it.galaxytrucker.networking.server.ServerInterface;
import it.polimi.it.galaxytrucker.view.CLI.ConsoleColors;

import java.rmi.RemoteException;

public class RMIClientHandler extends ClientHandler implements RMIVirtualServer {
    /**
     * Reference to the client, used for RMI clients. This is the remote object
     * that allows the server to invoke methods on the client.
     */
    private final RMIVirtualClient client;

    public RMIClientHandler(ServerInterface server, RMIVirtualClient client) throws RemoteException {
        super(server);
        this.client = client;
    }

    @Override
    public void run() {
        // Thread for grabbing incoming messages from the model and forwarding
        // them to the connected client. The method used for sending the
        // messages is based on the communication technology chosen by the
        // client when starting their connection to the server.
        new Thread(() -> {
            while (true) {
                GameUpdate message = null;
                try {
                    message = updatesForClientQueue.take();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                try {
                    client.sendMessageToClient(message);
                    System.out.println(ConsoleColors.CLIENT_HANDLER_DEBUG.tag(clientName) + "sent message of type " + message.getInstructionType() + ConsoleColors.RESET);
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }

    @Override
    public void receiveUserInput(Message userInput) throws RemoteException {
        processUserInput(userInput);
    }
}
