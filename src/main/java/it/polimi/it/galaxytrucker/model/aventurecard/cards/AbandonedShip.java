package it.polimi.it.galaxytrucker.model.aventurecard.cards;

import it.polimi.it.galaxytrucker.model.aventurecard.AdventureCard;
import it.polimi.it.galaxytrucker.model.cardEffects.CreditReward;
import it.polimi.it.galaxytrucker.model.cardEffects.CrewmatePenalty;
import it.polimi.it.galaxytrucker.model.cardEffects.FlightDayPenalty;
import it.polimi.it.galaxytrucker.model.cardEffects.Participation;
import it.polimi.it.galaxytrucker.model.managers.FlightBoard;
import it.polimi.it.galaxytrucker.model.managers.Player;

import java.util.*;

public class AbandonedShip extends AdventureCard implements Participation, CreditReward, FlightDayPenalty, CrewmatePenalty {

    public boolean isTaken;

    public AbandonedShip(Optional<Integer> penalty, Optional<Integer> flightDayPenalty, Optional<Integer> reward, int firePower, int creditReward) {
        super(penalty, flightDayPenalty, reward,firePower, creditReward);
        isTaken = false;
    }

    public void setTaken(boolean taken) {
        isTaken = taken;
    }

    public boolean getIsTaken() {
        return isTaken;
    }

    public void setPlayer(List<Player> partecipants) {
        super.setPartecipants(partecipants);
    }

    @Override
    public void giveCreditReward(Player player) {
        player.addCredits((super.getCreditReward()));
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
    public void applyFlightDayPenalty(FlightBoard board, Player player) {
        //board.movePlayerBackwards((int)getFlightDayPenalty().orElse(0), player.getPlayerID());
    }

    public void RequiredHumanVerification(FlightBoard board) {
        for (Player player : (List<Player>)super.getPartecipants()){
            if (!getIsTaken()) {
                int nMin = (int) super.getPenalty().orElse(0);
                if (player.getShipManager().countCrewmates() >= nMin) {
                    applyFlightDayPenalty(board, player);
                    giveCreditReward(player);
                    applyCrewmatePenalty((int)super.getPenalty().orElse(0), player);
                    setTaken(true);
                }
            }
        }
    }



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

    @Override
    public void partecipate(Player player, int choice) {

    }

    @Override
    public void decline(Player player) {

    }

    @Override
    public List<Integer> getSlots() {
        return List.of();
    }

    @Override
    public Set getRewards() {
        return Set.of();
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
