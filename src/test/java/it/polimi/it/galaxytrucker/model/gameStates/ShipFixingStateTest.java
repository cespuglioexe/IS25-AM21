package it.polimi.it.galaxytrucker.model.gameStates;

import it.polimi.it.galaxytrucker.model.exceptions.InvalidActionException;
import it.polimi.it.galaxytrucker.model.managers.GameManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ShipFixingStateTest {
    private GameManager gameManager;

    private UUID playerId1;
    private UUID playerId2;
    private UUID playerId3;

    @BeforeEach
    void initializeParameters() {
        gameManager = new GameManager(2, 3, "game");

        playerId1 = gameManager.addPlayer("Margarozzo");
        playerId2 = gameManager.addPlayer("Balzarini");
        playerId3 = gameManager.addPlayer("Ing. Conti");
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
        assertEquals(GameTurnStartState.class, gameManager.getCurrentState().getClass());

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
        assertEquals(GameTurnStartState.class, gameManager.getCurrentState().getClass());

        assertTrue(() -> gameManager.getFlightBoard().getPlayerOrder().size() == 3);
        assertTrue(() -> gameManager.getFlightBoard().getPlayerOrder().get(0).equals(gameManager.getPlayerByID(playerId1)));
        assertTrue(() -> gameManager.getFlightBoard().getPlayerOrder().get(1).equals(gameManager.getPlayerByID(playerId2)));
        assertTrue(() -> gameManager.getFlightBoard().getPlayerOrder().get(2).equals(gameManager.getPlayerByID(playerId3)));
    }
}