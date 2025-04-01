package it.polimi.it.galaxytrucker.aventurecard.interfaces;

import java.util.List;
import java.util.Set;

import it.polimi.it.galaxytrucker.managers.Player;

public interface Participation<T> {
    public void participate(Player player, int choice);
    public void decline(Player player);
    public List<Set<T>> getChoices();
}
