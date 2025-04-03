package it.polimi.it.galaxytrucker.model.crewmates;

import it.polimi.it.galaxytrucker.model.utility.AlienType;

public class Alien extends Crewmate {
    private AlienType alienType;

    public Alien(AlienType type) {
        super(true);
        this.alienType = type;
    }

    public AlienType getAlienType() {
        return alienType;
    }
}
