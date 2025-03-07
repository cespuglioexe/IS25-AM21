package it.polimi.it.galaxytrucker.aventurecard;

import it.polimi.it.galaxytrucker.managers.Player;

import java.util.List;
import java.util.Optional;

public abstract class AdventureCard<T> {
    private Optional<Integer> penalty;
    private Optional<Integer> flightDayPenalty;
    private Optional<T> reward;
    private Optional<List<Player>> partecipants;

    public void play(){

    }
}
