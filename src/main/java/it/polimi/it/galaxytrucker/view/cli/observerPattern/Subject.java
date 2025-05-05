package it.polimi.it.galaxytrucker.view.cli.observerPattern;

public interface Subject {
    public void addObserver(Observer observer);

    public void removeObserver(Observer observer);

    public void notifyObserver();
}
