package it.polimi.it.galaxytrucker.model.adventurecards.cardstates.smugglers;

import it.polimi.it.galaxytrucker.model.adventurecards.cardevents.InputNeeded;
import it.polimi.it.galaxytrucker.model.adventurecards.cards.Smugglers;
import it.polimi.it.galaxytrucker.model.design.observerPattern.Subject;
import it.polimi.it.galaxytrucker.model.design.statePattern.State;
import it.polimi.it.galaxytrucker.model.design.statePattern.StateMachine;
import it.polimi.it.galaxytrucker.model.adventurecards.cardstates.EndState;

public class CargoRewardState extends State {
    int numberCargoDecision = 0;

    @Override
    public void enter(StateMachine fsm) {
        Smugglers card = (Smugglers) fsm;
        Subject subject = (Subject) fsm;
        subject.notifyObservers(new InputNeeded(card));
    }

    @Override
    public void update(StateMachine fsm) {
        Smugglers card = (Smugglers) fsm;
        Subject subject = (Subject) fsm;
        int numberOfTotDecision = card.getCargoReward().size();
        numberCargoDecision ++;
        if(allCargoDecision(numberOfTotDecision)) {
            if(card.getCargoReward().isEmpty())
                changeState(fsm, new FlightDayPenaltyState());
            else changeState(fsm, new EndState());
        }
        subject.notifyObservers(new InputNeeded(card));
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
