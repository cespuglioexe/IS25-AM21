package it.polimi.it.galaxytrucker.cardEffects;

import it.polimi.it.galaxytrucker.utility.Cargo;

public interface CargoPenalty extends Penalty<Cargo> {
    @Override
    void applyPenalty(Cargo penalty);
}
