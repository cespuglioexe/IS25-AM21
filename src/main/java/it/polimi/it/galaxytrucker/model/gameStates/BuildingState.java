package it.polimi.it.galaxytrucker.model.gameStates;

import it.polimi.it.galaxytrucker.model.design.statePattern.State;
import it.polimi.it.galaxytrucker.model.design.statePattern.StateMachine;
import it.polimi.it.galaxytrucker.model.exceptions.InvalidActionException;
import it.polimi.it.galaxytrucker.model.managers.GameManager;
import it.polimi.it.galaxytrucker.networking.messages.GameUpdate;
import it.polimi.it.galaxytrucker.networking.messages.GameUpdateType;

public class BuildingState extends State {
    @Override
    public void enter(StateMachine fsm) {
        System.out.println( "BuildingState");

        GameManager gameManager = (GameManager) fsm;
        gameManager.initializeComponentTiles();

        gameManager.sendGameUpdateToServer(new GameUpdate.GameUpdateBuilder(GameUpdateType.NEW_STATE).setNewSate("Building").build());
        gameManager.sendGameUpdateToServer(
                new GameUpdate.GameUpdateBuilder(GameUpdateType.DRAWN_TILE)
                        .setNewTile(gameManager.drawComponentTile())
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