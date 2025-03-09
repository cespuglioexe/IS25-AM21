package managers;

import org.junit.jupiter.api.Test;

import it.polimi.it.galaxytrucker.componenttiles.ComponentTile;
import it.polimi.it.galaxytrucker.componenttiles.SingleCannon;
import it.polimi.it.galaxytrucker.componenttiles.StructuralModule;
import it.polimi.it.galaxytrucker.componenttiles.TileEdge;
import it.polimi.it.galaxytrucker.managers.ShipManager;

import static org.junit.jupiter.api.Assertions.*;

public class ShipmanagerTest {

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
         *    [ ][d][s][d][s]
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
