package it.polimi.it.galaxytrucker.observer;

import jdk.jfr.Event;

import java.util.ArrayList;
import java.util.List;

public class EventPublisher {
    private List<EventListener> listeners = new ArrayList<>();

    public void addListener(EventListener listener) {
        listeners.add(listener);
    }

    public void removeListener(EventListener listener) {
        listeners.remove(listener);
    }

    public void publish(Event event) {
        for (EventListener listener : listeners) {
            listener.notify(event);
        }
    }
}