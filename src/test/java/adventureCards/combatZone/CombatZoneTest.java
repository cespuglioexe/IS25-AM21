package adventureCards.combatZone;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import it.polimi.it.galaxytrucker.model.adventurecards.cardstates.combatZone.EndState;
import it.polimi.it.galaxytrucker.model.adventurecards.cardstates.combatZone.AttackState;
import it.polimi.it.galaxytrucker.model.adventurecards.cardstates.combatZone.CannonSelectionState;
import it.polimi.it.galaxytrucker.model.adventurecards.cardstates.combatZone.CrewmatePenaltyState;
import it.polimi.it.galaxytrucker.model.adventurecards.cardstates.combatZone.EngineSelectionState;
import it.polimi.it.galaxytrucker.model.adventurecards.refactored.CombatZone;
import it.polimi.it.galaxytrucker.model.managers.FlightBoard;
import it.polimi.it.galaxytrucker.model.managers.FlightBoardFlightRules;
import it.polimi.it.galaxytrucker.model.managers.Player;
import it.polimi.it.galaxytrucker.model.managers.ShipManager;
import it.polimi.it.galaxytrucker.model.utility.Color;
import it.polimi.it.galaxytrucker.model.utility.Projectile;

public class CombatZoneTest {
    private CombatZone card;
    private final int crewmatePenalty = 2;
    private final int flightDayPenalty = 3;

    private final int gameLevel = 2;

    private Player player1;
    private Player player2;
    private Player player3;
    private Player player4;

    private FlightBoard flightBoard;

    @BeforeEach
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

        ShipCreation.createShip1(player1);
        ShipCreation.createShip1(player2);
        ShipCreation.createShip2(player3);
        ShipCreation.createShip3(player4);

        card = new CombatZone(crewmatePenalty, flightDayPenalty, new FlightBoardFlightRules(flightBoard));

