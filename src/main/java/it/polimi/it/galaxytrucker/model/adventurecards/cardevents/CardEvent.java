package it.polimi.it.galaxytrucker.model.adventurecards.cardevents;

import it.polimi.it.galaxytrucker.model.adventurecards.interfaces.AdventureCard;

public interface CardEvent {
    public AdventureCard getSource();
    public void accept(EventVisitor visitor);
}
