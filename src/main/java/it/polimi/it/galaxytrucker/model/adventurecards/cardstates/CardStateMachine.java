package it.polimi.it.galaxytrucker.model.adventurecards.cardstates;

import it.polimi.it.galaxytrucker.model.design.observerPattern.Observer;
import it.polimi.it.galaxytrucker.model.design.observerPattern.Subject;
import it.polimi.it.galaxytrucker.model.design.statePattern.State;
import it.polimi.it.galaxytrucker.model.design.statePattern.StateMachine;

import java.util.ArrayList;
import java.util.List;

public class CardStateMachine extends StateMachine implements Subject {
    private final List<Observer> observers = new ArrayList<>();

    @Override
    public void changeState(State nextState) {
        State currentState = getCurrentState();

        currentState.exit(this);
        currentState = nextState;
        currentState.enter(this);

        notifyObservers();
    }

    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        for(Observer observer : observers) {
            observer.notify(getCurrentState().getClass().getSimpleName());
        }
    }
}
