package it.polimi.it.galaxytrucker.model.utility;

import java.util.Random;

public class Dice {
    private static final int MIN = 1;
    private static final int MAX = 6;
    private static final Random random = new Random();

    public static int roll() {
        return random.nextInt(MAX - MIN + 1) + MIN;
    }
}