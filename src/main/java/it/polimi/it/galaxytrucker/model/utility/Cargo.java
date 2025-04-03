package it.polimi.it.galaxytrucker.model.utility;

public class Cargo {
    private final Color color;
    private final boolean isSpecial;

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
}
