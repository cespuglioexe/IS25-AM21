package it.polimi.it.galaxytrucker.model.adventurecards.cardstates.smugglers;

import it.polimi.it.galaxytrucker.model.adventurecards.cardstates.smugglers.CargoRewardState;
import it.polimi.it.galaxytrucker.model.adventurecards.refactored.Smugglers;
import it.polimi.it.galaxytrucker.model.design.statePattern.State;
import it.polimi.it.galaxytrucker.model.design.statePattern.StateMachine;

public class CalculateFirePowerState extends State {

    @Override
    public void enter(StateMachine fsm) {
        Smugglers card = (Smugglers) fsm;
        card.setPlayerFirePower(card.getCurrentPlayer().getShipManager().calculateFirePower());
    }

    @Override
    public void update(StateMachine fsm) {
        Smugglers card = (Smugglers) fsm;
        if(card.getPlayerFirePower() > card.getRequiredFirePower())
            fsm.changeState(new CargoRewardState());
        else {
            if (card.getPlayerFirePower() < card.getRequiredFirePower()){
                fsm.changeState(new CargoPenaltyState());
            } else{
                fsm.changeState(new StartState());
            }
        }
    }

    @Override
    public void exit(StateMachine fsm) {

    }
}
