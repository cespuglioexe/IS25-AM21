package it.polimi.it.galaxytrucker.controller;

import java.io.Serial;
import java.io.Serializable;

public record GenericGameData(int level, int playerNum, int activePlayers) implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
}