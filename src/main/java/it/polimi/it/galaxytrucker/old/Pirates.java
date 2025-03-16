package it.polimi.it.galaxytrucker.old;


import it.polimi.it.galaxytrucker.aventurecard.AdventureDeck;
import it.polimi.it.galaxytrucker.cardEffects.*;
import it.polimi.it.galaxytrucker.managers.Player;
import it.polimi.it.galaxytrucker.utility.Direction;
import it.polimi.it.galaxytrucker.utility.Projectile;


import java.util.*;


public class Pirates extends Attack implements CreditReward,FlightDayPenalty {

        Map<Projectile, Direction> projectiles;

        public Pirates(Optional partecipants, Optional penalty, Optional flightDayPenalty, Optional reward, int firePowerRequired, int creditReward, AdventureDeck deck, Map<Projectile, Direction> projectiles) {
                super(partecipants, penalty, flightDayPenalty, reward, firePowerRequired, creditReward, deck, projectiles);
        }

        public void setProjectiles() {
                this.projectiles = super.getProjectiles();
        }

        @Override
        public void attack(Player player, Set<List<Integer>> listaScudi) {

        }

        @Override
        public void meteorStorm(Player player, int line, Map.Entry<Projectile, Direction> projectiles) {
                //non serve qui
        }

        @Override
        public void play() {

        }

        @Override
        public void giveCreditReward(int reward, Player player) {
                player.addCredits(reward);
        }

        @Override
        public void applyFlightDayPenalty(int penalty, Player player) {

        }
}

