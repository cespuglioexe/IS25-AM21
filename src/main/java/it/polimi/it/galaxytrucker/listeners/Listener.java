package it.polimi.it.galaxytrucker.listeners;

import it.polimi.it.galaxytrucker.commands.Command;

/**
 * Represents an entity capable of receiving notifications in the form of {@code Command} objects.
 * Implementing classes define the behavior upon receiving a command.
 *
 * <p>
 *     This interface follows a typical observer pattern structure, where listeners
 *     react to events or changes signaled by other components in the system.
 * </p>
 *
 * @author Giacomo Amaducci
 * @version 1.0
 */
public interface Listener {

    /**
     * Notifies the listener of a new {@code Command} to process.
     *
     * @param command the {@code Command} object being sent to the listener
     */
    void notify(Command command);
}
