package it.polimi.it.galaxytrucker.adventurecards.pirates;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import it.polimi.it.galaxytrucker.model.adventurecards.cards.Pirates;
import it.polimi.it.galaxytrucker.model.adventurecards.cardstates.pirates.*;
import it.polimi.it.galaxytrucker.model.adventurecards.interfaces.attack.Projectile;
import it.polimi.it.galaxytrucker.model.managers.FlightBoard;
import it.polimi.it.galaxytrucker.model.managers.FlightBoardFlightRules;
import it.polimi.it.galaxytrucker.model.managers.Player;
import it.polimi.it.galaxytrucker.model.managers.ShipManager;
import it.polimi.it.galaxytrucker.model.utility.Color;
import it.polimi.it.galaxytrucker.model.utility.Direction;
import it.polimi.it.galaxytrucker.model.utility.ProjectileType;
import it.polimi.it.galaxytrucker.model.adventurecards.cardstates.EndState;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
        player1 = new Player(UUID.randomUUID(), "Margarozzo", Color.BLUE, new ShipManager(gameLevel, Color.BLUE));
        player2 = new Player(UUID.randomUUID(), "Ing. Conti", Color.RED, new ShipManager(gameLevel, Color.BLUE));
        player3 = new Player(UUID.randomUUID(), "D'Abate", Color.YELLOW, new ShipManager(gameLevel, Color.BLUE));
        player4 = new Player(UUID.randomUUID(), "Balzarini", Color.GREEN, new ShipManager(gameLevel, Color.BLUE));

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
        System.out.println("Player "+player1.getPlayerName()+" FirePower : "+player1.getShipManager().calculateFirePower());
        System.out.println("Player "+player2.getPlayerName()+" FirePower : "+player2.getShipManager().calculateFirePower());
        System.out.println("Player "+player3.getPlayerName()+" FirePower : "+player3.getShipManager().calculateFirePower());
        System.out.println("Player "+player4.getPlayerName()+" FirePower : "+player4.getShipManager().calculateFirePower());

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
            System.out.println(("Meteor "+i+" : "+card.getRolledProjectileAndCoord().get(projectile).intValue()));
            assertNotEquals(card.getRolledProjectileAndCoord().get(projectile).intValue(),0);
            i++;
        }
        System.out.println("\nTot Meteor : "+card.getProjectiles().stream().toList().size());
        assertTrue(card.getProjectiles().stream().toList().size()==3);
    }

    @Test
    void notSelectAllPlayersTest() {
        initializeProjectileTest();

        assertEquals(CalculateFirePowerState.class, card.getCurrentState().getClass());
        doubleCannon.put(List.of(7,10),List.of(9,5));
        card.selectCannons(doubleCannon);
        assertEquals(CalculateFirePowerState.class, card.getCurrentState().getClass());
        card.selectNoCannons();
        card.selectNoCannons();
        assertEquals(CalculateFirePowerState.class, card.getCurrentState().getClass());
    }

    @Test
    void defeatPiratesTest() {
        initializeProjectileTest();

        assertEquals(CalculateFirePowerState.class, card.getCurrentState().getClass());
        doubleCannon.put(List.of(7,10),List.of(9,5));
        doubleCannon.put(List.of(6,5),List.of(9,5));
        doubleCannon.put(List.of(6,9),List.of(9,6));
        card.selectCannons(doubleCannon);
        assertEquals(CalculateFirePowerState.class, card.getCurrentState().getClass());
        card.selectNoCannons();
        doubleCannon.clear();
        doubleCannon.put(List.of(6,5),List.of(9,5));
        card.selectCannons(doubleCannon);
        card.selectNoCannons();
        assertEquals(CreditRewardState.class, card.getCurrentState().getClass());
    }

    @Test
    void leaderGetReward(){
        defeatPiratesTest();
        Player currentPlayer = card.getPlayer();
        int credit = currentPlayer.getCredits();
        card.applyCreditReward();
        assertNotEquals(credit,currentPlayer.getCredits());
        assertEquals(ActivateShieldState.class, card.getCurrentState().getClass());
    }

    @Test
    void leaderDoesNotGetReward(){
        defeatPiratesTest();
        Player currentPlayer = card.getPlayer();
        int prePos = flightBoard.getPlayerPosition().get(currentPlayer);
        int credit = currentPlayer.getCredits();
        card.discardCreditReward();
        assertEquals(credit,currentPlayer.getCredits());
        assertEquals(prePos,flightBoard.getPlayerPosition().get(currentPlayer));
        assertEquals(ActivateShieldState.class, card.getCurrentState().getClass());
    }

    @Test
    void loserPlayerActiveShield(){
        leaderGetReward();
        HashMap<List<Integer>,List<Integer>> shieldBattery = new HashMap<>();
        Player currentPlayer = card.getPlayer();
        shieldBattery.put(List.of(7,9),List.of(9,5));
        shieldBattery.put(List.of(8,4),List.of(9,5));
        currentPlayer.getShipManager().printBoard();
        card.activateShields(shieldBattery);
        for(Projectile p : card.getProjectiles()){
            System.out.println("Coordinate proiettili (ROW):  " +  card.getAimedCoordsByProjectile(p).getFirst());
            System.out.println("Coordinate proiettili (COLUMN):  " +  card.getAimedCoordsByProjectile(p).get(1));
        }

        currentPlayer.getShipManager().printBoard();

        assertEquals(ActivateShieldState.class, card.getCurrentState().getClass());
    }

    @Test
    void allPlayersActivateShieldsTest(){
        loserPlayerActiveShield();
        Player currentPlayer = card.getPlayer();
        currentPlayer.getShipManager().printBoard();
        currentPlayer = card.getPlayer();
        card.activateNoShield();
        currentPlayer.getShipManager().printBoard();
        assertEquals(EndState.class, card.getCurrentState().getClass());
    }
}
