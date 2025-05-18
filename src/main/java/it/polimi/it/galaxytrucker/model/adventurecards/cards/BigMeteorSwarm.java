package it.polimi.it.galaxytrucker.model.adventurecards.cards;

import java.util.List;

import it.polimi.it.galaxytrucker.model.adventurecards.interfaces.attack.Projectile;
import it.polimi.it.galaxytrucker.model.design.strategyPattern.FlightRules;

public class BigMeteorSwarm extends MeteorSwarm {
    public BigMeteorSwarm(List<Projectile> meteorsAndDirections, FlightRules flightRules) {
        super(meteorsAndDirections, flightRules);
    }
}
