package adventureCards.meteorSwarm;

import it.polimi.it.galaxytrucker.model.componenttiles.CabinModule;
import it.polimi.it.galaxytrucker.model.componenttiles.LifeSupport;
import it.polimi.it.galaxytrucker.model.componenttiles.TileEdge;
import it.polimi.it.galaxytrucker.model.crewmates.Alien;
import it.polimi.it.galaxytrucker.model.crewmates.Human;
import it.polimi.it.galaxytrucker.model.managers.Player;
import it.polimi.it.galaxytrucker.model.managers.ShipManager;
import it.polimi.it.galaxytrucker.model.utility.AlienType;

import java.util.List;

public class ShipCreation {
    public static void createShip1(Player player) {
        ShipManager ship = player.getShipManager();
        /*
         *     4  5  6  7  8  9  10
         * 5        [ ]   [ ]
         * 6     [ ][ ][C][ ][ ]
         * 7  [ ][A][c][x][c][N][ ]
         * 8  [ ][l][ ][ ][ ][C][ ]
         * 9  [ ][ ][ ]   [ ][ ][ ]
         *
         * Where C stands for CabinModule with two crewmates
         * Where c stands for CabinModule with one crewmate
         * Where N stands for CabinModule without crewmates
         * Where A stands for CabinModule with one alien
         * Where l stands for LifeSupport
         * Where x stands for CentralCabin with two crewmates
         *
         * Crewmates: 9
         */
        ship.addComponentTile(6, 7, new CabinModule(List.of(TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE)));

        ship.addComponentTile(7, 5, new CabinModule(List.of(TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE)));
        ship.addComponentTile(7, 6, new CabinModule(List.of(TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE)));
        ship.addComponentTile(7, 8, new CabinModule(List.of(TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE)));
        ship.addComponentTile(7, 9, new CabinModule(List.of(TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE)));

        ship.addComponentTile(8, 5, new LifeSupport(AlienType.PURPLEALIEN, List.of(TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE)));
        ship.addComponentTile(8, 9, new CabinModule(List.of(TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE)));

        ship.addCrewmate(6, 7, new Human());
        ship.addCrewmate(6, 7, new Human());

        ship.addCrewmate(7, 5, new Alien(AlienType.PURPLEALIEN));
        ship.addCrewmate(7, 6, new Human());
        ship.addCrewmate(7, 8, new Human());

        ship.addCrewmate(8, 9, new Human());
        ship.addCrewmate(8, 9, new Human());
    }
    public static void createShip2(Player player) {
        ShipManager ship = player.getShipManager();
        /*
         *     4  5  6  7  8  9  10
         * 5        [c]   [ ]
         * 6     [c][ ][ ][ ][ ]
         * 7  [ ][c][ ][x][N][ ][ ]
         * 8  [ ][ ][C][l][A][ ][ ]
         * 9  [ ][c][ ]   [ ][ ][ ]
         *
         * Where C stands for CabinModule with two crewmates
         * Where c stands for CabinModule with one crewmate
         * Where A stands for CabinModule with one alien
         * Where N stands for CabinModule without crewmates
         * Where x stands for CentralCabin with two crewmates
         *
         * Crewmates: 9
         */
        ship.addComponentTile(5, 6, new CabinModule(List.of(TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE)));

        ship.addComponentTile(6, 5, new CabinModule(List.of(TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE)));

        ship.addComponentTile(7, 5, new CabinModule(List.of(TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE)));
        ship.addComponentTile(7, 8, new CabinModule(List.of(TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE)));

        ship.addComponentTile(8, 6, new CabinModule(List.of(TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE)));
        ship.addComponentTile(8, 7, new LifeSupport(AlienType.PURPLEALIEN, List.of(TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE)));
        ship.addComponentTile(8, 8, new CabinModule(List.of(TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE)));

        ship.addComponentTile(9, 5, new CabinModule(List.of(TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE)));

        ship.addCrewmate(5, 6, new Human());

        ship.addCrewmate(6, 5, new Human());

        ship.addCrewmate(7, 5, new Human());

        ship.addCrewmate(8, 6, new Human());
        ship.addCrewmate(8, 6, new Human());
        ship.addCrewmate(8, 8, new Alien(AlienType.PURPLEALIEN));

        ship.addCrewmate(9, 5, new Human());
    }
    public static void createShip3(Player player) {
        ShipManager ship = player.getShipManager();
        /*
         *     4  5  6  7  8  9  10
         * 5        [ ]   [ ]
         * 6     [ ][C][ ][ ][ ]
         * 7  [c][ ][N][x][c][ ][ ]
         * 8  [ ][c][ ][ ][ ][c][ ]
         * 9  [c][ ][ ]   [ ][ ][c]
         *
         * Where C stands for CabinModule with two crewmates
         * Where c stands for CabinModule with one crewmate
         * Where N stands for CabinModule without crewmates
         * Where x stands for CentralCabin with two crewmates
         *
         * Crewmates: 10
         */
        ship.addComponentTile(6, 6, new CabinModule(List.of(TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE)));

        ship.addComponentTile(7, 4, new CabinModule(List.of(TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE)));
        ship.addComponentTile(7, 6, new CabinModule(List.of(TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE)));
        ship.addComponentTile(7, 8, new CabinModule(List.of(TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE)));

        ship.addComponentTile(8, 5, new CabinModule(List.of(TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE)));
        ship.addComponentTile(8, 9, new CabinModule(List.of(TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE)));

        ship.addComponentTile(9, 4, new CabinModule(List.of(TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE)));
        ship.addComponentTile(9, 10, new CabinModule(List.of(TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE)));

        ship.addCrewmate(6, 6, new Human());
        ship.addCrewmate(6, 6, new Human());

        ship.addCrewmate(7, 4, new Human());
        ship.addCrewmate(7, 8, new Human());

        ship.addCrewmate(8, 5, new Human());
        ship.addCrewmate(8, 9, new Human());

        ship.addCrewmate(9, 4, new Human());
        ship.addCrewmate(9, 10, new Human());
    }
    public static void createShip4(Player player) {
        ShipManager ship = player.getShipManager();
        /*
         *     4  5  6  7  8  9  10
         * 5        [ ]   [ ]
         * 6     [ ][ ][ ][ ][ ]
         * 7  [ ][ ][ ][x][ ][ ][ ]
         * 8  [ ][ ][ ][ ][ ][ ][ ]
         * 9  [ ][ ][ ]   [ ][ ][ ]
         *
         * Where x stands for CentralCabin with one crewmate
         *
         * Crewmates: 1
         */
        ship.removeCrewmate(7, 7);
    }
}
