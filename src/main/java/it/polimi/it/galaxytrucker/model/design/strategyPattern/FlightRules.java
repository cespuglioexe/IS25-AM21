package it.polimi.it.galaxytrucker.model.design.strategyPattern;

import java.util.List;

import it.polimi.it.galaxytrucker.model.managers.Player;

public interface FlightRules extends Strategy {
    public List<Player> getPlayerOrder();
    public void movePlayerBackwards(int progress, Player player);
    public void movePlayerForward(int progress, Player player);
}
