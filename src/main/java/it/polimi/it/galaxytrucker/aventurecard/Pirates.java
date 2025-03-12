package it.polimi.it.galaxytrucker.aventurecard;


import it.polimi.it.galaxytrucker.cardEffects.*;
import it.polimi.it.galaxytrucker.componenttiles.ComponentTile;
import it.polimi.it.galaxytrucker.componenttiles.Shield;
import it.polimi.it.galaxytrucker.componenttiles.TileEdge;
import it.polimi.it.galaxytrucker.managers.Player;
import it.polimi.it.galaxytrucker.managers.ShipManager;
import it.polimi.it.galaxytrucker.utility.Direction;
import it.polimi.it.galaxytrucker.utility.Projectile;


import java.util.*;


public class Pirates extends Attack implements CreditReward,FlightDayPenalty {
        Scanner scanner = new Scanner(System.in);
        public Pirates(Optional partecipants, Optional penalty, Optional flightDayPenalty, Optional reward, int firePowerRequired,int creditRreward, AdventureDeck deck, Map<Projectile, Direction> projectiles) {
                super(partecipants, penalty, flightDayPenalty, reward, firePowerRequired,creditRreward, deck, projectiles);
        }


        @Override
        public void attack(Player player) {
                int rotation=0;
                int line,col=0,row=0, index=0;
                ComponentTile componetHit = null;
                List<Optional<ComponentTile>> sequence;
                Map<Projectile, Direction> cannonate = getProjectiles();

                for(Map.Entry<Projectile, Direction> entry : cannonate.entrySet()) {
                        System.out.println(entry.getKey() + " " + entry.getValue());
                        //per ogni proiettile tira il dado per vedere in che colonna/riga si trova
                        line= super.rollDice();

                        //recupera la colonna/riga in cui c'è il proiettile
                        if(entry.getValue()==Direction.UP||entry.getValue()==Direction.DOWN){
                                //dato l'indice della colonna mi ritorna tutta la colonna
                                sequence =  player.getShipManager().getComponentsAtColumn(line);
                        }else {
                                //dato l'indice della riga mi ritorna tutta la riga
                                sequence =  player.getShipManager().getComponentsAtRow(line);
                        }

                        //trovo l'indice del primo pezzo che verrà colpito
                        for (Optional<ComponentTile> c : sequence) {
                                if(c.isPresent()){
                                        componetHit = c.get();
                                        break;
                                }
                                index++;
                        }

                        if(super.getDeck().getGameManager().getLevel()==1){
                                index=index+4;
                        }else{
                                index=index+5;
                        }

                        // in base alla grandezza del proiettile ho 2 comportamenti diversi
                        if(entry.getKey() == Projectile.SMALL){
                                if (entry.getValue()== shield.getOrientation()||entry.getValue()== Direction.values()[(shield.getOrientation().ordinal()+1)%4]){
                                        System.out.println("Activate shield? 1 = yes");
                                        if (scanner.nextInt() == 1){
                                                shield.activate();
                                        }
                                }else{
                                        if(entry.getValue()==Direction.UP||entry.getValue()==Direction.DOWN){
                                                player.getShipManager().removeComponentTile(index,line);
                                        }else {
                                                player.getShipManager().removeComponentTile(line,index);
                                        }
                                }
                        }else {//il proiettile è di tipo big quindi subisci danno in ogni caso
                                if(entry.getValue()==Direction.UP||entry.getValue()==Direction.DOWN){
                                        player.getShipManager().removeComponentTile(index,line);
                                }else {
                                        player.getShipManager().removeComponentTile(line,index);
                                }
                        }
                }


        }

        @Override
        public void play() {
                if (super.getPartecipants().isPresent()) {
                        List<Player> players = super.getPartecipants().stream().toList();
                        if (!players.isEmpty()) {
                                // La lista contiene elementi, procedi
                                for (Player player : players) {

                                        // Fai qualcosa con ogni player
                                        if (player.getShipManager().calculateFirePower() < super.getFirePowerRequired()){
                                                attack(player);
                                        }else{
                                                System.out.println("Do you want to take the reward? 1 = yes");
                                                if(scanner.nextInt() == 1){
                                                        giveCreditReward(super.getCreditReward(),player);
                                                        applyFlightDayPenalty((Integer) super.getFlightDayPenalty().orElse(0), player);                                                        //add credits and apply flight day penalty
                                                }else{/*no*/

                                                        break;
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
                //metodo che fa andare indietro il giocatore
                super.getDeck().getGameManager().movePlayerBackwords(penalty,player.getPlayerID());
        }

        @Override
        public void giveCreditReward(int reward, Player player) {
                player.addCredits(reward);
        }

}

