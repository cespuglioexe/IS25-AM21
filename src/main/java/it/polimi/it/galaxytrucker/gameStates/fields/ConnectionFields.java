package it.polimi.it.galaxytrucker.gameStates.fields;

import it.polimi.it.galaxytrucker.managers.Model;

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
