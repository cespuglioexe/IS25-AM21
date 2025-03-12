package managers;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;
import java.util.List;

import it.polimi.it.galaxytrucker.componenttiles.BatteryComponent;
import it.polimi.it.galaxytrucker.componenttiles.CabinModule;
import it.polimi.it.galaxytrucker.componenttiles.CargoHold;
import it.polimi.it.galaxytrucker.componenttiles.CentralCabin;
import it.polimi.it.galaxytrucker.componenttiles.ComponentTile;
import it.polimi.it.galaxytrucker.componenttiles.LifeSupport;
import it.polimi.it.galaxytrucker.componenttiles.OutOfBoundsTile;
import it.polimi.it.galaxytrucker.componenttiles.SingleCannon;
import it.polimi.it.galaxytrucker.componenttiles.SingleEngine;
import it.polimi.it.galaxytrucker.componenttiles.SpecialCargoHold;
import it.polimi.it.galaxytrucker.componenttiles.StructuralModule;
import it.polimi.it.galaxytrucker.componenttiles.TileEdge;
import it.polimi.it.galaxytrucker.crewmates.Human;
import it.polimi.it.galaxytrucker.crewmates.Alien;
import it.polimi.it.galaxytrucker.exceptions.IllegalComponentPositionException;
import it.polimi.it.galaxytrucker.exceptions.InvalidActionException;
import it.polimi.it.galaxytrucker.managers.ShipManager;
import it.polimi.it.galaxytrucker.utility.AlienType;
import it.polimi.it.galaxytrucker.utility.Color;
import it.polimi.it.galaxytrucker.utility.Cargo;

public class ShipManagerTest {

    @Test
    void getComponentsAtRowOutOfBounds() {
        ShipManager ship = new ShipManager(1);

        assertThrows(IndexOutOfBoundsException.class, () -> ship.getComponentsAtRow(0));
        assertThrows(IndexOutOfBoundsException.class, () -> ship.getComponentsAtRow(10));
    }

    @Test
    void getComponentsAtRowTest() {
        ShipManager ship = new ShipManager(1);

        ComponentTile cannon = new SingleCannon(TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE);

        /*
         *  4  5  6  7  8  9 10
         * 5        [c]
         * 6     [c][c][ ]
         * 7  [c][ ][x][c][ ]
         * 8  [ ][c][c][c][c]
         * 9  [ ][ ]   [c][ ]
        */

        for (int i = 5; i < 10; i++) {
            for (int j = 5; j < 10; j++) {
                switch (i) {
                    case 5:
                        if (j == 7) {
                            ship.addComponentTile(i, j, cannon);
                        }
                        break;
                    case 6:
                        if (j > 5 && j < 8) {
                            ship.addComponentTile(i, j, cannon);
                        }
                        break;
                    case 7:
                        if ((j == 5 || (j > 7 && j < 9)) && j != 7) {
                            ship.addComponentTile(i, j, cannon);
                        }
                        break;
                    case 8:
                        if (j > 5 && j < 10) {
                            ship.addComponentTile(i, j, cannon);
                        }
                        break;
                    case 9:
                        if (j == 8) {
                            ship.addComponentTile(i, j, cannon);
                        }
                        break;
                }
            }
        }

        assertIterableEquals(
            List.of(
                Optional.of(OutOfBoundsTile.class),
                Optional.of(OutOfBoundsTile.class),
                Optional.of(SingleCannon.class),
                Optional.of(SingleCannon.class),
                Optional.empty(),
                Optional.of(OutOfBoundsTile.class),
                Optional.of(OutOfBoundsTile.class)
            ),
            ship.getComponentsAtRow(6).stream()
                .map(opt -> opt.map(Object::getClass))
                .toList()
        );
    }

    @Test
    void getComponentsAtColumnOutOfBounds() {
        ShipManager ship = new ShipManager(1);

        assertThrows(IndexOutOfBoundsException.class, () -> ship.getComponentsAtColumn(0));
        assertThrows(IndexOutOfBoundsException.class, () -> ship.getComponentsAtColumn(11));
    }

