package it.polimi.it.galaxytrucker.gameStates;

import it.polimi.it.galaxytrucker.design.statePattern.State;
import it.polimi.it.galaxytrucker.design.statePattern.StateMachine;
import it.polimi.it.galaxytrucker.gameStates.fields.ConnectionFields;
import it.polimi.it.galaxytrucker.managers.GameManager;
import it.polimi.it.galaxytrucker.managers.Model;

public class ConnectionState extends State {
    @Override
    public void enter(StateMachine fsm) {
        GameManager gameManager = (GameManager) fsm;

        gameManager.initializeGameSpecifics();
    }

    @Override
    public void update(StateMachine fsm) {
        Model model = (Model) fsm;

        if (areAllFieldsSet(model)) {
            changeState(fsm, new BuildingState());
        }
    }

    private boolean areAllFieldsSet(Model model) {
        for (ConnectionFields field : ConnectionFields.values()) {
            if (!field.isSet(model)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void exit(StateMachine fsm) {

    }
}
