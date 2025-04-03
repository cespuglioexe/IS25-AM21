package it.polimi.it.galaxytrucker.aventurecard;

import it.polimi.it.galaxytrucker.model.aventurecard.Planet;
import it.polimi.it.galaxytrucker.model.managers.CargoManager;
import it.polimi.it.galaxytrucker.model.managers.FlightBoardState;
import it.polimi.it.galaxytrucker.model.managers.Player;
import it.polimi.it.galaxytrucker.model.utility.Cargo;
import it.polimi.it.galaxytrucker.model.utility.Color;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class PlanetTest {

    @Test
    void setPlayer() {
        CargoManager manager = new CargoManager();
        HashMap<Integer,Set<Cargo>> load = new HashMap<>();


        load.put(1,Set.of(new Cargo(Color.RED), new Cargo(Color.GREEN),new Cargo(Color.BLUE),new Cargo(Color.BLUE),new Cargo(Color.BLUE)));
        load.put(2,Set.of(new Cargo(Color.RED), new Cargo(Color.YELLOW),new Cargo(Color.GREEN)));
        load.put(3,Set.of(new Cargo(Color.RED), new Cargo(Color.BLUE),new Cargo(Color.BLUE),new Cargo(Color.BLUE)));
        load.put(4,Set.of(new Cargo(Color.RED), new Cargo(Color.GREEN)));

        Planet planet = new Planet(null,Optional.of(3),null,load,0,0,manager);
        Player player1=new Player(new UUID(0,1), "Margarozzo1",0, Color.RED);
        Player player2=new Player(new UUID(0,2), "Margarozzo2",0, Color.BLUE);
        Player player3=new Player(new UUID(0,3), "Margarozzo3",0, Color.GREEN);
        Player player4=new Player(new UUID(0,4), "Margarozzo4",0, Color.YELLOW);

        List<Player> partecipants = new ArrayList<>();
        partecipants.add(player2);
        partecipants.add(player3);
        partecipants.add(player1);
        partecipants.add(player4);

        planet.setPlayer(partecipants);
        assertEquals(planet.getOccupiedPlanets().get(0),player2);
        assertEquals(planet.getOccupiedPlanets().get(1),player3);
        assertEquals(planet.getOccupiedPlanets().get(2),player1);
        assertEquals(planet.getOccupiedPlanets().get(3),player4);
    }

    @Disabled
    @Test
    void applyFlightDayPenalty() {

        CargoManager manager = new CargoManager();
        HashMap<Integer,Set<Cargo>> load = new HashMap<>();

        load.put(1,Set.of(new Cargo(Color.RED), new Cargo(Color.GREEN),new Cargo(Color.BLUE),new Cargo(Color.BLUE),new Cargo(Color.BLUE)));
        load.put(2,Set.of(new Cargo(Color.RED), new Cargo(Color.YELLOW),new Cargo(Color.GREEN)));
        load.put(3,Set.of(new Cargo(Color.RED), new Cargo(Color.BLUE),new Cargo(Color.BLUE),new Cargo(Color.BLUE)));
        load.put(4,Set.of(new Cargo(Color.RED), new Cargo(Color.GREEN)));


        Planet planet = new Planet(null,Optional.of(3),null,load,0,0,manager);
        Player player1=new Player(new UUID(0,1), "Margarozzo1",0, Color.RED);
        FlightBoardState board = new FlightBoardState(18);
        board.setBoard();
        board.addPlayerMarker(player1.getPlayerID(),1);

        planet.applyFlightDayPenalty(board,player1);
        assertEquals(board.getPlayerPosition().get(player1.getPlayerID()),1);
    }
}