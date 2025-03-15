package it.polimi.it.galaxytrucker.gameStates.prova;

import it.polimi.it.galaxytrucker.gameStates.prova.states.*;

public class Controller implements EventListener {
    View view;
    GameManager manager;

    public Controller() {
        this.view = new View(this);
        this.manager = new GameManager();
        this.manager.addObserver(this);
        this.manager.start();
    }

    @Override
    public void onGameEvent(Events e) {
        switch(e) {
            case REQUESTGAMESPECIFICS:
                view.askForLevel();
                break;
            case RESPONSELEVEL:
                view.askForNumberOfPlayers();
                manager.nextState(new Connection(manager));
                break;
            case REQUESTNEWPLAYER:
                view.askForName();
                if (manager.getConnectedPlayers().size() == manager.getNumberOfPlayers()) {
                    manager.nextState(new Building(manager));
                    System.out.println("Pronti per buildare");
                } else {
                    manager.getCurrentState().update();
                }
        }
    }

    public void setLevel(int level) {
        manager.setLevel(level);
    }

    public void setNumberOfPlayers(int numberOfPlayers) {
        manager.setNumberOfPlayers(numberOfPlayers);
    }

    public void addNewPlayer(String name) {
        manager.addNewPlayer(name);
    }
}
