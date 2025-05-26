package it.polimi.it.galaxytrucker.model.gamestates;

import it.polimi.it.galaxytrucker.model.componenttiles.*;
import it.polimi.it.galaxytrucker.model.crewmates.Alien;
import it.polimi.it.galaxytrucker.model.crewmates.Human;
import it.polimi.it.galaxytrucker.model.managers.Player;
import it.polimi.it.galaxytrucker.model.managers.ShipManager;
import it.polimi.it.galaxytrucker.model.utility.AlienType;
import it.polimi.it.galaxytrucker.model.utility.Direction;

import java.util.List;

public class ShipCreation {
    public static void createLegalShip1(Player player) {
        ShipManager ship = player.getShipManager();
        /*
         *     4  5  6  7  8  9  10
         * 5        [ ]   [ ]
         * 6     [ ][c][C][c][ ]
         * 7  [h][m][b][x][b][s][ ]
         * 8  [l][m][e][E][e][ ][ ]
         * 9  [ ][ ][ ]   [ ][ ][ ]
         *
         * Where c stands for SingleCannon
         * Where C stands for DoubleCannon
         * Where e stands for SingleEngine
         * Where E stands for DoubleEngine
         * Where s stands for Shield (with rier coverage)
         * Where b stands for BatteryComponent
         * Where m stands for CabinModule
         * Where l stands for LifeSupport
         * Where h stands for CargoHold
         * Where x stands for CentralCabin which has all TileEdge.UNIVERSAL connectors
         *
         * Firepower:
         *  -base: 4
         *  -additional: 6
         *
         * Engine power:
         *  -base: 2
         *  -additional: 4
         *
         * Crewmates: 3, null
         */
        ship.addComponentTile(6, 6, new SingleCannon(List.of(TileEdge.INCOMPATIBLE,TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE), null));
        ship.addComponentTile(6, 7, new DoubleCannon(List.of(TileEdge.INCOMPATIBLE,TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE), null));
        ship.addComponentTile(6, 8, new SingleCannon(List.of(TileEdge.INCOMPATIBLE,TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE), null));

        ship.addComponentTile(7, 4, new CargoHold(3, List.of(TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE), null));
        ship.addComponentTile(7, 5, new CabinModule(List.of(TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE), null));
        ship.addComponentTile(7, 6, new BatteryComponent(2, List.of(TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE), null));
        ship.addComponentTile(7, 8, new BatteryComponent(2, List.of(TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE), null));
        ship.addComponentTile(7, 9, new Shield(Direction.DOWN, List.of(TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE), null));

        ship.addComponentTile(8, 4, new LifeSupport(AlienType.PURPLEALIEN, List.of(TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE), null));
        ship.addComponentTile(8, 5, new CabinModule(List.of(TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE), null));
        ship.addComponentTile(8, 6, new SingleEngine(List.of(TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.INCOMPATIBLE,TileEdge.SINGLE), null));
        ship.addComponentTile(8, 7, new DoubleEngine(List.of(TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.INCOMPATIBLE,TileEdge.SINGLE), null));
        ship.addComponentTile(8, 8, new SingleEngine(List.of(TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.INCOMPATIBLE,TileEdge.SINGLE), null));

        ship.addCrewmate(7, 5, new Human());
        ship.addCrewmate(7, 5, new Human());

        ship.addCrewmate(8, 5, new Alien(AlienType.PURPLEALIEN));
    }
    public static void createLegalShip2(Player player) {
        ShipManager ship = player.getShipManager();
        /*
         *     4  5  6  7  8  9  10
         * 5        [ ]   [ ]
         * 6     [ ][c][C][c][h]
         * 7  [ ][m][b][x][b][s][ ]
         * 8  [l][m][e][E][e][E][ ]
         * 9  [ ][ ][ ]   [ ][ ][ ]
         *
         * Where c stands for SingleCannon
         * Where C stands for DoubleCannon
         * Where e stands for SingleEngine
         * Where E stands for DoubleEngine
         * Where s stands for Shield (with rier coverage)
         * Where b stands for BatteryComponent
         * Where m stands for CabinModule
         * Where l stands for LifeSupport
         * Where h stands for CargoHold
         * Where x stands for CentralCabin which has all TileEdge.UNIVERSAL connectors
         *
         * Firepower:
         *  -base: 2
         *  -additional: 4
         *
         * Engine power:
         *  -base: 4
         *  -additional: 8
         *
         * Crewmates: 3
         */
        ship.addComponentTile(6, 6, new SingleCannon(List.of(TileEdge.INCOMPATIBLE,TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE), null));
        ship.addComponentTile(6, 7, new DoubleCannon(List.of(TileEdge.INCOMPATIBLE,TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE), null));
        ship.addComponentTile(6, 8, new SingleCannon(List.of(TileEdge.INCOMPATIBLE,TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE), null));
        ship.addComponentTile(6, 9, new CargoHold(3, List.of(TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE), null));

        ship.addComponentTile(7, 5, new CabinModule(List.of(TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE), null));
        ship.addComponentTile(7, 6, new BatteryComponent(2, List.of(TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE), null));
        ship.addComponentTile(7, 8, new BatteryComponent(2, List.of(TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE), null));
        ship.addComponentTile(7, 9, new Shield(Direction.DOWN, List.of(TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE), null));

        ship.addComponentTile(8, 4, new LifeSupport(AlienType.BROWNALIEN, List.of(TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE), null));
        ship.addComponentTile(8, 5, new CabinModule(List.of(TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE), null));
        ship.addComponentTile(8, 6, new SingleEngine(List.of(TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.INCOMPATIBLE,TileEdge.SINGLE), null));
        ship.addComponentTile(8, 7, new DoubleEngine(List.of(TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.INCOMPATIBLE,TileEdge.SINGLE), null));
        ship.addComponentTile(8, 8, new SingleEngine(List.of(TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.INCOMPATIBLE,TileEdge.SINGLE), null));
        ship.addComponentTile(8, 9, new DoubleEngine(List.of(TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.INCOMPATIBLE,TileEdge.SINGLE), null));

        ship.addCrewmate(7, 5, new Human());
        ship.addCrewmate(7, 5, new Human());

        ship.addCrewmate(8, 5, new Alien(AlienType.BROWNALIEN));
    }
    public static void createLegalShip3(Player player) {
        ShipManager ship = player.getShipManager();
        /*
         *     4  5  6  7  8  9  10
         * 5        [ ]   [ ]
         * 6     [ ][c][C][c][h]
         * 7  [ ][ ][b][x][b][s][ ]
         * 8  [l][m][e][E][e][ ][ ]
         * 9  [ ][ ][ ]   [ ][ ][ ]
         *
         * Where c stands for SingleCannon
         * Where C stands for DoubleCannon
         * Where e stands for SingleEngine
         * Where E stands for DoubleEngine
         * Where s stands for Shield (with rier coverage)
         * Where b stands for BatteryComponent
         * Where m stands for CabinModule
         * Where l stands for LifeSupport
         * Where h stands for CargoHold
         * Where x stands for CentralCabin which has all TileEdge.UNIVERSAL connectors
         *
         * Firepower:
         *  -base: 4
         *  -additional: 6
         *
         * Engine power:
         *  -base: 2
         *  -additional: 4
         *
         * Crewmates: 1
         */
        ship.addComponentTile(6, 6, new SingleCannon(List.of(TileEdge.INCOMPATIBLE,TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE), null));
        ship.addComponentTile(6, 7, new DoubleCannon(List.of(TileEdge.INCOMPATIBLE,TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE), null));
        ship.addComponentTile(6, 8, new SingleCannon(List.of(TileEdge.INCOMPATIBLE,TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE), null));
        ship.addComponentTile(6, 9, new CargoHold(3, List.of(TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE), null));

        ship.addComponentTile(7, 6, new BatteryComponent(2, List.of(TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE), null));
        ship.addComponentTile(7, 8, new BatteryComponent(2, List.of(TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE), null));
        ship.addComponentTile(7, 9, new Shield(Direction.DOWN, List.of(TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE), null));

        ship.addComponentTile(8, 4, new LifeSupport(AlienType.PURPLEALIEN, List.of(TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE), null));
        ship.addComponentTile(8, 5, new CabinModule(List.of(TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE), null));
        ship.addComponentTile(8, 6, new SingleEngine(List.of(TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.INCOMPATIBLE,TileEdge.SINGLE), null));
        ship.addComponentTile(8, 8, new SingleEngine(List.of(TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.INCOMPATIBLE,TileEdge.SINGLE), null));
        ship.addComponentTile(8, 7, new DoubleEngine(List.of(TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.INCOMPATIBLE,TileEdge.SINGLE), null));

        ship.addCrewmate(8, 5, new Alien(AlienType.PURPLEALIEN));
    }

