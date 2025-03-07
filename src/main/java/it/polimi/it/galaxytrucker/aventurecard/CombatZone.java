package it.polimi.it.galaxytrucker.aventurecard;

import it.polimi.it.galaxytrucker.cardEffects.CrewmatePenalty;
import it.polimi.it.galaxytrucker.cardEffects.FlightDayPenalty;
import it.polimi.it.galaxytrucker.crewmates.Crewmate;

import java.util.Optional;

public class CombatZone extends Attack implements CrewmatePenalty, FlightDayPenalty {
    final private int FIRSTFLYGHTDAYPENALTY = 3;
    final private int SECONDCREWMATEPENALTY = 2;

    public CombatZone(Optional partecipants, Optional penalty, Optional flightDayPenalty, Optional reward, Optional firePowerRequired) {
        super(partecipants, penalty, flightDayPenalty, reward, firePowerRequired);
    }

    @Override
    public void attack() {

    }

    @Override
    public void play() {
        //meno equipaggio dei due perde 3 giorni di volo
        if(player1.shipManager.calculateCrewmates < player2.shipManager.calculateCrewmates){
            player1
        }else{

        }

        //meno potenza motrice perde 2 membri equipaggio

        //meno potenza fuoco minacciato da una cannonata leggera e una pesante
    }

    @Override
    public void applyPenalty(Crewmate penalty) {

    }

    @Override
    public void applyPenalty(Integer penalty) {

    }
}
