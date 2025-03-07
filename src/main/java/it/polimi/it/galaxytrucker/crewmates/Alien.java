package it.polimi.it.galaxytrucker.crewmates;

import it.polimi.it.galaxytrucker.utility.AlienType;

public class Alien extends Crewmate {
    private AlienType type;

    public Alien(AlienType type,boolean lifeSupport) {
        super(lifeSupport);
        this.type = type;
    }
}
