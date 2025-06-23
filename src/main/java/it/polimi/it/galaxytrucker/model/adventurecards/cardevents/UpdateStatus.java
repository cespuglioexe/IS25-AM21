package it.polimi.it.galaxytrucker.model.adventurecards.cardevents;

import it.polimi.it.galaxytrucker.model.adventurecards.interfaces.AdventureCard;
import it.polimi.it.galaxytrucker.model.managers.Player;

public class UpdateStatus implements CardEvent {
    private AdventureCard source;

    public UpdateStatus(AdventureCard source) {
        this.source = source;
    }

    @Override
    public AdventureCard getSource() {
        return source;
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
