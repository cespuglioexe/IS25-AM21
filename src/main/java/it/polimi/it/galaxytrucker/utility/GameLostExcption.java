package it.polimi.it.galaxytrucker.utility;

public class GameLostExcption extends RuntimeException {
    public GameLostExcption(String message) {
        super(message);
    }
}
