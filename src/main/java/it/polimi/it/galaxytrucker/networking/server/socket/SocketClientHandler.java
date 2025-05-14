package it.polimi.it.galaxytrucker.networking.server.socket;


import it.polimi.it.galaxytrucker.commands.servercommands.GameUpdate;
import it.polimi.it.galaxytrucker.listeners.Listener;
import it.polimi.it.galaxytrucker.networking.CommunicationType;
import it.polimi.it.galaxytrucker.networking.server.ClientHandler;
import it.polimi.it.galaxytrucker.networking.server.Server;
import it.polimi.it.galaxytrucker.networking.server.ServerInterface;

import java.io.*;
import java.net.http.HttpRequest;
import java.rmi.RemoteException;

/**
 * Questa classe implementa la logica di interazione tra il server e un singolo client.
 */
public class SocketClientHandler extends ClientHandler implements Listener {

    Server server;
    BufferedReader input;
    PrintWriter output;

    protected SocketClientHandler(ServerInterface server, CommunicationType communicationType, InputStreamReader input, OutputStreamWriter output) throws RemoteException {
        super(server, communicationType, input, output);
    }

//    public SocketClientHandler(Server server, BufferedReader input, PrintWriter output) throws RemoteException {
//
//        this.server = server;
//        this.input = input;
//        this.output = output;
//    }

    @Override
    public void notify(GameUpdate update) {
        // TODO: forward game updates to remote socket client
    }

    //comunicazione dal client al server
    public void runVirtualView() throws IOException {
        String line;

        //TODO. Attenzione, qui non sto sfruttando il paradigma object oriented!
        while ((line = input.readLine()) != null) {
            // Reflection
            // Protocollo di serializzazione
            switch (line) {
                case "add" -> {
                    System.err.println("add request received");
                }
                case "reset" -> {
                    System.err.println("reset request received");
                }
                default -> System.err.println("[INVALID MESSAGE]");
            }
        }
    }
}
