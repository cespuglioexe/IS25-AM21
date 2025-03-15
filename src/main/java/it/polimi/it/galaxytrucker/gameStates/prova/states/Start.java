package it.polimi.it.galaxytrucker.gameStates.prova.states;

import it.polimi.it.galaxytrucker.gameStates.prova.Events;
import it.polimi.it.galaxytrucker.gameStates.prova.GameManager;

public class Start implements State {
    GameManager manager;

    public Start(GameManager manager) {
        this.manager = manager;
    }

    @Override
    public void enter() {
        manager.notify(Events.REQUESTGAMESPECIFICS);
    }

    @Override
    public void update() {

    }

    @Override
    public void exit() {
        
    }
}
