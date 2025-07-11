package it.polimi.it.galaxytrucker.model.adventurecards.cards;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import it.polimi.it.galaxytrucker.model.adventurecards.cardstates.CardStateMachine;
import it.polimi.it.galaxytrucker.model.adventurecards.cardstates.abandonedstation.StartState;
import it.polimi.it.galaxytrucker.model.adventurecards.interfaces.AdventureCard;
import it.polimi.it.galaxytrucker.model.adventurecards.interfaces.AdventureCardInputContext;
import it.polimi.it.galaxytrucker.model.adventurecards.interfaces.AdventureCardVisitor;
import it.polimi.it.galaxytrucker.model.adventurecards.interfaces.CargoReward;
import it.polimi.it.galaxytrucker.model.adventurecards.interfaces.FlightDayPenalty;
import it.polimi.it.galaxytrucker.model.adventurecards.interfaces.Participation;
import it.polimi.it.galaxytrucker.model.design.strategyPattern.FlightRules;
import it.polimi.it.galaxytrucker.exceptions.InvalidActionException;
import it.polimi.it.galaxytrucker.model.managers.CargoManager;
import it.polimi.it.galaxytrucker.model.managers.Player;
import it.polimi.it.galaxytrucker.model.managers.ShipManager;
import it.polimi.it.galaxytrucker.model.utility.Cargo;

public class AbandonedStation extends CardStateMachine implements AdventureCard, Participation<Cargo>, CargoReward, FlightDayPenalty {
    private Optional<Player> partecipant = Optional.empty();
    private List<Cargo> cargoReward;
    private int flightDayPenalty;
    private int numberofCrewmatesRequired;
    private String graphic;

    private  FlightRules flightRules;

    public AbandonedStation(List<Cargo> cargoReward, int numberofCrewmatesRequired, int flightDayPenalty, FlightRules flightRules, String graphic ) {
        this.cargoReward = loadCargoList(cargoReward);
        this.flightDayPenalty = flightDayPenalty;
        this.flightRules = flightRules;
        this.numberofCrewmatesRequired = numberofCrewmatesRequired;
        this.graphic = graphic;
    }

    @Override
    public String getGraphicPath() {
        return graphic;
    }

    public AbandonedStation(List<Cargo> cargoReward, int numberofCrewmatesRequired, int flightDayPenalty, FlightRules flightRules ) {
        this.cargoReward = loadCargoList(cargoReward);
        this.flightDayPenalty = flightDayPenalty;
        this.flightRules = flightRules;
        this.numberofCrewmatesRequired = numberofCrewmatesRequired;
    }
    
    @Override
    public void play() { start(new StartState()); }
    
    @Override
    public void participate(Player player, int choice) throws InvalidActionException{
        if (isCardOccupied()) {
            throw new InvalidActionException("The card is occupied");
        }
        partecipant = Optional.of(player);
        updateState();
    }

    public int getNumberofCrewmatesRequired(){
        return numberofCrewmatesRequired;
    }


    public boolean isCardOccupied() {
        if(partecipant.isEmpty())
        {
            return false;
        }
        return true;
    }

    public boolean hasPlayerRequiredNumberOfCrewmates(){
        if(!partecipant.isEmpty()) {
            ShipManager ship = partecipant.get().getShipManager();
            return ship.countCrewmates() >= numberofCrewmatesRequired;
        }
        return false;
    }


    @Override
    public void decline(Player player) {
        updateState();
    }
    
    @Override
    public List<List<Cargo>> getChoices() {
        return List.of(cargoReward);
    }

    @Override
    public HashMap<Integer, Player> getTakenChoices() {
        HashMap<Integer, Player> takenChoices = new HashMap<>();

        partecipant.ifPresent(player -> takenChoices.put(0, player));

        return takenChoices;
    }

    @Override
    public Map<Integer, List<Cargo>> getAvailableChoices() {
        List<List<Cargo>> allChoices = getChoices();
        Set<Integer> takenChoices = getTakenChoices().keySet();

        return IntStream.range(0, allChoices.size())
            .filter(i -> !takenChoices.contains(i))
            .boxed()
            .collect(Collectors.toMap(i -> i, allChoices::get));
    }

    public Player getPartecipant() {
        return partecipant.orElseThrow(() -> new IllegalStateException("No player is currently participating"));
    }


    /**
     * Returns the total cargo reward collected from all selected planets.
     *
     * <p>This method aggregates all {@link Cargo} items available as rewards
     * across the selected planets and returns them as a single set.</p>
     *
     * @return a set containing all cargo rewards from all the planets
     */
    @Override
    public List<Cargo> getCargoReward() {
        return cargoReward;
    }

    @Override
    public void acceptCargo(int loadIndex,int row, int column) {
        Cargo cargo = removeCargoFromCard(loadIndex);
        CargoManager.manageCargoAddition(cargo, List.of(row, column), partecipant.get());

        updateState();
    }

    @Override
    public void discardCargo(int loadIndex) {
        removeCargoFromCard(loadIndex);
        updateState();
    }

    private List<Cargo> loadCargoList(List<Cargo> list){
        return new ArrayList<>(list);
    }

    private Cargo removeCargoFromCard(int loadIndex) {
        List<Cargo> cargoList = cargoReward;
        Cargo cargo = cargoList.get(loadIndex);
        cargoList.remove(loadIndex);

        return cargo;
    }

    /**
     * Applies the flight day penalty to all players who have landed on a planet.
     *
     * <p>The penalty is applied in reverse player order. Each affected player
     * is moved backwards on the flight board by a fixed number of steps
     * defined by the flight day penalty value.</p>
     */
    @Override
    public void applyFlightDayPenalty() {
        flightRules.movePlayerBackwards(flightDayPenalty, partecipant.get());
    }

    public int getNumberOfBoardPlayers() {
        return flightRules.getPlayerOrder().size();
    }

    @Override
    public String toString() {
        return "AbandonedStation{" +
                " cargoReward=" + cargoReward +
                ", flightDayPenalty=" + flightDayPenalty +
                ", numberofCrewmatesRequired=" + numberofCrewmatesRequired +
                '}';
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

        data.put("cargoReward", serializeCargo());
        data.put("flightDayPenalty", flightDayPenalty);
        data.put("numberofCrewmatesRequired", numberofCrewmatesRequired);

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
