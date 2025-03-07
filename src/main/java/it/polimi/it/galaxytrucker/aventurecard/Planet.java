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



    public Planet(Optional<List<Player>> partecipants, Optional<Integer> penalty, Optional<Integer> flightDayPenalty, Optional<Integer> reward,HashMap<Integer, Set<Cargo>> planets) {
        super(partecipants, penalty, flightDayPenalty, reward);
        this.planets = planets;




    }


    @Override
    public void giveReward(Cargo reward) {

    }

    @Override
    public void applyPenalty(Integer penalty) {
        // Numero giorni di viaggio
    }

    @Override
    public boolean partecipate(Player player) {
        System.out.println("Do you want to partecipate");

    }

    //TODO : Metodi della carta
    @Override
    public void play() {

    }


}
