package it.polimi.it.galaxytrucker.model.crewmates;

public abstract class Crewmate {
    private boolean requiresLifeSupport;

    public Crewmate(boolean requiresLifeSupport) {
        this.requiresLifeSupport = requiresLifeSupport;
    }

    public boolean requiresLifeSupport() {
        return this.requiresLifeSupport;
    }
}
