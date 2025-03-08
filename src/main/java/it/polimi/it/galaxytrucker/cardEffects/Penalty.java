package it.polimi.it.galaxytrucker.cardEffects;

import it.polimi.it.galaxytrucker.managers.Player;

public interface Penalty<T> {
    void applyPenalty(T penalty, Player player);
}
