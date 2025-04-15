package adventureCards;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;


import it.polimi.it.galaxytrucker.model.adventurecards.cardstates.planets.CargoRewardState;
import it.polimi.it.galaxytrucker.model.adventurecards.cardstates.planets.EndState;
import it.polimi.it.galaxytrucker.model.adventurecards.cardstates.planets.ParticipationState;
import it.polimi.it.galaxytrucker.model.adventurecards.refactored.Planets;
import it.polimi.it.galaxytrucker.model.componenttiles.CargoHold;
import it.polimi.it.galaxytrucker.model.componenttiles.SpecialCargoHold;
import it.polimi.it.galaxytrucker.model.componenttiles.TileEdge;
import it.polimi.it.galaxytrucker.model.exceptions.InvalidActionException;
import it.polimi.it.galaxytrucker.model.managers.FlightBoard;
import it.polimi.it.galaxytrucker.model.managers.FlightBoardFlightRules;
import it.polimi.it.galaxytrucker.model.managers.Player;
import it.polimi.it.galaxytrucker.model.managers.ShipManager;
import it.polimi.it.galaxytrucker.model.utility.Cargo;
import it.polimi.it.galaxytrucker.model.utility.Color;

public class PlanetsTest {
    private Planets card;
    private final int numberOfPlanets = 3;
    private final List<Cargo> cargoPlanet1 = List.of(new Cargo(Color.GREEN), new Cargo(Color.GREEN));
    private final List<Cargo> cargoPlanet2 = List.of(new Cargo(Color.YELLOW));
    private final List<Cargo> cargoPlanet3 = List.of(new Cargo(Color.BLUE), new Cargo(Color.BLUE),new Cargo(Color.BLUE));
    private final int flightDayPenalty = 1;

    private final int gameLevel = 2;

    private Player player1;
    private Player player2;
    private Player player3;
    private Player player4;

    private FlightBoard flightBoard;

    @BeforeEach
    void initializeParameters() {
        player1 = new Player(UUID.randomUUID(), "Margara", Color.BLUE, new ShipManager(gameLevel));
        player2 = new Player(UUID.randomUUID(), "Ing. Conti", Color.RED, new ShipManager(gameLevel));
        player3 = new Player(UUID.randomUUID(), "D'Abate", Color.YELLOW, new ShipManager(gameLevel));
        player4 = new Player(UUID.randomUUID(), "Balzarini", Color.GREEN, new ShipManager(gameLevel));

        flightBoard = new FlightBoard(gameLevel);

        flightBoard.addPlayerMarker(player1);
        flightBoard.addPlayerMarker(player2);
        flightBoard.addPlayerMarker(player3);
        flightBoard.addPlayerMarker(player4);

        createShip(player1);
        createShip(player2);
        createShip(player3);
        createShip(player4);

        List<List<Cargo>> cargoRewardsByPlanet = List.of(cargoPlanet1, cargoPlanet2, cargoPlanet3);
        card = new Planets(numberOfPlanets, cargoRewardsByPlanet, flightDayPenalty, new FlightBoardFlightRules(flightBoard));

        card.play();
    }
    private void createShip(Player player) {
        ShipManager ship = player.getShipManager();
        /*
         *     4  5  6  7  8  9  10 
         * 5        [ ]   [ ]      
         * 6     [ ][ ][c][ ][ ]   
         * 7  [ ][ ][c][x][s][ ][ ]
         * 8  [ ][ ][c][ ][ ][ ][ ]
         * 9  [ ][ ][ ]   [ ][ ][ ]
         * 
         * Where c stands for cargoHold
         * Where s stands for SpecialCargoHold
         * Where x stands for CentralCabin which has all TileEdge.UNIVERSAL connectors
         */

        ship.addComponentTile(6,7, new CargoHold(2,List.of(TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE)));
        ship.addComponentTile(7,6, new CargoHold(2,List.of(TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE)));
        ship.addComponentTile(8,6, new CargoHold(2,List.of(TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE)));
    
        ship.addComponentTile(7,8, new SpecialCargoHold(2,List.of(TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE)));
    }