    @Test
    void getComponentsAtColumnTest() {
        ShipManager ship = new ShipManager(1);

        ComponentTile cannon = new SingleCannon(TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE);

        /*
         *  4  5  6  7  8  9 10
         * 5        [c]
         * 6     [c][c][ ]
         * 7  [c][ ][x][c][ ]
         * 8  [ ][c][c][c][c]
         * 9  [ ][ ]   [c][ ]
        */

        for (int i = 5; i < 10; i++) {
            for (int j = 5; j < 10; j++) {
                switch (i) {
                    case 5:
                        if (j == 7) {
                            ship.addComponentTile(i, j, cannon);
                        }
                        break;
                    case 6:
                        if (j > 5 && j < 8) {
                            ship.addComponentTile(i, j, cannon);
                        }
                        break;
                    case 7:
                        if ((j == 5 || (j > 7 && j < 9)) && j != 7) {
                            ship.addComponentTile(i, j, cannon);
                        }
                        break;
                    case 8:
                        if (j > 5 && j < 10) {
                            ship.addComponentTile(i, j, cannon);
                        }
                        break;
                    case 9:
                        if (j == 8) {
                            ship.addComponentTile(i, j, cannon);
                        }
                        break;
                }
            }
        }

        assertIterableEquals(
            List.of(
                Optional.of(SingleCannon.class),
                Optional.of(SingleCannon.class),
                Optional.of(CentralCabin.class),
                Optional.of(SingleCannon.class),
                Optional.of(OutOfBoundsTile.class)
            ),
            ship.getComponentsAtColumn(7).stream()
                .map(opt -> opt.map(Object::getClass))
                .toList()
        );
    }



    @Test
    void isShipLegalWithDisconnectedBranchesTest() {
        ShipManager ship = new ShipManager(1);

        ComponentTile cannon = new SingleCannon(TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE);

        /*
         *  4  5  6  7  8  9 10
         * 5        [c]
         * 6     [c][c][ ]
         * 7  [c][ ][x][c][ ]
         * 8  [ ][c][c][c][c]
         * 9  [ ][ ]   [c][ ]
        */

        for (int i = 5; i < 10; i++) {
            for (int j = 5; j < 10; j++) {
                switch (i) {
                    case 5:
                        if (j == 7) {
                            ship.addComponentTile(i, j, cannon);
                        }
                        break;
                    case 6:
                        if (j > 5 && j < 8) {
                            ship.addComponentTile(i, j, cannon);
                        }
                        break;
                    case 7:
                        if ((j == 5 || (j > 7 && j < 9)) && j != 7) {
                            ship.addComponentTile(i, j, cannon);
                        }
                        break;
                    case 8:
                        if (j > 5 && j < 10) {
                            ship.addComponentTile(i, j, cannon);
                        }
                        break;
                    case 9:
                        if (j == 8) {
                            ship.addComponentTile(i, j, cannon);
                        }
                        break;
                }
            }
        }

        assertFalse(() -> ship.isShipLegal());
    }

    @Test
    void isShipLegalWithEnginesPointingBackwardsTest() {
        ShipManager ship = new ShipManager(1);

        ComponentTile singleConnector = new StructuralModule(TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE);
        ComponentTile engine = new SingleEngine(TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.INCOMPATIBLE);

        //rotate the engine pointing up
        engine.rotate();
        engine.rotate();

        /*
         *  4  5  6  7  8  9 10
         * 5        [s]
         * 6     [s][s][ ]
         * 7  [ ][ ][x][s][ ]
         * 8  [ ][s][s][s][s]
         * 9  [ ][ ]   [e][ ]
         * 
         * Where s stands for singleConnector
         * Where e stands for engine
         * Where x stands for CentralCabin which has all TileEdge.UNIVERSAL connectors
         * 
         * Illegal engine is at [4][4]
        */

        for (int i = 5; i < 10; i++) {
            for (int j = 5; j < 10; j++) {
                switch (i) {
                    case 5:
                        if (j == 7) {
                            ship.addComponentTile(i, j, singleConnector);
                        }
                        break;
                    case 6:
                        if (j > 5 && j < 8) {
                            ship.addComponentTile(i, j, singleConnector);
                        }
                        break;
                    case 7:
                        if ((j > 6 && j < 9) && j != 7) {
                            ship.addComponentTile(i, j, singleConnector);
                        }
                        break;
                    case 8:
                        if (j > 5 && j < 10) {
                            ship.addComponentTile(i, j, singleConnector);
                        }
                        break;
                    case 9:
                        if (j == 8) {
                            ship.addComponentTile(i, j, engine);
                        }
                        break;
                }
            }
        }

        assertFalse(() -> ship.isShipLegal());
    }

