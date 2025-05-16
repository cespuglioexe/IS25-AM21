package it.polimi.it.galaxytrucker.adventurecards.combatzone;

import java.util.List;

import it.polimi.it.galaxytrucker.model.componenttiles.BatteryComponent;
import it.polimi.it.galaxytrucker.model.componenttiles.CabinModule;
import it.polimi.it.galaxytrucker.model.componenttiles.DoubleCannon;
import it.polimi.it.galaxytrucker.model.componenttiles.DoubleEngine;
import it.polimi.it.galaxytrucker.model.componenttiles.LifeSupport;
import it.polimi.it.galaxytrucker.model.componenttiles.Shield;
import it.polimi.it.galaxytrucker.model.componenttiles.SingleCannon;
import it.polimi.it.galaxytrucker.model.componenttiles.SingleEngine;
import it.polimi.it.galaxytrucker.model.componenttiles.TileEdge;
import it.polimi.it.galaxytrucker.model.crewmates.Alien;
import it.polimi.it.galaxytrucker.model.crewmates.Human;
import it.polimi.it.galaxytrucker.model.managers.Player;
import it.polimi.it.galaxytrucker.model.managers.ShipManager;
import it.polimi.it.galaxytrucker.model.utility.AlienType;
import it.polimi.it.galaxytrucker.model.utility.Direction;

public class ShipCreation {
    public static void createShip1(Player player) {
        ShipManager ship = player.getShipManager();
        /*
         *     4  5  6  7  8  9  10 
         * 5        [ ]   [ ]      
         * 6     [ ][c][C][c][ ]   
         * 7  [ ][m][b][x][b][s][ ]
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
         * Crewmates: 3
         */
        ship.addComponentTile(6, 6, new SingleCannon(List.of(TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE)));
        ship.addComponentTile(6, 7, new DoubleCannon(List.of(TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE)));
        ship.addComponentTile(6, 8, new SingleCannon(List.of(TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE)));

        ship.addComponentTile(7, 5, new CabinModule(List.of(TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE)));
        ship.addComponentTile(7, 6, new BatteryComponent(2, List.of(TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE)));
        ship.addComponentTile(7, 8, new BatteryComponent(2, List.of(TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE)));
        ship.addComponentTile(7, 9, new Shield(Direction.DOWN, List.of(TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE)));

        ship.addComponentTile(8, 4, new LifeSupport(AlienType.PURPLEALIEN, List.of(TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE)));
        ship.addComponentTile(8, 5, new CabinModule(List.of(TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE)));
        ship.addComponentTile(8, 6, new SingleEngine(List.of(TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE)));
        ship.addComponentTile(8, 7, new DoubleEngine(List.of(TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE)));
        ship.addComponentTile(8, 8, new SingleEngine(List.of(TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE)));

        ship.addCrewmate(7, 5, new Human());
        ship.addCrewmate(7, 5, new Human());

        ship.addCrewmate(8, 5, new Alien(AlienType.PURPLEALIEN));
    }
    public static void createShip2(Player player) {
        ShipManager ship = player.getShipManager();
        /*
         *     4  5  6  7  8  9  10 
         * 5        [ ]   [ ]      
         * 6     [ ][c][C][c][ ]   
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
        ship.addComponentTile(6, 6, new SingleCannon(List.of(TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE)));
        ship.addComponentTile(6, 7, new DoubleCannon(List.of(TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE)));
        ship.addComponentTile(6, 8, new SingleCannon(List.of(TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE)));

        ship.addComponentTile(7, 5, new CabinModule(List.of(TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE)));
        ship.addComponentTile(7, 6, new BatteryComponent(2, List.of(TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE)));
        ship.addComponentTile(7, 8, new BatteryComponent(2, List.of(TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE)));
        ship.addComponentTile(7, 9, new Shield(Direction.DOWN, List.of(TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE)));

        ship.addComponentTile(8, 4, new LifeSupport(AlienType.BROWNALIEN, List.of(TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE)));
        ship.addComponentTile(8, 5, new CabinModule(List.of(TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE)));
        ship.addComponentTile(8, 6, new SingleEngine(List.of(TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE)));
        ship.addComponentTile(8, 7, new DoubleEngine(List.of(TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE)));
        ship.addComponentTile(8, 8, new SingleEngine(List.of(TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE)));
        ship.addComponentTile(8, 9, new DoubleEngine(List.of(TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE)));

        ship.addCrewmate(7, 5, new Human());
        ship.addCrewmate(7, 5, new Human());

        ship.addCrewmate(8, 5, new Alien(AlienType.BROWNALIEN));
    }
    public static void createShip3(Player player) {
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
         * Where s stands for Shield (with rier coverage)
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
         */
        ship.addComponentTile(6, 6, new SingleCannon(List.of(TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE)));
        ship.addComponentTile(6, 7, new DoubleCannon(List.of(TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE)));
        ship.addComponentTile(6, 8, new SingleCannon(List.of(TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE)));

        ship.addComponentTile(7, 6, new BatteryComponent(2, List.of(TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE)));
        ship.addComponentTile(7, 8, new BatteryComponent(2, List.of(TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE)));
        ship.addComponentTile(7, 9, new Shield(Direction.DOWN, List.of(TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE)));

        ship.addComponentTile(8, 4, new LifeSupport(AlienType.PURPLEALIEN, List.of(TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE)));
        ship.addComponentTile(8, 5, new CabinModule(List.of(TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE)));
        ship.addComponentTile(8, 6, new SingleEngine(List.of(TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE)));
        ship.addComponentTile(8, 8, new SingleEngine(List.of(TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE)));
        ship.addComponentTile(8, 7, new DoubleEngine(List.of(TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE)));

        ship.addCrewmate(8, 5, new Alien(AlienType.PURPLEALIEN));
    }
}
