package it.polimi.it.galaxytrucker.model.cardEffects;

import it.polimi.it.galaxytrucker.model.managers.Player;

public interface CargoPenalty {
    void applyPenalty(int penalty, Player player);
}
