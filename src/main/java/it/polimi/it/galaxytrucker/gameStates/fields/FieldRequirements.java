package it.polimi.it.galaxytrucker.gameStates.fields;

import it.polimi.it.galaxytrucker.exceptions.InvalidActionException;
import it.polimi.it.galaxytrucker.managers.Model;

public interface FieldRequirements {
    boolean isSet(Model model) throws InvalidActionException;
}
