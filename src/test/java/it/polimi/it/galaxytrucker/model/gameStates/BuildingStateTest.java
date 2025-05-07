package it.polimi.it.galaxytrucker.model.gameStates;

import it.polimi.it.galaxytrucker.model.componenttiles.ComponentTile;
import it.polimi.it.galaxytrucker.model.exceptions.IllegalComponentPositionException;
import it.polimi.it.galaxytrucker.model.exceptions.InvalidActionException;
import it.polimi.it.galaxytrucker.model.exceptions.InvalidFunctionCallInState;
import it.polimi.it.galaxytrucker.model.managers.GameManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class BuildingStateTest {
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

    @Test
    void drawComponentTileTest() {
        gameManager.drawComponentTile(playerId1);

        assertNotEquals(null, gameManager.getPlayerByID(playerId1).getHeldComponent());
    }

    @Test
    void drawAllComponentTiles() {
        assertThrows(InvalidActionException.class, () -> {
            while (true) {
                gameManager.drawComponentTile(playerId1);
            }
        });
    }

    @Test
    void placeComponentTileTest() {
        gameManager.drawComponentTile(playerId1);
        gameManager.placeComponentTile(playerId1, 6, 7);

        assertTrue(() -> gameManager.getPlayerByID(playerId1).getShipManager().getComponent(6, 7).isPresent());
    }

    @Test
    void placeComponentTileSpotTakenTest() {
        gameManager.drawComponentTile(playerId1);
        gameManager.placeComponentTile(playerId1, 6, 7);
        gameManager.drawComponentTile(playerId1);

        assertThrows(IllegalComponentPositionException.class, () -> gameManager.placeComponentTile(playerId1, 6, 7));
    }

    @Test
    void placeComponentTileOutsideTheShipTest() {
        gameManager.drawComponentTile(playerId1);

        assertThrows(IndexOutOfBoundsException.class, () -> gameManager.placeComponentTile(playerId1, 0, 0));
    }

    @Test
    void rotateComponentTileTest() {
        gameManager.drawComponentTile(playerId1);
        gameManager.placeComponentTile(playerId1, 6, 7);
        gameManager.rotateComponentTile(playerId1, 6, 7);

        assertEquals(1, gameManager.getPlayerByID(playerId1).getShipManager().getComponent(6, 7).get().getRotation());
    }

    @Test
    void rotateMissingComponentTileTest() {
        assertThrows(IllegalComponentPositionException.class, () -> gameManager.rotateComponentTile(playerId1, 5, 6));
    }

    @Test
    void rotateOutsideComponentTileTest() {
        assertThrows(IllegalComponentPositionException.class, () -> gameManager.rotateComponentTile(playerId1, 5, 7));
    }

    @Test
    void finishBuildingTest() {
        gameManager.finishBuilding(playerId1);
        gameManager.finishBuilding(playerId2);
        assertEquals(BuildingState.class, gameManager.getCurrentState().getClass());

        gameManager.finishBuilding(playerId3);
        assertEquals(GameTurnStartState.class, gameManager.getCurrentState().getClass());
    }

    @Test
    void saveComponentTileTest() {
        gameManager.drawComponentTile(playerId1);
        gameManager.saveComponentTile(playerId1);

        assertTrue(() -> gameManager.getPlayerByID(playerId1).getShipManager().getSavedComponentTile(0) != null);
        assertTrue(() -> gameManager.getPlayerByID(playerId2).getShipManager().getSavedComponentTiles().isEmpty());
    }

    @Test
    void saveComponentTileNoComponentTest() {
        assertThrows(InvalidActionException.class, () -> gameManager.saveComponentTile(playerId1));
    }

    @Test
    void discardComponentTileTest() {
        gameManager.drawComponentTile(playerId1);
        ComponentTile comp = gameManager.getPlayerByID(playerId1).getHeldComponent();

        gameManager.discardComponentTile(playerId1);

        assertTrue(() -> gameManager.getDiscardedComponentTiles().contains(comp));
    }

    @Test
    void selectSavedComponentTileTest() {
        gameManager.drawComponentTile(playerId1);
        gameManager.saveComponentTile(playerId1);

        ComponentTile comp = gameManager.getPlayerByID(playerId1).getShipManager().getSavedComponentTiles().get(0);

        gameManager.selectSavedComponentTile(playerId1, 0);

        assertEquals(comp, gameManager.getPlayerByID(playerId1).getHeldComponent());
    }

    @Test
    void selectSavedComponentAlreadyHoldingTileTest() {
        gameManager.drawComponentTile(playerId1);
        gameManager.saveComponentTile(playerId1);
        gameManager.drawComponentTile(playerId1);

        assertThrows(InvalidActionException.class, () -> gameManager.selectSavedComponentTile(playerId1, 0));
    }

    @Test
    void selectDiscardedComponentTileTest() {
        gameManager.drawComponentTile(playerId1);
        ComponentTile comp = gameManager.getPlayerByID(playerId1).getHeldComponent();

        gameManager.discardComponentTile(playerId1);
        gameManager.selectDiscardedComponentTile(playerId1, 0);

        assertEquals(comp, gameManager.getPlayerByID(playerId1).getHeldComponent());
    }

    @Test
    void selectDiscardedComponentAlreadyHoldingTileTest() {
        gameManager.drawComponentTile(playerId1);
        gameManager.discardComponentTile(playerId1);
        gameManager.drawComponentTile(playerId1);

        assertThrows(InvalidActionException.class, () -> gameManager.selectDiscardedComponentTile(playerId1, 0));
    }

    @Test
    void saveComponentTileMoreThan2Test() {
        gameManager.drawComponentTile(playerId1);
        gameManager.saveComponentTile(playerId1);
        gameManager.drawComponentTile(playerId1);
        gameManager.saveComponentTile(playerId1);

        gameManager.drawComponentTile(playerId1);
        assertThrows(InvalidActionException.class, () -> gameManager.saveComponentTile(playerId1));
    }

    @Test
    void callInvalidFunctionTest() {
        assertThrows(InvalidFunctionCallInState.class, () -> gameManager.addPlayer("Schumi"));
    }
}