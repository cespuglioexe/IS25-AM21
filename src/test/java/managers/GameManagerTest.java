package managers;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;
import java.util.stream.Collectors;
import java.util.List;

import it.polimi.it.galaxytrucker.exceptions.InvalidActionException;
import it.polimi.it.galaxytrucker.gameStates.*;
import it.polimi.it.galaxytrucker.managers.GameManager;
import it.polimi.it.galaxytrucker.managers.Player;
import it.polimi.it.galaxytrucker.managers.ShipManager;

public class GameManagerTest {
    @Test
    void startGameTest() {
        GameManager manager = startGame();

        assertTrue(manager.getPlayers().isEmpty());
    }

    @Test
    void playersConnectTest() {
        GameManager manager = startGame();

        manager.changeState(new Connection(manager));

        assertTrue(() -> manager.getCurrentState() instanceof Connection);

        manager.addNewPlayer("Margara");
        manager.addNewPlayer("Conti");

        assertThrows(InvalidActionException.class, () -> manager.addNewPlayer("Margara"));

        manager.addNewPlayer("D'Abate");
        manager.addNewPlayer("Figini");

        assertTrue(() -> manager.allPlayersConnected());

        manager.changeState(new Building(manager));

        for (Player player : manager.getPlayers()) {
            assertTrue(() -> Optional.ofNullable(player.getShipManager()).isPresent());
        }

        assertTrue(manager.getPlayers().stream()
                .map(player -> player.getPlayerID())
                .distinct()
                .count() == manager.getNumberOfPlayers());
    }

    @Test
    void playersBuildTest() {
        GameManager manager = playersConnect();
        List<Player> players = manager.getPlayers();
        List<ShipManager> ships = players.stream()
            .map(player -> player.getShipManager())
            .collect(Collectors.toList());

        manager.changeState(new Building(manager));

        //simulation of ship building
    }

    private GameManager startGame() {
        GameManager manager = new GameManager();

        manager.setLevel(1);
        manager.setNumberOfPlayers(4);

        manager.changeState(new Building(manager));

        return manager;
    }

    private GameManager playersConnect() {
        GameManager manager = startGame();

        manager.changeState(new Connection(manager));

        manager.addNewPlayer("Margara");
        manager.addNewPlayer("Conti");
        manager.addNewPlayer("D'Abate");
        manager.addNewPlayer("Figini");

        return manager;
    }
}
