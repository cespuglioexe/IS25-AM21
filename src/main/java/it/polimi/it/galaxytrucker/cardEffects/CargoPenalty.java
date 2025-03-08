package it.polimi.it.galaxytrucker.cardEffects;

import it.polimi.it.galaxytrucker.managers.Player;
import it.polimi.it.galaxytrucker.utility.Cargo;

public interface CargoPenalty {
    void applyPenalty(int penalty, Player player);
}
