package it.polimi.it.galaxytrucker.model.gameStates;

import it.polimi.it.galaxytrucker.model.design.statePattern.State;
import it.polimi.it.galaxytrucker.model.design.statePattern.StateMachine;
import it.polimi.it.galaxytrucker.model.gameStates.fields.ConnectionFields;
import it.polimi.it.galaxytrucker.model.managers.GameManager;
import it.polimi.it.galaxytrucker.model.managers.Model;

public class ConnectionState extends State {
    @Override
    public void enter(StateMachine fsm) {
        GameManager gameManager = (GameManager) fsm;

        System.out.println("Entered connection state");
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
        System.out.println("Leaving connection state");
    }
}
