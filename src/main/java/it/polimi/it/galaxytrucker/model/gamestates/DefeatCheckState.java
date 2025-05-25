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

        if (gameManager.getActivePlayers().isEmpty()) {
            changeState(fsm, new GameEndState());
            return;
        }

        checkForDefeat(fsm);

        if (wasLastPlayedCardAttack()) {
            changeState(fsm, new AttackAftermathCheckState());
        } else {
            changeState(fsm, new GameTurnStartState());
        }
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
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'exit'");
    }
    
}