        printPartecipants();
        flightBoard.printFlightBoardState();
        card.play();
    }
    private void printPartecipants() {
        List<Player> players = flightBoard.getPlayerOrder();

        System.out.println("Players:");
        for (Player player : players) {
            System.out.println( player.getPlayerName() + ": " + player.getColor());
        }
        System.out.println();
    }
    
    @Test
    void flightDayPenaltyTest() {
        printflightDayPenalty();
        flightBoard.printFlightBoardState();
        int playerPosition = flightBoard.getPlayerPosition().get(player4);

        assertEquals(21, playerPosition);
        assertEquals(EngineSelectionState.class, card.getCurrentState().getClass());
    }
    private void printflightDayPenalty() {
        System.out.println("Player with least crewmates: " + card.findPlayerWithLeastCrewmates().getPlayerName());
        flightBoard.printFlightBoardState();
    }

    @Test
    void playersWithSameEnginePowerTest() {
        assertEquals(EngineSelectionState.class, card.getCurrentState().getClass());

        printflightDayPenalty();

        card.selectNoEngines(player1);
        card.selectNoEngines(player2);
        card.selectNoEngines(player3);

        HashMap<List<Integer>, List<Integer>> selectedEnginesAndBatteries = new HashMap<>();
        selectedEnginesAndBatteries.put(List.of(8, 7), List.of(7, 6));
        card.selectEngines(player4, selectedEnginesAndBatteries);

        printPlayerWithLeastEnginePower();
        assertEquals(player1, card.findPlayerWithLeastEnginePower());
    }
    private void printPlayerWithLeastEnginePower() {
        System.out.println("Player with least engine power: " + card.findPlayerWithLeastEnginePower().getPlayerName());
    }

    @Test
    void playersWithDifferentEnginePowerTest() {
        assertEquals(EngineSelectionState.class, card.getCurrentState().getClass());

        printflightDayPenalty();

        card.selectNoEngines(player1);

        HashMap<List<Integer>, List<Integer>> player2EnginesAndBatteries = new HashMap<>();
        player2EnginesAndBatteries.put(List.of(8, 7), List.of(7, 6));
        card.selectEngines(player2, player2EnginesAndBatteries);

        HashMap<List<Integer>, List<Integer>> player3EnginesAndBatteries = new HashMap<>();
        player3EnginesAndBatteries.put(List.of(8, 7), List.of(7, 6));
        player3EnginesAndBatteries.put(List.of(8, 9), List.of(7, 6));
        card.selectEngines(player3, player3EnginesAndBatteries);

        card.selectNoEngines(player4);

        printPlayerWithLeastEnginePower();
        assertEquals(player4, card.findPlayerWithLeastEnginePower());
    }

    @Test
    void crewmatePenaltyTest() {
        playersWithDifferentEnginePowerTest();

        assertEquals(CrewmatePenaltyState.class, card.getCurrentState().getClass());

        card.applyCrewmatePenalty(7, 7);
        card.applyCrewmatePenalty(7, 7);

        System.out.println(card.findPlayerWithLeastEnginePower().getPlayerName() + " lost " + crewmatePenalty + " crewmates");

        assertEquals(player4.getShipManager().countCrewmates(), 1);

        assertEquals(CannonSelectionState.class, card.getCurrentState().getClass());
    }

    @Test
    void playersWithSameFirePowerTest() {
        crewmatePenaltyTest();

        assertEquals(CannonSelectionState.class, card.getCurrentState().getClass());

        card.selectNoCannons(player1);
        card.selectNoCannons(player2);

        HashMap<List<Integer>, List<Integer>> selectedCannonsAndBatteries = new HashMap<>();
        selectedCannonsAndBatteries.put(List.of(6, 7), List.of(7, 8));
        card.selectCannons(player3, selectedCannonsAndBatteries);

        card.selectNoCannons(player4);

        printPlayerWithLeastFirePower();
        assertEquals(player4, card.findPlayerWithLeastFirePower());
    }
    private void printPlayerWithLeastFirePower() {
        System.out.println("Player with least fire power: " + card.findPlayerWithLeastFirePower().getPlayerName());
    }

    @Test
    void playersWithDifferentFirePowerTest() {
        crewmatePenaltyTest();

        assertEquals(CannonSelectionState.class, card.getCurrentState().getClass());

        card.selectNoCannons(player1);
        card.selectNoCannons(player2);
        card.selectNoCannons(player3);
        card.selectNoCannons(player4);

        printPlayerWithLeastFirePower();
        assertEquals(player3, card.findPlayerWithLeastFirePower());
    }

    @Test
    void attackStateWithoutShieldsTest() {
        playersWithDifferentFirePowerTest();
        assertEquals(AttackState.class, card.getCurrentState().getClass());
        
        ShipManager ship = player3.getShipManager();
        ship.printBoard();

        card.activateNoShield();

        List<Integer> smallShotCoords = card.getAimedCoordsByProjectile(Projectile.SMALL);
        List<Integer> bigShotCoords = card.getAimedCoordsByProjectile(Projectile.BIG);

        System.out.println("Ship after the attack:");
        ship.printBoard();

        assertComponentDestroyedAt(smallShotCoords, ship, "First shot");
        assertComponentDestroyedAt(bigShotCoords, ship, "Second shot");

        assertEquals(EndState.class, card.getCurrentState().getClass());
    }
    private void assertComponentDestroyedAt(List<Integer> coords, ShipManager ship, String label) {
        System.out.println(label + ": " + coords);
    
        try {
            int row = coords.get(0);
            int col = coords.get(1);
    
            if (!ship.isOutside(row, col)) {
                assertTrue(ship.getComponent(row, col).isEmpty(), "Expected component to be destroyed at " + coords);
                System.out.println("The shot destroyed the component at " + coords);
            }
        } catch (IndexOutOfBoundsException e) {
            System.out.println("The shot missed the ship");
        }
    }

    @Test
    void attackStateWithShieldsTest() {
        playersWithDifferentFirePowerTest();
        assertEquals(AttackState.class, card.getCurrentState().getClass());

        ShipManager ship = player3.getShipManager();
        ship.printBoard();

        HashMap<List<Integer>, List<Integer>> selectedShieldAndBatteries = new HashMap<>();
        selectedShieldAndBatteries.put(List.of(7, 9), List.of(7, 8));
        card.activateShields(selectedShieldAndBatteries);
    
        List<Integer> smallShotCoords = card.getAimedCoordsByProjectile(Projectile.SMALL);
        List<Integer> bigShotCoords = card.getAimedCoordsByProjectile(Projectile.BIG);

        System.out.println("Ship after the attack:");
        ship.printBoard();

        if (!smallShotCoords.equals(bigShotCoords)) {
            assertComponentProtectedAt(smallShotCoords, ship, "Small shot (should be protected)");
        } else {
            System.out.println("Small shot (should be protected): " + smallShotCoords);
            System.out.println("Shield protected the ship at " + smallShotCoords);
        }
        assertComponentDestroyedAt(bigShotCoords, ship, "Big shot (should destroy)");
    
        assertEquals(EndState.class, card.getCurrentState().getClass());
    }
    private void assertComponentProtectedAt(List<Integer> coords, ShipManager ship, String label) {
        System.out.println(label + ": " + coords);
    
        try {
            int row = coords.get(0);
            int col = coords.get(1);
    
            if (!ship.isOutside(row, col)) {
                assertTrue(ship.getComponent(row, col).isPresent(), "Expected component to be protected at " + coords);
                System.out.println("Shield protected the ship at " + coords);
            }
        } catch (IndexOutOfBoundsException e) {
            System.out.println("The shot missed the ship");
        }
    }
}
