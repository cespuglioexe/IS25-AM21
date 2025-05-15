package it.polimi.it.galaxytrucker.model.adventurecards.cardstates.meteorSwarm;

import it.polimi.it.galaxytrucker.model.adventurecards.refactored.MeteorSwarm;
import it.polimi.it.galaxytrucker.model.design.statePattern.State;
import it.polimi.it.galaxytrucker.model.design.statePattern.StateMachine;

public class AttackState extends State {

    @Override
    public void enter(StateMachine fsm) {
        MeteorSwarm card =(MeteorSwarm) fsm;
        card.attack();

        fsm.changeState(new EvaluateMeteorState());
    }

    @Override
    public void update(StateMachine fsm) {
        
    }

    @Override
    public void exit(StateMachine fsm) {

    }
}
