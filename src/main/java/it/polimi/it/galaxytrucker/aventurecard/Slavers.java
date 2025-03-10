package it.polimi.it.galaxytrucker.aventurecard;

import it.polimi.it.galaxytrucker.cardEffects.CreditReward;
import it.polimi.it.galaxytrucker.cardEffects.CrewmatePenalty;
import it.polimi.it.galaxytrucker.cardEffects.FlightDayPenalty;
import it.polimi.it.galaxytrucker.managers.Player;
import it.polimi.it.galaxytrucker.utility.Cargo;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class Slavers extends AdventureCard implements FlightDayPenalty, CreditReward, CrewmatePenalty {

    public Slavers(Optional<List<Player>> partecipants, Optional<Integer> penalty, Optional<Integer> flightDayPenalty, Optional<Cargo> reward, int firePower, int creditReward, AdventureDeck deck) {
        super(partecipants, penalty, flightDayPenalty, reward,firePower, creditReward, deck);
    }


    @Override
    public void giveCreditReward(int reward, Player player) {
            player.addCredits(reward);
    }

    @Override
    public void applyCrewmatePenalty(int penalty, Player player) {
            // togli umani o alieni
    }

    @Override
    public void applyFlightDayPenalty(int penalty, Player player) {
            //modifica di penalty il valore di player nella flightBoard
    }

    @Override
    public void play() {
        System.out.println("------------------------Slavers--------------------------");

        List<Player> players = (List<Player>) super.getPartecipants().orElse(Collections.emptyList());
        if (!players.isEmpty()) {

            // !!!!!! Ogni giocatore deve decidere se prendere il bottino rinunciando a giorni di viaggio

            //            int choise = 0;
            //           for (Player player : players) {
            //
            //                int nMin = (int)super.getPenalty().orElse(0);
            //
            //                if(player.getShipManager().calculateCrewmates(player.getPlayerID()) > nMin && choise==0 )  {
            //
            //                    System.out.println("Minimum number of crewmates:  " + nMin);
            //                    System.out.print("Credits: " + getCreditReward());
            //
            //                    if (partecipate(player) == true && choise == 0) {
            //                        giveCreditReward(getCreditReward(), player);
            //                        applyFlightDayPenalty((int) super.getFlightDayPenalty().orElse(0), player);
            //                        applyCrewmatePenalty((int) super.getPenalty().orElse(0), player);
            //                        choise = 1;
            //                    }
            //                } else
            //                    System.out.println("Player " + player.getPlayerID() + " doesn't have the minimum number of humans");
            //            }


        } else {
            System.out.println("No player can play this card");
        }
    }
}
