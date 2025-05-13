package it.polimi.it.galaxytrucker.exceptions;

public class InvalidFunctionCallInState extends RuntimeException {
    public InvalidFunctionCallInState(String message) {
        super(message);
    }
}
