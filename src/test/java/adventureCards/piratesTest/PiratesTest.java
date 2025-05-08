package adventureCards.piratesTest;

import adventureCards.piratesTest.ShipCreation;
import it.polimi.it.galaxytrucker.model.adventurecards.cardstates.pirates.*;
import it.polimi.it.galaxytrucker.model.adventurecards.interfaces.attack.Projectile;
import it.polimi.it.galaxytrucker.model.adventurecards.refactored.Pirates;
import it.polimi.it.galaxytrucker.model.componenttiles.Shield;
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

public class PiratesTest {
    private Pirates card;

    private final int gameLevel = 2;

    private Player player1;
    private Player player2;
    private Player player3;
    private Player player4;

    private FlightBoard flightBoard;

    HashMap<List<Integer>,List<Integer>> doubleCannon;

    @BeforeEach
    void initializeParameters() {
        player1 = new Player(UUID.randomUUID(), "Margarozzo", Color.BLUE, new ShipManager(gameLevel));
        player2 = new Player(UUID.randomUUID(), "Ing. Conti", Color.RED, new ShipManager(gameLevel));
        player3 = new Player(UUID.randomUUID(), "D'Abate", Color.YELLOW, new ShipManager(gameLevel));
        player4 = new Player(UUID.randomUUID(), "Balzarini", Color.GREEN, new ShipManager(gameLevel));

        flightBoard = new FlightBoard(gameLevel);
        doubleCannon = new HashMap<List<Integer>,List<Integer>>();

        flightBoard.addPlayerMarker(player1);
        flightBoard.addPlayerMarker(player2);
        flightBoard.addPlayerMarker(player3);
        flightBoard.addPlayerMarker(player4);

        ShipCreation.createShip1(player1);
        ShipCreation.createShip2(player2);
        ShipCreation.createShip3(player3);
        ShipCreation.createShip4(player4);
        System.out.println("Player 1 - FirePower : "+player1.getShipManager().calculateFirePower());
        System.out.println("Player 2 - FirePower : "+player2.getShipManager().calculateFirePower());
        System.out.println("Player 3 - FirePower : "+player3.getShipManager().calculateFirePower());
        System.out.println("Player 4 - FirePower : "+player4.getShipManager().calculateFirePower());

        List<Projectile> projectiles = new ArrayList<>();
        projectiles.add(new Projectile(ProjectileType.SMALL,Direction.DOWN));
        projectiles.add(new Projectile(ProjectileType.BIG,Direction.DOWN));
        projectiles.add(new Projectile(ProjectileType.SMALL,Direction.DOWN));

        card = new Pirates(9,4,1,projectiles,new FlightBoardFlightRules(flightBoard));
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
    void defeatPiratesTest() {
        initializeProjectileTest();
        assertEquals(CalculateFirePowerState.class, card.getCurrentState().getClass());
        doubleCannon.put(List.of(7,10),List.of(9,5));
        card.selectCannons(doubleCannon);
        assertEquals(CalculateFirePowerState.class, card.getCurrentState().getClass());
        card.selectNoCannons();
        card.selectNoCannons();
        card.selectNoCannons();
        assertEquals(CreditRewardState.class, card.getCurrentState().getClass());
    }

    @Test
    void leaderGetReward(){
        defeatPiratesTest();
        card.applyCreditReward();
        assertEquals(ActivateShieldState.class, card.getCurrentState().getClass());
    }

    @Test
    void loserPlayerActiveShield(){
        leaderGetReward();
        HashMap<List<Integer>,List<Integer>> shieldBattery = new HashMap<>();
        shieldBattery.put(List.of(7,9),List.of(9,5));
        shieldBattery.put(List.of(8,4),List.of(9,5));
        card.activateShields(shieldBattery);
        assertEquals(ActivateShieldState.class, card.getCurrentState().getClass());
    }


}
