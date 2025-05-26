package it.polimi.it.galaxytrucker.adventurecards;

import it.polimi.it.galaxytrucker.model.adventurecards.cardstates.openspace.CalculateEnginePowerState;
import it.polimi.it.galaxytrucker.model.adventurecards.cards.OpenSpace;
import it.polimi.it.galaxytrucker.model.adventurecards.cardstates.EndState;
import it.polimi.it.galaxytrucker.model.componenttiles.*;
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

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class OpenSpaceTest {
    private OpenSpace card;
    private final int gameLevel = 2;

    private Player player1;
    private Player player2;
    private Player player3;
    private Player player4;

    private FlightBoard flightBoard;
    HashMap<List<Integer>,List<Integer>> doubleEngine;

    @BeforeEach
    void initializeParameters() {
        player1 = new Player(UUID.randomUUID(), "Margarozzo", Color.BLUE, new ShipManager(gameLevel, Color.BLUE));
        player2 = new Player(UUID.randomUUID(), "Ing. Conti", Color.RED, new ShipManager(gameLevel, Color.BLUE));
        player3 = new Player(UUID.randomUUID(), "D'Abate", Color.YELLOW, new ShipManager(gameLevel, Color.BLUE));
        player4 = new Player(UUID.randomUUID(), "Balzarini", Color.GREEN, new ShipManager(gameLevel, Color.BLUE));

        flightBoard = new FlightBoard(gameLevel);

        doubleEngine = new HashMap<List<Integer>,List<Integer>>();

        flightBoard.addPlayerMarker(player1);
        flightBoard.addPlayerMarker(player2);
        flightBoard.addPlayerMarker(player3);
        flightBoard.addPlayerMarker(player4);

        createShip(player1);
        createShip(player2);
        createShip(player3);
        createShip(player4);

        card = new OpenSpace(new FlightBoardFlightRules(flightBoard));

        card.play();
    }

    private void createShip(Player player) {
        ShipManager ship = player.getShipManager();
        Human human = new Human();
        Alien alien = new Alien(AlienType.BROWNALIEN); // +2 Engine Power
        /*
         *     4  5  6  7  8  9  10
         * 5        [ ]   [ ]
         * 6     [][ ][c][ ][ ]
         * 7  [B][l][c][x][c][ ][B]
         * 8  [ ][ ][ ][ ][c][][ ]
         * 9  [D][P][ ]   [P][D][D]
         *
         * Where c stands for CabinModule
         * Where l stands for LifeSupport
         * Where x stands for CentralCabin which has all TileEdge.UNIVERSAL connectors
         */
        ship.addComponentTile(6, 7, new CabinModule(List.of(TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE), null));
        ship.addComponentTile(7, 6, new CabinModule(List.of(TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE), null));
        ship.addComponentTile(7, 8, new CabinModule(List.of(TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE), null));
        ship.addComponentTile(8, 8, new CabinModule(List.of(TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE), null));


        ship.addComponentTile(9, 5, new SingleEngine(List.of(TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE), null));
        ship.addComponentTile(9, 8, new SingleEngine(List.of(TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE), null));

        ship.addComponentTile(7, 4, new BatteryComponent(2,List.of(TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE), null));
        ship.addComponentTile(7, 10, new BatteryComponent(2,List.of(TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE), null));

        ship.addComponentTile(9, 4, new DoubleEngine(List.of(TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE), null));
        ship.addComponentTile(9, 10, new DoubleEngine(List.of(TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE), null));
        ship.addComponentTile(9, 9, new DoubleEngine(List.of(TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE), null));




        ship.addComponentTile(7, 5, new LifeSupport(AlienType.BROWNALIEN, List.of(TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE), null));

        ship.addCrewmate(6, 7, human);
        ship.addCrewmate(6, 7, human);
        ship.addCrewmate(7, 8, human);
        ship.addCrewmate(7, 8, human);
        ship.addCrewmate(8, 8, human);
        ship.addCrewmate(8, 8, human);

        ship.addCrewmate(7, 6, alien);
    }

    @Test
    void playersNotReadyToTravel() {

        assertEquals(CalculateEnginePowerState.class, card.getCurrentState().getClass());

        flightBoard.printFlightBoardState();

        int positionPreCard1= flightBoard.getPlayerPosition().get(player1);
        int positionPreCard2= flightBoard.getPlayerPosition().get(player2);
        int positionPreCard3= flightBoard.getPlayerPosition().get(player3);
        int positionPreCard4= flightBoard.getPlayerPosition().get(player4);


        card.selectEngine(player1,doubleEngine);
        card.selectEngine(player2,doubleEngine);
        card.selectEngine(player3,doubleEngine);

        assertNotEquals(EndState.class, card.getCurrentState().getClass());

    }


    @Test
    void playersTravel() {

        assertEquals(CalculateEnginePowerState.class, card.getCurrentState().getClass());

        flightBoard.printFlightBoardState();

        int positionPreCard1= flightBoard.getPlayerPosition().get(player1);
        int positionPreCard2= flightBoard.getPlayerPosition().get(player2);
        int positionPreCard3= flightBoard.getPlayerPosition().get(player3);
        int positionPreCard4= flightBoard.getPlayerPosition().get(player4);


        card.selectEngine(player1,doubleEngine);
        card.selectEngine(player2,doubleEngine);
        card.selectEngine(player3,doubleEngine);
        card.selectEngine(player4,doubleEngine);


        assertEquals(EndState.class, card.getCurrentState().getClass());

        flightBoard.printFlightBoardState();

        assertNotEquals(positionPreCard1, flightBoard.getPlayerPosition().get(player1));
        assertNotEquals(positionPreCard2, flightBoard.getPlayerPosition().get(player2));
        assertNotEquals(positionPreCard3, flightBoard.getPlayerPosition().get(player3));
        assertNotEquals(positionPreCard4, flightBoard.getPlayerPosition().get(player4));
    }




    @Test
    void playersChooseDoubleEngineAndTravel() {

        assertEquals(CalculateEnginePowerState.class, card.getCurrentState().getClass());

        flightBoard.printFlightBoardState();

        int positionPreCard1= flightBoard.getPlayerPosition().get(player1);
        int positionPreCard2= flightBoard.getPlayerPosition().get(player2);
        int positionPreCard3= flightBoard.getPlayerPosition().get(player3);
        int positionPreCard4= flightBoard.getPlayerPosition().get(player4);


        doubleEngine.put(List.of(9,4),List.of(7,4));
        card.selectEngine(player1,doubleEngine);
        doubleEngine.clear();
        doubleEngine.put(List.of(9,4),List.of(7,4));
        card.selectEngine(player2,doubleEngine);
        doubleEngine.clear();
        doubleEngine.put(List.of(9,4),List.of(7,4));
        doubleEngine.put(List.of(9,10),List.of(7,4));
        card.selectEngine(player3,doubleEngine);
        doubleEngine.clear();
        doubleEngine.put(List.of(9,10),List.of(7,4));
        card.selectEngine(player4,doubleEngine);
        doubleEngine.clear();


        assertEquals(EndState.class, card.getCurrentState().getClass());

        flightBoard.printFlightBoardState();

        assertNotEquals(positionPreCard1, flightBoard.getPlayerPosition().get(player1));
        assertNotEquals(positionPreCard2, flightBoard.getPlayerPosition().get(player2));
        assertNotEquals(positionPreCard3, flightBoard.getPlayerPosition().get(player3));
        assertNotEquals(positionPreCard4, flightBoard.getPlayerPosition().get(player4));
    }

    @Test
    void playerChooseDoubleEngineAndActivateInvalidBatteryComponentAndTravel() {

        assertEquals(CalculateEnginePowerState.class, card.getCurrentState().getClass());

        flightBoard.printFlightBoardState();

        int positionPreCard1= flightBoard.getPlayerPosition().get(player1);
        int positionPreCard2= flightBoard.getPlayerPosition().get(player2);
        int positionPreCard3= flightBoard.getPlayerPosition().get(player3);
        int positionPreCard4= flightBoard.getPlayerPosition().get(player4);

        doubleEngine.put(List.of(9,4),List.of(7,4));
        card.selectEngine(player1,doubleEngine);
        doubleEngine.clear();
        doubleEngine.put(List.of(9,4),List.of(7,4));
        card.selectEngine(player2,doubleEngine);
        doubleEngine.clear();
        doubleEngine.put(List.of(9,4),List.of(7,4));
        doubleEngine.put(List.of(9,10),List.of(7,4));
        doubleEngine.put(List.of(9,9),List.of(7,4));
        //Zero Battery in the component
        assertThrows(Exception.class, () -> card.selectEngine(player3, doubleEngine));
        doubleEngine.clear();
        doubleEngine.put(List.of(9,10),List.of(7,4));
        doubleEngine.put(List.of(9,10),List.of(7,6));
        //Invalid Battery posiition
        assertThrows(Exception.class, () -> card.selectEngine(player4, doubleEngine));
        doubleEngine.clear();

        // Until players have sent correct coordinates, the card doesn't change status
        assertEquals(CalculateEnginePowerState.class, card.getCurrentState().getClass());

        flightBoard.printFlightBoardState();

        assertEquals(positionPreCard1, flightBoard.getPlayerPosition().get(player1));
        assertEquals(positionPreCard2, flightBoard.getPlayerPosition().get(player2));
        assertEquals(positionPreCard3, flightBoard.getPlayerPosition().get(player3));
        assertEquals(positionPreCard4, flightBoard.getPlayerPosition().get(player4));
    }
}
