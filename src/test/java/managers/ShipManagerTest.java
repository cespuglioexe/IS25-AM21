package managers;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import it.polimi.it.galaxytrucker.model.componenttiles.BatteryComponent;
import it.polimi.it.galaxytrucker.model.componenttiles.CabinModule;
import it.polimi.it.galaxytrucker.model.componenttiles.CargoHold;
import it.polimi.it.galaxytrucker.model.componenttiles.CentralCabin;
import it.polimi.it.galaxytrucker.model.componenttiles.ComponentTile;
import it.polimi.it.galaxytrucker.model.componenttiles.DoubleCannon;
import it.polimi.it.galaxytrucker.model.componenttiles.DoubleEngine;
import it.polimi.it.galaxytrucker.model.componenttiles.LifeSupport;
import it.polimi.it.galaxytrucker.model.componenttiles.OutOfBoundsTile;
import it.polimi.it.galaxytrucker.model.componenttiles.SingleCannon;
import it.polimi.it.galaxytrucker.model.componenttiles.SingleEngine;
import it.polimi.it.galaxytrucker.model.componenttiles.SpecialCargoHold;
import it.polimi.it.galaxytrucker.model.componenttiles.StructuralModule;
import it.polimi.it.galaxytrucker.model.componenttiles.TileEdge;
import it.polimi.it.galaxytrucker.model.crewmates.Human;
import it.polimi.it.galaxytrucker.model.crewmates.Alien;
import it.polimi.it.galaxytrucker.model.exceptions.IllegalComponentPositionException;
import it.polimi.it.galaxytrucker.model.exceptions.InvalidActionException;
import it.polimi.it.galaxytrucker.model.managers.ShipManager;
import it.polimi.it.galaxytrucker.model.utility.AlienType;
import it.polimi.it.galaxytrucker.model.utility.Color;
import it.polimi.it.galaxytrucker.model.utility.Cargo;

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

        ComponentTile cannon = new SingleCannon(List.of(TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE));

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

        ComponentTile cannon = new SingleCannon(List.of(TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE));

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
    void getAllComponentsPositionOfTypeTypeNotPresentTest() {
        ShipManager ship = new ShipManager(1);

        assertEquals(Set.of(), ship.getAllComponentsPositionOfType(SingleCannon.class));
    }

    @Test
    void getAllComponentsPositionOfTypeTest() {
        ShipManager ship = new ShipManager(1);

        ComponentTile cannon = new SingleCannon(List.of(TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE));

        /*
         *  4  5  6  7  8  9 10
         * 5        [ ]
         * 6     [ ][ ][ ]
         * 7  [ ][ ][x][ ][ ]
         * 8  [ ][c][c][c][c]
         * 9  [ ][ ]   [c][ ]
        */

        for (int i = 5; i < 10; i++) {
            for (int j = 5; j < 10; j++) {
                switch (i) {
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

        assertTrue(() -> ship.getAllComponentsPositionOfType(SingleCannon.class).containsAll(
            Set.of(
                List.of(8, 6),
                List.of(8, 7),
                List.of(8, 8),
                List.of(8, 9),
                List.of(9, 8)
            )
        ));
    }

    @Test
    void getDisconnectedBranchesTest() {
        ShipManager ship = new ShipManager(1);

        ComponentTile cannon = new SingleCannon(List.of(TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE));

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

        List<Set<List<Integer>>> actual = ship.getDisconnectedBranches();

        List<Set<List<Integer>>> expected = new ArrayList<>();
        //isolated branch: [7][5]
        expected.add(Set.of(List.of(7, 5)));
        //other branch
        expected.add(Set.of(
            List.of(5, 7),
            List.of(6, 6),
            List.of(6, 7),
            List.of(7, 7),
            List.of(7, 8),
            List.of(8, 6),
            List.of(8, 7),
            List.of(8, 8),
            List.of(8, 9),
            List.of(9, 8)
        ));

        assertTrue(actual.containsAll(expected));
    }

    @Test
    void removeComponentTileCabinModuleUpdatesCrewmates() {
        ShipManager ship = new ShipManager(1);

        CabinModule cabin = new CabinModule(List.of(TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE));
        LifeSupport lifeSupport = new LifeSupport(AlienType.PURPLEALIEN, List.of(TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE));

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

        ship.removeComponentTile(6, 7);

        assertEquals(2, ship.countCrewmates());

        ship.addComponentTile(6, 7, cabin);
        
        ship.removeComponentTile(7, 7);

        assertEquals(1, ship.countCrewmates());
    }

    @Test
    void hasAlienTest() {
        ShipManager ship = new ShipManager(1);

        CabinModule cabinPurple = new CabinModule(List.of(TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE));
        CabinModule cabinBrown = new CabinModule(List.of(TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE));
        LifeSupport lifeSupportPurple = new LifeSupport(AlienType.PURPLEALIEN, List.of(TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE));
        LifeSupport lifeSupportBrown = new LifeSupport(AlienType.BROWNALIEN, List.of(TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE));

        Alien purple = new Alien(AlienType.PURPLEALIEN);
        Alien brown = new Alien(AlienType.BROWNALIEN);

        /*
         *  4  5  6  7  8  9 10
         * 5        [ ]
         * 6     [ ][c][l]
         * 7  [ ][ ][x][c][ ]
         * 8  [ ][ ][ ][l][ ]
         * 9  [ ][ ]   [ ][ ]
         * 
         * Where c stands for cabinModule
         * Where l stands for LifeSupport
         * Where x stands for CentralCabin which has all TileEdge.UNIVERSAL connectors
         * 
        */

        ship.addComponentTile(6, 7, cabinPurple);
        ship.addComponentTile(7, 8, cabinBrown);
        ship.addComponentTile(6, 8, lifeSupportPurple);
        ship.addComponentTile(8, 8, lifeSupportBrown);

        ship.addCrewmate(6, 7, purple);
        ship.addCrewmate(7, 8, brown);

        assertTrue(() -> ship.hasAlien(AlienType.PURPLEALIEN));
        assertTrue(() -> ship.hasAlien(AlienType.BROWNALIEN));

        ship.removeComponentTile(7, 8);

        assertFalse(() -> ship.hasAlien(AlienType.BROWNALIEN));
    }

    @Test
    void removeComponentTileRemovingLifeSupportTest() {
        ShipManager ship = new ShipManager(1);

        CabinModule cabinPurple = new CabinModule(List.of(TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE));
        CabinModule cabinBrown = new CabinModule(List.of(TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE));
        LifeSupport lifeSupportPurple = new LifeSupport(AlienType.PURPLEALIEN, List.of(TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE));
        LifeSupport lifeSupportBrown = new LifeSupport(AlienType.BROWNALIEN, List.of(TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE));

        Alien purple = new Alien(AlienType.PURPLEALIEN);
        Alien brown = new Alien(AlienType.BROWNALIEN);

        /*
         *  4  5  6  7  8  9 10
         * 5        [ ]
         * 6     [ ][c][l]
         * 7  [ ][ ][x][c][ ]
         * 8  [ ][ ][ ][l][ ]
         * 9  [ ][ ]   [ ][ ]
         * 
         * Where c stands for cabinModule
         * Where l stands for LifeSupport
         * Where x stands for CentralCabin which has all TileEdge.UNIVERSAL connectors
         * 
        */

        ship.addComponentTile(6, 7, cabinPurple);
        ship.addComponentTile(7, 8, cabinBrown);
        ship.addComponentTile(6, 8, lifeSupportPurple);
        ship.addComponentTile(8, 8, lifeSupportBrown);

        ship.addCrewmate(6, 7, purple);
        ship.addCrewmate(7, 8, brown);

        assertTrue(() -> ship.hasAlien(AlienType.PURPLEALIEN));

        ship.removeComponentTile(6, 8);

        assertFalse(() -> ship.hasAlien(AlienType.PURPLEALIEN));
    }

    @Test
    void removeBranchTest() {
        ShipManager ship = new ShipManager(1);

        ComponentTile cannon = new SingleCannon(List.of(TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE));

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

        List<Set<List<Integer>>> branches = ship.getDisconnectedBranches();

        //try to remove branch number 1
        ship.removeBranch(branches.get(0));

        assertTrue(() -> {
            for (List<Integer> coord : branches.get(0)) {
                if (!ship.getComponent(coord.get(0), coord.get(1)).equals(Optional.<ComponentTile>empty())) {
                    return false;
                }
            }
            return true;
        });
    }

    @Test
    void isShipLegalWithDisconnectedBranchesTest() {
        ShipManager ship = new ShipManager(1);

        ComponentTile cannon = new SingleCannon(List.of(TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE));

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

        ComponentTile singleConnector = new StructuralModule(List.of(TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE));
        ComponentTile engine = new SingleEngine(List.of(TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.INCOMPATIBLE));

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

        ComponentTile singleConnector = new StructuralModule(List.of(TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE));
        ComponentTile doubleConnector = new StructuralModule(List.of(TileEdge.DOUBLE, TileEdge.DOUBLE, TileEdge.DOUBLE, TileEdge.DOUBLE));

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

        ComponentTile singleConnector = new StructuralModule(List.of(TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE));

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

        ComponentTile singleConnector = new StructuralModule(List.of(TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE));

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
        CabinModule cabin = new CabinModule(List.of(TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE));

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

        assertEquals(centralCabin.getCrewmates().size(), 2);
        assertEquals(cabin.getCrewmates().size(), 1);
    }

    @Test
    void addCrewmateFullCabinTest() {
        ShipManager ship = new ShipManager(1);

        CabinModule cabin = new CabinModule(List.of(TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE));

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

        ship.addComponentTile(6, 7, cabin);

        ship.addCrewmate(6, 7, crewmate);
        ship.addCrewmate(6, 7, crewmate);

        assertThrows(InvalidActionException.class, () -> ship.addCrewmate(6, 7, crewmate));
    }

    @Test
    void addCrewmateAlienWithoutLifeSupportTest() {
        ShipManager ship = new ShipManager(1);

        CabinModule cabin = new CabinModule(List.of(TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE));

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

        CabinModule cabin = new CabinModule(List.of(TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE));
        LifeSupport lifeSupport = new LifeSupport(AlienType.PURPLEALIEN, List.of(TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE));

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
    void addCrewmateAlienAlreadyPresentTest() {
        ShipManager ship = new ShipManager(1);

        CabinModule cabin = new CabinModule(List.of(TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE));
        LifeSupport lifeSupport = new LifeSupport(AlienType.PURPLEALIEN, List.of(TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE));

        Alien alien = new Alien(AlienType.PURPLEALIEN);

        /*
         *  4  5  6  7  8  9 10
         * 5        [ ]
         * 6     [ ][c][l]
         * 7  [ ][ ][x][c][ ]
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
        ship.addComponentTile(7, 8, cabin);

        ship.addCrewmate(6, 7, alien);

        assertThrows(InvalidActionException.class, () -> ship.addCrewmate(7, 8, alien));
    }

    @Test
    void removeCrewmateIllegalComponentPositionTest() {
        ShipManager ship = new ShipManager(1);

        ComponentTile singleConnector = new StructuralModule(List.of(TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE));

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
        CabinModule cabin = new CabinModule(List.of(TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE));

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
        CabinModule cabin = new CabinModule(List.of(TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE));

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

        ship.removeCrewmate(6, 7);
        ship.removeCrewmate(7, 7);

        assertEquals(cabin.getCrewmates().size(), 1);
        assertEquals(centralCabin.getCrewmates().size(), 1);
    }

    @Test
    void removeCrewmateAlienTest() {
        ShipManager ship = new ShipManager(1);

        CabinModule cabin = new CabinModule(List.of(TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE));
        LifeSupport lifeSupport = new LifeSupport(AlienType.PURPLEALIEN, List.of(TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE));

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

        CabinModule cabin = new CabinModule(List.of(TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE));

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

        CargoHold cargoHold = new CargoHold(2, List.of(TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE));

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

        CargoHold cargoHold = new CargoHold(2, List.of(TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE));

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

        SpecialCargoHold specialCargoHold = new SpecialCargoHold(2, List.of(TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE));

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

        CargoHold cargoHold = new CargoHold(2, List.of(TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE));

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

        SpecialCargoHold specialCargoHold = new SpecialCargoHold(2, List.of(TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE));

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

        SpecialCargoHold specialCargoHold = new SpecialCargoHold(2, List.of(TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE));

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

        CabinModule cabin = new CabinModule(List.of(TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE));

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

        CargoHold cargoHold = new CargoHold(2, List.of(TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE));

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

        CargoHold cargoHold = new CargoHold(2, List.of(TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE));

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

        CargoHold cargoHold = new CargoHold(2, List.of(TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE));

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

        SpecialCargoHold specialCargoHold = new SpecialCargoHold(2, List.of(TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE));

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

        CargoHold cargoHold = new CargoHold(2, List.of(TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE));

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

        BatteryComponent batteryComponent = new BatteryComponent(2, List.of(TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE));

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

        BatteryComponent batteryComponent = new BatteryComponent(2, List.of(TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE));

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

    @Test
    void getCargoPositonTest() {
        ShipManager ship = new ShipManager(1);

        SpecialCargoHold specialCargoHold = new SpecialCargoHold(2, List.of(TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE));
        CargoHold cargoHold = new CargoHold(2, List.of(TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE));

        Cargo specialCargp = new Cargo(Color.RED);
        Cargo cargo1 = new Cargo(Color.BLUE);
        Cargo cargo2 = new Cargo(Color.YELLOW);

        /*
         *  4  5  6  7  8  9 10
         * 5        [ ]
         * 6     [ ][c][ ]
         * 7  [ ][ ][x][s][ ]
         * 8  [ ][ ][ ][ ][ ]
         * 9  [ ][ ]   [ ][ ]
         * 
         * Where c stands for cargoHold
         * Where s stands for specialCargoHold
         * Where x stands for CentralCabin which has all TileEdge.UNIVERSAL connectors
         * 
        */

        ship.addComponentTile(7, 8, specialCargoHold);
        ship.addComponentTile(6, 7, cargoHold);

        ship.addCargo(7, 8, specialCargp);
        ship.addCargo(7, 8, cargo2);
        ship.addCargo(6, 7, cargo1);
        ship.addCargo(6, 7, cargo2);        

        HashMap<Color, Set<List<Integer>>> cargoPosition = ship.getCargoPositon();

        assertEquals(Set.of(
            List.of(7, 8)
        ), cargoPosition.get(Color.RED));

        assertEquals(Set.of(
        List.of(6, 7)
        ), cargoPosition.get(Color.BLUE));

        assertEquals(Set.of(
            List.of(7, 8),
            List.of(6, 7)
        ), cargoPosition.get(Color.YELLOW));

        assertEquals(Set.of(), cargoPosition.get(Color.GREEN));

    }

    @Test
    void countExposedConnectorsOfTest() {
        ShipManager ship = new ShipManager(1);
        ComponentTile cannon = new SingleCannon(List.of(TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE));

        /*
         *  4  5  6  7  8  9 10
         * 5        [c]
         * 6     [c][c][ ]
         * 7  [c][ ][x][c][ ]
         * 8  [ ][c][c][c][c]
         * 9  [ ][ ]   [c][ ]
         * 
         * Where c stands for cannon
         * Where x stands for CentralCabin which has all TileEdge.UNIVERSAL connectors
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

        //exposed connectors of [7][5]: 4
        assertEquals(4, ship.countExposedConnectorsOf(7, 5));
        //exposed connectors of [5][7]: 3
        assertEquals(3, ship.countExposedConnectorsOf(5, 7));
        //exposed connectors of [7][8]: 2
        assertEquals(2, ship.countExposedConnectorsOf(7, 8));
        //exposed connectors of [8][7]: 1
        assertEquals(1, ship.countExposedConnectorsOf(8, 7));
        //exposed connectors of [8][8]: 0
        assertEquals(0, ship.countExposedConnectorsOf(8, 8));
        //exposed connectors of [7][6]: 0 (EMPTY)
        assertEquals(0, ship.countExposedConnectorsOf(7, 6));
        //exposed connectors of [5][4]: 0 (OUTSIDE)
        assertEquals(0, ship.countExposedConnectorsOf(5, 4));
    }

    @Test
    void countAllExposedConnectorsTest() {
        ShipManager ship = new ShipManager(1);
        ComponentTile cannon = new SingleCannon(List.of(TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE));

        /*
         *  4  5  6  7  8  9 10
         * 5        [c]
         * 6     [c][c][ ]
         * 7  [c][ ][x][c][ ]
         * 8  [ ][c][c][c][c]
         * 9  [ ][ ]   [c][ ]
         * 
         * Where c stands for cannon
         * Where x stands for CentralCabin which has all TileEdge.UNIVERSAL connectors
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

        assertEquals(24, ship.countAllExposedConnectors());
    }

    @Test
    void countHumansNoCabinTest() {
        ShipManager ship = new ShipManager(1);

        /*
         *  4  5  6  7  8  9 10
         * 5        [ ]
         * 6     [ ][ ][ ]
         * 7  [ ][ ][x][ ][ ]
         * 8  [ ][ ][ ][ ][ ]
         * 9  [ ][ ]   [ ][ ]
         * 
         * Where x stands for CentralCabin which has all TileEdge.UNIVERSAL connectors
         * 
        */

        assertEquals(2, ship.countHumans());
    }

    @Test
    void countHumansNoHumansTest() {
        ShipManager ship = new ShipManager(1);

        CabinModule cabin = new CabinModule(List.of(TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE));
        LifeSupport lifeSupport = new LifeSupport(AlienType.PURPLEALIEN, List.of(TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE));

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

        assertEquals(2, ship.countHumans());
    }

    @Test
    void countHumansTest() {
        ShipManager ship = new ShipManager(1);
        CabinModule cabin = new CabinModule(List.of(TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE));

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

        ship.addComponentTile(6, 7, cabin);
        ship.addCrewmate(6, 7, crewmate);

        assertEquals(3, ship.countHumans());
    }

    @Test
    void countCrewmatesNoCrewmatesTest() {
        ShipManager ship = new ShipManager(1);

        CabinModule cabin = new CabinModule(List.of(TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE));
        LifeSupport lifeSupport = new LifeSupport(AlienType.PURPLEALIEN, List.of(TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE));

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

        assertEquals(2, ship.countCrewmates());
    }

    @Test
    void countCrewmatesOnlyHumanstest() {
        ShipManager ship = new ShipManager(1);

        CabinModule cabin = new CabinModule(List.of(TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE));
        LifeSupport lifeSupport = new LifeSupport(AlienType.PURPLEALIEN, List.of(TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE));

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

        assertEquals(2, ship.countCrewmates());
    }

    @Test
    void countCrewmatesOnlyAliensTest() {
        ShipManager ship = new ShipManager(1);

        CabinModule cabin = new CabinModule(List.of(TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE));
        LifeSupport lifeSupport = new LifeSupport(AlienType.PURPLEALIEN, List.of(TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE));

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

        assertEquals(3, ship.countCrewmates());
    }

    @Test
    void countCrewmatesTest() {
        ShipManager ship = new ShipManager(1);

        CabinModule cabin = new CabinModule(List.of(TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE));
        LifeSupport lifeSupport = new LifeSupport(AlienType.PURPLEALIEN, List.of(TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE));

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

        assertEquals(3, ship.countCrewmates());
    }

    @Test
    void calculateFirePowerTest() {
        ShipManager ship = new ShipManager(1);

        SingleCannon cannon = new SingleCannon(List.of(TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE));
        DoubleCannon doubleCannon = new DoubleCannon(List.of(TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE));

        /*
         *  4  5  6  7  8  9 10
         * 5        [d]
         * 6     [ ][c][ ]
         * 7  [ ][c][x][c][ ]
         * 8  [ ][ ][ ][ ][ ]
         * 9  [ ][ ]   [ ][ ]
         * 
         * Where c stands for cannon
         * Where d stands for doubleCannon
         * Where x stands for CentralCabin which has all TileEdge.UNIVERSAL connectors
         * 
        */

        ship.addComponentTile(6, 7, cannon);
        ship.addComponentTile(7, 6, cannon);
        ship.addComponentTile(7, 8, cannon);
        ship.addComponentTile(5, 7, doubleCannon);

        assertEquals(3, ship.calculateFirePower());
    }

    @Test
    void calculateFirePowerWithAliensTest() {
        ShipManager ship = new ShipManager(1);

        SingleCannon cannon = new SingleCannon(List.of(TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE));
        DoubleCannon doubleCannon = new DoubleCannon(List.of(TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE));
        CabinModule cabin = new CabinModule(List.of(TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE));
        LifeSupport lifeSupport = new LifeSupport(AlienType.PURPLEALIEN, List.of(TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE));

        Alien alien = new Alien(AlienType.PURPLEALIEN);

        /*
         *  4  5  6  7  8  9 10
         * 5        [d]
         * 6     [ ][c][ ]
         * 7  [ ][c][x][c][ ]
         * 8  [ ][ ][ ][C][l]
         * 9  [ ][ ]   [ ][ ]
         * 
         * Where c stands for cannon
         * Where d stands for doubleCannon
         * Where C stands for cabin
         * Where l stands for lifeSupport
         * Where x stands for CentralCabin which has all TileEdge.UNIVERSAL connectors
         * 
        */

        ship.addComponentTile(6, 7, cannon);
        ship.addComponentTile(7, 6, cannon);
        ship.addComponentTile(7, 8, cannon);
        ship.addComponentTile(5, 7, doubleCannon);
        ship.addComponentTile(8, 8, cabin);
        ship.addComponentTile(8, 9, lifeSupport);

        ship.addCrewmate(8, 8, alien);

        assertEquals(5, ship.calculateFirePower());
    }

    @Test
    void calculateFirePowerWithAliensNoCannonTest() {
        ShipManager ship = new ShipManager(1);

        DoubleCannon doubleCannon = new DoubleCannon(List.of(TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE));
        CabinModule cabin = new CabinModule(List.of(TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE));
        LifeSupport lifeSupport = new LifeSupport(AlienType.PURPLEALIEN, List.of(TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE));

        Alien alien = new Alien(AlienType.PURPLEALIEN);

        /*
         *  4  5  6  7  8  9 10
         * 5        [d]
         * 6     [ ][ ]][ ]
         * 7  [ ][ ][x][ ][ ]
         * 8  [ ][ ][ ][C][l]
         * 9  [ ][ ]   [ ][ ]
         * 
         * Where d stands for doubleCannon
         * Where C stands for cabin
         * Where l stands for lifeSupport
         * Where x stands for CentralCabin which has all TileEdge.UNIVERSAL connectors
         * 
        */

        ship.addComponentTile(5, 7, doubleCannon);
        ship.addComponentTile(8, 8, cabin);
        ship.addComponentTile(8, 9, lifeSupport);

        ship.addCrewmate(8, 8, alien);
        
        assertEquals(0, ship.calculateFirePower());
    }
    
    @Test
    void calculateEnginePowerTest() {
        ShipManager ship = new ShipManager(1);

        SingleEngine engine = new SingleEngine(List.of(TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE));
        DoubleEngine doubleEngine = new DoubleEngine(List.of(TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE));

        /*
         *  4  5  6  7  8  9 10
         * 5        [d]
         * 6     [ ][e][ ]
         * 7  [ ][e][x][e][ ]
         * 8  [ ][ ][ ][ ][ ]
         * 9  [ ][ ]   [ ][ ]
         * 
         * Where c stands for engine
         * Where d stands for doubleEngine
         * Where x stands for CentralCabin which has all TileEdge.UNIVERSAL connectors
         * 
        */

        ship.addComponentTile(6, 7, engine);
        ship.addComponentTile(7, 6, engine);
        ship.addComponentTile(7, 8, engine);
        ship.addComponentTile(5, 7, doubleEngine);

        assertEquals(3, ship.calculateEnginePower());
    }

    @Test
    void activateComponentTest() {
        ShipManager ship = new ShipManager(1);

        SingleCannon cannon = new SingleCannon(List.of(TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE));
        DoubleCannon doubleCannon = new DoubleCannon(List.of(TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE));
        BatteryComponent battery = new BatteryComponent(2, List.of(TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE));

        /*
         *  4  5  6  7  8  9 10
         * 5        [d]
         * 6     [ ][c][ ]
         * 7  [ ][c][x][c][ ]
         * 8  [ ][ ][ ][b][ ]
         * 9  [ ][ ]   [ ][ ]
         * 
         * Where c stands for cannon
         * Where d stands for doubleCannon
         * Where b stands for battery
         * Where x stands for CentralCabin which has all TileEdge.UNIVERSAL connectors
         * 
        */

        ship.addComponentTile(6, 7, cannon);
        ship.addComponentTile(7, 6, cannon);
        ship.addComponentTile(7, 8, cannon);
        ship.addComponentTile(5, 7, doubleCannon);
        ship.addComponentTile(8, 8, battery);

        HashMap<List<Integer>, List<Integer>> cannonsAndBatteries = new HashMap<>();

        cannonsAndBatteries.put(List.of(5, 7), List.of(8, 8));

        assertEquals(5, ship.calculateFirePower() + ship.activateComponent(cannonsAndBatteries));
        assertEquals(1, battery.getBatteryCapacity());

        assertEquals(5, ship.calculateFirePower() + ship.activateComponent(cannonsAndBatteries));
        assertEquals(0, battery.getBatteryCapacity());

        assertThrows(InvalidActionException.class, () -> ship.activateComponent(cannonsAndBatteries));
    }

    @Test
    void calculateFirePowerOfOnlyDouble() {
        ShipManager ship = new ShipManager(1);

        DoubleCannon doubleCannon = new DoubleCannon(List.of(TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE));
        BatteryComponent battery = new BatteryComponent(2, List.of(TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE));
        CabinModule cabin = new CabinModule(List.of(TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE));
        LifeSupport lifeSupport = new LifeSupport(AlienType.PURPLEALIEN, List.of(TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE));

        Alien alien = new Alien(AlienType.PURPLEALIEN);

        /*
         *  4  5  6  7  8  9 10
         * 5        [d]
         * 6     [ ][ ]][ ]
         * 7  [ ][ ][x][ ][ ]
         * 8  [ ][b][ ][C][l]
         * 9  [ ][ ]   [ ][ ]
         *
         * Where d stands for doubleCannon
         * Where b stands for battery
         * Where C stands for cabin
         * Where l stands for lifeSupport
         * Where x stands for CentralCabin which has all TileEdge.UNIVERSAL connectors
         * 
        */

        ship.addComponentTile(5, 7, doubleCannon);
        ship.addComponentTile(8, 6, battery);
        ship.addComponentTile(8, 8, cabin);
        ship.addComponentTile(8, 9, lifeSupport);

        ship.addCrewmate(8, 8, alien);

        HashMap<List<Integer>, List<Integer>> cannonsAndBatteries = new HashMap<>();

        cannonsAndBatteries.put(List.of(5, 7), List.of(8, 6));

        assertEquals(4, ship.calculateFirePower(cannonsAndBatteries));
    }

    @Test
    void calculateFirePowerOfBothSingleAndDoubleTest() {
        ShipManager ship = new ShipManager(1);

        SingleCannon cannon = new SingleCannon(List.of(TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE));
        DoubleCannon doubleCannon = new DoubleCannon(List.of(TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE));
        BatteryComponent battery = new BatteryComponent(2, List.of(TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE));
        CabinModule cabin = new CabinModule(List.of(TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE));
        LifeSupport lifeSupport = new LifeSupport(AlienType.PURPLEALIEN, List.of(TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE));

        Alien alien = new Alien(AlienType.PURPLEALIEN);

        /*
         *  4  5  6  7  8  9 10
         * 5        [d]
         * 6     [ ][c]][ ]
         * 7  [ ][c][x][c][ ]
         * 8  [ ][b][ ][C][l]
         * 9  [ ][ ]   [ ][ ]
         *
         * Where c stands for cannon 
         * Where d stands for doubleCannon
         * Where b stands for battery
         * Where C stands for cabin
         * Where l stands for lifeSupport
         * Where x stands for CentralCabin which has all TileEdge.UNIVERSAL connectors
         * 
        */

        ship.addComponentTile(6, 7, cannon);
        ship.addComponentTile(7, 6, cannon);
        ship.addComponentTile(7, 8, cannon);
        ship.addComponentTile(5, 7, doubleCannon);
        ship.addComponentTile(8, 6, battery);
        ship.addComponentTile(8, 8, cabin);
        ship.addComponentTile(8, 9, lifeSupport);

        ship.addCrewmate(8, 8, alien);

        HashMap<List<Integer>, List<Integer>> cannonsAndBatteries = new HashMap<>();

        cannonsAndBatteries.put(List.of(5, 7), List.of(8, 6));

        assertEquals(7, ship.calculateFirePower(cannonsAndBatteries));
    }

    @Test
    void calculateFirePowerOfBothNoCannonsTest() {
        ShipManager ship = new ShipManager(1);

        SingleEngine engine = new SingleEngine(List.of(TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE));
        DoubleEngine doubleEngine = new DoubleEngine(List.of(TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE));
        BatteryComponent battery = new BatteryComponent(2, List.of(TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE));
        CabinModule cabin = new CabinModule(List.of(TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE));
        LifeSupport lifeSupport = new LifeSupport(AlienType.BROWNALIEN, List.of(TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE));

        Alien alien = new Alien(AlienType.BROWNALIEN);

        /*
         *  4  5  6  7  8  9 10
         * 5        [d]
         * 6     [ ][e]][ ]
         * 7  [ ][e][x][e][ ]
         * 8  [ ][b][ ][C][l]
         * 9  [ ][ ]   [ ][ ]
         *
         * Where e stands for engine 
         * Where d stands for doubleEngine
         * Where b stands for battery
         * Where C stands for cabin
         * Where l stands for lifeSupport
         * Where x stands for CentralCabin which has all TileEdge.UNIVERSAL connectors
         * 
        */

        ship.addComponentTile(6, 7, engine);
        ship.addComponentTile(7, 6, engine);
        ship.addComponentTile(7, 8, engine);
        ship.addComponentTile(5, 7, doubleEngine);
        ship.addComponentTile(8, 6, battery);
        ship.addComponentTile(8, 8, cabin);
        ship.addComponentTile(8, 9, lifeSupport);

        ship.addCrewmate(8, 8, alien);
        
        assertEquals(0, ship.calculateFirePower(new HashMap<>()));
    }

    @Test
    void calculateEnginePowerOfBothSingleAndDoubleTest() {
        ShipManager ship = new ShipManager(1);

        SingleEngine engine = new SingleEngine(List.of(TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE));
        DoubleEngine doubleEngine = new DoubleEngine(List.of(TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE));
        BatteryComponent battery = new BatteryComponent(2, List.of(TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE));
        CabinModule cabin = new CabinModule(List.of(TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE));
        LifeSupport lifeSupport = new LifeSupport(AlienType.BROWNALIEN, List.of(TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE));

        Alien alien = new Alien(AlienType.BROWNALIEN);

        /*
         *  4  5  6  7  8  9 10
         * 5        [d]
         * 6     [ ][e]][ ]
         * 7  [ ][e][x][e][ ]
         * 8  [ ][b][ ][C][l]
         * 9  [ ][ ]   [ ][ ]
         *
         * Where e stands for engine 
         * Where d stands for doubleEngine
         * Where b stands for battery
         * Where C stands for cabin
         * Where l stands for lifeSupport
         * Where x stands for CentralCabin which has all TileEdge.UNIVERSAL connectors
         * 
        */

        ship.addComponentTile(6, 7, engine);
        ship.addComponentTile(7, 6, engine);
        ship.addComponentTile(7, 8, engine);
        ship.addComponentTile(5, 7, doubleEngine);
        ship.addComponentTile(8, 6, battery);
        ship.addComponentTile(8, 8, cabin);
        ship.addComponentTile(8, 9, lifeSupport);

        ship.addCrewmate(8, 8, alien);

        HashMap<List<Integer>, List<Integer>> enginesAndBatteries = new HashMap<>();

        enginesAndBatteries.put(List.of(5, 7), List.of(8, 6));

        assertEquals(7, ship.calculateEnginePower(enginesAndBatteries));
    }
}