    @Test
    void isShipLegalWithDifferentConnectorsUpTest() {
        ShipManager ship = new ShipManager(1);

        ComponentTile singleConnector = new StructuralModule(TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE);
        ComponentTile doubleConnector = new StructuralModule(TileEdge.DOUBLE, TileEdge.DOUBLE, TileEdge.DOUBLE, TileEdge.DOUBLE);

        /*
         *  4  5  6  7  8  9 10
         * 5        [s]
         * 6     [s][s][ ]
         * 7  [ ][ ][x][s][ ]
         * 8  [ ][d][s][d][s]
         * 9  [ ][ ]   [s][ ]
         * 
         * Where s stands for singleConnector
         * Where d stands for doubleConnector
         * Where x stands for CentralCabin which has all TileEdge.UNIVERSAL connectors
         * 
         * Illegal connection is at [2][4] and [3][4]
        */

        for (int i = 5; i < 10; i++) {
            for (int j = 5; j < 10; j++) {
                switch (i) {
                    case 5:
                        if (j == 7) {
                            ship.addComponentTile(i, j, singleConnector);
                        }
                        break;
                    case 6:
                        if (j > 5 && j < 8) {
                            ship.addComponentTile(i, j, singleConnector);
                        }
                        break;
                    case 7:
                        if ((j > 6 && j < 9) && j != 7) {
                            ship.addComponentTile(i, j, singleConnector);
                        }
                        break;
                    case 8:
                        if (j > 5 && j < 10) {
                            if (j % 2 == 0) {
                                ship.addComponentTile(i, j, doubleConnector);
                            } else {
                                ship.addComponentTile(i, j, singleConnector);
                            }
                        }
                        break;
                    case 9:
                        if (j == 8) {
                            ship.addComponentTile(i, j, singleConnector);
                        }
                        break;
                }
            }
        }

        assertFalse(() -> ship.isShipLegal());
    }

    @Test
    void isShipLegalLegalTest() {
        ShipManager ship = new ShipManager(1);

        ComponentTile singleConnector = new StructuralModule(TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE);

        /*
         *  4  5  6  7  8  9 10
         * 5        [s]
         * 6     [s][s][ ]
         * 7  [ ][ ][x][s][ ]
         * 8  [ ][s][s][s][s]
         * 9  [ ][ ]   [s][ ]
         * 
         * Where s stands for singleConnector
         * Where x stands for CentralCabin which has all TileEdge.UNIVERSAL connectors
         * 
        */

        for (int i = 5; i < 10; i++) {
            for (int j = 5; j < 10; j++) {
                switch (i) {
                    case 5:
                        if (j == 7) {
                            ship.addComponentTile(i, j, singleConnector);
                        }
                        break;
                    case 6:
                        if (j > 5 && j < 8) {
                            ship.addComponentTile(i, j, singleConnector);
                        }
                        break;
                    case 7:
                        if ((j > 6 && j < 9) && j != 7) {
                            ship.addComponentTile(i, j, singleConnector);
                        }
                        break;
                    case 8:
                        if (j > 5 && j < 10) {
                            ship.addComponentTile(i, j, singleConnector);
                        }
                        break;
                    case 9:
                        if (j == 8) {
                            ship.addComponentTile(i, j, singleConnector);
                        }
                        break;
                }
            }
        }

        assertTrue(() -> ship.isShipLegal());
    }

    @Test
    void addCrewmateIllegalComponentPositionTest() {
        ShipManager ship = new ShipManager(1);

        ComponentTile singleConnector = new StructuralModule(TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE);

        Human crewmate = new Human();

        /*
         *  4  5  6  7  8  9 10
         * 5        [ ]
         * 6     [ ][s][ ]
         * 7  [ ][ ][x][ ][ ]
         * 8  [ ][ ][ ][ ][ ]
         * 9  [ ][ ]   [ ][ ]
         * 
         * Where s stands for singleConnector
         * Where x stands for CentralCabin which has all TileEdge.UNIVERSAL connectors
         * 
        */

        ship.addComponentTile(6, 7, singleConnector);

        assertThrows(IllegalComponentPositionException.class, () -> ship.addCrewmate(6, 7, crewmate));
    }

