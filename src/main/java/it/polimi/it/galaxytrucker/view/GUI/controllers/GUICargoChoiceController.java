package it.polimi.it.galaxytrucker.view.GUI.controllers;

import it.polimi.it.galaxytrucker.messages.clientmessages.UserInput;
import it.polimi.it.galaxytrucker.messages.clientmessages.UserInputType;
import it.polimi.it.galaxytrucker.model.componenttiles.CargoHold;
import it.polimi.it.galaxytrucker.model.componenttiles.SpecialCargoHold;
import it.polimi.it.galaxytrucker.model.utility.Cargo;
import it.polimi.it.galaxytrucker.model.utility.Color;
import it.polimi.it.galaxytrucker.model.utility.Coordinates;
import it.polimi.it.galaxytrucker.networking.client.clientmodel.ClientModel;
import it.polimi.it.galaxytrucker.view.GUI.GUIView;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.*;

public class GUICargoChoiceController extends GUIViewState implements GUIErrorHandler {

    private Map<String, ImageView> imageTiles = new HashMap<>();
    private List<Cargo> cargoReward = new ArrayList<>();
    private Map<Cargo, Integer> cargoToIndex = new HashMap<>();
    private HashMap<Integer,Coordinates> cargoCoords = new HashMap<>();
    private int i;

    @FXML
    private Label incorrectCoord, incorrectValues;

    @FXML
    private ImageView cargoImageView;

    @FXML private PlayerShipElementController shipController;

    private static GUICargoChoiceController instance;

    public static GUICargoChoiceController getInstance() {
        synchronized (GUICargoChoiceController.class) {
            if (instance == null) {
                instance = new GUICargoChoiceController();
            }
            return instance;
        }
    }

    public GUICargoChoiceController() {
        try {
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(GUICargoChoiceController.class.getResource("/it/polimi/it/galaxytrucker/fxmlstages/cargoChoice.fxml")));
            loader.setController(this);
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void initialize() {
        loadCardDetails();
        updateShip();
    }

    private void loadCardDetails() {
        ClientModel model = GUIView.getInstance().getClient().getModel();
        List<Cargo> rewardList = new ArrayList<>();

        if (model.getCurrentCard().equals("Planets")) {
            int selectedPlanet = model.getCardDetail("selectedPlanet", Integer.class);
            List<List<String>> serializedCargoRewards = model.getUnsafeCardDetail("rewards");

            for (String cargoValue : serializedCargoRewards.get(selectedPlanet)) {
                Cargo cargo = new Cargo(Color.valueOf(cargoValue));

                rewardList.add(cargo);
            }

        } else {
            List<String> serializedCargoReward = model.getUnsafeCardDetail("cargoReward");

            for (String cargoValue : serializedCargoReward) {
                Cargo cargo = new Cargo(Color.valueOf(cargoValue));

                rewardList.add(cargo);
            }
        }
        setCargoReward(rewardList);
        displayCargo();
    }

    @FXML
    private void cargoBack(){
        if (i >= 0 && i < cargoCoords.size()) {
            i--;
            displayCargo();
        } else cargoImageView.setImage(null);
    }

    @FXML
    private void cargoForward() {
        if (i >= 0 && i < cargoCoords.size()) {
            i++;
            displayCargo();
        } else cargoImageView.setImage(null);
    }

    public void displayCargo() {
        if(!cargoReward.isEmpty()) {
            Color color = cargoReward.get(i).getColor();
            switch (color) {
                case RED:
                    cargoImageView.setImage(new Image(Objects.requireNonNull(GUICargoChoiceController.class.getResourceAsStream("/it/polimi/it/galaxytrucker/graphics/general/cargoRed.png"))));
                    break;
                case YELLOW:
                    cargoImageView.setImage(new Image(Objects.requireNonNull(GUICargoChoiceController.class.getResourceAsStream("/it/polimi/it/galaxytrucker/graphics/general/cargoYellow.png"))));
                    break;
                case GREEN:
                    cargoImageView.setImage(new Image(Objects.requireNonNull(GUICargoChoiceController.class.getResourceAsStream("/it/polimi/it/galaxytrucker/graphics/general/cargoGreen.png"))));
                    break;
                case BLUE:
                    cargoImageView.setImage(new Image(Objects.requireNonNull(GUICargoChoiceController.class.getResourceAsStream("/it/polimi/it/galaxytrucker/graphics/general/cargoBlue.png"))));
                    break;
                default:
                    break;
            }
        } else cargoImageView.setImage(null);
    }

    @FXML
    private void addCargo() {
        if(!cargoReward.isEmpty()) {
            int col = shipController.selectedColumn;
            int row = shipController.selectedRow;
            if ((row < 5) || (row > 9) || (col < 4) || (col > 10)) {
                incorrectCoord.setVisible(true);
            } else {
                if (GUIView.getInstance().getClient().getModel().getPlayerShips(GUIView.getInstance().getClient().getModel().getMyData().getPlayerId()).get(row - 5).get(col - 4) != null) {
                    if (cargoReward.get(i).isSpecial()) {
                        if (GUIView.getInstance().getClient().getModel().getPlayerShips(GUIView.getInstance().getClient().getModel().getMyData().getPlayerId()).get(row-5).get(col-4).type().equals(SpecialCargoHold.class.getSimpleName())) {
                            cargoCoords.put(cargoToIndex.get(cargoReward.get(i)), new Coordinates(col, row));
                            cargoReward.remove(cargoReward.get(i));
                            i = 0;
                            displayCargo();
                            incorrectCoord.setVisible(false);
                        } else incorrectCoord.setVisible(true);
                    } else {
                        if (GUIView.getInstance().getClient().getModel().getPlayerShips(GUIView.getInstance().getClient().getModel().getMyData().getPlayerId()).get(row-5).get(col-4).type().equals(CargoHold.class.getSimpleName())) {
                            cargoCoords.put(cargoToIndex.get(cargoReward.get(i)), new Coordinates(col, row));
                            cargoReward.remove(cargoReward.get(i));
                            i = 0;
                            displayCargo();
                            incorrectCoord.setVisible(false);
                        } else incorrectCoord.setVisible(true);
                    }
                }
            }
        }
    }

    public void updateShip(){
        shipController.displayShip();
        displayCargo();
    }


    @FXML
    private void acceptReward() {
        if(!cargoReward.isEmpty()) {
            for(Cargo cargo: cargoReward) {
                cargoCoords.put(cargoToIndex.get(cargo),new Coordinates(0,0));
            }
        }

        GUIView.getInstance().getClient().receiveUserInput(
                new UserInput.UserInputBuilder(UserInputType.CARGO_REWARD)
                        .setAcceptedCargo(cargoCoords)
                        .build()
        );
    }

    public void setCargoReward(List<Cargo> cargoReward) {
        this.cargoReward = cargoReward;
        for (int i = 0; i < cargoReward.size(); i++) {
            cargoToIndex.put(cargoReward.get(i), i);
        }
    }

    @Override
    public void displayScene() {
        Platform.runLater(() -> {
            stage = (Stage) GUIView.stage.getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

            i=0;
            loadCardDetails();
            updateShip();
        });
    }

    @Override
    public void inputError() {
        incorrectValues.setVisible(true);
        incorrectCoord.setVisible(false);
        i=0;
        cargoReward =  cargoToIndex.keySet().stream().toList();
    }
}


