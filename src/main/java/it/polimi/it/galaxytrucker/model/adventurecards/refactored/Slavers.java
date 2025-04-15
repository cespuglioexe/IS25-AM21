package it.polimi.it.galaxytrucker.model.adventurecards.refactored;

import java.util.HashMap;
import java.util.List;

import it.polimi.it.galaxytrucker.model.adventurecards.interfaces.AdventureCard;
import it.polimi.it.galaxytrucker.model.adventurecards.interfaces.CreditReward;
import it.polimi.it.galaxytrucker.model.adventurecards.interfaces.CrewmatePenalty;
import it.polimi.it.galaxytrucker.model.adventurecards.interfaces.FlightDayPenalty;
import it.polimi.it.galaxytrucker.model.design.strategyPattern.FlightRules;
import it.polimi.it.galaxytrucker.model.exceptions.InvalidActionException;
import it.polimi.it.galaxytrucker.model.managers.Player;
import it.polimi.it.galaxytrucker.model.managers.ShipManager;

public class Slavers implements AdventureCard, CreditReward, CrewmatePenalty, FlightDayPenalty {
    private Player currentPlayer;
    private int playerFirePower;
    private int creditReward;
    private int crewmatePenalty;
    private int flightDayPenalty;

    private FlightRules flightRules;

    public Slavers(int creditReward, int crewmatePenalty, int flightDayPenalty, FlightRules flightRules) {
        this.creditReward = creditReward;
        this.crewmatePenalty = crewmatePenalty;
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
    public int getCreditReward() {
        return creditReward;
    }

    @Override
    public void applyCreditReward() {
        currentPlayer.addCredits(creditReward);
    }

    public void sellSlaves(List<List<Integer>> crewmatesCoords) throws InvalidActionException {
        if (crewmatesCoords.size() < crewmatePenalty) {
            throw new InvalidActionException("Not enough crewmates selected");
        }
        
        for (List<Integer> coord : crewmatesCoords) {
            applyCrewmatePenalty(coord.get(0), coord.get(1));
        }
    }

    @Override
    public void applyCrewmatePenalty(int shipRow, int shipColumn) {
        ShipManager ship = currentPlayer.getShipManager();

        ship.removeCrewmate(shipRow, shipColumn);
    }

    @Override
    public int getCrewmatePenalty() {
        return 0;
    }

    @Override
    public void applyFlightDayPenalty() {
        flightRules.movePlayerBackwards(flightDayPenalty, currentPlayer);
    }
}
