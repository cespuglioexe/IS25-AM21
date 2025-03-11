package it.polimi.it.galaxytrucker.aventurecard;


import it.polimi.it.galaxytrucker.cardEffects.CrewmatePenalty;
import it.polimi.it.galaxytrucker.cardEffects.FlightDayPenalty;
import it.polimi.it.galaxytrucker.crewmates.Crewmate;
import it.polimi.it.galaxytrucker.managers.Player;

import java.util.List;
import java.util.Optional;

public class CombatZone extends Attack implements FlightDayPenalty, CrewmatePenalty{

    public CombatZone(Optional partecipants, Optional penalty, Optional flightDayPenalty, Optional reward, int firePowerRequired, int creditReward, AdventureDeck adventureDeck) {
        super(partecipants, penalty, flightDayPenalty, reward, firePowerRequired, creditReward,adventureDeck);
    }

    @Override
    public void attack() {

    }

    @Override
    public void applyCrewmatePenalty(int penalty, Player player) {

    }

    @Override
    public void applyFlightDayPenalty(int penalty, Player player) {

    }

    @Override
    public void play() {

        List<Player> players = super.getPartecipants().stream().toList();

        //controllo equipaggio
        if(players.get(0).getShipManager().calculateCrewmates(players.get(0).getPlayerID()) < players.get(1).getShipManager().calculateCrewmates(players.get(1).getPlayerID())){

        }else{

        }

        //controllo potenza motrice
        if(players.get(0).getShipManager().calculateEnginePower(players.get(0).getPlayerID())< players.get(1).getShipManager().calculateEnginePower(players.get(1).getPlayerID())){

        }else{
            applyCrewmatePenalty((Integer) super.getCrewmatePenalty().orElse(0),players.get(1));
        }


        //controllo potenza di fuoco
        if(players.get(0).getShipManager().calculateFirePower(players.get(0).getPlayerID())< players.get(1).getShipManager().calculateEnginePower(players.get(1).getPlayerID())){

        }else{

        }
    }




}
