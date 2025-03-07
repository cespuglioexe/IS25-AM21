package it.polimi.it.galaxytrucker.cardEffects;

import it.polimi.it.galaxytrucker.utility.Cargo;

public interface CargoReward extends Reward<Cargo> {

    @Override
    void giveReward(Cargo reward);
}
