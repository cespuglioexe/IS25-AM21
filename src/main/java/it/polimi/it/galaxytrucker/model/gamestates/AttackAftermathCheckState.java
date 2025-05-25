package it.polimi.it.galaxytrucker.model.gamestates;

import java.util.List;
import java.util.stream.Collectors;

import it.polimi.it.galaxytrucker.model.design.statePattern.StateMachine;
import it.polimi.it.galaxytrucker.model.managers.GameManager;
import it.polimi.it.galaxytrucker.model.managers.Player;

public class AttackAftermathCheckState extends GameState {

    @Override
    public void enter(StateMachine fsm) {
        GameManager game = (GameManager) fsm;

        List<Player> playersWithIllegalShips = findPlayersWithIllegalShips(game.getActivePlayers());

        if (playersWithIllegalShips.isEmpty()) {
            fsm.changeState(new GameTurnStartState());
        } else {
            System.out.println("Players whit illegal ships: " + playersWithIllegalShips.stream().map(Player::getPlayerName).toList());
            fsm.changeState(new AttackAftermathFixingState(playersWithIllegalShips));
        }
    }
    public List<Player> findPlayersWithIllegalShips(List<Player> players) {
        return players.stream()
            .filter(player -> !player.getShipManager().isShipLegal())
            .collect(Collectors.toList());
    }

    @Override
    public void update(StateMachine fsm) {
        
    }

    @Override
    public void exit(StateMachine fsm) {
        
    }
    
}
