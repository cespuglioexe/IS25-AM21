package it.polimi.it.galaxytrucker.model.gamestates;

import it.polimi.it.galaxytrucker.exceptions.InvalidActionException;
import it.polimi.it.galaxytrucker.model.managers.GameManager;
import it.polimi.it.galaxytrucker.model.managers.Player;
import it.polimi.it.galaxytrucker.model.managers.ShipManager;
import it.polimi.it.galaxytrucker.model.utility.Color;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ShipFixingStateTest {
    private GameManager gameManager;

    private final UUID playerId1 = UUID.randomUUID();
    private final UUID playerId2 = UUID.randomUUID();
    private final UUID playerId3 = UUID.randomUUID();

    @BeforeEach
    void initializeParameters() {
        gameManager = new GameManager(2, 3);

        gameManager.addPlayer(new Player(playerId1, "Margarozzo", Color.RED, new ShipManager(2, Color.BLUE)));
        gameManager.addPlayer(new Player(playerId2, "Balzarini", Color.RED, new ShipManager(2, Color.BLUE)));
        gameManager.addPlayer(new Player(playerId3, "Ing. Conti", Color.RED, new ShipManager(2, Color.BLUE)));
    }

    private void playersBuildAllIllegalShips() {
        // All three players have illegal ships
        ShipCreation.createIllegalShip1(gameManager.getPlayerByID(playerId1));
        ShipCreation.createIllegalShip2(gameManager.getPlayerByID(playerId2));
        ShipCreation.createIllegalShip3(gameManager.getPlayerByID(playerId3));

        gameManager.finishBuilding(playerId1);
        gameManager.finishBuilding(playerId2);
        gameManager.finishBuilding(playerId3);
    }

    private void playersBuildSomeIllegalShips() {
        // Player 1 has a legal ship
        ShipCreation.createLegalShip1(gameManager.getPlayerByID(playerId1));

        // Players 2 and 3 have illegal ships
        ShipCreation.createIllegalShip2(gameManager.getPlayerByID(playerId2));
        ShipCreation.createIllegalShip4(gameManager.getPlayerByID(playerId3));

        gameManager.finishBuilding(playerId1);
        gameManager.finishBuilding(playerId2);
        gameManager.finishBuilding(playerId3);
    }

    @Test
    void legalizeAllIllegalShipsWithAllIllegalShipsTest() {
        playersBuildAllIllegalShips();

        assertTrue(() -> gameManager.getFlightBoard().getPlayerOrder().isEmpty());

        gameManager.deleteComponentTile(playerId1, 8, 10);
        gameManager.deleteComponentTile(playerId1, 9, 9);
        gameManager.deleteComponentTile(playerId1, 9, 10);
        gameManager.finishBuilding(playerId1);
        assertEquals(ShipFixingState.class, gameManager.getCurrentState().getClass());

        gameManager.deleteComponentTile(playerId2, 6, 7);
        gameManager.deleteComponentTile(playerId2, 8, 4);
        gameManager.finishBuilding(playerId2);
        assertEquals(ShipFixingState.class, gameManager.getCurrentState().getClass());

        gameManager.deleteComponentTile(playerId3, 8, 7);
        gameManager.deleteComponentTile(playerId3, 8, 8);
        gameManager.finishBuilding(playerId3);
        assertEquals(GameEndState.class, gameManager.getCurrentState().getClass());

        assertTrue(() -> gameManager.getFlightBoard().getPlayerOrder().size() == 3);
        assertTrue(() -> gameManager.getFlightBoard().getPlayerOrder().get(0).equals(gameManager.getPlayerByID(playerId1)));
        assertTrue(() -> gameManager.getFlightBoard().getPlayerOrder().get(1).equals(gameManager.getPlayerByID(playerId2)));
        assertTrue(() -> gameManager.getFlightBoard().getPlayerOrder().get(2).equals(gameManager.getPlayerByID(playerId3)));
    }

    @Test
    void legalizeSomeIllegalShipsWithAllIllegalShipsTest() {
        playersBuildAllIllegalShips();

        assertTrue(() -> gameManager.getFlightBoard().getPlayerOrder().isEmpty());

        gameManager.deleteComponentTile(playerId1, 8, 10);
        gameManager.deleteComponentTile(playerId1, 9, 9);
        gameManager.deleteComponentTile(playerId1, 9, 10);
        gameManager.finishBuilding(playerId1);
        assertEquals(ShipFixingState.class, gameManager.getCurrentState().getClass());

        gameManager.deleteComponentTile(playerId2, 6, 7);
        gameManager.deleteComponentTile(playerId2, 8, 4);
        gameManager.finishBuilding(playerId2);
        assertEquals(ShipFixingState.class, gameManager.getCurrentState().getClass());

        gameManager.deleteComponentTile(playerId3, 8, 7);
        gameManager.finishBuilding(playerId3);
        assertEquals(ShipFixingState.class, gameManager.getCurrentState().getClass());

        assertTrue(() -> gameManager.getFlightBoard().getPlayerOrder().size() == 2);
        assertTrue(() -> gameManager.getFlightBoard().getPlayerOrder().get(0).equals(gameManager.getPlayerByID(playerId1)));
        assertTrue(() -> gameManager.getFlightBoard().getPlayerOrder().get(1).equals(gameManager.getPlayerByID(playerId2)));
    }

    @Test
    void legalizeSomeIllegalShipsWithSomeIllegalShipsTest() {
        playersBuildSomeIllegalShips();

        assertTrue(() -> gameManager.getFlightBoard().getPlayerOrder().size() == 1);
        assertTrue(() -> gameManager.getFlightBoard().getPlayerOrder().get(0).equals(gameManager.getPlayerByID(playerId1)));

        assertThrows(InvalidActionException.class, () -> gameManager.deleteComponentTile(playerId1, 6, 7));
        assertThrows(InvalidActionException.class, () -> gameManager.finishBuilding(playerId1));
        assertEquals(ShipFixingState.class, gameManager.getCurrentState().getClass());

        gameManager.deleteComponentTile(playerId2, 6, 7);
        gameManager.deleteComponentTile(playerId2, 8, 4);
        gameManager.finishBuilding(playerId2);
        assertEquals(ShipFixingState.class, gameManager.getCurrentState().getClass());

        gameManager.deleteComponentTile(playerId3, 5, 6);
        gameManager.deleteComponentTile(playerId3, 5, 8);
        gameManager.deleteComponentTile(playerId3, 6, 5);
        gameManager.finishBuilding(playerId3);
        assertEquals(GameEndState.class, gameManager.getCurrentState().getClass());

        assertTrue(() -> gameManager.getFlightBoard().getPlayerOrder().size() == 3);
        assertTrue(() -> gameManager.getFlightBoard().getPlayerOrder().get(0).equals(gameManager.getPlayerByID(playerId1)));
        assertTrue(() -> gameManager.getFlightBoard().getPlayerOrder().get(1).equals(gameManager.getPlayerByID(playerId2)));
        assertTrue(() -> gameManager.getFlightBoard().getPlayerOrder().get(2).equals(gameManager.getPlayerByID(playerId3)));
    }
}