package adventureCards.meteorSwarm;

import adventureCards.meteorSwarm.ShipCreation;
import it.polimi.it.galaxytrucker.model.adventurecards.cardstates.meteorSwarm.BigMeteorState;
import it.polimi.it.galaxytrucker.model.adventurecards.cardstates.meteorSwarm.EndState;
import it.polimi.it.galaxytrucker.model.adventurecards.cardstates.meteorSwarm.SmallMeteorState;
import it.polimi.it.galaxytrucker.model.adventurecards.interfaces.attack.Projectile;
import it.polimi.it.galaxytrucker.model.adventurecards.refactored.MeteorSwarm;
import it.polimi.it.galaxytrucker.model.managers.FlightBoard;
import it.polimi.it.galaxytrucker.model.managers.FlightBoardFlightRules;
import it.polimi.it.galaxytrucker.model.managers.Player;
import it.polimi.it.galaxytrucker.model.managers.ShipManager;
import it.polimi.it.galaxytrucker.model.utility.Color;
import it.polimi.it.galaxytrucker.model.utility.Direction;
import it.polimi.it.galaxytrucker.model.utility.ProjectileType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class MeteorSwarmTest {
    private MeteorSwarm card;

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

        ShipCreation.createShip1(player1);
        ShipCreation.createShip2(player2);
        ShipCreation.createShip3(player3);
        ShipCreation.createShip4(player4);

        List<Projectile> projectiles = new ArrayList<>();
        projectiles.add(new Projectile(ProjectileType.BIG, Direction.DOWN));
        projectiles.add(new Projectile(ProjectileType.SMALL,Direction.RIGHT));
        projectiles.add(new Projectile(ProjectileType.SMALL,Direction.LEFT));

        card = new MeteorSwarm(projectiles,new FlightBoardFlightRules(flightBoard));
        card.play();
    }

    @Test
    void initializeProjectileTest() {
        int i=0;
        for(Projectile projectile: card.getProjectiles().stream().toList()){
            assertNotEquals(card.getAimedCoordsByProjectile(projectile).stream().toList().getFirst(),0);
            System.out.print(("Meteor "+i+" : "+card.getAimedCoordsByProjectile(projectile).stream().toList().get(0)));
            assertNotEquals(card.getAimedCoordsByProjectile(projectile).stream().toList().get(1),0);
            System.out.println((" "+card.getAimedCoordsByProjectile(projectile).stream().toList().get(1)));
            i++;
        }
        System.out.println("Tot Meteor : "+card.getProjectiles().stream().toList().size());
        assertTrue(card.getProjectiles().stream().toList().size()==3);
    }

    @Test
    void leaderPlaysCardTest() {
        initializeProjectileTest();

        assertEquals(BigMeteorState.class, card.getCurrentState().getClass());
        card.shootAtMeteorWith(List.of(6,5),List.of(9,5));
        assertEquals(SmallMeteorState.class, card.getCurrentState().getClass());
        HashMap<List<Integer>,List<Integer>> shieldBattery = new HashMap<>();
        Player currentPlayer = card.getPlayer();
        shieldBattery.put(List.of(8,10),List.of(9,5));
        card.activateShields(shieldBattery);
        currentPlayer.getShipManager().printBoard();
        assertEquals(SmallMeteorState.class, card.getCurrentState().getClass());
        card.activateNoShield();
        assertNotEquals(currentPlayer, card.getPlayer());
    }

    private void playCard(){
        assertEquals(BigMeteorState.class, card.getCurrentState().getClass());
        card.noShootAtMeteorWith();
        assertEquals(SmallMeteorState.class, card.getCurrentState().getClass());
        card.activateNoShield();
        assertEquals(SmallMeteorState.class, card.getCurrentState().getClass());
        card.activateNoShield();
    }

/*
    @Test
    void allPlayerCardTest() {
        leaderPlaysCardTest();

        playCard();
        playCard();
        playCard();
        assertEquals(EndState.class, card.getCurrentState().getClass());
    }
 */
}
