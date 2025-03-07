package it.polimi.it.galaxytrucker.crewmates;

public abstract class Crewmate {
    private final boolean requiresLifeSupport;

    public Crewmate(boolean requiresLifeSupport) {
        this.requiresLifeSupport = requiresLifeSupport;
    }

    public boolean isRequiresLifeSupport() {
        return requiresLifeSupport;
    }
}
