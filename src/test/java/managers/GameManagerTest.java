package managers;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;

import it.polimi.it.galaxytrucker.managers.Model;
import it.polimi.it.galaxytrucker.exceptions.InvalidActionException;
import it.polimi.it.galaxytrucker.gameStates.BuildingState;
import it.polimi.it.galaxytrucker.gameStates.ConnectionState;
import it.polimi.it.galaxytrucker.gameStates.StartState;
import it.polimi.it.galaxytrucker.managers.GameManager;

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
    void setInvalidLevelTest() {
        assertThrows(InvalidActionException.class, () -> model.setLevel(4));
    }

    @Test
    void setInvalidNumberOfPlayersTest() {
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
