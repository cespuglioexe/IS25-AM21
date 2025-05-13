package it.polimi.it.galaxytrucker.listeners;

/**
 * Represents an entity that allows classes implementing the {@link Listener}
 * interface to receive updates. Listeners should subscribe to updates with the
 * {@link #addListener(Listener)} function.
 * <p>
 * NOTE: classes implementing this interface should have a way of storing
 * references to their listeners, the recommended solution is a list: {@code List<Listener>}.
 *
 * @author giacomoamaducci
 * @version 1.0
 */
public interface Observable {
    public void addListener(Listener listener);
    public void removeListener(Listener listener);
}
