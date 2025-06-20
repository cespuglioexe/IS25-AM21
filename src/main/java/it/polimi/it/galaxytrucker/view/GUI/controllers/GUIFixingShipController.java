package it.polimi.it.galaxytrucker.view.GUI.controllers;

import it.polimi.it.galaxytrucker.messages.clientmessages.UserInput;
import it.polimi.it.galaxytrucker.messages.clientmessages.UserInputType;
import it.polimi.it.galaxytrucker.model.componenttiles.OutOfBoundsTile;
import it.polimi.it.galaxytrucker.model.componenttiles.TileData;
import it.polimi.it.galaxytrucker.view.GUI.GUIView;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class GUIFixingShipController extends GUIViewState{

    private static GUIFixingShipController instance;
    @FXML
    private ImageView shipBgImage;
    @FXML
    private ImageView imageTile57,imageTile66,imageTile75,imageTile85,imageTile95,imageTile86,imageTile96,imageTile76,imageTile,imageTile67,imageTile68,imageTile78,imageTile88,imageTile98,imageTile99,imageTile89,imageTile79,imageTile77,imageTile87,imageTile56,imageTile58,imageTile65,imageTile74,imageTile84,imageTile94,imageTile910,imageTile810,imageTile710,imageTile69;
    private Map<String, ImageView> imageTiles = new HashMap<>();
    private int rotation;

    @FXML
    private TextField removeXcoord,removeYcoord;

    public static GUIFixingShipController getInstance() {
        synchronized (GUIFixingShipController.class) {
            if (instance == null) {
                instance = new GUIFixingShipController();
            }
            return instance;
        }
    }

    public GUIFixingShipController() {
        try {
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(GUIFixingShipController.class.getResource("/it/polimi/it/galaxytrucker/fxmlstages/fixingShip.fxml")));
            loader.setController(this);
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void displayScene() {
        Platform.runLater(() -> {

            shipBgImage.setImage(new Image(Objects.requireNonNull(GUIFixingShipController.class.getResourceAsStream("/it/polimi/it/galaxytrucker/graphics/cardboard/shipboard-lvl" + GUIView.getInstance().getClient().getModel().getGameLevel() + ".jpg"))));
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
            stage = (Stage) GUIView.stage.getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        });
    }
    public void updateShip(){
        Platform.runLater(() -> {
            String componentGraphic = "";
            String imgViewNumber = "";
            int rowcount = 5, colcount = 4, rotation;
            List<List<TileData>> ship = GUIView.getInstance().getClient().getModel().getPlayerShips(GUIView.getInstance().getClient().getModel().getMyData().getPlayerId());
            for (List<TileData> row : ship) {
                for (TileData tileData : row) {
                    if(!tileData.type().equals(OutOfBoundsTile.class.getSimpleName())){
                        if (tileData !=null) {
                            componentGraphic = tileData.graphicPath();
                            rotation = tileData.rotation();

                            imgViewNumber = imgViewNumber + rowcount + colcount;
                            imageTiles.get(imgViewNumber).setImage(new Image(Objects.requireNonNull(GUIFixingShipController.class.getResourceAsStream(componentGraphic))));
                            imageTiles.get(imgViewNumber).setRotate(90 * rotation);
                        }else{
                            System.out.println("nell'else");
                            imgViewNumber = imgViewNumber + rowcount + colcount;
                            imageTiles.get(imgViewNumber).setImage(null);
                        }
                    }

                    imgViewNumber = "";
                    colcount++;
                }
                colcount = 4;
                rowcount++;
            }
        });
        rotation = 0;
    }



    @FXML
    public void removeSelectedTile() {
        int col = Integer.parseInt(removeXcoord.getText());;
        int row= Integer.parseInt(removeYcoord.getText());
        GUIView.getInstance().getClient().receiveUserInput(
                new UserInput.UserInputBuilder(UserInputType.REMOVE_COMPONENT)
                        .setCoords(col,row)
                        .build()
        );


        updateShip();
    }
}


