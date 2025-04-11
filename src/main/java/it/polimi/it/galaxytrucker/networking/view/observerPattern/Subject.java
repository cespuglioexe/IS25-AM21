package it.polimi.it.galaxytrucker.networking.view.observerPattern;

public interface Subject {
    public void addObserver(Observer observer);

    public void removeObserver(Observer observer);

    public void notifyObserver();
}
