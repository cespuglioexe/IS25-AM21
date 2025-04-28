package it.polimi.it.galaxytrucker.model.adventurecards.cardstates.abandonedstation;

import it.polimi.it.galaxytrucker.model.adventurecards.cardstates.abandonedstation.CargoRewardState;
import it.polimi.it.galaxytrucker.model.adventurecards.refactored.AbandonedStation;
import it.polimi.it.galaxytrucker.model.design.statePattern.State;
import it.polimi.it.galaxytrucker.model.design.statePattern.StateMachine;
import it.polimi.it.galaxytrucker.model.exceptions.InvalidActionException;

public class ParticipationState extends State {
    private int playerDecisions = 0;
    int numberOfPlayers;

    @Override
    public void enter(StateMachine fsm) {
        AbandonedStation card = (AbandonedStation) fsm;
        numberOfPlayers = card.getNumberOfBoardPlayers();
    }

    @Override
    public void update(StateMachine fsm) {
        AbandonedStation card = (AbandonedStation) fsm;

        if(card.isCardOccupied())        {
            if(card.hasPlayerRequiredNumberOfCrewmates()){
                fsm.changeState(new CargoRewardState());
            }else throw new InvalidActionException("The player does not have the required number of players");
        }
        if(allPlayersDecided())
            fsm.changeState(new EndState());
    }

    private boolean allPlayersDecided() {
        return ++playerDecisions == numberOfPlayers;
    }

    @Override
    public void exit(StateMachine fsm) {
    }
}

