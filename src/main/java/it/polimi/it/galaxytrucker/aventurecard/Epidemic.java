package it.polimi.it.galaxytrucker.aventurecard;

import it.polimi.it.galaxytrucker.cardEffects.CrewmatePenalty;
import it.polimi.it.galaxytrucker.componenttiles.CabinModule;
import it.polimi.it.galaxytrucker.componenttiles.CentralCabin;
import it.polimi.it.galaxytrucker.managers.Player;
import it.polimi.it.galaxytrucker.utility.Cargo;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class Epidemic extends AdventureCard {

    public Epidemic(Optional<Integer> penalty, Optional<Integer> flightDayPenalty, Optional<Cargo> reward, int firePower, int creditReward) {
        super(penalty, flightDayPenalty, reward,firePower, creditReward);
    }

    public void setPlayer(List<Player> partecipants) {
        super.setPartecipants(partecipants);
    }

    public void HumanRemove(Player player) {
        Set<List<Integer>> SetPositionCentre = player.getShipManager().getAllComponentsPositionOfType(CentralCabin.class);

        List<Integer> positionCentre = SetPositionCentre.stream()
                .flatMap(List::stream)
                .collect(Collectors.toList());

        for (List<Integer> position :  player.getShipManager().getAllComponentsPositionOfType(CabinModule.class)) {
            if(position.get(0) == positionCentre.get(0)+1 || position.get(0) == positionCentre.get(0)-1 || position.get(1) == positionCentre.get(1)+1 || position.get(1) == positionCentre.get(1)-1 ){
                player.getShipManager().removeCrewmate(position.get(0), position.get(1));
                player.getShipManager().removeCrewmate(positionCentre.get(0), positionCentre.get(1));}
            for (List<Integer> support :  player.getShipManager().getAllComponentsPositionOfType(CabinModule.class)) {
                if(position.get(0) == support.get(0)+1 || position.get(0) == support.get(0)-1 || position.get(1) == support.get(1)+1 || position.get(1) == support.get(1)-1 ){
                    player.getShipManager().removeCrewmate(position.get(0), position.get(1));
                    player.getShipManager().removeCrewmate(support.get(0), support.get(1));
                }
            }
        }
    }

    /*
    @Override
    public void play() {
        System.out.println("------------------------Epidemic--------------------------");

        List<Player> players = (List<Player>) super.getPartecipants().stream().toList();
        if (!players.isEmpty()) {

           for (Player player : players) {
               Set<List<Integer>> SetPositionCentre = player.getShipManager().getAllComponentsPositionOfType(CentralCabin.class);

               List<Integer> positionCentre = SetPositionCentre.stream()
                                                                .flatMap(List::stream)
                                                                .collect(Collectors.toList());

               for (List<Integer> position :  player.getShipManager().getAllComponentsPositionOfType(CabinModule.class)) {
                   if(position.get(0) == positionCentre.get(0)+1 || position.get(0) == positionCentre.get(0)-1 || position.get(1) == positionCentre.get(1)+1 || position.get(1) == positionCentre.get(1)-1 ){
                       player.getShipManager().removeCrewmate(position.get(0), position.get(1));
                       player.getShipManager().removeCrewmate(positionCentre.get(0), positionCentre.get(1));}
                   for (List<Integer> support :  player.getShipManager().getAllComponentsPositionOfType(CabinModule.class)) {
                       if(position.get(0) == support.get(0)+1 || position.get(0) == support.get(0)-1 || position.get(1) == support.get(1)+1 || position.get(1) == support.get(1)-1 ){
                           player.getShipManager().removeCrewmate(position.get(0), position.get(1));
                           player.getShipManager().removeCrewmate(support.get(0), support.get(1));
                       }
                   }
               }
           }
        } else {
            System.out.println("No player can play this card");
        }
    }
    */

}
