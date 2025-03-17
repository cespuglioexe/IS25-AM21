package it.polimi.it.galaxytrucker.aventurecard;

import it.polimi.it.galaxytrucker.cardEffects.CreditReward;
import it.polimi.it.galaxytrucker.cardEffects.CrewmatePenalty;
import it.polimi.it.galaxytrucker.cardEffects.FlightDayPenalty;
import it.polimi.it.galaxytrucker.cardEffects.Participation;
import it.polimi.it.galaxytrucker.managers.FlightBoardState;
import it.polimi.it.galaxytrucker.managers.Player;
import it.polimi.it.galaxytrucker.utility.Cargo;

import java.util.*;

public class AbandonedShip extends AdventureCard implements Participation, CreditReward, FlightDayPenalty, CrewmatePenalty {

    public final int MAX_PARTICIPATIONS;

    public AbandonedShip(Optional<Integer> penalty, Optional<Integer> flightDayPenalty, Optional<Integer> reward, int firePower, int creditReward) {
        super(penalty, flightDayPenalty, reward,firePower, creditReward);
        MAX_PARTICIPATIONS = 1;
    }


    public void setPlayer(List<Player> partecipants) {
        super.setPartecipants(partecipants);
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
        player.getShipManager().removeCrewmate(position[0], position[1]);
        */
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
        if (player.getShipManager().countCrewmates() > nMin) {
            return true;
        }
        return false;
    }


    //// View
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
     * Mi arrivano i giocatori già ordinati per ordine di FlightBoard (io aggiungo i partecipanti alla carta con setPlayer()):
     * Controllo che alla carta partecipi un Player e basta con il getter alla variabile final MAX_PARTICIPATION (choice = 0, if choice==0 ecc...)
     * Il controller chiede alla view se vuole partecipare un determinato partecipante
     * Di quel player viene calcolato se ha raggiunto il numero di umani richiesto
     * Applico le penalty o reward specifiche della carta
     * */
/*
    @Override
    public void play() {
        System.out.println("------------------------Abandoned Ship--------------------------");

        List<Player> players = (List<Player>) super.getPartecipants().get(1);
        if (!players.isEmpty()) {
            int choise = 0;
            for (Player player : players) {

                if(choise==0) {
                    int nMin = (int)super.getPenalty().orElse(0);
                    if (player.getShipManager().countCrewmates() > nMin) {

                        System.out.println("Minimum number of crewmates:  " + nMin);
                        System.out.print("Credits: " + getCreditReward());

                        if (partecipate(player) == true) {
                            giveCreditReward(getCreditReward(), player);
                            applyFlightDayPenalty((int) super.getFlightDayPenalty().orElse(0), player);
                            applyCrewmatePenalty((int) super.getPenalty().orElse(0), player);
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
