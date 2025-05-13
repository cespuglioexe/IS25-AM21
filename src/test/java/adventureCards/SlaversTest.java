package adventureCards;


import it.polimi.it.galaxytrucker.model.adventurecards.cardstates.slavers.*;
import it.polimi.it.galaxytrucker.model.adventurecards.refactored.Slavers;
import it.polimi.it.galaxytrucker.model.componenttiles.*;
import it.polimi.it.galaxytrucker.model.crewmates.Alien;
import it.polimi.it.galaxytrucker.model.crewmates.Human;
import it.polimi.it.galaxytrucker.exceptions.InvalidActionException;
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
import static org.junit.jupiter.api.Assertions.assertThrows;

public class SlaversTest {

    private Slavers card;
    private double firePowerRequired = 7;
    private final int flightDayPenalty = 2;
    private final int creditReward = 8;
    private  int crewmatePenalty = 4;

    private final int gameLevel = 2;

    private Player player1;
    private Player player2;
    private Player player3;
    private Player player4;

    private FlightBoard flightBoard;

    HashMap<List<Integer>,List<Integer>> doubleCannon;


    @BeforeEach
    void initializeParameters() {
        player1 = new Player(UUID.randomUUID(), "Margarozzo", Color.BLUE, new ShipManager(gameLevel));
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

        card = new Slavers(creditReward,crewmatePenalty,flightDayPenalty,firePowerRequired, new FlightBoardFlightRules(flightBoard));

        card.play();
    }

    private void createShip(Player player) {
        ShipManager ship = player.getShipManager();
        Human human = new Human();
        Alien alien = new Alien(AlienType.BROWNALIEN);
        /*
         *     4  5  6  7  8  9  10
         * 5        [ ]   [ ]
         * 6     [D][C][c][C][ ]
         * 7  [C][l][c][x][c][C][D]
         * 8  [ ][ ][ ][ ][c][ ][ ]
         * 9  [ ][B][ ]   [ ][ ][ ]
         *
         * Where c stands for CabinModule
         * Where l stands for LifeSupport
         * Where x stands for CentralCabin which has all TileEdge.UNIVERSAL connectors
         */
        ship.addComponentTile(6, 7, new CabinModule(List.of(TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE)));
        ship.addComponentTile(7, 6, new CabinModule(List.of(TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE)));
        ship.addComponentTile(7, 8, new CabinModule(List.of(TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE)));
        ship.addComponentTile(8, 8, new CabinModule(List.of(TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE)));

        ship.addComponentTile(7, 4, new SingleCannon(List.of(TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE)));
        ship.addComponentTile(6, 6, new SingleCannon(List.of(TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE)));
        ship.addComponentTile(6, 8, new SingleCannon(List.of(TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE)));
        ship.addComponentTile(7, 9, new SingleCannon(List.of(TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE)));

        ship.addComponentTile(7, 10, new DoubleCannon(List.of(TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE)));
        ship.addComponentTile(6, 5, new DoubleCannon(List.of(TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE)));
        ship.addComponentTile(9, 5, new BatteryComponent(2,List.of(TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE)));




        ship.addComponentTile(7, 5, new LifeSupport(AlienType.BROWNALIEN, List.of(TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE)));

        ship.addCrewmate(6, 7, human);
        ship.addCrewmate(6, 7, human);
        ship.addCrewmate(7, 8, human);
        ship.addCrewmate(7, 8, human);
        ship.addCrewmate(8, 8, human);
        ship.addCrewmate(8, 8, human);


        ship.addCrewmate(7, 6, alien);
    }

    @Test
    void leaderPlaysTest() {
        assertEquals(CalculateFirePowerState.class, card.getCurrentState().getClass());
        assertNotEquals(player1.getShipManager().calculateFirePower(),0);
        assertEquals(card.getCurrentPlayer(), player1);
    }

    @Test
    void leaderLostTest() {
        leaderPlaysTest();

        doubleCannon.put(List.of(7,10),List.of(9,5));
        card.selectCannons(doubleCannon);
        assertEquals(CrewmatePenaltyState.class, card.getCurrentState().getClass());
    }

    @Test
    void leaderWonTest() {
        leaderPlaysTest();

        doubleCannon.put(List.of(7,10),List.of(9,5));
        doubleCannon.put(List.of(6,5),List.of(9,5));
        card.selectCannons(doubleCannon);
        assertEquals(CreditRewardState.class, card.getCurrentState().getClass());
    }

    @Test
    void leaderAcceptRewardTest() {
        leaderWonTest();
        card.applyCreditReward();
        assertEquals(player1.getCredits(), creditReward);
        assertEquals(EndState.class, card.getCurrentState().getClass());
    }

    @Test
    void leaderDeclineRewardTest() {
        leaderWonTest();
        int playerPrePosition = flightBoard.getPlayerPosition().get(player1);
        card.discardCreditReward();
        assertEquals(flightBoard.getPlayerPosition().get(player1), playerPrePosition);
        assertEquals(EndState.class, card.getCurrentState().getClass());
    }

    // Test penalty
    @Test
    void leaderPenaltyTest() {
        leaderLostTest();

        card.sellSlaves(List.of(List.of(6,7),List.of(6,7), List.of(7,8), List.of(7,8)));

        assertEquals(CalculateFirePowerState.class, card.getCurrentState().getClass());
        assertEquals(player1.getShipManager().countCrewmates(), 3);
        assertEquals(card.getCurrentPlayer(), player2);
    }

    @Test
    void leaderPenaltyExceptionTest() {
        leaderLostTest();
        assertThrows(InvalidActionException.class, () -> card.sellSlaves(List.of(List.of(6,7),List.of(6,7), List.of(7,8))));
    }

    @Test
    void leaderPenaltyZeroCrewmateTest() {
        leaderLostTest();
        crewmatePenalty = 8;
        card.sellSlaves(List.of(List.of(6,7),List.of(6,7), List.of(7,8), List.of(7,8),List.of(8,8), List.of(8,8), List.of(7,6)));

        assertEquals(CalculateFirePowerState.class, card.getCurrentState().getClass());
        assertEquals(player1.getShipManager().countCrewmates(), 0);
        assertEquals(card.getCurrentPlayer(), player2);
    }


    private void playerLost(Player player){
        assertEquals(CalculateFirePowerState.class, card.getCurrentState().getClass());
        assertNotEquals(player.getShipManager().calculateFirePower(),0);
        assertEquals(card.getCurrentPlayer(), player);
        doubleCannon.put(List.of(7,10),List.of(9,5));
        card.selectCannons(doubleCannon);
        assertEquals(CrewmatePenaltyState.class, card.getCurrentState().getClass());
        card.sellSlaves(List.of(List.of(6,7),List.of(6,7), List.of(7,8), List.of(7,8)));
        assertEquals(player.getShipManager().countCrewmates(), 3);
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
        card.applyCreditReward();
        assertEquals(player.getCredits(), creditReward);
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
        card.setRequiredFirePower(4);
        assertEquals(CalculateFirePowerState.class, card.getCurrentState().getClass());
        assertNotEquals(player1.getShipManager().calculateFirePower(),0);
        assertEquals(card.getCurrentPlayer(), player1);
        card.selectCannons(doubleCannon);
        card.setRequiredFirePower(7);
        playerLost(player2);
        playerLost(player3);
        playerLost(player4);
        assertEquals(EndState.class, card.getCurrentState().getClass());
    }

    @Test
    void equalPlayerFirePowerTest() {
        playerLost(player1);
        card.setRequiredFirePower(4);
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
