package it.polimi.it.galaxytrucker.model.gameStates;

import it.polimi.it.galaxytrucker.model.design.statePattern.State;
import it.polimi.it.galaxytrucker.model.design.statePattern.StateMachine;
import it.polimi.it.galaxytrucker.model.exceptions.InvalidActionException;
import it.polimi.it.galaxytrucker.model.gameStates.fields.StartFields;
import it.polimi.it.galaxytrucker.model.managers.Model;
import it.polimi.it.galaxytrucker.view.ConsoleColors;

public class StartState extends State {
    @Override
    public void enter(StateMachine fsm) {
        System.out.println(ConsoleColors.BLUE_UNDERLINED + "\n> " + this.getClass().getSimpleName() + " <\n" + ConsoleColors.RESET);

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
