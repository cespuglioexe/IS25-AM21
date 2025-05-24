package it.polimi.it.galaxytrucker.model.adventurecards.cardevents;

import it.polimi.it.galaxytrucker.model.adventurecards.interfaces.AdventureCard;

public class InputNeeded implements CardEvent {
    private AdventureCard source;

    public InputNeeded(AdventureCard source) {
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
}
