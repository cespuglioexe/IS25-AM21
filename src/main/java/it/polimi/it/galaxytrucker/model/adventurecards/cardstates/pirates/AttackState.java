package it.polimi.it.galaxytrucker.model.adventurecards.cardstates.pirates;

import it.polimi.it.galaxytrucker.model.adventurecards.cards.Pirates;
import it.polimi.it.galaxytrucker.model.adventurecards.interfaces.attack.Projectile;
import it.polimi.it.galaxytrucker.model.design.statePattern.State;
import it.polimi.it.galaxytrucker.model.design.statePattern.StateMachine;

public class AttackState extends State {

    @Override
    public void enter(StateMachine fsm) {
        Pirates card = (Pirates) fsm;
        for (Projectile projectile : card.getProjectiles()) {
            card.aimAtCoordsWith(projectile);

        }
        card.attack();
        changeState(fsm, new EvaluatePlayerState());
    }

    @Override
    public void update(StateMachine fsm) {

    }

    @Override
    public void exit(StateMachine fsm) {

    }
}
