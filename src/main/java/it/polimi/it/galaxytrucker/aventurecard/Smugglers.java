package it.polimi.it.galaxytrucker.aventurecard;

import it.polimi.it.galaxytrucker.cardEffects.CargoPenalty;
import it.polimi.it.galaxytrucker.cardEffects.CargoReward;
import it.polimi.it.galaxytrucker.cardEffects.FlightDayPenalty;
import it.polimi.it.galaxytrucker.managers.FlightBoardState;
import it.polimi.it.galaxytrucker.managers.Player;
import it.polimi.it.galaxytrucker.utility.Cargo;

import java.util.*;

public class Smugglers extends AdventureCard implements CargoReward, CargoPenalty, FlightDayPenalty {

    private boolean isDefeated;
    private CargoManager manager;

    public Smugglers(Optional<Integer> penalty, Optional<Integer> flightDayPenalty, Optional<Cargo> reward, int firePower, int creditReward, AdventureDeck deck, CargoManager manager) {
        super(penalty, flightDayPenalty, reward,firePower, creditReward);
        this.manager = manager;
        this.isDefeated = false;
    }

    public void setDefeated(boolean defeated) {
        isDefeated = defeated;
    }

    public void setPlayer(List<Player> partecipants) {
        super.setPartecipants(partecipants);
    }


    public boolean checkReward(int firePower){
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
    public void applyPenalty(int penalty, Player player) {
            manager.manageCargoDischarge(penalty, player);
            // Controllo le merci più preziose e ti mando le coordinate e il colore del cargo
    }

    @Override
    public void giveCargoReward(Set<Cargo> reward, Player player) {
        for(Cargo cargo : reward) {
            manager.manageCargoAddition(cargo,player);
        }
    }

    @Override
    public void applyFlightDayPenalty(FlightBoardState board, Player player) {
        board.movePlayerBackwards((int)getFlightDayPenalty().orElse(0), player.getPlayerID());
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
/*
    @Override
    public void play() {

            System.out.println("------------------------Smugglers--------------------------");

            List<Player> players = (List<Player>) super.getPartecipants().orElse(Collections.emptyList());
            if (!players.isEmpty()) {

                int lost = 0;

                System.out.println("Smugglers require a firepower of" +super.getFirePowerRequired());
                for (Player player : players) {

                    if(lost == 0) {
                        if (player.getShipManager().calculateEnginePower() < super.getFirePowerRequired()) {
                            applyPenalty((int) super.getPenalty().orElse(0), player);
                        } else {
                            if (player.getShipManager().calculateEnginePower() > super.getFirePowerRequired()) {
                                if (selection(player)) {
                                    applyFlightDayPenalty((int) super.getFlightDayPenalty().orElse(0), player);
                                    giveCargoReward((Set<Cargo>) super.getReward().get(), player);
                                }
                            }
                            System.out.println("Player " + player.getPlayerID() + "  defeated the smugglers");
                            lost = 1;
                        }
                    }
                }
            } else
                System.out.println("No player can play this card");

    }
*/

}
