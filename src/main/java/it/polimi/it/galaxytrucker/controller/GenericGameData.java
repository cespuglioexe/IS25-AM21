package it.polimi.it.galaxytrucker.controller;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

public record GenericGameData(int level, int playerNum, int activePlayers, UUID gameId) implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
}