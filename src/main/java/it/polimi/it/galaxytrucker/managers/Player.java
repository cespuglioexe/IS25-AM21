package it.polimi.it.galaxytrucker.managers;

import it.polimi.it.galaxytrucker.utility.Color;

public class Player {
    private int PlayerID;
    private String PlayerName;
    private int credits;
    Color color;
    //ShipManager shipManager;

    public Player(int playerID, String playerName, int credits, Color color/*, ShipManager shipManager*/) {
        this.PlayerID = playerID;
        this.PlayerName = playerName;
        this.credits = credits;
        this.color = color;
        //this.shipManager = shipManager;
    }

}
