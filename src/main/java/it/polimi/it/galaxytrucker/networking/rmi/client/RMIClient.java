package it.polimi.it.galaxytrucker.networking.rmi.client;

import it.polimi.it.galaxytrucker.networking.rmi.server.RMIVirtualView;
import it.polimi.it.galaxytrucker.networking.view.CLIView;
import it.polimi.it.galaxytrucker.networking.view.listeners.DoubleEventListener;
import it.polimi.it.galaxytrucker.networking.view.listeners.EventType;
import it.polimi.it.galaxytrucker.networking.view.listeners.StringEventListener;
import it.polimi.it.galaxytrucker.networking.view.statePattern.viewstates.ConnectionState;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.Scanner;

public class RMIClient extends UnicastRemoteObject implements RMIVirtualView, StringEventListener {

    final CLIView view;
    RMIVirtualServer server;
    private String name;

    public RMIClient () throws RemoteException {
        super();
        this.view = new CLIView(this);
    }

    public static void main(String[] args) throws RemoteException, NotBoundException {
        new RMIClient().run();
    }

    private void run() throws RemoteException {
        view.start(new ConnectionState(this));
        this.server.connect(this);
    }

    @Override
    public String getName() throws RemoteException {
        return name;
    }


    private String nameRequest() throws RemoteException {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Insert name below\n> ");

        return scanner.nextLine();
    }

    @Override
    public void showUpdate(Integer number) throws RemoteException {
        System.out.println("Server update: " + number);
    }

    @Override
    public void reportError(String message) throws RemoteException {
        System.err.println("Server error: " + message);
    }

    @Override
    public void onStringEvent(EventType eventType, String string) {
        switch (eventType) {
            case SERVER_NAME:
                try {
                    Registry registry = LocateRegistry.getRegistry("127.0.0.1", 1234);
                    this.server = (RMIVirtualServer) registry.lookup(string);
                    this.server.connect(this);
                    System.out.println("Connected to server.");

                    view.updateState(false);

                } catch (Exception e) {
                    System.err.println("Failed to connect to '" + string + "'. Please try again.");
                    view.updateState(true);
                }
                break;
            case USERNAME:
                try {
                    name = string;
                    server.setUsername(this, name);

                    view.updateState(false);
                }
                catch (RemoteException e) {
                    System.err.println("Failed to set username to '" + string + "'. Please try again.");
                    view.updateState(true);
                }
                catch (Exception e) {
                    System.err.println(e.getMessage());
                    view.updateState(true);
                }
        }

    }

}
