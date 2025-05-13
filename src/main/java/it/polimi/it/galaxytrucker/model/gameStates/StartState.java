package it.polimi.it.galaxytrucker.model.gameStates;

import it.polimi.it.galaxytrucker.model.design.statePattern.StateMachine;
import it.polimi.it.galaxytrucker.exceptions.InvalidActionException;
import it.polimi.it.galaxytrucker.model.gameStates.fields.StartFields;
import it.polimi.it.galaxytrucker.model.managers.Model;

public class StartState extends GameState {
    @Override
    public void enter(StateMachine fsm) {
        Model model = (Model) fsm;

        if (areAllFieldsSet(model)) {
            changeState(fsm, new ConnectionState());
        }
    }

    @Override
    public void update(StateMachine fsm) throws InvalidActionException {
        Model model = (Model) fsm;

        if (areAllFieldsSet(model)) {
            changeState(fsm, new ConnectionState());
        }
    }

    private boolean areAllFieldsSet(Model model) throws InvalidActionException {
        boolean allSet = true;

        for (StartFields field : StartFields.values()) {
            if (!field.isSet(model)) {
                allSet = false;
            }
        }
        return allSet;
    }

    @Override
    public void exit(StateMachine fsm) {

    }
}
