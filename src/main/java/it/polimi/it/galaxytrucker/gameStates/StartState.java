package it.polimi.it.galaxytrucker.gameStates;

import it.polimi.it.galaxytrucker.design.statePattern.*;
import it.polimi.it.galaxytrucker.exceptions.InvalidActionException;
import it.polimi.it.galaxytrucker.gameStates.fields.StartFields;
import it.polimi.it.galaxytrucker.managers.GameManager;
import it.polimi.it.galaxytrucker.managers.Model;

public class StartState extends State {
    @Override
    public void enter(StateMachine fsm) {

    }

    @Override
    public void update(StateMachine fsm) throws InvalidActionException {
        Model model = (Model) fsm;

        if (areAllFieldsSet(model)) {
            changeState(fsm, new ConnectionState());
        }
    }

    private boolean areAllFieldsSet(Model model) throws InvalidActionException {
        for (StartFields field : StartFields.values()) {
            if (!field.isSet(model)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void exit(StateMachine fsm) {
        GameManager gameManager = (GameManager) fsm;

        gameManager.initializeGameSpecifics();
    }
}
