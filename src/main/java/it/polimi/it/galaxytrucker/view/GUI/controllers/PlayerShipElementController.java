package it.polimi.it.galaxytrucker.view.GUI.controllers;

import it.polimi.it.galaxytrucker.model.componenttiles.OutOfBoundsTile;
import it.polimi.it.galaxytrucker.model.componenttiles.TileData;
import it.polimi.it.galaxytrucker.view.GUI.GUIView;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

public class PlayerShipElementController {

    private boolean initialized = false;

    public int selectedRow = -1, selectedColumn = -1;
    private Predicate<TileData> highlightPredicate = tile -> tile == null || !tile.type().equals("OutOfBoundsTile");

    @FXML private StackPane playerShipRootPane;
    @FXML private ImageView shipBgImage;
    @FXML private GridPane grid;

    public void setHighlightPredicate(Predicate<TileData> highlightPredicate) {
        this.highlightPredicate = highlightPredicate;
    }

    private void initialize() {
        for (Node node : grid.getChildren()) {
            if (node instanceof StackPane stackPane) {
                int col = GridPane.getColumnIndex(stackPane);
                int row = GridPane.getRowIndex(stackPane);

                List<List<TileData>> ship = GUIView.getInstance().getClient().getModel().getPlayerShips(GUIView.getInstance().getClient().getModel().getMyData().getPlayerId());
                for (Node child : stackPane.getChildren()) {
                    if (child instanceof Button button) {
                        final int finalRow = row + 5;
                        final int finalCol = col + 4;

                        button.setOnAction(event -> {
                            selectTile(finalRow, finalCol);
                            System.out.println(finalRow + " " + finalCol);
                        });

                        if (highlightPredicate.test(ship.get(row).get(col))) {
                            button.setStyle("-fx-hover-border-color: rgba(98, 216, 57, 0.5);");
                        } else {
                            button.setStyle("-fx-hover-border-color: rgba(255, 0, 0, 0.5);");
                        }

                        button.setVisible(true);
                        button.setOpacity(1);
                    }
                }
            }
        }
    }

    @FXML
    private void selectTile(int row, int column) {
        selectedRow = row;
        selectedColumn = column;
    }

    public void resetSelectedTile() {
        selectedRow = -1;
        selectedColumn = -1;
    }

    public void displayShip() {
        if (!initialized) {
            initialize();
            initialized = true;
        }

        Platform.runLater(() -> {
            shipBgImage.setImage(new Image(Objects.requireNonNull(GUIFixingShipController.class.getResourceAsStream("/it/polimi/it/galaxytrucker/graphics/cardboard/shipboard-lvl" + GUIView.getInstance().getClient().getModel().getGameLevel() + ".jpg"))));

            List<List<TileData>> ship = GUIView.getInstance().getClient().getModel().getPlayerShips(GUIView.getInstance().getClient().getModel().getMyData().getPlayerId());
            for (Node node : grid.getChildren()) {
                if (node instanceof StackPane stackPane) {
                    int col = GridPane.getColumnIndex(stackPane);
                    int row = GridPane.getRowIndex(stackPane);

                    for (Node child : stackPane.getChildren()) {
                        if (child instanceof ImageView image) {
                            TileData tileData = ship.get(row).get(col);
                            if (tileData != null) {
                                if (!tileData.type().equals(OutOfBoundsTile.class.getSimpleName())) {
                                    image.setImage(new Image(Objects.requireNonNull(GUIFixingShipController.class.getResourceAsStream(tileData.graphicPath()))));
                                    image.setRotate(90 * tileData.rotation());

                                    double width = grid.getWidth() / grid.getColumnCount();
                                    double height = grid.getHeight() / grid.getRowCount();

                                    image.setFitWidth(width);
                                    image.setFitHeight(height);
                                }
                            } else {
                                image.setImage(null);
                            }
                        }
                    }
                }
            }
        });
    }


    public String tileSelectedTypeString(int row, int col) {
        List<List<TileData>> ship = GUIView.getInstance().getClient().getModel().getPlayerShips(GUIView.getInstance().getClient().getModel().getMyData().getPlayerId());
        TileData tileData = ship.get(row-5).get(col-4);
        

        return tileData.type();
    }
}
