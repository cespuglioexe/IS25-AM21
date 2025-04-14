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

        int occupiedPlanets = card.getTakenChoices().size();
        if (occupiedPlanets == numberOfPlanets || allPlayersDecided()) {
            fsm.changeState(new CargoRewardState());
        }
    }
    private boolean allPlayersDecided() {
        return ++playerDecisions == numberOfPlayers;
    }

    @Override
    public void exit(StateMachine fsm) {
        Planets card = (Planets) fsm; 

        card.initializeFirstPlayer();
    }
}
