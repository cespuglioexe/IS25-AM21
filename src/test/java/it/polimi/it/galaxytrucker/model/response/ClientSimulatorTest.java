package it.polimi.it.galaxytrucker.model.response;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import it.polimi.it.galaxytrucker.model.gamestates.AttackAftermathFixingState;
import it.polimi.it.galaxytrucker.model.gamestates.BuildingState;
import it.polimi.it.galaxytrucker.model.gamestates.ConnectionState;
import it.polimi.it.galaxytrucker.model.gamestates.GameEndState;
import it.polimi.it.galaxytrucker.model.gamestates.ShipCreation;
import it.polimi.it.galaxytrucker.model.managers.GameManager;
import it.polimi.it.galaxytrucker.model.managers.Player;
import it.polimi.it.galaxytrucker.model.managers.ShipManager;
import it.polimi.it.galaxytrucker.model.utility.Color;

public class ClientSimulatorTest {
    private GameManager model;
    private Client client1;
    private Client client2;

    @BeforeEach
    private void initialize() {
        model = new GameManager(2, 2);
        client1 = new Client(model);
        client2 = new Client(model);

        model.addListener(client1);
        model.addListener(client2);
    }

    @Test
    void connectionTest() {
        assertEquals(ConnectionState.class, model.getCurrentState().getClass());

        Player player1 = new Player(UUID.randomUUID(), "Margarozzo", Color.RED, new ShipManager(2, Color.RED));
        Player player2 = new Player(UUID.randomUUID(), "Ing. Conti", Color.BLUE, new ShipManager(2, Color.BLUE));

        client1.setClient(player1);
        client2.setClient(player2);

        model.addPlayer(player1);
        model.addPlayer(player2);

        assertEquals(BuildingState.class, model.getCurrentState().getClass());
    }

    @Test
    void buildingTest() {
        connectionTest();
        assertEquals(BuildingState.class, model.getCurrentState().getClass());

        ShipCreation.createLegalShip5(client1.getPlayer());
        ShipCreation.createLegalShip6(client2.getPlayer());

        model.finishBuilding(client1.getPlayer().getPlayerID());
        model.finishBuilding(client2.getPlayer().getPlayerID());

        assertNotEquals(BuildingState.class, model.getCurrentState().getClass());
    }

    @Test
    void cardExecutionTest() {
        buildingTest();

        Class<?> currentState = model.getCurrentState().getClass();

        System.out.format("\n> Current state: %s", currentState.getSimpleName());
        assertTrue(
            currentState.equals(GameEndState.class) ||
            currentState.equals(AttackAftermathFixingState.class)
        );
    }

    @Test
    void multipleCardExecutionTest() {
        final int MULTIPLE = 50;
        for (int i = 0; i < MULTIPLE; i++) {
            initialize();
            cardExecutionTest();
        }
    }
}
