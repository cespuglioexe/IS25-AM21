package it.polimi.it.galaxytrucker.model.adventurecards.cardstates.planets;

import it.polimi.it.galaxytrucker.model.adventurecards.refactored.Planets;
import it.polimi.it.galaxytrucker.model.design.statePattern.State;
import it.polimi.it.galaxytrucker.model.design.statePattern.StateMachine;

public class ParticipationState extends State {
    private int numberOfPlanets;
    private int numberOfPlayers;
    private int playerDecisions = 0;

    @Override
    public void enter(StateMachine fsm) {
        Planets card = (Planets) fsm;

        numberOfPlanets = card.getPlanets().size();
        numberOfPlayers = card.getNumberOfBoardPlayers();
    }

    @Override
    public void update(StateMachine fsm) {
        Planets card = (Planets) fsm;

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

    @Override
    public void exit(StateMachine fsm) {
        
    }
}
