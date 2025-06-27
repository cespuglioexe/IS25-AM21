package it.polimi.it.galaxytrucker.view.GUI.controllers;

import it.polimi.it.galaxytrucker.messages.clientmessages.UserInput;
import it.polimi.it.galaxytrucker.messages.clientmessages.UserInputType;
import it.polimi.it.galaxytrucker.model.adventurecards.interfaces.attack.Projectile;
import it.polimi.it.galaxytrucker.model.componenttiles.*;
import it.polimi.it.galaxytrucker.model.utility.Coordinates;
import it.polimi.it.galaxytrucker.model.utility.Direction;
import it.polimi.it.galaxytrucker.model.utility.ProjectileType;
import it.polimi.it.galaxytrucker.networking.client.clientmodel.ClientModel;
import it.polimi.it.galaxytrucker.view.GUI.GUIView;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.*;

public class GUIActivateShieldController extends GUIViewState{

    private Map<String, ImageView> imageTiles = new HashMap<>();
    private List<Coordinates> shieldCoords = new ArrayList<>();
    private List<Coordinates> batteryCoord = new ArrayList<>();
    private List<List<Coordinates>> shieldAndBatteryCoord = new ArrayList<>();
    int cont;
    List<Projectile> projectiles = new ArrayList<>();
    List<Integer> projectilesRolledCoord = new ArrayList<>();

    @FXML
    private Label incorrectCoord1,incorrectCoord2,incorrectValue, projectileValue;

    @FXML private PlayerShipElementController shipController;
    @FXML GridPane trasparent;


    @FXML
    Pane shieldPane, projectilePane;

    @FXML
    Button projectilesButton;

    @FXML ImageView projectileImage;

    private static GUIActivateShieldController instance;

    public static GUIActivateShieldController getInstance() {
        synchronized (GUIActivateEngineController.class) {
            if (instance == null) {
                instance = new GUIActivateShieldController();
            }
            return instance;
        }
    }

    public void updateShip(){
        shipController.displayShip();
    }

