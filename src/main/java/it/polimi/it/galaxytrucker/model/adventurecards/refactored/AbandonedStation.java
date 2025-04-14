package it.polimi.it.galaxytrucker.model.adventurecards.refactored;

import java.util.List;
import java.util.Set;

import it.polimi.it.galaxytrucker.model.adventurecards.interfaces.AdventureCard;
import it.polimi.it.galaxytrucker.model.adventurecards.interfaces.CargoReward;
import it.polimi.it.galaxytrucker.model.adventurecards.interfaces.FlightDayPenalty;
import it.polimi.it.galaxytrucker.model.adventurecards.interfaces.Participation;
import it.polimi.it.galaxytrucker.model.design.strategyPattern.FlightRules;
import it.polimi.it.galaxytrucker.model.exceptions.InvalidActionException;
import it.polimi.it.galaxytrucker.model.managers.Player;
import it.polimi.it.galaxytrucker.model.utility.Cargo;

public class AbandonedStation implements AdventureCard, Participation<Cargo>, CargoReward, FlightDayPenalty {
    private Player partecipant;
    private Set<Cargo> cargoReward;
    private final int flightDayPenalty;

    private final FlightRules flightRules;

    public AbandonedStation(Set<Cargo> cargoReward, int flightDayPenalty, FlightRules flightRules) {
        this.cargoReward = cargoReward;
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
    public List<Set<Cargo>> getChoices() {
        return List.of(cargoReward);
    }
        
        
    @Override
    public Set<Cargo> getCargoReward() {
        return cargoReward;
    }

    @Override
    public void applyCargoReward() {
        
    }
    
    @Override
    public void applyFlightDayPenalty() {
        flightRules.movePlayerBackwards(flightDayPenalty, partecipant);
    }
}
