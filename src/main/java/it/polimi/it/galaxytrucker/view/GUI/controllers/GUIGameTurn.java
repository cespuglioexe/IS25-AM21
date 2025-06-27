package it.polimi.it.galaxytrucker.view.GUI.controllers;

import it.polimi.it.galaxytrucker.model.managers.FlightBoard;
import it.polimi.it.galaxytrucker.model.utility.Color;
import it.polimi.it.galaxytrucker.view.GUI.GUIView;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.ImageCursor;
import javafx.scene.Parent;
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
    @FXML private ImageView activeCard;
    @FXML private ImageView p00,p01,p02,p03,p04,p05,p06,p07,p08,p09,p010,p011,p012,p013,p014,p015,p016,p017;
    @FXML private ImageView p100,p101,p102,p103,p104,p105,p106,p107,p108,p109,p1010,p1011,p1012,p1013,p1014,p1015,p1016,p1017,p1018,p1019,p1020,p1021,p1022,p1023;
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
            try {
                FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(
                        GUIGameTurn.class.getResource("/it/polimi/it/galaxytrucker/fxmlstages/gameTurn.fxml")
                ));
                loader.setController(this);
                Parent newRoot = loader.load();

                stage = (Stage) GUIView.stage.getScene().getWindow();
                scene = new Scene(newRoot);
                stage.setScene(scene);
                if (GUIView.getInstance().getClient().getModel().getGameLevel() == 1){
                    System.out.println("Level 1");
                    level1board.setVisible(true);
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

                }else {
                    System.out.println("Level 2");
                    level2board.setVisible(true);
                    imageViewMap.put("p100", p100);
                    imageViewMap.put("p101", p101);
                    imageViewMap.put("p102", p102);
                    imageViewMap.put("p103", p103);
                    imageViewMap.put("p104", p104);
                    imageViewMap.put("p105", p105);
                    imageViewMap.put("p106", p106);
                    imageViewMap.put("p107", p107);
                    imageViewMap.put("p108", p108);
                    imageViewMap.put("p109", p109);
                    imageViewMap.put("p1010", p1010);
                    imageViewMap.put("p1011", p1011);
                    imageViewMap.put("p1012", p1012);
                    imageViewMap.put("p1013", p1013);
                    imageViewMap.put("p1014", p1014);
                    imageViewMap.put("p1015", p1015);
                    imageViewMap.put("p1016", p1016);
                    imageViewMap.put("p1017", p1017);
                    imageViewMap.put("p1018", p1018);
                    imageViewMap.put("p1019", p1019);
                    imageViewMap.put("p1020", p1020);
                    imageViewMap.put("p1021", p1021);
                    imageViewMap.put("p1022", p1022);
                    imageViewMap.put("p1023", p1023);

                }

                updateBoard();



                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void updateBoard(){
        Platform.runLater(() -> {
            System.out.println("Updating board");
            HashMap<UUID, Integer> positions;
            ImageView targetView;
            int level = GUIView.getInstance().getClient().getModel().getGameLevel();
            String cellNumeber = "p0";
            if(level== 1){
                cellNumeber = "p0";
            }else{
                cellNumeber = "p10";
            }
            int cell;
            positions = GUIView.getInstance().getClient().getModel().getPlayerMarkerPositions();
            System.out.println("List map: "+positions);

            for(HashMap.Entry<UUID, Integer> entry : positions.entrySet()){
                System.out.println("printing image");
                cell=entry.getValue();
                cellNumeber = cellNumeber + cell;
                System.out.println(cellNumeber);
                targetView = imageViewMap.get(cellNumeber);


                if(GUIView.getInstance().getClient().getModel().getMyData().getNickname().equals(GUIView.getInstance().getSuperSecretUsername())){
                    switch (GUIView.getInstance().getClient().getModel().getPlayerColors().get(entry.getKey())){
                        case RED:
                            targetView.setImage(new Image(Objects.requireNonNull(GUIBuildingController.class.getResourceAsStream("/it/polimi/it/galaxytrucker/graphics/pedine/redPawnc.png"))));
                            break;
                        case BLUE:
                            System.out.println("Color blue");
                            targetView.setImage(new Image(Objects.requireNonNull(GUIBuildingController.class.getResourceAsStream("/it/polimi/it/galaxytrucker/graphics/pedine/bluePawnc.png"))));
                            break;
                        case YELLOW:
                            System.out.println("Color yellow");
                            targetView.setImage(new Image(Objects.requireNonNull(GUIBuildingController.class.getResourceAsStream("/it/polimi/it/galaxytrucker/graphics/pedine/yellowPawnc.png"))));
                            break;
                        case GREEN:
                            System.out.println("Color green");
                            targetView.setImage(new Image(Objects.requireNonNull(GUIBuildingController.class.getResourceAsStream("/it/polimi/it/galaxytrucker/graphics/pedine/greenPawnc.png"))));
                            break;
                    }
                }else{
                    switch (GUIView.getInstance().getClient().getModel().getPlayerColors().get(entry.getKey())){
                        case RED:
                            targetView.setImage(new Image(Objects.requireNonNull(GUIBuildingController.class.getResourceAsStream("/it/polimi/it/galaxytrucker/graphics/pedine/redPawn.png"))));
                            break;
                        case BLUE:
                            System.out.println("Color blue");
                            targetView.setImage(new Image(Objects.requireNonNull(GUIBuildingController.class.getResourceAsStream("/it/polimi/it/galaxytrucker/graphics/pedine/bluePawn.png"))));
                            break;
                        case YELLOW:
                            System.out.println("Color yellow");
                            targetView.setImage(new Image(Objects.requireNonNull(GUIBuildingController.class.getResourceAsStream("/it/polimi/it/galaxytrucker/graphics/pedine/yellowPawn.png"))));
                            break;
                        case GREEN:
                            System.out.println("Color green");
                            targetView.setImage(new Image(Objects.requireNonNull(GUIBuildingController.class.getResourceAsStream("/it/polimi/it/galaxytrucker/graphics/pedine/greenPawn.png"))));
                            break;
                    }
                }
                if (level==1){
                    cellNumeber = "p0";
                }else{
                    cellNumeber = "p10";
                }

            }
        });
    }


    @FXML
    private void viewFlightBoard() {

    }

    @FXML
    public void displayCard() {
        Platform.runLater(() -> {
            System.out.println("card: "+GUIView.getInstance().getClient().getModel().getActiveCardGraphicPath());
            activeCard.setImage(new Image(Objects.requireNonNull(GUIGameTurn.class.getResourceAsStream("/it/polimi/it/galaxytrucker/graphics/cards/" + GUIView.getInstance().getClient().getModel().getActiveCardGraphicPath()))));
        });
        try {
            Thread.sleep(3000); // 2000 millisecondi = 2 secondi
        } catch (InterruptedException e) {
            e.printStackTrace(); // Gestisci l'eccezione, se necessario
        }
    }

    public void showRankings(){
        GUIView.getInstance().showScoreBoard();
    }
}
