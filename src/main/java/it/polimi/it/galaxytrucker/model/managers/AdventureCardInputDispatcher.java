package it.polimi.it.galaxytrucker.model.managers;

import it.polimi.it.galaxytrucker.model.adventurecards.interfaces.AdventureCard;
import it.polimi.it.galaxytrucker.model.adventurecards.interfaces.AdventureCardInputContext;

public interface AdventureCardInputDispatcher {
    public void dispatch(AdventureCard card, AdventureCardInputContext context);
}
