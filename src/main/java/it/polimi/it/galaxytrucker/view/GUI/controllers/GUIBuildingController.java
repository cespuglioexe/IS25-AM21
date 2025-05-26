package it.polimi.it.galaxytrucker.view.GUI.controllers;

import it.polimi.it.galaxytrucker.messages.clientmessages.UserInput;
import it.polimi.it.galaxytrucker.messages.clientmessages.UserInputType;
import it.polimi.it.galaxytrucker.model.componenttiles.TileData;
import it.polimi.it.galaxytrucker.networking.client.clientmodel.ClientModel;
import it.polimi.it.galaxytrucker.view.CLI.ConsoleColors;
import it.polimi.it.galaxytrucker.view.GUI.GUIView;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class GUIBuildingController extends GUIViewState {

    @FXML
    private TextField xCoordText,yCoordText, rotationInput;
    @FXML
    private Pane displayShipPane;
    @FXML
    private Label timerSeconds,showTile;
    @FXML
    private ImageView shipBgImage,tileImageView;
    private static GUIBuildingController instance;

    private Map<String, ImageView> imageTiles = new HashMap<>();

    @FXML
    private ImageView imageTile57,imageTile66,imageTile75,imageTile85,imageTile95,imageTile86,imageTile96,imageTile76,imageTile,imageTile67,imageTile68,imageTile78,imageTile88,imageTile98,imageTile99,imageTile89,imageTile79,imageTile77,imageTile87,imageTile56,imageTile58,imageTile65,imageTile74,imageTile84,imageTile94,imageTile910,imageTile810,imageTile710,imageTile69;

    public static GUIBuildingController getInstance() {
        synchronized (GUIUsernameSelection.class) {
            if (instance == null) {
                instance = new GUIBuildingController();
            }
            return instance;
        }
    }

    public GUIBuildingController() {
       try {
           FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(GUITitleScreen.class.getResource("/it/polimi/it/galaxytrucker/fxmlstages/buildingPhase.fxml")));
           loader.setController(this);
           root = loader.load();

//           while (GUIView.getInstance().getClient().getModel().getGameLevel() == 0) {
//               Thread.onSpinWait();
//           }

           // System.out.println("Setting level to :" + GUIView.getInstance().getClient().getModel().getGameLevel());
           // shipBgImage.setImage(new Image(Objects.requireNonNull(GUITitleScreen.class.getResourceAsStream("/it/polimi/it/galaxytrucker/graphics/cardboard/shipboard-lvl" + GUIView.getInstance().getClient().getModel().getGameLevel() + ".jpg"))));

       } catch (IOException e) {
           e.printStackTrace();
       }
    }

    @FXML
    public void newRandomTile(){
        GUIView.getInstance().getClient().receiveUserInput(
                new UserInput.UserInputBuilder(UserInputType.SELECT_RANDOM_COMPONENT)
                        .build()
        );

        String randomTileGraphic = "/it/polimi/it/galaxytrucker/graphics/tiles/GT-component_tile_12.jpg";
        tileImageView.setImage(new Image(Objects.requireNonNull(GUITitleScreen.class.getResourceAsStream(randomTileGraphic))));
    }

    @FXML
    public void placeTile(){
        int x,y,rotation;
        x=Integer.parseInt(xCoordText.getText());
        y=Integer.parseInt(yCoordText.getText());
        rotation = Integer.parseInt(rotationInput.getText());

        GUIView.getInstance().getClient().receiveUserInput(
                new UserInput.UserInputBuilder(UserInputType.PLACE_COMPONENT)
                        .setCoords(x, y)
                        .setRotation(rotation)
                        .build()
        );

        String componentGraphic = "/it/polimi/it/galaxytrucker/graphics/tiles/GT-component_tile_1.jpg";
        String imgViewNumber = ""+x+y;
        imageTiles.get("57").setImage(new Image(Objects.requireNonNull(GUITitleScreen.class.getResourceAsStream(componentGraphic))));
        imageTiles.get("57").setRotate(90*rotation);
        System.out.println("tile placed");
    }



    @FXML
    public void saveTile(){
        GUIView.getInstance().getClient().receiveUserInput(
                new UserInput.UserInputBuilder(UserInputType.SAVE_SELECTED_COMPONENT)
                        .build()
        );

        System.out.println("tile saved");
    }

    @FXML
    public void discardTile(){
        GUIView.getInstance().getClient().receiveUserInput(
                new UserInput.UserInputBuilder(UserInputType.DISCARD_SELECTED_COMPONENT)
                        .build()
        );
        System.out.println("tile discarded");
    }

    @FXML
    public void startTimer() {
        if (!GUIView.getInstance().getClient().isBuildingTimerIsActive()) {
            GUIView.getInstance().getClient().receiveUserInput(
                    new UserInput.UserInputBuilder(UserInputType.RESTART_BUILDING_TIMER)
                            .build()
            );
        }
    }


    public void showSavedTile(){
        ClientModel model = GUIView.getInstance().getClient().getModel();
        List<TileData> savedTiles = model.getSavedTiles();
        GUIView.getInstance().displayTiles(savedTiles);
        showTile.setText(savedTiles.toString());
    }


    public void showDiscardedTile(){
        ClientModel model = GUIView.getInstance().getClient().getModel();
        List<TileData> discardedTiles = model.getDiscardedTiles();
        GUIView.getInstance().displayTiles(discardedTiles);
        //showTile.setText(discardedTiles.toString());
        tileImageView.setImage(new Image(Objects.requireNonNull(GUITitleScreen.class.getResourceAsStream("/it/polimi/it/galaxytrucker/graphics//"))));

    }


    @Override
    public void displayScene() {
        Platform.runLater(() -> {
            System.out.println(ConsoleColors.CLIENT_DEBUG + "displaying scene building");
            System.out.println(GUIView.getInstance().getClient().getModel().getGameLevel());
            shipBgImage.setImage(new Image(Objects.requireNonNull(GUITitleScreen.class.getResourceAsStream("/it/polimi/it/galaxytrucker/graphics/cardboard/shipboard-lvl" + GUIView.getInstance().getClient().getModel().getGameLevel() + ".jpg"))));

            imageTiles.put("57", imageTile57);
            //imageTiles.put("", );


            stage = (Stage) GUIView.stage.getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);

            stage.show();
        });
    }
}
