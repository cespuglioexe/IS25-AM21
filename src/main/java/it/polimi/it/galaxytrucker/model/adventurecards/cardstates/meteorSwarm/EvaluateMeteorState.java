package it.polimi.it.galaxytrucker.model.adventurecards.cardstates.meteorSwarm;

import it.polimi.it.galaxytrucker.model.adventurecards.refactored.MeteorSwarm;
import it.polimi.it.galaxytrucker.model.design.statePattern.State;
import it.polimi.it.galaxytrucker.model.design.statePattern.StateMachine;
import it.polimi.it.galaxytrucker.model.utility.ProjectileType;

public class EvaluateMeteorState extends State {
    @Override
    public void enter(StateMachine fsm)     {
        MeteorSwarm card = (MeteorSwarm) fsm;
        card.selectMeteor();
        if(card.getCurrentMeteor() == null)
            fsm.changeState(new StartState());
        else{
            if(card.getCurrentMeteor().getSize() == ProjectileType.BIG) {
                fsm.changeState(new BigMeteorState());
            }else  fsm.changeState(new SmallMeteorState());
        }
    }

    @Override
    public void update(StateMachine fsm) {

    }

    @Override
    public void exit(StateMachine fsm) {

    }
}
