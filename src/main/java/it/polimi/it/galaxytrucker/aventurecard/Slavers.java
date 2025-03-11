package it.polimi.it.galaxytrucker.aventurecard;

import it.polimi.it.galaxytrucker.cardEffects.CreditReward;
import it.polimi.it.galaxytrucker.cardEffects.CrewmatePenalty;
import it.polimi.it.galaxytrucker.cardEffects.FlightDayPenalty;
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
            // togli umani o alieni

    }

    @Override
    public void applyFlightDayPenalty(int penalty, Player player) {
            //modifica di penalty il valore di player nella flightBoard
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

        super.getDeck().getGameManager().

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
                    if (player.getShipManager().calculateEnginePower(player.getPlayerID()) < super.getFirePowerRequired()) {
                        applyCrewmatePenalty((int) super.getPenalty().orElse(0), player);
                    } else {
                        if (player.getShipManager().calculateEnginePower(player.getPlayerID()) > super.getFirePowerRequired()) {
                            System.out.println("Player " + player.getPlayerID() + "  defeated the slavers");
                            lost = 1;
                            if (selection(player)) {
                                applyFlightDayPenalty((int) super.getFlightDayPenalty().orElse(0), player);
                                giveCreditReward(super.getCreditReward(), player);
                            }
                        }
                    }
                }
            }


        } else {
            System.out.println("No player can play this card");
        }
    }
}
