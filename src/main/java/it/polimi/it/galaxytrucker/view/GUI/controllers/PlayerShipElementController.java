package it.polimi.it.galaxytrucker.view.GUI.controllers;

import it.polimi.it.galaxytrucker.model.componenttiles.BatteryComponent;
import it.polimi.it.galaxytrucker.model.componenttiles.CabinModule;
import it.polimi.it.galaxytrucker.model.componenttiles.CargoHold;
import it.polimi.it.galaxytrucker.model.componenttiles.CentralCabin;
import it.polimi.it.galaxytrucker.model.componenttiles.OutOfBoundsTile;
import it.polimi.it.galaxytrucker.model.componenttiles.SpecialCargoHold;
import it.polimi.it.galaxytrucker.model.componenttiles.TileData;
import it.polimi.it.galaxytrucker.model.utility.AlienType;
import it.polimi.it.galaxytrucker.model.utility.Color;
import it.polimi.it.galaxytrucker.view.GUI.GUIView;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

import java.net.URL;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Ship element controller with hover tooltip displaying component image + specs.
 *
 * <p>BUG FIX: il tooltip persisteva quando ci si spostava da un componente valido
 * a una cella vuota/out‑of‑bounds. Ora disinstalliamo/ reinstalliamo il tooltip
 * dinamicamente e non usiamo più l'installazione statica all'avvio.</p>
 */
public class PlayerShipElementController implements Initializable {

    private boolean initialized = false;

    public int selectedRow = -1, selectedColumn = -1;
    private Predicate<TileData> highlightPredicate = tile -> tile == null || !tile.type().equals("OutOfBoundsTile");

    @FXML private StackPane playerShipRootPane;
    @FXML public ImageView shipBgImage;
    @FXML private GridPane grid;

    /** Tooltip ri‑usabile */
    private final Tooltip hoverTip = new Tooltip();

    // ---------------------------------------------------------------------
    //  JavaFX lifecycle
    // ---------------------------------------------------------------------
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        hoverTip.setShowDelay(Duration.millis(80));
        hoverTip.setHideDelay(Duration.millis(100));
        hoverTip.setShowDuration(Duration.INDEFINITE);
        hoverTip.getStyleClass().add("tooltip");