    @Test
    void addCrewmateHumanTest() {
        ShipManager ship = new ShipManager(1);
        CabinModule cabin = new CabinModule(TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE);

        Human crewmate = new Human();

        /*
         *  4  5  6  7  8  9 10
         * 5        [ ]
         * 6     [ ][c][ ]
         * 7  [ ][ ][x][ ][ ]
         * 8  [ ][ ][ ][ ][ ]
         * 9  [ ][ ]   [ ][ ]
         * 
         * Where c stands for cabinModule
         * Where x stands for CentralCabin which has all TileEdge.UNIVERSAL connectors
         * 
        */

        CentralCabin centralCabin = (CentralCabin) ship.getComponent(7, 7).get();

        ship.addComponentTile(6, 7, cabin);
        
        ship.addCrewmate(7, 7, crewmate);
        ship.addCrewmate(6, 7, crewmate);

        assertEquals(centralCabin.getCrewmates().size(), 1);
        assertEquals(cabin.getCrewmates().size(), 1);
    }

    @Test
    void addCrewmateFullCabinTest() {
        ShipManager ship = new ShipManager(1);

        CabinModule cabin = new CabinModule(TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE);

        Human crewmate = new Human();

        /*
         *  4  5  6  7  8  9 10
         * 5        [ ]
         * 6     [ ][ ][ ]
         * 7  [ ][ ][x][ ][ ]
         * 8  [ ][ ][ ][ ][ ]
         * 9  [ ][ ]   [ ][ ]
         * 
         * Where c stands for cabinModule
         * Where x stands for CentralCabin which has all TileEdge.UNIVERSAL connectors
         * 
        */

        ship.addComponentTile(6, 7, cabin);

        ship.addCrewmate(6, 7, crewmate);
        ship.addCrewmate(6, 7, crewmate);

        assertThrows(InvalidActionException.class, () -> ship.addCrewmate(6, 7, crewmate));
    }

    @Test
    void addCrewmateAlienWithoutLifeSupportTest() {
        ShipManager ship = new ShipManager(1);

        CabinModule cabin = new CabinModule(TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE);

        Alien alien = new Alien(AlienType.PURPLEALIEN);

        /*
         *  4  5  6  7  8  9 10
         * 5        [ ]
         * 6     [ ][c][ ]
         * 7  [ ][ ][x][ ][ ]
         * 8  [ ][ ][ ][ ][ ]
         * 9  [ ][ ]   [ ][ ]
         * 
         * Where c stands for cabinModule
         * Where x stands for CentralCabin which has all TileEdge.UNIVERSAL connectors
         * 
        */

        ship.addComponentTile(6, 7, cabin);

        assertThrows(InvalidActionException.class, () -> ship.addCrewmate(6, 7, alien));
    }

    @Test
    void addCrewmateAlienTest() {
        ShipManager ship = new ShipManager(1);

        CabinModule cabin = new CabinModule(TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE);
        LifeSupport lifeSupport = new LifeSupport(AlienType.PURPLEALIEN, TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE);

        Alien alien = new Alien(AlienType.PURPLEALIEN);

        /*
         *  4  5  6  7  8  9 10
         * 5        [ ]
         * 6     [ ][c][l]
         * 7  [ ][ ][x][ ][ ]
         * 8  [ ][ ][ ][ ][ ]
         * 9  [ ][ ]   [ ][ ]
         * 
         * Where c stands for cabinModule
         * Where l stands for LifeSupport
         * Where x stands for CentralCabin which has all TileEdge.UNIVERSAL connectors
         * 
        */

        ship.addComponentTile(6, 7, cabin);
        ship.addComponentTile(6, 8, lifeSupport);

        ship.addCrewmate(6, 7, alien);

        assertEquals(cabin.getCrewmates().size(), 1);
    }

