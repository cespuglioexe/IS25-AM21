package it.polimi.it.galaxytrucker.aventurecard;


import it.polimi.it.galaxytrucker.cardEffects.*;
import it.polimi.it.galaxytrucker.managers.Player;
import it.polimi.it.galaxytrucker.managers.ShipManager;


import java.util.*;


public class Pirates extends Attack implements CreditReward,FlightDayPenalty {
        Scanner scanner = new Scanner(System.in);


        public Pirates(Optional partecipants, Optional penalty, Optional flightDayPenalty, Optional reward, int firePowerRequired,int creditRreward, AdventureDeck deck) {
                super(partecipants, penalty, flightDayPenalty, reward, firePowerRequired,creditRreward, deck);
        }


        @Override
        public void attack() {


        }


        @Override
        public void play() {
                if (super.getPartecipants().isPresent()) {
                        List<Player> players = super.getPartecipants().stream().toList();
                        if (!players.isEmpty()) {
                                // La lista contiene elementi, procedi
                                for (Player player : players) {

                                        // Fai qualcosa con ogni player
                                        if (player.getShipManager().calculateFirePower(player.getPlayerID()) < super.getFirePowerRequired()){//sto confrontando float con optional
                                                attack();
                                        }else{
                                                System.out.println("Do you want to take the reward?");
                                                // Legge la prima lettera inserita
                                                /*yes*/
                                                if(scanner.next() == "y"){
                                                        giveCreditReward(getCreditReward(),player);
                                                        applyFlightDayPenalty((Integer) getFlightDayPenalty().orElse(0), player);                                                        //add credits and apply flight day penalty
                                                }else{/*no*/


                                                }
                                        }


                                }
                        } else {
                                // La lista è vuota, gestisci il caso
                        }
                } else {
                        // L'Optional è vuoto, gestisci il caso
                }


        }


        @Override
        public void applyFlightDayPenalty(int penalty, Player player) {
                
        }

        @Override
        public void giveCreditReward(int reward, Player player) {
                player.setCredits(reward);
        }
}

