package it.polimi.it.galaxytrucker.gameStates;

import java.util.List;

public interface EndGame {
    int countPlayerCredits(int PlayerID);
    List<Integer> makeLeaderboard();
}
