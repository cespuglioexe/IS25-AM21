package adventureCards.piratesTest;

import adventureCards.epidemic.ShipCreation;
import it.polimi.it.galaxytrucker.model.adventurecards.refactored.Epidemic;
import it.polimi.it.galaxytrucker.model.adventurecards.refactored.Pirates;
import it.polimi.it.galaxytrucker.model.managers.FlightBoard;
import it.polimi.it.galaxytrucker.model.managers.FlightBoardFlightRules;
import it.polimi.it.galaxytrucker.model.managers.Player;
import it.polimi.it.galaxytrucker.model.managers.ShipManager;
import it.polimi.it.galaxytrucker.model.utility.Color;
import it.polimi.it.galaxytrucker.model.utility.Direction;
import it.polimi.it.galaxytrucker.model.utility.Projectile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class PiratesTest {
    private Pirates card;

    private final int gameLevel = 2;

    private Player player1;
    private Player player2;
    private Player player3;
    private Player player4;

    private FlightBoard flightBoard;

    @BeforeEach
    void initializeParameters() {
        player1 = new Player(UUID.randomUUID(), "Margarozzo", Color.BLUE, new ShipManager(gameLevel));
        player2 = new Player(UUID.randomUUID(), "Ing. Conti", Color.RED, new ShipManager(gameLevel));
        player3 = new Player(UUID.randomUUID(), "D'Abate", Color.YELLOW, new ShipManager(gameLevel));
        player4 = new Player(UUID.randomUUID(), "Balzarini", Color.GREEN, new ShipManager(gameLevel));

        flightBoard = new FlightBoard(gameLevel);

        flightBoard.addPlayerMarker(player1);
        flightBoard.addPlayerMarker(player2);
        flightBoard.addPlayerMarker(player3);
        flightBoard.addPlayerMarker(player4);

        adventureCards.epidemic.ShipCreation.createShip1(player1);
        adventureCards.epidemic.ShipCreation.createShip2(player2);
        adventureCards.epidemic.ShipCreation.createShip3(player3);
        ShipCreation.createShip4(player4);

        HashMap<Projectile, Direction> projectile = new HashMap<>();
        projectile.put(Projectile.SMALL,Direction.DOWN);
        projectile.put(Projectile.BIG,Direction.DOWN);
        projectile.put(Projectile.SMALL,Direction.DOWN);

        card = new Pirates(5,4,1,projectile,new FlightBoardFlightRules(flightBoard));
        card.play();
    }

    @Test
    void initializeProjectileTest() {
        for(Projectile projectile: card.getProjectilesAndDirection().keySet().stream().toList()){
            assertNotEquals(card.getProjectilesAndAimedComponent().get(projectile).stream().toList().get(0),0);
            System.out.println((card.getProjectilesAndAimedComponent().get(projectile).stream().toList().get(0)));
            assertNotEquals(card.getProjectilesAndAimedComponent().get(projectile).stream().toList().get(1),0);
            System.out.println((card.getProjectilesAndAimedComponent().get(projectile).stream().toList().get(0)));
        }
    }

}
