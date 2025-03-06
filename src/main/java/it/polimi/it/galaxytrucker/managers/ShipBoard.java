package it.polimi.it.galaxytrucker.managers;

import it.polimi.it.galaxytrucker.componenttiles.ComponentTile;
import it.polimi.it.galaxytrucker.managers.exceptions.IllegalComponentPositionException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ShipBoard {
    private List<List<Optional<ComponentTile>>> tileMatrix;
    private Map<ComponentTile, List<Integer>> componentTilesPosition;

    public ShipBoard() {
        final int COLUMNS = 7;
        final int ROWS = 5;

        this.tileMatrix = IntStream.range(0, ROWS)
            .mapToObj(_ -> IntStream.range(0, COLUMNS)
                .mapToObj(_ -> Optional.<ComponentTile>empty())
                .collect(Collectors.toList())
            )
            .collect(Collectors.toList());

        this.componentTilesPosition = new HashMap<ComponentTile, List<Integer>>();
    }

    public List<List<Optional<ComponentTile>>> getBoard() {
        return this.tileMatrix;
    }

    public void addComponentTile (int row, int column, ComponentTile component) throws IllegalComponentPositionException {
        if (this.tileMatrix.get(row).get(column).isPresent()) throw new IllegalComponentPositionException("You have already placed a component here");

        this.tileMatrix.get(row).set(column, Optional.of(component));
        this.componentTilesPosition.put(component, new ArrayList<>(List.of(row, column)));
    }
}
