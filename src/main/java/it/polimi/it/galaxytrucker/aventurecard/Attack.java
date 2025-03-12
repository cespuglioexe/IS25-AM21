package it.polimi.it.galaxytrucker.aventurecard;

import it.polimi.it.galaxytrucker.managers.Player;
import it.polimi.it.galaxytrucker.managers.ShipManager;
import it.polimi.it.galaxytrucker.utility.Direction;
import it.polimi.it.galaxytrucker.utility.Projectile;

import java.awt.*;
import java.util.*;
import java.util.List;

public abstract class Attack extends AdventureCard {
    private Map<Projectile, Direction> projectiles = new HashMap<>();

    public Attack(Optional partecipants, Optional penalty, Optional flightDayPenalty, Optional reward, int firePowerRequired, int creditReward, AdventureDeck deck, Map<Projectile, Direction> projectiles) {
        super(partecipants, penalty, flightDayPenalty, reward, firePowerRequired, creditReward, deck);
        this.projectiles = projectiles;
    }

    public Map<Projectile, Direction> getProjectiles() {
        return projectiles;
    }

    //ho bisogno si sapere quale player viene attaccato soprattutto nelle carte in cui devo attaccare più players
    public abstract void attack(Player player);

    public int rollDice(){
        int level = super.getDeck().getGameManager().getLevel();
        Random rand = new Random();
        int line=0;

        if (level == 1){
            line = rand.nextInt(4)+5; //numero da 5 a 9
        }
        if (level == 2){
            line = rand.nextInt(7)+4;//numero da 4 a 10
        }
        return line;
    }

}
