package it.polimi.it.galaxytrucker.model.adventurecards.interfaces.attack;

import java.util.List;

import it.polimi.it.galaxytrucker.model.design.strategyPattern.Strategy;
import it.polimi.it.galaxytrucker.model.managers.ShipManager;

public interface AimingStrategy extends Strategy {
    public List<Integer> aim(ShipManager ship);
}
