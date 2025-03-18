package it.polimi.it.galaxytrucker.componenttiles;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ComponentTileTest {

    @Test
    void testRotateComponentTile() {
        StructuralModule component = new StructuralModule(List.of(TileEdge.SMOOTH, TileEdge.SINGLE, TileEdge.DOUBLE, TileEdge.UNIVERSAL));

        /* ===== TEST 1 ==== */

        /*
         *  Single rotation, expect the last element of {@code component.tileEdges} to be
         *  in the first position of {@code component.getTileEdges()}
         */
        List<TileEdge> list1 = new ArrayList<>();
        list1.add(TileEdge.UNIVERSAL);
        list1.add(TileEdge.SMOOTH);
        list1.add(TileEdge.SINGLE);
        list1.add(TileEdge.DOUBLE);

        component.rotate();

        assertEquals(list1, component.getTileEdges());
        assertEquals(1, component.getRotation());

        /* ===== TEST 2 ==== */

        /*
         *  Single rotation, expect the last element of {@code component.tileEdges} to be
         *  in the second position of {@code component.getTileEdges()} and the second to
         *  last to be in the first position
         */
        List<TileEdge> list2 = new ArrayList<>();
        list2.add(TileEdge.DOUBLE);
        list2.add(TileEdge.UNIVERSAL);
        list2.add(TileEdge.SMOOTH);
        list2.add(TileEdge.SINGLE);

        component.rotate();

        assertEquals(list2, component.getTileEdges());
        assertEquals(2, component.getRotation());

        /* ===== TEST 3 ==== */

        /*
         *  Single rotation, expect the last element of {@code component.tileEdges} to be
         *  in the second position of {@code component.getTileEdges()} and the second to
         *  last to be in the first position
         */
        List<TileEdge> list3 = new ArrayList<>();
        list3.add(TileEdge.SINGLE);
        list3.add(TileEdge.DOUBLE);
        list3.add(TileEdge.UNIVERSAL);
        list3.add(TileEdge.SMOOTH);

        component.rotate();

        assertEquals(list3, component.getTileEdges());
        assertEquals(3, component.getRotation());

        /* ===== TEST 4 ==== */

        /*
         *  Final rotation, expect {@code component.getTileEdges()} to be the same as when it started
         */
        List<TileEdge> list4 = new ArrayList<>();
        list4.add(TileEdge.SMOOTH);
        list4.add(TileEdge.SINGLE);
        list4.add(TileEdge.DOUBLE);
        list4.add(TileEdge.UNIVERSAL);

        component.rotate();

        assertEquals(list4, component.getTileEdges());
        assertEquals(0, component.getRotation());
    }
}