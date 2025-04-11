package it.polimi.it.galaxytrucker.aventurecard.statesTest;

import java.util.HashMap;
import java.util.Optional;
import java.util.Set;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;

import it.polimi.it.galaxytrucker.model.aventurecard.cards.Planet;
import it.polimi.it.galaxytrucker.model.aventurecard.cardStates.EffectExecutionState;
import it.polimi.it.galaxytrucker.model.aventurecard.cardStates.ParticipationState;
import it.polimi.it.galaxytrucker.model.managers.CargoManager;
import it.polimi.it.galaxytrucker.model.managers.Player;
import it.polimi.it.galaxytrucker.model.utility.Cargo;
import it.polimi.it.galaxytrucker.model.utility.Color;

public class ParticipationTest {
    Planet planet;

    @BeforeEach
    void initializateCard() {
        CargoManager manager = new CargoManager();
        HashMap<Integer,Set<Cargo>> load = new HashMap<>();


        load.put(0,Set.of(new Cargo(Color.RED), new Cargo(Color.GREEN),new Cargo(Color.BLUE),new Cargo(Color.BLUE),new Cargo(Color.BLUE)));
        load.put(1,Set.of(new Cargo(Color.RED), new Cargo(Color.YELLOW),new Cargo(Color.GREEN)));
        load.put(2,Set.of(new Cargo(Color.RED), new Cargo(Color.BLUE),new Cargo(Color.BLUE),new Cargo(Color.BLUE)));

        planet = new Planet(null,Optional.of(3),null,load,0,0,manager);
        planet.start(new ParticipationState());
    }

    @Test
    void playerParticipateFullPlanetTest() {
        Player player1=new Player(UUID.randomUUID(), "Margarozzo1",0, Color.RED);
        Player player2=new Player(UUID.randomUUID(), "Margarozzo2",0, Color.BLUE);
        Player player3=new Player(UUID.randomUUID(), "Margarozzo3",0, Color.GREEN);
        Player player4=new Player(UUID.randomUUID(), "Margarozzo4",0, Color.YELLOW);

        planet.setPlayersInOrder(List.of(player2, player1, player3, player4));

        planet.partecipate(player1, 1);

        assertEquals(ParticipationState.class, planet.getCurrentState().getClass());

        planet.partecipate(player2, 0);

        assertEquals(ParticipationState.class, planet.getCurrentState().getClass());

        planet.partecipate(player3, 2);

        assertEquals(EffectExecutionState.class, planet.getCurrentState().getClass());
    }

    @Test
    void playerParticipateWithDeclineTest() {
        Player player1=new Player(UUID.randomUUID(), "Margarozzo1",0, Color.RED);
        Player player2=new Player(UUID.randomUUID(), "Margarozzo2",0, Color.BLUE);
        Player player3=new Player(UUID.randomUUID(), "Margarozzo3",0, Color.GREEN);
        Player player4=new Player(UUID.randomUUID(), "Margarozzo4",0, Color.YELLOW);

        planet.setPlayersInOrder(List.of(player2, player1, player3, player4));

        planet.decline(player1);
        planet.decline(player2);
        planet.decline(player3);
        planet.partecipate(player4, 2);

        assertEquals(EffectExecutionState.class, planet.getCurrentState().getClass());
    }

    @Test
    void playerParticipateAllDeclineTest() {
        Player player1=new Player(UUID.randomUUID(), "Margarozzo1",0, Color.RED);
        Player player2=new Player(UUID.randomUUID(), "Margarozzo2",0, Color.BLUE);
        Player player3=new Player(UUID.randomUUID(), "Margarozzo3",0, Color.GREEN);
        Player player4=new Player(UUID.randomUUID(), "Margarozzo4",0, Color.YELLOW);

        planet.setPlayersInOrder(List.of(player2, player1, player3, player4));

        planet.decline(player1);
        planet.decline(player2);
        planet.decline(player3);
        planet.decline(player4);

        assertEquals(EffectExecutionState.class, planet.getCurrentState().getClass());
    }
}
