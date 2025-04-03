package it.polimi.it.galaxytrucker.model.cardEffects;

import java.util.Set;
import java.util.List;

import it.polimi.it.galaxytrucker.model.managers.Player;

public interface Participation<T> {
    public void partecipate(Player player, int choice);
    public void decline(Player player);
    public List<Integer> getSlots();
    public Set<T> getRewards();
}