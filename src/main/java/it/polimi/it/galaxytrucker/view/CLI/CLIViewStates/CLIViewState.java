package it.polimi.it.galaxytrucker.view.CLI.CLIViewStates;

import it.polimi.it.galaxytrucker.exceptions.InvalidFunctionCallInState;
import it.polimi.it.galaxytrucker.model.componenttiles.TileData;
import it.polimi.it.galaxytrucker.view.View;

import java.util.Scanner;

public abstract class CLIViewState {
    protected static CLIViewState currentState;
    protected static View view;
    protected Scanner scanner = new Scanner(System.in);

    public static CLIViewState getCurrentState() {
        synchronized (CLIViewState.class) {
            return currentState;
        }
    }

    public static void setCurrentState(CLIViewState currentState) {
        synchronized (CLIViewState.class) {
            CLIViewState.currentState = currentState;
        }
    }

    public static void setView(View view) {
        synchronized (CLIViewState.class) {
            CLIViewState.view = view;
        }
    }

    public abstract void executeState();

    public void gameCreationSuccess(boolean success) {
        throw new InvalidFunctionCallInState("Can't call this function in state " + currentState.getClass().getSimpleName());
    }

    public void joinedGameIsFull() {
        throw new InvalidFunctionCallInState("Can't call this function in state " + currentState.getClass().getSimpleName());
    }

    public void remoteExceptionThrown() {
        throw new InvalidFunctionCallInState("Can't call this function in state " + currentState.getClass().getSimpleName());
    }

    public void nameNotAvailable() {
        throw new InvalidFunctionCallInState("Can't call this function in state " + currentState.getClass().getSimpleName());
    }

    public void displayComponentTile(TileData newTile) {
        throw new InvalidFunctionCallInState("Can't call this function in state " + currentState.getClass().getSimpleName());
    }
}
