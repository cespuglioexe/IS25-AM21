package it.polimi.it.galaxytrucker.model.adventurecards.cardevents;

import it.polimi.it.galaxytrucker.model.adventurecards.interfaces.AdventureCard;

public class CardResolved implements CardEvent {
    private AdventureCard source;

    public CardResolved(AdventureCard source) {
        this.source = source;
    }

    @Override
    public AdventureCard getSource() {
        return source;
    }
}
