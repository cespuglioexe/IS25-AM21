package it.polimi.it.galaxytrucker.view.GUI;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.util.Objects;

public class GUIApplication extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        stage.setFullScreen(true);
        stage.setResizable(false);

        Screen screen = Screen.getPrimary();
        stage.setWidth(screen.getVisualBounds().getWidth());
        stage.setHeight(screen.getVisualBounds().getHeight());

        Scene stageScene = new Scene(new Pane(), stage.getWidth(), stage.getHeight());
        AnchorPane initializationPane = new AnchorPane();
        initializationPane.setStyle("-fx-background-color: #00134e;");
        stageScene.setRoot(initializationPane);
        stage.setScene(stageScene);

        stage.setTitle("Galaxy Trucker");
        stage.setFullScreenExitHint("Press ESC to exit fullscreen. If you want to re-enter fullscreen afterwards, press F7.");

        stage.addEventHandler(KeyEvent.KEY_PRESSED, (event) -> {
            if (event.getCode().equals(KeyCode.ESCAPE)) {
                stage.setResizable(true);
                stage.setFullScreen(false);
                stage.setMaximized(true);
                stage.setResizable(false);
            }
        });

        stage.addEventHandler(KeyEvent.KEY_PRESSED, (event) -> {
            if (event.getCode().equals(KeyCode.F7)) {
                stage.setFullScreen(true);
            }
        });

        stage.setOnCloseRequest((event) -> System.exit(1));

        stage.show();
    }
}
