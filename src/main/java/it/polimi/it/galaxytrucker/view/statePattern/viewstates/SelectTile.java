package it.polimi.it.galaxytrucker.view.statePattern.viewstates;

import it.polimi.it.galaxytrucker.networking.messages.RequestType;
import it.polimi.it.galaxytrucker.networking.messages.UserInput;
import it.polimi.it.galaxytrucker.networking.messages.UserInputType;
import it.polimi.it.galaxytrucker.view.CLIView;
import it.polimi.it.galaxytrucker.view.statePattern.State;
import it.polimi.it.galaxytrucker.view.statePattern.StateMachine;

public class SelectTile extends State {

    public SelectTile(CLIView view) {
        super(view);
    }

    @Override
    public void enter(StateMachine fsm) {

    }

    @Override
    public void update(StateMachine fsm, boolean repeat) {

    }

    @Override
    public void exit(StateMachine fsm) {

    }
}
