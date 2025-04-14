package it.polimi.it.galaxytrucker.model.adventurecards.refactored;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import it.polimi.it.galaxytrucker.model.adventurecards.cardstates.abandonedShip.StartState;
import it.polimi.it.galaxytrucker.model.adventurecards.interfaces.AdventureCard;
import it.polimi.it.galaxytrucker.model.adventurecards.interfaces.CreditReward;
import it.polimi.it.galaxytrucker.model.adventurecards.interfaces.CrewmatePenalty;
import it.polimi.it.galaxytrucker.model.adventurecards.interfaces.FlightDayPenalty;
import it.polimi.it.galaxytrucker.model.adventurecards.interfaces.Participation;
import it.polimi.it.galaxytrucker.model.design.statePattern.StateMachine;
import it.polimi.it.galaxytrucker.model.design.strategyPattern.FlightRules;
import it.polimi.it.galaxytrucker.model.exceptions.InvalidActionException;
import it.polimi.it.galaxytrucker.model.managers.Player;
import it.polimi.it.galaxytrucker.model.managers.ShipManager;

public class AbandonedShip extends StateMachine implements AdventureCard, Participation<Integer>, CreditReward, CrewmatePenalty, FlightDayPenalty {
    private Optional<Player> partecipant = Optional.empty();
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
        start(new StartState());
    }

    @Override
    public void participate(Player player, int choice) throws InvalidActionException{
        if (isCardOccupied()) {
            throw new InvalidActionException("The card is occupied");
        }
        partecipant = Optional.of(player);
        updateState();
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
        updateState();
    }

    @Override
    public HashMap<Integer, Player> getTakenChoices() {
        HashMap<Integer, Player> takenChoices = new HashMap<>();

        partecipant.ifPresent(player -> takenChoices.put(0, player));

        return takenChoices;
    }

    public Player getPartecipant() {
        return partecipant.orElseThrow(() -> 
            new IllegalStateException("No player is currently participating"));
    }

    @Override
    public List<List<Integer>> getChoices() {
        return List.of(List.of(creditReward));
    }

    @Override
    public int getCreditReward() {
        return creditReward;
    }

    @Override
    public void applyCreditReward() {
        partecipant.get().addCredits(creditReward);
    }

    @Override
    public void applyCrewmatePenalty(int shipRow, int shipColumn) {
        ShipManager ship = partecipant.get().getShipManager();

        ship.removeCrewmate(shipRow, shipColumn);
        updateState();
    }

    @Override
    public int getCrewmatePenalty() {
        return crewmatePenalty;
    }

    @Override
    public void applyFlightDayPenalty() {
        flightRules.movePlayerBackwards(flightDayPenalty, partecipant.get());
    }

    public int getNumberOfBoardPlayers() {
        return flightRules.getPlayerOrder().size();
    }
}
