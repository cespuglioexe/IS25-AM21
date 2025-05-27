package it.polimi.it.galaxytrucker.model.adventurecards.cardstates.abandonedstation;

import it.polimi.it.galaxytrucker.model.adventurecards.cardevents.InputNeeded;
import it.polimi.it.galaxytrucker.model.adventurecards.cards.AbandonedStation;
import it.polimi.it.galaxytrucker.model.design.observerPattern.Subject;
import it.polimi.it.galaxytrucker.model.design.statePattern.State;
import it.polimi.it.galaxytrucker.model.design.statePattern.StateMachine;
import it.polimi.it.galaxytrucker.exceptions.InvalidActionException;
import it.polimi.it.galaxytrucker.model.adventurecards.cardstates.EndState;

public class ParticipationState extends State {
    private int playerDecisions = 0;
    int numberOfPlayers;

    @Override
    public void enter(StateMachine fsm) {
        AbandonedStation card = (AbandonedStation) fsm;
        Subject subject = (Subject) fsm;
        numberOfPlayers = card.getNumberOfBoardPlayers();
        subject.notifyObservers(new InputNeeded(card));
    }

    @Override
    public void update(StateMachine fsm) {
        AbandonedStation card = (AbandonedStation) fsm;
        Subject subject = (Subject) fsm;

        if(card.isCardOccupied())        {
            if(card.hasPlayerRequiredNumberOfCrewmates()){
                fsm.changeState(new CargoRewardState());
            }else throw new InvalidActionException("The player does not have the required number of players");
        }
        if(allPlayersDecided())
            fsm.changeState(new EndState());
        subject.notifyObservers(new InputNeeded(card));
    }

    private boolean allPlayersDecided() {
        return ++playerDecisions == numberOfPlayers;
    }

    @Override
    public void exit(StateMachine fsm) {
    }
}

