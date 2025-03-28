package it.polimi.it.galaxytrucker.aventurecard;

import it.polimi.it.galaxytrucker.componenttiles.*;
import it.polimi.it.galaxytrucker.managers.Player;
import it.polimi.it.galaxytrucker.managers.ShipManager;
import it.polimi.it.galaxytrucker.utility.Cargo;
import it.polimi.it.galaxytrucker.utility.Color;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class CargoManagerTest {

    @Test
    void manageCargoDischarge() {

        Player player1=new Player(new UUID(0,1), "Margarozzo1",0, Color.RED, new ShipManager(1));


        player1.getShipManager().addComponentTile(6,7, new SpecialCargoHold(2,List.of(TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE)));
        player1.getShipManager().addComponentTile(6,8, new CargoHold(2,List.of(TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE)));

        player1.getShipManager().addComponentTile(6,6, new BatteryComponent(2,List.of(TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE)));



        player1.getShipManager().addCargo(6,7,new Cargo(Color.GREEN));
        player1.getShipManager().addCargo(6,7,new Cargo(Color.RED));
        player1.getShipManager().addCargo(6,8,new Cargo(Color.BLUE));
        player1.getShipManager().addCargo(6,8,new Cargo(Color.GREEN));


        CargoManager cargoManager=new CargoManager();
        cargoManager.manageCargoDischarge(1,player1);

        // Cargo Only

        assertEquals(player1.getShipManager().getCargoPositon().get(Color.RED).isEmpty(),true);

        cargoManager.manageCargoDischarge(2,player1);

        assertEquals(player1.getShipManager().getCargoPositon().get(Color.GREEN).isEmpty(),true);

        // Cargo and Battery

        cargoManager.manageCargoDischarge(2,player1);

        HashMap<Color, Set<List<Integer>>> empty = player1.getShipManager().getCargoPositon();
        assertEquals(empty.get(Color.RED).size() == 0 && empty.get(Color.GREEN).size() == 0 && empty.get(Color.BLUE).size() == 0 && empty.get(Color.YELLOW).size() == 0,true);

        BatteryComponent battery = (BatteryComponent) player1.getShipManager().getComponent(6,6).orElse(null);
        assertEquals(battery.getBatteryCapacity(),1);

        // Only Battery

        cargoManager.manageCargoDischarge(1,player1);

        empty = player1.getShipManager().getCargoPositon();
        assertEquals(empty.get(Color.RED).size() == 0 && empty.get(Color.GREEN).size() == 0 && empty.get(Color.BLUE).size() == 0 && empty.get(Color.YELLOW).size() == 0,true);

        battery = (BatteryComponent) player1.getShipManager().getComponent(6,6).orElse(null);
        assertEquals(battery.getBatteryCapacity(),0);

        // Cargo and battery empty

        cargoManager.manageCargoDischarge(1,player1);

        empty = player1.getShipManager().getCargoPositon();
        assertEquals(empty.get(Color.RED).size() == 0 && empty.get(Color.GREEN).size() == 0 && empty.get(Color.BLUE).size() == 0 && empty.get(Color.YELLOW).size() == 0,true);

        battery = (BatteryComponent) player1.getShipManager().getComponent(6,6).orElse(null);
        assertEquals(battery.getBatteryCapacity(),0);

        // numCargo too high

        player1.getShipManager().addComponentTile(7,6, new BatteryComponent(2,List.of(TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE)));
        player1.getShipManager().addCargo(6,7,new Cargo(Color.GREEN));
        player1.getShipManager().addCargo(6,7,new Cargo(Color.RED));

        cargoManager.manageCargoDischarge(5,player1);

        empty = player1.getShipManager().getCargoPositon();
        assertEquals(empty.get(Color.RED).size() == 0 && empty.get(Color.GREEN).size() == 0 && empty.get(Color.BLUE).size() == 0 && empty.get(Color.YELLOW).size() == 0,true);

        battery = (BatteryComponent) player1.getShipManager().getComponent(6,6).orElse(null);
        assertEquals(battery.getBatteryCapacity(),0);

    }

    @Test
    void manageCargoAddition() {


    }
}