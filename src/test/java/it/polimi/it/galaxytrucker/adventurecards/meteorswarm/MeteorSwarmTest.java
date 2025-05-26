package it.polimi.it.galaxytrucker.adventurecards.meteorswarm;

import it.polimi.it.galaxytrucker.exceptions.InvalidActionException;
import it.polimi.it.galaxytrucker.model.adventurecards.cardstates.meteorswarm.BigMeteorState;
import it.polimi.it.galaxytrucker.model.adventurecards.cards.MeteorSwarm;
import it.polimi.it.galaxytrucker.model.adventurecards.cardstates.EndState;
import it.polimi.it.galaxytrucker.model.adventurecards.cardstates.meteorswarm.SmallMeteorState;
import it.polimi.it.galaxytrucker.model.adventurecards.interfaces.attack.Projectile;
import it.polimi.it.galaxytrucker.model.managers.FlightBoard;
import it.polimi.it.galaxytrucker.model.managers.FlightBoardFlightRules;
import it.polimi.it.galaxytrucker.model.managers.Player;
import it.polimi.it.galaxytrucker.model.managers.ShipManager;
import it.polimi.it.galaxytrucker.model.utility.Color;
import it.polimi.it.galaxytrucker.model.utility.Direction;
import it.polimi.it.galaxytrucker.model.utility.ProjectileType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class MeteorSwarmTest {
    private MeteorSwarm card;

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
        ShipCreation.createShip2(player2);
        ShipCreation.createShip3(player3);
        ShipCreation.createShip4(player4);

        List<Projectile> projectiles = new ArrayList<>();
        projectiles.add(new Projectile(ProjectileType.BIG, Direction.DOWN));
        projectiles.add(new Projectile(ProjectileType.SMALL,Direction.RIGHT));
        projectiles.add(new Projectile(ProjectileType.SMALL,Direction.LEFT));

        card = new MeteorSwarm(projectiles, new FlightBoardFlightRules(flightBoard));
        card.play();
    }

    @Test
    void playerShootsAtMeteorTest() {
        assertEquals(BigMeteorState.class, card.getCurrentState().getClass());

        List<Integer> cannonCoord = List.of(7, 4);
        Projectile meteor = card.getCurrentMeteor();
        List<Integer> aimedCoords = card.getAimedCoordsByProjectile(meteor);

        ShipManager ship = player1.getShipManager();
        ship.printBoard();

        if (aimedCoords.get(1) == cannonCoord.get(1)) {
            card.shootAtMeteorWith(cannonCoord);
            System.out.println("The cannon shot the meteor");
            assertComponentNotDestroyedAt(aimedCoords, ship, meteor.getSize().toString());
        } else {
            System.out.println(meteor.getSize().toString() + ": " + aimedCoords);
            assertThrows(InvalidActionException.class, () -> card.shootAtMeteorWith(cannonCoord));
        }

        System.out.println("Ship after the attack:");
        ship.printBoard();
    }
    private void assertComponentNotDestroyedAt(List<Integer> coords, ShipManager ship, String label) {
        System.out.println(label + ": " + coords);
    
        try {
            int row = coords.get(0);
            int col = coords.get(1);
    
            if (!ship.isOutside(row, col)) {
                assertTrue(ship.getComponent(row, col).isPresent(), "Expected component to not be destroyed at " + coords);
                System.out.println("The component survived the attack at: " + coords);
            }
        } catch (IndexOutOfBoundsException e) {
            System.out.println("The shot missed the ship");
        }
    }

    @Test
    void playerShootsAtMeteorDoubleCannonTest() {
         assertEquals(BigMeteorState.class, card.getCurrentState().getClass());

        List<Integer> cannonCoord = List.of(6, 5);
        List<Integer> batteryCoord = List.of(9, 5);
        Projectile meteor = card.getCurrentMeteor();
        List<Integer> aimedCoords = card.getAimedCoordsByProjectile(meteor);

        ShipManager ship = player1.getShipManager();
        ship.printBoard();

        if (aimedCoords.get(1) == cannonCoord.get(1)) {
            card.shootAtMeteorWith(cannonCoord, batteryCoord);
            System.out.println("The cannon shot the meteor");
            assertComponentNotDestroyedAt(aimedCoords, ship, meteor.getSize().toString());
        } else {
            System.out.println(meteor.getSize().toString() + ": " + aimedCoords);
            assertThrows(InvalidActionException.class, () -> card.shootAtMeteorWith(cannonCoord));
        }

        System.out.println("Ship after the attack:");
        ship.printBoard();
    }

    @Test
    void playerDoesNotShootAtMeteorTest() {
        assertEquals(BigMeteorState.class, card.getCurrentState().getClass());

        Projectile meteor = card.getCurrentMeteor();
        List<Integer> aimedCoords = card.getAimedCoordsByProjectile(meteor);

        ShipManager ship = player1.getShipManager();
        ship.printBoard();

        card.notShootAtMeteor();

        assertComponentDestroyedAt(aimedCoords, ship, meteor.getSize().toString());

        System.out.println("Ship after the attack:");
        ship.printBoard();
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
    void playerActivatesShieldTest() {
        playerDoesNotShootAtMeteorTest();

        assertEquals(SmallMeteorState.class, card.getCurrentState().getClass());

        Projectile meteor = card.getCurrentMeteor();
        List<Integer> aimedCoords = card.getAimedCoordsByProjectile(meteor);

        ShipManager ship = player1.getShipManager();
        ship.printBoard();

        HashMap<List<Integer>, List<Integer>> selectedShieldAndBatteries = new HashMap<>();
        selectedShieldAndBatteries.put(List.of(8, 10), List.of(9, 5));
        card.activateShields(selectedShieldAndBatteries);

        assertComponentProtectedAt(aimedCoords, ship, meteor.getSize().toString());

        System.out.println("Ship after the attack:");
        ship.printBoard();
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

    @Test
    void playerDoesNotActivateShieldTest() {
        playerDoesNotShootAtMeteorTest();

        assertEquals(SmallMeteorState.class, card.getCurrentState().getClass());

        Projectile meteor = card.getCurrentMeteor();
        List<Integer> aimedCoords = card.getAimedCoordsByProjectile(meteor);

        ShipManager ship = player1.getShipManager();
        ship.printBoard();

        boolean exposedConnectors = hasExposedConnector(ship, aimedCoords.get(0), aimedCoords.get(1), meteor.getDirection());

        card.activateNoShield();

        if (exposedConnectors) {
            System.out.println("The component had exposed connectors");
            assertComponentDestroyedAt(aimedCoords, ship, meteor.getSize().toString());
        } else {
            assertComponentNotDestroyedAt(aimedCoords, ship, meteor.getSize().toString());
        }

        System.out.println("Ship after the attack:");
        ship.printBoard();
    }
    private boolean hasExposedConnector(ShipManager ship, int row, int column, Direction direction) {
        try {
            return ship.hasExposedConnectorAtDirection(row, column, direction.reverse());
        } catch (IndexOutOfBoundsException e) {
            return false;
        }
    }

    @Test
    void endStateTest() {
        assertEquals(player1, card.getPlayer());
        card.notShootAtMeteor();
        card.activateNoShield();
        card.activateNoShield();

        assertEquals(player2, card.getPlayer());
        card.notShootAtMeteor();
        card.activateNoShield();
        card.activateNoShield();

        assertEquals(player3, card.getPlayer());
        card.notShootAtMeteor();
        card.activateNoShield();
        card.activateNoShield();

        assertEquals(player4, card.getPlayer());
        card.notShootAtMeteor();
        card.activateNoShield();
        card.activateNoShield();

        assertEquals(EndState.class, card.getCurrentState().getClass());
    }
}