    public static void createLegalShip4(Player player) {
        ShipManager ship = player.getShipManager();
        /*
         *     4  5  6  7  8  9  10
         * 5        [ ]   [ ]
         * 6     [ ][ ][ ][ ][ ]
         * 7  [m][m][m][x][m][m][m]
         * 8  [ ][ ][ ][ ][ ][ ][ ]
         * 9  [ ][ ][ ]   [ ][ ][ ]
         *
         * Where m stands for CabinModule
         * Where x stands for CentralCabin which has all TileEdge.UNIVERSAL connectors
         *
         * Firepower:
         *  -base: 0
         *  -additional: 0
         *
         * Engine power:
         *  -base: 0
         *  -additional: 0
         *
         * Crewmates: 14
         */
        ship.addComponentTile(7, 4, new CabinModule(List.of(TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE)));
        ship.addComponentTile(7, 5, new CabinModule(List.of(TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE)));
        ship.addComponentTile(7, 6, new CabinModule(List.of(TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE)));
        ship.addComponentTile(7, 8, new CabinModule(List.of(TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE)));
        ship.addComponentTile(7, 9, new CabinModule(List.of(TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE)));
        ship.addComponentTile(7, 10, new CabinModule(List.of(TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE)));

        ship.addCrewmate(7, 4, new Human());
        ship.addCrewmate(7, 5, new Human());
        ship.addCrewmate(7, 6, new Human());
        ship.addCrewmate(7, 8, new Human());
        ship.addCrewmate(7, 9, new Human());
        ship.addCrewmate(7, 10, new Human());
        ship.addCrewmate(7, 4, new Human());
        ship.addCrewmate(7, 5, new Human());
        ship.addCrewmate(7, 6, new Human());
        ship.addCrewmate(7, 8, new Human());
        ship.addCrewmate(7, 9, new Human());
        ship.addCrewmate(7, 10, new Human());
    }

