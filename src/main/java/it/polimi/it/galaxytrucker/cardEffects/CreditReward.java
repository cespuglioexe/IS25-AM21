package it.polimi.it.galaxytrucker.cardEffects;

import it.polimi.it.galaxytrucker.managers.Player;
import it.polimi.it.galaxytrucker.utility.Cargo;

import java.util.Set;

public interface CreditReward{
    void giveCreditReward(int reward, Player player);
}
