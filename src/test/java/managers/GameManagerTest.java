package managers;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;

import it.polimi.it.galaxytrucker.model.managers.Model;
import it.polimi.it.galaxytrucker.model.exceptions.InvalidActionException;
import it.polimi.it.galaxytrucker.model.gameStates.BuildingState;
import it.polimi.it.galaxytrucker.model.gameStates.ConnectionState;
import it.polimi.it.galaxytrucker.model.gameStates.StartState;
import it.polimi.it.galaxytrucker.model.managers.GameManager;

public class GameManagerTest {
    private Model model;

    @BeforeEach
    void start() {
        model = new GameManager();
    }

    @Test
    void checkStartStateTest() {
        assertTrue(() -> model.getCurrentState().getClass().equals(StartState.class));
    }

    @Test
    void setInvalidGameSpecificsTest() {
        assertThrows(InvalidActionException.class, () -> model.setLevel(4));
        assertThrows(InvalidActionException.class, () -> model.setNumberOfPlayers(5));
    }

    @Test
    void setGameSpecificsTest() {
        setGameSpecifics();

        assertEquals(ConnectionState.class, model.getCurrentState().getClass());
    }

    private void setGameSpecifics() {
        model.setLevel(1);
        model.setNumberOfPlayers(4);
    }

    @Test
    void addPlayerAlreadyPresentTest() {
        setGameSpecifics();

        model.addPlayer("Margara");
        assertThrows(InvalidActionException.class, () -> model.addPlayer("Margara"));
    }

    @Test
    void allPlayersConnectedTest() {
        addAllPlayers();

        assertEquals(BuildingState.class, model.getCurrentState().getClass());
    }

    private void addAllPlayers() {
        setGameSpecifics();

        model.addPlayer("Margara");
        model.addPlayer("Ing. Conti");
        model.addPlayer("D'Abate");
        model.addPlayer("Balzarini");
    }
}
