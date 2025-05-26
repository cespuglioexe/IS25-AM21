package it.polimi.it.galaxytrucker.model.gamestates;

import it.polimi.it.galaxytrucker.model.managers.GameManager;
import it.polimi.it.galaxytrucker.model.managers.Player;
import it.polimi.it.galaxytrucker.model.managers.ShipManager;
import it.polimi.it.galaxytrucker.model.utility.Color;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class LegalityCheckStateTest {
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

        gameManager.getFlightBoard().printFlightBoardState();
        assertEquals(1, gameManager.getFlightBoard().getPlayerOrder().size());
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

        assertEquals(GameEndState.class, gameManager.getCurrentState().getClass());
    }
}