package it.polimi.it.galaxytrucker.model.adventurecards.cardstates.meteorSwarm;

import java.util.Optional;

import it.polimi.it.galaxytrucker.model.adventurecards.refactored.MeteorSwarm;
import it.polimi.it.galaxytrucker.model.design.statePattern.State;
import it.polimi.it.galaxytrucker.model.design.statePattern.StateMachine;
import it.polimi.it.galaxytrucker.model.managers.Player;

public class UserSelectionState extends State {

    @Override
    public void enter(StateMachine fsm) {
        MeteorSwarm card =(MeteorSwarm) fsm;

        Optional<Player> player = card.nextPlayer();

        if (player.isEmpty()) {
            fsm.changeState(new EndState());
        } else {
            fsm.changeState(new EvaluateMeteorState());
        }
    }

    @Override
    public void update(StateMachine fsm) {
        
    }

    @Override
    public void exit(StateMachine fsm) {

    }
    
}
