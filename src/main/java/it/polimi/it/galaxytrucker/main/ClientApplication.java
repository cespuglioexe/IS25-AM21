package it.polimi.it.galaxytrucker.main;

import it.polimi.it.galaxytrucker.view.GUI.GUIApplication;
import javafx.application.Application;
import org.fusesource.jansi.AnsiConsole;

import java.io.IOException;
import java.util.Scanner;

public class ClientApplication {
    public static void main(String[] args) {
        Application.launch(GUIApplication.class);
    }
}
