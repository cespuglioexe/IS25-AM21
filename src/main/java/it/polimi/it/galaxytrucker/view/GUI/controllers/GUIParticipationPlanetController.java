package it.polimi.it.galaxytrucker.view.GUI.controllers;

import javafx.fxml.FXMLLoader;

import java.io.IOException;
import java.util.Objects;

public class GUIParticipationPlanetController extends GUIViewState{

    private static GUIParticipationPlanetController instance;

    public static GUIParticipationPlanetController getInstance() {
        synchronized (GUIParticipationPlanetController.class) {
            if (instance == null) {
                instance = new GUIParticipationPlanetController();
            }
            return instance;
        }
    }

    public GUIParticipationPlanetController() {
        try {
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(GUIParticipationPlanetController.class.getResource("/it/polimi/it/galaxytrucker/fxmlstages/participationPlanetChoice.fxml")));
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
