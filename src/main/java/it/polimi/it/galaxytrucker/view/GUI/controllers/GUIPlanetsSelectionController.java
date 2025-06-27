package it.polimi.it.galaxytrucker.view.GUI.controllers;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import it.polimi.it.galaxytrucker.messages.clientmessages.UserInput;
import it.polimi.it.galaxytrucker.messages.clientmessages.UserInputType;
import it.polimi.it.galaxytrucker.networking.client.clientmodel.ClientModel;
import it.polimi.it.galaxytrucker.view.GUI.GUIView;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class GUIPlanetsSelectionController extends GUIViewState {
    @FXML
    private ImageView planetView;

    @FXML
    private Button leftButton, rightButton;

    @FXML
    private Button landButton;

    private final String[] planets = {
        "/it/polimi/it/galaxytrucker/graphics/general/planet-red.png",
        "/it/polimi/it/galaxytrucker/graphics/general/planet-green.png",
        "/it/polimi/it/galaxytrucker/graphics/general/planet-blue.png",
        "/it/polimi/it/galaxytrucker/graphics/general/planet-purple.png"
    };

    private Set<Integer> occupiedPlanets = new HashSet<>();

    private int currentPlanetIndex = 0;
    private int numOfPlanets = 1;
    private int flightDayPenalty = 0;

    private HashMap<Integer, HashMap<String, Integer>> planetsAndCargo;

    private static GUIPlanetsSelectionController instance;

    public static GUIPlanetsSelectionController getInstance() {
        synchronized (GUIPlanetsSelectionController.class) {
            if (instance == null) {
                instance = new GUIPlanetsSelectionController();
            }
            return instance;
        }
    }

    public GUIPlanetsSelectionController() {
        try {
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(GUIPlanetsSelectionController.class.getResource("/it/polimi/it/galaxytrucker/fxmlstages/participationPlanetChoice.fxml")));
            loader.setController(this);
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void initialize() {
        loadCardDetails();

        leftButton.setOnAction(e -> {
            currentPlanetIndex = (currentPlanetIndex - 1 + numOfPlanets) % numOfPlanets;
            updatePlanetGUI();
            updateCargoGUI();
        });
        rightButton.setOnAction(e -> {
            currentPlanetIndex = (currentPlanetIndex + 1) % numOfPlanets;
            updatePlanetGUI();
            updateCargoGUI();
        });
    }

    private void loadCardDetails() {
        ClientModel model = GUIView.getInstance().getClient().getModel();

        numOfPlanets = model.getCardDetail("planets", Integer.class);
        List<List<String>> cargoRewards = model.getUnsafeCardDetail("rewards");

        planetsAndCargo = new HashMap<>();
        for (int i = 0; i < numOfPlanets; i++) {
            HashMap<String, Integer> cargoNumber = new HashMap<>();

            for (String cargo : cargoRewards.get(i)) {
                int count = cargoNumber.getOrDefault(cargo, 0) + 1;
                cargoNumber.put(cargo, count);
            }
            planetsAndCargo.put(i, cargoNumber);
        }

        flightDayPenalty = model.getCardDetail("flightDayPenalty", Integer.class);

        updatePlanetGUI();
        updateCargoGUI();
        setPenaltyValue(flightDayPenalty);
    }

    private void updatePlanetGUI() {
        planetView.setImage(new Image(planets[currentPlanetIndex]));
        DropShadow glow = new DropShadow();
        glow.setColor(Color.MEDIUMPURPLE);
        glow.setRadius(40);
        glow.setSpread(0.3);
        planetView.setEffect(glow);
        planetView.getStyleClass().remove("occupied-planet");
        landButton.setVisible(true);

        if (occupiedPlanets.contains(currentPlanetIndex)) {
            ColorAdjust grayscale = new ColorAdjust();
            grayscale.setSaturation(-1);
            planetView.setEffect(grayscale);
            planetView.getStyleClass().add("occupied-planet");
            landButton.setVisible(false);
        }
    }

    @FXML private Label redLabel, yellowLabel, blueLabel, greenLabel;

    private void updateCargoGUI() {
        HashMap<String, Integer> cargoNumber = planetsAndCargo.get(currentPlanetIndex);

        yellowLabel.setText(String.valueOf(cargoNumber.getOrDefault("YELLOW", 0)));
        redLabel.setText(String.valueOf(cargoNumber.getOrDefault("RED", 0)));
        greenLabel.setText(String.valueOf(cargoNumber.getOrDefault("GREEN", 0)));
        blueLabel.setText(String.valueOf(cargoNumber.getOrDefault("BLUE", 0)));
    }

    @FXML private Label penaltyValueLabel;

    private void setPenaltyValue(int value) {
        penaltyValueLabel.setText(String.valueOf(value));
    }

    public void updateOccupiedPlanets() {
        ClientModel model = GUIView.getInstance().getClient().getModel();

        Map<Integer, UUID> occupiedPlanetsAndPlayers = model.getUnsafeCardDetail("occupiedPlanets");
        occupiedPlanets = occupiedPlanetsAndPlayers.keySet();

        updatePlanetGUI();
    }

    @Override
    public void displayScene() {
        Platform.runLater(() -> {
            try {
                FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(
                        GUIPlanetsSelectionController.class.getResource("/it/polimi/it/galaxytrucker/fxmlstages/participationPlanetChoice.fxml")
                ));
                loader.setController(this);
                Parent newRoot = loader.load();

                stage = (Stage) GUIView.stage.getScene().getWindow();
                scene = new Scene(newRoot);
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @FXML
    private void landOnPlanet() {
        ClientModel model = GUIView.getInstance().getClient().getModel();

        model.putCardDetail("selectedPlanet", currentPlanetIndex);
        GUIView.getInstance().getClient().receiveUserInput(
                new UserInput.UserInputBuilder(UserInputType.PARTICIPATION)
                        .setParticipation(true)
                        .setParticipationChoice(currentPlanetIndex)
                        .build()
        );
    }

    @FXML
    private void skipTurn() {
        GUIView.getInstance().getClient().receiveUserInput(
                new UserInput.UserInputBuilder(UserInputType.PARTICIPATION)
                        .setParticipation(false)
                        .setParticipationChoice(-1)
                        .build()
        );
    }
}
