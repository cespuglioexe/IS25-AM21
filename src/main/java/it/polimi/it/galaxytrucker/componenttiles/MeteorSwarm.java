package it.polimi.it.galaxytrucker.componenttiles;

import it.polimi.it.galaxytrucker.aventurecard.AdventureDeck;
import it.polimi.it.galaxytrucker.aventurecard.Attack;
import it.polimi.it.galaxytrucker.managers.Player;
import it.polimi.it.galaxytrucker.utility.Direction;
import it.polimi.it.galaxytrucker.utility.Projectile;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class MeteorSwarm extends Attack {
    public MeteorSwarm(Optional partecipants, Optional penalty, Optional flightDayPenalty, Optional reward, int firePowerRequired, int creditReward, AdventureDeck deck, Map<Projectile, Direction> projectiles) {
        super(partecipants, penalty, flightDayPenalty, reward, firePowerRequired, creditReward, deck, projectiles);
    }

    @Override
    public void attack(Player player) {

    }

    @Override
    public void play() {
        List<Player> partecipants = super.getPartecipants().stream().toList();
        for (Player p : partecipants) {

        }
    }
}