    /**
     * Creates a ship that has a disconnected branch.
     * <p>
     * The ship has two branched, one main branch that includes the central cabin,
     * and one smaller branch bade of the components at positions [8, 10], [9, 9], [9, 10].
     *
     * @param player The player for which the ship will be built
     */
    public static void createIllegalShip1(Player player) {
        ShipManager ship = player.getShipManager();
        /*
         *     4  5  6  7  8  9  10
         * 5        [ ]   [ ]
         * 6     [ ][c][C][c][ ]
         * 7  [ ][ ][b][x][b][s][ ]
         * 8  [l][m][e][E][e][ ][m]
         * 9  [ ][ ][ ]   [ ][m][m]
         *
         * Where c stands for SingleCannon
         * Where C stands for DoubleCannon
         * Where e stands for SingleEngine
         * Where E stands for DoubleEngine
         * Where s stands for Shield (with rear coverage)
         * Where b stands for BatteryComponent
         * Where m stands for CabinModule
         * Where l stands for LifeSupport
         * Where x stands for CentralCabin which has all TileEdge.UNIVERSAL connectors
         *
         * Firepower:
         *  -base: 4
         *  -additional: 6
         *
         * Engine power:
         *  -base: 2
         *  -additional: 4
         *
         * Crewmates: 1
         *
         * Illegal part: ship is divided in two sections {[8, 10], [9, 9], [9, 10]}
         */
        ship.addComponentTile(6, 6, new SingleCannon(List.of(TileEdge.INCOMPATIBLE,TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE), null));
        ship.addComponentTile(6, 7, new DoubleCannon(List.of(TileEdge.INCOMPATIBLE,TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE), null));
        ship.addComponentTile(6, 8, new SingleCannon(List.of(TileEdge.INCOMPATIBLE,TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE), null));

        ship.addComponentTile(7, 6, new BatteryComponent(2, List.of(TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE), null));
        ship.addComponentTile(7, 8, new BatteryComponent(2, List.of(TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE), null));
        ship.addComponentTile(7, 9, new Shield(Direction.DOWN, List.of(TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE), null));

        ship.addComponentTile(8, 4, new LifeSupport(AlienType.PURPLEALIEN, List.of(TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE), null));
        ship.addComponentTile(8, 5, new CabinModule(List.of(TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE), null));
        ship.addComponentTile(8, 6, new SingleEngine(List.of(TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.INCOMPATIBLE,TileEdge.SINGLE), null));
        ship.addComponentTile(8, 8, new SingleEngine(List.of(TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.INCOMPATIBLE,TileEdge.SINGLE), null));
        ship.addComponentTile(8, 7, new DoubleEngine(List.of(TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.INCOMPATIBLE,TileEdge.SINGLE), null));

        ship.addComponentTile(8, 10, new CabinModule(List.of(TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE), null));
        ship.addComponentTile(9, 9, new CabinModule(List.of(TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE), null));
        ship.addComponentTile(9, 10, new CabinModule(List.of(TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE), null));

        ship.addCrewmate(8, 5, new Alien(AlienType.PURPLEALIEN));
    }

