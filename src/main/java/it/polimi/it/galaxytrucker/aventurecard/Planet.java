package it.polimi.it.galaxytrucker.aventurecard;

import it.polimi.it.galaxytrucker.cardEffects.CargoReward;
import it.polimi.it.galaxytrucker.cardEffects.FlightDayPenalty;
import it.polimi.it.galaxytrucker.cardEffects.Participation;
import it.polimi.it.galaxytrucker.managers.Player;
import it.polimi.it.galaxytrucker.utility.Cargo;

import java.util.*;

public class Planet extends AdventureCard implements Participation, CargoReward, FlightDayPenalty {
    private HashMap<Integer, Set<Cargo>> planets = new HashMap<Integer, Set<Cargo>>();
    private HashMap<Integer, Player> occupiedPlanets = new HashMap<Integer, Player>();
    private CargoManager manager;



    public Planet(Optional<List<Player>> partecipants, Optional<Integer> penalty, Optional<Integer> flightDayPenalty, Optional<Cargo> reward,HashMap<Integer, Set<Cargo>> planets, int firePower,int creditReward, AdventureDeck deck, CargoManager manager) {
        super(partecipants, penalty, flightDayPenalty, reward,firePower, creditReward, deck);
        this.planets = planets;
        this.manager = manager;
    }


    @Override
    public void giveCargoReward(Set<Cargo> reward,Player player) {
        manager.manageCargoAddition(reward, player);
    }

    @Override
    public void applyFlightDayPenalty(int penalty, Player player) {
       super.getDeck().getGameManager().getFlightBoardState().movePlayerBackwards(penalty, player.getPlayerID());
    }

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

    //TODO : Metodi della carta
    @Override
    public void play() {
        System.out.println("------------------------Planets-------------------------");

        List<Player> players = (List<Player>) super.getPartecipants().orElse(Collections.emptyList());
        if (!players.isEmpty()) {

            for (Player player : players) {

                if(partecipate(player) == true){

                    System.out.println("Available Planets:");
                    for (Map.Entry<Integer, Set<Cargo>> map : planets.entrySet()) {
                        Set<Cargo> values = map.getValue();
                        if(!occupiedPlanets.containsKey(map.getKey())){
                            System.out.print("Planet " + map.getKey() + "   Cargo: ");
                            for(Cargo cargo : values){
                                System.out.print(" " + cargo.getColor());
                            }
                        }
                    }

                    try {
                        System.out.println("Which planets do you choose?");
                        Scanner scanner = new Scanner(System.in);
                        int choice = scanner.nextInt();
                        if(occupiedPlanets.get(choice) == null){
                           occupiedPlanets.put(choice, player);
                           giveCargoReward(planets.get(choice), player);
                           applyFlightDayPenalty((int)super.getFlightDayPenalty().orElse(0),player);
                        }else{
                            System.out.println("Invalid Choice");
                        }
                    } catch(InputMismatchException e){
                        System.out.println("Invalid Choice");
                    }

                }

            }
        } else {

            System.out.println("No player can play this card");
        }

    }

}