    @Test
    void removeCrewmateIllegalComponentPositionTest() {
        ShipManager ship = new ShipManager(1);

        ComponentTile singleConnector = new StructuralModule(TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE);

        /*
         *  4  5  6  7  8  9 10
         * 5        [ ]
         * 6     [ ][s][ ]
         * 7  [ ][ ][x][ ][ ]
         * 8  [ ][ ][ ][ ][ ]
         * 9  [ ][ ]   [ ][ ]
         * 
         * Where s stands for singleConnector
         * Where x stands for CentralCabin which has all TileEdge.UNIVERSAL connectors
         * 
        */

        ship.addComponentTile(6, 7, singleConnector);

        assertThrows(IllegalComponentPositionException.class, () -> ship.removeCrewmate(6, 7));
    }

    @Test
    void removeCrewmateEmptyCabinTest() {
        ShipManager ship = new ShipManager(1);
        CabinModule cabin = new CabinModule(TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE);

        /*
         *  4  5  6  7  8  9 10
         * 5        [ ]
         * 6     [ ][c][ ]
         * 7  [ ][ ][x][ ][ ]
         * 8  [ ][ ][ ][ ][ ]
         * 9  [ ][ ]   [ ][ ]
         * 
         * Where c stands for cabinModule
         * Where x stands for CentralCabin which has all TileEdge.UNIVERSAL connectors
         * 
        */

        ship.addComponentTile(6, 7, cabin);

        assertThrows(InvalidActionException.class, () -> ship.removeCrewmate(6, 7));
    }

    @Test
    void removeCrewmateHumanTest() {
        ShipManager ship = new ShipManager(1);
        CabinModule cabin = new CabinModule(TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE);

        Human crewmate = new Human();

        /*
         *  4  5  6  7  8  9 10
         * 5        [ ]
         * 6     [ ][c][ ]
         * 7  [ ][ ][x][ ][ ]
         * 8  [ ][ ][ ][ ][ ]
         * 9  [ ][ ]   [ ][ ]
         * 
         * Where c stands for cabinModule
         * Where x stands for CentralCabin which has all TileEdge.UNIVERSAL connectors
         * 
        */

        CentralCabin centralCabin = (CentralCabin) ship.getComponent(7, 7).get();

        ship.addComponentTile(6, 7, cabin);

        ship.addCrewmate(6, 7, crewmate);
        ship.addCrewmate(6, 7, crewmate);
        ship.addCrewmate(7, 7, crewmate);

        ship.removeCrewmate(6, 7);
        ship.removeCrewmate(7, 7);

        assertEquals(cabin.getCrewmates().size(), 1);
        assertEquals(centralCabin.getCrewmates().size(), 0);
    }

    @Test
    void removeCrewmateAlienTest() {
        ShipManager ship = new ShipManager(1);

        CabinModule cabin = new CabinModule(TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE);
        LifeSupport lifeSupport = new LifeSupport(AlienType.PURPLEALIEN, TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE);

        Alien alien = new Alien(AlienType.PURPLEALIEN);

        /*
         *  4  5  6  7  8  9 10
         * 5        [ ]
         * 6     [ ][c][l]
         * 7  [ ][ ][x][ ][ ]
         * 8  [ ][ ][ ][ ][ ]
         * 9  [ ][ ]   [ ][ ]
         * 
         * Where c stands for cabinModule
         * Where l stands for LifeSupport
         * Where x stands for CentralCabin which has all TileEdge.UNIVERSAL connectors
         * 
        */

        ship.addComponentTile(6, 7, cabin);
        ship.addComponentTile(6, 8, lifeSupport);

        ship.addCrewmate(6, 7, alien);
        
        ship.removeCrewmate(6, 7);

        assertEquals(cabin.getCrewmates().size(), 0);
    }

    @Test
    void addCargoNormalIllegalComponentPositionTest() {
        ShipManager ship = new ShipManager(1);

        CabinModule cabin = new CabinModule(TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE);

        Cargo cargo = new Cargo(Color.BLUE);

        /*
         *  4  5  6  7  8  9 10
         * 5        [ ]
         * 6     [ ][c][ ]
         * 7  [ ][ ][x][ ][ ]
         * 8  [ ][ ][ ][ ][ ]
         * 9  [ ][ ]   [ ][ ]
         * 
         * Where c stands for cabinModule
         * Where x stands for CentralCabin which has all TileEdge.UNIVERSAL connectors
         * 
        */

        ship.addComponentTile(6, 7, cabin);

        assertThrows(IllegalComponentPositionException.class, () -> ship.addCargo(6, 7, cargo));
    }
 
