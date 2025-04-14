package adventureCards;

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

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;

public class PlanetsTest {
    private Planets card;
    private final int numberOfPlanets = 3;
    private final List<Cargo> cargoPlanet1 = List.of(new Cargo(Color.GREEN), new Cargo(Color.GREEN));
    private final List<Cargo> cargoPlanet2 = List.of(new Cargo(Color.YELLOW));
    private final List<Cargo> cargoPlanet3 = List.of(new Cargo(Color.BLUE), new Cargo(Color.BLUE),new Cargo(Color.BLUE));
    private final int flightDayPenalty = 1;

    private final int gameLevel = 2;

    Player player1;
    Player player2;
    Player player3;
    Player player4;

    FlightBoard flightBoard;

    @BeforeEach
    void initializeParameters() {

        player1 = new Player(UUID.randomUUID(), "Margarozzo", Color.BLUE, new ShipManager(gameLevel));
        player2 = new Player(UUID.randomUUID(), "Ing. Conti", Color.BLUE, new ShipManager(gameLevel));
        player3 = new Player(UUID.randomUUID(), "D'Abate", Color.BLUE, new ShipManager(gameLevel));
        player4 = new Player(UUID.randomUUID(), "Balzarini", Color.BLUE, new ShipManager(gameLevel));

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
    void playersParticipateTest() {
        assertEquals(ParticipationState.class, card.getCurrentState().getClass());

        printChoices(card.getChoices());

        card.participate(player1, 1);
        card.participate(player2, 2);
        card.participate(player3, 0);

        printPlanetsAndPlayers(card.getTakenChoices());
        assertEquals(CargoRewardState.class, card.getCurrentState().getClass());
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
    void printPlanetsAndPlayers(HashMap<Integer, Player> planetsAnsPlayers) {
        for (Integer planet : planetsAnsPlayers.keySet()) {
            System.out.print("[" + planet +"] --> ");

            Player player = planetsAnsPlayers.get(planet);
            System.out.println(player.getPlayerName());
        }
    }

    @Test
    void playerDeclineTest() {
        assertEquals(ParticipationState.class, card.getCurrentState().getClass());

        printChoices(card.getChoices());

        card.decline(player1);
        card.decline(player2);
        card.decline(player3);
        card.decline(player4);

        printPlanetsAndPlayers(card.getTakenChoices());
        assertEquals(CargoRewardState.class, card.getCurrentState().getClass());
    }

    @Test
    void playerParticipateAndDeclineTest() {
        assertEquals(ParticipationState.class, card.getCurrentState().getClass());

        printChoices(card.getChoices());

        card.participate(player1, 1);
        card.participate(player2, 2);
        card.decline(player3);
        card.decline(player4);

        printPlanetsAndPlayers(card.getTakenChoices());
        assertEquals(CargoRewardState.class, card.getCurrentState().getClass());
    }

    @Test
    void playerPartecipateSamePlanetTest() {
        assertEquals(ParticipationState.class, card.getCurrentState().getClass());

        printChoices(card.getChoices());

        card.participate(player1, 1);
        assertThrows(InvalidActionException.class, () -> card.participate(player2, 1));
    }

    @Test
    void playerParticipateInvalidPlanetTest() {
        assertEquals(ParticipationState.class, card.getCurrentState().getClass());

        printChoices(card.getChoices());

        card.participate(player1, 1);
        assertThrows(InvalidActionException.class, () -> card.participate(player2, 3));
        assertThrows(InvalidActionException.class, () -> card.participate(player2, -1));
    }

    @Test
    void playerAcceptsSinleCargoTest() {
        playersParticipateTest();
        final int choice = 1;

        assertEquals(player3, card.getCurrentPlayer());

        printCargoOptions();
        Cargo acceptedCargo = card.getChoices().get(0).get(choice);
        card.acceptCargo(choice, 6, 7);

        ShipManager ship = player3.getShipManager();
        CargoHold cargoHold = (CargoHold) ship.getComponent(6, 7).get();

        assertTrue(() -> cargoHold.getContainedCargo().contains(acceptedCargo));

        printCargoOptions();
        card.discardCargo(0);

        assertNotEquals(player3, card.getCurrentPlayer());
    }

    @Test
    void playersAcceptsAllCargo() {
        printFlightBoard();
        playersParticipateTest();

        printCargoOptions();
        card.acceptCargo(0, 6, 7);
        printCargoOptions();
        card.acceptCargo(0, 6, 7);

        printCargoOptions();
        card.acceptCargo(0, 6, 7);

        printCargoOptions();
        card.acceptCargo(0, 6, 7);
        printCargoOptions();
        card.acceptCargo(0, 6, 7);
        printCargoOptions();
        card.acceptCargo(0, 7, 6);

        assertEquals(EndState.class, card.getCurrentState().getClass());
        printFlightBoard();
    }
    private void printCargoOptions() {
        int i = 0;
        System.out.println(card.getCurrentPlayer().getPlayerName() + ", choose a cargo to collect:");
        for (Cargo cargo : card.getCargoReward()) {
            System.out.println("[" + i++ + "] -> " + cargo.getColor());
        }
    }
    private void printFlightBoard() {
        for (Player player : flightBoard.getBoard()) {
            if (player != null) {
                System.out.print("[ " + player.getPlayerName() + "] ");
            } else {
                System.out.print("[ ] ");
            }
        }
        System.out.println();
    }

    @Test
    void playerAcceptsAndDeclinesSingleCargoTest() {
        printFlightBoard();
        playersParticipateTest();

        printCargoOptions();
        card.acceptCargo(0, 6, 7);
        printCargoOptions();
        card.discardCargo(0);

        printCargoOptions();
        card.discardCargo(0);

        printCargoOptions();
        card.acceptCargo(0, 6, 7);
        printCargoOptions();
        card.discardCargo(0);
        printCargoOptions();
        card.acceptCargo(0, 7, 6);

        assertEquals(EndState.class, card.getCurrentState().getClass());
        printFlightBoard();
    }
}
