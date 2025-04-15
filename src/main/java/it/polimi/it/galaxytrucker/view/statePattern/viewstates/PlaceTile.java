package it.polimi.it.galaxytrucker.view.statePattern.viewstates;

import it.polimi.it.galaxytrucker.networking.messages.UserInput;
import it.polimi.it.galaxytrucker.networking.messages.UserInputType;
import it.polimi.it.galaxytrucker.view.CLIView;
import it.polimi.it.galaxytrucker.view.statePattern.State;
import it.polimi.it.galaxytrucker.view.statePattern.StateMachine;

import java.util.Scanner;

public class PlaceTile extends State {

    public PlaceTile(CLIView view) {
        super(view);
    }

    @Override
    public void enter(StateMachine fsm) {
        view.displayShip();

        System.out.println("Where do you want to place the tile? (x y)");
        String line = scanner.nextLine();
        
        Scanner linescanner = new Scanner(line);

        int x = linescanner.nextInt();
        int y = linescanner.nextInt();

        linescanner.close();

        view.getClient().receiveUserInput(
                new UserInput.UserInputBuilder(view.getClient(), UserInputType.PLACE_COMPONENT)
                        .setCoords(x, y)
                        .build()
        );
    }

    @Override
    public void update(StateMachine fsm, boolean repeat) {

    }

    @Override
    public void exit(StateMachine fsm) {

    }
}
