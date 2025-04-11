package it.polimi.it.galaxytrucker.networking.rmi.server;

import it.polimi.it.galaxytrucker.networking.rmi.client.RMIVirtualServer;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import org.fusesource.jansi.AnsiConsole;

import static org.fusesource.jansi.Ansi.Color.*;
import static org.fusesource.jansi.Ansi.ansi;

public class RMIServer extends UnicastRemoteObject implements RMIVirtualServer {

    final List<RMIVirtualView> clients = Collections.synchronizedList(new ArrayList<>());
    final List<RMIVirtualView> connectingClients = Collections.synchronizedList(new ArrayList<>());

    public RMIServer() throws RemoteException {
        super();

    }

    public static void main(String[] args) throws RemoteException {
        final String serverName = "server";

        RMIVirtualServer server = new RMIServer();
        Registry registry = LocateRegistry.createRegistry(1234);

        registry.rebind(serverName, server);

        System.out.println("Server started");
        AnsiConsole.systemInstall();
        System.out.println( ansi().eraseScreen().fg(RED).a("Hello").fg(GREEN).a(" World").reset() );
    }

    @Override
    public void connect(RMIVirtualView client) throws RemoteException {
        synchronized (this.connectingClients) {
            System.out.println( ansi().eraseScreen().fg(WHITE).a(">>  Entered lock on [connectingClients]").reset() );

            this.connectingClients.add(client);
        }
        System.out.println( ansi().eraseScreen().fg(WHITE).a("<<  Exited lock on [connectingClients]").reset() );
    }

    @Override
    public void setUsername(RMIVirtualView client, String username) throws RemoteException, Exception {
        synchronized (this.clients) {
            System.out.println( ansi().eraseScreen().fg(WHITE).a(">>  Entered lock on [clients]").reset() );
            for (RMIVirtualView c : this.clients) {
                if (c.getName().equals(username)) {
                    throw new Exception("Username already exists.");
                }
            }
            synchronized (this.connectingClients) {
                System.out.println( ansi().eraseScreen().fg(WHITE).a(">>  Entered lock on [connectingClients]").reset() );
                connectingClients.remove(client);
                clients.add(client);
            }
            System.out.println( ansi().eraseScreen().fg(WHITE).a("<<  Exited lock on [connectingClients]").reset() );
        }
        System.out.println( ansi().eraseScreen().fg(WHITE).a("<<  Exited lock on [clients]").reset() );
    }

//    public void newGame() throws RemoteException {
//        synchronized (this.controller) {
//            this.controller = new Controller();
//        }
//    }

    @Override
    public void add(Integer number) throws RemoteException {
        System.err.println("add request received");
    }

    @Override
    public void reset() throws RemoteException {
        System.err.println("reset request received");
    }

}