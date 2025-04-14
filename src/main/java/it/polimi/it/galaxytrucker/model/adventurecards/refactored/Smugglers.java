package it.polimi.it.galaxytrucker.model.adventurecards.refactored;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

import it.polimi.it.galaxytrucker.model.adventurecards.interfaces.AdventureCard;
import it.polimi.it.galaxytrucker.model.adventurecards.interfaces.CargoPenalty;
import it.polimi.it.galaxytrucker.model.adventurecards.interfaces.CargoReward;
import it.polimi.it.galaxytrucker.model.adventurecards.interfaces.FlightDayPenalty;
import it.polimi.it.galaxytrucker.model.design.strategyPattern.FlightRules;
import it.polimi.it.galaxytrucker.model.managers.CargoManager;
import it.polimi.it.galaxytrucker.model.managers.Player;
import it.polimi.it.galaxytrucker.model.managers.ShipManager;
import it.polimi.it.galaxytrucker.model.utility.Cargo;

public class Smugglers implements AdventureCard, CargoReward, CargoPenalty, FlightDayPenalty {
    private Player currentPlayer;
    private int playerFirePower; //TODO initialize in FSM
    private final int requiredFirePower;
    private Set<Cargo> cargoReward;
    private final int cargoPenalty;
    private final int flightDayPenalty;

    private final FlightRules flightRules;

    public Smugglers(int requiredFirePower, Set<Cargo> cargoReward, int cargoPenalty, int flightDayPenalty, FlightRules flightRules) {
        this.requiredFirePower = requiredFirePower;
        this.cargoReward = cargoReward;
        this.cargoPenalty = cargoPenalty;
        this.flightDayPenalty = flightDayPenalty;
        this.flightRules = flightRules;
    }

    @Override
    public void play() {

    }

    public void selectCannons(HashMap<List<Integer>, List<Integer>> doubleCannonsAndBatteries) {
        ShipManager ship = currentPlayer.getShipManager();
        playerFirePower += ship.activateComponent(doubleCannonsAndBatteries);
    }
   
    @Override
    public Set<Cargo> getCargoReward() {
        return cargoReward;
    }

    @Override
    public void applyCargoReward() {
        
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
}
