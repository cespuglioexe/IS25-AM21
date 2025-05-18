package it.polimi.it.galaxytrucker.model.adventurecards.cardstates.pirates;

import it.polimi.it.galaxytrucker.model.adventurecards.cards.Pirates;
import it.polimi.it.galaxytrucker.model.design.statePattern.State;
import it.polimi.it.galaxytrucker.model.design.statePattern.StateMachine;
import it.polimi.it.galaxytrucker.model.managers.Player;
import it.polimi.it.galaxytrucker.model.adventurecards.cardstates.EndState;

import java.util.NoSuchElementException;

public class EvaluatePlayerState extends State {

    @Override
    public void enter(StateMachine fsm) {
        Pirates card = (Pirates) fsm;
        try{
            Player player = card.getPlayersAndFirePower().keySet().stream().toList().getFirst();
            double firePower = card.getPlayersAndFirePower().get(player);
            card.setPlayer(player);
            card.getPlayersAndFirePower().remove(player);
            if(firePower > card.getFirePowerRequired())
                fsm.changeState(new CreditRewardState());
            else{
                if(firePower < card.getFirePowerRequired())
                    fsm.changeState(new ActivateShieldState());
                else fsm.changeState(new EvaluatePlayerState());
            }
        }catch(NoSuchElementException e){
            fsm.changeState(new EndState());
        }
    }

    @Override
    public void update(StateMachine fsm) {

    }

    @Override
    public void exit(StateMachine fsm) {

    }
}
