package it.polimi.it.galaxytrucker.commands;

import java.io.Serializable;

/**
 * Represents a generic command object.
 * <p>
 *     This interface doesn't implement any functionality. The extension of
 *     {@code Serializable} allows commands to be serialised and sent to
 *     remote objects over the network.
 * </p>
 */
public interface Command extends Serializable {
}
