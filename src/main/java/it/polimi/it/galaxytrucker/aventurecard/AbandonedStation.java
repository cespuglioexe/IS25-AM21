package it.polimi.it.galaxytrucker.aventurecard;

import it.polimi.it.galaxytrucker.cardEffects.CargoReward;
import it.polimi.it.galaxytrucker.cardEffects.CrewmatePenalty;
import it.polimi.it.galaxytrucker.cardEffects.FlightDayPenalty;
import it.polimi.it.galaxytrucker.cardEffects.Participation;
import it.polimi.it.galaxytrucker.managers.Player;
import it.polimi.it.galaxytrucker.utility.Cargo;

import java.util.*;

public class AbandonedStation extends AdventureCard implements Participation, CargoReward, FlightDayPenalty {


    public AbandonedStation(Optional<List<Player>> partecipants, Optional<Integer> penalty, Optional<Integer> flightDayPenalty, Optional<Cargo> reward, HashMap<Integer, Set<Cargo>> planets, int firePower, int creditReward, AdventureDeck deck) {
           super(partecipants, penalty, flightDayPenalty, reward,firePower, creditReward, deck);
    }


    @Override
    public boolean partecipate(Player player) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Do you want to participate? :");
        System.out.println("1. Yes");
        System.out.println("2. No");
        int choice = scanner.nextInt();
        if(choice == 1){
            return true;
        }
        return false;
    }


    @Override
    public void giveCargoReward(Set<Cargo> reward, Player player) {

    }

    @Override
    public void applyFlightDayPenalty(int penalty, Player player) {
        //    super.getDeck().getGameManager().
    }


    @Override
    public void play() {
        System.out.println("------------------------Abandoned Station--------------------------");

        List<Player> players = (List<Player>) super.getPartecipants().orElse(Collections.emptyList());
        if (!players.isEmpty()) {
            int choise = 0;
            for (Player player : players) {

                int nMin = (int)super.getPenalty().orElse(0);

                if(player.getShipManager().calculateCrewmates(player.getPlayerID()) > nMin && choise==0 )  {

                    System.out.println("Minimum number of crewmates:  " + nMin);
                    System.out.print("Cargo: ");
                    for (Cargo cargo : (Set<Cargo>) super.getReward().get()) {
                        System.out.print(" " + cargo.getColor());
                    }

                    if (partecipate(player) == true && choise == 0) {
                        giveCargoReward((Set<Cargo>) super.getReward().get(), player);
                        applyFlightDayPenalty((int) super.getFlightDayPenalty().orElse(0), player);
                        choise = 1;
                    }
                } else
                    System.out.println("Player " + player.getPlayerID() + " doesn't have the minimum number of humans");
            }
        } else {
            System.out.println("No player can play this card");
        }



    }
}
