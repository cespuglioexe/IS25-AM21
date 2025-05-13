package managers;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import it.polimi.it.galaxytrucker.model.componenttiles.ComponentTile;
import it.polimi.it.galaxytrucker.model.componenttiles.SingleCannon;
import it.polimi.it.galaxytrucker.model.componenttiles.SingleEngine;
import it.polimi.it.galaxytrucker.model.componenttiles.StructuralModule;
import it.polimi.it.galaxytrucker.model.componenttiles.TileEdge;
import it.polimi.it.galaxytrucker.exceptions.IllegalComponentPositionException;
import it.polimi.it.galaxytrucker.model.managers.ShipBoard;

public class ShipBoardTest {

    @Test
    void correctShipBoardSizeTest() {
        ShipBoard ship = new ShipBoard();

        assertEquals(ship.getBoard().size(), 5);
        assertEquals(ship.getBoard().get(0).size(), 7);
    }
    
    @Test
    void correctShipBoardTest() {
        ShipBoard ship = new ShipBoard();

        ship.printBoard();
    }

    @Test
    void correctLevel1Ship() {
        ShipBoard ship = new ShipBoard();

        ship.setShipBounds(1);
        ship.printBoard();
    }

    @Test
    void correctLevel2Ship() {
        ShipBoard ship = new ShipBoard();

        ship.setShipBounds(2);
        ship.printBoard();
    }

    @Test
    void getAllComponentsPositionOfTypeTest() throws IllegalComponentPositionException {
        ShipBoard ship = new ShipBoard();
        ComponentTile cannon1 = new SingleCannon(List.of(TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE));
        ComponentTile cannon2 = new SingleCannon(List.of(TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE));
        ComponentTile engine = new SingleEngine(List.of(TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE));
        ComponentTile structuralModule = new StructuralModule(List.of(TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE));
        
        ship.addComponentTile(0, 0, cannon1);
        ship.addComponentTile(1, 1, cannon2);
        ship.addComponentTile(2, 2, engine);
        ship.addComponentTile(3, 3, structuralModule);

        assertEquals(new HashSet<>(List.of(List.of(0, 0), List.of(1, 1))), 
             ship.getAllComponentsPositionOfType(cannon1.getClass()));

        assertEquals(new HashSet<>(List.of(List.of(2, 2))), 
             ship.getAllComponentsPositionOfType(engine.getClass()));

        assertEquals(new HashSet<>(List.of(List.of(3, 3))), 
             ship.getAllComponentsPositionOfType(structuralModule.getClass()));
    }

    @Test
    void addComponentTest() throws IllegalComponentPositionException {
        ShipBoard ship = new ShipBoard();
        ComponentTile cannon = new SingleCannon(List.of(TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE));
        List<Integer> position = List.of(0,2);

        ship.addComponentTile(position.get(0), position.get(1), cannon);

        assertTrue(ship.getComponent(position.get(0), position.get(1)).isPresent());
        assertEquals(ship.getComponent(position.get(0), position.get(1)).get(), cannon);

        assertTrue(ship.getAllComponentsPositionOfType(cannon.getClass()).contains(position));
    }

    @Test
    void illegalAddToAlreadyTakenPositionTest() throws IllegalComponentPositionException {
        ShipBoard ship = new ShipBoard();
        ComponentTile cannon = new SingleCannon(List.of(TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE));
        List<Integer> position = List.of(0,2);

        ship.addComponentTile(position.get(0), position.get(1), cannon);

        assertThrows(IllegalComponentPositionException.class, () -> ship.addComponentTile(position.get(0), position.get(1), cannon));
    }

    @Test
    void illegalAddOutsideShipPositionTest() {
        ShipBoard ship = new ShipBoard();
        ComponentTile cannon = new SingleCannon(List.of(TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE));

        ship.setShipBounds(1);

        assertThrows(IllegalComponentPositionException.class, () -> ship.addComponentTile(0, 0, cannon));
    }

    @Test
    void removeComponentTileTest() throws IllegalComponentPositionException {
        ShipBoard ship = new ShipBoard();
        ComponentTile cannon = new SingleCannon(List.of(TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE));
        List<Integer> position = List.of(0,2);

        ship.addComponentTile(position.get(0), position.get(1), cannon);
        ship.addComponentTile(position.get(0), position.get(1) + 1, cannon);
        ship.printBoard();

        ship.removeComponentTile(position.get(0), position.get(1));
        ship.printBoard();

        assertTrue(ship.getComponent(position.get(0), position.get(1)).isEmpty());
        assertTrue(!ship.getAllComponentsPositionOfType(cannon.getClass()).contains(position));
    }

    @Test
    void illegalRemoveEmptyPositionTest() {
        ShipBoard ship = new ShipBoard();
        List<Integer> position = List.of(0,2);

        assertThrows(IllegalComponentPositionException.class, () -> ship.removeComponentTile(position.get(0), position.get(1)));
    }

