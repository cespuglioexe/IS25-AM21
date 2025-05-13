package it.polimi.it.galaxytrucker.exceptions;

public class GameLostException extends RuntimeException {
    public GameLostException(String message) {
        super(message);
    }
}