        // Nascondi se esci dalla griglia o ti muovi fuori dai bottoni
        grid.setOnMouseExited(e -> hoverTip.hide());
        playerShipRootPane.addEventFilter(MouseEvent.MOUSE_MOVED, e -> {
            if (!(e.getTarget() instanceof Button)) hoverTip.hide();
        });
    }

    // ---------------------------------------------------------------------
    //  Public API
    // ---------------------------------------------------------------------
    public void setHighlightPredicate(Predicate<TileData> highlightPredicate) {
        this.highlightPredicate = highlightPredicate;
    }

    public void resetSelectedTile() {
        selectedRow = -1;
        selectedColumn = -1;
    }

    /**
     * Refreshes board graphics. First call wires up the grid & tooltip logic.
     */
    public void displayShip() {
        initializeGridOnce();

        Platform.runLater(() -> {
            shipBgImage.setImage(new Image(Objects.requireNonNull(
                    GUIFixingShipController.class.getResourceAsStream(
                            "/it/polimi/it/galaxytrucker/graphics/cardboard/shipboard-lvl"
                                    + GUIView.getInstance().getClient().getModel().getGameLevel() + ".jpg"))));

            List<List<TileData>> ship = GUIView.getInstance().getClient().getModel()
                    .getPlayerShips(GUIView.getInstance().getClient().getModel().getMyData().getPlayerId());

            for (Node node : grid.getChildren()) {
                if (node instanceof StackPane stackPane) {
                    int col = GridPane.getColumnIndex(stackPane);
                    int row = GridPane.getRowIndex(stackPane);

                    for (Node child : stackPane.getChildren()) {
                        if (child instanceof ImageView image) {
                            TileData tileData = ship.get(row).get(col);
                            if (tileData != null && !tileData.type().equals(OutOfBoundsTile.class.getSimpleName())) {
                                image.setImage(new Image(Objects.requireNonNull(
                                        GUIFixingShipController.class.getResourceAsStream(tileData.graphicPath()))));
                                image.setRotate(90 * tileData.rotation());

                                double width = grid.getWidth() / grid.getColumnCount();
                                double height = grid.getHeight() / grid.getRowCount();

                                image.setFitWidth(width);
                                image.setFitHeight(height);
                                image.setMouseTransparent(true);
                            } else {
                                image.setImage(null);
                            }
                        }
                    }
                }
            }
        });
    }

    // ---------------------------------------------------------------------
    //  Internal helpers
    // ---------------------------------------------------------------------
    private void initializeGridOnce() {
        if (initialized) return;

        List<List<TileData>> ship = GUIView.getInstance().getClient().getModel()
                .getPlayerShips(GUIView.getInstance().getClient().getModel().getMyData().getPlayerId());

        for (Node node : grid.getChildren()) {
            if (node instanceof StackPane stackPane) {
                int col = GridPane.getColumnIndex(stackPane);
                int row = GridPane.getRowIndex(stackPane);

                for (Node child : stackPane.getChildren()) {
                    if (child instanceof Button button) {
                        final int modelRow = row + 5;
                        final int modelCol = col + 4;

                        // Click -> select tile
                        button.setOnAction(evt -> selectTile(modelRow, modelCol));

                        // Green/red hover border highlighting
                        if (highlightPredicate.test(ship.get(row).get(col))) {
                            button.setStyle("-fx-hover-border-color: rgba(98, 216, 57, 0.5);");
                        } else {
                            button.setStyle("-fx-hover-border-color: rgba(255, 0, 0, 0.5);");
                        }

                        // Hover behaviour – tooltip attach/detach dinamico
                        button.setOnMouseEntered(ev -> handleMouseEntered(button, row, col));
                        button.setOnMouseExited(ev -> hoverTip.hide());

                        button.setVisible(true);
                        button.setOpacity(1);
                    }
                }
            }
        }
        initialized = true;
    }

    private void handleMouseEntered(Button btn, int gridRow, int gridCol) {
        TileData td = GUIView.getInstance().getClient().getModel()
                .getPlayerShips(GUIView.getInstance().getClient().getModel().getMyData().getPlayerId())
                .get(gridRow).get(gridCol);

        // Se la casella è vuota / out‑of‑bounds: disinstalla tooltip e chiudi
        if (td == null || td.type().equals(OutOfBoundsTile.class.getSimpleName())) {
            Tooltip.uninstall(btn, hoverTip);
            hoverTip.hide();
            return;
        }

        // Casella valida: aggiorna contenuto e installa tooltip
        updateTooltipContent(td);
        Tooltip.install(btn, hoverTip); // attach solo a questa cella valida
    }

    private void updateTooltipContent(TileData tileData) {
        // Graphic: tile image
        ImageView img = new ImageView(new Image(Objects.requireNonNull(
                GUIFixingShipController.class.getResourceAsStream(tileData.graphicPath()))));
        img.setFitHeight(80);
        img.setPreserveRatio(true);

        // Text specs
        Label specs = new Label(buildSpecsText(tileData));
        specs.setWrapText(true);
        specs.setMaxWidth(220);

        HBox box = new HBox(12, img, specs);
        box.setPadding(new Insets(10));

        hoverTip.setText("");
        hoverTip.setGraphic(box);
        hoverTip.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
    }

    /**
     * Simple textual description – extend if TileData exposes richer info.
     */
    private String buildSpecsText(TileData data) {
        StringBuilder sb = new StringBuilder();
        sb.append("Type: ").append(data.type());

        if (data.type().equals(BatteryComponent.class.getSimpleName())) {
            sb.append("\nBatteries: ").append(data.batteryCharge());

        } else if (data.type().equals(CargoHold.class.getSimpleName()) ||
                data.type().equals(SpecialCargoHold.class.getSimpleName())) {

            sb.append("\nCapacity: ").append(data.capacity());

            Map<Color, Long> cargoCount = data.cargo().stream()
                    .collect(Collectors.groupingBy(c -> c, Collectors.counting()));

            Map<String, String> emojiMap = Map.of(
                    "YELLOW", "🟨",
                    "GREEN", "🟩",
                    "BLUE", "🟦",
                    "RED", "🟥"
            );

            if (!cargoCount.isEmpty()) {
                sb.append("\nCargo:");
                cargoCount.forEach((color, count) ->
                        sb.append(emojiMap.getOrDefault(color.toString(), "?").repeat(Math.toIntExact(count))));
            }

        } else if (data.type().equals(CabinModule.class.getSimpleName()) ||
                data.type().equals(CentralCabin.class.getSimpleName())) {

            if (!data.crewmates().isEmpty()) {
                sb.append("\nCrewmates:");
                Map<String, Long> crewCount = data.crewmates().stream()
                        .collect(Collectors.groupingBy(c -> c, Collectors.counting()));

                crewCount.forEach((name, count) ->
                        sb.append("\n").append(name).append(": ").append(count));
            }
        }
        return sb.toString();
    }

    // ---------------------------------------------------------------------
    //  Tile selection
    // ---------------------------------------------------------------------
    private void selectTile(int row, int column) {
        selectedRow = row;
        selectedColumn = column;
    }


    public String tileSelectedTypeString(int row, int col) {
        List<List<TileData>> ship = GUIView.getInstance().getClient().getModel().getPlayerShips(GUIView.getInstance().getClient().getModel().getMyData().getPlayerId());
        TileData tileData = ship.get(row-5).get(col-4);

        if (tileData != null) {
            if(tileData.type().equals("LifeSupport")) {
                return tileData.type() + tileData.alienSupport();
            }else {
                return tileData.type();
            }
        }else {
            return "";
        }

    }


    public void displayOtherShip(UUID id) {
        initializeOtherGridOnce(id);

        Platform.runLater(() -> {
            shipBgImage.setImage(new Image(Objects.requireNonNull(
                    GUIFixingShipController.class.getResourceAsStream(
                            "/it/polimi/it/galaxytrucker/graphics/cardboard/shipboard-lvl"
                                    + GUIView.getInstance().getClient().getModel().getGameLevel() + ".jpg"))));

            List<List<TileData>> ship = GUIView.getInstance().getClient().getModel()
                    .getPlayerShips(id);

            for (Node node : grid.getChildren()) {
                if (node instanceof StackPane stackPane) {
                    int col = GridPane.getColumnIndex(stackPane);
                    int row = GridPane.getRowIndex(stackPane);

                    for (Node child : stackPane.getChildren()) {
                        if (child instanceof ImageView image) {
                            TileData tileData = ship.get(row).get(col);
                            if (tileData != null && !tileData.type().equals(OutOfBoundsTile.class.getSimpleName())) {
                                image.setImage(new Image(Objects.requireNonNull(
                                        GUIFixingShipController.class.getResourceAsStream(tileData.graphicPath()))));
                                image.setRotate(90 * tileData.rotation());

                                double width = grid.getWidth() / grid.getColumnCount();
                                double height = grid.getHeight() / grid.getRowCount();

                                image.setFitWidth(width);
                                image.setFitHeight(height);
                                image.setMouseTransparent(true);
                            } else {
                                image.setImage(null);
                            }
                        }
                    }
                }
            }
        });
    }

    private void initializeOtherGridOnce(UUID id) {
        if (initialized) return;

        List<List<TileData>> ship = GUIView.getInstance().getClient().getModel()
                .getPlayerShips(id);

        for (Node node : grid.getChildren()) {
            if (node instanceof StackPane stackPane) {
                int col = GridPane.getColumnIndex(stackPane);
                int row = GridPane.getRowIndex(stackPane);

                for (Node child : stackPane.getChildren()) {
                    if (child instanceof Button button) {
                        final int modelRow = row + 5;
                        final int modelCol = col + 4;

                        // Click -> select tile
                        button.setOnAction(evt -> selectTile(modelRow, modelCol));

                        // Green/red hover border highlighting
                        if (highlightPredicate.test(ship.get(row).get(col))) {
                            button.setStyle("-fx-hover-border-color: rgba(98, 216, 57, 0.5);");
                        } else {
                            button.setStyle("-fx-hover-border-color: rgba(255, 0, 0, 0.5);");
                        }

                        // Hover behaviour – tooltip attach/detach dinamico
                        button.setOnMouseEntered(ev -> handleMouseEntered(button, row, col));
                        button.setOnMouseExited(ev -> hoverTip.hide());

                        button.setVisible(true);
                        button.setOpacity(1);
                    }
                }
            }
        }
        initialized = true;
    }
}
