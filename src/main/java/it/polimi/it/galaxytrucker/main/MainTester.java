package it.polimi.it.galaxytrucker.main;

import it.polimi.it.galaxytrucker.view.GUI.GUIApplication;
import it.polimi.it.galaxytrucker.view.GUI.GUIView;
import javafx.application.Application;

public class MainTester {
    public static void main(String[] args) {
        new Thread(() -> Application.launch(GUIApplication.class)).start();
        GUIView.getInstance();
    }
}
