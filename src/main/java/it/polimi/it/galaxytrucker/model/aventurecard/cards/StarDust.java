package it.polimi.it.galaxytrucker.model.aventurecard.cards;

import it.polimi.it.galaxytrucker.model.aventurecard.AdventureCard;
import it.polimi.it.galaxytrucker.model.cardEffects.FlightDayPenalty;
import it.polimi.it.galaxytrucker.model.managers.FlightBoard;
import it.polimi.it.galaxytrucker.model.managers.Player;

import java.util.List;
import java.util.Optional;

public class StarDust extends AdventureCard implements FlightDayPenalty{


    public StarDust(Optional<Integer> penalty, Optional<Integer> flightDayPenalty, Optional<Integer> reward, int firePower, int creditReward) {
        super(penalty, flightDayPenalty, reward,firePower, creditReward);
    }

    public void setPlayer(List<Player>  partecipants) {
        super.setPartecipants(partecipants);
    }

    public void applyPenalty(FlightBoard board, List<Player> partecipants) {
        for (Player player : partecipants) {
            applyFlightDayPenalty(board,player);
        }
    }

    @Override
    public void applyFlightDayPenalty(FlightBoard board, Player player) {
        //board.movePlayerBackwards(player.getShipManager().countAllExposedConnectors(), player.getPlayerID());
    }



    /*
    *  Configuro con SetPlayer() la lista di giocatori passati come parametro
    *  Per ogni giocatore lancio il metodo applyPenalty() che applica FlightDayPenalty
    * */
    /*
    public void play(){
        System.out.println("------------------------Star Dust--------------------------");

        List<Player> players = (List<Player>) super.getPartecipants().orElse(Collections.emptyList());
        if (!players.isEmpty()) {

            for (Player player : players) {
               applyFlightDayPenalty(player.getShipManager().countAllExposedConnectors(),player);
            }
        } else {
            System.out.println("No player can play this card");
        }

    }
    
     */
}
