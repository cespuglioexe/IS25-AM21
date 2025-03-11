package it.polimi.it.galaxytrucker.managers;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class GameManagerTest {

    @Test
    void makeComponentTilePool() {
        GameManager gameManager = new GameManager();
        gameManager.makeComponentTilePool();
        assertEquals(1,1);
    }
}