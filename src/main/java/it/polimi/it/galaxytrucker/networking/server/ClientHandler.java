package it.polimi.it.galaxytrucker.networking.server;

import it.polimi.it.galaxytrucker.controller.Controller;
import it.polimi.it.galaxytrucker.controller.ControllerInterface;
import it.polimi.it.galaxytrucker.listeners.Listener;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.UUID;

public abstract class ClientHandler extends UnicastRemoteObject implements Listener {
    private String username = "";
    private UUID uuid;
    private ControllerInterface controller;

    protected ClientHandler() throws RemoteException {
    }
    
    public void setUsername(String username) {
        this.username = username;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getUsername() {
        return username;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setController(ControllerInterface controller) {
        this.controller = controller;
    }

    public ControllerInterface getController() {
        return controller;
    }
}
