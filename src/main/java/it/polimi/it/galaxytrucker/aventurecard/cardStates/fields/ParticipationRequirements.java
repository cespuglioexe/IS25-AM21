package  it.polimi.it.galaxytrucker.aventurecard.cardStates.fields;

import java.util.List;

import it.polimi.it.galaxytrucker.managers.Player;
import it.polimi.it.galaxytrucker.aventurecard.AdventureCard;
import it.polimi.it.galaxytrucker.cardEffects.Participation;

public enum ParticipationRequirements implements FieldRequirements {
    PLAYER {
        @Override
        public boolean isSet(AdventureCard<?> card) {
            if (areAllParticipationSlotsTaken(card)) {
                return true;
            }
            return false;
        }

        private boolean areAllParticipationSlotsTaken(AdventureCard<?> card) {
            Participation<?> participation = (Participation<?>) card;
            List<Integer> slots = participation.getSlots();
            List<Player> partecipants = card.getPartecipants();
        
            return partecipants.size() == slots.size();
        }
    }
}
