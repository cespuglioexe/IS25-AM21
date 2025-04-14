package it.polimi.it.galaxytrucker.model.adventurecards.refactored;

import java.util.List;
import java.util.Set;

import it.polimi.it.galaxytrucker.model.adventurecards.interfaces.AdventureCard;
import it.polimi.it.galaxytrucker.model.adventurecards.interfaces.CreditReward;
import it.polimi.it.galaxytrucker.model.adventurecards.interfaces.CrewmatePenalty;
import it.polimi.it.galaxytrucker.model.adventurecards.interfaces.FlightDayPenalty;
import it.polimi.it.galaxytrucker.model.adventurecards.interfaces.Participation;
import it.polimi.it.galaxytrucker.model.design.strategyPattern.FlightRules;
import it.polimi.it.galaxytrucker.model.exceptions.InvalidActionException;
import it.polimi.it.galaxytrucker.model.managers.Player;
import it.polimi.it.galaxytrucker.model.managers.ShipManager;

public class AbandonedShip implements AdventureCard, Participation<Integer>, CreditReward, CrewmatePenalty, FlightDayPenalty {
    private Player partecipant;
    private int creditReward;
    private int crewmatePenalty;
    private int flightDayPenalty;

    private final FlightRules flightRules;

    public AbandonedShip(int creditReward, int crewmatePenalty, int flightDayPenalty, FlightRules flightRules) {
        this.creditReward = creditReward;
        this.crewmatePenalty = crewmatePenalty;
        this.flightDayPenalty = flightDayPenalty;
        this.flightRules = flightRules;
    }

    @Override
    public void play() {

    }

    @Override
    public void participate(Player player, int choice) throws InvalidActionException{
        if (isCardOccupied()) {
            throw new InvalidActionException("The card is occupied");
        }
        partecipant = player;
    }
    private boolean isCardOccupied() {
        if(partecipant.isEmpty())
        {
            return false;
        }
        return true;
    }

    @Override
    public void decline(Player player) {

    }

    @Override
    public List<Set<Integer>> getChoices() {
        return List.of(Set.of(creditReward));
    }

    @Override
    public int getCreditReward() {
        return creditReward;
    }

    @Override
    public void applyCreditReward() {
        partecipant.addCredits(creditReward);
    }

    @Override
    public void applyCrewmatePenalty(int shipRow, int shipColumn) {
        ShipManager ship = partecipant.getShipManager();

        ship.removeCrewmate(shipRow, shipColumn);
    }

    @Override
    public void applyFlightDayPenalty() {
        flightRules.movePlayerBackwards(flightDayPenalty, partecipant);
    }
}
