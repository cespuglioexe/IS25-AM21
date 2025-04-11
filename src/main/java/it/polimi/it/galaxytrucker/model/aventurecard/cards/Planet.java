package it.polimi.it.galaxytrucker.model.aventurecard.cards;

import it.polimi.it.galaxytrucker.model.aventurecard.AdventureCard;
import it.polimi.it.galaxytrucker.model.cardEffects.CargoReward;
import it.polimi.it.galaxytrucker.model.cardEffects.FlightDayPenalty;
import it.polimi.it.galaxytrucker.model.cardEffects.Participation;
import it.polimi.it.galaxytrucker.model.exceptions.InvalidActionException;
import it.polimi.it.galaxytrucker.model.exceptions.NotFoundException;
import it.polimi.it.galaxytrucker.model.managers.CargoManager;
import it.polimi.it.galaxytrucker.model.managers.FlightBoard;
import it.polimi.it.galaxytrucker.model.managers.Player;
import it.polimi.it.galaxytrucker.model.utility.Cargo;

import java.util.*;

public class Planet extends AdventureCard implements Participation, CargoReward, FlightDayPenalty {
    private HashMap<Integer, Set<Cargo>> planets = new HashMap<Integer, Set<Cargo>>();
    private HashMap<Integer, Player> occupiedPlanets = new HashMap<Integer, Player>();
    private CargoManager manager;


    public Planet(Optional<Integer> penalty, Optional<Integer> flightDayPenalty, Optional<Set<Cargo>> reward,HashMap<Integer, Set<Cargo>> planets, int firePower,int creditReward,CargoManager manager) {
        super(penalty, flightDayPenalty, reward,firePower, creditReward);
        this.planets = planets;
        this.manager = manager;
    }


    public HashMap<Integer, Player> getOccupiedPlanets() {
        return occupiedPlanets;
    }

    public void setPlayer(List<Player> partecipants){
        int i=0;
        for(Player p : partecipants){
            occupiedPlanets.put(i,p);
            i++;
        }
        super.setPartecipants(partecipants);
    }


    @Override
    public void giveCargoReward(Player player) throws NotFoundException {
        int key=-1;
        for (Map.Entry<Integer, Player> map : occupiedPlanets.entrySet()) {
            if (map.getValue().equals(player)) {
                key = map.getKey();
            }
        }
        if(key==-1){
            throw new NotFoundException("Player not found");
        } else{
            for(Cargo cargo : planets.get(key)){
                manager.manageCargoAddition(cargo,player);
            }
        }
    }


    @Override
    public void applyFlightDayPenalty(FlightBoard board, Player player) {
        //board.movePlayerBackwards((int)getFlightDayPenalty().orElse(0), player.getPlayerID());
    }


    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void partecipate(Player player, int choice) throws InvalidActionException {
        if (isOccupied(choice)) {
            throw new InvalidActionException("The planet is already occupied");
        }
        occupiedPlanets.put(choice, player);
        updateState();
    }

    private boolean isOccupied(int planet) {
        if (occupiedPlanets.keySet().contains(planet)) {
            return true;
        }
        return false;
    }

    @Override
    public void decline(Player player) {
        updateState();
    }

    @Override
    public List<Integer> getSlots(){
        return planets.keySet().stream().toList();
    }

	@Override
    public Set<Cargo> getRewards(){
           return (Set<Cargo>) super.getReward().orElse(0);
    }

}
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
