package it.polimi.it.galaxytrucker.model.aventurecard.interfaces;

import it.polimi.it.galaxytrucker.model.managers.Player;

public interface CargoPenalty extends Penalty {
    public void applyCargoPenalty(Player player);
}
