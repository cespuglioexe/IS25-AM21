package adventureCards;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.UUID;

import it.polimi.it.galaxytrucker.model.adventurecards.refactored.Epidemic;
import it.polimi.it.galaxytrucker.model.componenttiles.CabinModule;
import it.polimi.it.galaxytrucker.model.componenttiles.LifeSupport;
import it.polimi.it.galaxytrucker.model.componenttiles.TileEdge;
import it.polimi.it.galaxytrucker.model.crewmates.Alien;
import it.polimi.it.galaxytrucker.model.crewmates.Human;
import it.polimi.it.galaxytrucker.model.managers.FlightBoard;
import it.polimi.it.galaxytrucker.model.managers.FlightBoardFlightRules;
import it.polimi.it.galaxytrucker.model.managers.Player;
import it.polimi.it.galaxytrucker.model.managers.ShipManager;
import it.polimi.it.galaxytrucker.model.utility.AlienType;
import it.polimi.it.galaxytrucker.model.utility.Color;

public class EpidemicTest {
    private Epidemic card;

    private final int gameLevel = 2;

    private Player player1;
    private Player player2;
    private Player player3;
    private Player player4;

    private FlightBoard flightBoard;

    void initializeParameters() {
        player1 = new Player(UUID.randomUUID(), "Margarozzo", Color.BLUE, new ShipManager(gameLevel));
        player2 = new Player(UUID.randomUUID(), "Ing. Conti", Color.RED, new ShipManager(gameLevel));
        player3 = new Player(UUID.randomUUID(), "D'Abate", Color.YELLOW, new ShipManager(gameLevel));
        player4 = new Player(UUID.randomUUID(), "Balzarini", Color.GREEN, new ShipManager(gameLevel));

        flightBoard = new FlightBoard(gameLevel);

        flightBoard.addPlayerMarker(player1);
        flightBoard.addPlayerMarker(player2);
        flightBoard.addPlayerMarker(player3);
        flightBoard.addPlayerMarker(player4);
    }

    private void createShipsWithCentralCabin() {
        createShipWithCentralCabin(player1);
        createShipWithCentralCabin(player2);
        createShipWithCentralCabin(player3);
        createShipWithCentralCabin(player4);
    }
    private void createShipWithCentralCabin(Player player) {
        ShipManager ship = player.getShipManager();
        Human human = new Human();
        Alien alien = new Alien(AlienType.BROWNALIEN);
        /*
         *     4  5  6  7  8  9  10 
         * 5        [ ]   [c]      
         * 6     [ ][ ][c][ ][ ]   
         * 7  [ ][l][c][x][ ][ ][ ]
         * 8  [ ][ ][ ][ ][ ][ ][ ]
         * 9  [ ][ ][ ]   [ ][ ][ ]
         * 
         * Where c stands for CabinModule
         * Where l stands for LifeSupport
         * Where x stands for CentralCabin which has all TileEdge.UNIVERSAL connectors
         */
        ship.addComponentTile(6, 7, new CabinModule(List.of(TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE)));
        ship.addComponentTile(7, 6, new CabinModule(List.of(TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE)));
        ship.addComponentTile(5, 8, new CabinModule(List.of(TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE)));
        
        ship.addComponentTile(7, 5, new LifeSupport(AlienType.BROWNALIEN, List.of(TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE)));

        ship.addCrewmate(6, 7, human);
        ship.addCrewmate(6, 7, human);

        ship.addCrewmate(5, 8, human);
        ship.addCrewmate(5, 8, human);

        ship.addCrewmate(7, 6, alien);
    }

    private void createShipsWithoutCentralCabin() {
        createShipWithoutCentralCabin(player1);
        createShipWithoutCentralCabin(player2);
        createShipWithoutCentralCabin(player3);
        createShipWithoutCentralCabin(player4);
    }
    private void createShipWithoutCentralCabin(Player player) {
        ShipManager ship = player.getShipManager();
        Human human = new Human();
        Alien alien = new Alien(AlienType.BROWNALIEN);
        /*
         *     4  5  6  7  8  9  10 
         * 5        [ ]   [c]      
         * 6     [ ][ ][c][ ][ ]   
         * 7  [ ][l][c][ ][ ][ ][ ]
         * 8  [ ][ ][ ][ ][ ][ ][ ]
         * 9  [ ][ ][ ]   [ ][ ][ ]
         * 
         * Where c stands for CabinModule
         * Where l stands for LifeSupport
         * Where x stands for CentralCabin which has all TileEdge.UNIVERSAL connectors
         */
        ship.addComponentTile(6, 7, new CabinModule(List.of(TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE)));
        ship.addComponentTile(7, 6, new CabinModule(List.of(TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE)));
        ship.addComponentTile(5, 8, new CabinModule(List.of(TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE)));
        
        ship.addComponentTile(7, 5, new LifeSupport(AlienType.BROWNALIEN, List.of(TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE)));

        ship.addCrewmate(6, 7, human);
        ship.addCrewmate(6, 7, human);

        ship.addCrewmate(5, 8, human);
        ship.addCrewmate(5, 8, human);

        ship.addCrewmate(7, 6, alien);

        ship.removeComponentTile(7, 7);
    }

    private void createShipsWithoutCrewmates() {
        createShipWithoutCrewmates(player1);
        createShipWithoutCrewmates(player2);
        createShipWithoutCrewmates(player3);
        createShipWithoutCrewmates(player4);
    }
    private void createShipWithoutCrewmates(Player player) {
        ShipManager ship = player.getShipManager();
        /*
         *     4  5  6  7  8  9  10 
         * 5        [ ]   [c]      
         * 6     [ ][ ][c][ ][ ]   
         * 7  [ ][l][c][ ][ ][ ][ ]
         * 8  [ ][ ][ ][ ][ ][ ][ ]
         * 9  [ ][ ][ ]   [ ][ ][ ]
         * 
         * Where c stands for CabinModule
         * Where l stands for LifeSupport
         * Where x stands for CentralCabin which has all TileEdge.UNIVERSAL connectors
         */
        ship.addComponentTile(6, 7, new CabinModule(List.of(TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE)));
        ship.addComponentTile(7, 6, new CabinModule(List.of(TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE)));
        ship.addComponentTile(5, 8, new CabinModule(List.of(TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE)));
        
        ship.addComponentTile(7, 5, new LifeSupport(AlienType.BROWNALIEN, List.of(TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE)));
    }

    @Test
    void epidemicWithCentralCabinTest() {
        initializeParameters();
        createShipsWithCentralCabin();

        card = new Epidemic(new FlightBoardFlightRules(flightBoard));
        card.play();

        ShipManager ship = player1.getShipManager();

        assertEquals(3, ship.countCrewmates());
    }

    @Test
    void epidemicWithoutCentralCabinTest() {
        initializeParameters();
        createShipsWithoutCentralCabin();

        card = new Epidemic(new FlightBoardFlightRules(flightBoard));
        card.play();

        ShipManager ship = player1.getShipManager();

        assertEquals(5, ship.countCrewmates());
    }

    @Test
    void epidemicWithoutCrewmatesTest() {
        initializeParameters();
        createShipsWithoutCrewmates();

        card = new Epidemic(new FlightBoardFlightRules(flightBoard));
        card.play();

        ShipManager ship = player1.getShipManager();

        assertEquals(0, ship.countCrewmates());
    }
}
