package it.polimi.it.galaxytrucker.model.adventurecards.cardstates.combatzone;

import it.polimi.it.galaxytrucker.model.adventurecards.cards.CombatZone;
import it.polimi.it.galaxytrucker.model.design.statePattern.State;
import it.polimi.it.galaxytrucker.model.design.statePattern.StateMachine;

public class CannonSelectionState extends State {
    private int numberOfPlayers;
    private int playerDecisions = 0;

    @Override
    public void enter(StateMachine fsm) {
        CombatZone card = (CombatZone) fsm;

        numberOfPlayers = card.getNumberOfBoardPlayers();
    }

    @Override
    public void update(StateMachine fsm) {
        if (allPlayersHaveResponded()) {
            changeState(fsm, new AttackState());
        }
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