    @Test
    void illegalRemoveOutsideShipPositionTest() {
        ShipBoard ship = new ShipBoard();
        ship.setShipBounds(1);

        assertThrows(IllegalComponentPositionException.class, () -> ship.removeComponentTile(0, 0));
    }

    @Test
    void getAllNeighbourComponentsTest() throws IllegalComponentPositionException {
        ShipBoard ship = new ShipBoard();
        ComponentTile cannon = new SingleCannon(List.of(TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE));
        List<Integer> position = List.of(2,2);

        //component
        ship.addComponentTile(position.get(0), position.get(1), cannon);
        //right component
        ship.addComponentTile(position.get(0), position.get(1) + 1, cannon);
        //up component
        ship.addComponentTile(position.get(0) - 1, position.get(1), cannon);
        //left component
        ship.addComponentTile(position.get(0), position.get(1) - 1, cannon);
        //down component
        ship.addComponentTile(position.get(0) + 1, position.get(1), cannon);

        List<Optional<ComponentTile>> result = ship.getNeighbourComponents(position.get(0), position.get(1));

        assertIterableEquals(List.of(Optional.of(cannon), Optional.of(cannon), Optional.of(cannon), Optional.of(cannon)), result);
    }

    @Test
    void getNeighbourComponentsWithBlanksTest() throws IllegalComponentPositionException {
        ShipBoard ship = new ShipBoard();
        ComponentTile cannon = new SingleCannon(List.of(TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE));
        List<Integer> position = List.of(2,2);

        //component
        ship.addComponentTile(position.get(0), position.get(1), cannon);
        //up component
        ship.addComponentTile(position.get(0) - 1, position.get(1), cannon);
        //right component is empty
        //down component
        ship.addComponentTile(position.get(0) + 1, position.get(1), cannon);
        //left component
        ship.addComponentTile(position.get(0), position.get(1) - 1, cannon);

        List<Optional<ComponentTile>> result = ship.getNeighbourComponents(position.get(0), position.get(1));

        assertIterableEquals(List.of(Optional.of(cannon), Optional.<ComponentTile>empty(), Optional.of(cannon), Optional.of(cannon)), result);
    }

    @Test
    void getNeighbourComponentsAtMarginTest() throws IllegalComponentPositionException {
        ShipBoard ship = new ShipBoard();
        ComponentTile cannon = new SingleCannon(List.of(TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE));
        List<Integer> position = List.of(1,2);

        ship.setShipBounds(1);

        //component
        ship.addComponentTile(position.get(0), position.get(1), cannon);
        //up component is outside
        //right component
        ship.addComponentTile(position.get(0), position.get(1) + 1, cannon);
        //down component
        ship.addComponentTile(position.get(0) + 1, position.get(1), cannon);
        //left component is outside

        List<Optional<ComponentTile>> result = ship.getNeighbourComponents(position.get(0), position.get(1));

        for (int i = 0; i < result.size(); i++) {
            assertEquals(result.get(i).get().getClass(), result.get(i).get().getClass());
        }
    }

    @Test
    void getNeighbourComponentsWithAllBlanksTest() throws IllegalComponentPositionException {
        ShipBoard ship = new ShipBoard();
        ComponentTile cannon = new SingleCannon(List.of(TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE));
        List<Integer> position = List.of(2,2);

        //component
        ship.addComponentTile(position.get(0), position.get(1), cannon);
        //right component is empty
        //up component is empty
        //left component is empty
        //down component is empty

        List<Optional<ComponentTile>> result = ship.getNeighbourComponents(position.get(0), position.get(1));

        assertIterableEquals(List.of(Optional.<ComponentTile>empty(), Optional.<ComponentTile>empty(), Optional.<ComponentTile>empty(), Optional.<ComponentTile>empty()), result);
    }

    @Test
    void countExposedConnectorsEdgesExposedTest() throws IllegalComponentPositionException {
        ShipBoard ship = new ShipBoard();
        ComponentTile cannon = new SingleCannon(List.of(TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE));

        /*
         * [ ][ ][ ][c][ ][ ][ ]
         * [ ][ ][ ][c][ ][ ][ ]
         * [ ][c][ ][c][c][ ][ ]
         * [ ][c][c][c][c][c][ ]
         * [ ][ ][ ][ ][c][ ][ ]
        */

        for (int i = 1; i < 5; i++) {
            for (int j = 1; j < 6; j++) {
                if (i == 1) {
                    if (j == 2) {
                        ship.addComponentTile(i, j, cannon);
                    }
                    continue;
                }
                if (i == 2) {
                    if (j == 1 || j == 3 || j == 4) {
                        ship.addComponentTile(i, j, cannon);
                    }
                    continue;
                }
                if (i == 3) {
                    ship.addComponentTile(i, j, cannon);
                } else {
                    if (j == 4) {
                        ship.addComponentTile(i, j, cannon);
                    }
                }
            }
        }

        //exposed connectors of [1][2]: 4
        assertEquals(4, ship.countExposedConnectors(1, 2));
        //exposed connectors of [2][1]: 3
        assertEquals(3, ship.countExposedConnectors(2, 1));
        //exposed connectors of [3][1]: 2
        assertEquals(2, ship.countExposedConnectors(3, 1));
        //exposed connectors of [3][3]: 1
        assertEquals(1, ship.countExposedConnectors(3, 3));
        //exposed connectors of [3][4]: 0
        assertEquals(0, ship.countExposedConnectors(3, 4));
        //exposed connectors of [0][0]: 0 (EMPTY)
        assertEquals(0, ship.countExposedConnectors(0, 0));
    }

