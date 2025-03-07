package it.polimi.it.galaxytrucker.cardEffects;

public interface CreditReward extends Reward<Integer>{
    @Override
    void giveReward(Integer reward);
}
