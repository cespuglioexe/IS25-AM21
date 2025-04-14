package it.polimi.it.galaxytrucker.view.statePattern.viewstates;

import it.polimi.it.galaxytrucker.view.CLIView;
import it.polimi.it.galaxytrucker.view.statePattern.State;
import it.polimi.it.galaxytrucker.view.statePattern.StateMachine;

public class PlaceTile extends State {

    public PlaceTile(CLIView view) {
        super(view);
    }

    @Override
    public void enter(StateMachine fsm) {
        System.out.println("Where do you want to place the tile? (x y)");
        System.out.print("> ");
        scanner.nextInt();
    }

    @Override
    public void update(StateMachine fsm, boolean repeat) {

    }

    @Override
    public void exit(StateMachine fsm) {

    }
}
