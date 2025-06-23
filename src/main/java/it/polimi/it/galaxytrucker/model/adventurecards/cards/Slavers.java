package it.polimi.it.galaxytrucker.model.adventurecards.cards;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import it.polimi.it.galaxytrucker.model.adventurecards.cardstates.CardStateMachine;
import it.polimi.it.galaxytrucker.model.adventurecards.cardstates.slavers.StartState;
import it.polimi.it.galaxytrucker.model.adventurecards.interfaces.AdventureCard;
import it.polimi.it.galaxytrucker.model.adventurecards.interfaces.AdventureCardInputContext;
import it.polimi.it.galaxytrucker.model.adventurecards.interfaces.AdventureCardVisitor;
import it.polimi.it.galaxytrucker.model.adventurecards.interfaces.CreditReward;
import it.polimi.it.galaxytrucker.model.adventurecards.interfaces.CrewmatePenalty;
import it.polimi.it.galaxytrucker.model.adventurecards.interfaces.FlightDayPenalty;
import it.polimi.it.galaxytrucker.model.design.strategyPattern.FlightRules;
import it.polimi.it.galaxytrucker.exceptions.InvalidActionException;
import it.polimi.it.galaxytrucker.model.managers.Player;
import it.polimi.it.galaxytrucker.model.managers.ShipManager;

public class Slavers extends CardStateMachine implements AdventureCard, CreditReward, CrewmatePenalty, FlightDayPenalty {
    private Player currentPlayer;
    private double playerFirePower;
    private int requiredFirePower;
    private int creditReward;
    private int crewmatePenalty;
    private int flightDayPenalty;
    private String graphic;

    private FlightRules flightRules;

    public Slavers(int creditReward, int crewmatePenalty, int flightDayPenalty, int requiredFirePower, FlightRules flightRules) {
        this.creditReward = creditReward;
        this.crewmatePenalty = crewmatePenalty;
        this.flightDayPenalty = flightDayPenalty;
        this.flightRules = flightRules;
        this.requiredFirePower = requiredFirePower;
    }

    public Slavers(int creditReward, int crewmatePenalty, int flightDayPenalty, int requiredFirePower, FlightRules flightRules,String graphic) {
        this.creditReward = creditReward;
        this.crewmatePenalty = crewmatePenalty;
        this.flightDayPenalty = flightDayPenalty;
        this.flightRules = flightRules;
        this.requiredFirePower = requiredFirePower;
        this.graphic = graphic;
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
    public int getCreditReward() {
        return creditReward;
    }

    @Override
    public void applyCreditReward() {
        currentPlayer.addCredits(creditReward);
        updateState();
    }

    public void discardCreditReward(){
        creditReward = 0;
        flightDayPenalty = 0;
        updateState();
    }

    public void sellSlaves(List<List<Integer>> crewmatesCoords) throws InvalidActionException {
        if(currentPlayer.getShipManager().countCrewmates() > crewmatePenalty) {
            if (crewmatesCoords.size() < crewmatePenalty) {
                throw new InvalidActionException("Not enough crewmates selected");
            }
        }
        for (List<Integer> coord : crewmatesCoords) {
            applyCrewmatePenalty(coord.get(0), coord.get(1));
        }
        updateState();
    }

    @Override
    public void applyCrewmatePenalty(int shipRow, int shipColumn) {
        ShipManager ship = currentPlayer.getShipManager();

        ship.removeCrewmate(shipRow, shipColumn);
    }

    @Override
    public int getCrewmatePenalty() {
        return crewmatePenalty;
    }

    @Override
    public void applyFlightDayPenalty() {
        flightRules.movePlayerBackwards(flightDayPenalty, currentPlayer);
    }

    public void setPlayer(){
        List<Player> players = flightRules.getPlayerOrder();
        if(currentPlayer == null){
            currentPlayer = players.getFirst();
            return;
        }
        currentPlayer = nextPlayer(players).orElse(null);
    }

    private Optional<Player> nextPlayer(List<Player> players) {
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

    public int getRequiredFirePower() {
        return requiredFirePower;
    }


    @Override
    public String toString() {
        return "Slavers{" +
                "requiredFirePower=" + requiredFirePower +
                ", creditReward=" + creditReward +
                ", crewmatePenalty=" + crewmatePenalty +
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

        data.put("creditReward", creditReward);
        data.put("crewmatePenalty", crewmatePenalty);
        data.put("flightDayPenalty", flightDayPenalty);
        data.put("requiredFirePower", requiredFirePower);

        return data;
    }
}
