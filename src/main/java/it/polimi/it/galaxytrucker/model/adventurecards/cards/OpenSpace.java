package it.polimi.it.galaxytrucker.model.adventurecards.cards;

import it.polimi.it.galaxytrucker.model.adventurecards.AdventureCard;
import it.polimi.it.galaxytrucker.model.managers.FlightBoard;
import it.polimi.it.galaxytrucker.model.managers.Player;

import java.util.Optional;

public class OpenSpace extends AdventureCard {

    //
    public OpenSpace(Optional<Integer> penalty, Optional<Integer> flightDayPenalty, Optional<Integer> reward, int firePower, int creditReward) {
        super(penalty, flightDayPenalty, reward,firePower, creditReward);
    }


    public void travel(FlightBoard board, Player player, int enginePower) {
        //board.movePlayerForward(enginePower,player.getPlayerID());
    }

/*

    public void play() {
        System.out.println("------------------------Open Space--------------------------");

        List<Player> players = (List<Player>) super.getPartecipants().orElse(Collections.emptyList());
        if (!players.isEmpty()) {

            for (Player player : players) {
                if(player.getShipManager().calculateEnginePower() > 0)
                    travel((int)getReward().orElse(0), player);
                else new GameLostException("Player "+player.getPlayerID()+" lost");
            }
        } else {
            System.out.println("No player can play this card");
        }
    }

 */
}
