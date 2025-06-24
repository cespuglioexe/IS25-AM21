package it.polimi.it.galaxytrucker.view.GUI.controllers;

import it.polimi.it.galaxytrucker.messages.clientmessages.UserInput;
import it.polimi.it.galaxytrucker.messages.clientmessages.UserInputType;
import it.polimi.it.galaxytrucker.view.GUI.GUIView;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class GUIUsernameSelection extends GUIViewState {

    @FXML public TextField nameField;
    @FXML public Button checkUsername;
    @FXML public Label usernameError;
    @FXML public VBox vbox;
    @FXML public ImageView backgroundImage;
    @FXML public ImageView titleImage;

    private static GUIUsernameSelection Instance;

    public static GUIUsernameSelection getInstance() {
        synchronized (GUIUsernameSelection.class) {
            if (Instance == null) {
                Instance = new GUIUsernameSelection();
            }
            return Instance;
        }
    }

    private GUIUsernameSelection() {
        try {
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(GUIUsernameSelection.class.getResource("/it/polimi/it/galaxytrucker/fxmlstages/usernameSelection.fxml")));
            loader.setController(this);
            root = loader.load();

            backgroundImage.setImage(new Image(Objects.requireNonNull(GUIUsernameSelection.class.getResourceAsStream("/it/polimi/it/galaxytrucker/graphics/general/generic_background.jpg"))));
            backgroundImage.setFitWidth(GUIView.screenSize.get(0));

            titleImage.setImage(new Image(Objects.requireNonNull(GUIUsernameSelection.class.getResourceAsStream("/it/polimi/it/galaxytrucker/graphics/general/title.png"))));
            titleImage.setFitHeight(GUIView.screenSize.get(1) * 0.3);



            double screenHeight = GUIView.screenSize.get(1);
            vbox.setPrefHeight(screenHeight * 0.5);
            vbox.setMaxHeight(screenHeight * 0.5);
            vbox.setTranslateY(-screenHeight * 0.05);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //---------------------------------------------------------//


    @FXML
    public void submitUsername(){
        GUIView.getInstance().getClient().receiveUserInput(
                new UserInput.UserInputBuilder(UserInputType.SET_PLAYER_USERNAME)
                        .setPlayerName(nameField.getText())
                        .build());
    }

    public void nameError(){
        Platform.runLater(() -> usernameError.setVisible(true));
    }

    public void nameSelectionSuccess() {
        Platform.runLater(() -> usernameError.setVisible(true));
        GUIView.getInstance().gameSelectionScreen();
    }

    @Override
    public void displayScene() {
        Platform.runLater(() -> {
            stage = (Stage) GUIView.stage.getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/it/polimi/it/galaxytrucker/cssstyles/futuristicUIStyles.css")).toExternalForm());

            stage.show();
        });
    }


}
