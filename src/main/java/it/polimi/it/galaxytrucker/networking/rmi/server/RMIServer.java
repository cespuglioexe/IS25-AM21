package it.polimi.it.galaxytrucker.networking.rmi.server;

import it.polimi.it.galaxytrucker.controller.Controller;
import it.polimi.it.galaxytrucker.networking.rmi.client.RMIVirtualServer;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class RMIServer extends UnicastRemoteObject implements RMIVirtualServer {

    final List<Controller> controllers;
    final List<RMIVirtualView> clients = new ArrayList<>();
    final List<RMIVirtualView> connectingClients = new ArrayList<>();

    public RMIServer() throws RemoteException {
        super();
        this.controllers = new ArrayList<>();
    }

    public static void main(String[] args) throws RemoteException {
        final String serverName = "server";

        RMIVirtualServer server = new RMIServer();
        Registry registry = LocateRegistry.createRegistry(1234);

        registry.rebind(serverName, server);

        System.out.println("Server bound");
    }

    @Override
    public void connect(RMIVirtualView client) throws RemoteException {
        synchronized (this.connectingClients) {
            this.connectingClients.add(client);
        }
    }

    @Override
    public void setUsername(RMIVirtualView client, String username) throws RemoteException, Exception {
        synchronized (this.clients) {
            for (RMIVirtualView c : this.clients) {
                if (c.getName().equals(username)) {
                    throw new Exception("Username already exists.");
                }
            }
            synchronized (this.connectingClients) {
                connectingClients.remove(client);
                clients.add(client);
            }

            synchronized (this.controllers) {
                client.sendAvailableGames(controllers);
            }
        }
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

    @Override
    public void addPlayerToGame(RMIVirtualView client, int gameNum) throws RemoteException {
        synchronized (this.clients) {
            synchronized (this.controllers) {
                controllers.get(gameNum).addPlayer(client);
            }
        }
    }

    @Override
    public void createNewGame(int playerNum, int level) throws RemoteException {
        synchronized (this.controllers) {
            controllers.add(new Controller(level, playerNum));
        }
    }
}
