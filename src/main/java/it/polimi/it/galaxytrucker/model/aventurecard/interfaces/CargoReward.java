package it.polimi.it.galaxytrucker.model.aventurecard.interfaces;

import java.util.Set;

import it.polimi.it.galaxytrucker.model.utility.Cargo;;

public interface CargoReward {
    public Set<Cargo> getCargoReward();
    public void applyCargoReward();
}
