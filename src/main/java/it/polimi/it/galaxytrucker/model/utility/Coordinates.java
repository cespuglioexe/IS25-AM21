package it.polimi.it.galaxytrucker.model.utility;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class Coordinates {
    public double column;
    public double row;

    @JsonCreator
    public Coordinates(
            @JsonProperty("column") double column,
            @JsonProperty("row") double row) {
        this.column = column;
        this.row = row;
    }

    public double getColumn() {
        return column;
    }

    public double getRow() {
        return row;
    }
}