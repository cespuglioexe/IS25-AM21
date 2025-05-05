package it.polimi.it.galaxytrucker.model.adventurecards.cardstates.pirates;

import it.polimi.it.galaxytrucker.model.adventurecards.refactored.Pirates;
import it.polimi.it.galaxytrucker.model.design.statePattern.State;
import it.polimi.it.galaxytrucker.model.design.statePattern.StateMachine;
import it.polimi.it.galaxytrucker.model.managers.Player;

public class EvaluatePlayerState extends State {

    @Override
    public void enter(StateMachine fsm) {
        Pirates card = (Pirates) fsm;
        Player player = card.getPlayersAndFirePower().keySet().stream().toList().getFirst();
        double firePower = card.getPlayersAndFirePower().get(player);

        if(player != null){
            card.getPlayersAndFirePower().remove(player);
            if(firePower > card.getFirePowerRequired())
                fsm.changeState(new CreditRewardState());
            else{
                if(firePower < card.getFirePowerRequired())
                    fsm.changeState(new AttackState());
                else fsm.changeState(new EvaluatePlayerState());
            }
        } else fsm.changeState(new EndState());
    }

    @Override
    public void update(StateMachine fsm) {

    }

    @Override
    public void exit(StateMachine fsm) {

    }
}
