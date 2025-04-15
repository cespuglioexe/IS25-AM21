package it.polimi.it.galaxytrucker.view.statePattern.viewstates;

import it.polimi.it.galaxytrucker.networking.messages.RequestType;
import it.polimi.it.galaxytrucker.networking.messages.UserInput;
import it.polimi.it.galaxytrucker.networking.messages.UserInputType;
import it.polimi.it.galaxytrucker.view.CLIView;
import it.polimi.it.galaxytrucker.view.ConsoleColors;
import it.polimi.it.galaxytrucker.view.statePattern.State;
import it.polimi.it.galaxytrucker.view.statePattern.StateMachine;

public class BuildingStateMenu extends State {

    public BuildingStateMenu(CLIView view) {
        super(view);
    }

    @Override
    public void enter(StateMachine fsm) {
        System.out.println("""
                Choose an option: \n
                [1]: Pick a new tile \n
                [2]: Look a pile of cards
                """);
        int option = scanner.nextInt();
        switch (option){
            case 1:
                view.getClient().receiveUserInput(
                        new UserInput.UserInputBuilder(view.getClient(), UserInputType.REQUEST)
                                .setRequestType(RequestType.NEW_TILE)
                                .build()
                );
                fsm.changeState(new PlaceTile(view));
                break;
            case 2:
                view.getClient().receiveUserInput(
                        new UserInput.UserInputBuilder(view.getClient(), UserInputType.REQUEST)
                                .setRequestType(RequestType.CARD_PILE)
                                .build()
                );
                System.out.println("");
                break;
            default:
                System.out.println(ConsoleColors.RED_BACKGROUND_BRIGHT + "Oops! Something went wrong!" + ConsoleColors.RESET);
            }
    }

    @Override
    public void update(StateMachine fsm, boolean repeat) {

    }

    @Override
    public void exit(StateMachine fsm) {

    }
}
