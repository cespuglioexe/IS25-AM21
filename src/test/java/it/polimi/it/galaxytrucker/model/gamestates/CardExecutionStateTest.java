package it.polimi.it.galaxytrucker.model.gamestates;

import it.polimi.it.galaxytrucker.model.adventurecards.AdventureDeck;
import it.polimi.it.galaxytrucker.model.adventurecards.cards.Planets;
import it.polimi.it.galaxytrucker.model.adventurecards.cardstates.EndState;
import it.polimi.it.galaxytrucker.model.adventurecards.interfaces.AdventureCard;
import it.polimi.it.galaxytrucker.model.managers.FlightBoardFlightRules;
import it.polimi.it.galaxytrucker.model.managers.GameManager;
import it.polimi.it.galaxytrucker.model.managers.Player;
import it.polimi.it.galaxytrucker.model.managers.ShipManager;
import it.polimi.it.galaxytrucker.model.utility.Cargo;
import it.polimi.it.galaxytrucker.model.utility.Color;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

class CardExecutionStateTest {
    private GameManager gameManager;

    private final UUID playerId1 = UUID.randomUUID();
    private final UUID playerId2 = UUID.randomUUID();
    private final UUID playerId3 = UUID.randomUUID();

    @BeforeEach
    void initializeParameters() {
        gameManager = new GameManager(2, 3);
        
        fillDeckWithPlanetCard();

        gameManager.addPlayer(new Player(playerId1, "Margarozzo", Color.RED, new ShipManager(2)));
        gameManager.addPlayer(new Player(playerId2, "Blazarini", Color.RED, new ShipManager(2)));
        gameManager.addPlayer(new Player(playerId3, "Ing. Conti", Color.RED, new ShipManager(2)));

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
    private void fillDeckWithPlanetCard() {
        AdventureDeck deck = gameManager.getAdventureDeck();

        final int numberOfPlanets = 3;
        final List<Cargo> cargoPlanet1 = List.of(new Cargo(Color.GREEN), new Cargo(Color.GREEN));
        final List<Cargo> cargoPlanet2 = List.of(new Cargo(Color.YELLOW));
        final List<Cargo> cargoPlanet3 = List.of(new Cargo(Color.BLUE), new Cargo(Color.BLUE),new Cargo(Color.BLUE));
        final int flightDayPenalty = 1;

        List<List<Cargo>> cargoRewardsByPlanet = List.of(cargoPlanet1, cargoPlanet2, cargoPlanet3);

        Planets card = new Planets(numberOfPlanets, cargoRewardsByPlanet, flightDayPenalty, new FlightBoardFlightRules(gameManager.getFlightBoard()));
        List<AdventureCard> cards = new ArrayList<>();
        cards.add(card);
        
        deck.initializeAdventureCards(cards);
    }

    @Test
    void cardExecutionStateTest() {
        Planets card = (Planets) gameManager.getAdventureDeck().getLastDrawnCard();

        printPlanets(card);
        card.participate(gameManager.getPlayerByID(playerId1), 0);

        printPlanets(card);
        card.participate(gameManager.getPlayerByID(playerId2), 1);

        printPlanets(card);
        card.decline(gameManager.getPlayerByID(playerId3));

        printCargoToAccept(card);
        card.acceptCargo(0, 7, 4);

        printCargoToAccept(card);
        card.acceptCargo(0, 7, 4);

        printCargoToAccept(card);
        card.acceptCargo(0, 6, 9);

        assertEquals(EndState.class, card.getCurrentState().getClass());
    }
    private void printPlanets(Planets card) {
        Map<Integer, List<Cargo>> choices = card.getAvailableChoices();

        for (Integer planet : choices.keySet()) {
            List<String> cargoColors = formatCargoToList(choices.get(planet));

            System.out.printf("[%d] -> %s%n", planet, cargoColors);
        }
        System.out.println();
    }
    private List<String> formatCargoToList(List<Cargo> cargo) {
        return cargo.stream()
            .map(c -> c.getColor().toString())
            .toList();
    } 
    private void printCargoToAccept(Planets card) {
        Player player = card.getCurrentPlayer();
        int planet = card.getOccupiedPlanetFromPlayer(player);

        List<String> cargoColors = formatCargoToList(card.getChoices().get(planet));
        
        System.out.printf("%s select a cargo to load: %s%n%n", player.getPlayerName(), cargoColors);
    }
}