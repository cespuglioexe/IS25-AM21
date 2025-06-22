package it.polimi.it.galaxytrucker.model.adventurecards.cardevents;

import it.polimi.it.galaxytrucker.model.adventurecards.interfaces.AdventureCard;
import it.polimi.it.galaxytrucker.model.managers.Player;

public interface CardEvent {
    public AdventureCard getSource();
    public Player getInterestedPlayer();
    public void accept(EventVisitor visitor);
}
