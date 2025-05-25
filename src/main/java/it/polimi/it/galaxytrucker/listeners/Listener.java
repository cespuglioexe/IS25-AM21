package it.polimi.it.galaxytrucker.listeners;

import it.polimi.it.galaxytrucker.messages.servermessages.GameUpdate;

/**
 * Represents an entity capable of receiving notifications in the form of
 * {@link GameUpdate} objects. Implementing classes define the behavior upon
 * receiving a command.
 *
 * @author Giacomo Amaducci
 * @version 1.0
 */
public interface Listener {

    /**
     * Notifies the listener of a new {@link GameUpdate} to process.
     *
     * @param update the {@link GameUpdate} object being sent to the listener
     */
    void notify(GameUpdate update);
}
