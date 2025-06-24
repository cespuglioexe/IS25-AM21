package it.polimi.it.galaxytrucker.model.adventurecards.cards;

import java.util.*;

import it.polimi.it.galaxytrucker.model.adventurecards.cardstates.CardStateMachine;
import it.polimi.it.galaxytrucker.model.adventurecards.cardstates.smugglers.StartState;
import it.polimi.it.galaxytrucker.model.adventurecards.interfaces.AdventureCard;
import it.polimi.it.galaxytrucker.model.adventurecards.interfaces.AdventureCardInputContext;
import it.polimi.it.galaxytrucker.model.adventurecards.interfaces.AdventureCardVisitor;
import it.polimi.it.galaxytrucker.model.adventurecards.interfaces.CargoPenalty;
import it.polimi.it.galaxytrucker.model.adventurecards.interfaces.CargoReward;
import it.polimi.it.galaxytrucker.model.adventurecards.interfaces.FlightDayPenalty;
import it.polimi.it.galaxytrucker.model.design.strategyPattern.FlightRules;
import it.polimi.it.galaxytrucker.model.managers.CargoManager;
import it.polimi.it.galaxytrucker.model.managers.Player;
import it.polimi.it.galaxytrucker.model.managers.ShipManager;
import it.polimi.it.galaxytrucker.model.utility.Cargo;

public class Smugglers extends CardStateMachine implements AdventureCard, CargoReward, CargoPenalty, FlightDayPenalty {
    private Player currentPlayer;
    private double playerFirePower;
    private int requiredFirePower;
    private List<Cargo> cargoReward;
    private int cargoPenalty;
    private int flightDayPenalty;
    private String graphic;

    private FlightRules flightRules;

    public Smugglers(int requiredFirePower, List<Cargo> cargoReward, int cargoPenalty, int flightDayPenalty, FlightRules flightRules) {
        this.requiredFirePower = requiredFirePower;
        this.cargoReward = loadCargoList(cargoReward);
        this.cargoPenalty = cargoPenalty;
        this.flightDayPenalty = flightDayPenalty;
        this.flightRules = flightRules;
    }

    public Smugglers(int requiredFirePower, List<Cargo> cargoReward, int cargoPenalty, int flightDayPenalty, FlightRules flightRules, String graphic) {
        this.requiredFirePower = requiredFirePower;
        this.cargoReward = loadCargoList(cargoReward);
        this.cargoPenalty = cargoPenalty;
        this.flightDayPenalty = flightDayPenalty;
        this.flightRules = flightRules;
        this.graphic=graphic;
    }

    @Override
    public String getGraphicPath() {
        return graphic;
    }

    @Override
    public void play() { start(new StartState());};

    public void selectCannons(HashMap<List<Integer>, List<Integer>> doubleCannonsAndBatteries) {
        ShipManager ship = currentPlayer.getShipManager();
        playerFirePower += ship.activateComponent(doubleCannonsAndBatteries);
        updateState();
    }

    public void selectNoCannons() {
        updateState();
    }
   
    @Override
    public List<Cargo> getCargoReward() {
        return cargoReward;
    }

    private List<Cargo> loadCargoList(List<Cargo> list){
        return new ArrayList<>(list);
    }

    @Override
    public void acceptCargo(int loadIndex,int row, int column) {
        Cargo cargo = removeCargoFromCard(loadIndex);
        CargoManager.manageCargoAddition(cargo, List.of(row, column), currentPlayer);

        updateState();
    }

    @Override
    public void discardCargo(int loadIndex) {
        updateState();
    }

    private Cargo removeCargoFromCard(int loadIndex) {
        List<Cargo> cargoList = cargoReward;
        Cargo cargo = cargoList.get(loadIndex);
        cargoList.remove(loadIndex);

        return cargo;
    }

    @Override
    public void applyCargoPenalty() {
        CargoManager cargoManager = new CargoManager();
        cargoManager.manageCargoDischarge(cargoPenalty, currentPlayer);
    }
    
    @Override
    public void applyFlightDayPenalty() {
        flightRules.movePlayerBackwards(flightDayPenalty, currentPlayer);
    }

    public void setPlayer(){
        List<Player> players = flightRules.getPlayerOrder();
        if(currentPlayer == null){
            currentPlayer = players.get(0);
            return;
        }
        currentPlayer = nextPlayer(players).orElse(null);
    }

    public Optional<Player> nextPlayer(List<Player> players) {
        for(int i=0;i<players.size();i++){
            if(players.get(i).equals(currentPlayer) && (i+1) < players.size()) {
                return Optional.of(players.get(i + 1));
            }
        }
        return Optional.empty();
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void setPlayerFirePower(double playerFirePower) {
        this.playerFirePower = playerFirePower;
    }

    public void setRequiredFirePower(int requiredFirePower) {
        this.requiredFirePower = requiredFirePower;
    }

    public double getPlayerFirePower() {
        return playerFirePower;
    }

    public double getRequiredFirePower() {
        return requiredFirePower;
    }

    @Override
    public String toString() {
        return "Smugglers{" +
                "requiredFirePower=" + requiredFirePower +
                ", cargoReward=" + cargoReward +
                ", cargoPenalty=" + cargoPenalty +
                ", flightDayPenalty=" + flightDayPenalty +
                '}';
    }

    @Override
    public void accept(AdventureCardVisitor visitor, AdventureCardInputContext context) {
        visitor.visit(this, context);
    }

    @Override
    public HashMap<String, Object> getEventData() {
        HashMap<String, Object> data = new HashMap<>();

        data.put("cargoReward", serializeCargo());
        data.put("cargoPenalty", cargoPenalty);
        data.put("flightDayPenalty", flightDayPenalty);
        data.put("requiredFirePower", requiredFirePower);

        return data;
    }
    private List<String> serializeCargo() {
        List<String> serializedList = new ArrayList<>();

        for (Cargo cargo : cargoReward) {
            serializedList.add(cargo.getColor().toString());
        }

        return serializedList;
    }
}
