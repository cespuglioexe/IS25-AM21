package it.polimi.it.galaxytrucker.adventurecards;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import it.polimi.it.galaxytrucker.model.adventurecards.cardstates.abandonedship.CrewmatePenaltyState;
import it.polimi.it.galaxytrucker.model.adventurecards.cards.AbandonedShip;
import it.polimi.it.galaxytrucker.model.adventurecards.cardstates.EndState;
import it.polimi.it.galaxytrucker.model.adventurecards.cardstates.abandonedship.ParticipationState;
import it.polimi.it.galaxytrucker.model.componenttiles.CabinModule;
import it.polimi.it.galaxytrucker.model.componenttiles.LifeSupport;
import it.polimi.it.galaxytrucker.model.componenttiles.TileEdge;
import it.polimi.it.galaxytrucker.model.crewmates.Alien;
import it.polimi.it.galaxytrucker.model.crewmates.Human;
import it.polimi.it.galaxytrucker.exceptions.InvalidActionException;
import it.polimi.it.galaxytrucker.model.managers.FlightBoard;
import it.polimi.it.galaxytrucker.model.managers.FlightBoardFlightRules;
import it.polimi.it.galaxytrucker.model.managers.Player;
import it.polimi.it.galaxytrucker.model.managers.ShipManager;
import it.polimi.it.galaxytrucker.model.utility.AlienType;
import it.polimi.it.galaxytrucker.model.utility.Color;

public class AbandonedShipTest {
    private AbandonedShip card;
    private final int creditReward = 4;
    private final int crewmatePenalty = 3;
    private final int flightDayPenalty = 1;

    private final int gameLevel = 2;

    private Player player1;
    private Player player2;
    private Player player3;
    private Player player4;

    private FlightBoard flightBoard;

    @BeforeEach
    void initializeParameters() {
        player1 = new Player(UUID.randomUUID(), "Margara", Color.BLUE, new ShipManager(gameLevel, Color.BLUE));
        player2 = new Player(UUID.randomUUID(), "Ing. Conti", Color.RED, new ShipManager(gameLevel, Color.BLUE));
        player3 = new Player(UUID.randomUUID(), "D'Abate", Color.YELLOW, new ShipManager(gameLevel, Color.BLUE));
        player4 = new Player(UUID.randomUUID(), "Balzarini", Color.GREEN, new ShipManager(gameLevel, Color.BLUE));

        flightBoard = new FlightBoard(gameLevel);

        flightBoard.addPlayerMarker(player1);
        flightBoard.addPlayerMarker(player2);
        flightBoard.addPlayerMarker(player3);
        flightBoard.addPlayerMarker(player4);

        createShip(player1);
        createShip(player2);
        createShip(player3);
        createShip(player4);

        card = new AbandonedShip(creditReward, crewmatePenalty, flightDayPenalty, new FlightBoardFlightRules(flightBoard));

        card.play();
    }
    private void createShip(Player player) {
        ShipManager ship = player.getShipManager();
        Human human = new Human();
        Alien alien = new Alien(AlienType.BROWNALIEN);
        /*
         *     4  5  6  7  8  9  10 
         * 5        [ ]   [ ]      
         * 6     [ ][ ][c][ ][ ]   
         * 7  [ ][l][c][x][ ][ ][ ]
         * 8  [ ][ ][ ][ ][ ][ ][ ]
         * 9  [ ][ ][ ]   [ ][ ][ ]
         * 
         * Where c stands for CabinModule
         * Where l stands for LifeSupport
         * Where x stands for CentralCabin, which has all TileEdge.UNIVERSAL connectors
         */
        ship.addComponentTile(6, 7, new CabinModule(List.of(TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE), null));
        ship.addComponentTile(7, 6, new CabinModule(List.of(TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE), null));
        
        ship.addComponentTile(7, 5, new LifeSupport(AlienType.BROWNALIEN, List.of(TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE), null));

        ship.addCrewmate(6, 7, human);
        ship.addCrewmate(6, 7, human);

        ship.addCrewmate(7, 6, alien);
    }

    @Test
    void playerParticipateTest() {
        assertEquals(ParticipationState.class, card.getCurrentState().getClass());

        printChoices(card.getChoices());

        card.participate(player1, 0);

        Player playerOccupyingCard = card.getTakenChoices().get(0);
        assertEquals(player1, playerOccupyingCard);
    }
    private void printChoices(List<List<Integer>> choices) {
        System.out.println("[0]: " + choices.get(0).get(0) + "$");
    }

    @Test
    void playersAllDeclineTest() {
        assertEquals(ParticipationState.class, card.getCurrentState().getClass());

        printChoices(card.getChoices());

        card.decline(player1);
        card.decline(player2);
        card.decline(player3);
        card.decline(player4);

        assertEquals(EndState.class, card.getCurrentState().getClass());
    }

    @Test
    void playerPartecipateToAlreadyTakenShipTest() {
        assertEquals(ParticipationState.class, card.getCurrentState().getClass());

        printChoices(card.getChoices());

        card.participate(player1, 0);

        assertThrows(InvalidActionException.class, () -> card.participate(player2, 0));
    }

    @Test
    void playersDeclineAndParticipateTest() {
        assertEquals(ParticipationState.class, card.getCurrentState().getClass());

        playersDeclineAndParticipate();

        Player playerOccupyingCard = card.getTakenChoices().get(0);
        assertEquals(player4, playerOccupyingCard);
        assertEquals(CrewmatePenaltyState.class, card.getCurrentState().getClass());
    }
    private void playersDeclineAndParticipate() {
        printChoices(card.getChoices());

        card.decline(player1);
        card.decline(player2);
        card.decline(player3);
        card.participate(player4, 0);

        printPartecipants();
    }
    private void printPartecipants() {
        HashMap<Integer, Player> occupiedShips = card.getTakenChoices();

        System.out.println("Occupied planets:");
        for (Integer ship : occupiedShips.keySet()) {
            Player player = occupiedShips.get(ship);
            System.out.println("[" + ship + "] -> " + player.getPlayerName() + ": " + player.getColor());
        }
    }

    @Test
    void appliedCreditRewardTest() {
        assertEquals(0, player4.getCredits());

        playersDeclineAndParticipate();

        assertEquals(4, player4.getCredits());
    }

    @Test
    void crewmatePenaltySingleCrewmateTest() {
        playersDeclineAndParticipate();

        card.applyCrewmatePenalty(6, 7);

        assertEquals(CrewmatePenaltyState.class, card.getCurrentState().getClass());
    }

    @Test
    void crewmatePenaltyTest() {
        playersDeclineAndParticipate();

        chosenCrewmatePenalty();
        assertEquals(EndState.class, card.getCurrentState().getClass());
    }
    private void chosenCrewmatePenalty() {
        card.applyCrewmatePenalty(6, 7);
        card.applyCrewmatePenalty(6, 7);
        card.applyCrewmatePenalty(7, 6);
    }

    @Test
    void flightDayPenaltyTest() {
        flightBoard.printFlightBoardState();
        int positionPreCard = flightBoard.getPlayerPosition().get(player4);

        playersDeclineAndParticipate();
        chosenCrewmatePenalty();

        flightBoard.printFlightBoardState();
        assertNotEquals(positionPreCard, flightBoard.getPlayerPosition().get(player4));
    }
}
