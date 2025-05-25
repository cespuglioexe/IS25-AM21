package it.polimi.it.galaxytrucker.model.adventurecards.interfaces.attack;

import it.polimi.it.galaxytrucker.model.utility.Direction;
import it.polimi.it.galaxytrucker.model.utility.ProjectileType;

public class Projectile {
    private ProjectileType type;
    private Direction direction;

    public Projectile(ProjectileType type, Direction direction) {
        this.type = type;
        this.direction = direction;
    }

    public ProjectileType getSize() {
        return type;
    }

    public Direction getDirection() {
        return direction;
    }

    @Override
    public String toString() {
        return "Projectile{" +
                "type=" + type +
                ", direction=" + direction +
                '}';
    }
}
