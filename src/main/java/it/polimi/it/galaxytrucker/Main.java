package it.polimi.it.galaxytrucker;

import it.polimi.it.galaxytrucker.gameStates.TestingClass;

public class Main {
    public static void main(String[] args) {
        int playerID = 10;
        TestingClass testingClass = new TestingClass();
        testingClass.addPlayer(playerID);
    }
}
