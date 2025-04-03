package it.polimi.it.galaxytrucker.controller;

import java.awt.*;

public class Controller {
    private final Model model;

    public Controller() {
        this.model = new Model();
    }

    public boolean add (Integer number) {
        synchronized (this.model) {
            return this.model.add(number);
        }
    }

    public Integer getCurrent () {
        synchronized (this.model) {
            return this.model.getState();
        }
    }

    public boolean reset() {
        synchronized (this.model) {
            if (this.model.getState() == 0) {
                return false;
            }
            else {
                this.model.add(-this.model.getState());
                return true;
            }
        }
    }
}
