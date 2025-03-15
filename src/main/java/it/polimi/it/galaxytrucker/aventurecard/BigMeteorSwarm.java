package it.polimi.it.galaxytrucker.aventurecard;

import it.polimi.it.galaxytrucker.utility.Direction;
import it.polimi.it.galaxytrucker.utility.Projectile;

import java.util.Map;
import java.util.Optional;

public class BigMeteorSwarm extends MeteorSwarm {
    public BigMeteorSwarm(Optional partecipants, Optional penalty, Optional flightDayPenalty, Optional reward, int firePowerRequired, int creditReward, AdventureDeck deck, Map<Projectile, Direction> projectiles) {
        super(partecipants, penalty, flightDayPenalty, reward, firePowerRequired, creditReward, deck, projectiles);
    }


}
