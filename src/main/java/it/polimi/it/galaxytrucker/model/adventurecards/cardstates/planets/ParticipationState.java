package it.polimi.it.galaxytrucker.model.adventurecards.cardstates.planets;

import it.polimi.it.galaxytrucker.model.design.observerPattern.Subject;
import it.polimi.it.galaxytrucker.model.design.statePattern.State;
import it.polimi.it.galaxytrucker.model.design.statePattern.StateMachine;
import it.polimi.it.galaxytrucker.model.managers.Player;

import java.util.List;

import it.polimi.it.galaxytrucker.model.adventurecards.cardevents.InputNeeded;
import it.polimi.it.galaxytrucker.model.adventurecards.cards.Planets;
import it.polimi.it.galaxytrucker.model.adventurecards.cardstates.EndState;

public class ParticipationState extends State {
    private int numberOfPlanets;
    private int numberOfPlayers;
    private int playerDecisions = 0;

    @Override
    public void enter(StateMachine fsm) {
        Planets card = (Planets) fsm;
        Subject subject = (Subject) fsm;

        numberOfPlanets = card.getPlanets().size();
        numberOfPlayers = card.getNumberOfBoardPlayers();

        subject.notifyObservers(new InputNeeded(card, getPlayerWhoChooses(card)));
    }

    @Override
    public void update(StateMachine fsm) {
        Planets card = (Planets) fsm;
        Subject subject = (Subject) fsm;

        if (allPlayersHaveResponded()) {
            if (noPlanetWasChosen(card)) {
                fsm.changeState(new EndState());
            } else {
                fsm.changeState(new CargoRewardState());
            }
            return;
        }

        if (allPlanetsAreOccupied(card)) {
            fsm.changeState(new CargoRewardState());
        }

        subject.notifyObservers(new InputNeeded(card, getPlayerWhoChooses(card)));
    }
    private boolean allPlayersHaveResponded() {
        return ++playerDecisions == numberOfPlayers;
    } 
    private boolean noPlanetWasChosen(Planets card) {
        return card.getTakenChoices().isEmpty();
    }
    private boolean allPlanetsAreOccupied(Planets card) {
        return card.getTakenChoices().size() == numberOfPlanets;
    }
    private Player getPlayerWhoChooses(Planets card) {
        List<Player> playersInFlightOrder = card.getPlayerOrder();

        return playersInFlightOrder.get(playerDecisions);
    }

    @Override
    public void exit(StateMachine fsm) {
        
    }
}
