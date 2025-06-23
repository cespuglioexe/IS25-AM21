package it.polimi.it.galaxytrucker.view.GUI.controllers;

import it.polimi.it.galaxytrucker.model.managers.FlightBoard;
import it.polimi.it.galaxytrucker.view.GUI.GUIView;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.ImageCursor;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class GUIGameTurn extends GUIViewState{

    private Map<String, ImageView> imageViewMap = new HashMap<>();

    private FlightBoard flightBoard;
    private static GUIGameTurn instance;
    @FXML private PlayerShipElementController shipController;
    @FXML private ImageView activeCard;
    @FXML private ImageView p00,p01,p02,p03,p04,p05,p06,p07,p08,p09,p010,p011,p012,p013,p014,p015,p016,p017;
    @FXML private Label creditsCount,engineCount;
    @FXML private Pane level1board,level2board;

    public static GUIGameTurn getInstance() {
        synchronized (GUIGameTurn.class) {
            if (instance == null) {
                instance = new GUIGameTurn();
            }
            return instance;
        }
    }

    public GUIGameTurn() {
        try {
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(GUIGameTurn.class.getResource("/it/polimi/it/galaxytrucker/fxmlstages/gameTurn.fxml")));
            loader.setController(this);
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void displayScene() {

        Platform.runLater(() -> {
            stage = (Stage) GUIView.stage.getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            if (GUIView.getInstance().getClient().getModel().getGameLevel() == 1){
                System.out.println("Level 1");
                level1board.setVisible(true);
            }else {
                System.out.println("Level 2");
                level2board.setVisible(true);
            }

            imageViewMap.put("p00", p00);
            imageViewMap.put("p01", p01);
            imageViewMap.put("p02", p02);
            imageViewMap.put("p03", p03);
            imageViewMap.put("p04", p04);
            imageViewMap.put("p05", p05);
            imageViewMap.put("p06", p06);
            imageViewMap.put("p07", p07);
            imageViewMap.put("p08", p08);
            imageViewMap.put("p09", p09);
            imageViewMap.put("p010", p010);
            imageViewMap.put("p011", p011);
            imageViewMap.put("p012", p012);
            imageViewMap.put("p013", p013);
            imageViewMap.put("p014", p014);
            imageViewMap.put("p015", p015);
            imageViewMap.put("p016", p016);
            imageViewMap.put("p017", p017);


            activeCard.setImage(new Image(Objects.requireNonNull(GUIFixingShipController.class.getResourceAsStream("/it/polimi/it/galaxytrucker/graphics/cards/" + GUIView.getInstance().getClient().getModel().getActiveCardGraphicPath()))));
            creditsCount.setText(String.valueOf(GUIView.getInstance().getClient().getModel().getCredits()));
            stage.show();
            updateBoard();
        });
    }

    public void updateBoard(){
        Platform.runLater(() -> {
            System.out.println("Updating board");
            Map<UUID, Integer> positions;
            ImageView targetView;
            String cellNumeber = "p0";
            int cell;
            positions = GUIView.getInstance().getClient().getModel().getPlayerMarkerPositions();
            System.out.println("List map: "+positions);
            for(Map.Entry<UUID, Integer> entry : positions.entrySet()){
                System.out.println("printing image");
                cell=entry.getValue();
                cellNumeber = cellNumeber + cell;
                targetView = imageViewMap.get(cellNumeber);
                targetView.setImage(new Image(Objects.requireNonNull(GUIBuildingController.class.getResourceAsStream("/it/polimi/it/galaxytrucker/graphics/pedine/ingconti.png"))));
                System.out.println("Position: " + entry.getValue());
                cellNumeber = "p0";
            }
        });
    }


    @FXML
    private void viewFlightBoard() {

    }


}
