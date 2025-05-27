package it.polimi.it.galaxytrucker.model.adventurecards.cardstates.abandonedship;

import it.polimi.it.galaxytrucker.model.adventurecards.cardevents.InputNeeded;
import it.polimi.it.galaxytrucker.model.adventurecards.cards.AbandonedShip;
import it.polimi.it.galaxytrucker.model.design.observerPattern.Subject;
import it.polimi.it.galaxytrucker.model.design.statePattern.State;
import it.polimi.it.galaxytrucker.model.design.statePattern.StateMachine;
import it.polimi.it.galaxytrucker.model.adventurecards.cardstates.EndState;

public class ParticipationState extends State {
    private int numberOfPlayers;
    private int playerDecisions = 0;

    @Override
    public void enter(StateMachine fsm) {
        AbandonedShip card = (AbandonedShip) fsm;
        Subject subject = (Subject) fsm;

        numberOfPlayers = card.getNumberOfBoardPlayers();
        subject.notifyObservers(new InputNeeded(card));
    }

    @Override
    public void update(StateMachine fsm) {
        AbandonedShip card = (AbandonedShip) fsm;
        Subject subject = (Subject) fsm;

        try {
            card.getPartecipant();
            changeState(fsm, new CreditRewardState());
        } catch (IllegalStateException e) {
            if (allPlayersHaveResponded()) {
                changeState(fsm, new EndState());
            }
        }
        subject.notifyObservers(new InputNeeded(card));
    }
    private boolean allPlayersHaveResponded() {
        return ++playerDecisions == numberOfPlayers;
    } 

    @Override
    public void exit(StateMachine fsm) {
        
    }
    
}
