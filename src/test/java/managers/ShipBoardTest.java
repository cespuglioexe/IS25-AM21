package managers;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import it.polimi.it.galaxytrucker.componenttiles.ComponentTile;
import it.polimi.it.galaxytrucker.componenttiles.SingleCannon;
import it.polimi.it.galaxytrucker.componenttiles.SingleEngine;
import it.polimi.it.galaxytrucker.componenttiles.StructuralModule;
import it.polimi.it.galaxytrucker.componenttiles.TileEdge;
import it.polimi.it.galaxytrucker.managers.ShipBoard;
import it.polimi.it.galaxytrucker.managers.exceptions.IllegalComponentPositionException;

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
    void getAllComponentsPositionOfTypeTest() throws IllegalComponentPositionException {
        ShipBoard ship = new ShipBoard();
        ComponentTile cannon1 = new SingleCannon(TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE);
        ComponentTile cannon2 = new SingleCannon(TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE);
        ComponentTile engine = new SingleEngine(TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE);
        ComponentTile structuralModule = new StructuralModule(TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE);
        
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
        ComponentTile cannon = new SingleCannon(TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE);
        List<Integer> position = List.of(0,2);

        ship.addComponentTile(position.get(0), position.get(1), cannon);

        assertTrue(ship.getComponent(position.get(0), position.get(1)).isPresent());
        assertEquals(ship.getComponent(position.get(0), position.get(1)).get(), cannon);

        assertTrue(ship.getAllComponentsPositionOfType(cannon.getClass()).contains(position));
    }

    @Test
    void IllegalAddPositionTest() throws IllegalComponentPositionException {
        ShipBoard ship = new ShipBoard();
        ComponentTile cannon = new SingleCannon(TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE);
        List<Integer> position = List.of(0,2);

        ship.addComponentTile(position.get(0), position.get(1), cannon);

        assertThrows(IllegalComponentPositionException.class, () -> ship.addComponentTile(position.get(0), position.get(1), cannon));
    }

    @Test
    void removeComponentTileTest() throws IllegalComponentPositionException {
        ShipBoard ship = new ShipBoard();
        ComponentTile cannon = new SingleCannon(TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE);
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
    void illegalRemovePositionTest() {
        ShipBoard ship = new ShipBoard();
        List<Integer> position = List.of(0,2);

        assertThrows(IllegalComponentPositionException.class, () -> ship.removeComponentTile(position.get(0), position.get(1)));
    }

    @Test
    void getAllNeighbourComponentsTest() throws IllegalComponentPositionException {
        ShipBoard ship = new ShipBoard();
        ComponentTile cannon = new SingleCannon(TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE);
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
        ComponentTile cannon = new SingleCannon(TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE);
        List<Integer> position = List.of(2,2);

        //component
        ship.addComponentTile(position.get(0), position.get(1), cannon);
        //right component is empty
        //up component
        ship.addComponentTile(position.get(0) - 1, position.get(1), cannon);
        //left component
        ship.addComponentTile(position.get(0), position.get(1) - 1, cannon);
        //down component
        ship.addComponentTile(position.get(0) + 1, position.get(1), cannon);

        List<Optional<ComponentTile>> result = ship.getNeighbourComponents(position.get(0), position.get(1));

        assertIterableEquals(List.of(Optional.<ComponentTile>empty(), Optional.of(cannon), Optional.of(cannon), Optional.of(cannon)), result);
    }

    @Test
    void getNeighbourComponentsWithAllBlanksTest() throws IllegalComponentPositionException {
        ShipBoard ship = new ShipBoard();
        ComponentTile cannon = new SingleCannon(TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE, TileEdge.SINGLE);
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
}
