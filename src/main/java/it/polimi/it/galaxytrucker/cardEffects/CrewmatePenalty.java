package it.polimi.it.galaxytrucker.cardEffects;

import it.polimi.it.galaxytrucker.crewmates.Crewmate;
import it.polimi.it.galaxytrucker.managers.Player;

public interface CrewmatePenalty extends Penalty <Crewmate>{

    @Override
    void applyPenalty(Crewmate penalty, Player player);
}
