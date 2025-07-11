package it.polimi.it.galaxytrucker.model.gamestates.fields;

import it.polimi.it.galaxytrucker.exceptions.InvalidActionException;
import it.polimi.it.galaxytrucker.model.managers.Model;

public interface FieldRequirements {
    public boolean isSet(Model model) throws InvalidActionException;
}
