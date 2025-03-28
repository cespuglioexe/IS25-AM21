package managers;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;

import it.polimi.it.galaxytrucker.managers.Model;
import it.polimi.it.galaxytrucker.exceptions.InvalidActionException;
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
        model.setLevel(2);

        assertTrue(() -> model.getCurrentState().getClass().equals(StartState.class));
        
        model.setNumberOfPlayers(4);

        assertTrue(() -> model.getCurrentState().getClass().equals(ConnectionState.class));
    }
}
