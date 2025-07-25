package it.polimi.it.galaxytrucker.networking.client;

import it.polimi.it.galaxytrucker.messages.clientmessages.UserInput;
import it.polimi.it.galaxytrucker.networking.client.clientmodel.ClientModel;

public interface ClientInterface {
    /**
     * Returns the client's model, this includes
     * @return
     */
    ClientModel getModel();

    /**
     * Returns whether the building time is active or not.
     *
     * @return {@code true} if the timer is active, {@code false} otherwise.
     */
    boolean isBuildingTimerIsActive();

    /**
     * Passes a {@link UserInput} object to the client to be interpreted
     * and converted into relevant game actions.
     *
     * @param input the {@code UserInput} object to be interpreted.
     */
    void receiveUserInput (UserInput input);
}