    public GUIActivateShieldController() {
        try {
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(GUIActivateShieldController.class.getResource("/it/polimi/it/galaxytrucker/fxmlstages/activateShield.fxml")));
            loader.setController(this);
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadCardDetails() {
        ClientModel model = GUIView.getInstance().getClient().getModel();
        List<List<String>> serializedProjectiles = model.getUnsafeCardDetail("projectiles");
        for(List<String> serializedProjectile : serializedProjectiles ) {
            projectiles.add(new Projectile(ProjectileType.valueOf(serializedProjectile.get(0)), Direction.valueOf(serializedProjectile.get(1))));
            if (model.getCurrentCard().equals("Pirates") )
                projectilesRolledCoord.add(Integer.parseInt(serializedProjectile.get(2)));
        }
    }

    @FXML
    public void initialize() {
        ClientModel model = GUIView.getInstance().getClient().getModel();
        if (model.getCurrentCard().equals("Pirates") || model.getCurrentCard().equals("MeteorSwarm") || model.getCurrentCard().equals("CombatZone")) {
            projectilesButton.setVisible(true);
        } else projectilesButton.setVisible(false);
    }

    @FXML
    private void addShield(){
        int col = shipController.selectedColumn;
        int row = shipController.selectedRow;
        if((row<5) || (row>9) || (col<4) || (col>10)){
            incorrectCoord1.setText("INCORRECT COORDINATES");
            incorrectCoord1.setVisible(true);
        } else {
            if(!shieldCoords.contains(new Coordinates(col, row))) {
                if (GUIView.getInstance().getClient().getModel().getPlayerShips(GUIView.getInstance().getClient().getModel().getMyData().getPlayerId()).get(row - 5).get(col - 4) != null) {
                    if (GUIView.getInstance().getClient().getModel().getPlayerShips(GUIView.getInstance().getClient().getModel().getMyData().getPlayerId()).get(row - 5).get(col - 4).type().equals(Shield.class.getSimpleName())) {
                        shieldCoords.add(new Coordinates(row, col));
                        incorrectCoord1.setText("ADD SHIELD");
                        incorrectCoord1.setVisible(true);
                    } else {
                        incorrectCoord1.setText("INCORRECT COORDINATES");
                        incorrectCoord1.setVisible(true);
                    }
                } else {
                    incorrectCoord1.setText("INCORRECT COORDINATES");
                    incorrectCoord1.setVisible(true);
                }
            } else{
                    incorrectCoord1.setText("INCORRECT COORDINATES");
                    incorrectCoord1.setVisible(true);
                }
        }
    }

    @FXML
    private void addBattery(){
        int col = shipController.selectedColumn;
        int row = shipController.selectedRow;
        if((row<5) || (row>9) || (col<4) || (col>10)){
            incorrectCoord2.setText("INCORRECT COORDINATES");
            incorrectCoord2.setVisible(true);
        } else {
            if(!batteryCoord.contains(new Coordinates(col, row))) {
                if (GUIView.getInstance().getClient().getModel().getPlayerShips(GUIView.getInstance().getClient().getModel().getMyData().getPlayerId()).get(row - 5).get(col - 4) != null) {
                    if (GUIView.getInstance().getClient().getModel().getPlayerShips(GUIView.getInstance().getClient().getModel().getMyData().getPlayerId()).get(row - 5).get(col - 4).type().equals(BatteryComponent.class.getSimpleName())) {
                        batteryCoord.add(new Coordinates(row, col));
                        incorrectCoord2.setText("ADD BATTERY");
                        incorrectCoord2.setVisible(true);
                    } else {
                        incorrectCoord2.setText("INCORRECT COORDINATES");
                        incorrectCoord2.setVisible(true);
                    }
                } else {
                    incorrectCoord2.setText("INCORRECT COORDINATES");
                    incorrectCoord2.setVisible(true);
                }
            } else{
                incorrectCoord2.setText("INCORRECT COORDINATES");
                incorrectCoord2.setVisible(true);
            }
        }
    }

    public void resetCoord(){
        incorrectCoord1.setVisible(false);
        incorrectCoord2.setVisible(false);
        incorrectValue.setVisible(true);
        for(String key:imageTiles.keySet()){
            imageTiles.get(key).setStyle("");
        }
    }

    @FXML
    private void activateShield(){
        if(!shieldCoords.isEmpty() && !batteryCoord.isEmpty() && (shieldCoords.size()==batteryCoord.size())){
            shieldAndBatteryCoord.add(shieldCoords);
            shieldAndBatteryCoord.add(batteryCoord);

            GUIView.getInstance().getClient().receiveUserInput(
                    new UserInput.UserInputBuilder(UserInputType.ACTIVATE_SHIELD)
                            .setComponentsForActivation(shieldAndBatteryCoord)
                            .build()
            );
        } else{
            if(shieldCoords.isEmpty() && batteryCoord.isEmpty() ) {
                shieldAndBatteryCoord.clear();
                GUIView.getInstance().getClient().receiveUserInput(
                        new UserInput.UserInputBuilder(UserInputType.ACTIVATE_SHIELD)
                                .setComponentsForActivation(shieldAndBatteryCoord)
                                .build()
                );
            }
            else resetCoord();
        }

        shieldAndBatteryCoord.clear();
        shieldCoords.clear();
        batteryCoord.clear();
    }

    @FXML
    private void projectilesView(){
        shieldPane.setVisible(false);
        projectilePane.setVisible(true);
        loadCardDetails();
        cont=0;
        displayProjectiles();
    }

    private void displayProjectiles(){
        ClientModel model = GUIView.getInstance().getClient().getModel();
        if (model.getCurrentCard().equals("Pirates"))
            projectileValue.setText(projectilesRolledCoord.get(cont).toString());
        else trasparent.setVisible(false);
        if (model.getCurrentCard().equals("Pirates")  || model.getCurrentCard().equals("CombatZone")) {
            displayCannon();
        } else displayMeteor();
    }

    @FXML
    private void leftImage(){
        if (cont >= 0) {
            cont--;
            displayProjectiles();
        } else{
            cont=projectiles.size()-1;
        }
    }

    @FXML
    private void rightImage(){
        if (cont < projectiles.size()-1) {
            cont++;
            displayProjectiles();
        } else {
            cont=0;
            displayProjectiles();
        }
    }

    private void displayMeteor(){
        // Prova
        projectileImage.setImage(new Image(Objects.requireNonNull(GUIActivateCannonController.class.getResourceAsStream("/it/polimi/it/galaxytrucker/graphics/general/bigMeteor_down.PNG"))));

        if(!projectiles.isEmpty()){
            System.out.println(projectiles.get(cont));
            if(projectiles.get(cont).getSize() == ProjectileType.BIG){
                if(projectiles.get(cont).getDirection() == Direction.DOWN)
                    projectileImage.setImage(new Image(Objects.requireNonNull(GUIActivateCannonController.class.getResourceAsStream("/it/polimi/it/galaxytrucker/graphics/general/bigMeteor_down.PNG"))));
                else if (projectiles.get(cont).getDirection() == Direction.UP) {
                    projectileImage.setImage(new Image(Objects.requireNonNull(GUIActivateCannonController.class.getResourceAsStream("/it/polimi/it/galaxytrucker/graphics/general/bigMeteor_up.PNG"))));
                } else if (projectiles.get(cont).getDirection() == Direction.LEFT) {
                    projectileImage.setImage(new Image(Objects.requireNonNull(GUIActivateCannonController.class.getResourceAsStream("/it/polimi/it/galaxytrucker/graphics/general/bigMeteor_left.PNG"))));
                } else if (projectiles.get(cont).getDirection() == Direction.RIGHT) {
                    projectileImage.setImage(new Image(Objects.requireNonNull(GUIActivateCannonController.class.getResourceAsStream("/it/polimi/it/galaxytrucker/graphics/general/bigMeteor_right.PNG"))));
                }
            } else if (projectiles.get(cont).getSize() == ProjectileType.SMALL) {
                if(projectiles.get(cont).getDirection() == Direction.DOWN)
                    projectileImage.setImage(new Image(Objects.requireNonNull(GUIActivateCannonController.class.getResourceAsStream("/it/polimi/it/galaxytrucker/graphics/general/smallMeteor_down.PNG"))));
                else if (projectiles.get(cont).getDirection() == Direction.UP) {
                    projectileImage.setImage(new Image(Objects.requireNonNull(GUIActivateCannonController.class.getResourceAsStream("/it/polimi/it/galaxytrucker/graphics/general/smallMeteor_up.PNG"))));
                } else if (projectiles.get(cont).getDirection() == Direction.LEFT) {
                    projectileImage.setImage(new Image(Objects.requireNonNull(GUIActivateCannonController.class.getResourceAsStream("/it/polimi/it/galaxytrucker/graphics/general/smallMeteor_left.PNG"))));
                } else if (projectiles.get(cont).getDirection() == Direction.RIGHT) {
                    projectileImage.setImage(new Image(Objects.requireNonNull(GUIActivateCannonController.class.getResourceAsStream("/it/polimi/it/galaxytrucker/graphics/general/smallMeteor_right.PNG"))));
                }
            }
        }
    }

    private void displayCannon(){
        // Prova
        projectileImage.setImage(new Image(Objects.requireNonNull(GUIActivateCannonController.class.getResourceAsStream("/it/polimi/it/galaxytrucker/graphics/general/bigCannon_down.PNG"))));

        if(!projectiles.isEmpty()){
            System.out.println(projectiles.get(cont));
            if(projectiles.get(cont).getSize() == ProjectileType.BIG){
                if(projectiles.get(cont).getDirection() == Direction.DOWN)
                    projectileImage.setImage(new Image(Objects.requireNonNull(GUIActivateCannonController.class.getResourceAsStream("/it/polimi/it/galaxytrucker/graphics/general/bigCannon_down.PNG"))));
                else if (projectiles.get(cont).getDirection() == Direction.UP) {
                    projectileImage.setImage(new Image(Objects.requireNonNull(GUIActivateCannonController.class.getResourceAsStream("/it/polimi/it/galaxytrucker/graphics/general/bigCannon_up.PNG"))));
                }
            } else if (projectiles.get(cont).getSize() == ProjectileType.SMALL) {
                if(projectiles.get(cont).getDirection() == Direction.DOWN)
                    projectileImage.setImage(new Image(Objects.requireNonNull(GUIActivateCannonController.class.getResourceAsStream("/it/polimi/it/galaxytrucker/graphics/general/smallCannon_down.PNG"))));
                else if (projectiles.get(cont).getDirection() == Direction.UP) {
                    projectileImage.setImage(new Image(Objects.requireNonNull(GUIActivateCannonController.class.getResourceAsStream("/it/polimi/it/galaxytrucker/graphics/general/smallCannon_up.PNG"))));
                }
            }
        }
    }



    @FXML
    private void back(){
        shieldPane.setVisible(true);
        projectilePane.setVisible(false);
        incorrectValue.setVisible(false);
        incorrectCoord1.setVisible(false);
        incorrectCoord2.setVisible(false);
    }

    @Override
    public void displayScene() {
        Platform.runLater(() -> {
            try {
                FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(
                        GUIActivateShieldController.class.getResource("/it/polimi/it/galaxytrucker/fxmlstages/activateShield.fxml")
                ));
                loader.setController(this);
                Parent newRoot = loader.load();

                stage = (Stage) GUIView.stage.getScene().getWindow();
                scene = new Scene(newRoot);
                stage.setScene(scene);
                stage.show();
                shipController.displayShip();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}