    @Test
    void addCargoNormalTest() {
        ShipManager ship = new ShipManager(1);

        CargoHold cargoHold = new CargoHold(2, TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE);

        Cargo cargo = new Cargo(Color.BLUE);

        /*
         *  4  5  6  7  8  9 10
         * 5        [ ]
         * 6     [ ][c][ ]
         * 7  [ ][ ][x][ ][ ]
         * 8  [ ][ ][ ][ ][ ]
         * 9  [ ][ ]   [ ][ ]
         * 
         * Where c stands for cabinModule
         * Where x stands for CentralCabin which has all TileEdge.UNIVERSAL connectors
         * 
        */

        ship.addComponentTile(6, 7, cargoHold);

        ship.addCargo(6, 7, cargo);

        assertEquals(cargoHold.getContainedCargo().size(), 1);
    }

    @Test
    void addCargoNormalFullCargoHoldTest() {
        ShipManager ship = new ShipManager(1);

        CargoHold cargoHold = new CargoHold(2, TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE);

        Cargo cargo = new Cargo(Color.BLUE);

        /*
         *  4  5  6  7  8  9 10
         * 5        [ ]
         * 6     [ ][c][ ]
         * 7  [ ][ ][x][ ][ ]
         * 8  [ ][ ][ ][ ][ ]
         * 9  [ ][ ]   [ ][ ]
         * 
         * Where c stands for cabinModule
         * Where x stands for CentralCabin which has all TileEdge.UNIVERSAL connectors
         * 
        */

        ship.addComponentTile(6, 7, cargoHold);

        ship.addCargo(6, 7, cargo);
        ship.addCargo(6, 7, cargo);

        assertThrows(InvalidActionException.class, () -> ship.addCargo(6, 7, cargo));
    }

    @Test
    void addCargoNormalToSpecialCargoHold() {
        ShipManager ship = new ShipManager(1);

        SpecialCargoHold specialCargoHold = new SpecialCargoHold(2, TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE);

        Cargo cargo = new Cargo(Color.BLUE);

        /*
         *  4  5  6  7  8  9 10
         * 5        [ ]
         * 6     [ ][s][ ]
         * 7  [ ][ ][x][ ][ ]
         * 8  [ ][ ][ ][ ][ ]
         * 9  [ ][ ]   [ ][ ]
         * 
         * Where s stands for specialCargoHold
         * Where x stands for CentralCabin which has all TileEdge.UNIVERSAL connectors
         * 
        */

        ship.addComponentTile(6, 7, specialCargoHold);

        ship.addCargo(6, 7, cargo);

        assertEquals(specialCargoHold.getContainedCargo().size(), 1);
    }

    @Test
    void addCargoSpecialNoSpecialCargoHoldTest() {
        ShipManager ship = new ShipManager(1);

        CargoHold cargoHold = new CargoHold(2, TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE);

        Cargo cargo = new Cargo(Color.RED);

        /*
         *  4  5  6  7  8  9 10
         * 5        [ ]
         * 6     [ ][c][ ]
         * 7  [ ][ ][x][ ][ ]
         * 8  [ ][ ][ ][ ][ ]
         * 9  [ ][ ]   [ ][ ]
         * 
         * Where c stands for cargoHold
         * Where x stands for CentralCabin which has all TileEdge.UNIVERSAL connectors
         * 
        */

        ship.addComponentTile(6, 7, cargoHold);

        assertThrows(IllegalComponentPositionException.class, () -> ship.addCargo(6, 7, cargo));
    }

    @Test
    void addCargoSpecialTest() {
        ShipManager ship = new ShipManager(1);

        SpecialCargoHold specialCargoHold = new SpecialCargoHold(2, TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE);

        Cargo cargo1 = new Cargo(Color.RED);
        Cargo cargo2 = new Cargo(Color.BLUE);

        /*
         *  4  5  6  7  8  9 10
         * 5        [ ]
         * 6     [ ][s][ ]
         * 7  [ ][ ][x][ ][ ]
         * 8  [ ][ ][ ][ ][ ]
         * 9  [ ][ ]   [ ][ ]
         * 
         * Where c stands for specialCargoHold
         * Where x stands for CentralCabin which has all TileEdge.UNIVERSAL connectors
         * 
        */

        ship.addComponentTile(6, 7, specialCargoHold);

        ship.addCargo(6, 7, cargo1);
        assertEquals(specialCargoHold.getContainedCargo().size(), 1);

        ship.addCargo(6, 7, cargo2);
        assertEquals(specialCargoHold.getContainedCargo().size(),2);
    }

