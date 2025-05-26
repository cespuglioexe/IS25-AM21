package it.polimi.it.galaxytrucker.model.gamestates;

import java.util.HashMap;
import java.util.List;
import java.util.function.Predicate;

import it.polimi.it.galaxytrucker.model.adventurecards.interfaces.AdventureCard;
import it.polimi.it.galaxytrucker.model.adventurecards.interfaces.attack.Attack;
import it.polimi.it.galaxytrucker.model.design.statePattern.StateMachine;
import it.polimi.it.galaxytrucker.model.managers.FlightBoard;
import it.polimi.it.galaxytrucker.model.managers.GameManager;
import it.polimi.it.galaxytrucker.model.managers.Player;
import it.polimi.it.galaxytrucker.model.managers.ShipManager;

public class DefeatCheckState extends GameState {
    GameManager gameManager;
    Player playerToCheck;
    List<Predicate<Player>> defeatConditions = List.of(
        player -> hasNoHumans(player),
        player -> hasBeenLapped(gameManager.getFlightBoard(), player)
    );

    @Override
    public void enter(StateMachine fsm) {
        gameManager = (GameManager) fsm;

        checkForDefeat(fsm);

        if (hasOneOrNoPlayersRemaining()) {
            changeState(fsm, new GameEndState());
        } else if (wasLastPlayedCardAttack()) {
            changeState(fsm, new AttackAftermathCheckState());
        } else {
            changeState(fsm, new GameTurnStartState());
        }
    }
    private boolean hasOneOrNoPlayersRemaining() {
        if (gameManager.getActivePlayers().size() <= 1) {
            return true;
        }
        return false;
    }
    private void checkForDefeat(StateMachine fsm) {
        for (Player player : gameManager.getActivePlayers()) {
            playerToCheck = player;
            update(fsm);
        }
    }
    private boolean wasLastPlayedCardAttack() {
        AdventureCard lastPlayedCard = gameManager.getAdventureDeck().getLastDrawnCard();

        if (lastPlayedCard instanceof Attack) {
            return true;
        }
        return false;
    }

    @Override
    public void update(StateMachine fsm) {
        for (Predicate<Player> condition : defeatConditions) {
            if (condition.test(playerToCheck)) {
                gameManager.defeat(playerToCheck);
                return;
            }
        }
    }
    private boolean hasNoHumans(Player player) {
        ShipManager ship = player.getShipManager();

        if (ship.countHumans() <= 0) {
            return true;
        }
        return false;
    }
    private boolean hasBeenLapped(FlightBoard flightBoard, Player player) {
        List<Player> playersInFlightOrder = flightBoard.getPlayerOrder();
        HashMap<Player, Integer> completedLaps = flightBoard.getCompletedLaps();

        return hasAnyPlayerTwoLapsAhead(player, completedLaps) || 
            hasPlayerAheadWithOneMoreLap(player, playersInFlightOrder, completedLaps);
    }
    private boolean hasAnyPlayerTwoLapsAhead(Player player, HashMap<Player, Integer> completedLaps) {
        int playerCompletedLaps = completedLaps.get(player);

        for (Player otherPlayer : completedLaps.keySet()) {
            int otherPlayerLaps = completedLaps.get(otherPlayer);

            if (otherPlayerLaps >= playerCompletedLaps + 2) {
                return true;
            }
        }
        return false;
    }
    private boolean hasPlayerAheadWithOneMoreLap(Player player, List<Player> playersInFlightOrder, HashMap<Player, Integer> completedLaps) {
        int playerPosition = playersInFlightOrder.indexOf(player);
        int playerCompletedLaps = completedLaps.get(player);

        for (int i = 0; i < playerPosition; i++) {
            Player playerAhead = playersInFlightOrder.get(i);

            if (completedLaps.get(playerAhead) > playerCompletedLaps) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void exit(StateMachine fsm) {
        
    }
    
}
