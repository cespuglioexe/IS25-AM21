package it.polimi.it.galaxytrucker.aventurecard;

import it.polimi.it.galaxytrucker.cardEffects.CreditReward;
import it.polimi.it.galaxytrucker.cardEffects.CrewmatePenalty;
import it.polimi.it.galaxytrucker.cardEffects.FlightDayPenalty;
import it.polimi.it.galaxytrucker.crewmates.Crewmate;
import it.polimi.it.galaxytrucker.managers.FlightBoardState;
import it.polimi.it.galaxytrucker.managers.Player;
import it.polimi.it.galaxytrucker.utility.Cargo;

import java.util.*;

public class Slavers extends AdventureCard implements FlightDayPenalty, CreditReward, CrewmatePenalty {

    private boolean isDefeated;

    public Slavers(Optional<Integer> penalty, Optional<Integer> flightDayPenalty, Optional<Integer> reward, int firePower, int creditReward) {
        super(penalty, flightDayPenalty, reward,firePower, creditReward);
        this.isDefeated = false;
    }

    public void setDefeated(boolean defeated) {
        isDefeated = defeated;
    }

    public void setPlayer(List<Player> partecipants) {
        super.setPartecipants(partecipants);
    }

    public boolean checkReward(int firePower) {
        if(!isDefeated) {
            if (firePower < super.getFirePowerRequired()) {
                return false;
            } else {
                if (firePower > super.getFirePowerRequired()) {
                    setDefeated(true);
                    return true;
                }
                else return false;
            }
        } else return false;
    }

    @Override
    public void giveCreditReward(Player player) {
        player.addCredits((int)super.getReward().orElse(0));
    }

    @Override
    public void applyCrewmatePenalty(int penalty, Player player) {

        /*
        int[] position = new int[2];
        position = super.getDeck().getGameManager().positionSelection(penalty,player);
        player.getShipManager().removeCrewmate(position[0], position[1]);cv
        */

        // Ricevo coordinate e mando a shipManager che se tutto va bene elimina il crewmate
    }

    @Override
    public void applyFlightDayPenalty(FlightBoardState board, Player player) {
        board.movePlayerBackwards((int)getFlightDayPenalty().orElse(0), player.getPlayerID());
    }




    ////////////////////////////////////////////////////////////////////////////////////////////////////////////
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
/*
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

 */

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////
}