    @Test
    void countExposedConnectorsLevel1ShipTest() throws IllegalComponentPositionException {
        ShipBoard ship = new ShipBoard();
        ComponentTile cannon = new SingleCannon(List.of(TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE));

        /*
         *          [c]
         *       [c][c][ ]
         *    [c][ ][x][c][ ]
         *    [ ][c][c][c][c]
         *    [ ][ ]   [c][ ]
        */

        ship.setShipBounds(1);
        for (int i = 0; i < 5; i++) {
            for (int j = 1; j < 6; j++) {
                switch (i) {
                    case 0:
                        if (j == 3) {
                            ship.addComponentTile(i, j, cannon);
                        }
                        break;
                    case 1:
                        if (j > 1 && j < 4) {
                            ship.addComponentTile(i, j, cannon);
                        }
                        break;
                    case 2:
                        if ((j == 1 || (j > 2 && j < 5)) && j != 3) {
                            ship.addComponentTile(i, j, cannon);
                        }
                        break;
                    case 3:
                        if (j > 1 && j < 6) {
                            ship.addComponentTile(i, j, cannon);
                        }
                        break;
                    case 4:
                        if (j == 4) {
                            ship.addComponentTile(i, j, cannon);
                        }
                        break;
                }
            }
        }

        ship.printBoard();
        //exposed connectors of [2][1]: 4
        assertEquals(4, ship.countExposedConnectors(2, 1));
        //exposed connectors of [0][3]: 3
        assertEquals(3, ship.countExposedConnectors(0, 3));
        //exposed connectors of [2][4]: 2
        assertEquals(2, ship.countExposedConnectors(2, 4));
        //exposed connectors of [3][3]: 1
        assertEquals(1, ship.countExposedConnectors(3, 3));
        //exposed connectors of [3][4]: 0
        assertEquals(0, ship.countExposedConnectors(3, 4));
        //exposed connectors of [2][2]: 0 (EMPTY)
        assertEquals(0, ship.countExposedConnectors(2, 2));
        //exposed connectors of [0][0]: 0 (OUTSIDE)
        assertEquals(0, ship.countExposedConnectors(0, 0));
    }

    @Test
    void getBranchOfComponentTest() throws IllegalComponentPositionException {
        ShipBoard ship = new ShipBoard();
        ComponentTile cannon = new SingleCannon(List.of(TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE));

        /*
         *          [c]
         *       [c][c][ ]
         *    [c][ ][x][c][ ]
         *    [ ][c][c][c][c]
         *    [ ][ ]   [c][ ]
        */

        ship.setShipBounds(1);
        for (int i = 0; i < 5; i++) {
            for (int j = 1; j < 6; j++) {
                switch (i) {
                    case 0:
                        if (j == 3) {
                            ship.addComponentTile(i, j, cannon);
                        }
                        break;
                    case 1:
                        if (j > 1 && j < 4) {
                            ship.addComponentTile(i, j, cannon);
                        }
                        break;
                    case 2:
                        if ((j == 1 || (j > 2 && j < 5)) && j != 3) {
                            ship.addComponentTile(i, j, cannon);
                        }
                        break;
                    case 3:
                        if (j > 1 && j < 6) {
                            ship.addComponentTile(i, j, cannon);
                        }
                        break;
                    case 4:
                        if (j == 4) {
                            ship.addComponentTile(i, j, cannon);
                        }
                        break;
                }
            }
        }

        ship.printBoard();

        //this should be all elements except for [2][1]

        Set<List<Integer>> actual = new HashSet<>();
        ship.getBranchOfComponent(0, 3, actual);

        ship.printBranch(1, actual);

        assertTrue(() -> !actual.contains(List.of(2, 1)));
    }

