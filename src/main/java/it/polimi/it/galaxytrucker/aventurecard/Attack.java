package it.polimi.it.galaxytrucker.aventurecard;

import it.polimi.it.galaxytrucker.managers.Player;
import it.polimi.it.galaxytrucker.utility.Projectile;

import java.util.List;
import java.util.Optional;

public abstract class Attack extends AdventureCard {
    private List<Projectile> projectiles;

    public Attack(Optional partecipants, Optional penalty, Optional flightDayPenalty, Optional reward, int firePowerRequired, int creditReward, AdventureDeck deck) {
        super(partecipants, penalty, flightDayPenalty, reward, firePowerRequired, creditReward, deck);
    }

    public List<Projectile> getProjectiles() {
        return projectiles;
    }

    public abstract void attack();

}
