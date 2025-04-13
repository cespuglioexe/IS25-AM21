package it.polimi.it.galaxytrucker.model.aventurecard.refactored;

import java.util.HashMap;

import it.polimi.it.galaxytrucker.model.design.strategyPattern.FlightRules;
import it.polimi.it.galaxytrucker.model.utility.Direction;
import it.polimi.it.galaxytrucker.model.utility.Projectile;

public class BigMeteorSwarm extends MeteorSwarm {
    public BigMeteorSwarm(HashMap<Projectile, Direction> meteorsAndDirections, FlightRules flightRules) {
        super(meteorsAndDirections, flightRules);
    }
}
