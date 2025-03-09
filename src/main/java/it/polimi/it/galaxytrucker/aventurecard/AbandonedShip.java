package it.polimi.it.galaxytrucker.aventurecard;

import it.polimi.it.galaxytrucker.cardEffects.CreditReward;
import it.polimi.it.galaxytrucker.cardEffects.CrewmatePenalty;
import it.polimi.it.galaxytrucker.cardEffects.FlightDayPenalty;
import it.polimi.it.galaxytrucker.cardEffects.Participation;
import it.polimi.it.galaxytrucker.managers.Player;
import it.polimi.it.galaxytrucker.utility.Cargo;

import java.util.*;

public class AbandonedShip extends AdventureCard implements Participation, CreditReward, FlightDayPenalty, CrewmatePenalty {


    public AbandonedShip(Optional<List<Player>> partecipants, Optional<Integer> penalty, Optional<Integer> flightDayPenalty, Optional<Integer> reward, int firePower, int creditReward, AdventureDeck deck) {
        super(partecipants, penalty, flightDayPenalty, reward,firePower, creditReward, deck);
    }

    @Override
    public void giveCreditReward(int reward, Player player) {
        player.addCredits(reward);
    }

    @Override
    public void applyCrewmatePenalty(int penalty, Player player) {
           // player.getShipManager().
    }

    @Override
    public void applyFlightDayPenalty(int penalty, Player player) {
           // super.getDeck().getGameManager().
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
    public void play() {
        System.out.println("------------------------Abandoned Ship--------------------------");

        List<Player> players = (List<Player>) super.getPartecipants().orElse(Collections.emptyList());
        if (!players.isEmpty()) {
            int choise = 0;
            for (Player player : players) {

                int nMin = (int)super.getPenalty().orElse(0);

                if(player.getShipManager().calculateCrewmates(player.getPlayerID()) > nMin && choise==0 )  {

                    System.out.println("Minimum number of crewmates:  " + nMin);
                    System.out.print("Credits: " + getCreditReward());

                    if (partecipate(player) == true && choise == 0) {
                        giveCreditReward(getCreditReward(), player);
                        applyFlightDayPenalty((int) super.getFlightDayPenalty().orElse(0), player);
                        applyCrewmatePenalty((int) super.getPenalty().orElse(0), player);
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
