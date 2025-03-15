package it.polimi.it.galaxytrucker.gameStates.prova.states;

import it.polimi.it.galaxytrucker.gameStates.prova.Events;
import it.polimi.it.galaxytrucker.gameStates.prova.GameManager;

public class Connection implements State {
    GameManager manager;

    public Connection(GameManager manager) {
        this.manager = manager;
    }

    @Override
    public void enter() {
        manager.notify(Events.REQUESTNEWPLAYER);
    }

    @Override
    public void update() {
        manager.notify(Events.REQUESTNEWPLAYER);
    }

    @Override
    public void exit() {
        
    }
}
