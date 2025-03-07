package it.polimi.it.galaxytrucker.cardEffects;

import it.polimi.it.galaxytrucker.crewmates.Crewmate;

public interface CrewmatePenalty extends Penalty <Crewmate>{

    @Override
    void applyPenalty(Crewmate penalty);
}
