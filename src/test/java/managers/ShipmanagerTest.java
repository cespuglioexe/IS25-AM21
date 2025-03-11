package managers;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;
import java.util.List;

import it.polimi.it.galaxytrucker.componenttiles.ComponentTile;
import it.polimi.it.galaxytrucker.componenttiles.SingleCannon;
import it.polimi.it.galaxytrucker.componenttiles.SingleEngine;
import it.polimi.it.galaxytrucker.componenttiles.StructuralModule;
import it.polimi.it.galaxytrucker.componenttiles.TileEdge;
import it.polimi.it.galaxytrucker.managers.ShipManager;

public class ShipManagerTest {

    // @Test
    // void toTileMatrixCoordOutOfBoundRowTest() {
    //     ShipManager ship = new ShipManager(1);

    //     //for values lower than game board start index
    //     assertThrows(IndexOutOfBoundsException.class, () -> ship.toTileMatrixCoord(Optional.of(0), Optional.empty()));
    //     assertThrows(IndexOutOfBoundsException.class, () -> ship.toTileMatrixCoord(Optional.empty(), Optional.of(0)));
    //     assertThrows(IndexOutOfBoundsException.class, () -> ship.toTileMatrixCoord(Optional.of(0), Optional.of(0)));

    //     //for values larger than game board capacity
    //     assertThrows(IndexOutOfBoundsException.class, () -> ship.toTileMatrixCoord(Optional.of(10), Optional.empty()));
    //     assertThrows(IndexOutOfBoundsException.class, () -> ship.toTileMatrixCoord(Optional.empty(), Optional.of(11)));
    //     assertThrows(IndexOutOfBoundsException.class, () -> ship.toTileMatrixCoord(Optional.of(10), Optional.of(11)));
    // }

    // @Test
    // void toTileMatrixCoordTest() {
    //     ShipManager ship = new ShipManager(1);

    //     assertEquals(List.of(Optional.of(0), Optional.empty()), ship.toTileMatrixCoord(Optional.of(5), Optional.empty()));
    //     assertEquals(List.of(Optional.empty(), Optional.of(0)), ship.toTileMatrixCoord(Optional.empty(), Optional.of(4)));
    //     assertEquals(List.of(Optional.of(0), Optional.of(0)), ship.toTileMatrixCoord(Optional.of(5), Optional.of(4)));
    // }

    // @Test
    // void toBoardCoordOutOfBoundRowTest() {
    //     ShipManager ship = new ShipManager(1);

    //     //for values lower than game board start index
    //     assertThrows(IndexOutOfBoundsException.class, () -> ship.toBoardCoord(Optional.of(-1), Optional.empty()));
    //     assertThrows(IndexOutOfBoundsException.class, () -> ship.toBoardCoord(Optional.empty(), Optional.of(-1)));
    //     assertThrows(IndexOutOfBoundsException.class, () -> ship.toBoardCoord(Optional.of(-1), Optional.of(-1)));

    //     //for values larger than game board capacity
    //     assertThrows(IndexOutOfBoundsException.class, () -> ship.toBoardCoord(Optional.of(5), Optional.empty()));
    //     assertThrows(IndexOutOfBoundsException.class, () -> ship.toBoardCoord(Optional.empty(), Optional.of(7)));
    //     assertThrows(IndexOutOfBoundsException.class, () -> ship.toBoardCoord(Optional.of(5), Optional.of(7)));
    // }

    // @Test
    // void toBoardCoordTest() {
    //     ShipManager ship = new ShipManager(1);

    //     assertEquals(List.of(Optional.of(5), Optional.empty()), ship.toBoardCoord(Optional.of(0), Optional.empty()));
    //     assertEquals(List.of(Optional.empty(), Optional.of(4)), ship.toBoardCoord(Optional.empty(), Optional.of(0)));
    //     assertEquals(List.of(Optional.of(5), Optional.of(4)), ship.toBoardCoord(Optional.of(0), Optional.of(0)));
    // }

