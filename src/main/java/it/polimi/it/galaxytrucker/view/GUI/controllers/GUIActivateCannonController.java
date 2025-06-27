package it.polimi.it.galaxytrucker.view.GUI.controllers;

import it.polimi.it.galaxytrucker.messages.clientmessages.UserInput;
import it.polimi.it.galaxytrucker.messages.clientmessages.UserInputType;
import it.polimi.it.galaxytrucker.model.adventurecards.interfaces.attack.Projectile;
import it.polimi.it.galaxytrucker.model.componenttiles.*;
import it.polimi.it.galaxytrucker.model.utility.*;
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
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.*;

public class GUIActivateCannonController extends GUIViewState {

    private Map<String, ImageView> imageTiles = new HashMap<>();
    private List<Coordinates> cannonCoords = new ArrayList<>();
    private List<Coordinates> batteryCoord = new ArrayList<>();
    private List<List<Coordinates>> cannonAndBatteryCoord = new ArrayList<>();
    int cont;
    List<Projectile> projectiles = new ArrayList<>();

    @FXML
    private Label incorrectCoord1,incorrectCoord2,incorrectValue;

    @FXML private PlayerShipElementController shipController;

    @FXML Pane cannonPane, projectilePane;

    @FXML
    Button projectilesButton;

    @FXML ImageView projectileImage;




    private static GUIActivateCannonController instance;

    public static GUIActivateCannonController getInstance() {
        synchronized (GUIActivateCannonController.class) {
            if (instance == null) {
                instance = new GUIActivateCannonController();
            }
            return instance;
        }
    }

    public void updateShip(){
        shipController.displayShip();
    }

    public GUIActivateCannonController() {
        try {
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(GUIActivateCannonController.class.getResource("/it/polimi/it/galaxytrucker/fxmlstages/activateCannon.fxml")));
            loader.setController(this);
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void initialize() {
        ClientModel model = GUIView.getInstance().getClient().getModel();
        if (model.getCurrentCard().equals("MeteorSwarm")) {
            projectilesButton.setVisible(true);
        } else projectilesButton.setVisible(false);
    }

    private void loadCardDetails() {
        ClientModel model = GUIView.getInstance().getClient().getModel();
        List<List<String>> serializedProjectiles = model.getUnsafeCardDetail("projectiles");
        if(!serializedProjectiles.isEmpty()){
            for(List<String> serializedProjectile : serializedProjectiles ) {
                projectiles.add(new Projectile(ProjectileType.valueOf(serializedProjectile.get(0)), Direction.valueOf(serializedProjectile.get(1))));
                System.out.println(projectiles.get(projectiles.size()-1));
            }
        } System.out.println("Errore: è vuota!!!1");
    }

    @FXML
    private void addCannon(){
        int col = shipController.selectedColumn;
        int row = shipController.selectedRow;

        if((row<5) || (row>9) || (col<4) || (col>10)){
            incorrectCoord1.setText("INCORRECT COORDINATES");
            incorrectCoord1.setVisible(true);
        } else {
            if (GUIView.getInstance().getClient().getModel().getPlayerShips(GUIView.getInstance().getClient().getModel().getMyData().getPlayerId()).get(row - 5).get(col - 4) != null) {
                if (GUIView.getInstance().getClient().getModel().getPlayerShips(GUIView.getInstance().getClient().getModel().getMyData().getPlayerId()).get(row - 5).get(col - 4).type().equals(DoubleCannon.class.getSimpleName())) {
                    cannonCoords.add(new Coordinates(row, col));
                    incorrectCoord1.setText("ADD CANNON");
                    incorrectCoord1.setVisible(true);
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
            if (GUIView.getInstance().getClient().getModel().getPlayerShips(GUIView.getInstance().getClient().getModel().getMyData().getPlayerId()).get(row - 5).get(col - 4) != null) {
                if (GUIView.getInstance().getClient().getModel().getPlayerShips(GUIView.getInstance().getClient().getModel().getMyData().getPlayerId()).get(row-5).get(col-4).type().equals(BatteryComponent.class.getSimpleName())) {
                    batteryCoord.add(new Coordinates(row, col));
                    incorrectCoord2.setText("ADD BATTERY");
                    incorrectCoord2.setVisible(true);
                } else{
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
    }

    @FXML
    private void projectilesView(){
        cannonPane.setVisible(false);
        projectilePane.setVisible(true);
        loadCardDetails();
        cont=0;
        displayProjectiles();
    }

    private void displayProjectiles(){
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

    @FXML
    private void leftImage(){
        if (cont > 0) {
            cont--;
            displayProjectiles();
        } else{
            cont=projectiles.size()-1;
            displayProjectiles();
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

    @FXML
    private void activateCannon(){
        if(!cannonCoords.isEmpty() && !batteryCoord.isEmpty() && (cannonCoords.size()==batteryCoord.size())){
            cannonAndBatteryCoord.add(cannonCoords);
            cannonAndBatteryCoord.add(batteryCoord);

            GUIView.getInstance().getClient().receiveUserInput(
                    new UserInput.UserInputBuilder(UserInputType.ACTIVATE_CANNON)
                            .setComponentsForActivation(cannonAndBatteryCoord)
                            .build()
            );
        } else{
            if(cannonCoords.isEmpty() && batteryCoord.isEmpty() ) {
                cannonAndBatteryCoord.clear();
                GUIView.getInstance().getClient().receiveUserInput(
                        new UserInput.UserInputBuilder(UserInputType.ACTIVATE_CANNON)
                                .setComponentsForActivation(cannonAndBatteryCoord)
                                .build()
                );
            }
            else resetCoord();
        }

        cannonAndBatteryCoord.clear();
        cannonCoords.clear();
        batteryCoord.clear();
    }



    @FXML
    private void back(){
        cannonPane.setVisible(true);
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
                        GUIActivateCannonController.class.getResource("/it/polimi/it/galaxytrucker/fxmlstages/activateCannon.fxml")
                ));
                loader.setController(this);
                Parent newRoot = loader.load();

                stage = (Stage) GUIView.stage.getScene().getWindow();
                scene = new Scene(newRoot);
                stage.setScene(scene);
                stage.show();

                updateShip();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
