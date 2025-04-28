package managers;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;

import it.polimi.it.galaxytrucker.model.managers.Model;
import it.polimi.it.galaxytrucker.model.exceptions.InvalidActionException;
import it.polimi.it.galaxytrucker.model.gameStates.BuildingState;
import it.polimi.it.galaxytrucker.model.gameStates.ConnectionState;
import it.polimi.it.galaxytrucker.model.managers.GameManager;

public class GameManagerTest {
    private Model model;
    private final int level = 1;
    private final int numberOfPlayers = 4;

    @BeforeEach
    void start() {
        model = new GameManager(level, numberOfPlayers, null, "Frigeri");
    }

    @Test
    void playerConnectsTest() {
        assertEquals(ConnectionState.class, model.getCurrentState().getClass());

        model.addPlayer("Margara");

        assertTrue(() -> model.getPlayers().stream()
            .map(player -> player.getPlayerName())
            .anyMatch(name -> name.equals("Margara"))
        );
    }

    @Test
    void allPlayersConnectTest() {
        assertEquals(ConnectionState.class, model.getCurrentState().getClass());

        addAllPlayers();

        assertEquals(BuildingState.class, model.getCurrentState().getClass());
    }
    private void addAllPlayers() {
        model.addPlayer("Margara");
        model.addPlayer("Ing. Conti");
        model.addPlayer("D'Abate");
        model.addPlayer("Balzarini");
    }

    @Test
    void playerConnectsToFullGameTest() {
        assertEquals(ConnectionState.class, model.getCurrentState().getClass());

        addAllPlayers();
        assertThrows(InvalidActionException.class, () -> model.addPlayer("Tomino"));
    }
}
