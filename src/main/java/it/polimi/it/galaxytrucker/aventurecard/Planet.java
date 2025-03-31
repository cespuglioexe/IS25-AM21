package it.polimi.it.galaxytrucker.aventurecard;

import it.polimi.it.galaxytrucker.cardEffects.CargoReward;
import it.polimi.it.galaxytrucker.cardEffects.FlightDayPenalty;
import it.polimi.it.galaxytrucker.cardEffects.Participation;
import it.polimi.it.galaxytrucker.exceptions.NotFoundException;
import it.polimi.it.galaxytrucker.managers.FlightBoardState;
import it.polimi.it.galaxytrucker.managers.Player;
import it.polimi.it.galaxytrucker.utility.Cargo;

import java.util.*;

public class Planet extends AdventureCard implements Participation, CargoReward, FlightDayPenalty {
    private HashMap<Integer, Set<Cargo>> planets = new HashMap<UUID, Set<Cargo>>();
    private HashMap<UUID, Player> occupiedPlanets = new HashMap<UUID, Player>();
    private CargoManager manager;


    public Planet(Optional<Integer> penalty, Optional<Integer> flightDayPenalty, Optional<Set<Cargo>> reward,HashMap<UUID, Set<Cargo>> planets, int firePower,int creditReward,CargoManager manager) {
        super(penalty, flightDayPenalty, reward,firePower, creditReward);
        this.planets = planets;
        this.manager = manager;
    }



    // Proprietà che mi tiene conto delle scelte

    @Override
    public void getChoise(){

    }

    public HashMap<UUID, Player> getOccupiedPlanets() {
        return occupiedPlanets;
    }

    public void addPlayer(Player player){
        //planets.put(player.getPlayerID(),)

    }

    @Override
    public List<Integer> getSlots(){
        return planets.keySet().stream().toList();
    }
/*
    public void setPlayer(List<Player> partecipants){
        int i=0;
        for(Player p : partecipants){
            occupiedPlanets.put(i,p);
            i++;
        }
        super.setPartecipants(partecipants);
    }
*/

    @Override
    public void giveCargoReward(Player player) {
        UUID key= new UUID(0,-1);
        for (Map.Entry<UUID, Player> map : occupiedPlanets.entrySet()) {
            if (map.getValue().equals(player)) {
                key = map.getKey();
            }
        }
        if(key.compareTo(new UUID(0,-1))==0){
            new NotFoundException("Player not found");
        } else{
            for(Cargo cargo : planets.get(key)){
                manager.manageCargoAddition(cargo,player);
            }
        }
    }


    @Override
    public void applyFlightDayPenalty(FlightBoardState board, Player player) {
        board.movePlayerBackwards((int)getFlightDayPenalty().orElse(0), player.getPlayerID());
    }


    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////



/*
    @Override
    public void play() {
        System.out.println("------------------------Planets-------------------------");

        List<Player> players = (List<Player>) super.getPartecipants().orElse(Collections.emptyList());
        if (!players.isEmpty()) {

            for (Player player : players) {

                if (partecipate(player) == true) {

                    System.out.println("Available Planets:");
                    for (Map.Entry<Integer, Set<Cargo>> map : planets.entrySet()) {
                        Set<Cargo> values = map.getValue();
                        if (!occupiedPlanets.containsKey(map.getKey())) {
                            System.out.print("Planet " + map.getKey() + "   Cargo: ");
                            for (Cargo cargo : values) {
                                System.out.print(" " + cargo.getColor());
                            }
                        }
                    }

                    try {
                        System.out.println("Which planets do you choose?");
                        Scanner scanner = new Scanner(System.in);
                        int choice = scanner.nextInt();
                        if (occupiedPlanets.get(choice) == null) {
                            occupiedPlanets.put(choice, player);
                            giveCargoReward(planets.get(choice), player);
                            applyFlightDayPenalty((int) super.getFlightDayPenalty().orElse(0), player);
                        } else {
                            System.out.println("Invalid Choice");
                        }
                    } catch (InputMismatchException e) {
                        System.out.println("Invalid Choice");
                    }

                }

            }
        } else {

            System.out.println("No player can play this card");
        }

    }
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////


 */
}
