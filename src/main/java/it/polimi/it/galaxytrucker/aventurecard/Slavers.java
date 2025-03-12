package it.polimi.it.galaxytrucker.aventurecard;

import it.polimi.it.galaxytrucker.cardEffects.CreditReward;
import it.polimi.it.galaxytrucker.cardEffects.CrewmatePenalty;
import it.polimi.it.galaxytrucker.cardEffects.FlightDayPenalty;
import it.polimi.it.galaxytrucker.crewmates.Crewmate;
import it.polimi.it.galaxytrucker.managers.Player;
import it.polimi.it.galaxytrucker.utility.Cargo;

import java.util.*;

public class Slavers extends AdventureCard implements FlightDayPenalty, CreditReward, CrewmatePenalty {

    public Slavers(Optional<List<Player>> partecipants, Optional<Integer> penalty, Optional<Integer> flightDayPenalty, Optional<Integer> reward, int firePower, int creditReward, AdventureDeck deck) {
        super(partecipants, penalty, flightDayPenalty, reward,firePower, creditReward, deck);
    }


    @Override
    public void giveCreditReward(int reward, Player player) {
            player.addCredits(reward);
    }

    @Override
    public void applyCrewmatePenalty(int penalty, Player player) {

        /*
        int[] position = new int[2];
        position = super.getDeck().getGameManager().positionSelection(penalty,player);
        player.getShipManager().removeCrewmate(position[0], position[1]);
        */

        // Ricevo coordinate e mando a shipManager che se tutto va bene elimina il crewmate
    }

    @Override
    public void applyFlightDayPenalty(int penalty, Player player) {
        super.getDeck().getGameManager().getFlightBoardState().movePlayerBackwards(penalty, player.getPlayerID());
    }

    private boolean selection(Player player) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Cargo: ");
        for (Cargo cargo : (Set<Cargo>) super.getReward().get()) {
            System.out.print(" " + cargo.getColor());
        }
        System.out.println("Do you want to retire the load losing days of travel? :");
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
        System.out.println("------------------------Slavers--------------------------");

        List<Player> players = (List<Player>) super.getPartecipants().orElse(Collections.emptyList());
        if (!players.isEmpty()) {

            int lost = 0;

            System.out.println("Slavers require a firepower of" +super.getFirePowerRequired());
            for (Player player : players) {

                if(lost == 0) {
                    if (player.getShipManager().calculateEnginePower() < super.getFirePowerRequired()) {
                        applyCrewmatePenalty((int) super.getPenalty().orElse(0), player);
                    } else {
                        if (player.getShipManager().calculateEnginePower() > super.getFirePowerRequired()) {
                            if (selection(player)) {
                                applyFlightDayPenalty((int) super.getFlightDayPenalty().orElse(0), player);
                                giveCreditReward(super.getCreditReward(), player);
                            }
                        }
                        System.out.println("Player " + player.getPlayerID() + "  defeated the slavers");
                        lost = 1;
                    }
                }
            }
        } else {
            System.out.println("No player can play this card");
        }
    }
}
