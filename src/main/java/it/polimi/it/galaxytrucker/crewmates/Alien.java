package it.polimi.it.galaxytrucker.crewmates;

import it.polimi.it.galaxytrucker.utility.AlienType;

public class Alien extends Crewmate {
    private AlienType alienType;

    public Alien(boolean requiresLifeSupport, AlienType type) {
        super(requiresLifeSupport);
        this.alienType = type;
    }

    public AlienType getAlienType() {
        return alienType;
    }
}
