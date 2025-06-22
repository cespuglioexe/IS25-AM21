package it.polimi.it.galaxytrucker.model.adventurecards.interfaces;

public interface AdventureCard {
    public abstract void play();
    public abstract String toString();
    public String getGraphicPath();
    public void accept(AdventureCardVisitor visitor, AdventureCardInputContext context);
} 
