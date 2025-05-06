package it.polimi.it.galaxytrucker.view.cli.statePattern.viewstates;

import it.polimi.it.galaxytrucker.commands.UserInput;
import it.polimi.it.galaxytrucker.commands.UserInputType;
import it.polimi.it.galaxytrucker.view.cli.CLIView;
import it.polimi.it.galaxytrucker.view.cli.statePattern.State;
import it.polimi.it.galaxytrucker.view.cli.statePattern.StateMachine;

public class ConnectionState extends State {

    public ConnectionState(CLIView view) {
        super(view);
    }

    @Override
    public void enter(StateMachine fsm) {
        System.out.print("Insert server name\n> ");
        String server = scanner.nextLine();
        view.getClient().receiveUserInput(
                new UserInput.UserInputBuilder(null, UserInputType.CONNECT_SERVER)
                        .setServerName(server)
                        .build());
    }

    @Override
    public void update(StateMachine fsm, boolean repeat) {
        if (repeat) {
            this.enter(fsm);
        }
        else {
            fsm.changeState(new NameInput(view));
        }
    }

    @Override
    public void exit(StateMachine fsm) {

    }
}
