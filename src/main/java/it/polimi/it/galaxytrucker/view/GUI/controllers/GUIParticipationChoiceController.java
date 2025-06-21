package it.polimi.it.galaxytrucker.view.GUI.controllers;

import javafx.fxml.FXMLLoader;

import java.io.IOException;
import java.util.Objects;

public class GUIParticipationChoiceController extends GUIViewState{

    private static GUIParticipationChoiceController instance;

    public static GUIParticipationChoiceController getInstance() {
        synchronized (GUIParticipationChoiceController.class) {
            if (instance == null) {
                instance = new GUIParticipationChoiceController();
            }
            return instance;
        }
    }

    public GUIParticipationChoiceController() {
        try {
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(GUIParticipationChoiceController.class.getResource("/it/polimi/it/galaxytrucker/fxmlstages/participationChoice.fxml")));
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
