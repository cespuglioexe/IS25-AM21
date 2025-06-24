package it.polimi.it.galaxytrucker.model.utility;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

public class Coordinates {
    public int column;
    public int row;

    @JsonCreator
    public Coordinates(
            @JsonProperty("column") int column,
            @JsonProperty("row") int row) {
        this.column = column;
        this.row = row;
    }

    public int getColumn() {
        return column;
    }

    public int getRow() {
        return row;
    }
}