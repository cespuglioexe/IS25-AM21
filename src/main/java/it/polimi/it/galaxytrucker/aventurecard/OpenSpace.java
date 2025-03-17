package it.polimi.it.galaxytrucker.aventurecard;

import it.polimi.it.galaxytrucker.exceptions.GameLostException;
import it.polimi.it.galaxytrucker.managers.FlightBoardState;
import it.polimi.it.galaxytrucker.managers.Player;
import it.polimi.it.galaxytrucker.utility.Cargo;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class OpenSpace extends AdventureCard{

    //
    public OpenSpace(Optional<Integer> penalty, Optional<Integer> flightDayPenalty, Optional<Integer> reward, int firePower, int creditReward) {
        super(penalty, flightDayPenalty, reward,firePower, creditReward);
    }


    public void travel(FlightBoardState board, Player player) {
        board.movePlayerForward((int)getFlightDayPenalty().orElse(0), player.getPlayerID());
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