    @Test
    void addCargoSpecialFullSpecialCargoHoldTest() {
        ShipManager ship = new ShipManager(1);

        SpecialCargoHold specialCargoHold = new SpecialCargoHold(2, TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE);

        Cargo cargo = new Cargo(Color.RED);

        /*
         *  4  5  6  7  8  9 10
         * 5        [ ]
         * 6     [ ][s][ ]
         * 7  [ ][ ][x][ ][ ]
         * 8  [ ][ ][ ][ ][ ]
         * 9  [ ][ ]   [ ][ ]
         * 
         * Where c stands for specialCargoHold
         * Where x stands for CentralCabin which has all TileEdge.UNIVERSAL connectors
         * 
        */

        ship.addComponentTile(6, 7, specialCargoHold);

        ship.addCargo(6, 7, cargo);
        ship.addCargo(6, 7, cargo);

        assertThrows(InvalidActionException.class, () -> ship.addCargo(6, 7, cargo));
    }

    @Test
    void removeCargoIllegalComponentPositionTest() {
        ShipManager ship = new ShipManager(1);

        CabinModule cabin = new CabinModule(TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE);

        /*
         *  4  5  6  7  8  9 10
         * 5        [ ]
         * 6     [ ][c][ ]
         * 7  [ ][ ][x][ ][ ]
         * 8  [ ][ ][ ][ ][ ]
         * 9  [ ][ ]   [ ][ ]
         * 
         * Where c stands for cabinModule
         * Where x stands for CentralCabin which has all TileEdge.UNIVERSAL connectors
         * 
        */

        ship.addComponentTile(6, 7, cabin);

        assertThrows(IllegalComponentPositionException.class, () -> ship.removeCargo(6, 7, Color.BLUE));
    }

    @Test
    void removeCargoNormalEmptyCargoHoldTest() {
        ShipManager ship = new ShipManager(1);

        CargoHold cargoHold = new CargoHold(2, TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE);

        /*
         *  4  5  6  7  8  9 10
         * 5        [ ]
         * 6     [ ][c][ ]
         * 7  [ ][ ][x][ ][ ]
         * 8  [ ][ ][ ][ ][ ]
         * 9  [ ][ ]   [ ][ ]
         * 
         * Where c stands for cargoHold
         * Where x stands for CentralCabin which has all TileEdge.UNIVERSAL connectors
         * 
        */

        ship.addComponentTile(6, 7, cargoHold);

        assertThrows(InvalidActionException.class, () -> ship.removeCargo(6, 7, Color.BLUE));
    }

    @Test
    void removeCargoNormalNoCargoOfThatColorTest() {
        ShipManager ship = new ShipManager(1);

        CargoHold cargoHold = new CargoHold(2, TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE);

        Cargo cargo = new Cargo(Color.BLUE);

        /*
         *  4  5  6  7  8  9 10
         * 5        [ ]
         * 6     [ ][c][ ]
         * 7  [ ][ ][x][ ][ ]
         * 8  [ ][ ][ ][ ][ ]
         * 9  [ ][ ]   [ ][ ]
         * 
         * Where c stands for cargoHold
         * Where x stands for CentralCabin which has all TileEdge.UNIVERSAL connectors
         * 
        */

        ship.addComponentTile(6, 7, cargoHold);

        ship.addCargo(6, 7, cargo);

        assertThrows(InvalidActionException.class, () -> ship.removeCargo(6, 7, Color.YELLOW));
    }

