package it.polimi.it.galaxytrucker.model.adventurecards.cardstates.combatzone;

import java.util.List;
import java.util.Set;

import it.polimi.it.galaxytrucker.model.adventurecards.cardevents.InputNeeded;
import it.polimi.it.galaxytrucker.model.adventurecards.cards.CombatZone;
import it.polimi.it.galaxytrucker.model.componenttiles.CabinModule;
import it.polimi.it.galaxytrucker.model.componenttiles.CentralCabin;
import it.polimi.it.galaxytrucker.model.design.observerPattern.Subject;
import it.polimi.it.galaxytrucker.model.design.statePattern.State;
import it.polimi.it.galaxytrucker.model.design.statePattern.StateMachine;
import it.polimi.it.galaxytrucker.model.managers.Player;
import it.polimi.it.galaxytrucker.model.managers.ShipManager;

public class CrewmatePenaltyState extends State {
    private int penalty;
    private int appliedPenalty = 0;

    @Override
    public void enter(StateMachine fsm) {
        CombatZone card = (CombatZone) fsm;
        Subject subject = (Subject) fsm;

        penalty = card.getCrewmatePenalty();
        Player player = card.findPlayerWithLeastEnginePower();

        if (!hasEnoughCrewmates(player)) {
            removeAllCrewmatesFrom(player.getShipManager());
            changeState(fsm, new CannonSelectionState());
        } else {
            subject.notifyObservers(new InputNeeded(card, card.findPlayerWithLeastEnginePower()));
        }
    }

    @Override
    public void update(StateMachine fsm) {
        CombatZone card = (CombatZone) fsm;
        Subject subject = (Subject) fsm;
        if (++appliedPenalty == penalty) {
            changeState(fsm, new CannonSelectionState());
            return;
        }
        subject.notifyObservers(new InputNeeded(card, card.findPlayerWithLeastEnginePower()));
    }

    @Override
    public void exit(StateMachine fsm) {
        
    }

    private boolean hasEnoughCrewmates(Player player) {
        ShipManager ship = player.getShipManager();

        return ship.countCrewmates() >= penalty;
    }

    private void removeAllCrewmatesFrom(ShipManager ship) {
        Set<List<Integer>> cabins = ship.getAllComponentsPositionOfType(CabinModule.class);
        Set<List<Integer>> centralCabins = ship.getAllComponentsPositionOfType(CentralCabin.class);

        removeCrewmatesFromCabins(ship, cabins);
        removeCrewmatesFromCabins(ship, centralCabins);
    }
    private void removeCrewmatesFromCabins(ShipManager ship, Set<List<Integer>> cabins) {
        for (List<Integer> cabinCoord : cabins) {
            CentralCabin cabin = (CentralCabin) ship.getComponent(cabinCoord.get(0), cabinCoord.get(1)).get();

            while (!cabin.getCrewmates().isEmpty()) {
                ship.removeCrewmate(cabinCoord.get(0), cabinCoord.get(1));
            }
        }
    }   
}
