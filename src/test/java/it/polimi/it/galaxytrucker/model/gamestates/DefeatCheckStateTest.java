package it.polimi.it.galaxytrucker.model.gamestates;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import it.polimi.it.galaxytrucker.model.adventurecards.AdventureDeck;
import it.polimi.it.galaxytrucker.model.adventurecards.cards.MeteorSwarm;
import it.polimi.it.galaxytrucker.model.adventurecards.cards.Planets;
import it.polimi.it.galaxytrucker.model.adventurecards.interfaces.AdventureCard;
import it.polimi.it.galaxytrucker.model.adventurecards.interfaces.attack.Projectile;
import it.polimi.it.galaxytrucker.model.managers.FlightBoard;
import it.polimi.it.galaxytrucker.model.managers.FlightBoardFlightRules;
import it.polimi.it.galaxytrucker.model.managers.GameManager;
import it.polimi.it.galaxytrucker.model.managers.Player;
import it.polimi.it.galaxytrucker.model.managers.ShipManager;
import it.polimi.it.galaxytrucker.model.utility.Cargo;
import it.polimi.it.galaxytrucker.model.utility.Color;
import it.polimi.it.galaxytrucker.model.utility.Direction;
import it.polimi.it.galaxytrucker.model.utility.ProjectileType;

public class DefeatCheckStateTest {
    private GameManager gameManager;

    private final UUID playerId1 = UUID.randomUUID();
    private final UUID playerId2 = UUID.randomUUID();
    private final UUID playerId3 = UUID.randomUUID();

    @Nested
    class PlanetCardsInDeck {
        @BeforeEach
        void initializeParameters() {
            gameManager = new GameManager(2, 3);
            
            fillDeckWithPlanetsCards();

            gameManager.addPlayer(new Player(playerId1, "Margarozzo", Color.RED, new ShipManager(2, Color.BLUE)));
            gameManager.addPlayer(new Player(playerId2, "Balzarini", Color.GREEN, new ShipManager(2, Color.BLUE)));
            gameManager.addPlayer(new Player(playerId3, "Ing. Conti", Color.YELLOW, new ShipManager(2, Color.BLUE)));

            playersBuildAllLegalShips();
        }
        private void playersBuildAllLegalShips() {
            // All three players have legal ships
            ShipCreation.createLegalShip1(gameManager.getPlayerByID(playerId1));
            ShipCreation.createLegalShip2(gameManager.getPlayerByID(playerId2));
            ShipCreation.createLegalShip3(gameManager.getPlayerByID(playerId3));

            gameManager.finishBuilding(playerId1);
            gameManager.finishBuilding(playerId2);
            gameManager.finishBuilding(playerId3);
        }
        private void fillDeckWithPlanetsCards() {
            AdventureDeck deck = gameManager.getAdventureDeck();

            final int numberOfPlanets = 3;
            final List<Cargo> cargoPlanet1 = List.of(new Cargo(Color.GREEN), new Cargo(Color.GREEN));
            final List<Cargo> cargoPlanet2 = List.of(new Cargo(Color.YELLOW));
            final List<Cargo> cargoPlanet3 = List.of(new Cargo(Color.BLUE), new Cargo(Color.BLUE),new Cargo(Color.BLUE));
            final int flightDayPenalty = 1;

            List<List<Cargo>> cargoRewardsByPlanet = List.of(cargoPlanet1, cargoPlanet2, cargoPlanet3);

            Planets card1 = new Planets(numberOfPlanets, cargoRewardsByPlanet, flightDayPenalty, new FlightBoardFlightRules(gameManager.getFlightBoard()));
            Planets card2 = new Planets(numberOfPlanets, cargoRewardsByPlanet, flightDayPenalty, new FlightBoardFlightRules(gameManager.getFlightBoard()));

            List<AdventureCard> cards = new ArrayList<>();
            cards.add(card1);
            cards.add(card2);
            
            deck.initializeAdventureCards(cards);
        }


