package it.polimi.it.galaxytrucker.adventurecards;

import it.polimi.it.galaxytrucker.model.adventurecards.cards.Smugglers;
import it.polimi.it.galaxytrucker.model.adventurecards.cardstates.smugglers.CalculateFirePowerState;
import it.polimi.it.galaxytrucker.model.adventurecards.cardstates.smugglers.CargoRewardState;
import it.polimi.it.galaxytrucker.model.adventurecards.cardstates.EndState;
import it.polimi.it.galaxytrucker.model.componenttiles.*;
import it.polimi.it.galaxytrucker.model.crewmates.Alien;
import it.polimi.it.galaxytrucker.model.crewmates.Human;
import it.polimi.it.galaxytrucker.model.managers.FlightBoard;
import it.polimi.it.galaxytrucker.model.managers.FlightBoardFlightRules;
import it.polimi.it.galaxytrucker.model.managers.Player;
import it.polimi.it.galaxytrucker.model.managers.ShipManager;
import it.polimi.it.galaxytrucker.model.utility.AlienType;
import it.polimi.it.galaxytrucker.model.utility.Cargo;
import it.polimi.it.galaxytrucker.model.utility.Color;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class SmugglersTest {

    private Smugglers card;
    private final List<Cargo> cargoReward = List.of(new Cargo(Color.RED), new Cargo(Color.YELLOW));
    private final int cargoPenalty = 2;
    private final int flightDayPenalty = 1;
    private final int requiredFirePower = 9;
    HashMap<List<Integer>,List<Integer>> doubleCannon;

    private final int gameLevel = 2;

    private Player player1;
    private Player player2;
    private Player player3;
    private Player player4;

    private FlightBoard flightBoard;


    @BeforeEach
    void initializeParameters() {
        player1 = new Player(UUID.randomUUID(), "Margarozzo1", Color.BLUE, new ShipManager(gameLevel));
        player2 = new Player(UUID.randomUUID(), "Ing. Conti", Color.RED, new ShipManager(gameLevel));
        player3 = new Player(UUID.randomUUID(), "D'Abate", Color.YELLOW, new ShipManager(gameLevel));
        player4 = new Player(UUID.randomUUID(), "Balzarini", Color.GREEN, new ShipManager(gameLevel));

        flightBoard = new FlightBoard(gameLevel);

        doubleCannon = new HashMap<List<Integer>,List<Integer>>();

        flightBoard.addPlayerMarker(player1);
        flightBoard.addPlayerMarker(player2);
        flightBoard.addPlayerMarker(player3);
        flightBoard.addPlayerMarker(player4);

        createShip(player1);
        createShip(player2);
        createShip(player3);
        createShip(player4);

        card = new Smugglers(requiredFirePower,cargoReward,cargoPenalty, flightDayPenalty, new FlightBoardFlightRules(flightBoard));

        card.play();
    }

    private void createShip(Player player) {
        ShipManager ship = player.getShipManager();
        Human human = new Human();
        Alien alien = new Alien(AlienType.PURPLEALIEN);
        /*
         *     4  5  6  7  8  9  10
         * 5        [ ]   [ ]
         * 6     [D][C][ ][C][ ]
         * 7  [C][l][c][x][ ][C][D]
         * 8  [ ][ ][ ][ ][ ][S][H]
         * 9  [ ][B][ ]   [ ][ ][ ]
         *
         * Where c stands for CabinModule
         * Where l stands for LifeSupport
         * Where x stands for CentralCabin which has all TileEdge.UNIVERSAL connectors
         */
        ship.addComponentTile(7, 6, new CabinModule(List.of(TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE)));

        ship.addComponentTile(8, 9, new SpecialCargoHold(2,List.of(TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE)));
        ship.addComponentTile(8, 10, new CargoHold(2,List.of(TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE)));


        ship.addComponentTile(7, 4, new SingleCannon(List.of(TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE)));
        ship.addComponentTile(6, 6, new SingleCannon(List.of(TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE)));
        ship.addComponentTile(6, 8, new SingleCannon(List.of(TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE)));
        ship.addComponentTile(7, 9, new SingleCannon(List.of(TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE)));

        ship.addComponentTile(7, 10, new DoubleCannon(List.of(TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE)));
        ship.addComponentTile(6, 5, new DoubleCannon(List.of(TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE)));
        ship.addComponentTile(9, 5, new BatteryComponent(2,List.of(TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE)));

        ship.addComponentTile(7, 5, new LifeSupport(AlienType.PURPLEALIEN, List.of(TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE)));

        ship.addCrewmate(7, 6, alien);
    }

    @Test
    void leaderPlaysTest() {
        assertEquals(CalculateFirePowerState.class, card.getCurrentState().getClass());
        assertNotEquals(player1.getShipManager().calculateFirePower(),0);
        assertEquals(card.getCurrentPlayer(), player1);
    }

    @Test
    void leaderLostZeroCargoPenaltyTest() {
        leaderPlaysTest();

        doubleCannon.put(List.of(7,10),List.of(9,5));
        card.selectCannons(doubleCannon);
        assertTrue(player1.getShipManager().getCargoPositon().get(Color.RED).isEmpty());
        assertTrue(player1.getShipManager().getCargoPositon().get(Color.YELLOW).isEmpty());
        assertTrue(player1.getShipManager().getCargoPositon().get(Color.BLUE).isEmpty());
        assertTrue(player1.getShipManager().getCargoPositon().get(Color.GREEN).isEmpty());
        BatteryComponent battery = (BatteryComponent) player1.getShipManager().getComponent(9,5).orElse(null);
        assertEquals(battery.getBatteryCapacity(),0);

        assertEquals(card.getCurrentPlayer(), player2);
        assertEquals(card.getCurrentPlayer(), player2);
        assertEquals(CalculateFirePowerState.class, card.getCurrentState().getClass());
    }

    @Test
    void leaderWonTest() {
        leaderPlaysTest();

        doubleCannon.put(List.of(7,10),List.of(9,5));
        doubleCannon.put(List.of(6,5),List.of(9,5));
        card.selectCannons(doubleCannon);
        BatteryComponent battery = (BatteryComponent) player1.getShipManager().getComponent(9,5).orElse(null);
        assertEquals(battery.getBatteryCapacity(),0);
        assertEquals(CargoRewardState.class, card.getCurrentState().getClass());
    }

    @Test
    void leaderAcceptRewardTest() {
        leaderWonTest();
        card.acceptCargo(0,8,9);
        card.acceptCargo(0,8,9);
        assertEquals(player1.getShipManager().getCargoPositon().get(Color.RED), Set.of(List.of(8,9)));
        assertEquals(player1.getShipManager().getCargoPositon().get(Color.YELLOW), Set.of(List.of(8,9)));

        assertEquals(EndState.class, card.getCurrentState().getClass());
    }

    @Test
    void leaderDeclineRewardTest() {
        leaderWonTest();
        int playerPrePosition = flightBoard.getPlayerPosition().get(player1);
        card.discardCargo(0);
        card.discardCargo(0);
        assertEquals(flightBoard.getPlayerPosition().get(player1), playerPrePosition);
        assertEquals(EndState.class, card.getCurrentState().getClass());
    } 


    @Test
    void leaderCargoPenaltyTest() {
        player1.getShipManager().addCargo(8,9,new Cargo(Color.RED));
        player1.getShipManager().addCargo(8,9,new Cargo(Color.BLUE));
        player1.getShipManager().addCargo(8,10,new Cargo(Color.BLUE));
        player1.getShipManager().addCargo(8,10,new Cargo(Color.YELLOW));

        leaderPlaysTest();

        doubleCannon.put(List.of(7,10),List.of(9,5));
        card.selectCannons(doubleCannon);
        assertTrue(player1.getShipManager().getCargoPositon().get(Color.RED).isEmpty());
        assertTrue(player1.getShipManager().getCargoPositon().get(Color.YELLOW).isEmpty());
        assertEquals(player1.getShipManager().getCargoPositon().get(Color.BLUE),Set.of(List.of(8,9),List.of(8,10)));
        assertTrue(player1.getShipManager().getCargoPositon().get(Color.GREEN).isEmpty());

        assertEquals(CalculateFirePowerState.class, card.getCurrentState().getClass());
        assertEquals(card.getCurrentPlayer(), player2);
    }


    private void playerLost(Player player){
        assertEquals(CalculateFirePowerState.class, card.getCurrentState().getClass());
        assertEquals(card.getCurrentPlayer(), player);
        doubleCannon.put(List.of(7,10),List.of(9,5));
        card.selectCannons(doubleCannon);
        assertTrue(player.getShipManager().getCargoPositon().get(Color.RED).isEmpty());
        assertTrue(player.getShipManager().getCargoPositon().get(Color.YELLOW).isEmpty());
        assertTrue(player.getShipManager().getCargoPositon().get(Color.BLUE).isEmpty());
        assertTrue(player.getShipManager().getCargoPositon().get(Color.GREEN).isEmpty());
        BatteryComponent battery = (BatteryComponent) player.getShipManager().getComponent(9,5).orElse(null);
        assertEquals(battery.getBatteryCapacity(),0);
    }

    @Test
    void allPlayersLostCardTest() {
        playerLost(player1);
        playerLost(player2);
        playerLost(player3);
        playerLost(player4);
        assertEquals(EndState.class, card.getCurrentState().getClass());
    }

    private void playerWon(Player player){
        assertEquals(CalculateFirePowerState.class, card.getCurrentState().getClass());
        assertNotEquals(player.getShipManager().calculateFirePower(),0);
        assertEquals(card.getCurrentPlayer(), player);
        doubleCannon.put(List.of(7,10),List.of(9,5));
        doubleCannon.put(List.of(6,5),List.of(9,5));
        card.selectCannons(doubleCannon);
        card.acceptCargo(0,8,9);
        card.acceptCargo(0,8,9);
        assertEquals(player.getShipManager().getCargoPositon().get(Color.RED), Set.of(List.of(8,9)));
        assertEquals(player.getShipManager().getCargoPositon().get(Color.YELLOW), Set.of(List.of(8,9)));

        assertEquals(EndState.class, card.getCurrentState().getClass());
    }

    @Test
    void playerWonTest() {
        playerLost(player1);
        playerLost(player2);
        playerWon(player3);
        assertEquals(EndState.class, card.getCurrentState().getClass());
    }

    @Test
    void lastPlayerWonTest() {
        playerLost(player1);
        playerLost(player2);
        playerLost(player3);
        playerWon(player4);
        assertEquals(EndState.class, card.getCurrentState().getClass());
    }

    @Test
    void equalLeaderFirePowerTest() {
        card.setRequiredFirePower(6);
        assertEquals(CalculateFirePowerState.class, card.getCurrentState().getClass());
        assertNotEquals(player1.getShipManager().calculateFirePower(),0);
        assertEquals(card.getCurrentPlayer(), player1);
        card.selectCannons(doubleCannon);
        card.setRequiredFirePower(9);
        playerLost(player2);
        playerLost(player3);
        playerLost(player4);
        assertEquals(EndState.class, card.getCurrentState().getClass());
    }

    @Test
    void equalPlayerFirePowerTest() {
        playerLost(player1);
        card.setRequiredFirePower(6);
        assertEquals(CalculateFirePowerState.class, card.getCurrentState().getClass());
        assertNotEquals(player2.getShipManager().calculateFirePower(),0);
        assertEquals(card.getCurrentPlayer(), player2);
        doubleCannon.clear();
        card.selectCannons(doubleCannon);
        card.setRequiredFirePower(7);
        playerWon(player3);
        assertEquals(EndState.class, card.getCurrentState().getClass());
    }






}
