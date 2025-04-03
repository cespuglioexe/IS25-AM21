package it.polimi.it.galaxytrucker.rmi.server;

import it.polimi.it.galaxytrucker.controller.Controller;
import it.polimi.it.galaxytrucker.rmi.client.RMIVirtualServer;
import it.polimi.it.galaxytrucker.rmi.server.*;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class RMIServer extends UnicastRemoteObject implements RMIVirtualServer {

    final Controller controller;
    final List<RMIVirtualView> clients = new ArrayList<>();

    public RMIServer() throws RemoteException {
        super();
        this.controller = new Controller();
    }

    public static void main(String[] args) throws RemoteException {
        final String serverName = "testRMIServer";

        RMIVirtualServer server = new RMIServer();
        Registry registry = LocateRegistry.createRegistry(1234);

        registry.rebind(serverName, server);

        System.out.println("Server bound");
    }

    @Override
    public void connect(RMIVirtualView client) throws RemoteException {
        synchronized (this.clients) {
            this.clients.add(client);
        }
    }

    @Override
    public void add(Integer number) throws RemoteException {
        System.err.println("add request received");
        this.controller.add(number);

        Integer currentState = this.controller.getCurrent();

        synchronized (this.clients) {
            for (RMIVirtualView client : this.clients) {
                client.showUpdate(currentState);
            }
        }
    }

    @Override
    public void reset() throws RemoteException {
        System.err.println("reset request received");
        boolean result = this.controller.reset();

        synchronized (this.clients) {
            if (result) {
                Integer currentState = this.controller.getCurrent();
                for (RMIVirtualView client : this.clients) {
                    client.showUpdate(currentState);
                }
            }
            else {
                for (RMIVirtualView client : this.clients) {
                    client.reportError("already reset");
                }
            }
        }
    }
}
