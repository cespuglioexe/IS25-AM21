package it.polimi.it.galaxytrucker.model.adventurecards.interfaces.attack;

import java.util.List;
import java.util.Map;

import it.polimi.it.galaxytrucker.model.managers.ShipManager;
import it.polimi.it.galaxytrucker.model.utility.Direction;

public class AimingSystem {
    private static final Map<Direction, AimingStrategy> strategies = Map.of(
        Direction.UP, new AimUp(),
        Direction.DOWN, new AimDown(),
        Direction.LEFT, new AimLeft(),
        Direction.RIGHT, new AimRight()
    );

    public static List<Integer> aimFrom(Direction direction, ShipManager ship) {
        return strategies.get(direction).aim(ship);
    }

    public static List<Integer> aimFrom(Direction direction, ShipManager ship, int coord) {
        return strategies.get(direction).aimWithFixedCoord(ship, coord);
    }
}