        @Test
        void defeatCheckNoHumansTest() {
            Player player3 = gameManager.getPlayerByID(playerId3);

            assertTrue(() -> gameManager.getActivePlayers().contains(player3));
            assertTrue(() -> gameManager.getFlightBoard().getPlayerOrder().contains(player3));

            removeHumansFromPlayer3();
            playPlanetCard();

            assertTrue(() -> !gameManager.getActivePlayers().contains(player3));
            assertTrue(() -> !gameManager.getFlightBoard().getPlayerOrder().contains(player3));
        }
        private void playPlanetCard() {
            Planets card = (Planets) gameManager.getAdventureDeck().getLastDrawnCard();

            card.participate(gameManager.getPlayerByID(playerId1), 0);
            card.decline(gameManager.getPlayerByID(playerId2));
            card.participate(gameManager.getPlayerByID(playerId3), 1);

            card.acceptCargo(0, 7, 4);
            card.acceptCargo(0, 7, 4);

            card.acceptCargo(0, 6, 9);
        }
        private void removeHumansFromPlayer3() {
            Player player3 = gameManager.getPlayerByID(playerId3);
            ShipManager ship = player3.getShipManager();
            
            ship.removeCrewmate(7, 7);
            ship.removeCrewmate(7, 7);
        }

        @Test
        void defeatCheckPlayerLappedTest() {
            Player player3 = gameManager.getPlayerByID(playerId3);

            gameManager.getFlightBoard().printFlightBoardState();
            movePlayer1Forward();
            gameManager.getFlightBoard().printFlightBoardState();
            playPlanetCard();
            gameManager.getFlightBoard().printFlightBoardState();

            assertTrue(() -> !gameManager.getActivePlayers().contains(player3));
            assertTrue(() -> !gameManager.getFlightBoard().getPlayerOrder().contains(player3));
        }
        private void movePlayer1Forward() {
            Player player1 = gameManager.getPlayerByID(playerId1);
            FlightBoard flightBoard = gameManager.getFlightBoard();

            flightBoard.movePlayerForward(19, player1);
        }

        @Test
        void gameEndTest() {
            gameManager.getFlightBoard().printFlightBoardState();
            movePlayer2Forward();
            gameManager.getFlightBoard().printFlightBoardState();
            playPlanetCard();
            gameManager.getFlightBoard().printFlightBoardState();

            assertEquals(GameEndState.class, gameManager.getCurrentState().getClass());
        }
        private void movePlayer2Forward() {
            Player player2 = gameManager.getPlayerByID(playerId2);
            FlightBoard flightBoard = gameManager.getFlightBoard();

            flightBoard.movePlayerForward(24, player2);
        }

        @Test
        void nextCardTest() {
            gameManager.getFlightBoard().printFlightBoardState();
            playPlanetCard();
            gameManager.getFlightBoard().printFlightBoardState();

            assertEquals(CardExecutionState.class, gameManager.getCurrentState().getClass());
        }
    }
    
    @Nested
    class AttackCardsInDeck {
        @BeforeEach
        void initializeParameters() {
            gameManager = new GameManager(2, 3);
            
            fillDeckWithMeteorSwarmCards();

            gameManager.addPlayer(new Player(playerId1, "Margarozzo", Color.RED, new ShipManager(2, Color.BLUE)));
            gameManager.addPlayer(new Player(playerId2, "Balzarini", Color.GREEN, new ShipManager(2, Color.BLUE)));
            gameManager.addPlayer(new Player(playerId3, "Ing. Conti", Color.YELLOW, new ShipManager(2, Color.BLUE)));

            playersBuildAllLegalShips();
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

        @Test
        void attackAftermathTest() {
            playMeteorSwarmCard();

            for (Player player : gameManager.getActivePlayers()) {
                System.out.printf("%n%n%s SHIP:%n", player.getPlayerName());
                player.getShipManager().printBoard();
            }

            assertEquals(AttackAftermathFixingState.class, gameManager.getCurrentState().getClass());
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
    }
}
