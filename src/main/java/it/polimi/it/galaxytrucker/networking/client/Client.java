package it.polimi.it.galaxytrucker.networking.client;

import it.polimi.it.galaxytrucker.commands.UserInput;
import it.polimi.it.galaxytrucker.controller.GenericGameData;
import it.polimi.it.galaxytrucker.networking.client.clientmodel.ClientModel;

import java.rmi.RemoteException;
import java.util.List;

public interface Client {
    ClientModel getModel();

    boolean isBuildingTimerIsActive();

    List<GenericGameData> getActiveGames() throws RemoteException;

    void receiveUserInput (UserInput input);
}
