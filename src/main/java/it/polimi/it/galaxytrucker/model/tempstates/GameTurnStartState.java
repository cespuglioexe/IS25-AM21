package it.polimi.it.galaxytrucker.model.tempstates;

import it.polimi.it.galaxytrucker.model.adventurecards.interfaces.AdventureCard;
import it.polimi.it.galaxytrucker.model.design.statePattern.StateMachine;
import it.polimi.it.galaxytrucker.model.managers.GameManager;

import java.util.NoSuchElementException;

public class GameTurnStartState extends GameState {
    @Override
    public void enter(StateMachine fsm) {
        GameManager game = (GameManager) fsm;

        try {
            AdventureCard card = game.getAdventureDeck().draw();
            fsm.changeState(new CardExecutionState(card));
        }
        catch (NoSuchElementException e) {
            fsm.changeState(new GameEndState());
        }

    }

    @Override
    public void update(StateMachine fsm) {

    }

    @Override
    public void exit(StateMachine fsm) {

    }
}
