package it.polimi.it.galaxytrucker.model.utility;

public enum Direction {
    UP, RIGHT, DOWN, LEFT;

    public static Direction fromInt(int rotation) {
        return values()[rotation % 4];
    }

    public Direction reverse() {
        return values()[(this.ordinal() + 2) % 4];
    }
}
