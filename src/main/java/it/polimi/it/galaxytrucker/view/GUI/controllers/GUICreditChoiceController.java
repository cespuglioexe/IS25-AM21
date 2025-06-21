package it.polimi.it.galaxytrucker.view.GUI.controllers;

import javafx.fxml.FXMLLoader;

import java.io.IOException;
import java.util.Objects;

public class GUICreditChoiceController extends GUIViewState{

    private static GUICreditChoiceController instance;

    public static GUICreditChoiceController getInstance() {
        synchronized (GUICreditChoiceController.class) {
            if (instance == null) {
                instance = new GUICreditChoiceController();
            }
            return instance;
        }
    }

    public GUICreditChoiceController() {
        try {
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(GUICreditChoiceController.class.getResource("/it/polimi/it/galaxytrucker/fxmlstages/creditRewardChoice.fxml")));
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
