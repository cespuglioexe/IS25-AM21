package it.polimi.it.galaxytrucker.view.GUI.controllers;

import it.polimi.it.galaxytrucker.messages.clientmessages.UserInput;
import it.polimi.it.galaxytrucker.messages.clientmessages.UserInputType;
import it.polimi.it.galaxytrucker.networking.client.clientmodel.ClientModel;
import it.polimi.it.galaxytrucker.view.GUI.GUIView;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class GUICreditChoiceController extends GUIViewState{

    @FXML
    private Label creditsPlayer;

    @FXML private Label creditCard;

    private static GUICreditChoiceController instance;

    public static GUICreditChoiceController getInstance() {
        synchronized (GUICreditChoiceController.class) {
            if (instance == null) {
                instance = new GUICreditChoiceController();
            }
            return instance;
        }
    }

    public GUICreditChoiceController() {
        try {
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(GUICreditChoiceController.class.getResource("/it/polimi/it/galaxytrucker/fxmlstages/creditRewardChoice.fxml")));
            loader.setController(this);
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void initialize() {
        loadCardDetails();
    }
    private void loadCardDetails() {
        ClientModel model = GUIView.getInstance().getClient().getModel();

        int creditReward = model.getCardDetail("creditReward", Integer.class);
        setCreditsCard(creditReward);
    }

    @FXML
    private void declineReward(){
        GUIView.getInstance().getClient().receiveUserInput(
                new UserInput.UserInputBuilder(UserInputType.CREDIT_REWARD)
                        .setCreditChoice(false)
                        .build()
        );
    }

    @FXML
    private void acceptReward(){
        GUIView.getInstance().getClient().receiveUserInput(
                new UserInput.UserInputBuilder(UserInputType.CREDIT_REWARD)
                        .setCreditChoice(true)
                        .build()
        );
    }

    public void setCreditsCard(int creditsCard){
        creditCard.setText(creditsCard + "");
    }


    @Override
    public void displayScene() {
        Platform.runLater(() -> {
            try {
                FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(
                        GUICargoChoiceController.class.getResource("/it/polimi/it/galaxytrucker/fxmlstages/creditRewardChoice.fxml")
                ));
                loader.setController(this);
                Parent newRoot = loader.load();

                stage = (Stage) GUIView.stage.getScene().getWindow();
                scene = new Scene(newRoot);
                stage.setScene(scene);
                stage.show();

                creditsPlayer.setText(""+GUIView.getInstance().getClient().getModel().getCredits());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
