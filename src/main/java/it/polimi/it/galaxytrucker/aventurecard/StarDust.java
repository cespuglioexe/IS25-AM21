package it.polimi.it.galaxytrucker.aventurecard;

import it.polimi.it.galaxytrucker.cardEffects.FlightDayPenalty;
import it.polimi.it.galaxytrucker.exceptions.GameLostException;
import it.polimi.it.galaxytrucker.managers.Player;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class StarDust extends AdventureCard implements FlightDayPenalty{


    public StarDust(Optional<List<Player>> partecipants, Optional<Integer> penalty, Optional<Integer> flightDayPenalty, Optional<Integer> reward, int firePower, int creditReward, AdventureDeck deck) {
        super(partecipants, penalty, flightDayPenalty, reward,firePower, creditReward, deck);
    }


    @Override
    public void applyFlightDayPenalty(int penalty, Player player) {
        //super.getDeck().getGameManager()
    }


    public void play(){
        System.out.println("------------------------Star Dust--------------------------");

        List<Player> players = (List<Player>) super.getPartecipants().orElse(Collections.emptyList());
        if (!players.isEmpty()) {

            for (Player player : players) {
             //   applyFlightDayPenalty(player.getShipManager().,player);
            }
        } else {
            System.out.println("No player can play this card");
        }

    }
}
