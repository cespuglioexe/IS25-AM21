package it.polimi.it.galaxytrucker.componenttiles;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ComponentTileTest {

    @Test
    void testRotate() {
        StructuralModule component = new StructuralModule(TileEdge.SMOOTH, TileEdge.SINGLE, TileEdge.DOUBLE, TileEdge.UNIVERSAL);

        component.rotate();
        assertEquals(
                component.getRotation(), 1
        );
    }

    @Test
    void testGetRotation() {
    }

    @Test
    void testGetTileEdges() {

    }
}