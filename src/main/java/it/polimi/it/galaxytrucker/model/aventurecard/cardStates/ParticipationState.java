package it.polimi.it.galaxytrucker.model.aventurecard.cardStates;

import it.polimi.it.galaxytrucker.model.aventurecard.AdventureCard;
import it.polimi.it.galaxytrucker.model.aventurecard.cardStates.fields.ParticipationRequirements;
import it.polimi.it.galaxytrucker.model.design.statePattern.State;
import it.polimi.it.galaxytrucker.model.design.statePattern.StateMachine;

public class ParticipationState extends State {
    int decisions = 0;

    @Override
    public void enter(StateMachine fsm) {

    }

    @Override
    public void update(StateMachine fsm) {
        AdventureCard<?> card = (AdventureCard<?>) fsm;
        decisions++;

        if (areAllFieldsSet(card) || decisions == card.getPlayersInOrder().size()) {
            changeState(fsm, new EffectExecutionState());
        }
    }

    private boolean areAllFieldsSet(AdventureCard<?> card) {
        for (ParticipationRequirements field : ParticipationRequirements.values()) {
            if (!field.isSet(card)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void exit(StateMachine fsm) {

    }
}
