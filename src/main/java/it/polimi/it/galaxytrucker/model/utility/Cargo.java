package it.polimi.it.galaxytrucker.model.utility;

import java.util.Map;

public class Cargo {
    private final Color color;
    private final boolean isSpecial;

    private static final Map<Color, Integer> CREDIT_VALUES = Map.of(
        Color.RED, 4,
        Color.YELLOW, 3,
        Color.BLUE, 2,
        Color.GREEN, 1
    );

    public Cargo(Color color) {
        this.color = color;
        isSpecial = (color == Color.RED);
    }

    public Color getColor() {
        return color;
    }

    public boolean isSpecial() {
        return isSpecial;
    }

    public int getCreditValue() {
        return CREDIT_VALUES.getOrDefault(CREDIT_VALUES, 1);
    }

    @Override
    public String toString() {
        return "Cargo{" +
                "color=" + color +
                '}';
    }
}
