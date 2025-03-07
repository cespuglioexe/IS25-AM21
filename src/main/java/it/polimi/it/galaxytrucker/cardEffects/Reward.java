package it.polimi.it.galaxytrucker.cardEffects;

import it.polimi.it.galaxytrucker.managers.Player;
import it.polimi.it.galaxytrucker.utility.Cargo;

import java.util.Set;

public interface Reward<T> {

    void giveReward(Set<T> reward, Player player);
}
