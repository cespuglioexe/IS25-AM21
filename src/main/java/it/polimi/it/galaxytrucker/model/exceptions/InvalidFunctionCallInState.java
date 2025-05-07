package it.polimi.it.galaxytrucker.model.exceptions;

public class InvalidFunctionCallInState extends RuntimeException {
    public InvalidFunctionCallInState(String message) {
        super(message);
    }
}
