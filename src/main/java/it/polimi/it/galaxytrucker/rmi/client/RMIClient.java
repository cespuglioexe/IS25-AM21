package it.polimi.it.galaxytrucker.rmi.client;

import it.polimi.it.galaxytrucker.rmi.server.RMIVirtualView;
import it.polimi.it.galaxytrucker.rmi.client.*;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

public class RMIClient extends UnicastRemoteObject implements RMIVirtualView {

    final RMIVirtualServer server;

    public RMIClient (RMIVirtualServer server) throws RemoteException {
        super();
        this.server = server;
    }

    public static void main(String[] args) throws RemoteException, NotBoundException {
        final String serverName = "testRMIServer";

        Registry registry = LocateRegistry.getRegistry(args[0], 1234);
        RMIVirtualServer server = (RMIVirtualServer) registry.lookup(serverName);

        new RMIClient(server).run();
    }

    private void run() throws RemoteException {
        this.server.connect(this);
        this.runCli();
    }

    private void runCli() throws RemoteException {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("> ");
            int command = scanner.nextInt();

            if (command == 0) {
                server.reset();
            }
            else {
                server.add(command);
            }
        }
    }

    @Override
    public void showUpdate(Integer number) throws RemoteException {
        System.out.println("Server update: " + number);
    }

    @Override
    public void reportError(String message) throws RemoteException {
        System.err.println("Server error: " + message);
    }
}
