package it.polimi.it.galaxytrucker.model.adventurecards.cardstates.pirates;

import it.polimi.it.galaxytrucker.model.adventurecards.cards.Pirates;
import it.polimi.it.galaxytrucker.model.design.statePattern.State;
import it.polimi.it.galaxytrucker.model.design.statePattern.StateMachine;
import it.polimi.it.galaxytrucker.model.managers.Player;

import java.util.Optional;

public class UserSelectionState extends State {


    @Override
    public void enter(StateMachine fsm) {
        Pirates card =(Pirates) fsm;

        Optional<Player> player = card.nextPlayer();

        if (player.isEmpty()) {
            fsm.changeState(new EvaluatePlayerState() );
        } else {
            fsm.changeState(new CalculateFirePowerState());
        }
    }

    @Override
    public void update(StateMachine fsm) {

    }

    @Override
    public void exit(StateMachine fsm) {

    }
}
