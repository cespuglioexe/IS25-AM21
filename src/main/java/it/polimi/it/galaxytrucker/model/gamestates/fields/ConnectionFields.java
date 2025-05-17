package it.polimi.it.galaxytrucker.model.gamestates.fields;

import it.polimi.it.galaxytrucker.model.managers.Model;

public enum ConnectionFields implements FieldRequirements {
    PLAYER {
        @Override
        public boolean isSet(Model model) {
            if (model.allPlayersConnected()) {
                return true;
            }
            return false;
        }
    }
}
