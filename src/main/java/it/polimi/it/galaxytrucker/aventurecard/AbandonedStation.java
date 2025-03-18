package it.polimi.it.galaxytrucker.aventurecard;

import it.polimi.it.galaxytrucker.cardEffects.CargoReward;
import it.polimi.it.galaxytrucker.cardEffects.FlightDayPenalty;
import it.polimi.it.galaxytrucker.cardEffects.Participation;
import it.polimi.it.galaxytrucker.managers.FlightBoardState;
import it.polimi.it.galaxytrucker.managers.Player;
import it.polimi.it.galaxytrucker.utility.Cargo;

import java.util.*;

public class AbandonedStation extends AdventureCard implements Participation, CargoReward, FlightDayPenalty {

    public final int MAX_PARTICIPATIONS;
    private CargoManager manager;

    public AbandonedStation(Optional<Integer> penalty, Optional<Integer> flightDayPenalty, Optional<Set<Cargo>> reward, int firePower, int creditReward,CargoManager manager) {
           super(penalty, flightDayPenalty, reward,firePower, creditReward);
           this.manager = manager;
           MAX_PARTICIPATIONS = 1;
    }

    public void setPlayer(List<Player> partecipants) {
        super.setPartecipants(partecipants);
    }

    @Override
    public Set<Cargo> giveCargoReward(Player player) {
        return (Set<Cargo>) super.getReward().orElse(0);
    }

    @Override
    public void applyFlightDayPenalty(FlightBoardState board, Player player) {
        board.movePlayerBackwards((int)getFlightDayPenalty().orElse(0), player.getPlayerID());
    }

    public int getMAX_PARTICIPATIONS() {
        return MAX_PARTICIPATIONS;
    }

    public boolean RequiredHumanVerification(Player player) {
        int nMin = (int)super.getPenalty().orElse(0);
        if (player.getShipManager().countCrewmates() >= nMin) {
            return true;
        }
        return false;
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

/*
    @Override
    public void play() {
        System.out.println("------------------------Abandoned Station--------------------------");

        List<Player> players = (List<Player>) super.getPartecipants().orElse(Collections.emptyList());
        if (!players.isEmpty()) {
            int choise = 0;
            for (Player player : players) {
                if (choise == 0) {
                    int nMin = (int) super.getPenalty().orElse(0);
                    if (player.getShipManager().countCrewmates() > nMin && choise == 0) {
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
            }
        } else {
            System.out.println("No player can play this card");
        }



    }
    */

}
