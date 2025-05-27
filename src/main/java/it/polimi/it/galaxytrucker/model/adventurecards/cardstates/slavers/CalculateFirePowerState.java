package it.polimi.it.galaxytrucker.model.adventurecards.cardstates.slavers;

import it.polimi.it.galaxytrucker.model.adventurecards.cardevents.InputNeeded;
import it.polimi.it.galaxytrucker.model.adventurecards.cards.Slavers;
import it.polimi.it.galaxytrucker.model.design.observerPattern.Subject;
import it.polimi.it.galaxytrucker.model.design.statePattern.State;
import it.polimi.it.galaxytrucker.model.design.statePattern.StateMachine;

public class CalculateFirePowerState extends State {

    @Override
    public void enter(StateMachine fsm) {
        Slavers card = (Slavers) fsm;
        Subject subject = (Subject) fsm;
        card.setPlayerFirePower(card.getCurrentPlayer().getShipManager().calculateFirePower());
        subject.notifyObservers(new InputNeeded(card));
    }

    @Override
    public void update(StateMachine fsm) {
        Slavers card = (Slavers) fsm;
        if(card.getPlayerFirePower() > card.getRequiredFirePower())
            fsm.changeState(new CreditRewardState());
        else{
            if(card.getPlayerFirePower() < card.getRequiredFirePower())
                fsm.changeState(new CrewmatePenaltyState());
            else fsm.changeState(new StartState());
        }
    }

    @Override
    public void exit(StateMachine fsm) {

    }
}