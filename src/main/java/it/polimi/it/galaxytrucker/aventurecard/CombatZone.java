package it.polimi.it.galaxytrucker.aventurecard;

import it.polimi.it.galaxytrucker.cardEffects.CrewmatePenalty;
import it.polimi.it.galaxytrucker.crewmates.Crewmate;

public class CombatZone extends Attack implements CrewmatePenalty {
    final private int FIRSTCREWMATEPENALTY = 3;
    final private int SECONDCREWMATEPENALTY = 2;

    @Override
    public void attack() {

    }

    @Override
    public void play() {

    }

    @Override
    public void applyPenalty(Crewmate penalty) {

    }
}