    @Test
    void removeCargoNormalTest() {
        ShipManager ship = new ShipManager(1);

        CargoHold cargoHold = new CargoHold(2, TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE);

        Cargo cargo1 = new Cargo(Color.BLUE);
        Cargo cargo2 = new Cargo(Color.YELLOW);

        /*
         *  4  5  6  7  8  9 10
         * 5        [ ]
         * 6     [ ][c][ ]
         * 7  [ ][ ][x][ ][ ]
         * 8  [ ][ ][ ][ ][ ]
         * 9  [ ][ ]   [ ][ ]
         * 
         * Where c stands for cargoHold
         * Where x stands for CentralCabin which has all TileEdge.UNIVERSAL connectors
         * 
        */

        ship.addComponentTile(6, 7, cargoHold);

        ship.addCargo(6, 7, cargo1);
        ship.addCargo(6, 7, cargo2);

        ship.removeCargo(6, 7, Color.BLUE);

        assertEquals(cargo2, cargoHold.getContainedCargo().get(0));

        ship.removeCargo(6, 7, Color.YELLOW);

        assertEquals(0, cargoHold.getContainedCargo().size());
    }

    @Test
    void removeCargoSpecial() {
        ShipManager ship = new ShipManager(1);

        SpecialCargoHold specialCargoHold = new SpecialCargoHold(2, TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE);

        Cargo cargo1 = new Cargo(Color.BLUE);
        Cargo cargo2 = new Cargo(Color.RED);

        /*
         *  4  5  6  7  8  9 10
         * 5        [ ]
         * 6     [ ][s][ ]
         * 7  [ ][ ][x][ ][ ]
         * 8  [ ][ ][ ][ ][ ]
         * 9  [ ][ ]   [ ][ ]
         * 
         * Where s stands for specialCargoHold
         * Where x stands for CentralCabin which has all TileEdge.UNIVERSAL connectors
         * 
        */

        ship.addComponentTile(6, 7, specialCargoHold);

        ship.addCargo(6, 7, cargo1);
        ship.addCargo(6, 7, cargo2);

        ship.removeCargo(6, 7, Color.BLUE);
        assertEquals(cargo2, specialCargoHold.getContainedCargo().get(0));

        ship.removeCargo(6, 7, Color.RED);
        assertEquals(0, specialCargoHold.getContainedCargo().size());
    }

    @Test
    void removeBatteryIllegalComponentPositionTest() {
        ShipManager ship = new ShipManager(1);

        CargoHold cargoHold = new CargoHold(2, TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE);

        /*
         *  4  5  6  7  8  9 10
         * 5        [ ]
         * 6     [ ][c][ ]
         * 7  [ ][ ][x][ ][ ]
         * 8  [ ][ ][ ][ ][ ]
         * 9  [ ][ ]   [ ][ ]
         * 
         * Where c stands for cargoHold
         * Where x stands for CentralCabin which has all TileEdge.UNIVERSAL connectors
         * 
        */

        ship.addComponentTile(6, 7, cargoHold);

        assertThrows(IllegalComponentPositionException.class, () -> ship.removeBattery(6, 7));
    }

    @Test
    void removeBatteryTest() {
        ShipManager ship = new ShipManager(1);

        BatteryComponent batteryComponent = new BatteryComponent(2, TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE);

        /*
         *  4  5  6  7  8  9 10
         * 5        [ ]
         * 6     [ ][b][ ]
         * 7  [ ][ ][x][ ][ ]
         * 8  [ ][ ][ ][ ][ ]
         * 9  [ ][ ]   [ ][ ]
         * 
         * Where c stands for batteryComponent
         * Where x stands for CentralCabin which has all TileEdge.UNIVERSAL connectors
         * 
        */

        ship.addComponentTile(6, 7, batteryComponent);

        ship.removeBattery(6, 7);

        assertEquals(batteryComponent.getBatteryCapacity(), 1);
    }

    @Test
    void removeBatteryEmptyComponentTest() {
        ShipManager ship = new ShipManager(1);

        BatteryComponent batteryComponent = new BatteryComponent(2, TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE);

        /*
         *  4  5  6  7  8  9 10
         * 5        [ ]
         * 6     [ ][b][ ]
         * 7  [ ][ ][x][ ][ ]
         * 8  [ ][ ][ ][ ][ ]
         * 9  [ ][ ]   [ ][ ]
         * 
         * Where c stands for batteryComponent
         * Where x stands for CentralCabin which has all TileEdge.UNIVERSAL connectors
         * 
        */

        ship.addComponentTile(6, 7, batteryComponent);

        ship.removeBattery(6, 7);
        ship.removeBattery(6, 7);

        assertThrows(InvalidActionException.class, () -> ship.removeBattery(6, 7));
    }
}
