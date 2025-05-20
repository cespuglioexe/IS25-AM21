package it.polimi.it.galaxytrucker.view.GUI;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;

import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class GUIApplication extends Application {
    @Override
    public void start(Stage stage) {
        //stage.setFullScreen(true);
        stage.setResizable(false);
        GUIView.stage = stage;

        Screen screen = Screen.getPrimary();
        stage.setWidth(screen.getVisualBounds().getWidth());
        stage.setHeight(screen.getVisualBounds().getHeight());

        GUIView.screenSize = List.of(screen.getVisualBounds().getWidth(), screen.getVisualBounds().getHeight());

        stage.setScene(new Scene(new Pane()));

        stage.setTitle("Galaxy Trucker");

        stage.setFullScreenExitHint("Press ESC to exit fullscreen. If you want to re-enter fullscreen afterwards, press F11.");

        stage.addEventHandler(KeyEvent.KEY_PRESSED, (event) -> {
            if (event.getCode().equals(KeyCode.ESCAPE)) {
                stage.setResizable(true);
                stage.setFullScreen(false);
                stage.setMaximized(true);
                stage.setResizable(false);
            }
        });

        stage.addEventHandler(KeyEvent.KEY_PRESSED, (event) -> {
            if (event.getCode().equals(KeyCode.F11)) {
                stage.setFullScreen(true);
            }
        });

        stage.setOnCloseRequest((event) -> System.exit(1));
    }
}
