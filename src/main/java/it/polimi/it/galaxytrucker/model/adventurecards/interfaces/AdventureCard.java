package it.polimi.it.galaxytrucker.model.adventurecards.interfaces;

import java.util.HashMap;

public interface AdventureCard {
    public abstract void play();
    public abstract String toString();
    public String getGraphicPath();
    public void accept(AdventureCardVisitor visitor, AdventureCardInputContext context);
    public HashMap<String, Object> getEventData();
} 
