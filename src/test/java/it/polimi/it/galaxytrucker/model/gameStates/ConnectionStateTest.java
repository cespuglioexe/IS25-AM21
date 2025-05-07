package it.polimi.it.galaxytrucker.model.gameStates;

import it.polimi.it.galaxytrucker.model.exceptions.InvalidFunctionCallInState;
import it.polimi.it.galaxytrucker.model.managers.GameManager;
import it.polimi.it.galaxytrucker.model.managers.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ConnectionStateTest {
    private ConnectionState connectionState;
    private GameManager gameManager;

    @BeforeEach
    void initializeParameters() {
        connectionState = new ConnectionState();
        gameManager = new GameManager(2, 3, "game");
    }

    @Test
    void addPlayerTest () {
        connectionState.addPlayer(gameManager ,"Frigeri");

        assertEquals(1, gameManager.getPlayers().size());
        assertTrue(gameManager.getPlayers().stream()
            .map(Player::getPlayerName)
            .anyMatch(name -> name.equals("Frigeri"))
        );
    }

    @Test
    void removePlayerTest () {
        UUID playerId1 = connectionState.addPlayer(gameManager ,"Frigeri");
        UUID playerId2 = connectionState.addPlayer(gameManager, "Margarozzo");

        connectionState.removePlayer(gameManager, playerId1);

        assertEquals(1, gameManager.getPlayers().size());
        assertTrue(gameManager.getPlayers().stream()
                .map(Player::getPlayerName)
                .noneMatch(name -> name.equals("Frigeri"))
        );

        connectionState.removePlayer(gameManager, playerId2);

        assertEquals(0, gameManager.getPlayers().size());
        assertTrue(gameManager.getPlayers().stream()
                .map(Player::getPlayerName)
                .noneMatch(name -> name.equals("Frigeri"))
        );
    }

    @Test
    void nonValidFunctionTest () {
        UUID playerId = connectionState.addPlayer(gameManager ,"Ing.Conti");
        InvalidFunctionCallInState e = assertThrows(InvalidFunctionCallInState.class, () -> connectionState.placeComponentTile(gameManager, playerId, 4, 6));
        System.out.println(e.getMessage());
    }
}