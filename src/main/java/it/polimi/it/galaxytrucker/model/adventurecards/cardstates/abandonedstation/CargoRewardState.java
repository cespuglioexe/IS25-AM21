package it.polimi.it.galaxytrucker.model.adventurecards.cardstates.abandonedstation;

import it.polimi.it.galaxytrucker.model.adventurecards.cards.AbandonedStation;

import it.polimi.it.galaxytrucker.model.design.statePattern.State;
import it.polimi.it.galaxytrucker.model.design.statePattern.StateMachine;

public class CargoRewardState extends State {
    int numberCargoDecision = 0;

    @Override
    public void enter(StateMachine fsm) {

    }

    @Override
    public void update(StateMachine fsm) {
        AbandonedStation card = (AbandonedStation) fsm;
        int numberOfTotDecision = card.getCargoReward().size();
        numberCargoDecision ++;
        if(allCargoDecision(numberOfTotDecision)) {
            changeState(fsm, new FlightDayPenaltyState());
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