    /**
     * Creates a ship that has some pieces with some incorrect connectors.
     * <p>
     * The ship has two {@link ComponentTile}s at positions {@code [6, 7], [8, 4]} that have connectors
     * incompatible with neighbouring components.
     *
     * @param player The player for which the ship will be built
     */
    public static void createIllegalShip2(Player player) {
        ShipManager ship = player.getShipManager();
        /*
         *     4  5  6  7  8  9  10
         * 5        [ ]   [ ]
         * 6     [ ][c][C][c][ ]
         * 7  [ ][ ][b][x][b][s][ ]
         * 8  [l][m][e][E][e][ ][ ]
         * 9  [ ][ ][ ]   [ ][ ][ ]
         *
         * Where c stands for SingleCannon
         * Where C stands for DoubleCannon
         * Where e stands for SingleEngine
         * Where E stands for DoubleEngine
         * Where s stands for Shield (with rear coverage)
         * Where b stands for BatteryComponent
         * Where m stands for CabinModule
         * Where l stands for LifeSupport
         * Where x stands for CentralCabin which has all TileEdge.UNIVERSAL connectors
         *
         * Firepower:
         *  -base: 4
         *  -additional: 6
         *
         * Engine power:
         *  -base: 2
         *  -additional: 4
         *
         * Crewmates: 1
         *
         * Illegal part: wrong connectors in {[6, 7], [8, 4]}
         */
        ship.addComponentTile(6, 6, new SingleCannon(List.of(TileEdge.INCOMPATIBLE,TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE), null));
        // TWO MIMATCHED EDGES
        ship.addComponentTile(6, 7, new DoubleCannon(List.of(TileEdge.INCOMPATIBLE,TileEdge.DOUBLE,TileEdge.SMOOTH,TileEdge.SINGLE), null));
        ship.addComponentTile(6, 8, new SingleCannon(List.of(TileEdge.INCOMPATIBLE,TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE), null));

        ship.addComponentTile(7, 6, new BatteryComponent(2, List.of(TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE), null));
        ship.addComponentTile(7, 8, new BatteryComponent(2, List.of(TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE), null));
        ship.addComponentTile(7, 9, new Shield(Direction.DOWN, List.of(TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE), null));

        // ALL WRONG EDGED
        ship.addComponentTile(8, 4, new LifeSupport(AlienType.PURPLEALIEN, List.of(TileEdge.SMOOTH,TileEdge.SMOOTH,TileEdge.SMOOTH,TileEdge.SMOOTH), null));
        ship.addComponentTile(8, 5, new CabinModule(List.of(TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE), null));
        ship.addComponentTile(8, 6, new SingleEngine(List.of(TileEdge.SINGLE,TileEdge.UNIVERSAL,TileEdge.INCOMPATIBLE,TileEdge.SINGLE), null));
        ship.addComponentTile(8, 8, new SingleEngine(List.of(TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.INCOMPATIBLE,TileEdge.SINGLE), null));
        ship.addComponentTile(8, 7, new DoubleEngine(List.of(TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.INCOMPATIBLE,TileEdge.SINGLE), null));

        ship.addCrewmate(8, 5, new Alien(AlienType.PURPLEALIEN));
    }

