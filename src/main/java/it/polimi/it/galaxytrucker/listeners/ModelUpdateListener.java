package it.polimi.it.galaxytrucker.listeners;

import it.polimi.it.galaxytrucker.commands.Command;
import it.polimi.it.galaxytrucker.networking.VirtualClient;

public class ModelUpdateListener implements Listener {

    private final VirtualClient client;

    public ModelUpdateListener(VirtualClient client) {
        this.client = client;
    }

    @Override
    public void notify(Command command) {

    }
}
