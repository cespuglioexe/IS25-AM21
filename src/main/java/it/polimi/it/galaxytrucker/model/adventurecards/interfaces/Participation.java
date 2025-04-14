package it.polimi.it.galaxytrucker.model.adventurecards.interfaces;

import java.util.HashMap;
import java.util.List;

import it.polimi.it.galaxytrucker.model.managers.Player;

public interface Participation<T> {
    public void participate(Player player, int choice);
    public void decline(Player player);
    public List<List<T>> getChoices();
    public HashMap<Integer, Player> getTakenChoices();
}
