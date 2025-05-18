package it.polimi.it.galaxytrucker.view.GUI.controllers;

import it.polimi.it.galaxytrucker.view.CLI.CLIViewStates.CLIViewState;
import it.polimi.it.galaxytrucker.view.GUI.GUIView;

import java.util.Scanner;

public abstract class GUIViewState {
    protected static GUIViewState currentState;
    protected static GUIView view;
    protected Scanner scanner = new Scanner(System.in);

    public static GUIViewState getCurrentState() {
        synchronized (CLIViewState.class) {
            return currentState;
        }
    }

    public static void setCurrentState(GUIViewState currentState) {
        synchronized (GUIViewState.class) {
            GUIViewState.currentState = currentState;
        }
    }
    public static void setView(GUIView view) {
        synchronized (GUIViewState.class) {
            GUIViewState.view = view;
        }
    }
    
    
}
