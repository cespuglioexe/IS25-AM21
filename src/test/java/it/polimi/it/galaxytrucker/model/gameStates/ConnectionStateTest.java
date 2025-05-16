package it.polimi.it.galaxytrucker.model.gameStates;

import it.polimi.it.galaxytrucker.exceptions.InvalidFunctionCallInState;
import it.polimi.it.galaxytrucker.model.gamestates.ConnectionState;
import it.polimi.it.galaxytrucker.model.managers.GameManager;
import it.polimi.it.galaxytrucker.model.managers.Player;
import it.polimi.it.galaxytrucker.model.managers.ShipManager;
import it.polimi.it.galaxytrucker.model.utility.Color;
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
        gameManager = new GameManager(2, 3);
    }

    @Test
    void addPlayerTest () {
        gameManager.addPlayer(new Player(UUID.randomUUID(), "Frigeri", Color.RED, new ShipManager(2)));

        assertEquals(1, gameManager.getPlayers().size());
        assertTrue(gameManager.getPlayers().stream()
            .map(Player::getPlayerName)
            .anyMatch(name -> name.equals("Frigeri"))
        );
    }

    @Test
    void removePlayerTest () {
        UUID playerId1 = UUID.randomUUID();
        UUID playerId2 = UUID.randomUUID();

        gameManager.addPlayer(new Player(playerId1, "Frigeri", Color.RED, new ShipManager(2)));
        gameManager.addPlayer(new Player(playerId2, "Blazarini", Color.RED, new ShipManager(2)));

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
        UUID playerId = UUID.randomUUID();

        gameManager.addPlayer(new Player(playerId, "Ing. Conti", Color.RED, new ShipManager(2)));
        InvalidFunctionCallInState e = assertThrows(InvalidFunctionCallInState.class, () -> connectionState.placeComponentTile(gameManager, playerId, 4, 6));
        System.out.println(e.getMessage());
    }
}