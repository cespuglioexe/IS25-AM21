package it.polimi.it.galaxytrucker.networking.view.statePattern.viewstates;

import it.polimi.it.galaxytrucker.networking.view.listeners.DoubleEventListener;
import it.polimi.it.galaxytrucker.networking.view.listeners.EventListener;
import it.polimi.it.galaxytrucker.networking.view.listeners.EventType;
import it.polimi.it.galaxytrucker.networking.view.statePattern.State;
import it.polimi.it.galaxytrucker.networking.view.statePattern.StateMachine;

public class GameCreation extends State {

    public GameCreation(EventListener listener) {
        super(listener);
    }

    @Override
    public void enter(StateMachine fsm) {
        System.out.print("How many players do you want in the game? (2, 3, 4)\n> ");
        int playerNum = scanner.nextInt();
        System.out.print("What level game do you want? (1, 2, 3)\n> ");
        int level = scanner.nextInt();
        ((DoubleEventListener)listener).onDoubleEvent(EventType.GAME_CREATION, playerNum, level);
    }

    @Override
    public void update(StateMachine fsm, boolean repeat) {
        if (!repeat) {
            fsm.changeState(new GameSelection(listener));
        }
    }

    @Override
    public void exit(StateMachine fsm) {

    }
}
