package it.polimi.it.galaxytrucker.networking.view.statePattern.viewstates;

import it.polimi.it.galaxytrucker.networking.view.listeners.DoubleEventListener;
import it.polimi.it.galaxytrucker.networking.view.listeners.EventListener;
import it.polimi.it.galaxytrucker.networking.view.listeners.EventType;
import it.polimi.it.galaxytrucker.networking.view.statePattern.State;
import it.polimi.it.galaxytrucker.networking.view.statePattern.StateMachine;

public class GameSelection extends State {

    public GameSelection(EventListener listener) {
        super(listener);
    }

    @Override
    public void enter(StateMachine fsm) {
        System.out.print("Enter which game you'd like to join\n> ");
        int gameNum = scanner.nextInt();
        ((DoubleEventListener)listener).onDoubleEvent(EventType.GAME_SELECTION, gameNum, 0);
    }

    @Override
    public void update(StateMachine fsm, boolean repeat) {

    }

    @Override
    public void exit(StateMachine fsm) {

    }
}
