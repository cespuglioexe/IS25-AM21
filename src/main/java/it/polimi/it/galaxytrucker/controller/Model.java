package it.polimi.it.galaxytrucker.controller;

public class Model {
    Integer state = 0;

    public boolean add (Integer num) {
        this.state += num;
        return true;
    }

    public Integer getState () {
        return this.state;
    }
}
