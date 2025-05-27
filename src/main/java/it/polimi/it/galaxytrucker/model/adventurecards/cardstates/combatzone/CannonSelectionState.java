package it.polimi.it.galaxytrucker.model.adventurecards.cardstates.combatzone;

import it.polimi.it.galaxytrucker.model.adventurecards.cardevents.InputNeeded;
import it.polimi.it.galaxytrucker.model.adventurecards.cards.CombatZone;
import it.polimi.it.galaxytrucker.model.design.observerPattern.Subject;
import it.polimi.it.galaxytrucker.model.design.statePattern.State;
import it.polimi.it.galaxytrucker.model.design.statePattern.StateMachine;

public class CannonSelectionState extends State {
    private int numberOfPlayers;
    private int playerDecisions = 0;

    @Override
    public void enter(StateMachine fsm) {
        CombatZone card = (CombatZone) fsm;
        Subject subject = (Subject) fsm;
        numberOfPlayers = card.getNumberOfBoardPlayers();
        subject.notifyObservers(new InputNeeded(card));
    }

    @Override
    public void update(StateMachine fsm) {
        CombatZone card = (CombatZone) fsm;
        Subject subject = (Subject) fsm;
        if (allPlayersHaveResponded()) {
            changeState(fsm, new AttackState());
        }
        subject.notifyObservers(new InputNeeded(card));
    }
    private boolean allPlayersHaveResponded() {
        return ++playerDecisions == numberOfPlayers;
    } 

    @Override
    public void exit(StateMachine fsm) {
        CombatZone card = (CombatZone) fsm;

        card.findPlayerWithLeastFirePower();
    }
    
}
