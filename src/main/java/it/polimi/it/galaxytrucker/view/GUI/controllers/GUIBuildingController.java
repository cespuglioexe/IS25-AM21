package it.polimi.it.galaxytrucker.view.GUI.controllers;

import it.polimi.it.galaxytrucker.messages.clientmessages.UserInput;
import it.polimi.it.galaxytrucker.messages.clientmessages.UserInputType;
import it.polimi.it.galaxytrucker.model.componenttiles.OutOfBoundsTile;
import it.polimi.it.galaxytrucker.model.componenttiles.TileData;
import it.polimi.it.galaxytrucker.networking.client.clientmodel.ClientModel;
import it.polimi.it.galaxytrucker.view.CLI.ConsoleColors;
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
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class GUIBuildingController extends GUIViewState {

    @FXML
    private TextField xCoordText,yCoordText, rotationInput;
    @FXML
    private Pane displayShipPane;
    @FXML
    private Label timerSeconds,showTile;
    @FXML
    private ImageView shipBgImage,tileImageView;
    @FXML
    private Button arrowForward,arrowBack;
    @FXML
    private Button rotationButton;
    @FXML
    private Pane popUpCardsPane,handPane;
    @FXML
    private ImageView pile0,pile1,pile2,card0,card1,card2;

    private static GUIBuildingController instance;
    private int rotation;
    private int savedSelected = -1,discardedSelected = -1;
    private Map<String, ImageView> imageTiles = new HashMap<>();
    private List<String> pileCards = new ArrayList<>();


    @FXML
    private ImageView imageTile57,imageTile66,imageTile75,imageTile85,imageTile95,imageTile86,imageTile96,imageTile76,imageTile,imageTile67,imageTile68,imageTile78,imageTile88,imageTile98,imageTile99,imageTile89,imageTile79,imageTile77,imageTile87,imageTile56,imageTile58,imageTile65,imageTile74,imageTile84,imageTile94,imageTile910,imageTile810,imageTile710,imageTile69;

    public static GUIBuildingController getInstance() {
        synchronized (GUIBuildingController.class) {
            if (instance == null) {
                instance = new GUIBuildingController();
            }
            return instance;
        }
    }

    public GUIBuildingController() {
        try {
           FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(GUIBuildingController.class.getResource("/it/polimi/it/galaxytrucker/fxmlstages/buildingPhase.fxml")));
           loader.setController(this);
           root = loader.load();
       } catch (IOException e) {
           e.printStackTrace();
       }



    }

    @FXML
    public void newRandomTile(){
        savedSelected = -1;
        discardedSelected = -1;
        rotation = 0;
        arrowForward.setVisible(false);
        arrowBack.setVisible(false);
        GUIView.getInstance().getClient().receiveUserInput(
                new UserInput.UserInputBuilder(UserInputType.SELECT_RANDOM_COMPONENT)
                        .build()
        );
    }


    public void updateVisibleTile(TileData tile){
        Platform.runLater(() -> {
            System.out.println(tile.type());
            String randomTileGraphic = tile.graphicPath();
            tileImageView.setImage(new Image(Objects.requireNonNull(GUIBuildingController.class.getResourceAsStream(randomTileGraphic))));
            tileImageView.setRotate(rotation*90);
            rotationButton.setOnAction(e -> {
                rotation++;
                updateVisibleTile(tile);
            });
        });
    }


    @FXML
    public void placeTile(){

        //------CONTROLLO SU PEZZO SALVATO O SCARTATO-------//
        if(savedSelected!=-1){
            GUIView.getInstance().getClient().receiveUserInput(
                    new UserInput.UserInputBuilder(UserInputType.SELECT_SAVED_COMPONENT)
                            .setSelectedTileIndex(savedSelected)
                            .build()
            );

            savedSelected = -1;
        }
        if (discardedSelected!=-1){
            GUIView.getInstance().getClient().receiveUserInput(
                    new UserInput.UserInputBuilder(UserInputType.SELECT_DISCARDED_COMPONENT)
                            .setSelectedTileIndex(discardedSelected)
                            .build()
            );
            discardedSelected = -1;
        }
        //--------------------------------------------------------//

        int x,y;
        x=Integer.parseInt(xCoordText.getText());
        y=Integer.parseInt(yCoordText.getText());


        GUIView.getInstance().getClient().receiveUserInput(
                new UserInput.UserInputBuilder(UserInputType.PLACE_COMPONENT)
                        .setCoords(x, y)
                        .setRotation(rotation)
                        .build()
        );

    }

    public void updateShip(){
        Platform.runLater(() -> {
            String componentGraphic = "";
            String imgViewNumber = "";
            int rowcount = 5, colcount = 4, rotation;
            List<List<TileData>> ship = GUIView.getInstance().getClient().getModel().getPlayerShips(GUIView.getInstance().getClient().getModel().getMyData().getPlayerId());
            for (List<TileData> row : ship) {
                for (TileData tileData : row) {
                    if (tileData != null && !tileData.type().equals(OutOfBoundsTile.class.getSimpleName())) {
                        componentGraphic = tileData.graphicPath();
                        rotation = tileData.rotation();

                        imgViewNumber = imgViewNumber + rowcount + colcount;
                        imageTiles.get(imgViewNumber).setImage(new Image(Objects.requireNonNull(GUIBuildingController.class.getResourceAsStream(componentGraphic))));
                        imageTiles.get(imgViewNumber).setRotate(90 * rotation);
                    }
                    imgViewNumber = "";
                    colcount++;
                }
                colcount = 4;
                rowcount++;
            }
        });
        rotation = 0;
        tileImageView.setImage(null);

    }


    @FXML
    public void saveTile(){
        GUIView.getInstance().getClient().receiveUserInput(
                new UserInput.UserInputBuilder(UserInputType.SAVE_SELECTED_COMPONENT)
                        .build()
        );
        tileImageView.setImage(null);
        System.out.println("tile saved");
    }

    @FXML
    public void discardTile(){
        GUIView.getInstance().getClient().receiveUserInput(
                new UserInput.UserInputBuilder(UserInputType.DISCARD_SELECTED_COMPONENT)
                        .build()
        );
        tileImageView.setImage(null);
        System.out.println("tile discarded");
    }




    public void showSavedTile(){
        ClientModel model = GUIView.getInstance().getClient().getModel();
        List<TileData> savedTiles = model.getSavedTiles();
        AtomicInteger index= new AtomicInteger();
        rotation = 0;
        updateVisibleTile(savedTiles.get(0));
        savedSelected=0;
        arrowForward.setVisible(true);
        arrowBack.setVisible(true);

        arrowBack.setOnAction(e -> {
            System.out.println("Saved tile index: "+index);
            if (!savedTiles.isEmpty()) {
                index.set((index.get() - 1 + savedTiles.size()) % savedTiles.size());
                updateVisibleTile(savedTiles.get(index.get()));

            }else{
                tileImageView.setImage(null);
            }
            savedSelected = index.get();

        });

        arrowForward.setOnAction(e -> {
            System.out.println("Saved tile index: "+index);

            if (!savedTiles.isEmpty()) {
                index.set((index.get() + 1 + savedTiles.size()) % savedTiles.size());
                updateVisibleTile(savedTiles.get(index.get()));

            }else{
                tileImageView.setImage(null);
            }
            savedSelected = index.get();
        });



    }


    public void showDiscardedTile(){
        rotation=0;
        ClientModel model = GUIView.getInstance().getClient().getModel();
        List<TileData> discardedTiles = model.getDiscardedTiles();
        AtomicInteger index= new AtomicInteger();

        updateVisibleTile(discardedTiles.get(0));
        discardedSelected=0;
        arrowForward.setVisible(true);
        arrowBack.setVisible(true);

        arrowBack.setOnAction(e -> {
            System.out.println("Saved tile index: "+index);
            if (!discardedTiles.isEmpty()) {
                index.set((index.get() - 1 + discardedTiles.size()) % discardedTiles.size());
                updateVisibleTile(discardedTiles.get(index.get()));

            }else{
                tileImageView.setImage(null);
            }
            discardedSelected = index.get();

        });

        arrowForward.setOnAction(e -> {
            System.out.println("Saved tile index: "+index);

            if (!discardedTiles.isEmpty()) {
                index.set((index.get() + 1 + discardedTiles.size()) % discardedTiles.size());
                updateVisibleTile(discardedTiles.get(index.get()));

            }else{
                tileImageView.setImage(null);
            }
            discardedSelected = index.get();
        });
        //da fare la scelta del tile
    }


    @Override
    public void displayScene() {
        Platform.runLater(() -> {

            shipBgImage.setImage(new Image(Objects.requireNonNull(GUIBuildingController.class.getResourceAsStream("/it/polimi/it/galaxytrucker/graphics/cardboard/shipboard-lvl" + GUIView.getInstance().getClient().getModel().getGameLevel() + ".jpg"))));
            imageTiles.put("57", imageTile57);
            imageTiles.put("66", imageTile66);
            imageTiles.put("75", imageTile75);
            imageTiles.put("85", imageTile85);
            imageTiles.put("95", imageTile95);
            imageTiles.put("86", imageTile86);
            imageTiles.put("96", imageTile96);
            imageTiles.put("76", imageTile76);
            imageTiles.put("67", imageTile67);
            imageTiles.put("68", imageTile68);
            imageTiles.put("78", imageTile78);
            imageTiles.put("88", imageTile88);
            imageTiles.put("98", imageTile98);
            imageTiles.put("99", imageTile99);
            imageTiles.put("89", imageTile89);
            imageTiles.put("79", imageTile79);
            imageTiles.put("77", imageTile77);
            imageTiles.put("87", imageTile87);
            imageTiles.put("56", imageTile56);
            imageTiles.put("58", imageTile58);
            imageTiles.put("65", imageTile65);
            imageTiles.put("74", imageTile74);
            imageTiles.put("84", imageTile84);
            imageTiles.put("94", imageTile94);
            imageTiles.put("910", imageTile910);
            imageTiles.put("810", imageTile810);
            imageTiles.put("710", imageTile710);
            imageTiles.put("69", imageTile69);

            updateShip();
            System.out.println("After UpdateShip");
            stage = (Stage) GUIView.stage.getScene().getWindow();
            System.out.println("After stage");
            scene = new Scene(root);
            System.out.println("After newscene");
            stage.setScene(scene);
            System.out.println("Before Show");
            stage.show();
        });
    }

    @FXML
    public void openPopUpCards(){
        popUpCardsPane.setVisible(true);
        displayCards();

    }

    public void displayCards(){
        ImageView[] images = {pile0, pile1, pile2 };

        for (int i = 0; i < images.length; i++) {
            int pileIndex = i;
            images[i].setOnMouseClicked(e -> {
                System.out.println("Hai cliccato sull'immagine " + pileIndex);
                pileCards=GUIView.getInstance().getClient().getModel().getCardPile(pileIndex);
                showHandCards(pileCards);
            });
        }
    }

    @FXML
    public void showHandCards(List<String> pileCards){
        handPane.setVisible(true);
        if(pileCards.size()==2){
            card0.setImage(new Image(Objects.requireNonNull(GUIBuildingController.class.getResourceAsStream("/it/polimi/it/galaxytrucker/graphics/cards/"+pileCards.get(0)))));
            card1.setImage(new Image(Objects.requireNonNull(GUIBuildingController.class.getResourceAsStream("/it/polimi/it/galaxytrucker/graphics/cards/"+pileCards.get(1)))));
            card2.setVisible(false);
        }
        if(pileCards.size()==3){
            card0.setImage(new Image(Objects.requireNonNull(GUIBuildingController.class.getResourceAsStream("/it/polimi/it/galaxytrucker/graphics/cards/"+pileCards.get(0)))));
            card1.setImage(new Image(Objects.requireNonNull(GUIBuildingController.class.getResourceAsStream("/it/polimi/it/galaxytrucker/graphics/cards/"+pileCards.get(1)))));
            card2.setVisible(true);
            card2.setImage(new Image(Objects.requireNonNull(GUIBuildingController.class.getResourceAsStream("/it/polimi/it/galaxytrucker/graphics/cards/"+pileCards.get(2)))));
        }
    }

    @FXML
    public void closePopUpCards(){
        handPane.setVisible(false);
        popUpCardsPane.setVisible(false);
    }


    public void endBuilding(){
        GUIView.getInstance().getClient().receiveUserInput(
                new UserInput.UserInputBuilder(UserInputType.CONFIRM_BUILDING_END)
                        .build()
        );
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
}
