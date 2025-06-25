package it.polimi.it.galaxytrucker.adventurecards;

import it.polimi.it.galaxytrucker.model.adventurecards.cards.AbandonedStation;
import it.polimi.it.galaxytrucker.model.adventurecards.cardstates.EndState;
import it.polimi.it.galaxytrucker.model.adventurecards.cardstates.abandonedstation.ParticipationState;
import it.polimi.it.galaxytrucker.model.componenttiles.*;
import it.polimi.it.galaxytrucker.model.crewmates.Alien;
import it.polimi.it.galaxytrucker.model.crewmates.Human;
import it.polimi.it.galaxytrucker.exceptions.InvalidActionException;
import it.polimi.it.galaxytrucker.model.managers.FlightBoard;
import it.polimi.it.galaxytrucker.model.managers.FlightBoardFlightRules;
import it.polimi.it.galaxytrucker.model.managers.Player;
import it.polimi.it.galaxytrucker.model.managers.ShipManager;
import it.polimi.it.galaxytrucker.model.utility.AlienType;
import it.polimi.it.galaxytrucker.model.utility.Cargo;
import it.polimi.it.galaxytrucker.model.utility.Color;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class AbandonedStationTest {
    private AbandonedStation card;
    private final List<Cargo> cargoReward = List.of(new Cargo(Color.RED), new Cargo(Color.YELLOW));
    private final int crewmateRequired = 7;
    private final int flightDayPenalty = 1;

    private final int gameLevel = 2;

    private Player player1;
    private Player player2;
    private Player player3;
    private Player player4;

    private FlightBoard flightBoard;


    @BeforeEach
    void initializeParameters() {
        player1 = new Player(UUID.randomUUID(), "Margarozzo", Color.BLUE, new ShipManager(gameLevel, Color.BLUE));
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

        card = new AbandonedStation(cargoReward, crewmateRequired, flightDayPenalty, new FlightBoardFlightRules(flightBoard));

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
         * 7  [ ][l][c][x][c][ ][ ]
         * 8  [ ][ ][ ][ ][c][S][ ]
         * 9  [ ][ ][ ]   [ ][ ][ ]
         *
         * Where c stands for CabinModule
         * Where l stands for LifeSupport
         * Where x stands for CentralCabin which has all TileEdge.UNIVERSAL connectors
         */
        ship.addComponentTile(6, 7, new CabinModule(List.of(TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE), null));
        ship.addComponentTile(7, 6, new CabinModule(List.of(TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE), null));
        ship.addComponentTile(7, 8, new CabinModule(List.of(TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE), null));
        ship.addComponentTile(8, 8, new CabinModule(List.of(TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE), null));

        ship.addComponentTile(8, 9, new SpecialCargoHold(2,List.of(TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE), null));



        ship.addComponentTile(7, 5, new LifeSupport(AlienType.BROWNALIEN, List.of(TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE), null));

        ship.addCargo(8,9,new Cargo(Color.GREEN));
        ship.addCargo(8,9,new Cargo(Color.GREEN));
        ship.addCrewmate(6, 7, human);
        ship.addCrewmate(6, 7, human);
        ship.addCrewmate(7, 8, human);
        ship.addCrewmate(7, 8, human);
        ship.addCrewmate(8, 8, human);
        ship.addCrewmate(8, 8, human);

        ship.addCrewmate(7, 6, alien);
    }

    @Test
    void playerParticipateTest() {
        assertEquals(ParticipationState.class, card.getCurrentState().getClass());

        card.participate(player1, 0);

        assertEquals(card.isCardOccupied(), true);
    }

    @Test
    void playersParticipateTest() {
        assertEquals(ParticipationState.class, card.getCurrentState().getClass());

        card.participate(player1, 0);

        assertEquals(card.isCardOccupied(), true);
        assertThrows(InvalidActionException.class, () -> card.participate(player2, 0));
    }

    @Test
    void playersAllDeclineTest() {
        assertEquals(ParticipationState.class, card.getCurrentState().getClass());

        card.decline(player1);
        card.decline(player2);
        card.decline(player3);
        card.decline(player4);

        assertEquals(EndState.class, card.getCurrentState().getClass());
    }

    @Test
    void playersDeclineAndPartecipateTest() {
        assertEquals(ParticipationState.class, card.getCurrentState().getClass());

        card.decline(player1);
        card.decline(player2);
        card.participate(player3,0);


        assertEquals(card.isCardOccupied(), true);
        assertThrows(InvalidActionException.class, () -> card.participate(player4, 0));
    }

    @Test
    void LastPlayerPartecipateTest() {
        assertEquals(ParticipationState.class, card.getCurrentState().getClass());

        card.decline(player1);
        card.decline(player2);
        card.decline(player3);
        card.participate(player4, 0);

        assertEquals(card.isCardOccupied(), true);
    }

    @Test
    void playerAcceptsCargoTest() {
        playerParticipateTest();
        printCargoChoices();

        final int row = 8;
        final int column = 9;
        Player player = card.getPartecipant();

        Cargo acceptedCargo = card.getChoices().get(0).get(0);
        card.acceptCargo(0, row, column);
        card.acceptCargo(0, row, column);

        ShipManager ship = player.getShipManager();
        CargoHold cargoHold = (CargoHold) ship.getComponent(row, column).get();

        assertTrue(() -> cargoHold.getContainedCargo().contains(acceptedCargo));
        assertTrue(() -> !card.getChoices().get(0).contains(acceptedCargo));
        for(Cargo cargo: cargoHold.getContainedCargo())
            System.out.println(cargo.toString());
        printCargoChoices();
    }
    private void printCargoChoices() {
        List<Cargo> cargoReward = card.getCargoReward();
        int i = 0;

        System.out.println(card.getPartecipant().getPlayerName() + ": choose a cargo:");
        for (Cargo cargo : cargoReward) {
            System.out.println("[" + i++ + "]: " + cargo.getColor());
        }
    }

    @Test
    void playerDiscardCargoTest() {
        playerParticipateTest();
        printCargoChoices();

        Cargo discardedCargo = card.getChoices().get(0).get(0);
        card.discardCargo(0);

        assertTrue(() -> !card.getChoices().get(0).contains(discardedCargo));
        printCargoChoices();
    }

    @Test
    void playerFinishAcceptingAndDecliningCargoTest() {
        playerParticipateTest();
        printCargoChoices();

        final int row = 8;
        final int column = 9;

        card.acceptCargo(0, row, column);
        card.discardCargo(0);

        assertEquals(EndState.class, card.getCurrentState().getClass());
    }

    @Test
    void flightDayPenaltyTest() {
        flightBoard.printFlightBoardState();
        int positionPreCard= flightBoard.getPlayerPosition().get(player1);


        playerFinishAcceptingAndDecliningCargoTest();


        flightBoard.printFlightBoardState();
        assertNotEquals(positionPreCard, flightBoard.getPlayerPosition().get(player1));
    }
}
