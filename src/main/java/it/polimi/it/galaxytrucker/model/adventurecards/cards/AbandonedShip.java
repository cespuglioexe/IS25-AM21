package it.polimi.it.galaxytrucker.model.adventurecards.cards;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import it.polimi.it.galaxytrucker.model.adventurecards.cardstates.CardStateMachine;
import it.polimi.it.galaxytrucker.model.adventurecards.cardstates.abandonedship.StartState;
import it.polimi.it.galaxytrucker.model.adventurecards.interfaces.AdventureCard;
import it.polimi.it.galaxytrucker.model.adventurecards.interfaces.AdventureCardInputContext;
import it.polimi.it.galaxytrucker.model.adventurecards.interfaces.AdventureCardVisitor;
import it.polimi.it.galaxytrucker.model.adventurecards.interfaces.CreditReward;
import it.polimi.it.galaxytrucker.model.adventurecards.interfaces.CrewmatePenalty;
import it.polimi.it.galaxytrucker.model.adventurecards.interfaces.FlightDayPenalty;
import it.polimi.it.galaxytrucker.model.adventurecards.interfaces.Participation;
import it.polimi.it.galaxytrucker.model.design.strategyPattern.FlightRules;
import it.polimi.it.galaxytrucker.exceptions.InvalidActionException;
import it.polimi.it.galaxytrucker.model.managers.Player;
import it.polimi.it.galaxytrucker.model.managers.ShipManager;

/**
 * Represents the "Abandoned Ship" adventure card in the game Galaxy Trucker.
 * <p>
 * This card allows players to decide whether to send a crewmember to explore a derelict ship
 * and claim a reward in credits. Only one player can participate, and their choice comes with
 * both a benefit and two penalties.
 *
 * <ul>
 *   <li>Players are offered the opportunity to participate in salvaging the abandoned ship.</li>
 *   <li>Only the first player to accept (in flight order) is allowed to participate.</li>
 *   <li>The participating player receives a credit reward, but also:</li>
 *   <ul>
 *     <li>loses one crewmember (chosen by position), and</li>
 *     <li>is moved backward on the flight board by a specified number of days.</li>
 *   </ul>
 *   <li>All other players simply are unaffected.</li>
 * </ul>
 *
 * The card is implemented as a Finite CLIViewState Machine, transitioning through the following states:
 * <pre>
 * StartState
 *     ↓
 * ParticipationState
 *     ↓
 * CreditRewardState
 *     ↓
 * CrewmatePenaltyState
 *     ↓
 * FlightDayPenaltyState
 *     ↓
 * EndState
 * </pre>
 * Each state handles a single phase of the card's effect and transitions to the next automatically.
 *
 * <h2>Implementation Notes</h2>
 * <ul>
 *   <li>Implements {@link Participation} with a single credit-based choice.</li>
 *   <li>Implements {@link CreditReward}, {@link CrewmatePenalty}, and {@link FlightDayPenalty}.</li>
 * </ul>
 * 
 * @author Stefano Carletto
 * @version 1.0
 *
 * @see AdventureCard
 * @see Participation
 * @see CreditReward
 * @see CrewmatePenalty
 * @see FlightDayPenalty
 * @see CardStateMachine
 */
public class AbandonedShip extends CardStateMachine implements AdventureCard, Participation<Integer>, CreditReward, CrewmatePenalty, FlightDayPenalty {
    private Optional<Player> partecipant = Optional.empty();
    private int creditReward;
    private int crewmatePenalty;
    private int flightDayPenalty;
    private String graphic;

    private FlightRules flightRules;

    public AbandonedShip(int creditReward, int crewmatePenalty, int flightDayPenalty, FlightRules flightRules, String graphic) {
        this.creditReward = creditReward;
        this.crewmatePenalty = crewmatePenalty;
        this.flightDayPenalty = flightDayPenalty;
        this.flightRules = flightRules;
        this.graphic = graphic;
    }

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

    @Override
    public Map<Integer, List<Integer>> getAvailableChoices() {
        List<List<Integer>> allChoices = getChoices();
        Set<Integer> takenChoices = getTakenChoices().keySet();

        return IntStream.range(0, allChoices.size())
            .filter(i -> !takenChoices.contains(i))
            .boxed()
            .collect(Collectors.toMap(i -> i, allChoices::get));
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

    @Override
    public String toString() {
        return "AbandonedShip{" +
                ", creditReward=" + creditReward +
                ", crewmatePenalty=" + crewmatePenalty +
                ", flightDayPenalty=" + flightDayPenalty +
                '}';
    }

    @Override
    public String getGraphicPath() {
        return graphic;
    }

    @Override
    public void accept(AdventureCardVisitor visitor, AdventureCardInputContext context) {
        visitor.visit(this, context);
    }

    public List<Player> getPlayerOrder() {
        return flightRules.getPlayerOrder();
    }

    @Override
    public HashMap<String, Object> getEventData() {
        HashMap<String, Object> data = new HashMap<>();

        data.put("creditReward", creditReward);
        data.put("crewmatePenalty", crewmatePenalty);
        data.put("flightDayPenalty", flightDayPenalty);

        return data;
    }
}