    @Test
    void isShipLegalWithDisconnectedBranchesTest() {
        ShipManager ship = new ShipManager(1);

        ComponentTile cannon = new SingleCannon(TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE);

        /*
         *          [c]
         *       [c][c][ ]
         *    [c][ ][x][c][ ]
         *    [ ][c][c][c][c]
         *    [ ][ ]   [c][ ]
        */

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
         *          [s]
         *       [s][s][ ]
         *    [ ][ ][x][s][ ]
         *    [ ][s][s][s][s]
         *    [ ][ ]   [e][ ]
         * 
         * Where s stands for singleConnector
         * Where e stands for engine
         * Where x stands for CentralCabin which has all TileEdge.UNIVERSAL connectors
         * 
         * Illegal engine is at [4][4]
        */

        for (int i = 0; i < 5; i++) {
            for (int j = 1; j < 6; j++) {
                switch (i) {
                    case 0:
                        if (j == 3) {
                            ship.addComponentTile(i, j, singleConnector);
                        }
                        break;
                    case 1:
                        if (j > 1 && j < 4) {
                            ship.addComponentTile(i, j, singleConnector);
                        }
                        break;
                    case 2:
                        if ((j > 2 && j < 5) && j != 3) {
                            ship.addComponentTile(i, j, singleConnector);
                        }
                        break;
                    case 3:
                        if (j > 1 && j < 6) {
                            ship.addComponentTile(i, j, singleConnector);
                        }
                        break;
                    case 4:
                        if (j == 4) {
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
         *          [s]
         *       [s][s][ ]
         *    [ ][ ][x][s][ ]
         *    [ ][d][s][d][s]
         *    [ ][ ]   [s][ ]
         * 
         * Where s stands for singleConnector
         * Where d stands for doubleConnector
         * Where x stands for CentralCabin which has all TileEdge.UNIVERSAL connectors
         * 
         * Illegal connection is at [2][4] and [3][4]
        */

        for (int i = 0; i < 5; i++) {
            for (int j = 1; j < 6; j++) {
                switch (i) {
                    case 0:
                        if (j == 3) {
                            ship.addComponentTile(i, j, singleConnector);
                        }
                        break;
                    case 1:
                        if (j > 1 && j < 4) {
                            ship.addComponentTile(i, j, singleConnector);
                        }
                        break;
                    case 2:
                        if ((j > 2 && j < 5) && j != 3) {
                            ship.addComponentTile(i, j, singleConnector);
                        }
                        break;
                    case 3:
                        if (j > 1 && j < 6) {
                            if (j % 2 == 0) {
                                ship.addComponentTile(i, j, doubleConnector);
                            } else {
                                ship.addComponentTile(i, j, singleConnector);
                            }
                        }
                        break;
                    case 4:
                        if (j == 4) {
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
         *          [s]
         *       [s][s][ ]
         *    [ ][ ][x][s][ ]
         *    [ ][s][s][s][s]
         *    [ ][ ]   [s][ ]
         * 
         * Where s stands for singleConnector
         * Where x stands for CentralCabin which has all TileEdge.UNIVERSAL connectors
         * 
        */

        for (int i = 0; i < 5; i++) {
            for (int j = 1; j < 6; j++) {
                switch (i) {
                    case 0:
                        if (j == 3) {
                            ship.addComponentTile(i, j, singleConnector);
                        }
                        break;
                    case 1:
                        if (j > 1 && j < 4) {
                            ship.addComponentTile(i, j, singleConnector);
                        }
                        break;
                    case 2:
                        if ((j > 2 && j < 5) && j != 3) {
                            ship.addComponentTile(i, j, singleConnector);
                        }
                        break;
                    case 3:
                        if (j > 1 && j < 6) {
                            ship.addComponentTile(i, j, singleConnector);
                        }
                        break;
                    case 4:
                        if (j == 4) {
                            ship.addComponentTile(i, j, singleConnector);
                        }
                        break;
                }
            }
        }

        assertTrue(() -> ship.isShipLegal());
    }
}
