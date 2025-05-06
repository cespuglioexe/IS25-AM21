package it.polimi.it.galaxytrucker.model.gameStates;

import it.polimi.it.galaxytrucker.model.design.statePattern.State;
import it.polimi.it.galaxytrucker.model.design.statePattern.StateMachine;
import it.polimi.it.galaxytrucker.model.exceptions.InvalidActionException;
import it.polimi.it.galaxytrucker.model.managers.GameManager;
import it.polimi.it.galaxytrucker.commands.servercommands.GameUpdate;
import it.polimi.it.galaxytrucker.commands.servercommands.GameUpdateType;
import it.polimi.it.galaxytrucker.view.cli.ConsoleColors;

public class BuildingState extends State {
    @Override
    public void enter(StateMachine fsm) {
        System.out.println(ConsoleColors.BLUE_UNDERLINED + "\n> " + this.getClass().getSimpleName() + " <\n" + ConsoleColors.RESET);

        GameManager gameManager = (GameManager) fsm;
        gameManager.initializeComponentTiles();

        gameManager.sendGameUpdateToAllPlayers(
                new GameUpdate.GameUpdateBuilder(GameUpdateType.NEW_STATE)
                        .setNewSate("BUILDING")
                        .build()
        );
    }

    @Override
    public void update(StateMachine fsm) throws InvalidActionException {
        
    }

    @Override
    public void exit(StateMachine fsm) {

    }
}