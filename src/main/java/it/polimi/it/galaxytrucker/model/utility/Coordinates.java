package it.polimi.it.galaxytrucker.model.utility;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

public class Coordinates implements Serializable {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coordinates that = (Coordinates) o;
        return column == that.column && row == that.row;
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(column, row);
    }
}