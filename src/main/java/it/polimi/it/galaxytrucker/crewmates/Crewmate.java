package it.polimi.it.galaxytrucker.crewmates;

public abstract class Crewmate {
    private boolean requiresLifeSupport;

    public Crewmate(boolean requiresLifeSupport) {
        this.requiresLifeSupport = requiresLifeSupport;
    }
}
