package it.polimi.it.galaxytrucker.view.GUI.controllers;

import java.util.HashMap;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

public class GUIPlanetsSelectionController {
    @FXML
    private ImageView planetView;

    @FXML
    private Button leftButton, rightButton;

    private final String[] planets = {
        "/it/polimi/it/galaxytrucker/graphics/general/planet-red.png",
        "/it/polimi/it/galaxytrucker/graphics/general/planet-green.png",
        "/it/polimi/it/galaxytrucker/graphics/general/planet-blue.png",
        "/it/polimi/it/galaxytrucker/graphics/general/planet-purple.png"
    };

    private int currentPlanetIndex = 0;
    private int numOfPlanets = 1;

    private HashMap<Integer, HashMap<it.polimi.it.galaxytrucker.model.utility.Color, Integer>> planetsAndCargo = new HashMap<>();

    @FXML
    public void initialize() {
        setPlanetsDefaultValues();

        updatePlanetImage();
        updateCargoCounts(planetsAndCargo.get(currentPlanetIndex));

        numOfPlanets = planetsAndCargo.keySet().size();
        leftButton.setOnAction(e -> {
            currentPlanetIndex = (currentPlanetIndex - 1 + numOfPlanets) % numOfPlanets;
            updatePlanetImage();

            HashMap<it.polimi.it.galaxytrucker.model.utility.Color, Integer> cargo = planetsAndCargo.get(currentPlanetIndex);
            updateCargoCounts(cargo);
        });
        rightButton.setOnAction(e -> {
            currentPlanetIndex = (currentPlanetIndex + 1) % numOfPlanets;
            updatePlanetImage();

            HashMap<it.polimi.it.galaxytrucker.model.utility.Color, Integer> cargo = planetsAndCargo.get(currentPlanetIndex);
            updateCargoCounts(cargo);
        });
    }

    private void setPlanetsDefaultValues() {
        HashMap<it.polimi.it.galaxytrucker.model.utility.Color, Integer> cargo0 = new HashMap<>();
        cargo0.put(it.polimi.it.galaxytrucker.model.utility.Color.RED, 1);
        cargo0.put(it.polimi.it.galaxytrucker.model.utility.Color.YELLOW, 1);
        cargo0.put(it.polimi.it.galaxytrucker.model.utility.Color.GREEN, 0);
        cargo0.put(it.polimi.it.galaxytrucker.model.utility.Color.BLUE, 0);
        planetsAndCargo.put(0, cargo0);

        HashMap<it.polimi.it.galaxytrucker.model.utility.Color, Integer> cargo1 = new HashMap<>();
        cargo1.put(it.polimi.it.galaxytrucker.model.utility.Color.RED, 0);
        cargo1.put(it.polimi.it.galaxytrucker.model.utility.Color.YELLOW, 1);
        cargo1.put(it.polimi.it.galaxytrucker.model.utility.Color.GREEN, 1);
        cargo1.put(it.polimi.it.galaxytrucker.model.utility.Color.BLUE, 1);
        planetsAndCargo.put(1, cargo1);

        HashMap<it.polimi.it.galaxytrucker.model.utility.Color, Integer> cargo2 = new HashMap<>();
        cargo2.put(it.polimi.it.galaxytrucker.model.utility.Color.RED, 0);
        cargo2.put(it.polimi.it.galaxytrucker.model.utility.Color.YELLOW, 0);
        cargo2.put(it.polimi.it.galaxytrucker.model.utility.Color.GREEN, 2);
        cargo2.put(it.polimi.it.galaxytrucker.model.utility.Color.BLUE, 0);
        planetsAndCargo.put(2, cargo2);

        HashMap<it.polimi.it.galaxytrucker.model.utility.Color, Integer> cargo3 = new HashMap<>();
        cargo3.put(it.polimi.it.galaxytrucker.model.utility.Color.RED, 0);
        cargo3.put(it.polimi.it.galaxytrucker.model.utility.Color.YELLOW, 1);
        cargo3.put(it.polimi.it.galaxytrucker.model.utility.Color.GREEN, 0);
        cargo3.put(it.polimi.it.galaxytrucker.model.utility.Color.BLUE, 0);
        planetsAndCargo.put(3, cargo3);
    }

    private void updatePlanetImage() {
        planetView.setImage(new Image(planets[currentPlanetIndex]));
        DropShadow glow = new DropShadow();
        glow.setColor(Color.MEDIUMPURPLE);
        glow.setRadius(40);
        glow.setSpread(0.3);
        planetView.setEffect(glow);
    }

    @FXML private Label redLabel, yellowLabel, blueLabel, greenLabel;

    public void loadPlanets(HashMap<Integer, HashMap<it.polimi.it.galaxytrucker.model.utility.Color, Integer>> planetsAndCargo) {
        this.planetsAndCargo = planetsAndCargo;

        currentPlanetIndex = 0;
        updatePlanetImage();

        numOfPlanets = planetsAndCargo.keySet().size();

        HashMap<it.polimi.it.galaxytrucker.model.utility.Color, Integer> cargo = planetsAndCargo.get(currentPlanetIndex);
        updateCargoCounts(cargo);
    }

    private void updateCargoCounts(HashMap<it.polimi.it.galaxytrucker.model.utility.Color, Integer> cargo) {
        yellowLabel.setText(String.valueOf(cargo.get(it.polimi.it.galaxytrucker.model.utility.Color.YELLOW)));
        redLabel.setText(String.valueOf(cargo.get(it.polimi.it.galaxytrucker.model.utility.Color.RED)));
        greenLabel.setText(String.valueOf(cargo.get(it.polimi.it.galaxytrucker.model.utility.Color.GREEN)));
        blueLabel.setText(String.valueOf(cargo.get(it.polimi.it.galaxytrucker.model.utility.Color.BLUE)));
    }

    @FXML private Label penaltyValueLabel;

    public void setPenaltyValue(int value) {
        penaltyValueLabel.setText(String.valueOf(value));
    }
}
