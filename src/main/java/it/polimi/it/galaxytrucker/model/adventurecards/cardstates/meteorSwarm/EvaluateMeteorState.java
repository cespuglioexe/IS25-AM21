package it.polimi.it.galaxytrucker.model.adventurecards.cardstates.meteorswarm;

import java.util.Optional;

import it.polimi.it.galaxytrucker.model.adventurecards.cards.MeteorSwarm;
import it.polimi.it.galaxytrucker.model.adventurecards.interfaces.attack.Projectile;
import it.polimi.it.galaxytrucker.model.design.statePattern.State;
import it.polimi.it.galaxytrucker.model.design.statePattern.StateMachine;
import it.polimi.it.galaxytrucker.model.utility.ProjectileType;

public class EvaluateMeteorState extends State {
    @Override
    public void enter(StateMachine fsm)     {
        MeteorSwarm card = (MeteorSwarm) fsm;

        Optional<Projectile> meteor = card.nextMeteor();

        if (meteor.isEmpty()) {
            fsm.changeState(new UserSelectionState());
            return;
        }

        if (meteor.get().getSize() == ProjectileType.BIG) {
            fsm.changeState(new BigMeteorState());
        } else {
            fsm.changeState(new SmallMeteorState());
        }
    }

    @Override
    public void update(StateMachine fsm) {

    }

    @Override
    public void exit(StateMachine fsm) {

    }
}