    /**
     * Creates a ship that has some incorrectly rotated engines.
     * <p>
     * The ship has two {@link SingleEngine}s at positions {@code [8, 7], [8, 8]} with rotation 2. The only allowed
     * rotation for engines is {@code 0}, so they are illegal.
     *
     * @param player The player for which the ship will be built
     */
    public static void createIllegalShip3(Player player) {
        ShipManager ship = player.getShipManager();
        /*
         *     4  5  6  7  8  9  10
         * 5        [ ]   [ ]
         * 6     [ ][c][C][c][ ]
         * 7  [ ][ ][b][x][b][s][ ]
         * 8  [l][m][e][E][e][ ][ ]
         * 9  [ ][ ][ ]   [ ][ ][ ]
         *
         * Where c stands for SingleCannon
         * Where C stands for DoubleCannon
         * Where e stands for SingleEngine
         * Where E stands for DoubleEngine
         * Where s stands for Shield (with rear coverage)
         * Where b stands for BatteryComponent
         * Where m stands for CabinModule
         * Where l stands for LifeSupport
         * Where x stands for CentralCabin which has all TileEdge.UNIVERSAL connectors
         *
         * Firepower:
         *  -base: 4
         *  -additional: 6
         *
         * Engine power:
         *  -base: 2
         *  -additional: 4
         *
         * Crewmates: 1
         *
         * Illegal part: engines pointing forward {[8, 7], [8, 8]}
         */
        ship.addComponentTile(6, 6, new SingleCannon(List.of(TileEdge.INCOMPATIBLE,TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE), null));
        ship.addComponentTile(6, 7, new DoubleCannon(List.of(TileEdge.INCOMPATIBLE,TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE), null));
        ship.addComponentTile(6, 8, new SingleCannon(List.of(TileEdge.INCOMPATIBLE,TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE), null));

        ship.addComponentTile(7, 6, new BatteryComponent(2, List.of(TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE), null));
        ship.addComponentTile(7, 8, new BatteryComponent(2, List.of(TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE), null));
        ship.addComponentTile(7, 9, new Shield(Direction.DOWN, List.of(TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE), null));

        ship.addComponentTile(8, 4, new LifeSupport(AlienType.PURPLEALIEN, List.of(TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE), null));
        ship.addComponentTile(8, 5, new CabinModule(List.of(TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE), null));
        ship.addComponentTile(8, 6, new SingleEngine(List.of(TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.INCOMPATIBLE,TileEdge.SINGLE), null));
        ship.addComponentTile(8, 8, new SingleEngine(List.of(TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.INCOMPATIBLE,TileEdge.SINGLE), null));
        ship.addComponentTile(8, 7, new DoubleEngine(List.of(TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.INCOMPATIBLE,TileEdge.SINGLE), null));

        ship.getComponent(8, 7).ifPresentOrElse(c -> {
            c.rotate();
            c.rotate();
        }, () -> {});
        ship.getComponent(8, 8).ifPresentOrElse(c -> {
            c.rotate();
            c.rotate();
        }, () -> {});

        ship.addCrewmate(8, 5, new Alien(AlienType.PURPLEALIEN));
    }

    public static void createIllegalShip4(Player player) {
        ShipManager ship = player.getShipManager();
        /*
         *     4  5  6  7  8  9  10
         * 5        [m]   [m]
         * 6     [m][c][ ][c][ ]
         * 7  [ ][C][b][x][b][s][ ]
         * 8  [l][m][e][E][e][ ][ ]
         * 9  [ ][ ][ ]   [ ][ ][ ]
         *
         * Where c stands for SingleCannon
         * Where C stands for DoubleCannon
         * Where e stands for SingleEngine
         * Where E stands for DoubleEngine
         * Where s stands for Shield (with rear coverage)
         * Where b stands for BatteryComponent
         * Where m stands for CabinModule
         * Where l stands for LifeSupport
         * Where x stands for CentralCabin which has all TileEdge.UNIVERSAL connectors
         *
         * Firepower:
         *  -base: 4
         *  -additional: 6
         *
         * Engine power:
         *  -base: 2
         *  -additional: 4
         *
         * Crewmates: 1
         *
         * Illegal part: cannons covered by other components {[6, 6], [6, 8], [7, 5]}
         */
        ship.addComponentTile(5, 6, new CabinModule(List.of(TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE), null));
        ship.addComponentTile(5, 8, new CabinModule(List.of(TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE), null));

        ship.addComponentTile(6, 5, new CabinModule(List.of(TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE), null));
        ship.addComponentTile(6, 6, new SingleCannon(List.of(TileEdge.INCOMPATIBLE,TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE), null));
        ship.addComponentTile(6, 8, new SingleCannon(List.of(TileEdge.INCOMPATIBLE,TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE), null));

        ship.addComponentTile(7, 5, new DoubleCannon(List.of(TileEdge.INCOMPATIBLE,TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE), null));
        ship.addComponentTile(7, 6, new BatteryComponent(2, List.of(TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE), null));
        ship.addComponentTile(7, 8, new BatteryComponent(2, List.of(TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE), null));
        ship.addComponentTile(7, 9, new Shield(Direction.DOWN, List.of(TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE), null));

        ship.addComponentTile(8, 4, new LifeSupport(AlienType.PURPLEALIEN, List.of(TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE), null));
        ship.addComponentTile(8, 5, new CabinModule(List.of(TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE), null));
        ship.addComponentTile(8, 6, new SingleEngine(List.of(TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE), null));
        ship.addComponentTile(8, 8, new SingleEngine(List.of(TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.INCOMPATIBLE,TileEdge.SINGLE), null));
        ship.addComponentTile(8, 7, new DoubleEngine(List.of(TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.INCOMPATIBLE,TileEdge.SINGLE), null));

        ship.addCrewmate(8, 5, new Alien(AlienType.PURPLEALIEN));
    }
}
