package it.polimi.it.galaxytrucker.view.GUI.controllers;

import javafx.fxml.FXMLLoader;

import java.io.IOException;
import java.util.Objects;

public class GUIGameTurn extends GUIViewState{

    private static GUIGameTurn instance;

    public static GUIGameTurn getInstance() {
        synchronized (GUIGameTurn.class) {
            if (instance == null) {
                instance = new GUIGameTurn();
            }
            return instance;
        }
    }

    public GUIGameTurn() {
        try {
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(GUIGameTurn.class.getResource("/it/polimi/it/galaxytrucker/fxmlstages/gameTurn.fxml")));
            loader.setController(this);
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void displayScene() {

    }
}
