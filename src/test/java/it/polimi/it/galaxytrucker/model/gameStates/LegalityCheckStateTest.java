package it.polimi.it.galaxytrucker.model.gameStates;

import it.polimi.it.galaxytrucker.model.managers.GameManager;
import it.polimi.it.galaxytrucker.model.managers.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class LegalityCheckStateTest {
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

    private void playersBuildAllLegalShips() {
        // All three players have legal ships
        ShipCreation.createLegalShip1(gameManager.getPlayerByID(playerId1));
        ShipCreation.createLegalShip2(gameManager.getPlayerByID(playerId2));
        ShipCreation.createLegalShip3(gameManager.getPlayerByID(playerId3));

        gameManager.finishBuilding(playerId1);
        gameManager.finishBuilding(playerId2);
        gameManager.finishBuilding(playerId3);
    }

    @Test
    void movesToFixingStateAllIllegalTest() {
        playersBuildAllIllegalShips();

        assertTrue(() -> gameManager.getFlightBoard().getPlayerOrder().isEmpty());
        assertEquals(ShipFixingState.class, gameManager.getCurrentState().getClass());
    }

    @Test
    void movesToFixingStateSomeIllegalTest() {
        playersBuildSomeIllegalShips();

        assertTrue(() -> gameManager.getFlightBoard().getPlayerOrder().size() == 1);
        assertTrue(() -> gameManager.getFlightBoard().getPlayerOrder().getFirst().equals(gameManager.getPlayerByID(playerId1)));

        assertEquals(ShipFixingState.class, gameManager.getCurrentState().getClass());
    }

    @Test
    void movesToTurnStartStateTest() {
        playersBuildAllLegalShips();

        assertTrue(() -> gameManager.getFlightBoard().getPlayerOrder().size() == 3);
        assertTrue(() -> gameManager.getFlightBoard().getPlayerOrder().get(0).equals(gameManager.getPlayerByID(playerId1)));
        assertTrue(() -> gameManager.getFlightBoard().getPlayerOrder().get(1).equals(gameManager.getPlayerByID(playerId2)));
        assertTrue(() -> gameManager.getFlightBoard().getPlayerOrder().get(2).equals(gameManager.getPlayerByID(playerId3)));

        assertEquals(GameTurnStartState.class, gameManager.getCurrentState().getClass());
    }
}