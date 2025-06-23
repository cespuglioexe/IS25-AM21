package it.polimi.it.galaxytrucker.model.adventurecards.cardevents;

import it.polimi.it.galaxytrucker.model.adventurecards.interfaces.AdventureCard;
import it.polimi.it.galaxytrucker.model.managers.Player;

public class CardResolved implements CardEvent {
    private AdventureCard source;

    public CardResolved(AdventureCard source) {
        this.source = source;
    }

    @Override
    public AdventureCard getSource() {
        return source;
    }

    @Override
    public Player getInterestedPlayer() {
        return null;
    }

    @Override
    public void accept(EventVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public Player getInterestedPlayer() {
        return null;
    }
}
