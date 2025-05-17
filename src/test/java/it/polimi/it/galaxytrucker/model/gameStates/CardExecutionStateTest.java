package it.polimi.it.galaxytrucker.model.gameStates;

import it.polimi.it.galaxytrucker.model.adventurecards.AdventureDeck;
import it.polimi.it.galaxytrucker.model.adventurecards.cards.Planets;
import it.polimi.it.galaxytrucker.model.adventurecards.interfaces.AdventureCard;
import it.polimi.it.galaxytrucker.model.managers.FlightBoardFlightRules;
import it.polimi.it.galaxytrucker.model.managers.GameManager;
import it.polimi.it.galaxytrucker.model.managers.Player;
import it.polimi.it.galaxytrucker.model.managers.ShipManager;
import it.polimi.it.galaxytrucker.model.utility.Cargo;
import it.polimi.it.galaxytrucker.model.utility.Color;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
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
        
    }
}