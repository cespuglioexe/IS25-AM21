package it.polimi.it.galaxytrucker.model.adventurecards.cardstates.smugglers;

import it.polimi.it.galaxytrucker.model.adventurecards.cardstates.smugglers.EndState;
import it.polimi.it.galaxytrucker.model.adventurecards.cardstates.smugglers.FlightDayPenaltyState;
import it.polimi.it.galaxytrucker.model.adventurecards.refactored.AbandonedStation;
import it.polimi.it.galaxytrucker.model.adventurecards.refactored.Smugglers;
import it.polimi.it.galaxytrucker.model.design.statePattern.State;
import it.polimi.it.galaxytrucker.model.design.statePattern.StateMachine;

public class CargoRewardState extends State {
    int numberCargoDecision = 0;

    @Override
    public void enter(StateMachine fsm) {

    }

    @Override
    public void update(StateMachine fsm) {
        Smugglers card = (Smugglers) fsm;
        int numberOfTotDecision = card.getCargoReward().size();
        numberCargoDecision ++;
        if(allCargoDecision(numberOfTotDecision)) {
            if(card.getCargoReward().isEmpty())
                changeState(fsm, new FlightDayPenaltyState());
            else changeState(fsm, new EndState());
        }
    }

    private boolean allCargoDecision(int numberOfDecision){
        if(numberCargoDecision == numberOfDecision)
            return true;
        return false;
    }

    @Override
    public void exit(StateMachine fsm) {

    }

}
