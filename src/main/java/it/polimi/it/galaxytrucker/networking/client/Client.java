package it.polimi.it.galaxytrucker.networking.client;

import it.polimi.it.galaxytrucker.commands.UserInput;
import it.polimi.it.galaxytrucker.controller.GenericGameData;
import it.polimi.it.galaxytrucker.networking.client.clientmodel.ClientModel;

import java.rmi.RemoteException;
import java.util.List;

public interface Client {
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
