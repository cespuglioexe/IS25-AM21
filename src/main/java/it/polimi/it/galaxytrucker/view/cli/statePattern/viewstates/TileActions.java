package it.polimi.it.galaxytrucker.view.cli.statePattern.viewstates;

import it.polimi.it.galaxytrucker.networking.messages.UserInput;
import it.polimi.it.galaxytrucker.networking.messages.UserInputType;
import it.polimi.it.galaxytrucker.view.cli.CLIView;
import it.polimi.it.galaxytrucker.view.cli.statePattern.State;
import it.polimi.it.galaxytrucker.view.cli.statePattern.StateMachine;

public class TileActions extends State {

    public TileActions(CLIView view) {
        super(view);
    }

    @Override
    public void enter(StateMachine fsm) {

        System.out.print("""
                \nChoose an option:
                [1]: Place the tile
                [2]: Save the tile
                [3]: Discard the tile
                > """);

        int option = scanner.nextInt();

        switch (option) {
            case 1:
                System.out.println("\nWhere do you want to place the tile?");
                System.out.print("Column> ");
                int x = scanner.nextInt();
                System.out.print("Row> ");
                int y = scanner.nextInt();

                System.out.println("\nIn which direction do you want to rotate the tile?");
                System.out.println("[0]: ↑, [1]: →, [2]: ↓, [3]: ←");
                System.out.print("> ");

                int rotation = scanner.nextInt();

                view.getClient().receiveUserInput(
                        new UserInput.UserInputBuilder(view.getClient(), UserInputType.PLACE_COMPONENT)
                                .setCoords(x, y)
                                .setRotation(rotation)
                                .build()
                );
                break;
            case 2:
                view.getClient().receiveUserInput(
                        new UserInput.UserInputBuilder(view.getClient(), UserInputType.SAVE_COMPONENT)
                                .build()
                );
                break;
            case 3:

                view.getClient().receiveUserInput(
                        new UserInput.UserInputBuilder(view.getClient(), UserInputType.DISCARD_COMPONENT)
                                .build()
                );
                break;
        }

        fsm.changeState(new BuildingStateMenu(view));
    }

    @Override
    public void update(StateMachine fsm, boolean repeat) {

    }

    @Override
    public void exit(StateMachine fsm) {

    }
}
