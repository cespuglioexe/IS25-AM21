package it.polimi.it.galaxytrucker.model.adventurecards.cardevents;

import it.polimi.it.galaxytrucker.model.adventurecards.interfaces.AdventureCard;
import it.polimi.it.galaxytrucker.model.managers.Player;

public class InputNeeded implements CardEvent {
    private AdventureCard source;
    private Player interestedPlayer;

    public InputNeeded(AdventureCard source, Player player) {
        this.source = source;
        this.interestedPlayer = player;
    }

    @Override
    public AdventureCard getSource() {
        return source;
    }

    @Override
    public Player getInterestedPlayer() {
        return interestedPlayer;
    }
    
    @Override
    public void accept(EventVisitor visitor) {
        visitor.visit(this);
    }
}
