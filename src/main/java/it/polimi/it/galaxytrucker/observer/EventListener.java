package it.polimi.it.galaxytrucker.observer;

import jdk.jfr.Event;

public interface EventListener {
    public void notify(Event event);
}
