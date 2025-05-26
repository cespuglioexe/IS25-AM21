package it.polimi.it.galaxytrucker.adventurecards;

import it.polimi.it.galaxytrucker.model.adventurecards.cards.StarDust;
import it.polimi.it.galaxytrucker.model.adventurecards.cardstates.EndState;
import it.polimi.it.galaxytrucker.model.crewmates.Alien;
import it.polimi.it.galaxytrucker.model.crewmates.Human;
import it.polimi.it.galaxytrucker.model.managers.FlightBoard;
import it.polimi.it.galaxytrucker.model.managers.FlightBoardFlightRules;
import it.polimi.it.galaxytrucker.model.managers.Player;
import it.polimi.it.galaxytrucker.model.managers.ShipManager;
import it.polimi.it.galaxytrucker.model.utility.AlienType;
import it.polimi.it.galaxytrucker.model.utility.Color;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class StarDustTest {

    private StarDust card;
    private final int gameLevel = 2;

    private Player player1;
    private Player player2;
    private Player player3;
    private Player player4;

    private FlightBoard flightBoard;


    @BeforeEach
    void initializeParameters() {
        player1 = new Player(UUID.randomUUID(), "Margarozzo", Color.BLUE, new ShipManager(gameLevel, Color.BLUE));
        player2 = new Player(UUID.randomUUID(), "Ing. Conti", Color.RED, new ShipManager(gameLevel, Color.BLUE));
        player3 = new Player(UUID.randomUUID(), "D'Abate", Color.YELLOW, new ShipManager(gameLevel, Color.BLUE));
        player4 = new Player(UUID.randomUUID(), "Balzarini", Color.GREEN, new ShipManager(gameLevel, Color.BLUE));

        flightBoard = new FlightBoard(gameLevel);

        flightBoard.addPlayerMarker(player1);
        flightBoard.addPlayerMarker(player2);
        flightBoard.addPlayerMarker(player3);
        flightBoard.addPlayerMarker(player4);

        createShip(player1);
        createShip(player2);
        createShip(player3);
        createShip(player4);

        card = new StarDust(new FlightBoardFlightRules(flightBoard));
    }

    private void createShip(Player player) {
        ShipManager ship = player.getShipManager();
        Human human = new Human();
        Alien alien = new Alien(AlienType.BROWNALIEN); // +2 Engine Power
        /*
         *     4  5  6  7  8  9  10
         * 5        [ ]   [ ]
         * 6     [ ][ ][ ][ ][ ]
         * 7  [ ][ ][ ][x][ ][ ][ ]
         * 8  [ ][ ][ ][ ][ ][ ][ ]
         * 9  [ ][ ][ ]   [ ][ ][ ]
         *
         *
         *
         * Where x stands for CentralCabin which has all TileEdge.UNIVERSAL connectors
         */
     /*
        ship.addComponentTile(6, 7, new CabinModule(List.of(TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE)));


        ship.addCrewmate(6, 7, human);
        ship.addCrewmate(6, 7, human);
        */
    }

    @Test
    void activateCard() {

        flightBoard.printFlightBoardState();

        int positionPreCard1= flightBoard.getPlayerPosition().get(player1);
        int positionPreCard2= flightBoard.getPlayerPosition().get(player2);
        int positionPreCard3= flightBoard.getPlayerPosition().get(player3);
        int positionPreCard4= flightBoard.getPlayerPosition().get(player4);

        card.play();

        assertEquals(EndState.class, card.getCurrentState().getClass());
        assertNotEquals(positionPreCard1, flightBoard.getPlayerPosition().get(player1));
        assertNotEquals(positionPreCard2, flightBoard.getPlayerPosition().get(player2));
        assertNotEquals(positionPreCard3, flightBoard.getPlayerPosition().get(player3));
        assertNotEquals(positionPreCard4, flightBoard.getPlayerPosition().get(player4));

        flightBoard.printFlightBoardState();

    }


}
