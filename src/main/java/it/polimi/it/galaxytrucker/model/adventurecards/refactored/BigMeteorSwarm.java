package it.polimi.it.galaxytrucker.model.adventurecards.refactored;

import java.util.HashMap;
import java.util.List;

import it.polimi.it.galaxytrucker.model.adventurecards.interfaces.attack.Projectile;
import it.polimi.it.galaxytrucker.model.design.strategyPattern.FlightRules;
import it.polimi.it.galaxytrucker.model.utility.Direction;

public class BigMeteorSwarm extends MeteorSwarm {
    public BigMeteorSwarm(List<Projectile> meteorsAndDirections, FlightRules flightRules) {
        super(meteorsAndDirections, flightRules);
    }
}
