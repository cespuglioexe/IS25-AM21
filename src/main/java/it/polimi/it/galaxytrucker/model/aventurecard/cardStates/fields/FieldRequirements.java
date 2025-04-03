package it.polimi.it.galaxytrucker.model.aventurecard.cardStates.fields;

import it.polimi.it.galaxytrucker.model.aventurecard.AdventureCard;

public interface FieldRequirements {
    public boolean isSet(AdventureCard<?> card);
}
