package it.polimi.it.galaxytrucker.aventurecard;

import it.polimi.it.galaxytrucker.exceptions.GameLostException;
import it.polimi.it.galaxytrucker.managers.Player;
import it.polimi.it.galaxytrucker.utility.Cargo;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class OpenSpace extends AdventureCard{

    //
    public OpenSpace(Optional<List<Player>> partecipants, Optional<Integer> penalty, Optional<Integer> flightDayPenalty, Optional<Integer> reward, int firePower, int creditReward, AdventureDeck deck) {
        super(partecipants, penalty, flightDayPenalty, reward,firePower, creditReward, deck);
    }

    public void travel(int num,Player player){
        // super.getDeck().getGameManager().FlightBoard
    }


    @Override
    public void play() {
        System.out.println("------------------------Open Space--------------------------");

        List<Player> players = (List<Player>) super.getPartecipants().orElse(Collections.emptyList());
        if (!players.isEmpty()) {

            for (Player player : players) {
                if(player.getShipManager().calculateEnginePower(player.getPlayerID()) > 0)
                    travel((int)getReward().orElse(0), player);
                else new GameLostException("Player "+player.getPlayerID()+" lost");
            }
        } else {
            System.out.println("No player can play this card");
        }
    }
}