    @Test
    void getDisconnectedBranchesTest() throws IllegalComponentPositionException {
        ShipBoard ship = new ShipBoard();
        ComponentTile cannon = new SingleCannon(List.of(TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE));

        /*
         *          [c]
         *       [c][c][ ]
         *    [c][ ][x][c][ ]
         *    [ ][c][c][c][c]
         *    [ ][ ]   [c][ ]
        */

        ship.setShipBounds(1);
        for (int i = 0; i < 5; i++) {
            for (int j = 1; j < 6; j++) {
                switch (i) {
                    case 0:
                        if (j == 3) {
                            ship.addComponentTile(i, j, cannon);
                        }
                        break;
                    case 1:
                        if (j > 1 && j < 4) {
                            ship.addComponentTile(i, j, cannon);
                        }
                        break;
                    case 2:
                        if ((j == 1 || (j > 2 && j < 5)) && j != 3) {
                            ship.addComponentTile(i, j, cannon);
                        }
                        break;
                    case 3:
                        if (j > 1 && j < 6) {
                            ship.addComponentTile(i, j, cannon);
                        }
                        break;
                    case 4:
                        if (j == 4) {
                            ship.addComponentTile(i, j, cannon);
                        }
                        break;
                }
            }
        }

        ship.printBoard();

        List<Set<List<Integer>>> actual = ship.getDisconnectedBranches();

        List<Set<List<Integer>>> expected = new ArrayList<>();
        //isolated branch: [2][1]
        expected.add(Set.of(List.of(2, 1)));
        //other branch
        expected.add(Set.of(
            List.of(0, 3),
            List.of(1, 2),
            List.of(1, 3),
            List.of(2, 3),
            List.of(2, 4),
            List.of(3, 2),
            List.of(3, 3),
            List.of(3, 4),
            List.of(3, 5),
            List.of(4, 4)
        ));

        int i = 0;
        for (Set<List<Integer>> branch : actual) {
            System.out.println("BRANCH NUMBER " + ++i);
            ship.printBranch(1, branch);
        }

        assertTrue(actual.containsAll(expected));
    }

    @Test
    void removeBranchTest() throws IllegalComponentPositionException {
        ShipBoard ship = new ShipBoard();
        ComponentTile cannon = new SingleCannon(List.of(TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE));

        /*
         *          [c]
         *       [c][c][ ]
         *    [c][ ][x][c][ ]
         *    [ ][c][c][c][c]
         *    [ ][ ]   [c][ ]
        */

        ship.setShipBounds(1);
        for (int i = 0; i < 5; i++) {
            for (int j = 1; j < 6; j++) {
                switch (i) {
                    case 0:
                        if (j == 3) {
                            ship.addComponentTile(i, j, cannon);
                        }
                        break;
                    case 1:
                        if (j > 1 && j < 4) {
                            ship.addComponentTile(i, j, cannon);
                        }
                        break;
                    case 2:
                        if ((j == 1 || (j > 2 && j < 5)) && j != 3) {
                            ship.addComponentTile(i, j, cannon);
                        }
                        break;
                    case 3:
                        if (j > 1 && j < 6) {
                            ship.addComponentTile(i, j, cannon);
                        }
                        break;
                    case 4:
                        if (j == 4) {
                            ship.addComponentTile(i, j, cannon);
                        }
                        break;
                }
            }
        }

        ship.printBoard();

        List<Set<List<Integer>>> branches = ship.getDisconnectedBranches();

        //try to remove branch number 1
        ship.removeBranch(branches.get(0));
        ship.printBoard();

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
    void removeBranchWithEmptySlotsTest() throws IllegalComponentPositionException {
        ShipBoard ship = new ShipBoard();
        ComponentTile cannon = new SingleCannon(List.of(TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE));

        /*
         *          [c]
         *       [c][c][ ]
         *    [c][ ][x][c][ ]
         *    [ ][c][c][c][c]
         *    [ ][ ]   [c][ ]
        */

        ship.setShipBounds(1);
        for (int i = 0; i < 5; i++) {
            for (int j = 1; j < 6; j++) {
                switch (i) {
                    case 0:
                        if (j == 3) {
                            ship.addComponentTile(i, j, cannon);
                        }
                        break;
                    case 1:
                        if (j > 1 && j < 4) {
                            ship.addComponentTile(i, j, cannon);
                        }
                        break;
                    case 2:
                        if ((j == 1 || (j > 2 && j < 5)) && j != 3) {
                            ship.addComponentTile(i, j, cannon);
                        }
                        break;
                    case 3:
                        if (j > 1 && j < 6) {
                            ship.addComponentTile(i, j, cannon);
                        }
                        break;
                    case 4:
                        if (j == 4) {
                            ship.addComponentTile(i, j, cannon);
                        }
                        break;
                }
            }
        }

        //creating an invalid branch of just one component: empty slot at [1][4]
        Set<List<Integer>> invalidBranch = Set.of(List.of(1, 4));
        assertThrows(IllegalComponentPositionException.class, () -> ship.removeBranch(invalidBranch));
    }
}
