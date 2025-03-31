package it.polimi.it.galaxytrucker.aventurecard;

import it.polimi.it.galaxytrucker.componenttiles.*;
import it.polimi.it.galaxytrucker.managers.Player;
import it.polimi.it.galaxytrucker.managers.ShipManager;
import it.polimi.it.galaxytrucker.utility.Cargo;
import it.polimi.it.galaxytrucker.utility.Color;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class CargoManagerTest {

    CargoManager cargoManager;
    Player player;
    ShipManager shipManager;

    @BeforeEach
    void initializeNewTestPlayer() {
        player = new Player(new UUID(0,1), "Alberto",0, Color.RED, new ShipManager(1));

        shipManager = player.getShipManager();

        shipManager.addComponentTile(6,7, new SpecialCargoHold(2,List.of(TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE)));
        shipManager.addComponentTile(6,8, new CargoHold(2,List.of(TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE)));
        shipManager.addComponentTile(6,6, new BatteryComponent(2,List.of(TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE)));

        shipManager.addCargo(6,7,new Cargo(Color.RED));
        shipManager.addCargo(6,7,new Cargo(Color.GREEN));
        shipManager.addCargo(6,8,new Cargo(Color.BLUE));
        shipManager.addCargo(6,8,new Cargo(Color.GREEN));

        cargoManager = new CargoManager();
    }

    @Test
    void correctAmountOfCargo() {
        // Discard 3 cargo
        // Expect 1 RED and 2 GREEN to be discarded, 1 BLUE should be left
        cargoManager.manageCargoDischarge(3, player);

        assertTrue(shipManager.getCargoPositon().get(Color.RED).isEmpty());
        assertTrue(shipManager.getCargoPositon().get(Color.GREEN).isEmpty());
        assertFalse(shipManager.getCargoPositon().get(Color.BLUE).isEmpty());
    }

    @Test
    void tooLittleCargo() {
        // Discard 5 cargo
        // Expect all cargo and 1 energy to be discarded
        cargoManager.manageCargoDischarge(5, player);

        assertTrue(shipManager.getCargoPositon().get(Color.RED).isEmpty());
        assertTrue(shipManager.getCargoPositon().get(Color.GREEN).isEmpty());
        assertTrue(shipManager.getCargoPositon().get(Color.BLUE).isEmpty());

        BatteryComponent battery = (BatteryComponent) player.getShipManager().getComponent(6,6).orElse(null);
        assertEquals(1, battery.getBatteryCapacity());
    }

    @Test
    void tooLittleCargoAndBattery() {
        // Discard 7 cargo
        // Expect all cargo and all energy to be discarded
        cargoManager.manageCargoDischarge(7, player);

        assertTrue(shipManager.getCargoPositon().get(Color.RED).isEmpty());
        assertTrue(shipManager.getCargoPositon().get(Color.GREEN).isEmpty());
        assertTrue(shipManager.getCargoPositon().get(Color.BLUE).isEmpty());

        BatteryComponent battery = (BatteryComponent) player.getShipManager().getComponent(6,6).orElse(null);
        assertEquals(0, battery.getBatteryCapacity());
    }

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