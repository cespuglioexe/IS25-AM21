package it.polimi.it.galaxytrucker.aventurecard;

import it.polimi.it.galaxytrucker.cardEffects.*;
import it.polimi.it.galaxytrucker.managers.Player;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Pirates extends Attack implements CreditReward, FlightDayPenalty{
        private int piratesPower;
        Scanner scanner = new Scanner(System.in);

        public Pirates(Optional partecipants, Optional penalty, Optional flightDayPenalty, Optional reward, int piratesPower) {
                super(partecipants, penalty, flightDayPenalty, reward);
                this.piratesPower = piratesPower;
        }



        @Override
        public void attack() {

        }

        @Override
        public void giveReward(Integer reward) {
        }

        @Override
        public void applyPenalty(Integer penalty) {
        }

        @Override
        public void play() {
                if (super.getPartecipants().isPresent()) {
                        List<Player> players = super.getPartecipants().stream().toList();
                        if (!players.isEmpty()) {
                                // La lista contiene elementi, procedi
                                for (Player player : players) {
                                        // Fai qualcosa con ogni player
                                        if (player.shipManager.calculateFirePower() < piratesPower){
                                                attack();
                                        }else{
                                                System.out.println("Do you want to take the reward?");
                                                // Legge la prima lettera inserita
                                                /*yes*/
                                                if(scanner.next() == "y"){
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
                        System.out.println("L'Optional dei giocatori è vuoto.");
                }

        }

        // for
        // if
        // partecipants.get(i).shipManager.calculateFirePower(float) // Chiedere float parametro
}

