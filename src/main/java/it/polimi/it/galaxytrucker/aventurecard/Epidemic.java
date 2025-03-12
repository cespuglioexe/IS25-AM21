package it.polimi.it.galaxytrucker.aventurecard;

import it.polimi.it.galaxytrucker.cardEffects.CrewmatePenalty;
import it.polimi.it.galaxytrucker.managers.Player;
import it.polimi.it.galaxytrucker.utility.Cargo;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class Epidemic extends AdventureCard implements CrewmatePenalty {

    public Epidemic(Optional<List<Player>> partecipants, Optional<Integer> penalty, Optional<Integer> flightDayPenalty, Optional<Cargo> reward, int firePower, int creditReward, AdventureDeck deck) {
        super(partecipants, penalty, flightDayPenalty, reward,firePower, creditReward, deck);
    }

    @Override
    public void applyCrewmatePenalty(int penalty, Player player) {

        // So il numero di gente da togliere e procedo ad eliminarli, se sono vicini

        //player.getShipManager()
    }

    @Override
    public void play() {
        System.out.println("------------------------Epidemic--------------------------");

        List<Player> players = (List<Player>) super.getPartecipants().stream().toList();
        if (!players.isEmpty()) {


           for (Player player : players) {

              // player.getShipManager().

            }


        } else {
            System.out.println("No player can play this card");
        }
    }
}
