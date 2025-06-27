package it.polimi.it.galaxytrucker.view.GUI.controllers;

import it.polimi.it.galaxytrucker.messages.clientmessages.UserInput;
import it.polimi.it.galaxytrucker.messages.clientmessages.UserInputType;
import it.polimi.it.galaxytrucker.model.componenttiles.*;
import it.polimi.it.galaxytrucker.model.utility.Coordinates;
import it.polimi.it.galaxytrucker.networking.client.clientmodel.ClientModel;
import it.polimi.it.galaxytrucker.view.GUI.GUIView;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;
import javafx.collections.SetChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class GUICrewmatePenaltyController extends GUIViewState implements GUIErrorHandler {

    /* ---------- FXML nodes ---------- */
    @FXML private PlayerShipElementController shipController;
    @FXML private ListView<CheckBox> cabinListView;
    @FXML private Label counterLabel;
    @FXML private Label incorrectValue;

    /* ---------- internal ---------- */
    private final ObservableSet<CheckBox> selected = FXCollections.observableSet();
    private int  crewmatePenalty = 1;

    /* ---------- singleton ---------- */
    private static GUICrewmatePenaltyController instance;
    public static GUICrewmatePenaltyController getInstance() {
        synchronized (GUICrewmatePenaltyController.class) {
            if (instance == null) instance = new GUICrewmatePenaltyController();
            return instance;
        }
    }

    /* ---------- constructor ---------- */
    public GUICrewmatePenaltyController() {
        loadFXML();
    }

    private void loadFXML() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    Objects.requireNonNull(getClass().getResource(
                            "/it/polimi/it/galaxytrucker/fxmlstages/crewmatePenalty.fxml")));
            loader.setController(this);
            root = loader.load();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /* ---------- init ---------- */
    @FXML
    public void initialize() {
        ClientModel model = GUIView.getInstance().getClient().getModel();
        crewmatePenalty  = model.getCardDetail("crewmatePenalty", Integer.class);

        buildCabinList();
        counterLabel.setText("0 / " + crewmatePenalty + " selected");
        shipController.displayShip();
    }

    /* ---------- crea lista con una voce per ogni crewmate ---------- */
    private void buildCabinList() {
        cabinListView.getItems().clear();
        selected.clear(); // reset previous selections

        ClientModel model = GUIView.getInstance().getClient().getModel();
        List<List<TileData>> ship = model.getPlayerShips(model.getMyData().getPlayerId());

        for (int r = 0; r < ship.size(); r++) {
            for (int c = 0; c < ship.get(r).size(); c++) {
                TileData td = ship.get(r).get(c);
                if (td == null) continue;

                boolean isCabin = td.type().equals(CabinModule.class.getSimpleName())
                        || td.type().equals(CentralCabin.class.getSimpleName());

                if (isCabin) {
                    int crew = td.crewmates().size();
                    for (int k = 1; k <= crew; k++) {
                        String label = "(" + (c + 4) + "," + (r + 5) + ")#" + k;
                        cabinListView.getItems().add(buildLimitedCheckBox(label));
                    }
                }
            }
        }

        // Re-add listener (only once)
        counterLabel.setText(selected.size() + " / " + crewmatePenalty + " selected");
        selected.addListener((SetChangeListener<CheckBox>) ch ->
                counterLabel.setText(selected.size() + " / " + crewmatePenalty + " selected"));
    }

    private CheckBox buildLimitedCheckBox(String label) {
        CheckBox cb = new CheckBox(label);
        BooleanProperty p = cb.selectedProperty();

        p.addListener((obs, oldV, newV) -> {
            if (newV) {
                if (selected.size() < crewmatePenalty) {
                    selected.add(cb);
                } else {
                    // superato limite: torna falso
                    Platform.runLater(() -> cb.setSelected(false));
                }
            } else {
                selected.remove(cb);
            }
        });
        return cb;
    }

    /* ---------- invio dei dati ---------- */
    @FXML
    private void applyPenalty() {

        List<Coordinates> coords = selected.stream()
            .map(cb -> cb.getText().split("#")[0])          // prendi parte "(x,y)"
            .map(s -> s.replace("(", "").replace(")", "").split(","))
            .map(xy -> new Coordinates(Integer.parseInt(xy[0].trim()),
                                       Integer.parseInt(xy[1].trim())))
            .collect(Collectors.toCollection(ArrayList::new));

        // riempi con placeholder
        while (coords.size() < crewmatePenalty)
            coords.add(new Coordinates(0, 0));

        GUIView.getInstance().getClient().receiveUserInput(
            new UserInput.UserInputBuilder(UserInputType.CREWMATE_PENALTY)
                         .setRemovedCrewmate(coords)
                         .build());
    }

    /* ---------- scene switch ---------- */
    @Override
    public void displayScene() {
        Platform.runLater(() -> {
            loadFXML();                                // nuovo root ogni volta
            selected.clear();
            Stage stage = (Stage) GUIView.stage.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        });
    }

    /* ---------- error dal server ---------- */
    @Override
    public void inputError() {
        incorrectValue.setVisible(true);
        selected.clear();
        counterLabel.setText("0 / " + crewmatePenalty + " selected");
    }
}