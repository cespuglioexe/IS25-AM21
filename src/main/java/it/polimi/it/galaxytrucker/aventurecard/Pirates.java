package it.polimi.it.galaxytrucker.aventurecard;

import it.polimi.it.galaxytrucker.cardEffects.*;
import it.polimi.it.galaxytrucker.managers.Player;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Pirates extends Attack implements CreditReward, FlightDayPenalty{
        Scanner scanner = new Scanner(System.in);

        public Pirates(Optional partecipants, Optional penalty, Optional flightDayPenalty, Optional reward, int firePowerRequired) {
                super(partecipants, penalty, flightDayPenalty, reward, firePowerRequired);
        }



        @Override
        public void attack() {

        }

        @Override
        public void giveReward(Integer reward) {
                if (super.getPartecipants().isPresent()) {
                        List<Player> players = super.getPartecipants().stream().toList();
                        if (!players.isEmpty()) {
                                // La lista contiene elementi, procedi
                                for (Player player : players) {
                                        player.setCredits(player.getCredits() + reward);
                                }
                        }
                }
        }

        @Override
        public void applyPenalty(Integer dayPenalty) {

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
                                                        giveReward(5);
                                                        applyPenalty(2);
                                                        //add credits and apply flight day penalty
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


}

