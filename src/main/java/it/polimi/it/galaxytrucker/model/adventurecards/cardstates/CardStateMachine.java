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
        super.changeState(nextState);
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
    public void notifyObservers(Object event) {
        List<Observer> observersCopy = new ArrayList<>(observers);
        for (Observer observer : observersCopy) {
            observer.notify(event);
        }
    }
}
