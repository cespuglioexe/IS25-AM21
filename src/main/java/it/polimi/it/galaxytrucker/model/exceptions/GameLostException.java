package it.polimi.it.galaxytrucker.model.exceptions;

public class GameLostException extends RuntimeException {
    public GameLostException(String message) {
        super(message);
    }
}
