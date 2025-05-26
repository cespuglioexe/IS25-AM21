package it.polimi.it.galaxytrucker.model.gamestates;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import it.polimi.it.galaxytrucker.model.adventurecards.AdventureDeck;
import it.polimi.it.galaxytrucker.model.adventurecards.cards.MeteorSwarm;
import it.polimi.it.galaxytrucker.model.adventurecards.interfaces.AdventureCard;
import it.polimi.it.galaxytrucker.model.adventurecards.interfaces.attack.Projectile;
import it.polimi.it.galaxytrucker.model.managers.FlightBoardFlightRules;
import it.polimi.it.galaxytrucker.model.managers.GameManager;
import it.polimi.it.galaxytrucker.model.managers.Player;
import it.polimi.it.galaxytrucker.model.managers.ShipManager;
import it.polimi.it.galaxytrucker.model.utility.Color;
import it.polimi.it.galaxytrucker.model.utility.Direction;
import it.polimi.it.galaxytrucker.model.utility.ProjectileType;

public class AttackAftermathFixingStateTest {
    private GameManager gameManager;

    private final UUID playerId1 = UUID.randomUUID();
    private final UUID playerId2 = UUID.randomUUID();
    private final UUID playerId3 = UUID.randomUUID();

    @BeforeEach
    void initializeParameters() {
        gameManager = new GameManager(2, 3);
        
        fillDeckWithMeteorSwarmCards();

        gameManager.addPlayer(new Player(playerId1, "Margarozzo", Color.RED, new ShipManager(2)));
        gameManager.addPlayer(new Player(playerId2, "Blazarini", Color.GREEN, new ShipManager(2)));
        gameManager.addPlayer(new Player(playerId3, "Ing. Conti", Color.YELLOW, new ShipManager(2)));

        playersBuildAllLegalShips();
        playMeteorSwarmCard();

        for (Player player : gameManager.getActivePlayers()) {
            System.out.printf("%n%n%s SHIP:%n", player.getPlayerName());
            player.getShipManager().printBoard();
        }
    }
    private void playersBuildAllLegalShips() {
        // All three players have legal ships
        ShipCreation.createLegalShip4(gameManager.getPlayerByID(playerId1));
        ShipCreation.createLegalShip4(gameManager.getPlayerByID(playerId2));
        ShipCreation.createLegalShip4(gameManager.getPlayerByID(playerId3));

        gameManager.finishBuilding(playerId1);
        gameManager.finishBuilding(playerId2);
        gameManager.finishBuilding(playerId3);
    }
    private void fillDeckWithMeteorSwarmCards() {
        AdventureDeck deck = gameManager.getAdventureDeck();

        List<Projectile> cannonBalls = List.of(
            new Projectile(ProjectileType.BIG, Direction.DOWN),
            new Projectile(ProjectileType.BIG, Direction.DOWN),
            new Projectile(ProjectileType.BIG, Direction.DOWN),
            new Projectile(ProjectileType.BIG, Direction.LEFT),
            new Projectile(ProjectileType.BIG, Direction.RIGHT)
        );

        MeteorSwarm card1 = new MeteorSwarm(cannonBalls, new FlightBoardFlightRules(gameManager.getFlightBoard()));
        MeteorSwarm card2 = new MeteorSwarm(cannonBalls, new FlightBoardFlightRules(gameManager.getFlightBoard()));

        List<AdventureCard> cards = new ArrayList<>();
        cards.add(card1);
        cards.add(card2);
        
        deck.initializeAdventureCards(cards);
    }
    private void playMeteorSwarmCard() {
        MeteorSwarm card = (MeteorSwarm) gameManager.getAdventureDeck().getLastDrawnCard();

        card.notShootAtMeteor();
        card.notShootAtMeteor();
        card.notShootAtMeteor();
        card.notShootAtMeteor();
        card.notShootAtMeteor();

        card.notShootAtMeteor();
        card.notShootAtMeteor();
        card.notShootAtMeteor();
        card.notShootAtMeteor();
        card.notShootAtMeteor();

        card.notShootAtMeteor();
        card.notShootAtMeteor();
        card.notShootAtMeteor();
        card.notShootAtMeteor();
        card.notShootAtMeteor();
        System.out.println(card.getCurrentState().getClass().getSimpleName());
    }

    @Test
    void fixingTest() {
        System.out.println("AFTER FIXING:");
        for (Player player : gameManager.getActivePlayers()) {
            ShipManager ship = player.getShipManager();

            List<Set<List<Integer>>> disconnectedBranches = ship.getDisconnectedBranches();

            while (disconnectedBranches.size() > 1) {
                Set<List<Integer>> branch = disconnectedBranches.remove(0);
                gameManager.deleteBranch(player.getPlayerID(), branch);
            }

            gameManager.finishBuilding(player.getPlayerID());
            System.out.printf("%n%n%s SHIP:%n", player.getPlayerName());
            player.getShipManager().printBoard();
        }

        assertEquals(CardExecutionState.class, gameManager.getCurrentState().getClass());
    }
}
