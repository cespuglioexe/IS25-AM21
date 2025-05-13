package it.polimi.it.galaxytrucker.exceptions;

public class GameFullException extends RuntimeException {
    public GameFullException(String message) {
        super(message);
    }
}
