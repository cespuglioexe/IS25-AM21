package it.polimi.it.galaxytrucker.model.gamestates;

import it.polimi.it.galaxytrucker.model.componenttiles.ComponentTile;
import it.polimi.it.galaxytrucker.exceptions.IllegalComponentPositionException;
import it.polimi.it.galaxytrucker.exceptions.InvalidActionException;
import it.polimi.it.galaxytrucker.exceptions.InvalidFunctionCallInState;
import it.polimi.it.galaxytrucker.model.managers.GameManager;
import it.polimi.it.galaxytrucker.model.managers.Player;
import it.polimi.it.galaxytrucker.model.managers.ShipManager;
import it.polimi.it.galaxytrucker.model.utility.Color;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class BuildingStateTest {
    private GameManager gameManager;

    private final UUID playerId1 = UUID.randomUUID();
    private final UUID playerId2 = UUID.randomUUID();
    private final UUID playerId3 = UUID.randomUUID();

    @BeforeEach
    void initializeParameters() {
        gameManager = new GameManager(2, 3);

        gameManager.addPlayer(new Player(playerId1, "Margarozzo", Color.RED, new ShipManager(2)));
        gameManager.addPlayer(new Player(playerId2, "Blazarini", Color.RED, new ShipManager(2)));
        gameManager.addPlayer(new Player(playerId3, "Ing. Conti", Color.RED, new ShipManager(2)));
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
        gameManager.placeComponentTile(playerId1, 6, 7, 0);

        assertTrue(() -> gameManager.getPlayerByID(playerId1).getShipManager().getComponent(6, 7).isPresent());
    }

    @Test
    void placeComponentTileSpotTakenTest() {
        gameManager.drawComponentTile(playerId1);
        gameManager.placeComponentTile(playerId1, 6, 7, 0);
        gameManager.drawComponentTile(playerId1);

        assertThrows(IllegalComponentPositionException.class, () -> gameManager.placeComponentTile(playerId1, 6, 7, 0));
    }

    @Test
    void placeComponentTileOutsideTheShipTest() {
        gameManager.drawComponentTile(playerId1);

        assertThrows(IndexOutOfBoundsException.class, () -> gameManager.placeComponentTile(playerId1, 0, 0, 0));
    }

    @Test
    void rotateComponentTileTest() {
        gameManager.drawComponentTile(playerId1);
        gameManager.placeComponentTile(playerId1, 6, 7, 0);
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

        assertTrue(() -> gameManager.getDiscardedComponentTiles(new UUID(0,0)).contains(comp));
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
        assertThrows(InvalidFunctionCallInState.class, () -> gameManager.addPlayer(new Player(playerId1, "Schumi", Color.RED, new ShipManager(2))));
    }

    @Test
    void initializeAdventureDeckTest() {
        gameManager.initializeAdventureDeck();

        assertNotEquals(gameManager.getAdventureDeck().getCards().size(), 0);

        for(int i=0;i<gameManager.getAdventureDeck().getCards().size();i++){
            System.out.println(gameManager.getAdventureDeck().getCards().get(i).toString());
        }
    }
}