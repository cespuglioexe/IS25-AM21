package it.polimi.it.galaxytrucker.model.adventurecards.cardstates.meteorSwarm;

import it.polimi.it.galaxytrucker.model.adventurecards.refactored.MeteorSwarm;
import it.polimi.it.galaxytrucker.model.design.statePattern.State;
import it.polimi.it.galaxytrucker.model.design.statePattern.StateMachine;

public class BigMeteorState extends State {

    @Override
    public void enter(StateMachine fsm) {

    }

    @Override
    public void update(StateMachine fsm) {
        MeteorSwarm card = (MeteorSwarm) fsm;
        if(card.getMeteorAttack()==null)
            fsm.changeState(new EvaluateMeteorState());
        else fsm.changeState(new AttackState());
    }

    @Override
    public void exit(StateMachine fsm) {

    }
}
