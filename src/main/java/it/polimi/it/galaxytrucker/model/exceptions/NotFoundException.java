package it.polimi.it.galaxytrucker.model.exceptions;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }   
}
