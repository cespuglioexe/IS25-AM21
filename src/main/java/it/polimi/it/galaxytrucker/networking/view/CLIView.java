package it.polimi.it.galaxytrucker.networking.view;

import it.polimi.it.galaxytrucker.controller.Controller;
import it.polimi.it.galaxytrucker.networking.rmi.client.RMIClient;
import it.polimi.it.galaxytrucker.networking.view.statePattern.StateMachine;
import it.polimi.it.galaxytrucker.networking.view.statePattern.viewstates.GameCreation;
import it.polimi.it.galaxytrucker.networking.view.statePattern.viewstates.GameSelection;
import java.util.List;

import java.rmi.RemoteException;

public class CLIView extends StateMachine {

    private final RMIClient client;

    public CLIView(RMIClient client) {
        this.client = client;
    }

    public void displayAvailableGames(List<Controller> games) throws RemoteException {
        if (games.isEmpty()) {
            System.out.println("There are no available games. Please create a new game.");
            changeState(new GameCreation(client));
        }
        else {
            System.out.println("Available games:");
            int i = 1;
            for (Controller game : games) {
                System.out.println(i + ": " + game.toString());
                i++;
            }
            changeState(new GameSelection(client));
        }
    }
}
