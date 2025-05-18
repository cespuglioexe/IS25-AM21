package it.polimi.it.galaxytrucker.model.adventurecards.cardstates.combatzone;

import it.polimi.it.galaxytrucker.model.adventurecards.cards.CombatZone;
import it.polimi.it.galaxytrucker.model.design.statePattern.State;
import it.polimi.it.galaxytrucker.model.design.statePattern.StateMachine;
import it.polimi.it.galaxytrucker.model.adventurecards.cardstates.EndState;

public class AttackState extends State {

    @Override
    public void enter(StateMachine fsm) {
        CombatZone card = (CombatZone) fsm;

        card.findPlayerWithLeastFirePower();
    }

    @Override
    public void update(StateMachine fsm) {
        CombatZone card = (CombatZone) fsm;

        card.attack();

        changeState(fsm, new EndState());
    }

    @Override
    public void exit(StateMachine fsm) {
        
    }
    
}
