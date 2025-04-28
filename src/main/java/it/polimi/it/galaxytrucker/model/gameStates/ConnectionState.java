package it.polimi.it.galaxytrucker.model.gameStates;

import it.polimi.it.galaxytrucker.model.design.statePattern.State;
import it.polimi.it.galaxytrucker.model.design.statePattern.StateMachine;
import it.polimi.it.galaxytrucker.model.gameStates.fields.ConnectionFields;
import it.polimi.it.galaxytrucker.model.managers.GameManager;
import it.polimi.it.galaxytrucker.model.managers.Model;
import it.polimi.it.galaxytrucker.networking.messages.GameUpdate;
import it.polimi.it.galaxytrucker.networking.messages.GameUpdateType;
import it.polimi.it.galaxytrucker.view.ConsoleColors;

public class ConnectionState extends State {
    @Override
    public void enter(StateMachine fsm) {
        System.out.println(ConsoleColors.BLUE_UNDERLINED + "\n> " + this.getClass().getSimpleName() + " <\n" + ConsoleColors.RESET);
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