    @Test
    void playerParticipateTest() {
        assertEquals(ParticipationState.class, card.getCurrentState().getClass());

        printChoices(card.getChoices());

        card.participate(player1, 0);

        Player playerOccupyingPlanet = card.getTakenChoices().get(0);
        assertEquals(player1, playerOccupyingPlanet);
    }
    private void printChoices(List<List<Cargo>> choices) {
        int i = 0;

        for (List<Cargo> cargoSet : choices) {
            System.out.print("[" + i++ +"] ");
            for (Cargo cargo : cargoSet) {
                System.out.print(cargo.getColor() + " ");
            }
            System.out.println();
        }
    }

    @Test
    void playerDeclineTest() {
        assertEquals(ParticipationState.class, card.getCurrentState().getClass());

        printChoices(card.getChoices());

        card.decline(player1);

        for (Integer planet : card.getTakenChoices().keySet()) {
            assertTrue(() -> !card.getTakenChoices().get(planet).equals(player1));
        }
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
    void playerPartecipateToAlreadyTakenPlanetTest() {
        assertEquals(ParticipationState.class, card.getCurrentState().getClass());

        printChoices(card.getChoices());

        card.participate(player1, 0);

        assertThrows(InvalidActionException.class, () -> card.participate(player2, 0));
    }

    @Test
    void playersPartecipateUntilFullPlanetsTest() {
        assertEquals(ParticipationState.class, card.getCurrentState().getClass());

        printChoices(card.getChoices());

        playersPartecipateUntilFullPlanets();

        assertEquals(CargoRewardState.class, card.getCurrentState().getClass());
    }
    private void playersPartecipateUntilFullPlanets() {
        card.participate(player1, 0);
        card.participate(player2, 1);
        card.participate(player3, 2);

        printPartecipants();
    }
    private void printPartecipants() {
        HashMap<Integer, Player> occupiedPlanets = card.getTakenChoices();

        System.out.println("Occupied planets:");
        for (Integer planet : occupiedPlanets.keySet()) {
            Player player = occupiedPlanets.get(planet);
            System.out.println("[" + planet + "] -> " + player.getPlayerName() + ": " + player.getColor());
        }
    }

    @Test
    void playerAcceptsCargoTest() {
        playersPartecipateUntilFullPlanets();
        printCargoChoices();

        final int row = 6;
        final int column = 7;
        Player player = card.getCurrentPlayer();

        Cargo acceptedCargo = card.getChoices().get(0).get(0);
        card.acceptCargo(0, row, column);

        ShipManager ship = player.getShipManager();
        CargoHold cargoHold = (CargoHold) ship.getComponent(row, column).get();

        assertTrue(() -> cargoHold.getContainedCargo().contains(acceptedCargo));
        assertTrue(() -> !card.getChoices().get(0).contains(acceptedCargo));
        printCargoChoices();
    }
    private void printCargoChoices() {
        List<Cargo> cargoReward = card.getCargoReward();
        int i = 0;

        System.out.println(card.getCurrentPlayer().getPlayerName() + ": choose a cargo:");
        for (Cargo cargo : cargoReward) {
            System.out.println("[" + i++ + "]: " + cargo.getColor());
        }
    }

    @Test
    void playerDiscardCargoTest() {
        playersPartecipateUntilFullPlanets();
        printCargoChoices();

        Cargo discardedCargo = card.getChoices().get(0).get(0);
        card.discardCargo(0);

        assertTrue(() -> !card.getChoices().get(0).contains(discardedCargo));
        printCargoChoices();
    }

    @Test
    void playerFinishAcceptingAndDecliningCargoTest() {
        playersPartecipateUntilFullPlanets();
        printCargoChoices();

        final int row = 6;
        final int column = 7;
        Player player = card.getCurrentPlayer();

        card.acceptCargo(0, row, column);
        card.discardCargo(0);

        assertNotEquals(player, card.getCurrentPlayer());
    }

    @Test
    void playersFinishAcceptingAndDecliningCargoTest() {
        printChoices(card.getChoices());
        playersPartecipateUntilFullPlanets();
        flightBoard.printFlightBoardState();

        printCargoChoices();
        card.acceptCargo(0, 6, 7);
        printCargoChoices();
        card.discardCargo(0);

        printCargoChoices();
        card.discardCargo(0);

        printCargoChoices();
        card.acceptCargo(0, 6, 7);
        printCargoChoices();
        card.discardCargo(0);
        printCargoChoices();
        card.acceptCargo(0, 6, 7);
        
        flightBoard.printFlightBoardState();
        assertEquals(EndState.class, card.getCurrentState().getClass());
    }
}
