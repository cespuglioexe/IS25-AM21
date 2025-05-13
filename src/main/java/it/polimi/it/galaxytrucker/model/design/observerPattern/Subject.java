package it.polimi.it.galaxytrucker.model.design.observerPattern;

public interface Subject {
    public void addObserver(Observer observer);

    public void removeObserver(Observer observer);

    public void notifyObservers();
}
